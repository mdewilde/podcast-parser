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
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

public class ItunesU implements Namespace {

	private static final String NAME = "http://www.itunesu.com/feed";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "category" :
			item.addOtherValue(OtherValueKey.ITUNES_UNIVERSITY_CATEGORY, ctx.getAttribute("code"));
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus stats

     12553 	--> http://www.itunesu.com/feed level=item localName=category attributes=[code]]
        90 	--> http://www.itunesu.com/feed level=item localName=category attributes=[]]
        14 	--> http://www.itunesu.com/feed level=feed localName=category attributes=[code]]
         7 	--> http://www.itunesu.com/feed level=feed localName=category attributes=[code, text]]
         6 	--> http://www.itunesu.com/feed level=item localName=category attributes=[text]]

*/