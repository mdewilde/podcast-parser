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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Chapter;
import be.ceau.podcastparser.models.support.Image;
import be.ceau.podcastparser.models.support.Link;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Durations;
import be.ceau.podcastparser.util.Strings;

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
			item.addChapter(parseChapter(ctx));
			break;
		case "chapters":
			// no special handling -> chapters is represented by a List
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Chapter parseChapter(PodParseContext ctx) throws XMLStreamException {
		String aTime = ctx.getAttribute("start");
		Long millis = Durations.parse(aTime);
		if (millis == null) {
			logger.debug("failure parsing {} to milliseconds", aTime);
			return null;
		}
		Chapter chapter = new Chapter();
		chapter.setStart(millis);
		chapter.setTitle(ctx.getAttribute("title"));
		String href = ctx.getAttribute("href");
		if (Strings.isNotBlank(href)) {
			Link link = new Link();
			link.setHref(href);
			chapter.setHref(link);
		}
		String img = ctx.getAttribute("image");
		if (Strings.isNotBlank(img)) {
			Image image = new Image();
			image.setLink(img);
			chapter.setImage(image);
		}
		return chapter;
	}
	
}