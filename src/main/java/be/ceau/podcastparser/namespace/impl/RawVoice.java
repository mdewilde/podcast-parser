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

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Image;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Link;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.Attributes;

/**
 * <p>
 * “RawVoice RSS” is a RSS module that supplements the {@code <enclosure>}
 * element function of RSS 2.0 as well as extend iTunes Media RSS (e.g.
 * {@code <itunes:xxx>}) and/or the Yahoo Media RSS modules by including extra
 * media information that would otherwise not be syndicated. RawVoice extends
 * the enclosures as well as the Apple iTunes/Yahoo Media RSS modules to include
 * additional media information such as TV specific ratings, live stream/embed
 * data, episode poster art, location and episode frequency.
 * </p>
 * 
 * @see http://www.rawvoice.com/services/tools-and-resources/rawvoice-rss-2-0-module-xmlns-namespace-rss2/
 */
public class RawVoice implements Namespace {

	private static final String NAME = "http://www.rawvoice.com/rawvoiceRssModule/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "donate":
			// one required attribute "href"
			String href = ctx.getAttribute("href");
			if (StringUtils.isNotBlank(href)) {
				Link link = new Link();
				link.setHref(href);
				// value may be specified but is not required
				link.setTitle(ctx.getElementText());
				link.setRel("donate");
				ctx.getFeed().addLink(link);
			}
			break;
		case "frequency":
			// the readable frequency of the media episodes
			ctx.getFeed().computeUpdateInfoIfAbsent().setPeriod(ctx.getElementText());
			break;
		case "location":
			// the readable location of the content
			ctx.getFeed().setLocation(ctx.getElementText());
			break;
		case "rating":
			/*
			 * Element value must be one of the following TV (V-chip) parental
			 * ratings.
			 * 
			 * TV-Y TV-Y7 TV-Y7-FV TV-G TV-PG TV-14 TV-MA
			 */
			ctx.getFeed().computeRatingIfAbsent().setText(ctx.getElementText()).setScheme("V-chip");
			break;
		case "subscribe":
			// Include subscription information. 
			// It has six attributes feed, itunes, googleplay, html, blubrry and stitcher.
			// The feed attribute is required. The itunes, googleplay, and html attributes are optional.
			for (int i = 0; i < ctx.getReader().getAttributeCount(); i++) {
				Link link = new Link();
				link.setHref(ctx.getReader().getAttributeValue(i));
				link.setRel("subscribe");
				link.setTitle(ctx.getReader().getAttributeLocalName(i));
				ctx.getFeed().addLink(link);
			}
			break;
		}
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "embed":
			// embed, if present as a sub-item of <item>, includes a block of
			// embed HTML markup that corresponds to the item’s media content.
			LoggerFactory.getLogger(Namespace.class).info("RawVoice embed --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "poster":
			/*
			 * poster, if present as a sub-item of <item>, specifies the artwork
			 * image for the specific media episode included within <item>. The
			 * artwork image should be in either JPEG or PNG format. A square
			 * image 600 x 600 pixels in size is recommended. The URL must end
			 * in “.jpg” or “.png”. It has one required attribute url.
			 * 
			 * <rawvoice:poster url=”http://www.example.com/path/to/poster.png”
			 * />
			 * 
			 * url value should be a complete URL including schema (http://) to
			 * the image.
			 * 
			 * Note: The <rawvoice:poster> element is nearly identical to the
			 * Yahoo Media <media:thumbnail> element except that there can be
			 * only one <rawvoice:poster> element in a given <item>.
			 * 
			 * Possible image sizes:
			 * 
			 * Square image either 300 x 300, 600 x 600 or 1400×1400 pixels in
			 * size. Letterbox (4:3 aspect ratio) image 320 x 240 or 640 x 480
			 * pixels in size. Widescreen (16:9 aspect ratio) image 640 x 360,
			 * 1280 x 72, or 1920 x 1080 pixels in size.
			 */
			Image poster = new Image();
			poster.setUrl(ctx.getAttribute("url"));
			poster.setTitle("poster");
			item.addImage(poster);
			break;
		case "isHD":
			/*
			 * isHd, if present as a sub-item of <item>, indicates if the video
			 * media specified in the <item>’s child <enclosure> is in High
			 * Definition (HD). Any video that is widescreen (16:9 aspect ratio)
			 * with a 720p, 720i, 1080p or 1080i resolution (or better) should
			 * specify the value “yes” to indicate it is HD.
			 * 
			 * <rawvoice:isHd>yes</rawvoice:isHd>
			 * 
			 * If this tag is present and set to “yes” (case insensitive), then
			 * the corresponding media content specified by item’s <enclosure>
			 * is assumed to be in HD. If the tag is not present or uses any
			 * other value, it is assumed the corresponding media content is not
			 * HD.
			 */
			LoggerFactory.getLogger(Namespace.class).info("RawVoice isHD --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "webm":
			/*
			 * webm, if present as a sub-item of <item> and <item> includes an
			 * <enclosure> item, specifies an alternative ‘WebM’ version of the
			 * video specified in <enclosure>. Media content should be identical
			 * to the media found in the <enclosure>. The src URL must end in
			 * “.webm”. It has one required attribute src and two optional
			 * attributes type and length.
			 * 
			 * <rawvoice:webm src=”http://www.example.com/path/to/media.webm”
			 * type=”video/webm” length=”753682846″ />
			 * 
			 * If this item is present, then the corresponding media content
			 * specified by item’s <enclosure> is assumed to be the primary
			 * format for playback. The WebM version is assumed to be an
			 * alternative format for playback on hardware/software that does
			 * not support the video format specified in the <enclosure>.
			 * 
			 * src value should specify URL to the media.
			 * 
			 * type value should specify the content type of source. e.g.
			 * video/webm
			 * 
			 * length value should specify the size of source file in bytes.
			 */
			LoggerFactory.getLogger(Namespace.class).info("RawVoice webm --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());
			break;
		case "metamark":
			/*
			 * additional meta information that may complement the enclosure of
			 * an item and/or may be used during the playback of the enclosure’s
			 * media. It has four attributes type, link, position and duration
			 * and may contain a value.
			 * 
			 * <rawvoice:metamark type=”tag” position=”480″>This is where I
			 * discuss how to properly beat eggs</rawvoice:metamark>
			 * 
			 * <rawvoice:metamark type=”ad” position=”120″ duration=”60″
			 * link=”http://www.example.com/path/to/advertisement.html”>campaign
			 * :5634</rawvoice:metamark>
			 * 
			 * <rawvoice:metamark type=”video”
			 * link=”http://www.example.com/path/to/media.mp4″ position=”240″ />
			 * 
			 * <rawvoice:metamark type=”comment”
			 * link=”http://www.example.com/path/to/comment/” position=”360″
			 * duration=”60″>Tom: Loved how you discuss how to make cookies with
			 * a toaster oven.</rawvoice:metamark>
			 * 
			 * If this item is present, the information within the metamark’s
			 * attributes and value correspond with the item’s <enclosure> and
			 * can be used for and possibly displayed during media playback.
			 * 
			 * link value should specify URL to the media, web page or image.
			 * 
			 * type value should specify the type of metamark. e.g. video
			 * 
			 * position value should specify the position in seconds where the
			 * metamark should be handled during playback.
			 * 
			 * duration value should specify the duration in seconds the
			 * metamark should be displayed.
			 * 
			 * Element attribute type must be one of the following and include
			 * the noted attributes and/or values.
			 * 
			 * video – Attributes link and position must be specified. The link
			 * value should be to a valid video in the same format as
			 * <enclosure>, e,g, http://example.com/path/to/video.mp4. audio –
			 * Attributes link and position must be specified. The link value
			 * should be to a valid audio in the same format as <enclosure>,
			 * e,g, http://example.com/path/to/video.mp3. image – Attributes
			 * link, position and duration must be specified. The link value
			 * should be to a valid image in jpg or png format. comment –
			 * Attributes position and duration must be specified, a value in
			 * the form of a textual comment should be present. Attribute link
			 * is optional. tag – Attributes position must be specified, a value
			 * in the form of a textual label should be present. ad – Attributes
			 * position and duration must be specified, a value in the form of a
			 * unique identifier may be present. Attribute link is optional.
			 * Note: the value is intended to be an identifier, it is not
			 * intended to be re-displayed. lowerthird – Attributes link,
			 * position and duration must be specified and a value in the form
			 * of a URL to a web page is optional. Note: This type is for video
			 * enclosures and intended for the video player to display the image
			 * specified in link as the lower third advertisement. The link
			 * value should be to a valid image in png format with appropriate
			 * transparency set if desired.
			 */
			String type = ctx.getAttribute("type");
			String link = ctx.getAttribute("link");

			// position in seconds where the metamark should be handled during playback
			String position = ctx.getAttribute("position");
			
			// duration value should specify the duration in seconds the metamark should be displayed
			String duration = ctx.getAttribute("duration");
			
			String text = ctx.getElementText();
			LoggerFactory.getLogger(Namespace.class).info("RawVoice metamark --> {} {}", Attributes.toString(ctx.getReader()), ctx.getElementText());

			break;
		}
	}

}

