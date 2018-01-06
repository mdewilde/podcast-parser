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

import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.ParseLevel;
import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Category;
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
public class Atom implements RootNamespace, Namespace {

	private static final String NAME = "http://www.w3.org/2005/Atom";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of("http://www.w3.org/2005/Atom/");

	private static final Atom INSTANCE = new Atom();

	public static Atom instance() {
		return INSTANCE;
	}

	private Atom() {
		// threadsafe singleton via static instance()
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	public void parseFeed(PodParseContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("feed".equals(ctx.getReader().getLocalName())) {
					return;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				ctx.beforeProcess();
				process(ctx);
				break;
			}
		}
		return;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
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
		case "author":
			ctx.getFeed().addAuthor(parsePerson(ctx, "author"));
			break;
		case "category":
			Category category = new Category();
			Attributes.get("term").from(ctx.getReader()).ifPresent(category::setName);
			ctx.getFeed().addCategory(category);
			break;
		case "contributor":
			ctx.getFeed().addContributor(parsePerson(ctx, "contributor"));
			break;
		case "generator":
			ctx.getFeed().setGenerator(ctx.getElementText());
			break;
		case "icon":
			ctx.getFeed().addImage(parseImage(ctx, "icon"));
			break;
		case "logo":
			ctx.getFeed().addImage(parseImage(ctx, "logo"));
			break;
		case "id":
			ctx.getFeed().setId(ctx.getElementText());
			break;
		case "link":
			ctx.getFeed().addLink(parseLink(ctx));
			break;
		case "rights":
			ctx.getFeed().setCopyright(parseCopyright(ctx));
			break;
		case "subtitle":
			ctx.getFeed().setSubtitle(ctx.getElementText());
			break;
		case "title":
			ctx.getFeed().setTitle(ctx.getElementText());
			break;
		case "updated":
			ctx.getFeed().setLastBuildDate(Dates.parse(ctx.getElementText()));
			break;
		case "entry":
			ctx.getFeed().addItem(parseEntry(ctx));
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
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
			item.addAuthor(parsePerson(ctx, "author"));
			break;
		case "category": 
			item.addCategory(new Category().setName(ctx.getAttribute("term")));
			break;
		case "content":
			item.setContent(parseContent(ctx));
			break;
		case "contributor":
			item.addAuthor(parsePerson(ctx, "contributor"));
			break;
		case "id":
			item.setGuid(ctx.getElementText());
			break;
		case "link":
			if ("enclosure".equals(ctx.getAttribute("rel"))) {
				item.setEnclosure(parseEnclosure(ctx));
			} else {
				item.addLink(parseLink(ctx));
			}
			break;
		case "published":
			item.setPubDate(Dates.parse(ctx.getElementText()));
			break;
		case "rights":
			item.setCopyright(parseCopyright(ctx));
			break;
		case "summary":
			item.setDescription(ctx.getElementText());
			break;
		case "title":
			item.setTitle(ctx.getElementText());
			break;
		case "updated":
			item.setUpdated(Dates.parse(ctx.getElementText()));
			break;
		case "source":
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private TypedString parseContent(PodParseContext ctx) throws XMLStreamException {
		TypedString typedString = new TypedString();
		typedString.setType(ctx.getAttribute("type"));
		typedString.setText(ctx.getElementText());
		return typedString;
	}
	
	private Copyright parseCopyright(PodParseContext ctx) throws XMLStreamException {
		Copyright copyright = new Copyright();
		copyright.setText(ctx.getElementText());
		return copyright;
	}

	/**
	 * @param ctx.getReader()
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event on element
	 *            {@code content}
	 * @return an {@link Enclosure} instance parsed from the given
	 *         {@link XMLStreamReader}
	 * @throws XMLStreamException
	 */
	private Enclosure parseEnclosure(PodParseContext ctx) throws XMLStreamException {
		Enclosure enclosure = new Enclosure();
		enclosure.setUrl(ctx.getAttribute("href"));
		enclosure.setType(ctx.getAttribute("type"));
		String length = ctx.getAttribute("length");
		if (length != null) {
			try {
				enclosure.setLength(Long.parseLong(length));
			} catch (NumberFormatException e) {

			}
		}
		return enclosure;
	}

	private Item parseEntry(PodParseContext ctx) throws XMLStreamException {
		Item item = new Item();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("entry".equals(ctx.getReader().getLocalName())) {
					return item;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				ctx.beforeProcess(item);
				process(ctx, item);
				break;
			}
		}
		return item;
	}

	private Image parseImage(PodParseContext ctx, String elementName) throws XMLStreamException {
		Image image = new Image();
		image.setUrl(ctx.getElementText());
		image.setDescription(elementName);
		return image;
	}

	private Link parseLink(PodParseContext ctx) throws XMLStreamException {
		Link link = new Link();
		// links in Atom are self-closing
		Attributes.get("href").from(ctx.getReader()).ifPresent(link::setHref);
		Attributes.get("rel").from(ctx.getReader()).ifPresent(link::setRel);
		Attributes.get("type").from(ctx.getReader()).ifPresent(link::setType);
		Attributes.get("hreflang").from(ctx.getReader()).ifPresent(link::setHreflang);
		Attributes.get("title").from(ctx.getReader()).ifPresent(link::setTitle);
		Attributes.get("length").from(ctx.getReader()).ifPresent(link::setLength);
		return link;
	}

	private Person parsePerson(PodParseContext ctx, String elementName) throws XMLStreamException {
		Person person = new Person();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if (elementName.equals(ctx.getReader().getLocalName())) {
					return person;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (ctx.getReader().getLocalName()) {
				case "name":
					person.setName(ctx.getElementText());
					break;
				case "email":
					person.setEmail(ctx.getElementText());
					break;
				case "uri":
					person.setUri(ctx.getElementText());
					break;
				}
				break;
			}
		}
		return person;
	}

}
