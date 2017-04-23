package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

public class YahooVideo implements Namespace {

	private static final String NAME = "http://video.yahooapis.com/v2/video/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "adData" : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo adData --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "adInfo" : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo adInfo --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "adTargeting" : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo adTargeting --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "id" : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo id --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "name" : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo name --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "value" : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo value --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
		Namespace.super.process(ctx, item);
	}

}

/*
	corpus stats

	  6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=value attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adInfo attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adTargeting attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=id attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adData attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=name attributes=[]]

*/