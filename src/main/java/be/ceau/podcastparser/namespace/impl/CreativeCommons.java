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

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

/**
 * <h1>Creative Commons Rights Expression Language</h1>
 * 
 * <p>
 * The Creative Commons Rights Expression Language (CC REL) lets you describe
 * copyright licenses in RDF. For more information on describing licenses in RDF
 * and attaching those descriptions to digital works, see
 * <a href="//wiki.creativecommons.org/CC_REL">CC REL</a> in the
 * <a href="//wiki.creativecommons.org/">Creative Commons wiki</a>.
 * </p>
 */
public class CreativeCommons implements Namespace {

	private static final String NAME = "http://web.resource.org/cc/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "requires":
			LoggerFactory.getLogger(Namespace.class).info("CreativeCommons requires --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "permits":
			LoggerFactory.getLogger(Namespace.class).info("CreativeCommons permits --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "License":
		case "license":
			LoggerFactory.getLogger(Namespace.class).info("CreativeCommons License --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "Work":
			LoggerFactory.getLogger(Namespace.class).info("CreativeCommons Work --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "prohibits":
			LoggerFactory.getLogger(Namespace.class).info("CreativeCommons prohibits --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus stats
	
	    14 	--> http://web.resource.org/cc/ level=item localName=requires attributes=[resource]]
        14 	--> http://web.resource.org/cc/ level=item localName=permits attributes=[resource]]
         7 	--> http://web.resource.org/cc/ level=item localName=License attributes=[about]]
         7 	--> http://web.resource.org/cc/ level=item localName=Work attributes=[about]]
         7 	--> http://web.resource.org/cc/ level=item localName=prohibits attributes=[resource]]
         7 	--> http://web.resource.org/cc/ level=item localName=license attributes=[resource]]

*/
