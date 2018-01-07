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
import be.ceau.podcastparser.models.support.TypedString;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Strings;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>RDF Site Summary 1.0 Modules: Content</h1>
 * <p>
 * A module for the actual content of websites, in multiple formats.
 * </p>
 * 
 * @see http://web.resource.org/rss/1.0/modules/content/
 */
public class Content implements Namespace {

	private static final String NAME = "http://purl.org/rss/1.0/modules/content/";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of(
			"http://purl.org/rss/1.0/modules/content",
			"https://purl.org/rss/1.0/modules/content");

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
		case "encoded":
			item.setContent(parseEncoded(ctx));
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private TypedString parseEncoded(PodcastParserContext ctx) throws XMLStreamException {
		TypedString typedString = new TypedString();
		typedString.setText(ctx.getElementText());
		typedString.setType(Strings.isHtml(typedString.getText()) ? "html" : "plain");
		return typedString;
	}
	
}