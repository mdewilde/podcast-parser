package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

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
			LoggerFactory.getLogger(Namespace.class).info("RadioFrance originStation --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "businessReference":
			LoggerFactory.getLogger(Namespace.class).info("RadioFrance businessReference --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "magnetothequeID":
			LoggerFactory.getLogger(Namespace.class).info("RadioFrance magnetothequeID --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "stepID":
			LoggerFactory.getLogger(Namespace.class).info("RadioFrance stepID --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
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