/*

	corpus stats

     27416 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=embed attributes=[]]
     13116 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=poster attributes=[url]]
     
      6727 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=location attributes=[]]
      6565 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=frequency attributes=[]]
      5240 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes]]
      4486 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=rating attributes=[]]
      
      3591 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=isHD attributes=[]]
      
       712 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html]]
       529 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=donate attributes=[href]]
       507 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes]]
       
       355 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=webm attributes=[src, length, type]]
       272 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[type]]
       
       128 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, blubrry]]
       126 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[link, type]]
       124 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher]]
       112 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html]]
       111 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, tunein, blubrry]]
       108 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher]]
       105 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html, blubrry]]
        85 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html, tunein, blubrry]]
        71 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay]]
        62 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html]]
        50 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, blubrry]]
        48 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, blubrry]]
        46 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html, blubrry]]
        44 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, html, tunein]]
        39 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, blubrry]]
        35 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html]]
        34 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=webm attributes=[src, type]]
        32 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[position, type]]
        31 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, stitcher, tunein]]
        29 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html, blubrry]]
        29 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, position, type]]
        26 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[link, position, type]]
        26 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html, blubrry]]
        24 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, tunein, blubrry]]
        22 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, tunein]]
        14 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html, tunein, blubrry]]
        11 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, blubrry]]
         9 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, link, position, type]]
         8 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, tunein]]
         6 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher]]
         6 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html, tunein]]
         5 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, tunein, blubrry]]
         5 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, html, blubrry]]
         5 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, stitcher]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, link, type]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, tunein]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, itunes, html, tunein, blubrry]]
         4 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, html]]
         3 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, html]]
         3 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html, tunein, blubrry]]
         3 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, stitcher, tunein, blubrry]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, stitcher, html, tunein]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, blubrry]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=item localName=metamark attributes=[duration, type]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, tunein, blubrry]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, tunein]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, itunes, html, tunein]]
         2 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, blubrry]]
         1 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, tunein]]
         1 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, googleplay, stitcher, html, tunein, blubrry]]
         1 	--> http://www.rawvoice.com/rawvoiceRssModule/ level=feed localName=subscribe attributes=[feed, stitcher, tunein, blubrry]]

*/
