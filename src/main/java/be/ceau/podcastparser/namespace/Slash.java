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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * Slash is the source code and database that was originally used to create
 * Slashdot, and has now been released under the GNU General Public License. It
 * is a bona fide Open Source / Free Software project.
 * 
 * The Slash RSS 1.0 module augments the RSS core and Dublin Core module's
 * metadata with channel and item-level elements specific to Slash-based sites.
 *
 * @see http://web.resource.org/rss/1.0/modules/slash/
 */
public class Slash implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://purl.org/rss/1.0/modules/slash/");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "section":
			break;
		case "department":
			break;
		case "comments":
			break;
		case "hit_parade":
			break;
		}
		Namespace.super.process(item, reader);
	}

}

/*

	corpus stats
	
    736937 	--> http://purl.org/rss/1.0/modules/slash/ level=item localName=comments attributes=[]]

*/