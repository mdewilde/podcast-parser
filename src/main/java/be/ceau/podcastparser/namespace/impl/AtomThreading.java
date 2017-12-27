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
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

/**
 * An extension for expressing threaded discussions within the Atom Syndication
 * Format [RFC4287].
 * 
 * @see http://www.ietf.org/rfc/rfc4685.txt
 */
public class AtomThreading implements Namespace {

	private static final String NAME = "http://purl.org/syndication/thread/1.0";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "total":
			// The "total" element is used to indicate the total number of
			// unique responses to an entry known to the publisher
			ctx.getFeed().addOtherValue(OtherValueKey.ATOM_THREADING_TOTAL, ctx.getElementText());
			break;
		case "in-reply-to":
			// The "in-reply-to" element is used to indicate that an entry is a
			// response to another resource. The element MUST contain a "ref"
			// attribute identifying the resource that is being responded to.
			Link link = new Link();
			link.setHref(ctx.getAttribute("href"));
			link.setType(ctx.getAttribute("type"));
			link.setRel("in-reply-to");
			link.setTitle(ctx.getAttribute("ref"));
			item.addLink(link);
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus stats
	
    116578 	--> http://purl.org/syndication/thread/1.0 level=item localName=total attributes=[]]
       348 	--> http://purl.org/syndication/thread/1.0 level=item localName=in-reply-to attributes=[ref, href, type]]

*/