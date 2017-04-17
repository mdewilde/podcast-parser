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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>Dublin Core</h1>
 * 
 * <p>
 * The Dublin Core Schema is a small set of vocabulary terms that can be used to
 * describe web resources (video, images, web pages, etc.), as well as physical
 * resources such as books or CDs, and objects like artworks.
 * </p>
 * <p>
 * The original Dublin Core Metadata Element Set consists of 15 metadata
 * elements:
 * </p>
 * <ul>
 * <li>Title
 * <li>Creator
 * <li>Subject
 * <li>Description
 * <li>Publisher
 * <li>Contributor
 * <li>Date
 * <li>Type
 * <li>Format
 * <li>Identifier
 * <li>Source
 * <li>Language
 * <li>Relation
 * <li>Coverage
 * <li>Rights
 * </ul>
 * 
 * @see https://en.wikipedia.org/wiki/Dublin_Core
 */
public class DublinCore implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://purl.org/dc/elements/1.1/");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "creator":
			break;
		case "date":
			break;
		case "publisher":
			break;
		case "rights":
			break;
		case "subject":
			break;
		default:
			Namespace.super.process(feed, reader);
			break;
		}
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "creater":
			break;
		case "creator":
			break;
		case "date":
			break;
		case "format":
			break;
		case "language":
			break;
		case "modifieddate":
			break;
		case "subject":
			break;
		case "type":
			break;
		default:
			Namespace.super.process(item, reader);
			break;
		}
	}

}
