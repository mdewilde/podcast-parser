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

import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Category;
import be.ceau.podcastparser.models.Enclosure;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.models.Person;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.namespace.NamespaceFactory;
import be.ceau.podcastparser.namespace.RootNamespace;
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
		if (StringUtils.isNotBlank(ns) && !NAME.equals(ns) && !getAlternativeNames().contains(ns)) {
			Namespace namespace = NamespaceFactory.getInstance(ns);
			if (mustDelegateTo(namespace)) {
				namespace.process(ctx);
				return;
			}
			// not root namespace and not other namespace we can handle
			ctx.registerUnknownNamespace("feed");
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
			Image icon = new Image();
			icon.setUrl(ctx.getElementText());
			icon.setDescription("icon");
			ctx.getFeed().addImage(icon);
			break;
		case "logo":
			Image logo = new Image();
			logo.setUrl(ctx.getElementText());
			logo.setDescription("logo");
			ctx.getFeed().addImage(logo);
			break;
		case "id":
			ctx.getFeed().setId(ctx.getElementText());
			break;
		case "link":
			ctx.getFeed().addLink(parseLink(ctx));
			break;
		case "rights":
			ctx.getFeed().setCopyright(ctx.getElementText());
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

	public Item parseEntry(PodParseContext ctx) throws XMLStreamException {
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

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		String ns = ctx.getReader().getNamespaceURI();
		if (StringUtils.isNotBlank(ns) && !NAME.equals(ns) && !getAlternativeNames().contains(ns)) {
			Namespace namespace = NamespaceFactory.getInstance(ns);
			if (mustDelegateTo(namespace)) {
				namespace.process(ctx, item);
				return;
			}
			// not root namespace and not other namespace we can handle
			ctx.registerUnknownNamespace("item");
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
			Namespace.super.process(ctx, item);
			// The "atom:content" element either contains or links to
			// the content of the entry.
			// item.setEnclosure(parseEnclosure(ctx.getReader()));
			break;
		case "contributor":
			Namespace.super.process(ctx, item);
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
			item.setCopyright(ctx.getElementText());
			break;
		case "source":
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
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
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

}
