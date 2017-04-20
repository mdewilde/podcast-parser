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
package be.ceau.podcastparser.stax.namespace;

import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.stax.models.Item;

/**
 * <p>
 * Implements logic specific to a specific XML namespace used in podcasting RSS
 * feeds.
 * </p>
 * <p>
 * Implementations should be threadsafe.
 * </p>
 */
public interface Namespace {

	/**
	 * A namespace has a single, specific, agreed upon name. However, quite a
	 * few namespaces are denoted by multiple names in real world podcast XML
	 * files.
	 * 
	 * @return a {@link Set} of names by which this {@link Namespace} is
	 *         identified.
	 */
	public Set<String> getNames();

	/**
	 * Process any additional information from the {@link XMLStreamReader}, at
	 * its current position, onto the given {@link Feed} according to the
	 * namespace specification.
	 *
	 * @param feed
	 *            {@link Feed} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 * @throws XMLStreamException
	 */
	public default void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {
		// default is to do nothing
	}

	/**
	 * Process any additional information from the {@link XMLStreamReader}, at
	 * its current position, onto the given {@link Item} according to the
	 * namespace specification.
	 * 
	 * @param item
	 *            {@link Item} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 * @throws XMLStreamException
	 */
	public default void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		// default is to do nothing
	}

	public default boolean mustDelegateTo(Namespace namespace) {
		return namespace != null && !this.getClass().equals(namespace.getClass());
	}

}
