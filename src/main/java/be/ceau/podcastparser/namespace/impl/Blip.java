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
package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

/**
 * 
 */
public class Blip implements Namespace {

	private static final String NAME = "http://blip.tv/dtd/blip/1.0";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "adChannel":
			LoggerFactory.getLogger(Namespace.class).info("Blip adChannel --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "adminRating":
			LoggerFactory.getLogger(Namespace.class).info("Blip adminRating --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "categories":
			LoggerFactory.getLogger(Namespace.class).info("Blip categories --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "category":
			LoggerFactory.getLogger(Namespace.class).info("Blip category --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "channel_list":
			LoggerFactory.getLogger(Namespace.class).info("Blip channel_list --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "channel_name":
			LoggerFactory.getLogger(Namespace.class).info("Blip channel_name --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "core":
			LoggerFactory.getLogger(Namespace.class).info("Blip core --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "core_value":
			LoggerFactory.getLogger(Namespace.class).info("Blip core_value --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "datestamp":
			LoggerFactory.getLogger(Namespace.class).info("Blip datestamp --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "embedLookup":
			LoggerFactory.getLogger(Namespace.class).info("Blip embedLookup --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "embedUrl":
			LoggerFactory.getLogger(Namespace.class).info("Blip embedUrl --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "item_id":
			LoggerFactory.getLogger(Namespace.class).info("Blip item_id --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "item_type":
			LoggerFactory.getLogger(Namespace.class).info("Blip item_type --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "language":
			LoggerFactory.getLogger(Namespace.class).info("Blip language --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "license":
			LoggerFactory.getLogger(Namespace.class).info("Blip license --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "picture":
			LoggerFactory.getLogger(Namespace.class).info("Blip picture --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "poster_image":
			LoggerFactory.getLogger(Namespace.class).info("Blip poster_image --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "posts_id":
			LoggerFactory.getLogger(Namespace.class).info("Blip posts_id --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "puredescription":
			LoggerFactory.getLogger(Namespace.class).info("Blip puredescription --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "rating":
			LoggerFactory.getLogger(Namespace.class).info("Blip rating --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "recommendable":
			LoggerFactory.getLogger(Namespace.class).info("Blip recommendable --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "recommendations":
			LoggerFactory.getLogger(Namespace.class).info("Blip recommendations --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "runtime":
			LoggerFactory.getLogger(Namespace.class).info("Blip runtime --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "safeusername":
			LoggerFactory.getLogger(Namespace.class).info("Blip safeusername --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "show":
			LoggerFactory.getLogger(Namespace.class).info("Blip show --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "showpage":
			LoggerFactory.getLogger(Namespace.class).info("Blip showpage --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "showpath":
			LoggerFactory.getLogger(Namespace.class).info("Blip showpath --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "smallThumbnail":
			LoggerFactory.getLogger(Namespace.class).info("Blip smallThumbnail --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "thumbnail_src":
			LoggerFactory.getLogger(Namespace.class).info("Blip thumbnail_src --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "user":
			LoggerFactory.getLogger(Namespace.class).info("Blip user --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "userid":
			LoggerFactory.getLogger(Namespace.class).info("Blip userid --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
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

