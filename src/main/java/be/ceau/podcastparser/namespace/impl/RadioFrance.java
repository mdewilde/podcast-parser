package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
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
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "businessReference":
		case "magnetothequeID":
		case "stepID":
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus statistics 

     74216 	--> http://radiofrance.fr/Lancelot/Podcast# level=item localName=businessReference attributes=[]]
     74202 	--> http://radiofrance.fr/Lancelot/Podcast# level=item localName=magnetothequeID attributes=[]]
     74202 	--> http://radiofrance.fr/Lancelot/Podcast# level=item localName=stepID attributes=[]]
      1232 	--> http://radiofrance.fr/Lancelot/Podcast# level=feed localName=originStation attributes=[]]

*/