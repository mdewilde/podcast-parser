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
package be.ceau.podcastparser.namespace.root.impl;

import java.time.Duration;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.ParseLevel;
import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Category;
import be.ceau.podcastparser.models.support.Comments;
import be.ceau.podcastparser.models.support.Copyright;
import be.ceau.podcastparser.models.support.Enclosure;
import be.ceau.podcastparser.models.support.Image;
import be.ceau.podcastparser.models.support.Link;
import be.ceau.podcastparser.models.support.Person;
import be.ceau.podcastparser.models.support.TypedString;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.namespace.NamespaceFactory;
import be.ceau.podcastparser.namespace.RootNamespace;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Dates;
import be.ceau.podcastparser.util.Strings;

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
 */
public class RSS implements RootNamespace, Namespace {

	private static final String NAME = "";

	private static final RSS INSTANCE = new RSS();

	public static RSS instance() {
		return INSTANCE;
	}

	private RSS() {
		// threadsafe singleton via static instance()
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void parseFeed(PodcastParserContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("channel".equals(ctx.getReader().getLocalName())) {
					return;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				if (ctx.isSkip()) {
					ctx.skip();
					break;
				}
				ctx.beforeProcess();
				process(ctx);
				break;
			}
		}
		return;
	}

	@Override
	public Item parseItem(PodcastParserContext ctx) throws XMLStreamException {
		Item item = new Item();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("item".equals(ctx.getReader().getLocalName())) {
					return item;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				if (ctx.isSkip()) {
					ctx.skip();
					break;
				}
				ctx.beforeProcess(item);
				process(ctx, item);
				break;
			}
		}
		return item;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		String ns = ctx.getReader().getNamespaceURI();
		if (Strings.isNotBlank(ns) && !NAME.equals(ns) && !getAlternativeNames().contains(ns)) {
			Namespace namespace = NamespaceFactory.getInstance(ns);
			if (mustDelegateTo(namespace)) {
				namespace.process(ctx);
				return;
			}
			// not root namespace and not other namespace we can handle
			ctx.registerUnknownNamespace(ParseLevel.FEED);
			return;
		}

