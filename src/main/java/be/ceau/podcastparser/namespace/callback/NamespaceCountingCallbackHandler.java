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

import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.util.ElementCounter;
import be.ceau.podcastparser.util.EncounteredElement;

/**
 * <p>
 * {@link NamespaceCallbackHandler} implementation that counts every occurrence of any element in any namespace.
 * </p>
 * <p>
 * Add an instance to any number of {@link be.ceau.podcastparser.PodcastParser} instances. Generate a report using
 * {@link #toString()}.
 * </p>
 * <p>
 * This implementation is threadsafe.
 * </p>
 */
public class NamespaceCountingCallbackHandler implements NamespaceCallbackHandler {

	private final ElementCounter counter = new ElementCounter();

	@Override
	public void beforeProcess(String rootNamespace, Feed feed, XMLStreamReader reader) {
		counter.count(new EncounteredElement(rootNamespace, reader, "feed"));
	}

	@Override
	public void beforeProcess(String rootNamespace, Item item, XMLStreamReader reader) {
		counter.count(new EncounteredElement(rootNamespace, reader, "item"));
	}

	@Override
	public void registerUnknownNamespace(String rootNamespace, XMLStreamReader reader, String level) {
		counter.count(new EncounteredElement(rootNamespace, reader, level));
	}

	@Override
	public String toString() {
		return counter.toString();
	}

}
