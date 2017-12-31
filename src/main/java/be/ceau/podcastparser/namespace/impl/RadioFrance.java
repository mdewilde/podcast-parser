package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

public class RadioFrance implements Namespace {

	private static final String NAME = "http://radiofrance.fr/Lancelot/Podcast#";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "originStation":
			ctx.getFeed().addOtherValue(OtherValueKey.RADIO_FRANCE_ORIGIN_STATION, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "businessReference":
			item.addOtherValue(OtherValueKey.RADIO_FRANCE_BUSINESS_REFERENCE, ctx.getElementText());
			break;
		case "magnetothequeID":
			item.addOtherValue(OtherValueKey.RADIO_FRANCE_MAGNETOTHEQUE_ID, ctx.getElementText());
			break;
		case "stepID":
			item.addOtherValue(OtherValueKey.RADIO_FRANCE_STEP_ID, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}