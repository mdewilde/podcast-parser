/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		https://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.podcastparser.namespace.callback;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.models.Item;

/**
 * <p>
 * {@link NamespaceCallbackHandler} implementation that counts every occurrence of any element in any namespace.
 * </p>
 * <p>
 * Add an instance to any number of {@link be.ceau.podcastparser.PodcastParser} instances. Generate a report using
 * {@link #toString()}.
 * </p>
 * <p>
 * This implementation is threadsafe.
 * </p>
 */
public class NamespaceCountingCallbackHandler implements NamespaceCallbackHandler {

	private final Map<EncounteredElement, LongAdder> counts = new ConcurrentHashMap<>();

	@Override
	public void beforeProcess(String rootNamespace, Feed feed, XMLStreamReader reader) {
		counts.computeIfAbsent(new EncounteredElement(rootNamespace, reader, "feed"), x -> new LongAdder()).increment();
	}

	@Override
	public void beforeProcess(String rootNamespace, Item item, XMLStreamReader reader) {
		counts.computeIfAbsent(new EncounteredElement(rootNamespace, reader, "item"), x -> new LongAdder()).increment();
	}

	@Override
	public void registerUnknownNamespace(XMLStreamReader reader, String level) {
		counts.computeIfAbsent(new EncounteredElement("", reader, level), x -> new LongAdder()).increment();
	}

	@Override
	public String toString() {
		return counts.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().sum()))
				.entrySet()
				.stream()
				.sorted(Map.Entry.<EncounteredElement, Long>comparingByValue().reversed())
				.map(e -> String.format("%10d", e.getValue()) + " \t--> " + e.getKey())
				.collect(Collectors.joining(System.lineSeparator()));
	}

	private static class EncounteredElement {

		private final String namespaceUri;
		private final String localName;
		private final Set<String> attributes;
		private final String level;

		public EncounteredElement(String rootNamespace, XMLStreamReader reader, String level) {
			String uri = reader.getNamespaceURI();
			this.namespaceUri = StringUtils.isBlank(uri) ? rootNamespace : uri;
			this.localName = reader.getLocalName();
			this.attributes = Collections.unmodifiableSet(extractAttributes(reader));
			this.level = level;
		}

		@Override
		public String toString() {
			return new StringBuilder()
					.append(namespaceUri)
					.append(" level=").append(level)
					.append(" localName=").append(localName)
					.append(" attributes=").append(attributes)
					.append("]")
					.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
			result = prime * result + ((level == null) ? 0 : level.hashCode());
			result = prime * result + ((localName == null) ? 0 : localName.hashCode());
			result = prime * result + ((namespaceUri == null) ? 0 : namespaceUri.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EncounteredElement other = (EncounteredElement) obj;
			if (attributes == null) {
				if (other.attributes != null)
					return false;
			} else if (!attributes.equals(other.attributes))
				return false;
			if (level == null) {
				if (other.level != null)
					return false;
			} else if (!level.equals(other.level))
				return false;
			if (localName == null) {
				if (other.localName != null)
					return false;
			} else if (!localName.equals(other.localName))
				return false;
			if (namespaceUri == null) {
				if (other.namespaceUri != null)
					return false;
			} else if (!namespaceUri.equals(other.namespaceUri))
				return false;
			return true;
		}

		private static Set<String> extractAttributes(XMLStreamReader reader) {
			Set<String> attributes = new HashSet<>();
			for (int i = 0; i < reader.getAttributeCount(); i++) {
				attributes.add(reader.getAttributeLocalName(i));
			}
			return attributes;
		}

	}

}
