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

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.stax.PodcastParser;
import be.ceau.podcastparser.stax.models.Category;
import be.ceau.podcastparser.stax.models.Enclosure;
import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.stax.models.Image;
import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.stax.models.Link;
import be.ceau.podcastparser.stax.models.Person;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Dates;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>Atom Syndication Format</h1>
 * 
 * <p>
 * Atom is an XML-based document format that describes lists of related
 * information known as "feeds". Feeds are composed of a number of items, known
 * as "entries", each with an extensible set of attached metadata.
 * </p>
 * 
 * @see https://tools.ietf.org/html/rfc4287
 */
public class Atom implements Namespace {

	private static final Logger logger = LoggerFactory.getLogger(Atom.class);

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.w3.org/2005/atom");

	private static final Atom INSTANCE = new Atom();

	public static Atom instance() {
		return INSTANCE;
	}

	private Atom() {

	}

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	public Feed parseFeed(XMLStreamReader reader) throws XMLStreamException {
		Feed feed = new Feed();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("feed".equals(reader.getLocalName())) {
					return feed;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				PodcastParser.countAtFeed(reader);
				process(feed, reader);
				break;
			}
		}
		return feed;
	}

	@Override
	public void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {
		Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
		if (mustDelegateTo(namespace)) {
			namespace.process(feed, reader);
			return;
		}

		switch (reader.getLocalName()) {
		case "author":
			feed.addAuthor(parsePerson(reader, "author"));
			break;
		case "category":
			Category category = new Category();
			Attributes.get("term").from(reader).ifPresent(category::setName);
			feed.addCategory(category);
			break;
		case "contributor":
			feed.addContributor(parsePerson(reader, "contributor"));
			break;
		case "generator":
			feed.setGenerator(reader.getElementText());
			break;
		case "icon":
			Image icon = new Image();
			icon.setUrl(reader.getElementText());
			icon.setDescription("icon");
			feed.addImage(icon);
			break;
		case "logo":
			Image logo = new Image();
			logo.setUrl(reader.getElementText());
			logo.setDescription("logo");
			feed.addImage(logo);
			break;
		case "id":
			feed.setId(reader.getElementText());
			break;
		case "link":
			feed.addLink(parseLink(reader));
			break;
		case "rights":
			feed.setCopyright(reader.getElementText());
			break;
		case "subtitle":
			feed.setSubtitle(reader.getElementText());
			break;
		case "title":
			feed.setTitle(reader.getElementText());
			break;
		case "updated":
			feed.setLastBuildDate(Dates.parse(reader.getElementText()));
			break;
		case "entry":
			feed.addItem(parseEntry(reader));
			break;
		}
		Namespace.super.process(feed, reader);
	}

	public Item parseEntry(XMLStreamReader reader) throws XMLStreamException {
		Item item = new Item();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("entry".equals(reader.getLocalName())) {
					return item;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				PodcastParser.countAtItem(reader);
				process(item, reader);
				break;
			}
		}
		return item;
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
		if (mustDelegateTo(namespace)) {
			namespace.process(item, reader);
			return;
		}

		switch (reader.getLocalName()) {
		case "author":
			item.addAuthor(parsePerson(reader, "author"));
			break;
		case "category": 
			item.addCategory(new Category(reader.getAttributeValue(null, "term")));
			break;
		case "content":
			// The "atom:content" element either contains or links to
			// the content of the entry.
			// item.setEnclosure(parseEnclosure(reader));
			break;
		case "contributor":
			break;
		case "id":
			item.setGuid(reader.getElementText());
			break;
		case "link":
			if ("enclosure".equals(reader.getAttributeValue(null, "rel"))) {
				item.setEnclosure(parseEnclosure(reader));
			} else {
				item.addLink(parseLink(reader));
			}
			break;
		case "published":
			item.setPubDate(Dates.parse(reader.getElementText()));
			break;
		case "rights":
			item.setCopyright(reader.getElementText());
			break;
		case "source":
			break;
		case "summary":
			item.setDescription(reader.getElementText());
			break;
		case "title":
			item.setTitle(reader.getElementText());
			break;
		case "updated":
			item.setUpdated(Dates.parse(reader.getElementText()));
			break;
		}
		Namespace.super.process(item, reader);
	}

	private Person parsePerson(XMLStreamReader reader, String elementName) throws XMLStreamException {
		Person person = new Person();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if (elementName.equals(reader.getLocalName())) {
					return person;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName()) {
				case "name":
					person.setName(reader.getElementText());
					break;
				case "email":
					person.setEmail(reader.getElementText());
					break;
				case "uri":
					person.setUri(reader.getElementText());
					break;
				}
				break;
			}
		}
		return person;
	}

	private Link parseLink(XMLStreamReader reader) throws XMLStreamException {
		Link link = new Link();
		// links in Atom are self-closing
		Attributes.get("href").from(reader).ifPresent(link::setHref);
		Attributes.get("rel").from(reader).ifPresent(link::setRel);
		Attributes.get("type").from(reader).ifPresent(link::setType);
		Attributes.get("hreflang").from(reader).ifPresent(link::setHreflang);
		Attributes.get("title").from(reader).ifPresent(link::setTitle);
		Attributes.get("length").from(reader).ifPresent(link::setLength);
		return link;
	}

	/**
	 * @param reader
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event on element
	 *            {@code content}
	 * @return an {@link Enclosure} instance parsed from the given
	 *         {@link XMLStreamReader}
	 * @throws XMLStreamException
	 */
	private Enclosure parseEnclosure(XMLStreamReader reader) throws XMLStreamException {
		Enclosure enclosure = new Enclosure();
		enclosure.setUrl(reader.getAttributeValue(null, "href"));
		enclosure.setType(reader.getAttributeValue(null, "type"));
		String length = reader.getAttributeValue(null, "length");
		if (length != null) {
			try {
				enclosure.setLength(Long.parseLong(length));
			} catch (NumberFormatException e) {

			}
		}
		return enclosure;
	}

}

