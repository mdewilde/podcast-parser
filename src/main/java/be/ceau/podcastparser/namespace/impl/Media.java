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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Category;
import be.ceau.podcastparser.models.Credit;
import be.ceau.podcastparser.models.Hash;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.MediaContent;
import be.ceau.podcastparser.models.TypedString;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Durations;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>Media RSS Specification</h1>
 * 
 * <p>
 * An RSS module that supplements the &lt;enclosure&gt; element capabilities of RSS
 * 2.0 to allow for more robust media syndication.
 * </p>
 * 
 * <p>
 * Primary elements
 * </p>
 * <ul>
 * <li>{@code <media:group>} sub-element of {@code <item>}. It allows grouping
 * of {@code <media:content>} elements that are effectively the same content,
 * yet different representations
 * <li>{@code <media:content>} is a sub-element of either {@code <item>} or
 * {@code <media:group>}. It contains 14 attributes, most of which are optional.
 * <ul>
 * <li>url should specify the direct URL to the media object. If not included, a
 * <media:player> element must be specified.
 * 
 * <li>fileSize is the number of bytes of the media object. It is an optional
 * attribute.
 * 
 * <li>type is the standard MIME type of the object. It is an optional
 * attribute.
 * 
 * <li>medium is the type of object (image | audio | video | document |
 * executable). While this attribute can at times seem redundant if type is
 * supplied, it is included because it simplifies decision making on the reader
 * side, as well as flushes out any ambiguities between MIME type and object
 * type. It is an optional attribute.
 * 
 * <li>isDefault determines if this is the default object that should be used
 * for the <media:group>. There should only be one default object per
 * <media:group>. It is an optional attribute.
 * 
 * <li>expression determines if the object is a sample or the full version of
 * the object, or even if it is a continuous stream (sample | full | nonstop).
 * Default value is "full". It is an optional attribute.
 * 
 * <li>bitrate is the kilobits per second rate of media. It is an optional
 * attribute.
 * 
 * <li>framerate is the number of frames per second for the media object. It is
 * an optional attribute.
 * 
 * <li>samplingrate is the number of samples per second taken to create the
 * media object. It is expressed in thousands of samples per second (kHz). It is
 * an optional attribute.
 * 
 * <li>channels is number of audio channels in the media object. It is an
 * optional attribute.
 * 
 * <li>duration is the number of seconds the media object plays. It is an
 * optional attribute.
 * 
 * <li>height is the height of the media object. It is an optional attribute.
 * 
 * <li>width is the width of the media object. It is an optional attribute.
 * 
 * <li>lang is the primary language encapsulated in the media object. Language
 * codes possible are detailed in RFC 3066. This attribute is used similar to
 * the xml:lang attribute detailed in the XML 1.0 Specification (Third Edition).
 * It is an optional attribute.
 * </ul>
 * </ul>
 * 
 * @see http://www.rssboard.org/media-rss
 */
public class Media implements Namespace {

