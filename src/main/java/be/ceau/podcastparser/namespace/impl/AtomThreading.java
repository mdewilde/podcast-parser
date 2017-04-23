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

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

/**
 * an extension for expressing threaded
   discussions within the Atom Syndication Format [RFC4287].
 * 
 * @see http://www.ietf.org/rfc/rfc4685.txt
 */
public class AtomThreading implements Namespace {

	private static final String NAME = "http://purl.org/syndication/thread/1.0";

	@Override
	public String getName() {
		return NAME;
	}

	public final Set<String> total = Collections.newSetFromMap(new ConcurrentHashMap<>());

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "total":
			total.add(ctx.getElementText());
			break;
		case "in-reply-to":
			LoggerFactory.getLogger(Namespace.class).info("AtomThreading in-reply-to --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		default:
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