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

import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * 
 */
public class Blogger implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://schemas.google.com/blogger/2008");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "adultContent":
			break;
		default:
			Namespace.super.process(feed, reader);
			break;
		}
	}

}
