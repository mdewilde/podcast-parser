/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
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

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.namespace.Namespace;

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

	private static final String NAME = "http://purl.org/rss/1.0/modules/slash/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "comments":
			Integer num = ctx.getElementTextAsInteger();
			if (num != null) {
				item.setNumberOfComments(num);
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