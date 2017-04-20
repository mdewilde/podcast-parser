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

import be.ceau.podcastparser.stax.PodcastParser;
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
			String duration = reader.getElementText();
			PodcastParser.DURATION_STRINGS.add(duration);
			item.setDuration(duration);
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

	corpus stats
	
   7799656 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=duration attributes=[]]
   7707696 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitle attributes=[]]
   6934210 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[]]
   6911181 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=explicit attributes=[]]
   6825643 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=summary attributes=[]]
   5512949 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=keywords attributes=[]]
   4414316 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[href]]
    703209 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=block attributes=[]]
    272165 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
    194437 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=author attributes=[]]
    189391 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href]]
    189127 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=order attributes=[]]
    182775 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=explicit attributes=[]]
    178869 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subtitle attributes=[]]
    168997 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[]]
    162615 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summary attributes=[]]
     98654 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=keywords attributes=[]]
     45938 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=block attributes=[]]
     43811 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=category attributes=[text]]
     17952 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[]]
      6324 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=new-feed-url attributes=[]]
      3607 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=isClosedCaptioned attributes=[]]
      3499 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[url]]
      3086 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=album attributes=[]]
      2247 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[]]
      2203 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=duration attributes=[]]
      2166 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=keywords attributes=[]]
      1965 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=email attributes=[]]
      1840 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=description attributes=[]]
      1688 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=owner attributes=[]]
      1450 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=new-feed-url attributes=[]]
      1219 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=link attributes=[rel, href, type]]
      1086 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=name attributes=[]]
      1038 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=pubDate attributes=[]]
       835 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=category attributes=[]]
       824 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subitle attributes=[]]
       546 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=keyword attributes=[]]
       514 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=title attributes=[]]
       502 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[text]]
       432 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=artist attributes=[]]
       395 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=email attributes=[]]
       384 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=summary attributes=[]]
       289 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=copyright attributes=[]]
       271 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=complete attributes=[]]
       246 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[alt, href]]
       242 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=url attributes=[]]
       238 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=category attributes=[code]]
       237 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=link attributes=[]]
       212 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=link attributes=[href]]
       202 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=length attributes=[]]
       202 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=link attributes=[rel, href, type]]
       163 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[]]
       148 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=synopsis attributes=[]]
       129 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[]]
       118 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitle attributes=[]]
       106 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=item localName=subtitle attributes=[]]
       106 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=item localName=summary attributes=[]]
       106 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=item localName=keywords attributes=[]]
       106 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=item localName=explicit attributes=[]]
       106 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=item localName=duration attributes=[]]
       106 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=item localName=author attributes=[]]
       105 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=explicit attributes=[]]
       105 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=pic attributes=[]]
        85 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=isCloseCaptioned attributes=[]]
        70 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[href]]
        64 	--> http://www.itunes.com/dtds/podcast-1.0.dtd" version="2.0" level=item localName=author attributes=[]]
        64 	--> http://www.itunes.com/dtds/podcast-1.0.dtd" version="2.0" level=item localName=summary attributes=[]]
        64 	--> http://www.itunes.com/dtds/podcast-1.0.dtd" version="2.0" level=item localName=keywords attributes=[]]
        63 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[WIDTH, href, HEIGHT]]
        53 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=provider attributes=[]]
        33 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=name attributes=[]]
        31 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image-small attributes=[href]]
        29 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=excplicit attributes=[]]
        28 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=item localName=duration attributes=[]]
        28 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=item localName=summary attributes=[]]
        28 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[]]
        28 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=item localName=keywords attributes=[]]
        28 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitle attributes=[]]
        25 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=podcastskeywords attributes=[]]
        23 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitle attributes=[lang]]
        23 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subTitle attributes=[]]
        21 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=copyright attributes=[]]
        19 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=duration attributes=[]]
        18 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
        17 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=author attributes=[]]
        17 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=artwork attributes=[]]
        16 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summary attributes=[]]
        16 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href]]
        15 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=email attributes=[]]
        15 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=name attributes=[]]
        15 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[actor, director]]
        15 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[]]
        14 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=description attributes=[]]
        14 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=catago attributes=[text]]
        12 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[rel, href, type]]
        12 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=link attributes=[href]]
        11 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subtitle attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=owner attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=category attributes=[text]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=name attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=summary attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=email attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=subtitle attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=author attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=explicit attributes=[]]
        11 	--> http://www.itunes.com/dtds/podcast-1.0.dtd/ level=feed localName=image attributes=[href]]
        10 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=isClosedCaptioned attributes=[text]]
        10 	--> www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitle attributes=[]]
        10 	--> www.itunes.com/dtds/podcast-1.0.dtd level=item localName=keywords attributes=[]]
        10 	--> www.itunes.com/dtds/podcast-1.0.dtd level=item localName=duration attributes=[]]
        10 	--> www.itunes.com/dtds/podcast-1.0.dtd level=item localName=summary attributes=[]]
        10 	--> www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[]]
         9 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=explicit attributes=[text]]
         9 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[alt, href]]
         9 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=link attributes=[]]
         8 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=explicit attributes=[]]
         8 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=link attributes=[rel, width, href, type, height]]
         8 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[rel, href]]
         8 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=new_feed_url attributes=[]]
         7 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=keyword attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=item localName=duration attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=year attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=item localName=keywords attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=item localName=subtitle attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=item localName=author attributes=[]]
         6 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=keywords attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=item localName=summary attributes=[]]
         6 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subitle attributes=[]]
         5 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=br attributes=[]]
         5 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subcategory attributes=[text]]
         4 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[Text]]
         4 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=pubDate attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=link attributes=[rel, href]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=language attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href, title]]
         3 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=item localName=keywords attributes=[]]
         3 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=item localName=summary attributes=[]]
         3 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=item localName=duration attributes=[]]
         3 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=item localName=author attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=provider attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[url]]
         3 	--> https://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=new-feed-url attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=image attributes=[href, type]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=catagory attributes=[text]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=isClosedCaptioned attributes=[]]
         3 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=item localName=subtitle attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=bitrate attributes=[]]
         3 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=explicit attributes=[text]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=title attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=a attributes=[href]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=explict attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=artist attributes=[]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=u attributes=[]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=i attributes=[]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=track attributes=[]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[width, href, height]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subtitle attributes=[]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=author attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=email attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=name attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=authors attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=explicit attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summary attributes=[]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[src]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=name attributes=[]]
         2 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitel attributes=[]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subtitle attributes=[]]
         2 	--> http//www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=author attributes=[]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
         2 	--> //www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=pubdate attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=summary attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=image attributes=[href]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=new-feed-url attributes=[href]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[link, href]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=images attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=summary attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=duration attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=explicit attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=name attributes=[]]
         1 	--> hhtp://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=category attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd" version="2.0" level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=creator attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=feed localName=image attributes=[href]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[name, email]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=author attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=explicit attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[name]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitles attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=name attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=feed localName=category attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd" version="2.0" level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=image attributes=[href]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=category attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=explicit attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=owner attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=summary attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=keywords attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=managingEditor attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=series attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=name attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=image attributes=[href]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd" version="2.0" level=feed localName=name attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=album attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=category attributes=[text]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summary attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=author attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=email attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=subtitle attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=publisher attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=name attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=catagory attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=summary attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=summary attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summmary attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=summary attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=explicit attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=news-feed-url attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=Riverbroncs.com attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summary attributes=[space]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=image attributes=[href]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=summary attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=keywords attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=subtitle attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast,1.0.dtd level=feed localName=category attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=artwork attributes=[href]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=summary attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=author attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=email attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=link attributes=[rel, type]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd level=item localName=explicit attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=category attributes=[text]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=subTitle attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=feed localName=owner attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast=1.0.dtd level=feed localName=name attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=webMaster attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0dtd level=feed localName=category attributes=[text]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=name attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=image attributes=[href, type]]
         1 	--> http://www.itunes.com/dtds/podcast-'1.0'.dtd level=feed localName=image attributes=[href]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=name attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast- 1.0.dtd level=feed localName=author attributes=[]]
         1 	-->  http://www.itunes.com/dtds/podcast-1.0.dtd  level=feed localName=summary attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=email attributes=[]]
         1 	--> www.itunes.com/dtds/podcast-1.0.dtd level=feed localName=keywords attributes=[]]
         1 	--> http://www.itunes.com/dtds/podcast-2.0.dtd level=feed localName=category attributes=[text]]

*/