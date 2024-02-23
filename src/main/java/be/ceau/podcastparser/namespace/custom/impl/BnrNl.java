/*
	
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
import be.ceau.podcastparser.util.UnmodifiableSet;

public class BnrNl implements Namespace {

	private static final String NAME = "http://www.bnr.nl/rss/podcast";

	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of("http://www.bnr.nl/rss/podcast/meta");

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
		case "title":
			item.setTitle(ctx.getElementText());
			break;
		case "description":
			item.setDescription(ctx.getElementText());
			break;
		case "brandStory":
		case "broadcastDate":
		case "category":
		case "name":
		case "spotlight":
		case "type":
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}