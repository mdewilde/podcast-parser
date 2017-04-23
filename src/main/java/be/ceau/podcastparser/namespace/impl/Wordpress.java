package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

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
			LoggerFactory.getLogger(Namespace.class).info("Wordpress site --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "post-id" :
			LoggerFactory.getLogger(Namespace.class).info("Wordpress post-id --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

}

/*
	corpus stats
	
    193028 	--> com-wordpress:feed-additions:1 level=item localName=post-id attributes=[]]
      5263 	--> com-wordpress:feed-additions:1 level=feed localName=site attributes=[]]

*/