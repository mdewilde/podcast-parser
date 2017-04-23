/*
	Copyright 2017 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Dates;

/**
 * Metadata terms maintained by the Dublin Core Metadata Initiative:
 * 
 * @see http://dublincore.org/documents/2012/06/14/dcmi-terms/
 */
public class DublinCoreTerms implements Namespace {

	private static final String NAME = "http://purl.org/dc/terms/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "modified": {
			// Date on which the resource was changed.
			item.setUpdated(Dates.parse(ctx.getElementText()));
			break;
		}
		case "created": {
			// Date of creation of the resource.
			item.setPubDate(Dates.parse(ctx.getElementText()));
			break;
		}
		case "valid": {
			// Date (often a range) of validity of a resource.
			item.setValidity(Dates.parse(ctx.getElementText()));
			break;
		}
		case "subject": {
			// The topic of the resource.
			item.setSubject(ctx.getElementText());
		}
		}
	}

}

/*

	corpus stats
	
    285271 	--> http://purl.org/dc/terms/ level=item localName=modified attributes=[]]
    285266 	--> http://purl.org/dc/terms/ level=item localName=created attributes=[]]
     10815 	--> http://purl.org/dc/terms/ level=item localName=valid attributes=[]]
      3168 	--> http://purl.org/dc/terms/ level=item localName=subject attributes=[]]

*/

