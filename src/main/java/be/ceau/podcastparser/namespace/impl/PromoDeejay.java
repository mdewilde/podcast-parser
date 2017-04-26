package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

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
			item.addOtherValue(OtherValueKey.PROMODEEJAY_FILE_ID, ctx.getElementText());
			break;
		case "kind":
			item.addOtherValue(OtherValueKey.PROMODEEJAY_KIND, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus statistics 

     40673 	--> http://promodeejay.net/api/xml/ level=item localName=fileID attributes=[]]
     40673 	--> http://promodeejay.net/api/xml/ level=item localName=kind attributes=[]]

*/