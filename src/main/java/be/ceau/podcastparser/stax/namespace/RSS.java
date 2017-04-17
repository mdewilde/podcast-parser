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

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.stax.PodcastParser;
import be.ceau.podcastparser.stax.models.Category;
import be.ceau.podcastparser.stax.models.Enclosure;
import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.stax.models.Image;
import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.stax.models.Link;
import be.ceau.podcastparser.stax.models.Person;
import be.ceau.podcastparser.stax.models.UnmappedElement;
import be.ceau.podcastparser.util.Attributes;

/**
 * <h1>Really Simple Syndication</h1>
 * <p>
 * RSS is dialect of XML. All RSS files must conform to the XML 1.0
 * specification, as published on the World Wide Web Consortium (W3C) website.
 * </p>
 * <p>
 * At the top level, a RSS document is a {@code <rss>} element, with a mandatory
 * attribute called version, that specifies the version of RSS that the document
 * conforms to. If it conforms to this specification, the version attribute must
 * be 2.0.
 * </p>
 * <p>
 * Subordinate to the {@code <rss>} element is a single {@code <channel>}
 * element, which contains information about the channel (metadata) and its
 * contents.
 * </p>
 * 
 * @see https://validator.w3.org/feed/docs/rss2.html
 *
 */
public class RSS implements Namespace {

	public static final Set<String> NAMES = Collections.emptySet();

	private static final RSS INSTANCE = new RSS();

	public static RSS instance() {
		return INSTANCE;
	}

	private RSS() {

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
				if ("channel".equals(reader.getLocalName())) {
					return feed;
				}
				break;
			case XMLStreamConstants.START_ELEMENT :
				switch (reader.getLocalName()) {
				case "channel":
					// first element in rss file
					break;
				case "title":
					feed.setTitle(reader.getElementText());
					break;
				case "link": {
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(feed, reader);
					} else {
						Link link = new Link();
						link.setHref(reader.getElementText());
						feed.addLink(link);
					}
					break;
				}
				case "description":
					feed.setDescription(reader.getElementText());
					break;
				case "item":
					feed.addItem(parseItem(reader));
					break;
				case "language":
					feed.setLanguage(reader.getElementText());
					break;
				case "copyright":
					feed.setCopyright(reader.getElementText());
					break;
				case "managingEditor":
					feed.setManagingEditor(reader.getElementText());
					break;
				case "webMaster":
					feed.setWebMaster(reader.getElementText());
					break;
				case "pubDate":
					feed.setPubDate(reader.getElementText());
					break;
				case "lastBuildDate":
					feed.setLastBuildDate(reader.getElementText());
					break;
				case "category": {
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(feed, reader);
					} else {
						feed.addCategory(new Category(reader.getElementText()));
					}
					break;
				}
				case "generator":
					feed.setGenerator(reader.getElementText());
					break;
				case "docs":
					feed.setDocs(reader.getElementText());
					break;
				case "cloud":
					feed.setCloud(reader.getElementText());
					break;
				case "ttl":
					feed.setTtl(reader.getElementText());
					break;
				case "image": {
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(feed, reader);
					} else {
						feed.addImage(parseImage(reader));
					}
					break;
				}
				case "textInput":
					feed.setTextInput(reader.getElementText());
					break;
				case "skipHours":
					feed.setSkipHours(reader.getElementText());
					break;
				case "skipDays":
					feed.setSkipDays(reader.getElementText());
					break;
				default: {
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(feed, reader);
					} else {
						PodcastParser.UNMAPPED.computeIfAbsent(new UnmappedElement(reader, "feed"), x -> new LongAdder()).increment();
					}
					break;
				}
				}
				break;
			}
		}
		return feed;
	}

	private Item parseItem(XMLStreamReader reader) throws XMLStreamException {
		Item item = new Item();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("item".equals(reader.getLocalName())) {
					return item;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName()) {
				case "title":
					item.setTitle(reader.getElementText());
					break;
				case "link":
					Link link = new Link();
					link.setHref(reader.getElementText());
					item.addLink(link);
					break;
				case "description":
					item.setDescription(reader.getElementText());
					break;
				case "author":
					Person person = new Person();
					person.setEmail(reader.getElementText());
					item.addAuthor(person);
					break;
				case "category": {
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(item, reader);
					} else {
						item.addCategory(new Category(reader.getElementText()));
					}
					break;
				}
				case "comments": {
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(item, reader);
					} else {
						item.setComments(reader.getElementText());
					}
					break;
				}
				case "enclosure":
					item.setEnclosure(parseEnclosure(reader));
					break;
				case "guid":
					item.setGuid(reader.getElementText());
					break;
				case "pubDate":
					item.setPubDate(reader.getElementText());
					break;
				case "source":
					item.setSource(reader.getElementText());
					break;
				default:
					Namespace namespace = NamespaceFactory.getInstance(reader.getNamespaceURI());
					if (namespace != null) {
						namespace.process(item, reader);
					} else {
						PodcastParser.UNMAPPED.computeIfAbsent(new UnmappedElement(reader, "item"), x -> new LongAdder()).increment();
					}
					break;
				}
				break;
			}
		}
		return item;
	}

	/**
	 * @param reader
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event on element
	 *            {@code image}
	 * @return an {@link Image} instance parsed from the given
	 *         {@link XMLStreamReader}
	 * @throws XMLStreamException
	 */
	private Image parseImage(XMLStreamReader reader) throws XMLStreamException {
		Image image = new Image();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("image".equals(reader.getLocalName())) {
					return image;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName()) {
				case "url":
					image.setUrl(reader.getElementText());
					break;
				case "title":
					image.setTitle(reader.getElementText());
					break;
				case "link":
					image.setLink(reader.getElementText());
					break;
				case "width":
					String width = reader.getElementText();
					if (width != null) {
						try {
							image.setWidth(Integer.parseInt(width.trim()));
						} catch (NumberFormatException e) {	}
					}
					break;
				case "height":
					String height = reader.getElementText();
					if (height != null) {
						try {
							image.setHeight(Integer.parseInt(height.trim()));
						} catch (NumberFormatException e) {	}
					}
					break;
				case "description":
					image.setDescription(reader.getElementText());
					break;
				}
				break;
			}
		}
		return image;
	}
	
	/**
	 * @param reader
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event on element
	 *            {@code enclosure}
	 * @return an {@link Enclosure} instance parsed from the given
	 *         {@link XMLStreamReader}
	 * @throws XMLStreamException
	 */
	private Enclosure parseEnclosure(XMLStreamReader reader) throws XMLStreamException {
		Enclosure enclosure = new Enclosure();
		Attributes.get("url").from(reader).ifPresent(enclosure::setUrl);
		Attributes.get("type").from(reader).ifPresent(enclosure::setType);
		Attributes.get("length").from(reader).ifPresent(length -> {
			if (length != null) {
				try {
					enclosure.setLength(Long.parseLong(length));
				} catch (NumberFormatException e) {}
			}
		});
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("enclosure".equals(reader.getLocalName())) {
					return enclosure;
				}
				break;
			}
		}
		return enclosure;
	}
	
}
