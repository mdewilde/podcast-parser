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

import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Category;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.models.Person;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Strings;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * @see https://help.apple.com/itc/podcasts_connect/#/itcb54353390
 */
public class ITunes implements Namespace {

	private static final Logger logger = LoggerFactory.getLogger(ITunes.class);

	private static final String NAME = "http://www.itunes.com/dtds/podcast-1.0.dtd";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of(
			"http://www.itunes.com/dtds/podcast-1.0.dtd/", 
			"https://www.itunes.com/dtds/podcast-1.0.dtd", 
			"http://itunes.com/dtds/podcast-1.0.dtd", 
			"//www.itunes.com/dtds/podcast-1.0.dtd", 
			"itunes");

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		String localName = ctx.getReader().getLocalName();
		switch (localName) {
		case "author":
			ctx.getFeed().addAuthor(parseAuthor(ctx));
			break;
		case "block":
			ctx.getFeed().setBlock(parseBlock(ctx));
			break;
		case "category":
			ctx.getFeed().addCategory(parseCategory(ctx));
			break;
		case "complete":
			ctx.getFeed().setComplete(parseComplete(ctx));
			break;
		case "explicit":
			ctx.getFeed().getRating().setExplicit(ctx.getElementText());
			break;
		case "image":
			ctx.getFeed().addImage(parseImage(ctx));
			break;
		case "link":
			ctx.getFeed().addLink(parseLink(ctx));
			break;
		case "keywords":
			ctx.getFeed().addKeywords(parseKeywords(ctx));
			break;
		case "new-feed-url":
			Link link = new Link();
			link.setHref(ctx.getElementText());
			link.setTitle("new-feed-url");
			ctx.getFeed().addLink(link);
			break;
		case "owner":
			ctx.getFeed().setOwner(parseOwner(ctx));
			return;
		case "subtitle":
			// The contents of this tag are shown in the Description column in iTunes. 
			// The subtitle displays best if it is only a few words long.
			String subtitle = ctx.getElementText();
			if (StringUtils.isNotBlank(subtitle)) {
				ctx.getFeed().setSubtitle(subtitle);
			}
			break;
		case "summary":
			String summary = ctx.getElementText();
			if (StringUtils.isNotBlank(summary)) {
				ctx.getFeed().setSummary(summary);
			}
			break;
		case "type":
			String type = ctx.getElementText();
			if (StringUtils.isNotBlank(type)) {
				ctx.getFeed().setType(type);
			}
			break;
		default : 
			logger.warn("iTunes {} @FEED --> [ATTRIBUTES {}] [TEXT {}]", localName, Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		}

	}

