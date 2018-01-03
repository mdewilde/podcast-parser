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
package be.ceau.podcastparser.namespace.callback;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.namespace.Namespace;

/**
 * <p>
 * Implement callback logic to be executed anytime a {@code process()} method on a {@link Namespace}
 * implementation is called. These methods are:
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
	 * <p>
	 * Receives a callback before {@link Namespace#process(Feed, XMLStreamReader)} is invoked.
	 * </p>
	 * <p>
	 * Note that any implementation must not move the reader. Doing so will cause an
	 * {@link IllegalStateException} to be thrown.
	 * </p>
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace (RSS|Atom)
	 * @param feed
	 *            {@link Feed} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this namespace.
	 */
	public default void beforeProcess(String rootNamespace, Feed feed, XMLStreamReader reader) {
		// default is to do nothing
	}

	/**
	 * <p>
	 * Receives a callback before {@link Namespace#process(Item, XMLStreamReader)} is invoked.
	 * </p>
	 * <p>
	 * Note that any implementation must not move the reader. Doing so will cause an
	 * {@link IllegalStateException} to be thrown.
	 * </p>
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace (RSS|Atom)
	 * @param item
	 *            {@link Item} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this namespace.
	 */
	public default void beforeProcess(String rootNamespace, Item item, XMLStreamReader reader) {
		// default is to do nothing
	}

	/**
	 * <p>
	 * Receives a callback when an element is encountered for which no handling has been defined in this
	 * library.
	 * </p>
	 * <p>
	 * Note that any implementation must not move the reader. Doing so will cause an
	 * {@link IllegalStateException} to be thrown.
	 * </p>
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace (RSS|Atom)
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with the given namespace.
	 * @param level
	 *            {@link String} name of the level at which the element is encountered (feed|item)
	 */
	public default void registerUnknownNamespace(String rootNamespace, XMLStreamReader reader, String level) {
		// default is to do nothing
	}

	/**
	 * <p>
	 * Receives a callback when an element with an unknown namespace is encountered. Unknown in this
	 * context means not implemented in this libary.
	 * </p>
	 * <p>
	 * Note that any implementation must not move the reader. Doing so will cause an
	 * {@link IllegalStateException} to be thrown.
	 * </p>
	 * 
	 * @param rootNamespace
	 *            {@link String} name of the root namespace (RSS|Atom)
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event.
	 * @param level
	 *            {@link String} name of the level at which the element is encountered (feed|item)
	 */
	public default void registerUnhandledElement(String rootNamespace, XMLStreamReader reader, String level) {
		// default is to do nothing
	}

}
