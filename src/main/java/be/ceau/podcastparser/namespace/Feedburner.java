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
package be.ceau.podcastparser.namespace;

import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * 
 * @see http://web.archive.org/web/20110822032923/http://code.google.com/apis/feedburner/feedburner_namespace_reference.html
 * @see http://intertwingly.net/blog/2007/11/20/RFC-FeedBurner-Namespace-Documentation
 * @see http://web.archive.org/web/20110822034410/http://code.google.com:80/apis/feedburner/awareness_api.html
 */
public class Feedburner implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://rssnamespace.org/feedburner/ext/1.0");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "awareness":
			// child text is URI
			/*
			 * As a feed-level element, the feedburner:awareness element points
			 * to current basic feed awareness data for the feed. It is only
			 * present if the feed publisher has enabled the Awareness API for
			 * the feed.
			 * 
			 * The feedburner:awareness element can appear 0 or 1 times as a
			 * child of <atom:feed> or <channel>. The value of this element is
			 * an http or https URI. The URI can not be relative. Leading and
			 * trailing whitespace is not significant.
			 * 
			 * Feed Awareness describes the extent and frequency with which a
			 * publisher's feed and its content items are consumed, clicked on,
			 * and referred to by independent sources (i.e., "syndicated").
			 */
			break;
		case "browserFriendly":
			/*
			 * The feedburner:browserFriendly element is simply a human-readable
			 * description explaining the concept of syndication feeds. In older
			 * browsers, clicking a syndicated feed causes the browser to
			 * display a raw XML dump to the end user. This is clearly
			 * undesirable, so FeedBurner uses client-side stylesheets to
			 * reformat the feed into something more friendly. The content of
			 * this element is a message displayed to the end user to explain
			 * what they just clicked on.
			 */
			break;
		case "emailServiceId":
			// child text is integer
			/*
			 * The feedburner:emailServiceId element is a unique identifier for
			 * feeds that use the Email Subscriptions service. It is only
			 * present if the feed publisher has enabled the Email Subscriptions
			 * service.
			 */
			break;
		case "feedburnerHostname":
			// child text is URI
			/*
			 * The feedburner:feedburnerHostname element is the base URI of the
			 * site to be used for verifying email subscriptions. It is only
			 * present if the feed publisher has enabled the Email Subscriptions
			 * service.
			 * 
			 * FeedBurner email is a service that allows publishers to deliver
			 * their feed content to subscribers via email.
			 */
			break;
		case "feedFlare":
			/*
			 * The feedburner:feedFlare element describes a piece of "flare"
			 * that is displayed on the feed's landing page to allow the end
			 * user to subscribe to the blog with a variety of online services.
			 * If a publisher has created custom BrowserFriendly chicklets and
			 * added them to a feed, these chicklets will also appear in the
			 * feed as feedburner:feedFlare elements.
			 * 
			 * The href attribute is a link to a feed subscription service. The
			 * src attribute is a link to an image. The element also has text
			 * content, which is used as alternate text for the image.
			 */
			break;
		case "info":
			/*
			 * The feedburner:info element is an element that appears in every
			 * FeedBurner feed. The uri attribute is required, and indicates the
			 * feed URI chosen by the publisher where the feed can be accessed
			 * under feeds.feedburner.com.
			 */
			Attributes.get("uri").from(reader).ifPresent(uri -> {
				Link link = new Link();
				link.setHref("https://feeds.feedburner.com/" + uri);
				link.setRel("feedburner");
				feed.addLink(link);
			});
			break;
		default:
			Namespace.super.process(feed, reader);
			break;
		}
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "awareness":
			// child text is URI
			/*
			 * As an item-level element, the feedburner:awareness element points
			 * to current item awareness data for the item. It is only present
			 * if the feed publisher has enabled the Awareness API for the feed.
			 */
			break;
		case "origEnclosureLink": {
			/*
			 * The feedburner:origEnclosureLink element points to the original
			 * URI of an enclosure. It is only present in podcasting feeds, and
			 * only if the feed publisher has opted to track enclosure
			 * clickthroughs.
			 */
			Link link = new Link();
			link.setHref(reader.getElementText());
			link.setRel("origEnclosureLink");
			item.addLink(link);
			break;
		}
		case "origLink": {
			/*
			 * The feedburner:origLink element points to the original URI of an
			 * item. It is only present if the feed publisher has opted to track
			 * item clickthroughs.
			 */
			// child text is URI
			Link link = new Link();
			link.setHref(reader.getElementText());
			link.setRel("origLink");
			item.addLink(link);
			break;
		}
		default:
			Namespace.super.process(item, reader);
			break;
		}
	}

}

/*

	corpus stats
	
   1053738 	--> http://rssnamespace.org/feedburner/ext/1.0 level=item localName=origLink attributes=[]]
    772712 	--> http://rssnamespace.org/feedburner/ext/1.0 level=item localName=origEnclosureLink attributes=[]]
     41112 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=feedFlare attributes=[src, href]]
     35095 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=info attributes=[uri]]
      3866 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=emailServiceId attributes=[]]
      3866 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=feedburnerHostname attributes=[]]
      2357 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=browserFriendly attributes=[]]
         4 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=origLink attributes=[]]
         1 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=origEnclosureLink attributes=[]]
         1 	--> http://rssnamespace.org/feedburner/ext/1.0 level=feed localName=awareness attributes=[]]

*/

