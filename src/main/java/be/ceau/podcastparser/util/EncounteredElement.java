/*
	
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
package be.ceau.podcastparser.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.ParseLevel;

/**
 * Encapsulate a full element for logging and debugging purposes.
 */
public class EncounteredElement {

	private final ParseLevel level;
	private final String localName;
	private final Set<String> attributes;
	private final String namespaceUri;

	public EncounteredElement(String rootNamespace, XMLStreamReader reader, ParseLevel level) {
		String uri = reader.getNamespaceURI();
		this.namespaceUri = Strings.isBlank(uri) ? rootNamespace : uri;
		this.localName = reader.getLocalName();
		this.attributes = Collections.unmodifiableSet(extractAttributes(reader));
		this.level = level;
	}

	public ParseLevel getLevel() {
		return level;
	}

	public String getLocalName() {
		return localName;
	}

	public Set<String> getAttributes() {
		return attributes;
	}

	public String getAttributesString() {
		return attributes.stream().collect(Collectors.joining(","));
	}

	public String getNamespaceUri() {
		return namespaceUri;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(level)
				.append("  |  ")
				.append(namespaceUri)
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
		if (reader.getEventType() == XMLStreamReader.START_ELEMENT || reader.getEventType() == XMLStreamReader.ATTRIBUTE) {
			Set<String> attributes = new HashSet<>();
			for (int i = 0; i < reader.getAttributeCount(); i++) {
				attributes.add(reader.getAttributeLocalName(i));
			}
			return attributes;
		}
		return Collections.emptySet();
	}

}