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
package be.ceau.podcastparser.namespace;

import java.util.Collections;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.util.Attributes;

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
	 * @return the canonical name for this {@link Namespace}, not {@code null}
	 *         identified.
	 */
	public String getName();

	/**
	 * A namespace has a single, specific, agreed upon name. However, quite a
	 * few namespaces are denoted by multiple names in real world podcast XML
	 * files.
	 * 
	 * @return a {@link Set} of names by which this {@link Namespace} is also
	 *         identified, may be empty, never {@code null}
	 */
	public default Set<String> getAlternativeNames() {
		return Collections.emptySet();
	}

	/**
	 * Process any additional information from the {@link XMLStreamReader}, at
	 * its current position, onto the given {@link Feed} in the
	 * {@link PodParseContext} according to the namespace specification.
	 *
	 * @param ctx
	 *            {@link PodParseContext} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 * @throws XMLStreamException
	 */
	public default void process(PodParseContext ctx) throws XMLStreamException {
		// default is to do nothing
		LoggerFactory.getLogger(Namespace.class).info("{} {} --> {} {}", ctx.getReader().getNamespaceURI(), ctx.getReader().getLocalName(), Attributes.toString(ctx.getReader()), ctx.getElementText());
	}

	/**
	 * Process any additional information from the {@link XMLStreamReader}, at
	 * its current position, onto the given {@link Item} according to the
	 * namespace specification.
	 * 
	 * @param ctx
	 *            {@link PodParseContext} instance in the process of being built
	 * @param item
	 *            {@link Item} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, having just processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 * @throws XMLStreamException
	 */
	public default void process(PodParseContext ctx, Item item) throws XMLStreamException {
		// default is to do nothing
		LoggerFactory.getLogger(Namespace.class).info("{} {} --> {} {}", ctx.getReader().getNamespaceURI(), ctx.getReader().getLocalName(), Attributes.toString(ctx.getReader()), ctx.getElementText());
	}

	/**
	 * @param namespace
	 *            a {@link Namespace} implementation, or {@code null}
	 * @return true if the given {@link Namespace} is not {@code this}
	 *         {@link Namespace}
	 */
	public default boolean mustDelegateTo(Namespace namespace) {
		return namespace != null && !this.getClass().equals(namespace.getClass());
	}

}
