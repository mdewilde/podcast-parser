package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

public class ARD implements Namespace {

	private static final String NAME = "http://www.ard.de/ardNamespace";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		default:
			LoggerFactory.getLogger(Namespace.class).info("ARD {} --> {} {}", ctx.getReader().getLocalName(), Attributes.toString(ctx.getReader()), ctx.getElementText());
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		default:
			LoggerFactory.getLogger(Namespace.class).info("ARD {} --> {} {}", ctx.getReader().getLocalName(), Attributes.toString(ctx.getReader()), ctx.getElementText());
		}
	}

}

/*

	corpus statistics 

     11122 	--> http://www.ard.de/ardNamespace level=item localName=visibleFrom attributes=[]]
     11122 	--> http://www.ard.de/ardNamespace level=item localName=visibleUntil attributes=[]]
     11120 	--> http://www.ard.de/ardNamespace level=item localName=visibility attributes=[]]
      3042 	--> http://www.ard.de/ardNamespace level=item localName=crid attributes=[]]
      3042 	--> http://www.ard.de/ardNamespace level=item localName=title attributes=[]]
      2036 	--> http://www.ard.de/ardNamespace level=item localName=broadcast attributes=[]]
      2036 	--> http://www.ard.de/ardNamespace level=item localName=programInformation attributes=[]]
      2036 	--> http://www.ard.de/ardNamespace level=item localName=subtitle attributes=[]]
      2036 	--> http://www.ard.de/ardNamespace level=item localName=end attributes=[]]
      2036 	--> http://www.ard.de/ardNamespace level=item localName=start attributes=[]]
      1006 	--> http://www.ard.de/ardNamespace level=item localName=stationId attributes=[]]
      1006 	--> http://www.ard.de/ardNamespace level=item localName=url attributes=[]]
      1006 	--> http://www.ard.de/ardNamespace level=item localName=series attributes=[]]
      
        70 	--> http://www.ard.de/ardNamespace level=feed localName=stationId attributes=[]]
        70 	--> http://www.ard.de/ardNamespace level=feed localName=url attributes=[]]
        70 	--> http://www.ard.de/ardNamespace level=feed localName=programInformation attributes=[]]
        70 	--> http://www.ard.de/ardNamespace level=feed localName=series attributes=[]]
        70 	--> http://www.ard.de/ardNamespace level=feed localName=title attributes=[]]
        70 	--> http://www.ard.de/ardNamespace level=feed localName=crid attributes=[]]
         3 	--> http://www.ard.de/ardNamespace level=item localName=visibility attributes=[schemaLocation]]

*/