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

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.namespace.Namespace;

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
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "requires":
		case "permits":
		case "License":
		case "Work":
		case "prohibits":
		default : 
			ctx.log();
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
