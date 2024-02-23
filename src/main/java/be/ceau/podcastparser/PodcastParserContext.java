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
package be.ceau.podcastparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.filter.ElementFilter;
import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.namespace.callback.NamespaceCallbackHandler;
import be.ceau.podcastparser.util.RequiredState;
import be.ceau.podcastparser.util.Strings;

/**
 * Logic and state for the parse process of a single podcast XML feed
 */
public class PodcastParserContext {

	private final String rootNamespace;
	private final XMLStreamReader reader;
	private final List<NamespaceCallbackHandler> namespaceCallbackHandlers;
	private final Set<ElementFilter> elementFilters;
	private final Feed feed;

	/**
	 * Constructor
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace, not {@code null}
	 * @param reader
	 *            {@link XMLStreamReader} instance over the XML input, not {@code null}
	 */
	public PodcastParserContext(String rootNamespace, XMLStreamReader reader) {
		this(rootNamespace, reader, Collections.emptyList(), Collections.emptySet());
	}

	/**
	 * Constructor
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace, not {@code null}
	 * @param reader
	 *            {@link XMLStreamReader} instance over the XML input, not {@code null}
	 * @param callbacks
	 *            {@link Collection} of {@link NamespaceCallbackHandler} instances, can be {@code null}
	 */
	public PodcastParserContext(String rootNamespace, XMLStreamReader reader, Collection<NamespaceCallbackHandler> callbacks) {
		this(rootNamespace, reader, callbacks, Collections.emptySet());
	}

	/**
	 * Constructor
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace, not {@code null}
	 * @param reader
	 *            {@link XMLStreamReader} instance over the XML input, not {@code null}
	 * @param callbacks
	 *            {@link Collection} of {@link NamespaceCallbackHandler} instances, can be {@code null}
	 * @param filters
	 *            {@link Collection} of {@link ElementFilter} instances, can be {@code null}
	 */
	public PodcastParserContext(String rootNamespace, XMLStreamReader reader, Collection<NamespaceCallbackHandler> callbacks, Collection<ElementFilter> filters) {
		Objects.requireNonNull(rootNamespace);
		Objects.requireNonNull(reader);
		this.rootNamespace = rootNamespace;
		this.reader = reader;
		if (callbacks != null) {
			this.namespaceCallbackHandlers = Collections.unmodifiableList(new ArrayList<>(callbacks));
			this.namespaceCallbackHandlers.forEach(Objects::requireNonNull);
		} else {
			this.namespaceCallbackHandlers = Collections.emptyList();
		}
		if (filters != null) {
			this.elementFilters = Collections.unmodifiableSet(new LinkedHashSet<>(filters));
			this.elementFilters.forEach(Objects::requireNonNull);
		} else {
			this.elementFilters = Collections.emptySet();
		}
		this.feed = new Feed();
	}

	/**
	 * @return {@code XMLStreamReader}, never {@code null}
	 */
	public XMLStreamReader getReader() {
		return reader;
	}

	/**
	 * @return {@link Feed}, never {@code null}
	 */
	public Feed getFeed() {
		return feed;
	}

	/**
	 * Process callback before processing {@link Feed}
	 * 
	 * @throws XMLStreamException
	 *             if any
	 */
	public void beforeProcess() throws XMLStreamException {
		if (!namespaceCallbackHandlers.isEmpty()) {
			RequiredState state = RequiredState.from(reader);
			namespaceCallbackHandlers.forEach(h -> h.beforeProcess(rootNamespace, feed, reader));
			state.validate(reader);
		}
	}

	/**
	 * Process callback before processing {@link Item}
	 * 
	 * @param item
	 *            {@link Item} instance, not {@code null}
	 * @throws XMLStreamException
	 *             if any
	 */
	public void beforeProcess(Item item) throws XMLStreamException {
		if (!namespaceCallbackHandlers.isEmpty()) {
			RequiredState state = RequiredState.from(reader);
			namespaceCallbackHandlers.forEach(h -> h.beforeProcess(rootNamespace, item, reader));
			state.validate(reader);
		}
	}

