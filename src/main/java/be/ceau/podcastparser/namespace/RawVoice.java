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

import be.ceau.podcastparser.models.Feed;
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

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.rawvoice.com/rawvoiceRssModule/");

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

/*

	corpus stats

     27416 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=embed attributes=[]]
     13116 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=poster attributes=[url]]
      6727 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=location attributes=[]]
      6565 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=frequency attributes=[]]
      5240 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes]]
      4486 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=rating attributes=[]]
      3591 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=isHD attributes=[]]
       712 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html]]
       529 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=donate attributes=[href]]
       507 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes]]
       355 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=webm attributes=[src, length, type]]
       272 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[type]]
       128 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, blubrry]]
       126 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[link, type]]
       124 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher]]
       112 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html]]
       111 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, tunein, blubrry]]
       108 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher]]
       105 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html, blubrry]]
        85 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html, tunein, blubrry]]
        71 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay]]
        62 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html]]
        50 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, blubrry]]
        48 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, blubrry]]
        46 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html, blubrry]]
        44 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html, tunein]]
        39 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, blubrry]]
        35 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html]]
        34 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=webm attributes=[src, type]]
        32 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[position, type]]
        31 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, tunein]]
        29 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html, blubrry]]
        29 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, position, type]]
        26 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[link, position, type]]
        26 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html, blubrry]]
        24 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, tunein, blubrry]]
        22 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, tunein]]
        14 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html, tunein, blubrry]]
        11 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, blubrry]]
         9 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, link, position, type]]
         8 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, tunein]]
         6 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher]]
         6 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html, tunein]]
         5 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, tunein, blubrry]]
         5 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, html, blubrry]]
         5 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, stitcher]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, link, type]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, tunein]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html, tunein, blubrry]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, html]]
         3 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, html]]
         3 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html, tunein, blubrry]]
         3 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, stitcher, tunein, blubrry]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html, tunein]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, blubrry]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, type]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, tunein, blubrry]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, tunein]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html, tunein]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, blubrry]]
         1 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, tunein]]
         1 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, stitcher, html, tunein, blubrry]]
         1 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, tunein, blubrry]]

*/
