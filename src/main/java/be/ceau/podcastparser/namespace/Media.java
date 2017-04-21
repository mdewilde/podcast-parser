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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.PodcastParser;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.MediaContent;
import be.ceau.podcastparser.models.Rating;
import be.ceau.podcastparser.util.Attributes;
import be.ceau.podcastparser.util.Durations;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>Media RSS Specification</h1>
 * 
 * <p>
 * An RSS module that supplements the <enclosure> element capabilities of RSS
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

	public static final Set<String> NAMES = UnmodifiableSet.of("http://search.yahoo.com/mrss/",
			"http://www.rssboard.org/media-rss");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "content":
			item.addMediaContent(parseMediaContent(reader));
			return;
		case "group":
			// do not map this as a separate model
			// extract content elements and add to item directly
			item.addMediaContents(parseMediaGroup(reader));
			return;
		case "thumbnail":
			item.addImage(parseImage(reader));
			return;
		case "adult": {
			// This is deprecated and has been replaced with 'rating'
			Rating rating = item.getRating().orElse(new Rating());
			rating.setScheme("urn:simple");
			rating.setText(reader.getElementText());
			item.setRating(rating);
			return;
		}
		case "rating": {
			// This allows the permissible audience to be declared.
			Rating rating = item.getRating().orElse(new Rating());
			String scheme = reader.getAttributeValue(null, "scheme");
			rating.setScheme(scheme != null ? scheme : "urn:simple");				
			rating.setText(reader.getElementText());
			item.setRating(rating);
			return;
		}
		case "title":
			// type specifies the type of text embedded. Possible values are
			// either "plain" or "html". Default value is "plain". All HTML must
			// be entity-encoded. It is an optional attribute.
			return;
		case "description":
			// Short description describing the media object typically a sentence in length. It has one optional attribute.

			return;
		case "keywords":
			return;
		case "thumbnails":
			return;
		case "category":
			return;
		case "hash":
			return;
		case "player":
			return;
		case "credit":
			return;
		case "copyright":
			return;
		case "text":
			return;
		case "restriction":
			return;
		case "community":
			return;
		case "comments":
			return;
		case "embed":
			return;
		case "responses":
			return;
		case "backLinks":
			return;
		case "status":
			return;
		case "price":
			return;
		case "license":
			return;
		case "subTitle":
			return;
		case "peerLink":
			return;
		case "rights":
			return;
		case "scenes":
			return;
		}
		Namespace.super.process(item, reader);
	}

	private List<MediaContent> parseMediaGroup(XMLStreamReader reader) throws XMLStreamException {
		List<MediaContent> list = new ArrayList<>();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("group".equals(reader.getLocalName())) {
					return list;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				if ("content".equals(reader.getLocalName())) {
					list.add(parseMediaContent(reader));
				}
				break;
			}
		}
		return list;
	}

	private MediaContent parseMediaContent(XMLStreamReader reader) throws XMLStreamException {
		MediaContent mediaContent = new MediaContent();
		mediaContent.setUrl(reader.getAttributeValue(null, "url"));
		String fileSize = reader.getAttributeValue(null, "fileSize");
		if (fileSize != null) {
			try {
				mediaContent.setFileSize(Long.parseLong(fileSize.trim()));
			} catch (NumberFormatException e) {
			}
		}
		mediaContent.setType(reader.getAttributeValue(null, "type"));
		mediaContent.setMedium(reader.getAttributeValue(null, "medium"));
		mediaContent.setIsDefault(reader.getAttributeValue(null, "isDefault"));
		String bitrate = reader.getAttributeValue(null, "bitrate");
		if (bitrate != null) {
			try {
				mediaContent.setBitrate(Long.parseLong(bitrate.trim()));
			} catch (NumberFormatException e) {
			}
		}
		String framerate = reader.getAttributeValue(null, "framerate");
		if (framerate != null) {
			try {
				mediaContent.setFramerate(Long.parseLong(framerate.trim()));
			} catch (NumberFormatException e) {
			}
		}
		mediaContent.setSamplingrate(reader.getAttributeValue(null, "samplingrate"));
		mediaContent.setChannels(reader.getAttributeValue(null, "channels"));
		String duration = reader.getAttributeValue(null, "duration");
		if (duration != null) {
			PodcastParser.DURATION_STRINGS.add(duration);
			try {
				mediaContent.setDuration(Duration.ofSeconds(Integer.parseInt(duration.trim())));
			} catch (NumberFormatException e) {
			}
		}
		String height = reader.getAttributeValue(null, "height");
		if (height != null) {
			try {
				mediaContent.setHeight(Integer.parseInt(height.trim()));
			} catch (NumberFormatException e) {
			}
		}
		String width = reader.getAttributeValue(null, "width");
		if (width != null) {
			try {
				mediaContent.setWidth(Integer.parseInt(width.trim()));
			} catch (NumberFormatException e) {
			}
		}
		mediaContent.setLang(reader.getAttributeValue(null, "lang"));
		return mediaContent;
	}

	private Image parseImage(XMLStreamReader reader) throws XMLStreamException {
		Image image = new Image();
		image.setUrl(reader.getAttributeValue(null, "url"));
		String width = reader.getAttributeValue(null, "width");
		if (width != null) {
			try {
				image.setWidth(Integer.parseInt(width.trim()));
			} catch (NumberFormatException e) {	}
		}
		String height = reader.getAttributeValue(null, "height");
		if (height != null) {
			try {
				image.setHeight(Integer.parseInt(height.trim()));
			} catch (NumberFormatException e) {	}
		}
		String time = Attributes.get("time").from(reader).orElse(null);
		image.setTime(Durations.parse(time));
		return image;
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
