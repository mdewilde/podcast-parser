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

import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * 
 */
public class Blip implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://blip.tv/dtd/blip/1.0");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "adChannel":
			break;
		case "adminRating":
			break;
		case "categories":
			break;
		case "category":
			break;
		case "channel_list":
			break;
		case "channel_name":
			break;
		case "core":
			break;
		case "core_value":
			break;
		case "datestamp":
			break;
		case "embedLookup":
			break;
		case "embedUrl":
			break;
		case "item_id":
			break;
		case "item_type":
			break;
		case "language":
			break;
		case "license":
			break;
		case "picture":
			break;
		case "poster_image":
			break;
		case "posts_id":
			break;
		case "puredescription":
			break;
		case "rating":
			break;
		case "recommendable":
			break;
		case "recommendations":
			break;
		case "runtime":
			break;
		case "safeusername":
			break;
		case "show":
			break;
		case "showpage":
			break;
		case "showpath":
			break;
		case "smallThumbnail":
			break;
		case "thumbnail_src":
			break;
		case "user":
			break;
		case "userid":
			break;
		default:
			Namespace.super.process(item, reader);
			break;
		}
	}

}
