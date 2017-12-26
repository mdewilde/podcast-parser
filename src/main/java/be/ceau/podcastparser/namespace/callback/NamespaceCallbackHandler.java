package be.ceau.podcastparser.namespace.callback;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;

/**
 * <p>
 * Implement callback logic to be executed anytime a {@code process()} method on
 * a {@link Namespace} implementation is called. These methods are:
 * </p>
 * <ul>
 * <li>{@link Namespace#process(Feed, XMLStreamReader)}
 * <li>{@link Namespace#process(Item, XMLStreamReader)}
 * </ul>
 * <p>
 * Implementations should be threadsafe.
 * </p>
 */
public interface NamespaceCallbackHandler {

	/**
	 * Receives a callback before
	 * {@link Namespace#process(Feed, XMLStreamReader)} is invoked.
	 *
	 * @param rootNamespace
	 *            {@link String} name of the root namespace (RSS|Atom)
	 * @param feed
	 *            {@link Feed} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 */
	public default void beforeProcess(String rootNamespace, Feed feed, XMLStreamReader reader) {
		// default is to do nothing
	}

	/**
	 * Receives a callback before
	 * {@link Namespace#process(Item, XMLStreamReader)} is invoked.
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace (RSS|Atom)
	 * @param item
	 *            {@link Item} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 */
	public default void beforeProcess(String rootNamespace, Item item, XMLStreamReader reader) {
		// default is to do nothing
	}

	/**
	 * Receives a callback when an element with an unknown namespace is
	 * encountered. Unknown in this context means not implemented in this
	 * libary.
	 * 
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with the given
	 *            namespace.
	 * @param level
	 *            {@link String} name of the level at which the element is
	 *            encountered (feed|item)
	 */
	public default void registerUnknownNamespace(XMLStreamReader reader, String level) {
		// default is to do nothing
	}

}