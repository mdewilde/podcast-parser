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
package be.ceau.podcastparser.namespace;

import java.util.Collections;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.ParseLevel;
import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.models.core.Item;

/**
 * <p>
 * Implements logic specific to a specific XML namespace used in podcasting RSS feeds.
 * </p>
 * <p>
 * Implementations should be threadsafe.
 * </p>
 */
public interface Namespace {

	/**
	 * A namespace has a single, specific, agreed upon name. However, quite a few namespaces are denoted
	 * by multiple names in real world podcast XML files.
	 * 
	 * @return the canonical name for this {@link Namespace}, not {@code null} identified.
	 * @see #getAlternativeNames()
	 */
	public String getName();

	/**
	 * A namespace has a single, specific, agreed upon name. However, quite a few namespaces are denoted
	 * by multiple names in real world podcast XML files.
	 * 
	 * @return a {@link Set} of names by which this {@link Namespace} is also identified, may be empty,
	 *         never {@code null}
	 * @see #getName()
	 */
	public default Set<String> getAlternativeNames() {
		return Collections.emptySet();
	}

	/**
	 * Check if the given namespaceURI is for {@code this} {@link Namespace}
	 * 
	 * @param namespaceURI
	 *            {@link String} or {@code null}
	 * @return {@code true} if the given {@code namespaceURI} is associated with {@code this}
	 *         {@link Namespace}
	 */
	public default boolean isMatch(String namespaceURI) {
		if (namespaceURI == null) {
			return false;
		}
		return getName().equals(namespaceURI) || getAlternativeNames().contains(namespaceURI);
	}

	/**
	 * Process any additional information from the {@link XMLStreamReader}, at its current position,
	 * onto the given {@link Feed} in the {@link PodcastParserContext} according to the namespace
	 * specification.
	 *
	 * @param ctx
	 *            {@link PodcastParserContext} instance in the process of being built
	 * @throws XMLStreamException
	 *             if any
	 */
	public default void process(PodcastParserContext ctx) throws XMLStreamException {
		// default is to do nothing
		ctx.registerUnhandledElement(ParseLevel.FEED);
	}

	/**
	 * Process any additional information from the {@link XMLStreamReader}, at its current position,
	 * onto the given {@link Item} according to the namespace specification.
	 * 
	 * @param ctx
	 *            {@link PodcastParserContext} instance in the process of being built
	 * @param item
	 *            {@link Item} instance in the process of being built
	 * @throws XMLStreamException
	 *             if any
	 */
	public default void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		// default is to do nothing
		ctx.registerUnhandledElement(ParseLevel.ITEM);
	}

	/**
	 * @param namespace
	 *            a {@link Namespace} implementation, or {@code null}
	 * @return true if the given {@link Namespace} is not {@code this} {@link Namespace}
	 */
	public default boolean mustDelegateTo(Namespace namespace) {
		return namespace != null && !this.getClass().equals(namespace.getClass());
	}

}
