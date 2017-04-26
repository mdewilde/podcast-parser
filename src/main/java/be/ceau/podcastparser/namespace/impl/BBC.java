package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Enclosure;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

public class BBC implements Namespace {

	private static final String NAME = "http://bbc.co.uk/2009/01/ppgRss";

	@Override
	public String getName() {
		return NAME;
	}

	/*
		BBC systemRef --> systemId=pid.brand, key=b007b5wg 
		BBC systemRef --> systemId=pid.genre, key=C00188 
		BBC systemRef --> systemId=pid.format, key=PT004 
		BBC seriesDetails --> frequency=weekly, daysLive=30 
		BBC seriesDetails --> frequency=occasionally, daysLive=-1 
		BBC seriesDetails --> frequency=daily, daysLive=7 
		BBC seriesDetails --> frequency=monthly, daysLive=30 
		BBC network --> id=worldserviceradio, name=BBC World Service 
		BBC network --> id=radio4, name=BBC Radio 4 
	 */
	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "systemRef":
			ctx.getFeed().addOtherValue(OtherValueKey.BBC_SYSTEM_REF, ctx.getElementText());
			LoggerFactory.getLogger(Namespace.class).info("BBC systemRef --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "seriesDetails":
			LoggerFactory.getLogger(Namespace.class).info("BBC seriesDetails --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "network":
			LoggerFactory.getLogger(Namespace.class).info("BBC network --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "enclosureLegacy":
			// a legacy link to the enclosure in this item
			Enclosure enclosureLegacy = new Enclosure();
			enclosureLegacy.setLength(ctx.getAttribute("length"));
			enclosureLegacy.setType(ctx.getAttribute("type"));
			enclosureLegacy.setUrl(ctx.getElementText());
			enclosureLegacy.setDescription("BBC enclosureLegacy");
			item.addOtherEnclosure("BBC enclosureLegacy", enclosureLegacy);
			break;
		case "enclosureSecure":
			// a secure link to the enclosure in this item
			Enclosure enclosureSecure = new Enclosure();
			enclosureSecure.setLength(ctx.getAttribute("length"));
			enclosureSecure.setType(ctx.getAttribute("type"));
			enclosureSecure.setUrl(ctx.getElementText());
			enclosureSecure.setDescription("BBC enclosureSecure");
			item.addOtherEnclosure("BBC enclosureSecure", enclosureSecure);
			break;
		case "canonical":
			// a canonical link to the program this podcast was created from
			Link link = new Link();
			String text = ctx.getElementText();
			if (StringUtils.startsWithIgnoreCase(text, "http")) {
				link.setHref(text);
			} else {
				link.setHref("https://www.bbc.co.uk" + text);
			}
			link.setTitle("BBC canonical");
			item.addLink(link);
			break;
		default : 
			Namespace.super.process(ctx, item);
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