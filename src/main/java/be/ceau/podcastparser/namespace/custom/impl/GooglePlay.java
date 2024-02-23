/*
	
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

import java.util.Set;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodcastParserContext;
import be.ceau.podcastparser.models.core.Item;
import be.ceau.podcastparser.models.support.Category;
import be.ceau.podcastparser.models.support.Image;
import be.ceau.podcastparser.models.support.Person;
import be.ceau.podcastparser.models.support.Rating;
import be.ceau.podcastparser.models.support.TypedString;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * @see <a href="http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd">Google Play
 *      podcasts schema</a>
 */
public class GooglePlay implements Namespace {

	private static final String NAME = "http://www.google.com/schemas/play-podcasts/1.0";
	private static final Set<String> ALTERNATIVE_NAMES = UnmodifiableSet.of("http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd");

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Set<String> getAlternativeNames() {
		return ALTERNATIVE_NAMES;
	}

	@Override
	public void process(PodcastParserContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "author":
			// The author of the podcast or episode. The author is specified in
			// the <channel> or <item> tags.
			ctx.getFeed().addAuthor(parseAuthor(ctx));
			break;
		case "block":
			String block = ctx.getElementText();
			ctx.getFeed().setBlock("yes".equalsIgnoreCase(block));
			break;
		case "category":
			ctx.getFeed().addCategory(parseCategory(ctx));
			break;
		case "description":
			ctx.getFeed().setDescription(parseDescription(ctx));
			break;
		case "email":
			// The email address of the podcast owner. This email will be used
			// to verify the ownership of the podcast during registration. The
			// email is only specified in the <channel> tag.
			ctx.getFeed().setEmail(ctx.getElementText());
			break;
		case "explicit":
			parseExplicit(ctx);
			break;
		case "image":
			// A URL that points to the the artwork of your podcast or episode.
			// The image can be specified in the <channel> or <item> tags.
			ctx.getFeed().addImage(parseImage(ctx));
			break;
		case "newFeedUrl":
			// Allows the podcast owner to change the URL where the RSS podcast
			// feed is located. After adding the tag, you should maintain the
			// old feed for 48 hours before retiring it. newFeedUrl is only
			// specified in the <channel> tag.
			Namespace.super.process(ctx);
			break;
		default:
			Namespace.super.process(ctx);
			break;
		}
	}

	@Override
	public void process(PodcastParserContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "author":
			item.addAuthor(parseAuthor(ctx));
			break;
		case "block":
			// If this tag is set to 'yes' in the <channel> tag, the podcast
			// will will not appear in Google Play Music. If this tag is set to
			// 'yes' in the <item> tag, that episode will not appear in Google
			// Play Music. If it's not set, it will be treated as 'no'.
			String block = ctx.getElementText();
			item.setBlock("yes".equalsIgnoreCase(block));
			break;
		case "description":
			item.setDescription(parseDescription(ctx));
			break;
		case "explicit":
			parseExplicit(ctx, item);
			break;
		case "image":
			item.addImage(parseImage(ctx));
			break;
		default:
			Namespace.super.process(ctx, item);
			break;
		}
	}

	private Person parseAuthor(PodcastParserContext ctx) throws XMLStreamException {
		Person author = new Person();
		author.setName(ctx.getElementText());
		return author;
	}

	// Specify the category that your podcast relates to. If more than
	// one category is specified, only the first one will be used.
	// Categories can only be specified in the <channel> tag. The 'text'
	// must match one of the pre-defined categories specified in the
	// help center article:
	// https://support.google.com/googleplay/podcasts/answer/6260341#spt
	private Category parseCategory(PodcastParserContext ctx) throws XMLStreamException {
		Category category = new Category();
		category.setName(ctx.getElementText());
		category.setScheme(NAME);
		return category;
	}

	/**
	 * A description of the podcast or episode. The description can be specified in the
	 * {@code <channel>} or {@code <item>} tags and must be plain-text (no markup allowed).
	 */
	private TypedString parseDescription(PodcastParserContext ctx) throws XMLStreamException {
		TypedString typedString = new TypedString();
		typedString.setText(ctx.getElementText());
		typedString.setType("plain");
		return typedString;
	}

	private void parseExplicit(PodcastParserContext ctx) throws XMLStreamException {
		parseExplicit(ctx, ctx.getFeed().getRating());
	}

	private void parseExplicit(PodcastParserContext ctx, Item item) throws XMLStreamException {
		parseExplicit(ctx, item.getRating());
	}

	private void parseExplicit(PodcastParserContext ctx, Rating rating) throws XMLStreamException {
		// Indicates whether the podcast or a specific episode contains
		// explicit material. If not specified, it is considered not
		// explicit. The explicit symbol will appear next to the podcast
		// when the tag is placed in the <channel> and next to the episode
		// when placed in the <item>. A 'clean' value can only be used in
		// the <item> tag, and it means this episode is not explicit, but
		// there's also an explicit version of of the same episode.
		rating.setExplicit(ctx.getElementText());
	}

	private Image parseImage(PodcastParserContext ctx) {
		String href = ctx.getAttribute("href");
		Image image = new Image();
		image.setUrl(href);
		return image;
	}

}