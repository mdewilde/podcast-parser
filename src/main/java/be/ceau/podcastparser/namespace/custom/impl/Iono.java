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
package be.ceau.podcastparser.namespace.custom.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Image;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Strings;

/**
 * 
 * 
 * @see <a href="http://support.iono.fm/knowledgebase/articles/559560-iono-fm-rss-namespace">Iono FM
 *      specification</a>
 */
public class Iono implements Namespace {

	private static final String NAME = "http://iono.fm/rss-namespace-1.0";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "thumbnail":
			Image thumbnail = parseImage(ctx);
			if (thumbnail != null) {
				ctx.getFeed().addImage(thumbnail);
			}
			break;
		default:
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "thumbnail":
			Image thumbnail = parseImage(ctx);
			if (thumbnail != null) {
				item.addImage(thumbnail);
			}
			break;
		default:
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Image parseImage(PodcastParserContext ctx) {
		String href = ctx.getAttribute("href");
		if (Strings.isNotBlank(href)) {
			Image thumbnail = new Image();
			thumbnail.setUrl(href.trim());
			thumbnail.setDescription("Iono thumbnail");
			return thumbnail;
		}
		return null;
	}

}