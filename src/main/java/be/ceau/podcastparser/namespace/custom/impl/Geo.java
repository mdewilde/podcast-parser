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
import be.ceau.podcastparser.namespace.Namespace;

/**
 * <h1>WGS84 Geo Positioning: an RDF vocabulary</h1>
 * <p>
 * A vocabulary for representing latitude, longitude and altitude information in
 * the WGS84 geodetic reference datum.
 * </p>
 */
public class Geo implements Namespace {

	private static final String NAME = "http://www.w3.org/2003/01/geo/wgs84_pos#";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "long":
			ctx.getFeed().setLongitude(ctx.getElementTextAsBigDecimal());
			break;
		case "lat":
			ctx.getFeed().setLatitude(ctx.getElementTextAsBigDecimal());
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "long":
			item.setLongitude(ctx.getElementTextAsBigDecimal());
			break;
		case "lat":
			item.setLatitude(ctx.getElementTextAsBigDecimal());
			break;
		case "Point":
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}
