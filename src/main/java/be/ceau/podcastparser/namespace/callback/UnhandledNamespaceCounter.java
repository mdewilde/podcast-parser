/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.ParseLevel;
import be.ceau.podcastparser.PodcastParser;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.ElementCounter;
import be.ceau.podcastparser.util.EncounteredElement;

/**
 * <p>
 * {@link NamespaceCallbackHandler} implementation that counts every occurrence of any
 * {@link Namespace} that is not known to this library.
 * </p>
 * <p>
 * Add an instance to any number of {@link PodcastParser} instances. Generate a report using
 * {@link #toString()}.
 * </p>
 * <p>
 * This implementation is threadsafe.
 * </p>
 */
public class UnhandledNamespaceCounter implements NamespaceCallbackHandler {

	static final Logger logger = LoggerFactory.getLogger(UnhandledNamespaceCounter.class);

	private final ElementCounter counter = new ElementCounter();

	@Override
	public void registerUnknownNamespace(String rootNamespace, XMLStreamReader reader, ParseLevel level) {
		EncounteredElement element = new EncounteredElement(rootNamespace, reader, level);
		counter.count(element);
	}

	public Set<String> getNamespaceURIs() {
		return counter.getMap().keySet().stream()
			.map(EncounteredElement::getNamespaceUri)
			.collect(Collectors.toCollection(TreeSet::new));
	}
	
	@Override
	public String toString() {
		return counter.toString();
	}

}
