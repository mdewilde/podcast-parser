package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

public class Wordpress implements Namespace {

	private static final String NAME = "com-wordpress:feed-additions:1";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "site" :
			ctx.getFeed().addOtherValue(OtherValueKey.WORDPRESS_SITE, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "post-id" :
			item.addOtherValue(OtherValueKey.WORDPRESS_POST_ID, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*
	corpus stats
	
    193028 	--> com-wordpress:feed-additions:1 level=item localName=post-id attributes=[]]
      5263 	--> com-wordpress:feed-additions:1 level=feed localName=site attributes=[]]

*/