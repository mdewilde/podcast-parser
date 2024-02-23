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

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

public class PromoDeejay implements Namespace {

	private static final String NAME = "http://promodeejay.net/api/xml/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "fileID":
			item.addOtherValue(OtherValueKey.PROMODEEJAY_FILE_ID, ctx.getElementText());
			break;
		case "kind":
			item.addOtherValue(OtherValueKey.PROMODEEJAY_KIND, ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus statistics 

     40673 	--> http://promodeejay.net/api/xml/ level=item localName=fileID attributes=[]]
     40673 	--> http://promodeejay.net/api/xml/ level=item localName=kind attributes=[]]

*/