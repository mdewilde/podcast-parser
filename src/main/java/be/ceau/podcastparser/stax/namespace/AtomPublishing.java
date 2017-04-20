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
package be.ceau.podcastparser.stax.namespace;

import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * The Atom Publishing Protocol is an application-level protocol for publishing
 * and editing Web Resources using HTTP and XML 1.0.
 * 
 * @see https://tools.ietf.org/html/rfc5023
 */
public class AtomPublishing implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("https://www.w3.org/2007/app");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "edited":
			break;
		case "control":
			break;
		default:
			Namespace.super.process(item, reader);
			break;
		}
	}

}

/*

	corpus stats
	
       746 	--> http://www.w3.org/2007/app level=item localName=edited attributes=[]]
         1 	--> http://www.w3.org/2007/app level=item localName=control attributes=[]]

*/