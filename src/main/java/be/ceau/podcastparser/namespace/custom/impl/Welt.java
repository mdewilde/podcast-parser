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

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

public class Welt implements Namespace {

	private static final String NAME = "https://www.welt.de/spec";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "premium":
			item.addOtherValue(OtherValueKey.WELT_PREMIUM, ctx.getElementText());
			break;
		case "source":
			item.addOtherValue(OtherValueKey.WELT_SOURCE, ctx.getElementText());
			break;
		case "topic":
			item.addOtherValue(OtherValueKey.WELT_TOPIC, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}
