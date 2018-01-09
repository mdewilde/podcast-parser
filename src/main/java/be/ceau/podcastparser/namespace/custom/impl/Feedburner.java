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

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Image;
import be.ceau.podcastparser.models.support.Link;
import be.ceau.podcastparser.models.support.TypedString;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

/**
 * @see <a href=
 *      "http://web.archive.org/web/20110822032923/http://code.google.com/apis/feedburner/feedburner_namespace_reference.html">FeedBurner
 *      Namespace Reference</a>
 * @see <a href=
 *      "http://intertwingly.net/blog/2007/11/20/RFC-FeedBurner-Namespace-Documentation">FeedBurner
 *      Namespace documentation</a>
 * @see <a href=
 *      "http://web.archive.org/web/20110822034410/http://code.google.com:80/apis/feedburner/awareness_api.html">FeedBurner
 *      Awareness documentation</a>
 */
public class Feedburner implements Namespace {

	private static final String NAME = "http://rssnamespace.org/feedburner/ext/1.0";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "awareness":
			ctx.getFeed().addLink(parseAwareness(ctx));
			break;
		case "browserFriendly":
			ctx.getFeed().setBrowserFriendly(parseBrowserFriendly(ctx));
			break;
		case "emailServiceId":
			// child text is integer
			/*
			 * The feedburner:emailServiceId element is a unique identifier for feeds that use the Email
			 * Subscriptions service. It is only present if the feed publisher has enabled the Email
			 * Subscriptions service.
			 */
			Namespace.super.process(ctx);
			break;
		case "feedburnerHostname":
			// child text is URI
			/*
			 * The feedburner:feedburnerHostname element is the base URI of the site to be used for verifying
			 * email subscriptions. It is only present if the feed publisher has enabled the Email Subscriptions
			 * service.
			 * 
			 * FeedBurner email is a service that allows publishers to deliver their feed content to subscribers
			 * via email.
			 */
			Namespace.super.process(ctx);
			break;
		case "feedFlare":
			ctx.getFeed().addImage(parseFeedFlare(ctx));
			break;
		case "info":
			/*
			 * The feedburner:info element is an element that appears in every FeedBurner feed. The uri
			 * attribute is required, and indicates the feed URI chosen by the publisher where the feed can be
			 * accessed under feeds.feedburner.com.
			 */
			Attributes.get("uri").from(ctx.getReader()).ifPresent(uri -> {
				Link link = new Link();
				link.setHref("https://feeds.feedburner.com/" + uri);
				link.setRel("feedburner");
				ctx.getFeed().addLink(link);
			});
			break;
		default:
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "awareness":
			item.addLink(parseAwareness(ctx));
			break;
		case "origEnclosureLink":
			item.addLink(parseOrigEnclosureLink(ctx));
			break;
		case "origLink":
			item.addLink(parseOrigLink(ctx));
			break;
		default:
			Namespace.super.process(ctx, item);
			break;
		}
	}

	/*
	 * Feed Awareness describes the extent and frequency with which a publisher's feed and its content
	 * items are consumed, clicked on, and referred to by independent sources (i.e., "syndicated").
	 * 
	 * As a feed-level element, the feedburner:awareness element points to current basic feed awareness
	 * data for the feed. It is only present if the feed publisher has enabled the Awareness API for the
	 * feed.
	 * 
	 * The feedburner:awareness element can appear 0 or 1 times as a child of <atom:feed> or <channel>.
	 * The value of this element is an http or https URI. The URI can not be relative. Leading and
	 * trailing whitespace is not significant.
	 *
	 * As an item-level element, the feedburner:awareness element points to current item awareness data
	 * for the item. It is only present if the feed publisher has enabled the Awareness API for the
	 * feed.
	 */
	private Link parseAwareness(PodcastParserContext ctx) throws XMLStreamException {
		Link link = new Link();
		link.setHref(ctx.getElementText());
		link.setHref("feedburner:awareness");
		return link;
	}

	/*
	 * The feedburner:browserFriendly element is simply a human-readable description explaining the
	 * concept of syndication feeds. In older browsers, clicking a syndicated feed causes the browser to
	 * display a raw XML dump to the end user. This is clearly undesirable, so FeedBurner uses
	 * client-side stylesheets to reformat the feed into something more friendly. The content of this
	 * element is a message displayed to the end user to explain what they just clicked on.
	 */
	private TypedString parseBrowserFriendly(PodcastParserContext ctx) throws XMLStreamException {
		TypedString typedString = new TypedString();
		typedString.setText(ctx.getElementText());
		return typedString;
	}

	/*
	 * The feedburner:feedFlare element describes a piece of "flare" that is displayed on the feed's
	 * landing page to allow the end user to subscribe to the blog with a variety of online services. If
	 * a publisher has created custom BrowserFriendly chicklets and added them to a feed, these
	 * chicklets will also appear in the feed as feedburner:feedFlare elements.
	 * 
	 * The href attribute is a link to a feed subscription service. The src attribute is a link to an
	 * image. The element also has text content, which is used as alternate text for the image.
	 */
	private Image parseFeedFlare(PodcastParserContext ctx) throws XMLStreamException {
		Image image = new Image();
		image.setUrl(ctx.getAttribute("src"));
		image.setLink(ctx.getAttribute("href"));
		image.setTitle(ctx.getElementText());
		return image;
	}

	/*
	 * The feedburner:origEnclosureLink element points to the original URI of an enclosure. It is only
	 * present in podcasting feeds, and only if the feed publisher has opted to track enclosure
	 * clickthroughs.
	 */
	private Link parseOrigEnclosureLink(PodcastParserContext ctx) throws XMLStreamException {
		Link link = new Link();
		link.setHref(ctx.getElementText());
		link.setRel("origEnclosureLink");
		return link;
	}

	/*
	 * The feedburner:origLink element points to the original URI of an item. It is only present if the
	 * feed publisher has opted to track item clickthroughs.
	 */
	private Link parseOrigLink(PodcastParserContext ctx) throws XMLStreamException {
		Link link = new Link();
		link.setHref(ctx.getElementText());
		link.setRel("origLink");
		return link;
	}

}