		switch (ctx.getReader().getLocalName()) {
		case "channel":
			// first element in rss file
			break;
		case "title":
			ctx.getFeed().setTitle(ctx.getElementText());
			break;
		case "link":
			Link link = new Link();
			link.setHref(ctx.getElementText());
			ctx.getFeed().addLink(link);
			break;
		case "description":
			ctx.getFeed().setDescription(parseDescription(ctx));
			break;
		case "item":
			ctx.getFeed().addItem(parseItem(ctx));
			break;
		case "language":
			ctx.getFeed().setLanguage(ctx.getElementText());
			break;
		case "copyright":
			ctx.getFeed().setCopyright(parseCopyright(ctx));
			break;
		case "managingEditor":
			ctx.getFeed().setManagingEditor(ctx.getElementText());
			break;
		case "webMaster":
			ctx.getFeed().setWebMaster(ctx.getElementText());
			break;
		case "pubDate":
			ctx.getFeed().setPubDate(Dates.parse(ctx.getElementText()));
			break;
		case "lastBuildDate":
			ctx.getFeed().setLastBuildDate(Dates.parse(ctx.getElementText()));
			break;
		case "category":
			ctx.getFeed().addCategory(new Category().setName(ctx.getElementText()));
			break;
		case "generator":
			ctx.getFeed().setGenerator(ctx.getElementText());
			break;
		case "docs":
			ctx.getFeed().setDocs(ctx.getElementText());
			break;
		case "cloud":
			ctx.getFeed().setCloud(ctx.getElementText());
			break;
		case "ttl":
			ctx.getFeed().setTtl(parseTtl(ctx));
			break;
		case "image":
			ctx.getFeed().addImage(parseImage(ctx));
			break;
		case "textInput":
			ctx.getFeed().setTextInput(ctx.getElementText());
			break;
		case "skipHours":
			// An XML element that contains up to 24 <hour> sub-elements whose value is a number between 0 and
			// 23, representing a time in GMT, when aggregators, if they support the feature, may not read the
			// channel on hours listed in the skipHours element. The hour beginning at midnight is hour zero.
			parseSkipHours(ctx);
			break;
		case "skipDays":
			// An XML element that contains up to seven <day> sub-elements whose value is Monday, Tuesday,
			// Wednesday, Thursday, Friday, Saturday or Sunday. Aggregators may not read the channel during days
			// listed in the skipDays element.
			parseSkipDays(ctx);
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		String ns = ctx.getReader().getNamespaceURI();
		if (Strings.isNotBlank(ns) && !NAME.equals(ns) && !getAlternativeNames().contains(ns)) {
			Namespace namespace = NamespaceFactory.getInstance(ns);
			if (mustDelegateTo(namespace)) {
				namespace.process(ctx, item);
				return;
			}
			// not root namespace and not other namespace we can handle
			ctx.registerUnknownNamespace(ParseLevel.ITEM);
			return;
		}

		switch (ctx.getReader().getLocalName()) {
		case "author":
			Person person = new Person();
			person.setEmail(ctx.getElementText());
			item.addAuthor(person);
			break;
		case "category": 
			item.addCategory(new Category().setName(ctx.getElementText()));
			break;
		case "comments": 
			item.setComments(parseComments(ctx));
			break;
		case "description":
			item.setDescription(ctx.getElementText());
			break;
		case "enclosure":
			item.setEnclosure(parseEnclosure(ctx));
			break;
		case "guid":
			item.setGuid(ctx.getElementText());
			break;
		case "image":
			item.addImage(parseImage(ctx));
			break;
		case "link":
			Link link = new Link();
			link.setHref(ctx.getElementText());
			item.addLink(link);
			break;
		case "pubDate":
			item.setPubDate(Dates.parse(ctx.getElementText()));
			break;
		case "source":
			item.setSource(ctx.getElementText());
			break;
		case "title":
			item.setTitle(ctx.getElementText());
			break;
		case "reader":
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Comments parseComments(PodcastParserContext ctx) throws XMLStreamException {
		Link link = new Link();
		link.setHref(ctx.getElementText());
		Comments comments = new Comments();
		comments.setLink(link);
		return comments;
	}

	private Copyright parseCopyright(PodcastParserContext ctx) throws XMLStreamException {
		Copyright copyright = new Copyright();
		copyright.setText(ctx.getElementText());
		return copyright;
	}

	/**
	 * Phrase or sentence describing the channel, or item synopsis. Entity-encoded HTML is allowed
	 * 
	 * @param ctx
	 *            {@link PodcastParserContext}, not {@code null}
	 * @return a new {@link TypedString}, never {@code null}
	 * @throws XMLStreamException
	 */
	private TypedString parseDescription(PodcastParserContext ctx) throws XMLStreamException {
		String text = ctx.getElementText();
		String type = Strings.isHtml(text) ? "html" : "plain";
		TypedString typedString = new TypedString();
		typedString.setText(ctx.getElementText());
		typedString.setType(type);
		return typedString;
	}
	
	/**
	 * @param ctx.getReader()
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event on element
	 *            {@code image}
	 * @return an {@link Image} instance parsed from the given
	 *         {@link XMLStreamReader}
	 * @throws XMLStreamException
	 */
	private Image parseImage(PodcastParserContext ctx) throws XMLStreamException {
		Image image = new Image();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("image".equals(ctx.getReader().getLocalName())) {
					return image;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (ctx.getReader().getLocalName()) {
				case "url":
					image.setUrl(ctx.getElementText());
					break;
				case "title":
					image.setTitle(ctx.getElementText());
					break;
				case "link":
					image.setLink(ctx.getElementText());
					break;
				case "width":
					String width = ctx.getElementText();
					if (width != null) {
						try {
							image.setWidth(Integer.parseInt(width.trim()));
						} catch (NumberFormatException e) {
						}
					}
					break;
				case "height":
					String height = ctx.getElementText();
					if (height != null) {
						try {
							image.setHeight(Integer.parseInt(height.trim()));
						} catch (NumberFormatException e) {
						}
					}
					break;
				case "description":
					image.setDescription(ctx.getElementText());
					break;
				}
				break;
			}
		}
		return image;
	}

	/**
	 * @param ctx.getReader()
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event on element
	 *            {@code enclosure}
	 * @return an {@link Enclosure} instance parsed from the given
	 *         {@link XMLStreamReader}
	 * @throws XMLStreamException
	 */
	private Enclosure parseEnclosure(PodcastParserContext ctx) throws XMLStreamException {
		Enclosure enclosure = new Enclosure();
		Attributes.get("url").from(ctx.getReader()).ifPresent(enclosure::setUrl);
		Attributes.get("type").from(ctx.getReader()).ifPresent(enclosure::setType);
		Attributes.get("length").from(ctx.getReader()).ifPresent(length -> {
			if (length != null) {
				try {
					enclosure.setLength(Long.parseLong(length));
				} catch (NumberFormatException e) {
				}
			}
		});
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("enclosure".equals(ctx.getReader().getLocalName())) {
					return enclosure;
				}
				break;
			}
		}
		return enclosure;
	}

	
	private void parseSkipHours(PodcastParserContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("skipHours".equals(ctx.getReader().getLocalName())) {
					return;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (ctx.getReader().getLocalName()) {
				case "hour":
					ctx.getFeed().addSkipHour(ctx.getElementTextAsInteger());
					break;
				}
			}
		}
	}

	private void parseSkipDays(PodcastParserContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("skipDays".equals(ctx.getReader().getLocalName())) {
					return;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (ctx.getReader().getLocalName()) {
				case "hour":
					ctx.getFeed().addSkipHour(ctx.getElementTextAsInteger());
					break;
				}
			}
		}
	}

	/*
	 * <ttl> is an optional sub-element of <channel>.
	 * 
	 * ttl stands for time to live. It's a number of minutes that indicates how long a channel can be
	 * cached before refreshing from the source. This makes it possible for RSS sources to be managed by
	 * a file-sharing network such as Gnutella.
	 * 
	 * Example: <ttl>60</ttl>
	 */
	private Duration parseTtl(PodcastParserContext ctx) throws XMLStreamException {
		Integer minutes = ctx.getElementTextAsInteger();
		if (minutes != null) {
			return Duration.ofMinutes(minutes);
		}
		return null;
	}

}