	/**
	 * Process callback when encountering unknown namespace
	 * 
	 * @param level
	 *            {@link ParseLevel} of current parse process, not {@code null}
	 * @throws XMLStreamException
	 *             if any
	 */
	public void registerUnknownNamespace(ParseLevel level) throws XMLStreamException {
		if (!namespaceCallbackHandlers.isEmpty()) {
			RequiredState state = RequiredState.from(reader);
			namespaceCallbackHandlers.forEach(h -> h.registerUnknownNamespace(rootNamespace, reader, level));
			state.validate(reader);
		}
	}

	/**
	 * Process callback when encountering unknown element
	 * 
	 * @param level
	 *            {@link ParseLevel} of current parse process, not {@code null}
	 * @throws XMLStreamException
	 *             if any
	 */
	public void registerUnhandledElement(ParseLevel level) throws XMLStreamException {
		if (!namespaceCallbackHandlers.isEmpty()) {
			RequiredState state = RequiredState.from(reader);
			namespaceCallbackHandlers.forEach(h -> h.registerUnhandledElement(rootNamespace, reader, level));
			state.validate(reader);
		}
	}

	/**
	 * Retrieves the element text if currently at a start element.
	 * 
	 * @return a {@link String} with element text content, or {@code null}
	 * @throws XMLStreamException
	 *             if any
	 */
	public String getElementText() throws XMLStreamException {
		if (reader.isStartElement() && !reader.isStandalone()) {
			final String localName = reader.getLocalName();
			StringBuilder sb = new StringBuilder();
			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamReader.END_ELEMENT:
					if (localName.equals(reader.getLocalName())) {
						return sb.toString();
					}
					break;
				case XMLStreamReader.CHARACTERS:
					sb.append(reader.getText());
					break;
				case XMLStreamReader.CDATA:
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
	 *             if any
	 */
	public Integer getElementTextAsInteger() throws XMLStreamException {
		try {
			return Integer.valueOf(reader.getElementText().trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Extract and attempt parsing current element text as {@link BigDecimal}, catching any exception
	 * 
	 * @return {@link BigDecimal} or {@code null}
	 * @throws XMLStreamException
	 *             if any
	 */
	public BigDecimal getElementTextAsBigDecimal() throws XMLStreamException {
		try {
			return new BigDecimal(reader.getElementText().trim());
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
	 * @param localName
	 *            {@link String} attribute name
	 * @return attribute value parsed as {@link Long}, or {@code null}
	 */
	public Long getAttributeAsLong(String localName) {
		String value = getAttribute(localName);
		if (value != null) {
			try {
				return Long.valueOf(value);
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}

	/**
	 * Evaluate the current element against the {@link ElementFilter} instances in this
	 * {@link PodcastParserContext}.
	 * 
	 * @return {@code true} if the current element should be skipped
	 */
	public boolean isSkip() {
		for (ElementFilter filter : elementFilters) {
			if (filter.skip(reader.getNamespaceURI(), reader.getLocalName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If called after having processed a {@link XMLStreamConstants#START_ELEMENT} event, will skip
	 * until the end of the newly opened element is reached.
	 * 
	 * @throws XMLStreamException
	 *             if any
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
	 *             if any
	 */
	public void log() throws XMLStreamException {
		if (reader.isStartElement()) {
			LoggerFactory.getLogger(Namespace.class).info(serialize());
		}
	}

	/**
	 * Serialize, non-repeatably, the current element, including its internal hierarchy
	 * 
	 * @return {@link String}, not {@code null}
	 * @throws XMLStreamException
	 *             if any
	 */
	public String serialize() throws XMLStreamException {
		if (reader.isStartElement()) {
			final String localName = reader.getLocalName();
			StringBuilder xml = new StringBuilder();
			xml.append(asStartElementString(reader));

			boolean endElement = false;

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
					endElement = false;
					break;
				case XMLStreamConstants.END_ELEMENT:
					if (endElement) {
						xml.append(System.lineSeparator());
					}
					xml.append(asEndElementString(reader));
					if (localName.equals(reader.getLocalName())) {
						return xml.toString();
					}
					endElement = true;
					break;
				}
			}
		}
		return "";
	}

	private String asStartElementString(XMLStreamReader reader) {
		return new StringBuilder()
				.append("<")
				.append(Strings.isBlank(reader.getName().getPrefix()) ? rootNamespace : reader.getName().getPrefix())
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
				.append(Strings.isBlank(reader.getName().getPrefix()) ? rootNamespace : reader.getName().getPrefix())
				.append(":")
				.append(reader.getLocalName())
				.append(">")
				.toString();
	}

}
