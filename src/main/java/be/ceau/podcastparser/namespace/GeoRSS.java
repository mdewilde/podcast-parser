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
package be.ceau.podcastparser.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

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

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.georss.org/georss");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}

/*

	corpus stats
	
     25956 	--> http://www.georss.org/georss level=item localName=point attributes=[]]
      6026 	--> http://www.georss.org/georss level=item localName=featurename attributes=[]]
      6021 	--> http://www.georss.org/georss level=item localName=box attributes=[]]
         2 	--> http://www.georss.org/georss level=item localName=where attributes=[]]

*/

