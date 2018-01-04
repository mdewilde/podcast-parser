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

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.exceptions.InvalidFeedFormatException;
import be.ceau.podcastparser.exceptions.NotPodcastFeedException;
import be.ceau.podcastparser.exceptions.PodcastParserException;
import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.namespace.callback.NamespaceCallbackHandler;
import be.ceau.podcastparser.namespace.callback.NoOpNamespaceCallback;
import be.ceau.podcastparser.namespace.root.impl.Atom;
import be.ceau.podcastparser.namespace.root.impl.RSS;
import be.ceau.podcastparser.util.Strings;

/**
 * Parser class for converting podcast XML feeds into {@link Feed} objects.
 */
public class PodcastParser {

	private final XMLInputFactory factory;
	private final NamespaceCallbackHandler namespaceCallbackHandler;

	/**
	 * <p>
	 * No-arg constructor
	 * </p>
	 */
	public PodcastParser() {
		this(new NoOpNamespaceCallback());
	}

	/**
	 * <p>
	 * Constructor with {@link NamespaceCallbackHandler}. Use this constructor if you want to add custom logic to the
	 * parsing process. 
	 * </p>
	 *
	 * @param callbackHandler
	 *            a {@link be.ceau.podcastparser.namespace.callback.NamespaceCallbackHandler} object.
	 */
	public PodcastParser(NamespaceCallbackHandler callbackHandler) {
		Objects.requireNonNull(callbackHandler);
		factory = XMLInputFactory.newFactory();
		factory.setXMLResolver(new QuietResolver());
		namespaceCallbackHandler = callbackHandler;
	}

	/**
	 * <p>
	 * Parse the given XML {@link String} into a {@link Feed} object.
	 * </p>
	 *
	 * @param xml
	 *            a {@link java.lang.String} object.
	 * @return a {@link Feed} object
	 * @throws {@link be.ceau.podcastparser.exceptions.PodcastParserException}
	 *             if any
	 */
	public Feed parse(String xml) throws PodcastParserException {
		if (Strings.isBlank(xml)) {
			throw new NotPodcastFeedException("xml input is blank");
		}
		try (StringReader reader = new StringReader(xml.trim().replaceFirst("^([\\W]+)<","<"))) {
			return parse(reader);
		}
	}

	/**
	 * <p>
	 * Parse the given XML {@link InputStream} into a {@link Feed} object.
	 * </p>
	 *
	 * @param reader
	 *            a {@link java.io.Reader} object.
	 * @return a {@link Feed} object.
	 * @throws {@link be.ceau.podcastparser.exceptions.PodcastParserException}
	 *             if any.
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
			case XMLStreamConstants.START_ELEMENT:
				switch (streamReader.getLocalName()) {
				case "rss": {
					PodParseContext ctx = new PodParseContext("rss", streamReader, namespaceCallbackHandler);
					RSS.instance().parseFeed(ctx);
					return ctx.getFeed();
				}
				case "feed": {
					PodParseContext ctx = new PodParseContext("atom", streamReader, namespaceCallbackHandler);
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
