/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		https://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.podcastparser.namespace.custom.impl;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Visibility;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Dates;

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
			Namespace.super.process(ctx);
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "visibility" : 
			item.setVisibility(parseVisibility(ctx));
			break;
		default:
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Visibility parseVisibility(PodParseContext ctx) throws XMLStreamException {
		Visibility visibility = new Visibility();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("visibility".equals(ctx.getReader().getLocalName())) {
					return visibility;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (ctx.getReader().getLocalName()) {
				case "visibleFrom":
					visibility.setFrom(Dates.parse(ctx.getElementText()));
					break;
				case "visibleUntil":
					visibility.setTo(Dates.parse(ctx.getElementText()));
					break;
				}
				break;
			}
		}
		return visibility;
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