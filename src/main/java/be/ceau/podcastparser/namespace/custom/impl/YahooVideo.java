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

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.namespace.Namespace;

public class YahooVideo implements Namespace {

	private static final String NAME = "http://video.yahooapis.com/v2/video/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "adData" : 
		case "adInfo" : 
		case "adTargeting" : 
		case "id" : 
		case "name" : 
		case "value" : 
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private void parseAdInfo(PodcastParserContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.START_ELEMENT : 
				switch (ctx.getReader().getLocalName()) {
				case "adTargeting" : 
					break;
				case "adData" : 
					break;
				case "id" : 
					break;
				case "name" : 
					break;
				case "value" : 
					break;
				}
			case XMLStreamConstants.END_ELEMENT : 
				switch (ctx.getReader().getLocalName()) {
				case "adInfo" : 
					return;
				}
			}
		}
	}
	
	/*
      <yv:adInfo>
        <yv:adTargeting>
          <yv:adData>
            <yv:id>YahooPartnerVideoID</yv:id>
            <yv:name>Blip post id</yv:name>
            <yv:value>5713460</yv:value>
          </yv:adData>
        </yv:adTargeting>
      </yv:adInfo>

	 */
}

/*
	corpus stats

	  6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=value attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adInfo attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adTargeting attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=id attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adData attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=name attributes=[]]

*/