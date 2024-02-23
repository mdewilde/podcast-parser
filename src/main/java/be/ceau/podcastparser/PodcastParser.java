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

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.exceptions.InvalidFeedFormatException;
import be.ceau.podcastparser.exceptions.NotPodcastFeedException;
import be.ceau.podcastparser.exceptions.PodcastParserException;
import be.ceau.podcastparser.filter.ElementFilter;
import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.namespace.callback.NamespaceCallbackHandler;
import be.ceau.podcastparser.namespace.root.impl.Atom;
import be.ceau.podcastparser.namespace.root.impl.RSS;
import be.ceau.podcastparser.util.Strings;

/**
 * Parser class for converting podcast XML feeds into {@link Feed} objects.
 */
public class PodcastParser {

	private final XMLInputFactory factory;
	private final List<NamespaceCallbackHandler> namespaceCallbackHandlers;
	private final Set<ElementFilter> elementFilters;

	/**
	 * No-arg constructor
	 */
	public PodcastParser() {
		this(Collections.emptySet(), Collections.emptySet());
	}

	/**
	 * Constructor with {@link NamespaceCallbackHandler}. Use this constructor if
	 * you want to add custom callbacks to the parsing process.
	 *
	 * @param callbackHandler a {@link NamespaceCallbackHandler} implementation
	 * @throws NullPointerException if argument is {@code null}
	 */
	public PodcastParser(NamespaceCallbackHandler callbackHandler) {
		this(Collections.singleton(callbackHandler), Collections.emptySet());
	}

	/**
	 * Constructor with {@link ElementFilter}. Use this constructor if you want to
	 * filter elements from the parsing process.
	 *
	 * @param elementFilter a {@link ElementFilter} implementation
	 * @throws NullPointerException if argument is {@code null}
	 */
	public PodcastParser(ElementFilter elementFilter) {
		this(Collections.emptySet(), Collections.singleton(elementFilter));
	}

	/**
	 * Constructor with {@link NamespaceCallbackHandler} and {@link ElementFilter}.
	 * Use this constructor if you want to add custom callbacks to and filter
	 * elements from the parsing process.
	 *
	 * @param callbackHandler a {@link NamespaceCallbackHandler} implementation
	 * @param elementFilter   a {@link ElementFilter} implementation
	 * @throws NullPointerException if argument is {@code null}
	 */
	public PodcastParser(NamespaceCallbackHandler callbackHandler, ElementFilter elementFilter) {
		this(Collections.singleton(callbackHandler), Collections.singleton(elementFilter));
	}

	/**
	 * Constructor with {@link NamespaceCallbackHandler}. Use this constructor if
	 * you want to multiple callback handlers to execute custom logic to the parsing
	 * process.
	 *
	 * @param callbackHandlers a {@link Collection} of
	 *                         {@link NamespaceCallbackHandler} implementations
	 * @param elementFilters   a {@link Collection} of {@link ElementFilter}
	 *                         implementations
	 * @throws NullPointerException if either argument is {@code null} or contains
	 *                              {@code null}
	 */
	public PodcastParser(Collection<NamespaceCallbackHandler> callbackHandlers, Collection<ElementFilter> elementFilters) {
		Objects.requireNonNull(callbackHandlers);
		Objects.requireNonNull(elementFilters);
		this.factory = XMLInputFactory.newFactory();
		this.factory.setXMLResolver(new QuietResolver());
		this.namespaceCallbackHandlers = Collections.unmodifiableList(new ArrayList<>(callbackHandlers));
		this.namespaceCallbackHandlers.forEach(Objects::requireNonNull);
		this.elementFilters = Collections.unmodifiableSet(new LinkedHashSet<>(elementFilters));
		this.elementFilters.forEach(Objects::requireNonNull);
	}

	/**
	 * Parse the given XML {@link String} into a {@link Feed} object.
	 *
	 * @param xml a {@link java.lang.String} object.
	 * @return a {@link Feed} object
	 * @throws PodcastParserException if any
	 */
	public Feed parse(String xml) throws PodcastParserException {
		if (Strings.isBlank(xml)) {
			throw new NotPodcastFeedException("xml input is blank");
		}
		try (StringReader reader = new StringReader(xml.trim().replaceFirst("^([\\W]+)<", "<"))) {
			return parse(reader);
		}
	}

	/**
	 * Parse the given XML {@link InputStream} into a {@link Feed} object.
	 *
	 * @param reader a {@link java.io.Reader} object.
	 * @return a {@link Feed} object.
	 * @throws PodcastParserException if any.
	 */
	public Feed parse(Reader reader) throws PodcastParserException {
		try {
			return doParse(reader);
		} catch (XMLStreamException e) {
			throw new InvalidFeedFormatException(e);
		}
	}

	private Feed doParse(Reader reader) throws XMLStreamException, PodcastParserException {
		XMLStreamReader streamReader = factory.createXMLStreamReader(reader);
		while (streamReader.hasNext()) {
			switch (streamReader.next()) {
			case XMLStreamConstants.DTD:
				String text = streamReader.getText();
				if ("<!DOCTYPE html>".equalsIgnoreCase(text)) {
					throw new NotPodcastFeedException("the input appears to be HTML");
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (streamReader.getLocalName()) {
				case "rss": {
					PodcastParserContext ctx = new PodcastParserContext("rss", streamReader, namespaceCallbackHandlers, elementFilters);
					RSS.instance().parseFeed(ctx);
					return ctx.getFeed();
				}
				case "feed": {
					PodcastParserContext ctx = new PodcastParserContext("atom", streamReader, namespaceCallbackHandlers, elementFilters);
					Atom.instance().parseFeed(ctx);
					return ctx.getFeed();
				}
				default:
					throw new NotPodcastFeedException("root element must be rss or feed but it is {}", streamReader.getLocalName());
				}
			case XMLStreamConstants.END_DOCUMENT:
				streamReader.close();
				break;
			}
		}
		throw new PodcastParserException("provided feed XML is empty");
	}

}
