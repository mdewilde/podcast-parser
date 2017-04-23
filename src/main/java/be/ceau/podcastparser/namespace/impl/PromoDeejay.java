package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

public class PromoDeejay implements Namespace {

	private static final String NAME = "http://promodeejay.net/api/xml/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "fileID":
			LoggerFactory.getLogger(Namespace.class).info("PromoDeejay fileID --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "kind":
			LoggerFactory.getLogger(Namespace.class).info("PromoDeejay kind --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

}

/*

	corpus statistics 

     40673 	--> http://promodeejay.net/api/xml/ level=item localName=fileID attributes=[]]
     40673 	--> http://promodeejay.net/api/xml/ level=item localName=kind attributes=[]]

*/