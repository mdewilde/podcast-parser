/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Visibility;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Dates;

public class ARD implements Namespace {

	private static final String NAME = "http://www.ard.de/ardNamespace";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		default:
			Namespace.super.process(ctx);
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "visibility" : 
			item.setVisibility(parseVisibility(ctx));
			break;
		default:
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Visibility parseVisibility(PodcastParserContext ctx) throws XMLStreamException {
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