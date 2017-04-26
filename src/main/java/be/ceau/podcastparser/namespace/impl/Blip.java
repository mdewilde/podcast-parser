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

import java.time.temporal.Temporal;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Category;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Dates;
import be.ceau.podcastparser.util.Strings;

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
		case "channel_list":
		case "channel_name":
			// these contain category-type information
			String name = ctx.getElementText();
			if (StringUtils.isNotBlank(name)) {
				Category category = new Category();
				category.setName(ctx.getElementText());
				item.addCategory(category);
				// TODO add differently?
			}
			break;
		case "adminRating":
			item.addOtherValue(OtherValueKey.BLIP_ADMIN_RATING, ctx.getElementText());
			break;
		case "categories":
			parseCategories(ctx, item);
			break;
		case "core":
			item.addOtherValue(OtherValueKey.BLIP_CORE, ctx.getElementText());
			break;
		case "core_value":
			item.addOtherValue(OtherValueKey.BLIP_CORE_VALUE, ctx.getElementText());
			break;
		case "datestamp":
			Temporal datestamp = Dates.parse(ctx.getElementText());
			if (datestamp != null) {
				// TODO -> might be overwriting a different date
				item.setPubDate(datestamp);
			}
			break;
		case "embedLookup":
			item.addOtherValue(OtherValueKey.BLIP_EMBED_LOOKUP, ctx.getElementText());
			break;
		case "embedUrl": {
			String type = ctx.getAttribute("type");
			String href = ctx.getElementText();
			if (Strings.isNotBlank(href)) {
				Link link = new Link();
				link.setHref(href.trim());
				link.setType(type);
				link.setTitle("Blip.tv embedUrl");
				item.addLink(link);
			}
			break;
		}
		case "item_id":
			item.addOtherValue(OtherValueKey.BLIP_ITEM_ID, ctx.getElementText());
			break;
		case "item_type":
			item.addOtherValue(OtherValueKey.BLIP_ITEM_TYPE, ctx.getElementText());
			break;
		case "language":
			item.addOtherValue(OtherValueKey.BLIP_LANGUAGE, ctx.getElementText());
			break;
		case "license":
			item.addOtherValue(OtherValueKey.BLIP_LICENSE, ctx.getElementText());
			break;
		case "picture": {
			String href = ctx.getElementText();
			if (Strings.isNotBlank(href)) {
				Image image = new Image();
				image.setUrl(href);
				image.setTitle("Blip.tv picture");
			}
			break;
		}
		case "poster_image":
			item.addOtherValue(OtherValueKey.BLIP_POSTER_IMAGE, ctx.getElementText());
			break;
		case "posts_id":
			item.addOtherValue(OtherValueKey.BLIP_POSTS_ID, ctx.getElementText());
			break;
		case "puredescription":
			item.addOtherValue(OtherValueKey.BLIP_PURE_DESCRIPTION, ctx.getElementText());
			break;
		case "rating":
			item.addOtherValue(OtherValueKey.BLIP_RATING, ctx.getElementText());
			break;
		case "recommendable":
			item.addOtherValue(OtherValueKey.BLIP_RECOMMENDABLE, ctx.getElementText());
			break;
		case "recommendations":
			item.addOtherValue(OtherValueKey.BLIP_RECOMMENDATIONS, ctx.getElementText());
			break;
		case "runtime":
			// TODO -> appears to be a runtime in seconds (100 - 1000 range)
			item.addOtherValue(OtherValueKey.BLIP_RUNTIME, ctx.getElementText());
			break;
		case "safeusername":
			item.addOtherValue(OtherValueKey.BLIP_SAFE_USERNAME, ctx.getElementText());
			break;
		case "show":
			item.addOtherValue(OtherValueKey.BLIP_SHOW, ctx.getElementText());
			break;
		case "showpage":
			Link showpage = new Link();
			showpage.setHref(ctx.getElementText());
			showpage.setTitle("Blip.tv showpage");
			item.addLink(showpage);
			break;
		case "showpath":
			item.addOtherValue(OtherValueKey.BLIP_SHOW_PATH, ctx.getElementText());
			break;
		case "smallThumbnail":
			Image smallThumbnail = new Image();
			smallThumbnail.setUrl(ctx.getElementText());
			smallThumbnail.setDescription("Blip.tv smallThumbnail");
			item.addImage(smallThumbnail);
			break;
		case "thumbnail_src":
			item.addOtherValue(OtherValueKey.BLIP_THUMBNAIL_SRC, ctx.getElementText());
			break;
		case "user":
			item.addOtherValue(OtherValueKey.BLIP_USER, ctx.getElementText());
			break;
		case "userid":
			item.addOtherValue(OtherValueKey.BLIP_USERID, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private void parseCategories(PodParseContext ctx, Item item) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.START_DOCUMENT:
				if ("category".equals(ctx.getReader().getLocalName())) {
					Category category = new Category();
					category.setName(ctx.getElementText());
					item.addCategory(category);
					return;
				}
				break;
			case XMLStreamConstants.END_ELEMENT :
				if ("categories".equals(ctx.getReader().getLocalName())) {
					return;
				}
				break;
			}
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

