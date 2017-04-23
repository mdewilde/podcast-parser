package be.ceau.podcastparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.namespace.NamespaceCallbackHandler;
import be.ceau.podcastparser.namespace.NoOpNamespaceCallback;
import be.ceau.podcastparser.namespace.impl.Atom;
import be.ceau.podcastparser.namespace.impl.RSS;

public class PodcastParser {

	private static final Logger logger = LoggerFactory.getLogger(PodcastParser.class);

	private final XMLInputFactory factory;
	private final NamespaceCallbackHandler namespaceCallbackHandler;

	public PodcastParser() {
		this(new NoOpNamespaceCallback());
	}

	public PodcastParser(NamespaceCallbackHandler callbackHandler) {
		Objects.requireNonNull(callbackHandler);
		factory = XMLInputFactory.newFactory();
		factory.setXMLResolver(new QuietResolver());
		namespaceCallbackHandler = callbackHandler;
	}

	public Optional<Feed> parse(String xml) throws XMLStreamException {
		try (InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			return parse(in);
		} catch (IOException e) {
			logger.error("parse(String xml)", e);
			return Optional.empty();
		}
	}

	public Optional<Feed> parse(InputStream in) throws XMLStreamException {
		XMLStreamReader reader = factory.createXMLStreamReader(in);
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName()) {
				case "rss": {
					PodParseContext ctx = new PodParseContext("rss", reader, namespaceCallbackHandler);
					RSS.instance().parseFeed(ctx);
					return Optional.ofNullable(ctx.getFeed());
				}
				case "feed": {
					PodParseContext ctx = new PodParseContext("atom", reader, namespaceCallbackHandler);
					Atom.instance().parseFeed(ctx);
					return Optional.ofNullable(ctx.getFeed());
				}
				default:
					String message = String.format("root element must be <rss> or <feed> but it is <%s>", reader.getLocalName());
					// throw new XMLStreamException(message);
					return Optional.empty();
				}
			case XMLStreamConstants.END_DOCUMENT: 
				reader.close();
				break;
			}
		}
		return Optional.empty();
	}

}
