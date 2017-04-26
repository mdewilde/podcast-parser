package be.ceau.podcastparser;

import java.util.Objects;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.NamespaceCallbackHandler;

public class PodParseContext {

	private final String rootNamespace;
	private final XMLStreamReader reader;
	private final NamespaceCallbackHandler namespaceCallbackHandler;
	private final Feed feed;

	public PodParseContext(String rootNamespace, XMLStreamReader reader, NamespaceCallbackHandler namespaceCallbackHandler) {
		Objects.requireNonNull(rootNamespace);
		Objects.requireNonNull(reader);
		Objects.requireNonNull(namespaceCallbackHandler);
		this.rootNamespace = rootNamespace;
		this.reader = reader;
		this.namespaceCallbackHandler = namespaceCallbackHandler;
		this.feed = new Feed();
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

	public void unknownNamespace(String level) {
		namespaceCallbackHandler.unknownNamespace(reader, level);
	}

	public String getElementText() throws XMLStreamException {
		return reader.getElementText();
	}

	public String getAttribute(String localName) {
		String value = reader.getAttributeValue(null, localName);
		if (value != null) {
			value = value.trim();
		}
		return value;
	}

}
