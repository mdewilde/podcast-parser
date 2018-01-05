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
package be.ceau.podcastparser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.SkippableElement;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.namespace.callback.NamespaceCallbackHandler;
import be.ceau.podcastparser.util.RequiredState;

public class PodParseContext {

	private final String rootNamespace;
	private final XMLStreamReader reader;
	private final List<NamespaceCallbackHandler> namespaceCallbackHandlers;
	private final Feed feed;
	private final Set<SkippableElement> skippableElements;

	public PodParseContext(String rootNamespace, XMLStreamReader reader) {
		this(rootNamespace, reader, Collections.emptyList(), Collections.emptySet());
	}

	public PodParseContext(String rootNamespace, XMLStreamReader reader, List<NamespaceCallbackHandler> namespaceCallbackHandlers) {
		this(rootNamespace, reader, namespaceCallbackHandlers, Collections.emptySet());
	}

	public PodParseContext(String rootNamespace, XMLStreamReader reader, List<NamespaceCallbackHandler> namespaceCallbackHandlers, Collection<SkippableElement> skippableElements) {
		Objects.requireNonNull(rootNamespace);
		Objects.requireNonNull(reader);
		Objects.requireNonNull(namespaceCallbackHandlers);
		Objects.requireNonNull(skippableElements);
		this.rootNamespace = rootNamespace;
		this.reader = reader;
		this.namespaceCallbackHandlers = namespaceCallbackHandlers;
		this.feed = new Feed();
		this.skippableElements = Collections.unmodifiableSet(new HashSet<>(skippableElements));
	}

	public String getRootNamespace() {
		return rootNamespace;
	}

	public XMLStreamReader getReader() {
		return reader;
	}

	public Feed getFeed() {
		return feed;
	}

	public void beforeProcess() throws XMLStreamException {
		RequiredState state = RequiredState.from(reader);
		namespaceCallbackHandlers.forEach(h -> h.beforeProcess(rootNamespace, feed, reader));
		state.validate(reader);
	}

	public void beforeProcess(Item item) throws XMLStreamException {
		RequiredState state = RequiredState.from(reader);
		namespaceCallbackHandlers.forEach(h -> h.beforeProcess(rootNamespace, item, reader));
		state.validate(reader);
	}

	public void registerUnknownNamespace(ParseLevel level) throws XMLStreamException {
		RequiredState state = RequiredState.from(reader);
		namespaceCallbackHandlers.forEach(h -> h.registerUnknownNamespace(rootNamespace, reader, level));
		state.validate(reader);
	}

	public void registerUnhandledElement(ParseLevel level) throws XMLStreamException {
		RequiredState state = RequiredState.from(reader);
		namespaceCallbackHandlers.forEach(h -> h.registerUnhandledElement(rootNamespace, reader, level));
		state.validate(reader);
	}

	/**
	 * Retrieves the element text if currently at a start element.
	 * 
	 * @return a {@link String} with element text content, or {@code null}
	 * @throws XMLStreamException
	 */
	public String getElementText() throws XMLStreamException {
		if (reader.isStartElement() && !reader.isStandalone()) {
			final String localName = reader.getLocalName();
			StringBuilder sb = new StringBuilder();
			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamReader.END_ELEMENT : 
					if (localName.equals(reader.getLocalName())) {
						return sb.toString();
					}
					break;
				case XMLStreamReader.CHARACTERS :
					sb.append(reader.getText());
					break;
				case XMLStreamReader.CDATA :
					sb.append(reader.getText());
					break;
				}
			}
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
	 * @param localName
	 *            {@link String} attribute name
	 * @return attribute value parsed as {@link Integer}, or {@code null}
	 */
	public Integer getAttributeAsInteger(String localName) {
		String value = getAttribute(localName);
		if (value != null) {
			try {
				return Integer.valueOf(value);
			} catch (NumberFormatException e) {
			}
		}
		return null;
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
			while (reader.hasNext()) {
				if (reader.next() == XMLStreamConstants.END_ELEMENT && localname.equals(reader.getLocalName())) {
					return;
				}
			}
		}
	}

	/**
	 * Log, non-repeatably, the current element, including its internal hierarchy
	 * 
	 * @throws XMLStreamException
	 */
	public void log() throws XMLStreamException {
		if (reader.isStartElement()) {
			LoggerFactory.getLogger(Namespace.class).info(serialize());
		}
	}
	
	/**
	 * Serialize, non-repeatably, the current element, including its internal hierarchy
	 * 
	 * @throws XMLStreamException
	 */
	public String serialize() throws XMLStreamException {
		if (reader.isStartElement()) {
			final String localName = reader.getLocalName();
			StringBuilder xml = new StringBuilder();
			xml.append(asStartElementString(reader));

			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamConstants.COMMENT:
					xml.append("<!-- ");
					xml.append(reader.getText().trim());
					xml.append(" -->");
					break;
				case XMLStreamConstants.CHARACTERS:
					xml.append(reader.getText().trim());
					break;
				case XMLStreamConstants.START_ELEMENT:
					xml.append(System.lineSeparator());
					xml.append(asStartElementString(reader));
					break;
				case XMLStreamConstants.END_ELEMENT:
					xml.append(asEndElementString(reader));
					if (localName.equals(reader.getLocalName())) {
						return xml.toString();
					}
					break;
				}
			}
		}
		return "";
	}
	
	private String asStartElementString(XMLStreamReader reader) {
		return new StringBuilder()
				.append("<")
				.append(Strings.isBlank(reader.getName().getPrefix()) ? getRootNamespace() : reader.getName().getPrefix())
				.append(":")
				.append(reader.getLocalName())
				.append(asAttributes(reader))
				.append(reader.isStandalone() ? "/" : "")
				.append(">")
				.toString();
	}
	
	private String asAttributes(XMLStreamReader reader) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			sb.append(" ").append(reader.getAttributeLocalName(i)).append("=\"").append(reader.getAttributeValue(i)).append("\"");
		}
		return sb.toString();
	}
	
	private String asEndElementString(XMLStreamReader reader) {
		return new StringBuilder()
				.append("</")
				.append(Strings.isBlank(reader.getName().getPrefix()) ? getRootNamespace() : reader.getName().getPrefix())
				.append(":")
				.append(reader.getLocalName())
				.append(">")
				.toString();
	}

	
}
