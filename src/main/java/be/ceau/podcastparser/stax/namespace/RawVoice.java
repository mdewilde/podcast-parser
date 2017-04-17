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
 * <h1>RawVoice RSS Description</h1>
 * 
 * <p>
 * “RawVoice RSS” is a RSS module that supplements the {@code <enclosure>}
 * element function of RSS 2.0 as well as extend iTunes Media RSS (e.g.
 * {@code <itunes:xxx>}) and/or the Yahoo Media RSS modules by including extra
 * media information that would otherwise not be syndicated. RawVoice extends
 * the enclosures as well as the Apple iTunes/Yahoo Media RSS modules to include
 * additional media information such as TV specific ratings, live stream/embed
 * data, episode poster art, location and episode frequency.
 * </p>
 * 
 * @see http://www.rawvoice.com/services/tools-and-resources/rawvoice-rss-2-0-module-xmlns-namespace-rss2/
 */
public class RawVoice implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.rawvoice.com/rawvoicerssmodule/");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "donate":
			break;
		case "frequency":
			break;
		case "location":
			break;
		case "rating":
			break;
		case "subscribe":
			break;
		default:
			Namespace.super.process(feed, reader);
			break;
		}
	}

}
