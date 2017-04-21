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

import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.Item;
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

/*

	corpus stats
	
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=datestamp attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=embedUrl attributes=[type]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=runtime attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=userid attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=item_type attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=item_id attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=thumbnail_src attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=rating attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=show attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=showpage attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=embedLookup attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=puredescription attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=safeusername attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=language attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=user attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=posts_id attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=license attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=picture attributes=[]]
      7325 	--> http://blip.tv/dtd/blip/1.0 level=item localName=adChannel attributes=[]]
      7315 	--> http://blip.tv/dtd/blip/1.0 level=item localName=recommendable attributes=[]]
      7315 	--> http://blip.tv/dtd/blip/1.0 level=item localName=core attributes=[]]
      7315 	--> http://blip.tv/dtd/blip/1.0 level=item localName=recommendations attributes=[]]
      7315 	--> http://blip.tv/dtd/blip/1.0 level=item localName=adminRating attributes=[]]
      7298 	--> http://blip.tv/dtd/blip/1.0 level=item localName=showpath attributes=[]]
      7081 	--> http://blip.tv/dtd/blip/1.0 level=item localName=poster_image attributes=[]]
      4489 	--> http://blip.tv/dtd/blip/1.0 level=item localName=smallThumbnail attributes=[]]
      3785 	--> http://blip.tv/dtd/blip/1.0 level=item localName=channel_list attributes=[]]
      3785 	--> http://blip.tv/dtd/blip/1.0 level=item localName=channel_name attributes=[]]
      2825 	--> http://blip.tv/dtd/blip/1.0 level=item localName=core_value attributes=[]]
      2058 	--> http://blip.tv/dtd/blip/1.0 level=item localName=category attributes=[]]
      1747 	--> http://blip.tv/dtd/blip/1.0 level=item localName=categories attributes=[]]
       764 	--> http://blip.tv/dtd/blip/1.0 level=item localName=youtube_category attributes=[]]
       356 	--> http://blip.tv/dtd/blip/1.0 level=item localName=thumbnail attributes=[]]
       356 	--> http://blip.tv/dtd/blip/1.0 level=item localName=description attributes=[]]
       356 	--> http://blip.tv/dtd/blip/1.0 level=item localName=distributions_info attributes=[]]
       356 	--> http://blip.tv/dtd/blip/1.0 level=item localName=token attributes=[]]
       356 	--> http://blip.tv/dtd/blip/1.0 level=item localName=title attributes=[]]
       269 	--> http://blip.tv/dtd/blip/1.0 level=item localName=is_premium attributes=[]]
       127 	--> http://blip.tv/dtd/blip/1.0 level=item localName=betaUser attributes=[]]
        10 	--> http://blip.tv/dtd/blip/1.0 level=item localName=contentRating attributes=[]]

*/