	/**
	 * @param item
	 *            {@link Item} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 * @throws XMLStreamException
	 */
	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		String localName = ctx.getReader().getLocalName();
		switch (localName) {
		case "author":
			Person person = new Person();
			person.setEmail(ctx.getElementText());
			item.addAuthor(person);
			return;
		case "block":
			item.setBlock(parseBlock(ctx));
			return;
		case "category":
			item.addCategory(parseCategory(ctx));
			return;
		case "description":
			String description = ctx.getElementText();
			if (Strings.isNotBlank(description)) {
				item.setDescription(description);
			}
			return;
		case "duration":
			String duration = ctx.getElementText();
			item.setDuration(duration);
			return;
		case "email":
			String email = ctx.getElementText();
			if (Strings.isNotBlank(email)) {
				Person emailPerson = new Person();
				emailPerson.setEmail(email);
				item.addAuthor(emailPerson);
			}
			return;
		case "episode":
			String episode = ctx.getElementText();
			if (Strings.isNotBlank(episode)) {
				item.setEpisode(episode);
			}
			return;
		case "episodeType":
			item.setEpisodeType(ctx.getElementText());
			return;
		case "explicit":
			item.getRating().setExplicit(ctx.getElementText());
			return;
		case "guid":
			// This is a global RSS item, but many publishers add it to the iTunes namespace.
			item.setGuid(ctx.getElementText());
			return;
		case "image":
			item.addImage(parseImage(ctx));
			return;
		case "keywords":
			item.addKeywords(parseKeywords(ctx));
			return;
		case "order":
			item.setOrder(Integer.parseInt(ctx.getElementText()));
			return;
		case "provider":
			item.setProvider(ctx.getElementText());
			return;
		case "subtitle":
			String subtitle = ctx.getElementText();
			if (Strings.isNotBlank(subtitle)) {
				item.setSubtitle(subtitle);
			}
			return;
		case "season":
			String season = ctx.getElementText();
			if (Strings.isNotBlank(season)) {
				item.setSeason(season);
			}
			return;
		case "summary":
			String summary = ctx.getElementText();
			if (Strings.isNotBlank(summary)) {
				item.setSummary(summary);
			}
			return;
		case "title":
			String title = ctx.getElementText();
			if (Strings.isNotBlank(title)) {
				item.setTitle(title);
			}
			return;
		case "album":
			// fallthrough intended
		case "isClosedCaptioned":
			// fallthrough intended
		default : 
			Namespace.super.process(ctx, item);
			return;
		}

	}

	/**
	 * If the {@link <itunes:author>} tag isn’t present at the channel level, Apple Podcasts uses the
	 * contents of the {@link <itunes:author>} tag at the item level. If {@link <itunes:author>} isn’t
	 * present at the item level, Apple Podcasts uses the contents of the {@code <managingEditor>} tag.
	 * 
	 * @param ctx
	 * @return
	 * @throws XMLStreamException
	 */
	private Person parseAuthor(PodParseContext ctx) throws XMLStreamException {
		Person person = new Person();
		person.setName(ctx.getElementText());
		return person;
	}
	
	/**
	 * Use this inside a <channel> element to prevent the entire podcast
	 * from appearing in the iTunes Podcast directory. Use this inside
	 * an <item> element to prevent that episode from appearing in the
	 * iTunes Podcast directory. For example, you may want a specific
	 * episode blocked from iTunes if it's content might cause the feed
	 * to be removed from iTunes.
	 * 
	 * If this tag is present and set to "yes" (case insensitive), that
	 * means to block the feed or the episode. If the tag's value is any
	 * other value, including empty string, it's indicated as a signal
	 * to unblock the feed or episode. At the feed level, if there is no
	 * block tag, then the block status of the feed is left unchanged.
	 * At the episode level, if there is no block tag, it is the same as
	 * if a block=no were present.
	 */
	private boolean parseBlock(PodParseContext ctx) throws XMLStreamException {
		return "yes".equalsIgnoreCase(ctx.getElementText());
	}
	
	/*
	 * When browsing Podcasts in the iTunes Music Store, categories are shown in
	 * the 2nd column and subcategories are shown in the 3rd column. Not all
	 * categories have subcategories.
	 * 
	 * Categories and subcategories can be specified as follows. Use a top level
	 * <itunes:category> to specify the browse category, and a nested
	 * <itunes:category> to specify the browse subcategory. Choose from the
	 * existing categories and subcategories on the iTunes Music Store. Be sure
	 * to properly escape ampersands. Note that multiple categories are allowed.
	 * Single category:
	 * 
	 * <itunes:category text="Audio Blogs" />
	 * 
	 * Category with ampersand:
	 * 
	 * <itunes:category text="Movies &amp; Television" />
	 * 
	 * Category with Subcategory:
	 * 
	 * <itunes:category text="Arts &amp; Entertainment"> <itunes:category
	 * text="Games" /> </itunes:category>
	 * 
	 * Entry with multiple categories:
	 * 
	 * <itunes:category text="Arts &amp; Entertainment"> <itunes:category
	 * text="Games" /> </itunes:category> <itunes:category text="Technology">
	 * <itunes:category text="Computers" /> </itunes:category>
	 */
	private Category parseCategory(PodParseContext ctx) throws XMLStreamException {
		String text = ctx.getAttribute("text");
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.START_ELEMENT:
				if ("category".equals(ctx.getReader().getLocalName())) {
					String subcategory = ctx.getAttribute("text");
					return new Category().setName(text).setSubcategory(subcategory);
				} else {
					// logger.warn("unexpected element {} inside itunes:category element", reader.getLocalName());
					break;
				}
			case XMLStreamConstants.END_ELEMENT:
				return new Category().setName(text);
			}
		}
		return new Category().setName(text);
	}

	/**
	 * The podcast update status.
	 * 
	 * Specifying the {@link <itunes:complete>} tag with a Yes value indicates that a podcast is complete and
	 * you will not post any more episodes in the future. This tag is only supported at the channel
	 * level (podcast).
	 * 
	 * Specifying any value other than Yes has no effect.
	 */
	private boolean parseComplete(PodParseContext ctx) throws XMLStreamException {
		return "yes".equalsIgnoreCase(ctx.getElementText());
	}
	
	/*
	 * This tag specifies the artwork for your podcast. Put the url to the image
	 * in the href attribute. iTunes prefers square .jpg images that are at
	 * least 300 x 300 pixels, which is different than what is specified for the
	 * standard RSS image tag.
	 * 
	 * 
	 * iTunes supports images in JPEG and PNG formats. The url must end in
	 * ".jpg" or ".png". If the itunes:image tag is not present, iTunes will use
	 * the contents of the RSS image tag.
	 */
	private Image parseImage(PodParseContext ctx) throws XMLStreamException {
		return new Image()
				.setUrl(ctx.getAttribute("href"));
	}

	private List<String> parseKeywords(PodParseContext ctx) throws XMLStreamException {
		return Strings.splitOnComma(ctx.getElementText());
	}

	private Person parseOwner(PodParseContext ctx) throws XMLStreamException {
		Person person = new Person();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("owner".equals(ctx.getReader().getLocalName())) {
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
				}
			}
		}
		return person;
	}

	// XXX duplicate code -> see Atom
	private Link parseLink(PodParseContext ctx) throws XMLStreamException {
		Link link = new Link();
		Attributes.get("href").from(ctx.getReader()).ifPresent(link::setHref);
		Attributes.get("rel").from(ctx.getReader()).ifPresent(link::setRel);
		Attributes.get("type").from(ctx.getReader()).ifPresent(link::setType);
		Attributes.get("hreflang").from(ctx.getReader()).ifPresent(link::setHreflang);
		Attributes.get("title").from(ctx.getReader()).ifPresent(link::setTitle);
		Attributes.get("length").from(ctx.getReader()).ifPresent(link::setLength);
		return link;
	}
	
}