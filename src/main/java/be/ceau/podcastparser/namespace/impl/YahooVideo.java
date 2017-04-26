package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamConstants;
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
		case "adInfo" : 
		case "adTargeting" : 
		case "id" : 
		case "name" : 
		case "value" : 
		default : 
			LoggerFactory.getLogger(Namespace.class).info("YahooVideo {} --> {} {}", ctx.getReader().getLocalName(), Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}
	}

	private void parseAdInfo(PodParseContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.START_ELEMENT : 
				switch (ctx.getReader().getLocalName()) {
				case "adTargeting" : 
					break;
				case "adData" : 
					break;
				case "id" : 
					break;
				case "name" : 
					break;
				case "value" : 
					break;
				}
			case XMLStreamConstants.END_ELEMENT : 
				switch (ctx.getReader().getLocalName()) {
				case "adInfo" : 
					return;
				}
			}
		}
	}
	
	/*
      <yv:adInfo>
        <yv:adTargeting>
          <yv:adData>
            <yv:id>YahooPartnerVideoID</yv:id>
            <yv:name>Blip post id</yv:name>
            <yv:value>5713460</yv:value>
          </yv:adData>
        </yv:adTargeting>
      </yv:adInfo>

	 */
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