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

import java.math.BigDecimal;

import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Strings;

/**
 * <h1>GeoRSS : Geographically Encoded Objects for RSS feeds</h1>
 * 
 * <p>
 * GeoRSS was designed as a lightweight, community driven way to extend existing
 * feeds with geographic information.
 * </p>
 * 
 * <p>
 * There are currently two encodings of GeoRSS, Simple and GML:
 * </p>
 * 
 * <ul>
 * <li>GeoRSS-Simple is meant as a very lightweight format that developers and
 * users can quickly and easily add to their existing feeds with little effort.
 * It supports basic geometries (point, line, box, polygon) and covers the
 * typical use cases when encoding locations.
 * <li>GeoRSS GML is a formal GML Application Profile, and supports a greater
 * range of features, notably coordinate reference systems other than WGS-84
 * latitude/longitude.
 * </ul>
 * 
 * {@code <georss:point>45.256 -71.92</georss:point>}
 * 
 * {@code <georss:where> <gml:Point> <gml:pos>45.256 -71.92</gml:pos> </gml:Point>
 * </georss:where>}
 * 
 * @see http://www.georss.org
 */
public class GeoRSS implements Namespace {

	private static final String NAME = "http://www.georss.org/georss";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "point":
			// A point contains a single latitude-longitude pair, separated by
			// whitespace.
			String point = ctx.getElementText();
			if (Strings.isNotBlank(point)) {
				String[] split = point.trim().split("\\s+");
				if (split.length == 2) {
					item.computeGeoPointIfAbsent().setLatitude(new BigDecimal(split[0].trim()));
					item.computeGeoPointIfAbsent().setLongitude(new BigDecimal(split[1].trim()));
				}
			}
			break;
		case "featurename":
			item.addOtherValue(OtherValueKey.GEO_RSS_FEATURE_NAME, ctx.getElementText());
			break;
		case "box":
			// A bounding box is a rectangular region, often used to define the
			// extents of a map or a rough area of interest. A box contains two
			// space separate latitude-longitude pairs, with each pair separated
			// by whitespace. The first pair is the lower corner, the second is
			// the upper corner.
			LoggerFactory.getLogger(Namespace.class).info("GeoRSS box --> {} {}", Attributes.toString(ctx.getReader()),
					ctx.getElementText());
			break;
		case "where":
			LoggerFactory.getLogger(Namespace.class).info("GeoRSS where --> {} {}",
					Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*
 * 
 * corpus stats
 * 
 * 25956 --> http://www.georss.org/georss level=item localName=point attributes=[]] 
 *  6026 --> http://www.georss.org/georss level=item localName=featurename attributes=[]] 
 *  6021 --> http://www.georss.org/georss level=item localName=box attributes=[]]
 *     2 --> http://www.georss.org/georss level=item localName=where attributes=[]]
 * 
 */
