/*
	Copyright 2017 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.SkippableElement;
import be.ceau.podcastparser.namespace.callback.NamespaceCallbackHandler;

public class PodParseContext {

	private final String rootNamespace;
	private final XMLStreamReader reader;
	private final NamespaceCallbackHandler namespaceCallbackHandler;
	private final Feed feed;
	private final Set<SkippableElement> skippableElements;

	public PodParseContext(String rootNamespace, XMLStreamReader reader, NamespaceCallbackHandler namespaceCallbackHandler) {
		this(rootNamespace, reader, namespaceCallbackHandler, Collections.emptySet());
	}

	public PodParseContext(String rootNamespace, XMLStreamReader reader, NamespaceCallbackHandler namespaceCallbackHandler, Collection<SkippableElement> skippableElements) {
		Objects.requireNonNull(rootNamespace);
		Objects.requireNonNull(reader);
		Objects.requireNonNull(namespaceCallbackHandler);
		Objects.requireNonNull(skippableElements);
		this.rootNamespace = rootNamespace;
		this.reader = reader;
		this.namespaceCallbackHandler = namespaceCallbackHandler;
		this.feed = new Feed();
		this.skippableElements = Collections.unmodifiableSet(new HashSet<>(skippableElements));
	}

	public XMLStreamReader getReader() {
		return reader;
	}

	public NamespaceCallbackHandler getNamespaceCallbackHandler() {
		return namespaceCallbackHandler;
	}

	public Feed getFeed() {
		return feed;
	}

	public void beforeProcess() {
		namespaceCallbackHandler.beforeProcess(rootNamespace, feed, reader);
	}

	public void beforeProcess(Item item) {
		namespaceCallbackHandler.beforeProcess(rootNamespace, item, reader);
	}

	public void registerUnknownNamespace(String level) {
		namespaceCallbackHandler.registerUnknownNamespace(reader, level);
	}

	/**
	 * Retrieves the element text if currently at a start element.
	 * 
	 * @return a {@link String} with element text content, or {@code null}
	 * @throws XMLStreamException
	 */
	public String getElementText() throws XMLStreamException {
		if (reader.isStartElement()) {
			return reader.getElementText();
		}
		return null;
	}

	/**
	 * Extract and attempt parsing current element text as {@link Integer}, catching any exception
	 * 
	 * @return {@link Integer} or {@code null}
	 * @throws XMLStreamException
	 */
	public Integer getElementTextAsInteger() throws XMLStreamException {
		try {
			return Integer.valueOf(reader.getElementText().trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * @param localName
	 *            {@link String} attribute name
	 * @return {@link String} attribute value, or {@code null}
	 */
	public String getAttribute(String localName) {
		String value = reader.getAttributeValue(null, localName);
		if (value != null) {
			value = value.trim();
		}
		return value;
	}

	/**
	 * If called after having processed a {@link XMLStreamConstants#START_ELEMENT} event, will skip
	 * until the end of the newly opened element is reached.
	 * 
	 * @throws XMLStreamException
	 */
	public void skip() throws XMLStreamException {
		if (reader.isStartElement()) {
			String localname = reader.getLocalName();
			while (getReader().hasNext()) {
				if (getReader().next() == XMLStreamConstants.END_ELEMENT && localname.equals(getReader().getLocalName())) {
					return;
				}
			}
		}
	}

}
