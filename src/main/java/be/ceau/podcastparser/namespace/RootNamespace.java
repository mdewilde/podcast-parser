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
package be.ceau.podcastparser.namespace;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;

/**
 * <p>
 * One of the root namespaces used in podcast feed XML files.
 * </p>
 * <p>
 * Implementations should be threadsafe.
 * </p>
 */
public interface RootNamespace {

	public void parseFeed(PodcastParserContext ctx) throws XMLStreamException;

	public Item parseItem(PodcastParserContext ctx) throws XMLStreamException;

}
