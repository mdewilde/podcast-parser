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
import be.ceau.podcastparser.models.Chapter;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Durations;

/**
 * <h1>Podlove Simple Chapters</h1>
 * 
 * <p>
 * Podlove Simple Chapters is an XML 1.0 based format meant to extend file
 * formats like Atom Syndication and RSS 2.0 that reference enclosures
 * (podcasts). As the name implies, this format defines simple chapter
 * structures in media files.
 * </p>
 * 
 * @see http://podlove.org/simple-chapters
 */
public class SimpleChapters implements Namespace {

	private static final Logger logger = LoggerFactory.getLogger(SimpleChapters.class);

	private static final String NAME = "http://podlove.org/simple-chapters";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "chapter":
			String aTime = ctx.getAttribute("start");
			Long millis = Durations.parse(aTime);
			if (millis == null) {
				logger.debug("failure parsing {} to milliseconds", aTime);
				return;
			}
			Chapter chapter = new Chapter();
			chapter.setStart(millis);
			chapter.setTitle(ctx.getAttribute("title"));
			String href = ctx.getAttribute("href");
			if (href != null) {
				chapter.computeLinkIfAbsent().setHref(href);
			}
			String image = ctx.getAttribute("image");
			if (image != null) {
				chapter.computeImageIfAbsent().setLink(image);
			}
			item.addChapter(chapter);
			break;
		case "chapters":
			// no special handling -> chapters is represented by a List
			break;
		}
	}

}

/*
	corpus stats
	
     52486 	--> http://podlove.org/simple-chapters level=item localName=chapter attributes=[start, title]]
      5677 	--> http://podlove.org/simple-chapters level=item localName=chapters attributes=[version]]
      5213 	--> http://podlove.org/simple-chapters level=item localName=chapter attributes=[image, start, href, title]]
      2930 	--> http://podlove.org/simple-chapters level=item localName=chapter attributes=[start, href, title]]
       138 	--> http://podlove.org/simple-chapters level=item localName=chapter attributes=[image, start, title]]
        73 	--> http://podlove.org/simple-chapters level=item localName=chapter attributes=[start]]
         3 	--> http://podlove.org/simple-chapters level=item localName=chapters attributes=[]]

*/