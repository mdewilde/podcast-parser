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

import java.util.Set;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.stax.models.Category;
import be.ceau.podcastparser.stax.models.Feed;
import be.ceau.podcastparser.stax.models.Image;
import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.stax.models.Person;
import be.ceau.podcastparser.stax.models.Rating;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * @see http://lists.apple.com/archives/syndication-dev/2005/Nov/msg00002.html
 */
public class ITunes implements Namespace {

	private static final Logger logger = LoggerFactory.getLogger(ITunes.class);

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.itunes.com/dtds/podcast-1.0.dtd", "itunes");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	/**
	 * @param feed
	 *            {@link Feed} instance in the process of being built
	 * @param reader
	 *            {@link XMLStreamReader} instance, just having processed a
	 *            {@link XMLStreamConstants#START_ELEMENT} event with this
	 *            namespace.
	 * @throws XMLStreamException
	 */
	@Override
	public void process(Feed feed, XMLStreamReader reader) throws XMLStreamException {

		switch (reader.getLocalName()) {
		case "author":
			Person author = new Person();
			author.setName(reader.getElementText());
			feed.addAuthor(author);
			return;
		case "block":
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
			return;
		case "category":
			feed.addCategory(parseCategory(reader));
			return;
		case "explicit":
			feed.setExplicit(reader.getElementText());
			return;
		case "keywords":
			// comma separated list of keywords
			String keywords = reader.getElementText();
			if (keywords != null) {
				String[] split = keywords.split(",");
				for (int i = 0; i < split.length; i++) {
					feed.addKeyword(split[i].trim());
				}
			}
			return;
		case "owner":
			feed.setOwner(parseOwner(reader));
			return;
		case "subtitle":
			// The contents of this tag are shown in the Description column in iTunes. 
			// The subtitle displays best if it is only a few words long.
			String subtitle = reader.getElementText();
			if (subtitle != null && !subtitle.trim().isEmpty()) {
				feed.setSubtitle(subtitle);
			}
			return;
		case "summary":
			// The contents of this tag are shown in a separate window that appears when the "circled i" in 
			// the Description column is clicked. It also appears on the iTunes Music Store page for your podcast.
			// This field can be up to 4000 characters. If <itunes:summary> is not included, 
			// the contents of the <description> tag are used.
			return;
		case "image":
			feed.addImage(parseImage(reader));
			return;
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
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {

		switch (reader.getLocalName()) {
		case "author":
			Person person = new Person();
			person.setEmail(reader.getElementText());
			item.addAuthor(person);
			return;
		case "block":
			return;
		case "duration":
			item.setDuration(reader.getElementText());
			return;
		case "explicit":
			Rating rating = item.getRating().orElse(new Rating());
			rating.setExplicit(reader.getElementText());
			item.setRating(rating);
			return;
		case "keywords":
			// comma separated list of keywords
			String keywords = reader.getElementText();
			if (keywords != null) {
				String[] split = keywords.split(",");
				for (int i = 0; i < split.length; i++) {
					item.addKeyword(split[i].trim());
				}
			}
			return;
		case "subtitle":
			String subtitle = reader.getElementText();
			if (subtitle != null && !subtitle.trim().isEmpty()) {
				item.setSubtitle(subtitle);
			}
			return;
		case "summary":
			return;
		case "image":
			item.addImage(parseImage(reader));
			return;
		}

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
	private Category parseCategory(XMLStreamReader reader) throws XMLStreamException {
		String text = reader.getAttributeValue(null, "text");
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				if ("category".equals(reader.getLocalName())) {
					String subcategory = reader.getAttributeValue(null, "text");
					return new Category(text, subcategory);
				} else {
					logger.warn("unexpected element {} inside itunes:category element", reader.getLocalName());
					break;
				}
			case XMLStreamConstants.END_ELEMENT:
				return new Category(text);
			}
		}
		return new Category(text);
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
	private Image parseImage(XMLStreamReader reader) throws XMLStreamException {
		Image image = new Image();
		String href = reader.getAttributeValue(null, "href");
		image.setUrl(href);
		return image;
	}

	private Person parseOwner(XMLStreamReader reader) throws XMLStreamException {
		Person person = new Person();
		while (reader.hasNext()) {
			switch (reader.next()) {
			case XMLStreamConstants.END_ELEMENT:
				if ("owner".equals(reader.getLocalName())) {
					return person;
				}
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName()) {
				case "name":
					person.setName(reader.getElementText());
					break;
				case "email":
					person.setEmail(reader.getElementText());
					break;
				}
			}
		}
		return person;
	}

}

/*
 * 1098819 --> subtitle 1064935 --> duration 988252 --> keywords 975016 -->
 * explicit 973032 --> author 948212 --> summary 397944 --> image 107323 -->
 * category 92548 --> block 17607 --> email 17560 --> owner 16870 --> name 7123
 * --> order 1625 --> album 1029 --> isClosedCaptioned 980 --> new-feed-url 802
 * --> link 612 --> pubDate 409 --> title 103 --> complete 42 --> description 8
 * --> keyword 5 --> copyright 3 --> bitrate 2 --> artist 2 --> url 1 -->
 * language
 */