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
 * <h1>WGS84 Geo Positioning: an RDF vocabulary</h1>
 * <p>
 * A vocabulary for representing latitude, longitude and altitude information in
 * the WGS84 geodetic reference datum.
 * </p>
 */
public class Geo implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.w3.org/2003/01/geo/wgs84_pos#");
	
	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}

/*

	corpus stats
	
      5143 	--> http://www.w3.org/2003/01/geo/wgs84_pos# level=item localName=long attributes=[]]
      4861 	--> http://www.w3.org/2003/01/geo/wgs84_pos# level=item localName=lat attributes=[]]
      1908 	--> http://www.w3.org/2003/01/geo/wgs84_pos# level=feed localName=long attributes=[]]
      1908 	--> http://www.w3.org/2003/01/geo/wgs84_pos# level=feed localName=lat attributes=[]]
        21 	--> http://www.w3.org/2003/01/geo/wgs84_pos# level=item localName=Point attributes=[]]

*/

