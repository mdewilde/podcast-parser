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

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

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

	private static final Logger logger = LoggerFactory.getLogger(Slash.class);
	
	private static final String NAME = "http://purl.org/rss/1.0/modules/slash/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "comments":
			String num = ctx.getElementText();
			try {
				int number = Integer.parseInt(num);
				item.computeCommentsIfAbsent().setNumber(number);
			} catch (NumberFormatException e) {
				logger.debug("{} on {}", e.getMessage(), num);
			}
			break;
		case "section":
			// part of spec but not encountered in corpus
		case "department":
			// part of spec but not encountered in corpus
		case "hit_parade":
			// part of spec but not encountered in corpus
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus stats
	
    736937 	--> http://purl.org/rss/1.0/modules/slash/ level=item localName=comments attributes=[]]

*/