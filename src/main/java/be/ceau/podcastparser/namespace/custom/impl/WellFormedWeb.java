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
package be.ceau.podcastparser.namespace.custom.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Link;
import be.ceau.podcastparser.namespace.Namespace;

/**
 * <p>
 * The {@code <wfw:commentRss>} element lets you syndicate your comments. The URI in the
 * {@code <wfw:commentRss>} element must point to an RSS feed containing the comments for the
 * {@code <item>} it is contained in.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.mozilla.org/en-US/docs/Web/RSS/Module/Well-Formed_Web/Element">Well-Formed
 *      Web specification</a>
 */
public class WellFormedWeb implements Namespace {

	private static final String NAME = "http://wellformedweb.org/commentapi/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "comment": {
			// a link to directly POST comments about this item
			Link link = new Link();
			link.setHref(ctx.getElementText());
			link.setRel("comment");
			item.addLink(link);
			break;
		}
		case "commentRss":
		case "commentRSS": {
			// a link to a feed of comments about this item
			Link link = new Link();
			link.setHref(ctx.getElementText());
			link.setRel("commentRss");
			item.addLink(link);
			break;
		}
		default:
			Namespace.super.process(ctx, item);
			break;
		}
	}

}