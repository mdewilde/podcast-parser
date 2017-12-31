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
package be.ceau.podcastparser.namespace.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Category;
import be.ceau.podcastparser.models.Copyright;
import be.ceau.podcastparser.models.Credit;
import be.ceau.podcastparser.models.Hash;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.MediaContent;
import be.ceau.podcastparser.models.MediaPlayer;
import be.ceau.podcastparser.models.Rating;
import be.ceau.podcastparser.models.Scene;
import be.ceau.podcastparser.models.Transcript;
import be.ceau.podcastparser.models.TypedString;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Durations;
import be.ceau.podcastparser.util.Strings;
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
	public void process(PodParseContext ctx) throws XMLStreamException {
		String localName = ctx.getReader().getLocalName();
		switch (localName) {
		case "category":
			ctx.getFeed().addCategory(parseCategory(ctx));
			break;
		case "copyright":
			ctx.getFeed().setMediaCopyright(parseCopyright(ctx));
			break;
		case "credit":
			ctx.getFeed().setCredit(parseCredit(ctx));
			break;
		case "description":
			ctx.getFeed().setDescription(parseDescription(ctx));
			break;
		case "keywords":
			ctx.getFeed().addKeywords(parseKeywords(ctx));
			break;
		case "rating":
			ctx.getFeed().setRating(parseRating(ctx));
			break;
		case "thumbnail":
			ctx.getFeed().addImage(parseImage(ctx));
			break;
		default : 
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "adult":
			// This is deprecated and has been replaced with 'rating'
			item.getRating().setText(ctx.getElementText()).setScheme("urn:simple");
			break;
		case "category":
			item.addCategory(parseCategory(ctx));
			break;
		case "content":
			item.addMediaContent(parseMediaContent(ctx));
			break;
		case "description":
			item.setDescription(parseDescription(ctx));
			break;
		case "group":
			// do not map this as a separate model
			// extract content elements and add to item directly
			item.addMediaContents(parseMediaGroup(ctx));
			break;
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
			break;
		}
		case "keywords":
			ctx.getFeed().addKeywords(parseKeywords(ctx));
			break;
		case "rating": 
			item.setRating(parseRating(ctx));
			break;
		case "thumbnail":
			item.addImage(parseImage(ctx));
			return;
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
		case "player":
			MediaPlayer player = new MediaPlayer();
			player.setUrl(ctx.getAttribute("url"));
			try {
				int height = Integer.parseInt(ctx.getAttribute("height"));
				player.setHeight(height);
			} catch (NumberFormatException e) {
			}
			try {
				int width = Integer.parseInt(ctx.getAttribute("width"));
				player.setWidth(width);
			} catch (NumberFormatException e) {
			}
			item.setMediaPlayer(player);
			break;
		case "credit":
			item.addCredit(parseCredit(ctx));
			break;
		case "copyright":
			item.setMediaCopyright(parseCopyright(ctx));
			break;
		case "text":
			item.addTranscript(parseText(ctx));
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
			Namespace.super.process(ctx, item);
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
			Namespace.super.process(ctx, item);
			return;
		case "comments":
			/*
			 * Allows inclusion of all the comments a media object has received.
			 */
			Namespace.super.process(ctx, item);
			return;
		case "embed":
			/*
			 * Sometimes player-specific embed code is needed for a player to
			 * play any video. <media:embed> allows inclusion of such
			 * information in the form of key-value pairs.
			 */
			Namespace.super.process(ctx, item);
			return;
		case "responses":
			/*
			 * Allows inclusion of a list of all media responses a media object has received.
			 */
			Namespace.super.process(ctx, item);
			return;
		case "backLinks":
			/*
			 * Allows inclusion of all the URLs pointing to a media object.
			 */
			Namespace.super.process(ctx, item);
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
			Namespace.super.process(ctx, item);
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
			Namespace.super.process(ctx, item);
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
			Namespace.super.process(ctx, item);
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
			Namespace.super.process(ctx, item);
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
			Namespace.super.process(ctx, item);
			break;
		case "scenes":
			parseScenes(ctx).forEach(item::addScene);
			break;
		default : 
			Namespace.super.process(ctx, item);
			break;
		}
	}

	/*
	 * Allows a taxonomy to be set that gives an indication of the type
	 * of media content, and its particular contents.
	 */
	private Category parseCategory(PodParseContext ctx) throws XMLStreamException {
		Category category = new Category();
		String scheme = ctx.getAttribute("scheme");
		if (Strings.isBlank(scheme)) {
			// scheme is an optional attribute. If not included, the default is
			// "http://search.yahoo.com/mrss/category_schema".
			scheme = "http://search.yahoo.com/mrss/category_schema";
		}
		category.setScheme(scheme);
		category.setLabel(ctx.getAttribute("label"));
		category.setName(ctx.getElementText());
		return category;
	}
	
	private Copyright parseCopyright(PodParseContext ctx) throws XMLStreamException {
		Copyright copyright = new Copyright();
		copyright.setText(ctx.getElementText());
		copyright.setUrl(ctx.getAttribute("url"));
		return copyright;
	}
	
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
	private Credit parseCredit(PodParseContext ctx) throws XMLStreamException {
		Credit credit = new Credit();
		credit.setScheme(ctx.getAttribute("scheme"));
		credit.setRole(ctx.getAttribute("role"));
		credit.setEntity(ctx.getElementText());
		return credit;
	}

	/**
	 * Short description describing the media object typically a sentence in length. It has one optional
	 * attribute.
	 * {@code 
	 * <media:description type="plain">This was some really bizarre band I listened to as a young
	 * lad.</media:description>
	 * }
	 * type specifies the type of text embedded. Possible values are either "plain" or "html". Default
	 * value is "plain". All HTML must be entity-encoded. It is an optional attribute.
	 * 
	 * @return
	 */
	private TypedString parseDescription(PodParseContext ctx) throws XMLStreamException {
		TypedString typedString = new TypedString();
		if ("html".equals(ctx.getAttribute("type"))) {
			typedString.setType("html");
		} else {
			typedString.setType("plain");
		}
		typedString.setText(ctx.getElementText());
		return typedString;
	}
	
	/*
	 * Allows particular images to be used as representative images for
	 * the media object. If multiple thumbnails are included, and time
	 * coding is not at play, it is assumed that the images are in order
	 * of importance. It has one required attribute and three optional
	 * attributes.
	 */
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

	/*
	 * Comma-delimited keywords describing the media object with
	 * typically a maximum of 10 words.
	 */
	private Set<String> parseKeywords(PodParseContext ctx) throws XMLStreamException {
		Set<String> set = new HashSet<>();
		String keywords = ctx.getElementText();
		if (keywords != null) {
			String[] split = keywords.split(",");
			for (int i = 0; i < split.length; i++) {
				set.add(split[i].trim());
			}
		}
		return set;
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

	/**
	 * This allows the permissible audience to be declared. If this element is not included, it assumes
	 * that no restrictions are necessary. It has one optional attribute.
	 * 
	 * <media:rating scheme="urn:simple">adult</media:rating>
	 * <media:rating scheme="urn:icra">r (cz 1 lz 1 nz 1 oz 1 vz 1)</media:rating>
	 * <media:rating scheme="urn:mpaa">pg</media:rating>
	 * <media:rating scheme="urn:v-chip">tv-y7-fv</media:rating>
	 * 
	 * scheme is the URI that identifies the rating scheme. It is an optional attribute. If this
	 * attribute is not included, the default scheme is urn:simple (adult | nonadult).
	 * 
	 * @param ctx
	 * @return
	 * @throws XMLStreamException
	 */
	private Rating parseRating(PodParseContext ctx)throws XMLStreamException {
		String scheme = ctx.getAttribute("scheme");
		if (Strings.isBlank(scheme)) {
			scheme = "urn:simple";
		}
		Rating rating = new Rating();
		rating.setText(ctx.getElementText());
		rating.setScheme(scheme);
		return rating;
	}
	
	private List<Scene> parseScenes(PodParseContext ctx) throws XMLStreamException {
		/*
		 * Optional element to specify various scenes within a media object.
		 * It can have multiple child <media:scene> elements, where each
		 * <media:scene> element contains information about a particular
		 * scene. <media:scene> has the optional sub-elements <sceneTitle>,
		 * <sceneDescription>, <sceneStartTime> and <sceneEndTime>, which
		 * contains title, description, start and end time of a particular
		 * scene in the media, respectively.
		 */
		List<Scene> scenes = new ArrayList<>();
		Scene scene = null;
		while (ctx.getReader().hasNext()) {
			switch (ctx.getReader().next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("scenes".equals(ctx.getReader().getLocalName())) {
					return scenes;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				if ("scene".equals(ctx.getReader().getLocalName())) {
					scene = new Scene();
					scenes.add(scene);
				}
				if ("sceneTitle".equals(ctx.getReader().getLocalName())) {
					scene.setTitle(ctx.getElementText());
				}
				if ("sceneDescription".equals(ctx.getReader().getLocalName())) {
					scene.setDescription(ctx.getElementText());
				}
				if ("sceneStartTime".equals(ctx.getReader().getLocalName())) {
					scene.setStartTime(ctx.getElementText());
				}
				if ("sceneEndTime".equals(ctx.getReader().getLocalName())) {
					scene.setEndTime(ctx.getElementText());
				}
				break;
			}
		}
		return scenes;

	}
	
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
	private Transcript parseText(PodParseContext ctx) throws XMLStreamException {
		
		Transcript transcript = new Transcript();
		transcript.setLang(ctx.getAttribute("lang"));

		String type = ctx.getAttribute("type");
		if (Strings.isBlank(type)) {
			type = "plain";
		}
		transcript.setType(type);
		
		String start = ctx.getAttribute("start");
		if (Strings.isNotBlank(start)) {
			transcript.setStart(Durations.parse(start));
		}
		
		String end = ctx.getAttribute("end");
		if (Strings.isNotBlank(end)) {
			transcript.setEnd(Durations.parse(end));
		}
		transcript.setText(ctx.getElementText());
		return transcript;
		
	}
}
