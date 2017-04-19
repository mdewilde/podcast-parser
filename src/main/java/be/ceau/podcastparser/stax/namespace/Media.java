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
package be.ceau.podcastparser.stax.namespace;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.stax.PodcastParser;
import be.ceau.podcastparser.stax.models.Image;
import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.stax.models.MediaContent;
import be.ceau.podcastparser.stax.models.Rating;
import be.ceau.podcastparser.stax.models.UnmappedElement;
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
				} else {
					PodcastParser.UNMAPPED
							.computeIfAbsent(new UnmappedElement(reader, "media:group"), x -> new LongAdder())
							.increment();
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
		Durations.of(reader.getAttributeValue(null, "time")).ifPresent(image::setTime);
		return image;
	}

}
