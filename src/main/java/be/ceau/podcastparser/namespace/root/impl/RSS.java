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

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Category;
import be.ceau.podcastparser.models.Enclosure;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.models.Person;
import be.ceau.podcastparser.models.TypedString;
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

	public void parseFeed(PodParseContext ctx) throws XMLStreamException {
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("channel".equals(ctx.getReader().getLocalName())) {
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
			ctx.getFeed().setCopyright(ctx.getElementText());
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
			ctx.getFeed().setTtl(ctx.getElementText());
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

	private Item parseItem(PodParseContext ctx) throws XMLStreamException {
		Item item = new Item();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("item".equals(ctx.getReader().getLocalName())) {
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
			Person person = new Person();
			person.setEmail(ctx.getElementText());
			item.addAuthor(person);
			break;
		case "category": 
			item.addCategory(new Category().setName(ctx.getElementText()));
			break;
		case "comments": 
			Link commentLink = new Link();
			commentLink.setHref(ctx.getElementText());
			item.computeCommentsIfAbsent().setLink(commentLink);
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
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	/**
	 * Phrase or sentence describing the channel, or item synopsis. Entity-encoded HTML is allowed
	 * 
	 * @param ctx
	 *            {@link PodParseContext}, not {@code null}
	 * @return a new {@link TypedString}, never {@code null}
	 * @throws XMLStreamException
	 */
	private TypedString parseDescription(PodParseContext ctx) throws XMLStreamException {
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
	private Image parseImage(PodParseContext ctx) throws XMLStreamException {
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
	private Enclosure parseEnclosure(PodParseContext ctx) throws XMLStreamException {
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

	
	private void parseSkipHours(PodParseContext ctx) throws XMLStreamException {
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

	private void parseSkipDays(PodParseContext ctx) throws XMLStreamException {
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

}

/*

	corpus stats
	
   9522931  --> [nsrss] level=item localName=title attributes=[]]
   9507643 	--> [nsrss] level=feed localName=item attributes=[]]
   9483886 	--> [nsrss] level=item localName=pubDate attributes=[]]
   9264954 	--> [nsrss] level=item localName=enclosure attributes=[length, type, url]]
   8432572 	--> [nsrss] level=item localName=description attributes=[]]
   7887712 	--> [nsrss] level=item localName=link attributes=[]]
   7042002 	--> [nsrss] level=item localName=category attributes=[]]
   6048849 	--> [nsrss] level=item localName=guid attributes=[isPermaLink]]
   2931826 	--> [nsrss] level=item localName=guid attributes=[]]
   1906849 	--> [nsrss] level=item localName=comments attributes=[]]
   1240878 	--> [nsrss] level=item localName=author attributes=[]]
    705667 	--> [nsrss] level=item localName=category attributes=[domain]]
    205466 	--> [nsrss] level=item localName=enclosure attributes=[type, url]]
    204266 	--> [nsrss] level=feed localName=title attributes=[]]
    204155 	--> [nsrss] level=feed localName=channel attributes=[]]
    203875 	--> [nsrss] level=feed localName=link attributes=[]]
    203423 	--> [nsrss] level=feed localName=language attributes=[]]
    202441 	--> [nsrss] level=feed localName=description attributes=[]]
    139792 	--> [nsrss] level=feed localName=copyright attributes=[]]
    125025 	--> [nsrss] level=feed localName=image attributes=[]]
    124161 	--> [nsrss] level=feed localName=lastBuildDate attributes=[]]
    121982 	--> [nsrss] level=feed localName=generator attributes=[]]
    121021 	--> [nsrss] level=feed localName=category attributes=[]]
    110946 	--> [nsrss] level=item localName=reader attributes=[]]
    100679 	--> [nsrss] level=feed localName=pubDate attributes=[]]
     99936 	--> [nsrss] level=item localName=source attributes=[url]]
     71782 	--> [nsrss] level=item localName=image attributes=[]]
     68471 	--> [nsrss] level=item localName=url attributes=[]]
     66086 	--> [nsrss] level=item localName=programid attributes=[]]
     66086 	--> [nsrss] level=item localName=poddid attributes=[]]
     50946 	--> [nsrss] level=feed localName=managingEditor attributes=[]]
     48736 	--> [nsrss] level=feed localName=ttl attributes=[]]
     44157 	--> [nsrss] level=item localName=div attributes=[class]]
     37446 	--> [nsrss] level=item localName=imageurl attributes=[]]
     35143 	--> [nsrss] level=feed localName=webMaster attributes=[]]
     32574 	--> [nsrss] level=item localName=mediaTitle attributes=[]]
     32574 	--> [nsrss] level=item localName=mediaId attributes=[]]
     32574 	--> [nsrss] level=item localName=mediaType attributes=[]]
     30122 	--> [nsrss] level=item localName=p attributes=[]]
     26855 	--> [nsrss] level=feed localName=docs attributes=[]]
     26728 	--> [nsrss] level=item localName=span attributes=[class]]
     25948 	--> [nsrss] level=item localName=enclosure attributes=[url]]
     23452 	--> [nsrss] level=item localName=a attributes=[rel, href]]
     20715 	--> [nsrss] level=item localName=podcast_downloadable attributes=[]]
     20702 	--> [nsrss] level=item localName=related_album attributes=[]]
     20124 	--> [nsrss] level=feed localName=b attributes=[]]
     19300 	--> [nsrss] level=item localName=epidsode-js-function attributes=[]]
     16700 	--> [nsrss] level=item localName=copyright attributes=[]]
     13644 	--> [nsrss] level=feed localName=br attributes=[]]
     12250 	--> [nsrss] level=item localName=a attributes=[href]]
     11638 	--> [nsrss] level=item localName=id attributes=[]]
      9166 	--> [nsrss] level=item localName=duration attributes=[]]
      9117 	--> [nsrss] level=item localName=enclosure attributes=[length, url]]
      8987 	--> [nsrss] level=item localName=host attributes=[]]
      8987 	--> [nsrss] level=item localName=imgof attributes=[]]
      8987 	--> [nsrss] level=item localName=guest attributes=[]]
      8980 	--> [nsrss] level=item localName=img attributes=[sizes, src, width, alt, srcset, class, height]]
      8820 	--> [nsrss] level=item localName=audio attributes=[controls, style, id, class, preload]]
      8820 	--> [nsrss] level=item localName=source attributes=[src, type]]
      8317 	--> [nsrss] level=feed localName=cloud attributes=[path, protocol, port, registerProcedure, domain]]
      7449 	--> [nsrss] level=item localName=br attributes=[]]
      7253 	--> [nsrss] level=item localName=thumbnail attributes=[]]
      6386 	--> [nsrss] level=feed localName=category attributes=[domain]]
      5797 	--> [nsrss] level=item localName=guid attributes=[isPermalink]]
      5667 	--> [nsrss] level=item localName=enclosure attributes=[lenght, type, url]]
      5224 	--> [nsrss] level=feed localName=genre attributes=[]]
      4976 	--> [nsrss] level=item localName=date attributes=[]]
      4826 	--> [nsrss] level=item localName=b attributes=[]]
      4805 	--> [nsrss] level=item localName=speaker attributes=[]]
      4311 	--> [nsrss] level=item localName=href attributes=[]]
      4302 	--> [nsrss] level=item localName=dureeReference attributes=[]]
      4302 	--> [nsrss] level=item localName=lead attributes=[]]
      4158 	--> [nsrss] level=item localName=showdate attributes=[]]
      4147 	--> [nsrss] level=item localName=subhead attributes=[]]
      4074 	--> [nsrss] level=item localName=imgw attributes=[]]
      4074 	--> [nsrss] level=item localName=imgh attributes=[]]
      4074 	--> [nsrss] level=item localName=complink attributes=[]]
      4074 	--> [nsrss] level=item localName=subfmt attributes=[]]
      4074 	--> [nsrss] level=item localName=descfmt attributes=[]]
      3798 	--> [nsrss] level=item localName=enclosure attributes=[length, guid, type, url]]
      3601 	--> [nsrss] level=item localName=source attributes=[]]
      2720 	--> [nsrss] level=item localName=content attributes=[]]
      2449 	--> [nsrss] level=item localName=audio attributes=[]]
      2297 	--> [nsrss] level=item localName=guid attributes=[ispermalink, isPermaLink]]
      2281 	--> [nsrss] level=item localName=achievesn attributes=[]]
      2278 	--> [nsrss] level=item localName=complink3 attributes=[]]
      2278 	--> [nsrss] level=item localName=complink2 attributes=[]]
      2073 	--> [nsrss] level=item localName=video attributes=[]]
      2071 	--> [nsrss] level=item localName=videoclip attributes=[]]
      2056 	--> [nsrss] level=item localName=verse attributes=[]]
      1925 	--> [nsrss] level=item localName=name attributes=[]]
      1895 	--> [nsrss] level=feed localName=item attributes=[id, type]]
      1866 	--> [nsrss] level=item localName=summary attributes=[]]
      1783 	--> [nsrss] level=item localName=keyWord attributes=[]]
      1779 	--> [nsrss] level=item localName=image attributes=[href]]
      1756 	--> [nsrss] level=item localName=language attributes=[]]
      1713 	--> [nsrss] level=item localName=guide attributes=[]]
      1679 	--> [nsrss] level=item localName=explicit attributes=[]]
      1631 	--> [nsrss] level=item localName=guid attributes=[isPermaLink, guid]]
      1629 	--> [nsrss] level=item localName=audioId attributes=[]]
      1629 	--> [nsrss] level=item localName=toPubDate attributes=[]]
      1596 	--> [nsrss] level=item localName=a attributes=[data-id, class]]
      1582 	--> [nsrss] level=item localName=image attributes=[reference]]
      1501 	--> [nsrss] level=item localName=height attributes=[]]
      1501 	--> [nsrss] level=item localName=width attributes=[]]
      1485 	--> [nsrss] level=item localName=tag attributes=[]]
      1139 	--> [nsrss] level=item localName=displaydate attributes=[]]
      1134 	--> [nsrss] level=item localName=keywords attributes=[]]
      1110 	--> [nsrss] level=item localName=artist attributes=[]]
      1101 	--> [nsrss] level=item localName=pubdate attributes=[]]
      1095 	--> [nsrss] level=item localName=length attributes=[]]
      1058 	--> [nsrss] level=item localName=meta attributes=[name, content]]
      1046 	--> [nsrss] level=item localName=li attributes=[]]
      1030 	--> [nsrss] level=item localName=lastUpdated attributes=[]]
      1007 	--> [nsrss] level=feed localName=url attributes=[]]
       997 	--> [nsrss] level=item localName=wextAired attributes=[]]
       925 	--> [nsrss] level=item localName=subtitle attributes=[]]
       900 	--> [nsrss] level=item localName=slot attributes=[max, timecode]]
       890 	--> [nsrss] level=item localName=episodeId attributes=[]]
       846 	--> [nsrss] level=feed localName=item attributes=[id]]
       840 	--> [nsrss] level=item localName=notes attributes=[]]
       807 	--> [nsrss] level=item localName=enclosure attributes=[fileSize, type, url]]
       785 	--> [nsrss] level=item localName=title_in_language attributes=[]]
       764 	--> [nsrss] level=item localName=tags attributes=[]]
       748 	--> [nsrss] level=item localName=rte-days attributes=[]]
       730 	--> [nsrss] level=item localName=meta attributes=[property, content]]
       698 	--> [nsrss] level=item localName=img attributes=[src, alt]]
       667 	--> [nsrss] level=item localName=stream attributes=[]]
       667 	--> [nsrss] level=item localName=hq attributes=[]]
       667 	--> [nsrss] level=item localName=hq_filename attributes=[]]
       667 	--> [nsrss] level=item localName=hq_filetype attributes=[]]
       631 	--> [nsrss] level=item localName=description attributes=[type]]
       566 	--> [nsrss] level=item localName=titleApp attributes=[]]
       563 	--> [nsrss] level=feed localName=author attributes=[]]
       559 	--> [nsrss] level=item localName=time attributes=[]]
       552 	--> [nsrss] level=item localName=permalink attributes=[]]
       552 	--> [nsrss] level=item localName=seriesArt attributes=[]]
       546 	--> [nsrss] level=item localName=topTitleApp attributes=[]]
       512 	--> [nsrss] level=item localName=season attributes=[]]
       508 	--> [nsrss] level=item localName=enclosure attributes=[length, id, type, url]]
       500 	--> [nsrss] level=item localName=enclosure attributes=[size, type, url]]
       490 	--> [nsrss] level=item localName=episode attributes=[]]
       484 	--> [nsrss] level=item localName=category attributes=[scheme]]
       484 	--> [nsrss] level=item localName=coverimage attributes=[]]
       484 	--> [nsrss] level=item localName=uniqueid attributes=[]]
       479 	--> [nsrss] level=item localName=expiryTime attributes=[]]
       468 	--> [nsrss] level=item localName=series attributes=[]]
       450 	--> [nsrss] level=feed localName=keywords attributes=[]]
       448 	--> [nsrss] level=item localName=keyword attributes=[]]
       444 	--> [nsrss] level=item localName=enclosure attributes=[]]
       426 	--> [nsrss] level=feed localName=guid attributes=[isPermaLink]]
       425 	--> [nsrss] level=feed localName=item attributes=[sdImg, hdImg]]
       377 	--> [nsrss] level=item localName=series attributes=[id, partno]]
       377 	--> [nsrss] level=item localName=references attributes=[]]
       370 	--> [nsrss] level=item localName=strong attributes=[]]
       367 	--> [nsrss] level=item localName=Speaker attributes=[]]
       353 	--> [nsrss] level=item localName=enclosure attributes=[length1, type, url]]
       347 	--> [nsrss] level=item localName=Date attributes=[]]
       340 	--> [nsrss] level=item localName=streamBitrate attributes=[]]
       340 	--> [nsrss] level=item localName=streamQuality attributes=[]]
       340 	--> [nsrss] level=item localName=Summary attributes=[]]
       340 	--> [nsrss] level=item localName=media attributes=[]]
       333 	--> [nsrss] level=item localName=em attributes=[]]
       325 	--> [nsrss] level=item localName=contentId attributes=[]]
       325 	--> [nsrss] level=item localName=streamFormat attributes=[]]
       325 	--> [nsrss] level=item localName=contentQuality attributes=[]]
       325 	--> [nsrss] level=item localName=synopsis attributes=[]]
       325 	--> [nsrss] level=item localName=streamURL attributes=[]]
       323 	--> [nsrss] level=item localName=guid attributes=[IsPermaLink]]
       316 	--> [nsrss] level=item localName=Keywords attributes=[]]
       309 	--> [nsrss] level=item localName=BibleText attributes=[]]
       303 	--> [nsrss] level=item localName=guid attributes=[ispermalink]]
       298 	--> [nsrss] level=item localName=explict attributes=[]]
       283 	--> [nsrss] level=item localName=fullsummary attributes=[]]
       273 	--> [nsrss] level=item localName=nonp attributes=[]]
       264 	--> [nsrss] level=item localName=lastBuildDate attributes=[]]
       264 	--> [nsrss] level=item localName=body attributes=[]]
       249 	--> [nsrss] level=item localName=email attributes=[]]
       245 	--> [nsrss] level=item localName=username attributes=[]]
       241 	--> [nsrss] level=item localName=img attributes=[src, alt, style]]
       240 	--> [nsrss] level=item localName=a attributes=[property, rel, href]]
       240 	--> [nsrss] level=item localName=style attributes=[type]]
       238 	--> [nsrss] level=item localName=span attributes=[property, rel, href]]
       234 	--> [nsrss] level=item localName=size attributes=[]]
       233 	--> [nsrss] level=item localName=p attributes=[class]]
       233 	--> [nsrss] level=item localName=EpisodeGUID attributes=[]]
       226 	--> [nsrss] level=item localName=youTube attributes=[]]
       223 	--> [nsrss] level=item localName=lastModDate attributes=[]]
       223 	--> [nsrss] level=item localName=enclosure attributes=[artist, part, length, bandWebsite, type, episodeNumber, url, listenUrl, dateRecorded, blogUrl, imageUrl, season, radioSong]]
       218 	--> [nsrss] level=item localName=datesAired attributes=[]]
       217 	--> [nsrss] level=item localName=a attributes=[href, target]]
       216 	--> [nsrss] level=item localName=guid attributes=[isPermalink, isPermaLink]]
       216 	--> [nsrss] level=item localName=seriesID attributes=[]]
       215 	--> [nsrss] level=feed localName=item attributes=[width, id, type, height]]
       215 	--> [nsrss] level=item localName=img attributes=[]]
       207 	--> [nsrss] level=item localName=updDate attributes=[]]
       207 	--> [nsrss] level=item localName=alert attributes=[]]
       205 	--> [nsrss] level=item localName=owner attributes=[]]
       205 	--> [nsrss] level=feed localName=webmaster attributes=[]]
       203 	--> [nsrss] level=item localName=featured attributes=[]]
       202 	--> [nsrss] level=item localName=img attributes=[src, width, alt, class, height]]
       200 	--> [nsrss] level=item localName=creator attributes=[]]
       196 	--> [nsrss] level=feed localName=item attributes=[lang]]
       189 	--> [nsrss] level=item localName=featured_small attributes=[]]
       188 	--> [nsrss] level=item localName=featuredimage attributes=[]]
       188 	--> [nsrss] level=item localName=h2 attributes=[]]
       186 	--> [nsrss] level=item localName=videoLink attributes=[url]]
       186 	--> [nsrss] level=feed localName=style attributes=[]]
       184 	--> [nsrss] level=item localName=indTag attributes=[]]
       180 	--> [nsrss] level=item localName=content attributes=[type]]
       180 	--> [nsrss] level=feed localName=item attributes=[base]]
       179 	--> [nsrss] level=item localName=pubDateParsed attributes=[]]
       174 	--> [nsrss] level=item localName=album attributes=[]]
       172 	--> [nsrss] level=item localName=img attributes=[src, style]]
       172 	--> [nsrss] level=feed localName=Zone attributes=[]]
       170 	--> [nsrss] level=item localName=vidfile attributes=[]]
       170 	--> [nsrss] level=feed localName=script attributes=[type]]
       170 	--> [nsrss] level=item localName=mobile attributes=[url]]
       163 	--> [nsrss] level=item localName=embed_img attributes=[]]
       162 	--> [nsrss] level=item localName=Subtitle attributes=[]]
       157 	--> [nsrss] level=feed localName=image attributes=[href]]
       155 	--> [nsrss] level=feed localName=script attributes=[src, type]]
       144 	--> [nsrss] level=item localName=enclosure attributes=[duration, fileSize, type, url]]
       142 	--> [nsrss] level=item localName=medialink attributes=[]]
       139 	--> [nsrss] level=item localName=catid attributes=[]]
       138 	--> [nsrss] level=item localName=img attributes=[src, alt, width, align, height]]
       136 	--> [nsrss] level=feed localName=item attributes=[number, id]]
       136 	--> [nsrss] level=item localName=tr attributes=[]]
       136 	--> [nsrss] level=item localName=td attributes=[width]]
       136 	--> [nsrss] level=item localName=thumbnail attributes=[url]]
       136 	--> [nsrss] level=item localName=tbody attributes=[]]
       136 	--> [nsrss] level=item localName=table attributes=[width]]
       135 	--> [nsrss] level=item localName=img attributes=[src, alt, width, title, class, height]]
       134 	--> [nsrss] level=item localName=hour attributes=[]]
       123 	--> [nsrss] level=feed localName=item attributes=[version]]
       123 	--> [nsrss] level=feed localName=managingeditor attributes=[]]
       122 	--> [nsrss] level=item localName=link attributes=[url]]
       120 	--> [nsrss] level=feed localName=a attributes=[href]]
       118 	--> [nsrss] level=item localName=i_image attributes=[]]
       113 	--> [nsrss] level=item localName=adzone attributes=[]]
       113 	--> [nsrss] level=item localName=icon attributes=[]]
       112 	--> [nsrss] level=item localName=thumbnail attributes=[type, url]]
       107 	--> [nsrss] level=item localName=highlight attributes=[]]
       103 	--> [nsrss] level=item localName=youtube attributes=[]]
       102 	--> [nsrss] level=item localName=script attributes=[src, type]]
       100 	--> [nsrss] level=item localName=gui attributes=[href]]
       100 	--> [nsrss] level=item localName=mobilesubtitle attributes=[]]
       100 	--> [nsrss] level=item localName=mobilecorner attributes=[]]
       100 	--> [nsrss] level=item localName=span attributes=[title, class]]
       100 	--> [nsrss] level=item localName=mobileimg attributes=[src, width, height]]
       100 	--> [nsrss] level=item localName=i attributes=[]]
        99 	--> [nsrss] level=item localName=media attributes=[fileSize, type, url]]
        97 	--> [nsrss] level=item localName=pubblica attributes=[]]
        95 	--> [nsrss] level=item localName=episodeCategory attributes=[]]
        94 	--> [nsrss] level=feed localName=rating attributes=[]]
        94 	--> [nsrss] level=item localName=episodeCategories attributes=[]]
        88 	--> [nsrss] level=item localName=thumbnailurl attributes=[url]]
        88 	--> [nsrss] level=item localName=imageitem attributes=[]]
        87 	--> [nsrss] level=item localName=link attributes=[rel, href, type]]
        86 	--> [nsrss] level=item localName=enclosure attributes=[ttype, length, url]]
        86 	--> [nsrss] level=item localName=itunes attributes=[]]
        84 	--> [nsrss] level=item localName=param attributes=[name, value]]
        83 	--> [nsrss] level=item localName=post-thumbnail attributes=[]]
        79 	--> [nsrss] level=item localName=soundCloud attributes=[]]
        79 	--> [nsrss] level=item localName=enclosure attributes=[filesize, type, url]]
        78 	--> [nsrss] level=item localName=enclosure attributes=[length, type, url, onload]]
        77 	--> [nsrss] level=item localName=movwidth attributes=[]]
        77 	--> [nsrss] level=item localName=videoID attributes=[]]
        77 	--> [nsrss] level=item localName=movheight attributes=[]]
        76 	--> [nsrss] level=item localName=videolink attributes=[]]
        76 	--> [nsrss] level=item localName=year attributes=[]]
        76 	--> [nsrss] level=item localName=shownotes attributes=[]]
        74 	--> [nsrss] level=item localName=a attributes=[href, title]]
        71 	--> [nsrss] level=item localName=custom1 attributes=[anUrl, label]]
        70 	--> [nsrss] level=item localName=vv-category attributes=[url]]
        69 	--> [nsrss] level=item localName=a attributes=[title, url]]
        68 	--> [nsrss] level=feed localName=displayrows attributes=[]]
        68 	--> [nsrss] level=item localName=title attributes=[type]]
        68 	--> [nsrss] level=feed localName=title_fg attributes=[]]
        68 	--> [nsrss] level=feed localName=foreground attributes=[]]
        68 	--> [nsrss] level=feed localName=background1 attributes=[]]
        68 	--> [nsrss] level=feed localName=background2 attributes=[]]
        68 	--> [nsrss] level=feed localName=lg_headerimage attributes=[]]
        68 	--> [nsrss] level=feed localName=textsize attributes=[]]
        68 	--> [nsrss] level=feed localName=link_color attributes=[]]
        68 	--> [nsrss] level=feed localName=sm_headerimage attributes=[]]
        68 	--> [nsrss] level=feed localName=md_headerimage attributes=[]]
        68 	--> [nsrss] level=feed localName=show_xmltag attributes=[]]
        68 	--> [nsrss] level=feed localName=title_bg attributes=[]]
        66 	--> [nsrss] level=item localName=foto_207 attributes=[href]]
        66 	--> [nsrss] level=item localName=itunes_image attributes=[href]]
        66 	--> [nsrss] level=item localName=pdf attributes=[url]]
        62 	--> [nsrss] level=item localName=the_date attributes=[]]
        61 	--> [nsrss] level=item localName=guid attributes=[url]]
        60 	--> [nsrss] level=item localName=custom_fields attributes=[]]
        60 	--> [nsrss] level=item localName=madDate attributes=[]]
        60 	--> [nsrss] level=item localName=img attributes=[src, width, align, height]]
        58 	--> [nsrss] level=item localName=ul attributes=[]]
        57 	--> [nsrss] level=item localName=vimeo attributes=[]]
        57 	--> [nsrss] level=feed localName=skipDays attributes=[]]
        56 	--> [nsrss] level=item localName=img attributes=[src, width, class]]
        56 	--> [nsrss] level=item localName=ul attributes=[class]]
        56 	--> [nsrss] level=item localName=center attributes=[]]
        56 	--> [nsrss] level=item localName=h1 attributes=[]]
        55 	--> [nsrss] level=feed localName=domain attributes=[]]
        55 	--> [nsrss] level=item localName=h3 attributes=[]]
        54 	--> [nsrss] level=item localName=descrition attributes=[]]
        54 	--> [nsrss] level=feed localName=pubdate attributes=[]]
        53 	--> [nsrss] level=item localName=updated attributes=[]]
        52 	--> [nsrss] level=feed localName=lastbuilddate attributes=[]]
        52 	--> [nsrss] level=item localName=h4 attributes=[]]
        51 	--> [nsrss] level=feed localName=meta attributes=[name, content]]
        51 	--> [nsrss] level=item localName=enclosure attributes=[length, element_type, type, url]]
        50 	--> [nsrss] level=item localName=udid attributes=[]]
        50 	--> [nsrss] level=item localName=showImage attributes=[]]
        50 	--> [nsrss] level=item localName=videourl attributes=[]]
        50 	--> [nsrss] level=feed localName=lame attributes=[]]
        50 	--> [nsrss] level=feed localName=ffmpeg attributes=[]]
        50 	--> [nsrss] level=item localName=showIcon attributes=[]]
        50 	--> [nsrss] level=item localName=showThumb attributes=[]]
        50 	--> [nsrss] level=feed localName=broadcastlimit attributes=[]]
        50 	--> [nsrss] level=item localName=audio_file attributes=[]]
        49 	--> [nsrss] level=item localName=head_office attributes=[]]
        46 	--> [nsrss] level=feed localName=channelExportDir attributes=[]]
        46 	--> [nsrss] level=item localName=nolist attributes=[]]
        46 	--> [nsrss] level=feed localName=ilink attributes=[]]
        46 	--> [nsrss] level=item localName=photo attributes=[]]
        45 	--> [nsrss] level=item localName=imagetitle attributes=[]]
        44 	--> [nsrss] level=feed localName=Description attributes=[]]
        44 	--> [nsrss] level=item localName=enclosure attributes=[duration, length, type, url]]
        44 	--> [nsrss] level=item localName=expires attributes=[]]
        43 	--> [nsrss] level=item localName=twitter attributes=[]]
        43 	--> [nsrss] level=feed localName=Zones attributes=[]]
        43 	--> [nsrss] level=feed localName=DisplayName attributes=[]]
        43 	--> [nsrss] level=feed localName=TotalComments attributes=[]]
        43 	--> [nsrss] level=feed localName=LinkParameter attributes=[]]
        43 	--> [nsrss] level=item localName=facebook attributes=[]]
        43 	--> [nsrss] level=item localName=twitter2 attributes=[]]
        43 	--> [nsrss] level=feed localName=Priority attributes=[]]
        43 	--> [nsrss] level=item localName=guestweb2 attributes=[]]
        43 	--> [nsrss] level=feed localName=DocID attributes=[]]
        43 	--> [nsrss] level=feed localName=HitCount attributes=[]]
        43 	--> [nsrss] level=item localName=format attributes=[]]
        43 	--> [nsrss] level=feed localName=PageIsSSL attributes=[]]
        43 	--> [nsrss] level=feed localName=ShortDescription attributes=[]]
        43 	--> [nsrss] level=feed localName=GoLiveDateRaw attributes=[]]
        43 	--> [nsrss] level=item localName=facebook2 attributes=[]]
        43 	--> [nsrss] level=feed localName=DisplayTargetTypeID attributes=[]]
        43 	--> [nsrss] level=item localName=eventid attributes=[]]
        43 	--> [nsrss] level=feed localName=Node attributes=[]]
        43 	--> [nsrss] level=feed localName=GoLiveDate attributes=[]]
        43 	--> [nsrss] level=feed localName=TotalRatings attributes=[]]
        43 	--> [nsrss] level=item localName=guestweb attributes=[]]
        42 	--> [nsrss] level=item localName=srm_youtube attributes=[url]]
        42 	--> [nsrss] level=feed localName=FileSize attributes=[]]
        42 	--> [nsrss] level=item localName=srm_vodurl attributes=[url]]
        42 	--> [nsrss] level=item localName=date_time attributes=[]]
        42 	--> [nsrss] level=feed localName=ImageFile attributes=[]]
        40 	--> [nsrss] level=feed localName=span attributes=[class]]
        40 	--> [nsrss] level=item localName=descriptionApp attributes=[]]
        40 	--> [nsrss] level=item localName=postid attributes=[]]
        40 	--> [nsrss] level=item localName=span attributes=[style]]
        39 	--> [nsrss] level=item localName=juegos attributes=[]]
        39 	--> [nsrss] level=item localName=item attributes=[]]
        38 	--> [nsrss] level=item localName=enclosure attributes=[LENGTH, TYPE, url]]
        38 	--> [nsrss] level=item localName=enclosure attributes=[type, lngt, url]]
        37 	--> [nsrss] level=item localName=startDate attributes=[]]
        37 	--> [nsrss] level=item localName=CPDpublic attributes=[]]
        37 	--> [nsrss] level=item localName=MediaSource attributes=[]]
        37 	--> [nsrss] level=item localName=Player attributes=[]]
        37 	--> [nsrss] level=feed localName=itunesowner attributes=[]]
        37 	--> [nsrss] level=item localName=Media attributes=[]]
        37 	--> [nsrss] level=item localName=interviewer attributes=[]]
        37 	--> [nsrss] level=item localName=CPDcourse attributes=[]]
        37 	--> [nsrss] level=item localName=interviewee attributes=[]]
        36 	--> [nsrss] level=item localName=iphone attributes=[]]
        36 	--> [nsrss] level=item localName=script attributes=[type]]
        36 	--> [nsrss] level=feed localName=skipHours attributes=[]]
        36 	--> [nsrss] level=item localName=topic attributes=[]]
        36 	--> [nsrss] level=item localName=Description attributes=[]]
        36 	--> [nsrss] level=item localName=discipline attributes=[]]
        34 	--> [nsrss] level=item localName=coverImage attributes=[]]
        32 	--> [nsrss] level=item localName=img attributes=[data-orig-file, data-orig-size, data-medium-file, data-attachment-id, data-image-meta, src, data-image-description, data-permalink, data-large-file, alt, data-image-title, srcset, sizes, width, class, data-comments-opened, height]]
        32 	--> [nsrss] level=item localName=script attributes=[]]
        32 	--> [nsrss] level=item localName=latitude attributes=[]]
        32 	--> [nsrss] level=item localName=longitude attributes=[]]
        32 	--> [nsrss] level=item localName=position attributes=[]]
        31 	--> [nsrss] level=item localName=message attributes=[]]
        31 	--> [nsrss] level=feed localName=all-js-function attributes=[]]
        31 	--> [nsrss] level=item localName=img attributes=[src, alt, title, class]]
        30 	--> [nsrss] level=item localName=blog attributes=[]]
        30 	--> [nsrss] level=feed localName=meta attributes=[property, content]]
        30 	--> [nsrss] level=item localName=guid-size attributes=[]]
        30 	--> [nsrss] level=item localName=guid-name attributes=[]]
        30 	--> [nsrss] level=item localName=podcastFile attributes=[]]
        30 	--> [nsrss] level=item localName=br attributes=[class]]
        30 	--> [nsrss] level=feed localName=div attributes=[style]]
        30 	--> [nsrss] level=item localName=A attributes=[HREF]]
        29 	--> [nsrss] level=item localName=type attributes=[]]
        28 	--> [nsrss] level=item localName=embed attributes=[controller, src, width, pluginspage, type, autoplay, height]]
        28 	--> [nsrss] level=item localName=LI attributes=[TYPE]]
        28 	--> [nsrss] level=item localName=object attributes=[classid, codebase, width, height]]
        28 	--> [nsrss] level=item localName=published attributes=[]]
        27 	--> [nsrss] level=item localName=subTitleLink attributes=[]]
        27 	--> [nsrss] level=item localName=custom_permalink attributes=[]]
        26 	--> [nsrss] level=feed localName=p attributes=[class]]
        26 	--> [nsrss] level=feed localName=guid attributes=[]]
        26 	--> [nsrss] level=item localName=enclosure attributes=[size, url]]
        26 	--> [nsrss] level=feed localName=path attributes=[]]
        26 	--> [nsrss] level=feed localName=audiopath attributes=[]]
        25 	--> [nsrss] level=item localName=style attributes=[]]
        24 	--> [nsrss] level=item localName=managingEditor attributes=[]]
        24 	--> [nsrss] level=feed localName=videoExist attributes=[]]
        24 	--> [nsrss] level=feed localName=audioExist attributes=[]]
        24 	--> [nsrss] level=item localName=enclosure attributes=[length, url, ype]]
        24 	--> [nsrss] level=feed localName=frequenceMiseAJour attributes=[]]
        24 	--> [nsrss] level=feed localName=leRSS attributes=[]]
        24 	--> [nsrss] level=feed localName=nomDocCategorie attributes=[]]
        24 	--> [nsrss] level=feed localName=leRSSitunes attributes=[]]
        24 	--> [nsrss] level=feed localName=EmissionMusical attributes=[]]
        24 	--> [nsrss] level=feed localName=nomTypePodcast attributes=[]]
        24 	--> [nsrss] level=item localName=a attributes=[hreflang, href]]
        24 	--> [nsrss] level=feed localName=nomURLPodCast attributes=[]]
        24 	--> [nsrss] level=feed localName=EmissionParlee attributes=[]]
        23 	--> [nsrss] level=item localName=lowres attributes=[]]
        23 	--> [nsrss] level=item localName=title attributes=[lang]]
        23 	--> [nsrss] level=item localName=catagory attributes=[]]
        23 	--> [nsrss] level=item localName=description attributes=[lang]]
        22 	--> [nsrss] level=item localName=df-title attributes=[]]
        22 	--> [nsrss] level=item localName=episode_number attributes=[]]
        22 	--> [nsrss] level=feed localName=eventid attributes=[]]
        22 	--> [nsrss] level=item localName=episode_image attributes=[]]
        22 	--> [nsrss] level=item localName=podcastimge2 attributes=[href]]
        22 	--> [nsrss] level=item localName=podcastimge1 attributes=[href]]
        21 	--> [nsrss] level=item localName=span attributes=[]]
        20 	--> [nsrss] level=item localName=enclosure attributes=[TYPE, url]]
        20 	--> [nsrss] level=item localName=customtag-image attributes=[href]]
        20 	--> [nsrss] level=item localName=fecha attributes=[]]
        20 	--> [nsrss] level=item localName=guild attributes=[]]
        20 	--> [nsrss] level=item localName=enclosure attributes=[duration, type, url]]
        20 	--> [nsrss] level=item localName=div attributes=[style, class]]
        20 	--> [nsrss] level=feed localName=textInput attributes=[]]
        20 	--> [nsrss] level=item localName=customtag-summary attributes=[]]
        20 	--> [nsrss] level=item localName=customtag-duration attributes=[]]
        19 	--> [nsrss] level=item localName=jwplayer attributes=[]]
        19 	--> [nsrss] level=feed localName=category attributes=[text]]
        18 	--> [nsrss] level=item localName=div attributes=[id]]
        17 	--> [nsrss] level=item localName=filename attributes=[]]
        17 	--> [nsrss] level=item localName=youtubeID attributes=[]]
        17 	--> [nsrss] level=feed localName=subtitle attributes=[]]
        17 	--> [nsrss] level=item localName=blockquote attributes=[]]
        17 	--> [nsrss] level=feed localName=enclosure attributes=[length, type, url]]
        17 	--> [nsrss] level=item localName=foto_640 attributes=[href]]
        16 	--> [nsrss] level=item localName=src attributes=[]]
        16 	--> [nsrss] level=feed localName=explicit attributes=[]]
        16 	--> [nsrss] level=item localName=ytlink attributes=[]]
        16 	--> [nsrss] level=item localName=PubDate attributes=[]]
        15 	--> [nsrss] level=item localName=img attributes=[src, alt, width, height]]
        15 	--> [nsrss] level=item localName=hdImg attributes=[]]
        15 	--> [nsrss] level=item localName=div attributes=[style, id]]
        15 	--> [nsrss] level=item localName=sdImg attributes=[]]
        15 	--> [nsrss] level=item localName=youtube_image attributes=[]]
        15 	--> [nsrss] level=item localName=streamUrl attributes=[]]
        15 	--> [nsrss] level=item localName=youtube_html attributes=[]]
        15 	--> [nsrss] level=item localName=enclosure attributes=[length, bitrate, type, url, quality]]
        14 	--> [nsrss] level=item localName=iexplicit attributes=[]]
        14 	--> [nsrss] level=feed localName=time attributes=[datetime, itemprop]]
        14 	--> [nsrss] level=feed localName=span attributes=[style, class]]
        14 	--> [nsrss] level=item localName=ikeywords attributes=[]]
        14 	--> [nsrss] level=feed localName=p attributes=[itemid, itemscope, itemtype, id, class]]
        14 	--> [nsrss] level=item localName=isummary attributes=[]]
        14 	--> [nsrss] level=item localName=iduration attributes=[]]
        14 	--> [nsrss] level=feed localName=abstract attributes=[]]
        14 	--> [nsrss] level=feed localName=a attributes=[rel, href, class]]
        14 	--> [nsrss] level=feed localName=channel attributes=[lang]]
        14 	--> [nsrss] level=item localName=isubtitle attributes=[]]
        14 	--> [nsrss] level=item localName=iauthor attributes=[]]
        13 	--> [nsrss] level=item localName=fembed attributes=[]]
        13 	--> [nsrss] level=item localName=eclass attributes=[]]
        13 	--> [nsrss] level=feed localName=owner attributes=[]]
        13 	--> [nsrss] level=item localName=imagefembed attributes=[]]
        13 	--> [nsrss] level=item localName=category attributes=[text]]
        13 	--> [nsrss] level=feed localName=script attributes=[]]
        13 	--> [nsrss] level=item localName=artwork attributes=[]]
        13 	--> [nsrss] level=item localName=guid attributes=[isPermaLink, isPermalLink]]
        13 	--> [nsrss] level=item localName=class attributes=[]]
        13 	--> [nsrss] level=item localName=edate attributes=[]]
        13 	--> [nsrss] level=item localName=streaming attributes=[]]
        12 	--> [nsrss] level=item localName=noscript attributes=[]]
        12 	--> [nsrss] level=feed localName=value attributes=[]]
        12 	--> [nsrss] level=item localName=label attributes=[]]
        12 	--> [nsrss] level=item localName=guide attributes=[isPermaLink]]
        12 	--> [nsrss] level=item localName=siteDate attributes=[]]
        12 	--> [nsrss] level=feed localName=param attributes=[]]
        12 	--> [nsrss] level=item localName=sup attributes=[]]
        11 	--> [nsrss] level=feed localName=p attributes=[]]
        11 	--> [nsrss] level=item localName=streamhi attributes=[]]
        11 	--> [nsrss] level=item localName=streamlo attributes=[]]
        10 	--> [nsrss] level=item localName=webMaster attributes=[]]
        10 	--> [nsrss] level=item localName=thumb attributes=[]]
        10 	--> [nsrss] level=item localName=Page attributes=[]]
        10 	--> [nsrss] level=item localName=i attributes=[class]]
        10 	--> [nsrss] level=item localName=reg attributes=[]]
        10 	--> [nsrss] level=item localName=img attributes=[sizes, src, width, alt, style, srcset, class, height]]
        10 	--> [nsrss] level=feed localName=div attributes=[id]]
        10 	--> [nsrss] level=feed localName=summary attributes=[]]
        10 	--> [nsrss] level=feed localName=email attributes=[]]
        10 	--> [nsrss] level=item localName=formatDate attributes=[]]
        10 	--> [nsrss] level=item localName=linkcategory attributes=[]]
        10 	--> [nsrss] level=item localName=Image attributes=[]]
        10 	--> [nsrss] level=feed localName=name attributes=[]]
        10 	--> [nsrss] level=item localName=arfmtbk attributes=[]]
        10 	--> [nsrss] level=item localName=search_title attributes=[]]
        10 	--> [nsrss] level=item localName=a attributes=[href, class]]
        10 	--> [nsrss] level=item localName=featured-image attributes=[]]
         9 	--> [nsrss] level=item localName=order attributes=[]]
         9 	--> [nsrss] level=item localName=publication attributes=[]]
         9 	--> [nsrss] level=item localName=u attributes=[]]
         9 	--> [nsrss] level=item localName=description attributes=[space]]
         8 	--> [nsrss] level=item localName=attachments attributes=[]]
         8 	--> [nsrss] level=item localName=image_width attributes=[]]
         8 	--> [nsrss] level=item localName=attachment attributes=[]]
         8 	--> [nsrss] level=item localName=image_title attributes=[]]
         8 	--> [nsrss] level=item localName=editor attributes=[]]
         8 	--> [nsrss] level=item localName=image_height attributes=[]]
         8 	--> [nsrss] level=item localName=image_url attributes=[]]
         8 	--> [nsrss] level=feed localName=explict attributes=[]]
         8 	--> [nsrss] level=feed localName=link attributes=[href]]
         8 	--> [nsrss] level=item localName=a_id attributes=[]]
         8 	--> [nsrss] level=item localName=has_video attributes=[]]
         8 	--> [nsrss] level=feed localName=itunes_category attributes=[text]]
         8 	--> [nsrss] level=item localName=div attributes=[]]
         8 	--> [nsrss] level=feed localName=artwork attributes=[]]
         8 	--> [nsrss] level=item localName=has_audio attributes=[]]
         7 	--> [nsrss] level=item localName=youtubelist attributes=[]]
         7 	--> [nsrss] level=feed localName=ituneslink attributes=[]]
         7 	--> [nsrss] level=feed localName=updated attributes=[]]
         7 	--> [nsrss] level=item localName=nsfw attributes=[]]
         7 	--> [nsrss] level=feed localName=title attributes=[type]]
         7 	--> [nsrss] level=item localName=recipe attributes=[]]
         7 	--> [nsrss] level=item localName=itunes attributes=[duration]]
         7 	--> [nsrss] level=item localName=enclosure attributes=[path, lastdownloaderror, length, downloadstatus, type, url]]
         7 	--> [nsrss] level=feed localName=new-feed-url attributes=[]]
         7 	--> [nsrss] level=item localName=playlist attributes=[]]
         6 	--> [nsrss] level=feed localName=params attributes=[]]
         6 	--> [nsrss] level=feed localName=image attributes=[link, title, url]]
         6 	--> [nsrss] level=feed localName=link attributes=[rel, href, type, title]]
         6 	--> [nsrss] level=item localName=iframe attributes=[scrolling, src, allowTransparency, frameborder, style]]
         6 	--> [nsrss] level=feed localName=methodName attributes=[]]
         6 	--> [nsrss] level=feed localName=a attributes=[name]]
         6 	--> [nsrss] level=feed localName=api attributes=[apiLink, name, blogID, preferred]]
         6 	--> [nsrss] level=item localName=a attributes=[href, title, target]]
         6 	--> [nsrss] level=item localName=Author attributes=[]]
         6 	--> [nsrss] level=feed localName=methodCall attributes=[]]
         5 	--> [nsrss] level=feed localName=lang attributes=[]]
         5 	--> [nsrss] level=item localName=div attributes=[id, dir, class]]
         5 	--> [nsrss] level=feed localName=height attributes=[]]
         5 	--> [nsrss] level=feed localName=logo attributes=[]]
         5 	--> [nsrss] level=item localName=span attributes=[id, class]]
         5 	--> [nsrss] level=item localName=link attributes=[rel, id, href, media, type]]
         5 	--> [nsrss] level=item localName=picture attributes=[]]
         5 	--> [nsrss] level=item localName=image attributes=[src, alt]]
         5 	--> [nsrss] level=item localName=a attributes=[href, class, target]]
         5 	--> [nsrss] level=item localName=iframe attributes=[marginwidth, scrolling, src, width, frameborder, marginheight, height]]
         5 	--> [nsrss] level=item localName=B attributes=[]]
         5 	--> [nsrss] level=feed localName=width attributes=[]]
         4 	--> [nsrss] level=item localName=docs attributes=[]]
         4 	--> [nsrss] level=item localName=streamUrlx attributes=[]]
         4 	--> [nsrss] level=item localName=img attributes=[src]]
         4 	--> [nsrss] level=feed localName=PubDate attributes=[]]
         4 	--> [nsrss] level=item localName=link attributes=[rel, media, href, type]]
         4 	--> [nsrss] level=item localName=mp3duration attributes=[]]
         4 	--> [nsrss] level=item localName=div attributes=[onclick, class]]
         4 	--> [nsrss] level=feed localName=a attributes=[href, title]]
         4 	--> [nsrss] level=item localName=del attributes=[]]
         4 	--> [nsrss] level=feed localName=ad_url attributes=[]]
         4 	--> [nsrss] level=item localName=mp3pass attributes=[]]
         4 	--> [nsrss] level=item localName=span attributes=[property]]
         4 	--> [nsrss] level=feed localName=ad_image attributes=[]]
         4 	--> [nsrss] level=item localName=docName attributes=[]]
         4 	--> [nsrss] level=item localName=docType attributes=[]]
         4 	--> [nsrss] level=item localName=newsid attributes=[]]
         4 	--> [nsrss] level=item localName=docUrl attributes=[]]
         4 	--> [nsrss] level=feed localName=description attributes=[type]]
         4 	--> [nsrss] level=feed localName=feedcss attributes=[]]
         4 	--> [nsrss] level=item localName=document attributes=[]]
         4 	--> [nsrss] level=item localName=ol attributes=[]]
         4 	--> [nsrss] level=feed localName=artist attributes=[]]
         4 	--> [nsrss] level=feed localName=copyright attributes=[type]]
         3 	--> [nsrss] level=feed localName=ad_amazon_url attributes=[]]
         3 	--> [nsrss] level=feed localName=ad_baidu_url attributes=[]]
         3 	--> [nsrss] level=feed localName=em attributes=[]]
         3 	--> [nsrss] level=feed localName=schedule attributes=[]]
         3 	--> [nsrss] level=feed localName=link attributes=[rel, href, lang, title]]
         3 	--> [nsrss] level=feed localName=xmlUrl attributes=[]]
         3 	--> [nsrss] level=item localName=feature_title attributes=[]]
         3 	--> [nsrss] level=item localName=feature_image attributes=[]]
         3 	--> [nsrss] level=feed localName=ad_samsung_url attributes=[]]
         3 	--> [nsrss] level=item localName=itunefeeddesc attributes=[]]
         3 	--> [nsrss] level=feed localName=LastBuildDate attributes=[]]
         3 	--> [nsrss] level=item localName=strong attributes=[style]]
         3 	--> [nsrss] level=item localName=price attributes=[]]
         3 	--> [nsrss] level=item localName=p attributes=[style]]
         3 	--> [nsrss] level=item localName=div attributes=[dir]]
         3 	--> [nsrss] level=item localName=page_info attributes=[]]
         3 	--> [nsrss] level=item localName=playlist_kevin attributes=[]]
         3 	--> [nsrss] level=item localName=maintitle attributes=[]]
         3 	--> [nsrss] level=item localName=playlist_trevor attributes=[]]
         3 	--> [nsrss] level=feed localName=a attributes=[href, target]]
         3 	--> [nsrss] level=feed localName=link attributes=[rel, href]]
         3 	--> [nsrss] level=feed localName=enclosure attributes=[url]]
         3 	--> [nsrss] level=feed localName=img_banner attributes=[]]
         3 	--> [nsrss] level=item localName=hr attributes=[]]
         3 	--> [nsrss] level=item localName=content1 attributes=[]]
         3 	--> [nsrss] level=feed localName=Category attributes=[]]
         3 	--> [nsrss] level=item localName=P attributes=[]]
         3 	--> [nsrss] level=item localName=bonus attributes=[]]
         3 	--> [nsrss] level=item localName=UL attributes=[]]
         3 	--> [nsrss] level=feed localName=channel attributes=[lastdownloaderror]]
         3 	--> [nsrss] level=item localName=feature_description attributes=[]]
         3 	--> [nsrss] level=feed localName=rating attributes=[scheme]]
         3 	--> [nsrss] level=feed localName=presenters attributes=[]]
         2 	--> [nsrss] level=feed localName=homePageLink attributes=[]]
         2 	--> [nsrss] level=item localName=vidId attributes=[]]
         2 	--> [nsrss] level=feed localName=menu_slug attributes=[]]
         2 	--> [nsrss] level=feed localName=div attributes=[class]]
         2 	--> [nsrss] level=feed localName=link attributes=[atom]]
         2 	--> [nsrss] level=feed localName=updateFrequency attributes=[]]
         2 	--> [nsrss] level=feed localName=code attributes=[]]
         2 	--> [nsrss] level=feed localName=podcastImage attributes=[href]]
         2 	--> [nsrss] level=feed localName=api attributes=[apiLink, name, preferred]]
         2 	--> [nsrss] level=item localName=enclosure attributes=[lengh, type, url]]
         2 	--> [nsrss] level=item localName=img attributes=[sizes, src, alt, width, title, srcset, class, height]]
         2 	--> [nsrss] level=feed localName=itunes-category attributes=[text]]
         2 	--> [nsrss] level=feed localName=description attributes=[space]]
         2 	--> [nsrss] level=feed localName=date attributes=[]]
         2 	--> [nsrss] level=feed localName=website attributes=[]]
         2 	--> [nsrss] level=feed localName=engineName attributes=[]]
         2 	--> [nsrss] level=feed localName=subcategory attributes=[]]
         2 	--> [nsrss] level=item localName=italics attributes=[]]
         2 	--> [nsrss] level=feed localName=contributor attributes=[]]
         2 	--> [nsrss] level=feed localName=apis attributes=[]]
         2 	--> [nsrss] level=item localName=featured_image_large attributes=[]]
         2 	--> [nsrss] level=feed localName=podcastThumb attributes=[href]]
         2 	--> [nsrss] level=item localName=source attributes=[length, type, url]]
         2 	--> [nsrss] level=item localName=p attributes=[dir]]
         2 	--> [nsrss] level=feed localName=creator attributes=[]]
         2 	--> [nsrss] level=feed localName=title attributes=[author]]
         2 	--> [nsrss] level=feed localName=engineLink attributes=[]]
         2 	--> [nsrss] level=feed localName=nav_menu_locations attributes=[]]
         2 	--> [nsrss] level=item localName=chapters attributes=[version]]
         2 	--> [nsrss] level=item localName=div attributes=[data-show-faces, data-colorscheme, data-layout, class, data-href]]
         2 	--> [nsrss] level=feed localName=description attributes=[lang]]
         2 	--> [nsrss] level=feed localName=image attributes=[width, height]]
         2 	--> [nsrss] level=feed localName=menu_name attributes=[]]
         2 	--> [nsrss] level=feed localName=updatePeriod attributes=[]]
         2 	--> [nsrss] level=item localName=strike attributes=[]]
         2 	--> [nsrss] level=feed localName=a attributes=[rel, href]]
         2 	--> [nsrss] level=item localName=featured_image_thumb attributes=[]]
         2 	--> [nsrss] level=item localName=chapter attributes=[starttime]]
         2 	--> [nsrss] level=feed localName=enclosure attributes=[type, url]]
         2 	--> [nsrss] level=feed localName=service attributes=[]]
         2 	--> [nsrss] level=feed localName=BR attributes=[]]
         2 	--> [nsrss] level=item localName=img attributes=[border, src, alt, style, title]]
         2 	--> [nsrss] level=item localName=s attributes=[]]
         2 	--> [nsrss] level=feed localName=keyword attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_pages attributes=[]]
         1 	--> [nsrss] level=item localName=emdash attributes=[]]
         1 	--> [nsrss] level=feed localName=itunes attributes=[href]]
         1 	--> [nsrss] level=feed localName=h2 attributes=[class]]
         1 	--> [nsrss] level=feed localName=iphone_ipod attributes=[]]
         1 	--> [nsrss] level=feed localName=desciption attributes=[]]
         1 	--> [nsrss] level=item localName=small attributes=[]]
         1 	--> [nsrss] level=feed localName=uri attributes=[]]
         1 	--> [nsrss] level=feed localName=tjnextshowtitle attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_show_post_navigation attributes=[]]
         1 	--> [nsrss] level=feed localName=note attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_show_related_post attributes=[]]
         1 	--> [nsrss] level=feed localName=subconten2 attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_text attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_fadein_effect attributes=[]]
         1 	--> [nsrss] level=feed localName=subconten3 attributes=[]]
         1 	--> [nsrss] level=item localName=BR attributes=[]]
         1 	--> [nsrss] level=feed localName=feed attributes=[]]
         1 	--> [nsrss] level=feed localName=head attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_frontpage attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_mobile_icon attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_rtl attributes=[]]
         1 	--> [nsrss] level=feed localName=subconten1 attributes=[]]
         1 	--> [nsrss] level=feed localName=div attributes=[style, class]]
         1 	--> [nsrss] level=feed localName=channel attributes=[link, rel, type]]
         1 	--> [nsrss] level=item localName=xerosocial attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_menu_item_text attributes=[]]
         1 	--> [nsrss] level=item localName=punDate attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=win8 attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_sidebar_widget_title attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_heading attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_excerpt_list attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_show_post_author_information attributes=[]]
         1 	--> [nsrss] level=feed localName=image attributes=[url]]
         1 	--> [nsrss] level=item localName=SUP attributes=[]]
         1 	--> [nsrss] level=feed localName=manageEditor attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_scheme attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_footer_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=subject attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_text_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=isFeed attributes=[]]
         1 	--> [nsrss] level=item localName=enclosure attributes=[length, typ.mp3e, url]]
         1 	--> [nsrss] level=feed localName=odeo attributes=[]]
         1 	--> [nsrss] level=feed localName=googleplus attributes=[img, url]]
         1 	--> [nsrss] level=feed localName=atom attributes=[linkhref, rel, type]]
         1 	--> [nsrss] level=feed localName=theme_option_share_buttons attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_nav_menu attributes=[]]
         1 	--> [nsrss] level=feed localName=odToken attributes=[]]
         1 	--> [nsrss] level=feed localName=pcast attributes=[]]
         1 	--> [nsrss] level=item localName=ink attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_posts_showmeta attributes=[]]
         1 	--> [nsrss] level=item localName=font attributes=[face, size]]
         1 	--> [nsrss] level=feed localName=channel attributes=[lastdownloaderror, lang]]
         1 	--> [nsrss] level=feed localName=number attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_meta_demo_site attributes=[]]
         1 	--> [nsrss] level=feed localName=rsslink attributes=[img, url]]
         1 	--> [nsrss] level=item localName=img attributes=[src, class]]
         1 	--> [nsrss] level=feed localName=img attributes=[src, alt, width, style, height]]
         1 	--> [nsrss] level=feed localName=theme_option_color_auxiliary_content attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_bordered attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_head_left attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_front_page attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_custom_logo_for_loading attributes=[]]
         1 	--> [nsrss] level=feed localName=executiveProducer attributes=[]]
         1 	--> [nsrss] level=feed localName=i attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_categories attributes=[]]
         1 	--> [nsrss] level=item localName=vid attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_footer_widget_page attributes=[]]
         1 	--> [nsrss] level=feed localName=maintitle attributes=[]]
         1 	--> [nsrss] level=feed localName=href attributes=[]]
         1 	--> [nsrss] level=feed localName=h1 attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_menu_activated_item_text attributes=[]]
         1 	--> [nsrss] level=feed localName=ipad attributes=[]]
         1 	--> [nsrss] level=item localName=itemimage attributes=[]]
         1 	--> [nsrss] level=item localName=episode_mp3 attributes=[]]
         1 	--> [nsrss] level=feed localName=meta attributes=[http-equiv, content]]
         1 	--> [nsrss] level=feed localName=ipad_ad_url attributes=[]]
         1 	--> [nsrss] level=item localName=div attributes=[id, class]]
         1 	--> [nsrss] level=feed localName=theme_option_footer_text_color attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_descriptions_your_message attributes=[]]
         1 	--> [nsrss] level=item localName=dzsap_footer_vpconfig attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_custom_favicon attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_custom_footer_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_show_social_medias attributes=[]]
         1 	--> [nsrss] level=item localName=summary attributes=[type]]
         1 	--> [nsrss] level=feed localName=iphone_ad_image attributes=[]]
         1 	--> [nsrss] level=feed localName=ipad_ad_image attributes=[]]
         1 	--> [nsrss] level=feed localName=category_description attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_title_list attributes=[]]
         1 	--> [nsrss] level=item localName=div attributes=[dir, class]]
         1 	--> [nsrss] level=feed localName=theme_option_descriptions_comment_title attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_footer_instagram attributes=[]]
         1 	--> [nsrss] level=feed localName=site attributes=[]]
         1 	--> [nsrss] level=feed localName=Image attributes=[]]
         1 	--> [nsrss] level=feed localName=urlItems attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_header_topbar_left_text attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_footer_widget_title_color attributes=[]]
         1 	--> [nsrss] level=feed localName=programarid attributes=[]]
         1 	--> [nsrss] level=item localName=dzsap_footer_type attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_footer_widget_post attributes=[]]
         1 	--> [nsrss] level=item localName=h2 attributes=[style]]
         1 	--> [nsrss] level=item localName=h1 attributes=[style]]
         1 	--> [nsrss] level=item localName=kink attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_lines attributes=[]]
         1 	--> [nsrss] level=feed localName=desription attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_bg_page_post attributes=[]]
         1 	--> [nsrss] level=feed localName=app_ads attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_style_heading attributes=[]]
         1 	--> [nsrss] level=feed localName=published attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_bordered attributes=[]]
         1 	--> [nsrss] level=feed localName=footertitle attributes=[]]
         1 	--> [nsrss] level=feed localName=websitelink attributes=[]]
         1 	--> [nsrss] level=feed localName=copywrite attributes=[]]
         1 	--> [nsrss] level=feed localName=image attributes=[link, href]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_search_button attributes=[]]
         1 	--> [nsrss] level=feed localName=android_10inch attributes=[]]
         1 	--> [nsrss] level=feed localName=script attributes=[charset, src, type]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_back_to_top attributes=[]]
         1 	--> [nsrss] level=feed localName=titleshort attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_descriptions_load_more attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_calendar attributes=[]]
         1 	--> [nsrss] level=item localName=bitrate attributes=[]]
         1 	--> [nsrss] level=feed localName=aranova attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_footer_bottom_right attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_descriptions_comment_submit attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_menu_shown attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_style_post_title attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_descriptions_read_more attributes=[]]
         1 	--> [nsrss] level=feed localName=android_7inch attributes=[]]
         1 	--> [nsrss] level=feed localName=twitter attributes=[img, url]]
         1 	--> [nsrss] level=feed localName=channel attributes=[series, expected, used]]
         1 	--> [nsrss] level=feed localName=img attributes=[src, class]]
         1 	--> [nsrss] level=feed localName=theme_widgets attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_scheme_current attributes=[]]
         1 	--> [nsrss] level=feed localName=image attributes=[alt]]
         1 	--> [nsrss] level=feed localName=refURL attributes=[]]
         1 	--> [nsrss] level=feed localName=img attributes=[src, alt, width, class, height]]
         1 	--> [nsrss] level=feed localName=theme_option_show_social attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_footer_widget_page attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_selected_text_bg attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_show_social_medias_blank attributes=[]]
         1 	--> [nsrss] level=item localName=font attributes=[color]]
         1 	--> [nsrss] level=feed localName=android_phone attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_footer_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_post_page_widget_tit attributes=[]]
         1 	--> [nsrss] level=item localName=div attributes=[data-tooltip, tabindex, id, class]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_title_archive attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_family_main attributes=[]]
         1 	--> [nsrss] level=feed localName=encoded attributes=[]]
         1 	--> [nsrss] level=item localName=htmlData attributes=[]]
         1 	--> [nsrss] level=item localName=link attributes=[href]]
         1 	--> [nsrss] level=feed localName=source attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_style_main attributes=[]]
         1 	--> [nsrss] level=feed localName=maxItems attributes=[]]
         1 	--> [nsrss] level=feed localName=apple attributes=[img, url]]
         1 	--> [nsrss] level=item localName=cite attributes=[]]
         1 	--> [nsrss] level=feed localName=generator attributes=[uri]]
         1 	--> [nsrss] level=feed localName=theme_option_hide_category_on_post_page attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_post_page_content attributes=[]]
         1 	--> [nsrss] level=item localName=enclosure attributes=[length, type]]
         1 	--> [nsrss] level=feed localName=theme_option_custom_logo_top attributes=[]]
         1 	--> [nsrss] level=feed localName=statement attributes=[]]
         1 	--> [nsrss] level=feed localName=tag attributes=[]]
         1 	--> [nsrss] level=feed localName=page_on_front attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_style_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_family_post_title attributes=[]]
         1 	--> [nsrss] level=feed localName=ManagingEditor attributes=[]]
         1 	--> [nsrss] level=feed localName=podmaster attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_family_menu attributes=[]]
         1 	--> [nsrss] level=item localName=div attributes=[align]]
         1 	--> [nsrss] level=feed localName=content1 attributes=[]]
         1 	--> [nsrss] level=feed localName=content2 attributes=[]]
         1 	--> [nsrss] level=item localName=script attributes=[language]]
         1 	--> [nsrss] level=feed localName=youtube attributes=[img, url]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_post_title attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_family_heading attributes=[]]
         1 	--> [nsrss] level=item localName=iframe attributes=[marginwidth, scrolling, src, width, frameborder, style, marginheight, height]]
         1 	--> [nsrss] level=item localName=praecis attributes=[]]
         1 	--> [nsrss] level=item localName=ID3 attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_copyright attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_footer_widget_bg_color attributes=[]]
         1 	--> [nsrss] level=feed localName=instructions attributes=[]]
         1 	--> [nsrss] level=feed localName=iphone_ad_url attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_enable_meta_post_page attributes=[]]
         1 	--> [nsrss] level=feed localName=link attributes=[rel, href, type]]
         1 	--> [nsrss] level=feed localName=iframe attributes=[id]]
         1 	--> [nsrss] level=feed localName=theme_option_text_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_theme_main attributes=[]]
         1 	--> [nsrss] level=feed localName=a attributes=[style, href, class]]
         1 	--> [nsrss] level=feed localName=facebook attributes=[img, url]]
         1 	--> [nsrss] level=feed localName=widget_tag_cloud attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_font_size_logo attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_search attributes=[]]
         1 	--> [nsrss] level=item localName=enclosure attributes=[listenUrl, dateRecorded, artist, imageUrl, part, length, bandWebsite, season, radioSong, type, episodeNumber, url]]
         1 	--> [nsrss] level=feed localName=widget_recent_posts attributes=[]]
         1 	--> [nsrss] level=feed localName=icon attributes=[]]
         1 	--> [nsrss] level=item localName=sub attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_custom_css attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_sidebar_content_color attributes=[]]
         1 	--> [nsrss] level=feed localName=channel attributes=[base]]
         1 	--> [nsrss] level=feed localName=itunes attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_footer_widget_content_color attributes=[]]
         1 	--> [nsrss] level=feed localName=img attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_rss attributes=[]]
         1 	--> [nsrss] level=feed localName=editor attributes=[]]
         1 	--> [nsrss] level=feed localName=in_dash_app_pro attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_content_text attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_color_second_auxiliary attributes=[]]
         1 	--> [nsrss] level=feed localName=theme_option_descriptions_related_posts_title attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_meta attributes=[]]
         1 	--> [nsrss] level=feed localName=widget_archives attributes=[]]
         1 	--> [nsrss] level=item localName=en
*/
