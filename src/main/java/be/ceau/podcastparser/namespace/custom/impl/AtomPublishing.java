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

import java.util.Set;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Dates;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * The Atom Publishing Protocol is an application-level protocol for publishing
 * and editing Web Resources using HTTP and XML 1.0.
 * 
 * @see <a href="https://tools.ietf.org/html/rfc5023">RFC 5023</a>
 */
public class AtomPublishing implements Namespace {

	private static final String NAME = "http://www.w3.org/2007/app";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of("https://www.w3.org/2007/app");

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "edited":
			item.setPubDate(Dates.parse(ctx.getElementText()));
			break;
		case "control":
			// single instance in corpus has no text or attributes
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}