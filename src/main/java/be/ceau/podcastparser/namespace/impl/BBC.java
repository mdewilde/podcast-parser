package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

public class BBC implements Namespace {

	private static final String NAME = "http://bbc.co.uk/2009/01/ppgRss";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "systemRef":
			LoggerFactory.getLogger(Namespace.class).info("BBC systemRef --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "seriesDetails":
			LoggerFactory.getLogger(Namespace.class).info("BBC seriesDetails --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "network":
			LoggerFactory.getLogger(Namespace.class).info("BBC network --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "enclosureLegacy":
			LoggerFactory.getLogger(Namespace.class).info("BBC enclosureLegacy --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "enclosureSecure":
			LoggerFactory.getLogger(Namespace.class).info("BBC enclosureSecure --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "canonical":
			LoggerFactory.getLogger(Namespace.class).info("BBC canonical --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

}

/*

	corpus statistics 

	31585 	--> http://bbc.co.uk/2009/01/ppgRss level=item localName=enclosureLegacy attributes=[length, type, url]]
	31585 	--> http://bbc.co.uk/2009/01/ppgRss level=item localName=enclosureSecure attributes=[length, type, url]]
	31585 	--> http://bbc.co.uk/2009/01/ppgRss level=item localName=canonical attributes=[]]
	 1237 	--> http://bbc.co.uk/2009/01/ppgRss level=feed localName=systemRef attributes=[systemId, key]]
	  443 	--> http://bbc.co.uk/2009/01/ppgRss level=feed localName=seriesDetails attributes=[daysLive, frequency]]
	  443 	--> http://bbc.co.uk/2009/01/ppgRss level=feed localName=network attributes=[name, id]]

*/