	private static final String NAME = "http://search.yahoo.com/mrss/";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of(
			"http://search.yahoo.com/mrss", 
			"http://www.rssboard.org/media-rss");
	

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "content":
			item.addMediaContent(parseMediaContent(ctx));
			return;
		case "group":
			// do not map this as a separate model
			// extract content elements and add to item directly
			item.addMediaContents(parseMediaGroup(ctx));
			return;
		case "thumbnail":
			/*
			 * Allows particular images to be used as representative images for
			 * the media object. If multiple thumbnails are included, and time
			 * coding is not at play, it is assumed that the images are in order
			 * of importance. It has one required attribute and three optional
			 * attributes.
			 */
			item.addImage(parseImage(ctx));
			return;
		case "adult": {
			// This is deprecated and has been replaced with 'rating'
			item.getRating().setText(ctx.getElementText()).setScheme("urn:simple");
			return;
		}
		case "rating": {
			// This allows the permissible audience to be declared.
			String scheme = ctx.getAttribute("scheme");
			if (scheme == null) {
				scheme = "urn:simple";
			}
			item.getRating().setText(ctx.getElementText()).setScheme(scheme);
			return;
		}
		case "title": {
			// type specifies the type of text embedded. Possible values are
			// either "plain" or "html". Default value is "plain". All HTML must
			// be entity-encoded. It is an optional attribute.
			TypedString title = new TypedString();
			title.setType(ctx.getAttribute("type"));
			title.setText(ctx.getElementText());
			item.setTitle(title);
			return;
		}
		case "description": {
			// Short description describing the media object typically a sentence in length. It has one optional attribute.
			TypedString description = new TypedString();
			description.setType(ctx.getAttribute("type"));
			description.setText(ctx.getElementText());
			item.setDescription(description);
			return;
		}
		case "keywords": {
			/*
			 * Comma-delimited keywords describing the media object with
			 * typically a maximum of 10 words.
			 */
			String keywords = ctx.getElementText();
			if (keywords != null) {
				String[] split = keywords.split(",");
				for (int i = 0; i < split.length; i++) {
					item.addKeyword(split[i].trim());
				}
			}
			return;
		}
		case "category": {
			/*
			 * Allows a taxonomy to be set that gives an indication of the type
			 * of media content, and its particular contents.
			 */
			String scheme = ctx.getAttribute("scheme");
			String label = ctx.getAttribute("label");
			Category category = new Category()
				.setName(ctx.getElementText())
				.setScheme(scheme == null ? "http://search.yahoo.com/mrss/category_schema" : scheme)
				.setLabel(label);
			item.addCategory(category);
			return;
		}
		case "hash": {
			/*
			 * This is the hash of the binary media file. It can appear multiple
			 * times as long as each instance is a different algo.
			 */
			String algo = ctx.getAttribute("algo");
			Hash hash = new Hash()
					.setAlgo(algo)
					.setHash(ctx.getElementText());
			item.addHash(hash);
			return;
		}
		case "player":
			/*
			 * Allows the media object to be accessed through a web browser
			 * media player console. This element is required only if a direct
			 * media url attribute is not specified in the <media:content>
			 * element. It has one required attribute and two optional
			 * attributes.
			 * 
			 * <media:player url="http://www.foo.com/player?id=1111"
			 * height="200" width="400" />
			 * 
			 * url is the URL of the player console that plays the media. It is
			 * a required attribute.
			 * 
			 * height is the height of the browser window that the URL should be
			 * opened in. It is an optional attribute.
			 * 
			 * width is the width of the browser window that the URL should be
			 * opened in. It is an optional attribute.
			 */
			LoggerFactory.getLogger(Media.class).info("Media player --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "credit":
			/*
			 * Notable entity and the contribution to the creation of the media
			 * object. Current entities can include people, companies,
			 * locations, etc. Specific entities can have multiple roles, and
			 * several entities can have the same role. These should appear as
			 * distinct <media:credit> elements. It has two optional attributes.
			 * 
			 * <media:credit role="producer" scheme="urn:ebu">entity
			 * name</media:credit>
			 * 
			 * <media:credit role="owner" scheme="urn:yvs">copyright holder of
			 * the entity</media:credit>
			 * 
			 * role specifies the role the entity played. Must be lowercase. It
			 * is an optional attribute.
			 * 
			 * scheme is the URI that identifies the role scheme. It is an
			 * optional attribute and possible values for this attribute are (
			 * urn:ebu | urn:yvs ) . The default scheme is "urn:ebu". The list
			 * of roles supported under urn:ebu scheme can be found at European
			 * Broadcasting Union Role Codes. The roles supported under urn:yvs
			 * scheme are ( uploader | owner ).
			 */
			Credit credit = new Credit();
			credit.setScheme(ctx.getAttribute("scheme"));
			credit.setRole(ctx.getAttribute("role"));
			credit.setEntity(ctx.getElementText());
			item.addCredit(credit);
			return;
		case "copyright":
			/*
			 * Copyright information for the media object. It has one optional
			 * attribute.
			 * 
			 * <media:copyright url="http://blah.com/additional-info.html">2005
			 * FooBar Media</media:copyright>
			 * 
			 * url is the URL for a terms of use page or additional copyright
			 * information. If the media is operating under a Creative Commons
			 * license, the Creative Commons module should be used instead. It
			 * is an optional attribute.
			 */
			LoggerFactory.getLogger(Media.class).info("Media copyright --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "text":
			/*
			 * Allows the inclusion of a text transcript, closed captioning or
			 * lyrics of the media content. Many of these elements are permitted
			 * to provide a time series of text. In such cases, it is
			 * encouraged, but not required, that the elements be grouped by
			 * language and appear in time sequence order based on the start
			 * time. Elements can have overlapping start and end times. It has
			 * four optional attributes.
			 * 
			 * <media:text type="plain" lang="en" start="00:00:03.000"
			 * end="00:00:10.000"> Oh, say, can you see</media:text>
			 * 
			 * <media:text type="plain" lang="en" start="00:00:10.000"
			 * end="00:00:17.000">By the dawn's early light</media:text>
			 * 
			 * type specifies the type of text embedded. Possible values are
			 * either "plain" or "html". Default value is "plain". All HTML must
			 * be entity-encoded. It is an optional attribute.
			 * 
			 * lang is the primary language encapsulated in the media object.
			 * Language codes possible are detailed in RFC 3066. This attribute
			 * is used similar to the xml:lang attribute detailed in the XML 1.0
			 * Specification (Third Edition). It is an optional attribute.
			 * 
			 * start specifies the start time offset that the text starts being
			 * relevant to the media object. An example of this would be for
			 * closed captioning. It uses the NTP time code format (see: the
			 * time attribute used in <media:thumbnail>). It is an optional
			 * attribute.
			 * 
			 * end specifies the end time that the text is relevant. If this
			 * attribute is not provided, and a start time is used, it is
			 * expected that the end time is either the end of the clip or the
			 * start of the next <media:text> element.
			 */
			LoggerFactory.getLogger(Media.class).info("Media text --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "restriction":
			/*
			 * Allows restrictions to be placed on the aggregator rendering the
			 * media in the feed. Currently, restrictions are based on
			 * distributor (URI), country codes and sharing of a media object.
			 * This element is purely informational and no obligation can be
			 * assumed or implied. Only one <media:restriction> element of the
			 * same type can be applied to a media object -- all others will be
			 * ignored. Entities in this element should be space-separated. To
			 * allow the producer to explicitly declare his/her intentions, two
			 * literals are reserved: "all", "none". These literals can only be
			 * used once. This element has one required attribute and one
			 * optional attribute (with strict requirements for its exclusion).
			 * 
			 * <media:restriction relationship="allow" type="country">au
			 * us</media:restriction>
			 * 
			 * relationship indicates the type of relationship that the
			 * restriction represents (allow | deny). In the example above, the
			 * media object should only be syndicated in Australia and the
			 * United States. It is a required attribute.
			 * 
			 * Note: If the "allow" element is empty and the type of
			 * relationship is "allow", it is assumed that the empty list means
			 * "allow nobody" and the media should not be syndicated.
			 * 
			 * A more explicit method would be:
			 * 
			 * <media:restriction relationship="allow" type="country">au
			 * us</media:restriction>
			 * 
			 * type specifies the type of restriction (country | uri | sharing )
			 * that the media can be syndicated. It is an optional attribute;
			 * however can only be excluded when using one of the literal values
			 * "all" or "none".
			 * 
			 * "country" allows restrictions to be placed based on country code.
			 * [ISO 3166]
			 * 
			 * "uri" allows restrictions based on URI. Examples: urn:apple,
			 * http://images.google.com, urn:yahoo, etc.
			 * 
			 * "sharing" allows restriction on sharing.
			 * 
			 * "deny" means content cannot be shared -- for example via embed
			 * tags. If the sharing type is not present, the default
			 * functionality is to allow sharing. For example:
			 * 
			 * <media:restriction type="sharing" relationship="deny" />
			 */
			LoggerFactory.getLogger(Media.class).info("Media restriction --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "community":
			/*
			 * This element stands for the community related content. This
			 * allows inclusion of the user perception about a media object in
			 * the form of view count, ratings and tags.
			 * 
			 * <media:community> <media:starRating average="3.5" count="20"
			 * min="1" max="10" /> <media:statistics views="5" favorites="5" />
			 * <media:tags>news: 5, abc:3, reuters</media:tags>
			 * </media:community>
			 * 
			 * starRating This element specifies the rating-related information
			 * about a media object. Valid attributes are average, count, min
			 * and max.
			 * 
			 * statistics This element specifies various statistics about a
			 * media object like the view count and the favorite count. Valid
			 * attributes are views and favorites.
			 * 
			 * tags This element contains user-generated tags separated by
			 * commas in the decreasing order of each tag's weight. Each tag can
			 * be assigned an integer weight in tag_name:weight format. It's up
			 * to the provider to choose the way weight is determined for a tag;
			 * for example, number of occurences can be one way to decide weight
			 * of a particular tag. Default weight is 1.
			 */
			LoggerFactory.getLogger(Media.class).info("Media community --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "comments":
			/*
			 * Allows inclusion of all the comments a media object has received.
			 */
			LoggerFactory.getLogger(Media.class).info("Media comments --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "embed":
			/*
			 * Sometimes player-specific embed code is needed for a player to
			 * play any video. <media:embed> allows inclusion of such
			 * information in the form of key-value pairs.
			 */
			LoggerFactory.getLogger(Media.class).info("Media embed --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "responses":
			/*
			 * Allows inclusion of a list of all media responses a media object has received.
			 */
			LoggerFactory.getLogger(Media.class).info("Media responses --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "backLinks":
			/*
			 * Allows inclusion of all the URLs pointing to a media object.
			 */
			LoggerFactory.getLogger(Media.class).info("Media backLinks --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "status":
			/*
			 * Optional tag to specify the status of a media object -- whether
			 * it's still active or it has been blocked/deleted.
			 * 
			 * <media:status state="blocked"
			 * reason="http://www.reasonforblocking.com" />
			 * 
			 * state can have values "active", "blocked" or "deleted". "active"
			 * means a media object is active in the system, "blocked" means a
			 * media object is blocked by the publisher, "deleted" means a media
			 * object has been deleted by the publisher.
			 * 
			 * reason is a reason explaining why a media object has been
			 * blocked/deleted. It can be plain text or a URL.
			 */
			LoggerFactory.getLogger(Media.class).info("Media status --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "price":
			/*
			 * Optional tag to include pricing information about a media object.
			 * If this tag is not present, the media object is supposed to be
			 * free. One media object can have multiple instances of this tag
			 * for including different pricing structures. The presence of this
			 * tag would mean that media object is not free.
			 * 
			 * <media:price type="rent" price="19.99" currency="EUR" />
			 * 
			 * <media:price type="package"
			 * info="http://www.dummy.jp/package_info.html" price="19.99"
			 * currency="EUR" />
			 * 
			 * <media:price type="subscription"
			 * info="http://www.dummy.jp/subscription_info" price="19.99"
			 * currency="EUR" />
			 * 
			 * type Valid values are "rent", "purchase", "package" or
			 * "subscription". If nothing is specified, then the media is free.
			 * 
			 * info if the type is "package" or "subscription", then info is a
			 * URL pointing to package or subscription information. This is an
			 * optional attribute.
			 * 
			 * price is the price of the media object. This is an optional
			 * attribute.
			 * 
			 * currency -- use [ISO 4217] for currency codes. This is an
			 * optional attribute.
			 */
			LoggerFactory.getLogger(Media.class).info("Media price --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "license": {
			/*
			 * Optional link to specify the machine-readable license associated
			 * with the content.
			 * 
			 * <media:license type="text/html"
			 * href="http://creativecommons.org/licenses/by/3.0/us/">Creative
			 * Commons Attribution 3.0 United States License</media:license>
			 */
			String type = ctx.getAttribute("type");
			String label = ctx.getAttribute("label");
			item.computeLicenseIfAbsent()
				.setHref(ctx.getElementText())
				.setType(type)
				.setLabel(label);
			return;
		}
		case "subTitle":
			/*
			 * Optional element for subtitle/CC link. It contains type and
			 * language attributes. Language is based on RFC 3066. There can be
			 * more than one such tag per media element, for example one per
			 * language. Please refer to Timed Text spec - W3C for more
			 * information on Timed Text and Real Time Subtitling.
			 */
			LoggerFactory.getLogger(Media.class).info("Media subTitle --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "peerLink":
			/*
			 * Optional element for P2P link.
			 * 
			 * <media:peerLink type="application/x-bittorrent"
			 * href="http://www.example.org/sampleFile.torrent" />
			 * 
			 * For a valid Media RSS item, at least one of the following links
			 * is required:
			 * 
			 * media:content media:player media:peerLink media:location
			 * 
			 * Optional element to specify geographical information about
			 * various locations captured in the content of a media object. The
			 * format conforms to geoRSS.
			 * 
			 * <media:location description="My house" start="00:01" end="01:00">
			 * <georss:where> <gml:Point> <gml:pos>35.669998
			 * 139.770004</gml:pos> </gml:Point> </georss:where>
			 * </media:location>
			 * 
			 * description description of the place whose location is being
			 * specified.
			 * 
			 * start time at which the reference to a particular location starts
			 * in the media object.
			 * 
			 * end time at which the reference to a particular location ends in
			 * the media object.
			 */
			LoggerFactory.getLogger(Media.class).info("Media peerLink --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "rights":
			/*
			 * Optional element to specify the rights information of a media
			 * object.
			 * 
			 * <media:rights status="userCreated" />
			 * 
			 * <media:rights status="official" />
			 * 
			 * status is the status of the media object saying whether a media
			 * object has been created by the publisher or they have rights to
			 * circulate it. Supported values are "userCreated" and "official".
			 */
			LoggerFactory.getLogger(Media.class).info("Media rights --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		case "scenes":
			/*
			 * Optional element to specify various scenes within a media object.
			 * It can have multiple child <media:scene> elements, where each
			 * <media:scene> element contains information about a particular
			 * scene. <media:scene> has the optional sub-elements <sceneTitle>,
			 * <sceneDescription>, <sceneStartTime> and <sceneEndTime>, which
			 * contains title, description, start and end time of a particular
			 * scene in the media, respectively.
			 * 
			 * <media:scenes> <media:scene> <sceneTitle>sceneTitle1</sceneTitle>
			 * <sceneDescription>sceneDesc1</sceneDescription>
			 * <sceneStartTime>00:15</sceneStartTime>
			 * <sceneEndTime>00:45</sceneEndTime> </media:scene> <media:scene>
			 * <sceneTitle>sceneTitle2</sceneTitle>
			 * <sceneDescription>sceneDesc2</sceneDescription>
			 * <sceneStartTime>00:57</sceneStartTime>
			 * <sceneEndTime>01:45</sceneEndTime> </media:scene> </media:scenes>
			 */
			LoggerFactory.getLogger(Media.class).info("Media scenes --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			return;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private List<MediaContent> parseMediaGroup(PodParseContext ctx) throws XMLStreamException {
		List<MediaContent> list = new ArrayList<>();
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("group".equals(ctx.getReader().getLocalName())) {
					return list;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				if ("content".equals(ctx.getReader().getLocalName())) {
					list.add(parseMediaContent(ctx));
				}
				break;
			}
		}
		return list;
	}

	private MediaContent parseMediaContent(PodParseContext ctx) throws XMLStreamException {
		MediaContent mediaContent = new MediaContent();
		mediaContent.setUrl(ctx.getAttribute("url"));
		String fileSize = ctx.getAttribute("fileSize");
		if (fileSize != null) {
			try {
				mediaContent.setFileSize(Long.parseLong(fileSize.trim()));
			} catch (NumberFormatException e) {
			}
		}
		mediaContent.setType(ctx.getAttribute("type"));
		mediaContent.setMedium(ctx.getAttribute("medium"));
		mediaContent.setIsDefault(ctx.getAttribute("isDefault"));
		String bitrate = ctx.getAttribute("bitrate");
		if (bitrate != null) {
			try {
				mediaContent.setBitrate(Long.parseLong(bitrate.trim()));
			} catch (NumberFormatException e) {
			}
		}
		String framerate = ctx.getAttribute("framerate");
		if (framerate != null) {
			try {
				mediaContent.setFramerate(Long.parseLong(framerate.trim()));
			} catch (NumberFormatException e) {
			}
		}
		mediaContent.setSamplingrate(ctx.getAttribute("samplingrate"));
		mediaContent.setChannels(ctx.getAttribute("channels"));
		String duration = ctx.getAttribute("duration");
		if (duration != null) {
			try {
				mediaContent.setDuration(Duration.ofSeconds(Integer.parseInt(duration.trim())));
			} catch (NumberFormatException e) {
			}
		}
		String height = ctx.getAttribute("height");
		if (height != null) {
			try {
				mediaContent.setHeight(Integer.parseInt(height.trim()));
			} catch (NumberFormatException e) {
			}
		}
		String width = ctx.getAttribute("width");
		if (width != null) {
			try {
				mediaContent.setWidth(Integer.parseInt(width.trim()));
			} catch (NumberFormatException e) {
			}
		}
		mediaContent.setLang(ctx.getAttribute("lang"));
		return mediaContent;
	}

	private Image parseImage(PodParseContext ctx) throws XMLStreamException {
		String url = ctx.getAttribute("url");
		String width = ctx.getAttribute("width");
		String height = ctx.getAttribute("width");
		String time = ctx.getAttribute("time");
		return new Image()
				.setUrl(url)
				.setWidth(width)
				.setHeight(height)
				.setTime(Durations.parse(time))
				.setTitle("thumbnail");
	}

}

/*

	corpus stats
	
    975653 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, type, url]]
    222037 	--> http://search.yahoo.com/mrss/ level=item localName=keywords attributes=[]]
    210790 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[medium, url]]
    207406 	--> http://search.yahoo.com/mrss/ level=item localName=rights attributes=[status]]
    175765 	--> http://search.yahoo.com/mrss/ level=item localName=title attributes=[type]]
    169383 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[type, url]]
    146950 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[medium, type, url]]
    101757 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[url]]
     91487 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[width, url, height]]
     79898 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, medium, type, lang, url]]
     70156 	--> http://search.yahoo.com/mrss/ level=item localName=title attributes=[]]
     69699 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, medium, type, lang, url]]
     51576 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, medium, type, url]]
     46977 	--> http://search.yahoo.com/mrss/ level=feed localName=category attributes=[scheme]]
     43166 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, type, url]]
     42661 	--> http://search.yahoo.com/mrss/ level=item localName=text attributes=[type]]
     38565 	--> http://search.yahoo.com/mrss/ level=item localName=player attributes=[url]]
     36922 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, medium, type, url]]
     31702 	--> http://search.yahoo.com/mrss/ level=feed localName=rating attributes=[]]
     28050 	--> http://search.yahoo.com/mrss/ level=feed localName=thumbnail attributes=[url]]
     27594 	--> http://search.yahoo.com/mrss/ level=feed localName=credit attributes=[role]]
     25930 	--> http://search.yahoo.com/mrss/ level=feed localName=keywords attributes=[]]
     25782 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[]]
     24486 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, bitrate, type, url]]
     24392 	--> http://search.yahoo.com/mrss/ level=feed localName=description attributes=[type]]
     22049 	--> http://search.yahoo.com/mrss/ level=item localName=group attributes=[]]
     21794 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, medium, type, url]]
     20570 	--> http://search.yahoo.com/mrss/ level=feed localName=copyright attributes=[]]
     18689 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[width, medium, type, url, height]]
     18330 	--> http://search.yahoo.com/mrss/ level=item localName=credit attributes=[role]]
     16120 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[vcodec, isDefault, role, expression, fileSize, acodec, width, type, url, height]]
     14409 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[length, medium, type, url]]
     12333 	--> http://search.yahoo.com/mrss/ level=item localName=description attributes=[type]]
      9814 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[url]]
      9391 	--> http://search.yahoo.com/mrss/ level=item localName=category attributes=[scheme]]
      9276 	--> http://search.yahoo.com/mrss/ level=item localName=description attributes=[]]
      6636 	--> http://search.yahoo.com/mrss/ level=item localName=rating attributes=[scheme]]
      5317 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, width, bitrate, medium, type, url, height]]
      4167 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, medium, type, url]]
      3909 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration]]
      3699 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, width, bitrate, medium, type, lang, url, height]]
      3675 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, width, medium, type, url, height]]
      3262 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, fileSize, width, bitrate, medium, type, url, height]]
      2539 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, medium, type, url]]
      2516 	--> http://search.yahoo.com/mrss/ level=item localName=rating attributes=[]]
      2493 	--> http://search.yahoo.com/mrss/ level=item localName=copyright attributes=[]]
      1813 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[href]]
      1766 	--> http://search.yahoo.com/mrss/ level=item localName=credit attributes=[role, scheme]]
      1749 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, width, medium, type, url, height]]
      1552 	--> http://search.yahoo.com/mrss/ level=item localName=category attributes=[]]
      1287 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, role, expression, fileSize, acodec, type, url]]
      1178 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, fileSize, medium, url]]
      1061 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[duration, expression, fileSize, bitrate, type, url]]
      1047 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, bitrate, type, url]]
       971 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[width, type, url, height]]
       968 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[width, type, url, height]]
       914 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, medium, type, url]]
       911 	--> http://search.yahoo.com/mrss/ level=item localName=player attributes=[width, url, height]]
       902 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[]]
       876 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, medium, url]]
       842 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, role, expression, fileSize, width, type, url, height]]
       764 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, width, medium, type, url, height]]
       753 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, fileSize, bitrate, medium, type, url]]
       739 	--> http://search.yahoo.com/mrss/ level=item localName=restriction attributes=[relationship, type]]
       737 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[fileSize, type, url]]
       691 	--> http://search.yahoo.com/mrss/ level=feed localName=rating attributes=[scheme]]
       691 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, url]]
       681 	--> http://search.yahoo.com/mrss level=item localName=credit attributes=[]]
       662 	--> http://search.yahoo.com/mrss/ level=item localName=hash attributes=[algo]]
       605 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, bitrate, medium, type, url]]
       567 	--> http://search.yahoo.com/mrss level=item localName=thumbnail attributes=[url]]
       535 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[type]]
       535 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[lang]]
       535 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize]]
       511 	--> http://search.yahoo.com/mrss level=item localName=adult attributes=[]]
       498 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, role, expression, fileSize, acodec, width, type, url, height]]
       495 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, role, expression, fileSize, type, url]]
       493 	--> http://search.yahoo.com/mrss/ level=item localName=credit attributes=[]]
       469 	--> http://search.yahoo.com/mrss/ level=item localName=adult attributes=[]]
       421 	--> http://search.yahoo.com/mrss/ level=item localName=category attributes=[scheme, label]]
       420 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, channels, width, medium, type, lang, url, height]]
       401 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, fileSize, type, url]]
       375 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, fileSize, type, url]]
       372 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, framerate, width, bitrate, type, url, height]]
       356 	--> http://search.yahoo.com/mrss level=item localName=category attributes=[label]]
       348 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, medium]]
       338 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, bitrate, medium, type, url]]
       334 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[length, type, url]]
       314 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, width, medium, type, lang, url, height]]
       309 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[width, medium, url, height]]
       308 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, fileSize, type, url]]
       285 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[width, height]]
       285 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[framerate]]
       235 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[length, type, url]]
       234 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, fileSize, medium, type, url]]
       216 	--> http://search.yahoo.com/mrss/ level=item localName=category attributes=[label]]
       200 	--> http://search.yahoo.com/mrss level=item localName=restriction attributes=[relationship, type]]
       200 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, fileSize, width, medium, type, url, height]]
       199 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, width, type, url, height]]
       196 	--> http://search.yahoo.com/mrss/ level=item localName=text attributes=[]]
       171 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, channels, fileSize, framerate, width, bitrate, type, url, height]]
       164 	--> http://search.yahoo.com/mrss/ level=item localName=backLinks attributes=[]]
       164 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[type, url]]
       164 	--> http://search.yahoo.com/mrss/ level=item localName=backLink attributes=[]]
       164 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, width, bitrate, medium, type, lang, url, height]]
       164 	--> http://search.yahoo.com/mrss/ level=item localName=embed attributes=[url]]
       164 	--> http://search.yahoo.com/mrss/ level=item localName=status attributes=[state]]
       164 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, width, bitrate, medium, type, lang, url, height]]
       152 	--> http://search.yahoo.com/mrss level=item localName=keywords attributes=[]]
       147 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[duration, width, type, url, height]]
       141 	--> http://search.yahoo.com/mrss level=item localName=adult attributes=[scheme]]
       140 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, bitrate, type, url, height]]
       134 	--> http://search.yahoo.com/mrss level=item localName=title attributes=[]]
       112 	--> http://search.yahoo.com/mrss level=item localName=rating attributes=[scheme]]
       110 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, channels, fileSize, bitrate, samplingrate, medium, type, url]]
       109 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[url]]
       108 	--> http://search.yahoo.com/mrss level=item localName=title attributes=[type]]
       108 	--> http://search.yahoo.com/mrss level=item localName=description attributes=[type]]
        99 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, bitrate, type, url]]
        97 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, type, lang, url]]
        96 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, channels, bitrate, medium, url]]
        95 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, width, bitrate, type, url]]
        93 	--> http://search.yahoo.com/mrss level=item localName=thumbnail attributes=[width, url, height]]
        86 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, aspectRatio, medium, type, url]]
        83 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[filesize, type, url]]
        76 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, type, url]]
        73 	--> http://search.yahoo.com/mrss level=item localName=player attributes=[width, url, height]]
        73 	--> http://search.yahoo.com/mrss level=item localName=group attributes=[]]
        70 	--> http://search.yahoo.com/mrss/ level=item localName=text attributes=[start, end, type, lang]]
        69 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, fileSize, medium, type, lang, url]]
        68 	--> http://video.search.yahoo.com/mrss level=item localName=title attributes=[]]
        68 	--> http://video.search.yahoo.com/mrss level=item localName=content attributes=[medium, type, lang, url]]
        68 	--> http://video.search.yahoo.com/mrss level=item localName=keywords attributes=[]]
        68 	--> http://video.search.yahoo.com/mrss level=item localName=description attributes=[]]
        65 	--> http://search.yahoo.com/mrss/ level=item localName=thumb attributes=[width, url, height]]
        63 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, fileSize, medium, type, url]]
        62 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, fileSize, medium, lang, type, url]]
        61 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, framerate, bitrate, type, url]]
        55 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, medium, type, lang, url]]
        53 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[width, time, url, height]]
        52 	--> http://search.yahoo.com/mrss/ level=item localName=enclosure attributes=[length, type, url]]
        50 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, medium, url]]
        50 	--> http://search.yahoo.com/mrss/ level=item localName=enclosure attributes=[type]]
        39 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, type, lang, url]]
        37 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, medium, lang, url]]
        36 	--> http://search.yahoo.com/mrss level=item localName=description attributes=[]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=rating attributes=[scheme]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, medium, type, url]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=title attributes=[]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=credit attributes=[]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=description attributes=[]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=keywords attributes=[]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=player attributes=[width, url, height]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[url]]
        35 	--> https://search.yahoo.com/mrss/ level=item localName=category attributes=[]]
        34 	--> http://search.yahoo.com/mrss/ level=feed localName=thumbnail attributes=[width, url, height]]
        32 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, idDefault, medium, url]]
        31 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, bitrate, type, url]]
        30 	--> http://search.yahoo.com/mrss/ level=item localName=thumbnail attributes=[width, medium, type, url, height]]
        30 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, medium, lang, type, url]]
        28 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, type, url]]
        27 	--> http://search.yahoo.com/mrss/ level=item localName=text attributes=[lang, type]]
        26 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[medium, url]]
        25 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, fileSize, type, url]]
        24 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[width, bitrate, url]]
        21 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, type, lang, url]]
        21 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, medium, type, url]]
        20 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, width, medium, type, url, height]]
        19 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[vcodec, isDefault, role, expression, fileSize, width, type, url, height]]
        18 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, mediaformat, bitrate, type, url]]
        16 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, type, url]]
        16 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, medium, type, url]]
        15 	--> http://search.yahoo.com/mrss level=item localName=player attributes=[url]]
        14 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, fileSize, width, type, url, height]]
        13 	--> http://search.yahoo.com/mrss/ level=feed localName=copyright attributes=[url]]
        12 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[width, url, height]]
        11 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, bitrate, medium, type, url]]
        11 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[vcodec, isDefault, role, expression, acodec, width, type, url, height]]
        10 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, channels, fileSize, bitrate, type, url]]
        10 	--> http://search.yahoo.com/mrss level=item localName=thumbnail attributes=[width, url]]
        10 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, medium, type, url]]
        10 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[width, medium, type, url, height]]
         9 	--> http://search.yahoo.com/mrss/ level=feed localName=title attributes=[type]]
         8 	--> http://search.yahoo.com/mrss level=item localName=copyright attributes=[]]
         8 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, expression, fileSize, framerate, width, bitrate, medium, type, lang, url, height]]
         8 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[expression, channels, bitrate, medium]]
         8 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, bitrate, samplingrate, medium, type, url, duration, isDefault, channels, fileSize, width, lang, height]]
         7 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, width, type, url, height]]
         7 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, width, medium, type, lang, url, height]]
         7 	--> http://search.yahoo.com/mrss/ level=feed localName=credit attributes=[role, scheme]]
         4 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, channels, fileSize, bitrate, medium, type, url]]
         4 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, bitrate, medium, type, lang, url]]
         3 	--> http://search.yahoo.com/mrss level=item localName=item attributes=[fileSize, type, url]]
         2 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[expression, framerate, bitrate, samplingrate, medium, type, url, duration, isDefault, channels, fileSize, width, lang, height]]
         2 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, isDefault, expression, fileSize, bitrate, medium, type, lang, url]]
         2 	--> http://search.yahoo.com/mrss level=feed localName=rating attributes=[scheme]]
         2 	--> http://search.yahoo.com/mrss/ level=feed localName=content attributes=[fileSize, type, url]]
         2 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[vcodec, isDefault, role, expression, fileSize, acodec, type, url]]
         2 	--> http://search.yahoo.com/mrss/ level=feed localName=description attributes=[]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=copyright attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=feed localName=backlink attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, bitrate, medium, type, lang, url]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=credit attributes=[role]]
         1 	--> http://search.yahoo.com/mrss/ level=feed localName=content attributes=[width, type, url, height]]
         1 	--> http://search.yahoo.com/mrss level=feed localName=keywords attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=feed localName=title attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=feed localName=backLinks attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[isDefault, expression, fileSize, bitrate, samplingrate, medium, type, lang, url]]
         1 	--> http://search.yahoo.com/mrss level=feed localName=description attributes=[type]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=keywords attributes=[]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=rating attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[duration, fileSize, framerate, width, medium, type, url, height]]
         1 	--> http://search.yahoo.com/mrss level=feed localName=title attributes=[type]]
         1 	--> http://video.search.yahoo.com/mrss level=feed localName=keywords attributes=[]]
         1 	--> http://search.yahoo.com/mrss/ level=feed localName=content attributes=[type, url]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=category attributes=[scheme]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=thumbnail attributes=[url]]
         1 	--> http://search.yahoo.com/mrss/ level=feed localName=text attributes=[type, lang]]
         1 	--> https://search.yahoo.com/mrss/ level=feed localName=description attributes=[type]]
         1 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, medium, url]]
         1 	--> http://search.yahoo.com/mrss/ level=item localName=content attributes=[fileSize, medium, type, lang, url]]
         1 	--> http://search.yahoo.com/mrss level=item localName=content attributes=[duration, width, url, height]]
         1 	--> http://video.search.yahoo.com/mrss level=feed localName=thumbail attributes=[url]]


     60083 	--> http://www.rssboard.org/media-rss level=item localName=title attributes=[type]]
     60083 	--> http://www.rssboard.org/media-rss level=item localName=content attributes=[isDefault, width, medium, type, url, height]]
       300 	--> http://www.rssboard.org/media-rss level=feed localName=category attributes=[scheme]]
       279 	--> http://www.rssboard.org/media-rss level=feed localName=rating attributes=[]]
       279 	--> http://www.rssboard.org/media-rss level=feed localName=credit attributes=[role]]
       279 	--> http://www.rssboard.org/media-rss level=feed localName=description attributes=[type]]
       168 	--> http://www.rssboard.org/media-rss level=feed localName=keywords attributes=[]]
        76 	--> http://www.rssboard.org/media-rss level=feed localName=copyright attributes=[]]
        73 	--> http://www.rssboard.org/media-rss level=item localName=content attributes=[length, type, url]]

*/
