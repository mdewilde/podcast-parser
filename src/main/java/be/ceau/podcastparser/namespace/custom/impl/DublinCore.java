/*
	
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

import java.util.Set;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Category;
import be.ceau.podcastparser.models.support.Person;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Dates;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>Dublin Core</h1>
 * 
 * <p>
 * The Dublin Core Schema is a small set of vocabulary terms that can be used to
 * describe web resources (video, images, web pages, etc.), as well as physical
 * resources such as books or CDs, and objects like artworks.
 * </p>
 * <p>
 * The original Dublin Core Metadata Element Set consists of 15 metadata
 * elements:
 * </p>
 * <ul>
 * <li>Title
 * <li>Creator
 * <li>Subject
 * <li>Description
 * <li>Publisher
 * <li>Contributor
 * <li>Date
 * <li>Type
 * <li>Format
 * <li>Identifier
 * <li>Source
 * <li>Language
 * <li>Relation
 * <li>Coverage
 * <li>Rights
 * </ul>
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Dublin_Core">Dublin Core documentation</a>
 */
public class DublinCore implements Namespace {

	private static final String NAME = "http://purl.org/dc/elements/1.1/";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of(
			"https://purl.org/dc/elements/1.1/",
			"http://purl.org/dc/elements/1.1");

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "creator":
			Person creator = new Person();
			creator.setName(ctx.getElementText());
			ctx.getFeed().addAuthor(creator);
			break;
		case "date":
			ctx.getFeed().setPubDate(Dates.parse(ctx.getElementText()));
			break;
		case "publisher":
		case "rights":
		case "subject":
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "creater":
		case "creator":
			Person creator = new Person();
			creator.setName(ctx.getElementText());
			item.addAuthor(creator);
			break;
		case "date":
			item.setPubDate(Dates.parse(ctx.getElementText()));
			break;
		case "language":
			item.setLanguage(ctx.getElementText());
			break;
		case "modifieddate": 
			item.setUpdated(Dates.parse(ctx.getElementText()));
			break;
		case "subject": 
			item.setSubject(ctx.getElementText());
			break;
		case "type" :
			Category category = new Category();
			category.setName(ctx.getElementText());
			item.addCategory(category);
			break;
		case "format":
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

}

/*

	corpus stats
	
   2036707 	--> http://purl.org/dc/elements/1.1/ level=item localName=creator attributes=[]]
     23509 	--> http://purl.org/dc/elements/1.1/ level=item localName=date attributes=[]]
      7258 	--> http://purl.org/dc/elements/1.1/ level=item localName=subject attributes=[]]
      1809 	--> http://purl.org/dc/elements/1.1/ level=item localName=title attributes=[]]
      1206 	--> http://purl.org/dc/elements/1.1/ level=item localName=publisher attributes=[]]
       982 	--> http://purl.org/dc/elements/1.1/ level=item localName=description attributes=[]]
       680 	--> http://purl.org/dc/elements/1.1/ level=feed localName=creator attributes=[]]
       652 	--> http://purl.org/dc/elements/1.1/ level=item localName=language attributes=[]]
       633 	--> http://purl.org/dc/elements/1.1/ level=item localName=type attributes=[]]
       497 	--> http://purl.org/dc/elements/1.1/ level=item localName=rights attributes=[]]
       314 	--> http://purl.org/dc/elements/1.1/ level=item localName=format attributes=[]]
       289 	--> http://purl.org/dc/elements/1.1/ level=feed localName=date attributes=[]]
       191 	--> http://purl.org/dc/elements/1.1/ level=feed localName=language attributes=[]]
       176 	--> http://purl.org/dc/elements/1.1/ level=item localName=copyright attributes=[]]
       151 	--> http://purl.org/dc/elements/1.1/ level=feed localName=rights attributes=[]]
       145 	--> http://purl.org/dc/elements/1.1 level=item localName=creator attributes=[]]
       145 	--> http://purl.org/dc/elements/1.1/ level=item localName=identifier attributes=[]]
       125 	--> http://purl.org/dc/elements/1.1/ level=item localName=contributor attributes=[]]
       101 	--> http://purl.org/dc/elements/1.1/ level=item localName=creater attributes=[]]
        95 	--> https://purl.org/dc/elements/1.1/ level=item localName=creator attributes=[]]
        84 	--> http://purl.org/dc/elements/1.1/ level=item localName=source attributes=[]]
        35 	--> http://purl.org/dc/elements/1.1/ level=item localName=modified attributes=[]]
        30 	--> http://purl.org/dc/elements/1.1/ level=feed localName=subject attributes=[]]
        25 	--> http://purl.org/dc/elements/1.1/ level=item localName=date.Taken attributes=[]]
        18 	--> http://purl.org/dc/elements/1.1/ level=feed localName=publisher attributes=[]]
        13 	--> http://purl.org/dc/elements/1.1/ level=feed localName=title attributes=[]]
         8 	--> http://purl.org/dc/elements/1.1/ level=item localName=modifieddate attributes=[]]
         7 	--> http://purl.org/dc/elements/1.1/ level=item localName=type attributes=[resource]]
         6 	--> http://purl.org/dc/elements/1.1/ level=feed localName=description attributes=[]]
         3 	--> http://purl.org/dc/elements/1.1/ level=feed localName=coverage attributes=[]]
         2 	--> http://purl.org/dc/elements/1.1/ level=feed localName=type attributes=[]]
         2 	--> http://purl.org/dc/elements/1.1/ level=feed localName=contributor attributes=[]]
         1 	--> http://purl.org/dc/elements/1.1/ level=feed localName=relation attributes=[]]
         1 	--> http://purl.org/dc/elements/1.1/ level=feed localName=identifier attributes=[]]
         1 	--> http://purl.org/dc/elements/1.1/ level=feed localName=format attributes=[]]
         1 	--> http://purl.org/dc/elements/1.1/ level=feed localName=source attributes=[]]

*/
