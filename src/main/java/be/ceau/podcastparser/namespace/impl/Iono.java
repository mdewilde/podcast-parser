package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Strings;

/**
 * 
 * 
 * @see http://support.iono.fm/knowledgebase/articles/559560-iono-fm-rss-namespace
 */
public class Iono implements Namespace {

	private static final String NAME = "http://iono.fm/rss-namespace-1.0";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "thumbnail":
			Image thumbnail = parseImage(ctx);
			if (thumbnail != null) {
				ctx.getFeed().addImage(thumbnail);
			}
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "thumbnail":
			Image thumbnail = parseImage(ctx);
			if (thumbnail != null) {
				item.addImage(thumbnail);
			}
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Image parseImage(PodParseContext ctx) {
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
/*

	corpus stats

      8621 	--> http://iono.fm/rss-namespace-1.0 level=item localName=thumbnail attributes=[href]]
       111 	--> http://iono.fm/rss-namespace-1.0 level=feed localName=thumbnail attributes=[href]]

*/