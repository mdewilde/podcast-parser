package be.ceau.podcastparser.stax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.stax.models.UnmappedElement;
import be.ceau.podcastparser.stax.namespace.Atom;
import be.ceau.podcastparser.stax.namespace.RSS;

public class PodcastParser {

	private static final Logger logger = LoggerFactory.getLogger(PodcastParser.class);

	public static final Map<UnmappedElement, LongAdder> UNMAPPED = new ConcurrentHashMap<>();
	public static final Set<String> DATE_STRINGS = Collections.newSetFromMap(new ConcurrentHashMap<>());
	public static final Set<String> DURATION_STRINGS = Collections.newSetFromMap(new ConcurrentHashMap<>());

	private final XMLInputFactory factory;

	public PodcastParser() {
		factory = XMLInputFactory.newFactory();
		factory.setXMLResolver(new QuietResolver());
	}

	public Optional<Feed> parse(String xml) {
		try (InputStream in = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			return parse(in, xml);
		} catch (IOException e) {
			logger.error("parse(String xml)", e);
			return Optional.empty();
		}
	}

	public Optional<Feed> parse(InputStream in, String xml) {
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(in);
			while (reader.hasNext()) {
				switch (reader.next()) {
				case XMLStreamConstants.START_ELEMENT:
					switch (reader.getLocalName()) {
					case "rss":
						return Optional.ofNullable(RSS.instance().parseFeed(reader));
					case "feed":
						return Optional.ofNullable(Atom.instance().parseFeed(reader));
					default:
						String message = String.format("root element must be <rss> or <feed> but it is <%s>", reader.getLocalName());
						throw new XMLStreamException(message);
					}
				case XMLStreamConstants.END_DOCUMENT: {
					reader.close();
					break;
				}
				}
			}
		} catch (XMLStreamException e) {
			logger.error("parse(InputStream)", e);
		}
		return Optional.empty();
	}

}
