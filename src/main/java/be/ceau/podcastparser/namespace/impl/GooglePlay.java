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

import java.util.Set;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.Person;
import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * @see http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd
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
	public void process(PodParseContext ctx) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "author":
			// The author of the podcast or episode. The author is specified in
			// the <channel> or <item> tags.
			Person author = new Person();
			author.setName(ctx.getElementText());
			ctx.getFeed().addAuthor(author);
			break;
		case "email":
			// The email address of the podcast owner. This email will be used
			// to verify the ownership of the podcast during registration. The
			// email is only specified in the <channel> tag.
			break;
		case "image":
			// A URL that points to the the artwork of your podcast or episode.
			// The image can be specified in the <channel> or <item> tags.
			break;
		case "description":
			// A description of the podcast or episode. The description can be
			// specified in the <channel> or <item> tags and must be plain-text
			// (no markup allowed).
			break;
		case "newFeedUrl":
			// Allows the podcast owner to change the URL where the RSS podcast
			// feed is located. After adding the tag, you should maintain the
			// old feed for 48 hours before retiring it. newFeedUrl is only
			// specified in the <channel> tag.
			break;
		case "category":
			// Specify the category that your podcast relates to. If more than
			// one category is specified, only the first one will be used.
			// Categories can only be specified in the <channel> tag. The 'text'
			// must match one of the pre-defined categories specified in the
			// help center article:
			// https://support.google.com/googleplay/podcasts/answer/6260341#spt
			break;
		}
		Namespace.super.process(ctx);
	}

	@Override
	public void process(PodParseContext ctx, Item item) throws XMLStreamException {
		switch (ctx.getReader().getLocalName()) {
		case "description":
			// A description of the podcast or episode. The description can be
			// specified in the <channel> or <item> tags and must be plain-text
			// (no markup allowed).
			break;
		case "explicit":
			// Indicates whether the podcast or a specific episode contains
			// explicit material. If not specified, it is considered not
			// explicit. The explicit symbol will appear next to the podcast
			// when the tag is placed in the <channel> and next to the episode
			// when placed in the <item>. A 'clean' value can only be used in
			// the <item> tag, and it means this episode is not explicit, but
			// there's also an explicit version of of the same episode.
			break;
		case "block":
			// If this tag is set to 'yes' in the <channel> tag, the podcast
			// will will not appear in Google Play Music. If this tag is set to
			// 'yes' in the <item> tag, that episode will not appear in Google
			// Play Music. If it's not set, it will be treated as 'no'.
			break;
		}
		Namespace.super.process(ctx, item);
	}

}

/*

	corpus stats
	
    210007 	--> http://www.google.com/schemas/play-podcasts/1.0 level=item localName=explicit attributes=[]]
    177359 	--> http://www.google.com/schemas/play-podcasts/1.0 level=item localName=description attributes=[]]
    165902 	--> http://www.google.com/schemas/play-podcasts/1.0 level=item localName=author attributes=[]]
    121355 	--> http://www.google.com/schemas/play-podcasts/1.0 level=item localName=image attributes=[href]]
     44434 	--> http://www.google.com/schemas/play-podcasts/1.0 level=item localName=block attributes=[]]
      5098 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=item localName=description attributes=[]]
      4744 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=explicit attributes=[]]
      3867 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=email attributes=[]]
      3691 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=description attributes=[]]
      3622 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=author attributes=[]]
      3532 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=image attributes=[href]]
      3419 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=feed localName=category attributes=[text]]
      3010 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=feed localName=email attributes=[]]
      2060 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=feed localName=image attributes=[href]]
      1938 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=feed localName=description attributes=[]]
      1401 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=category attributes=[text]]
       655 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=feed localName=explicit attributes=[]]
       402 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=item localName=explicit attributes=[]]
       307 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=block attributes=[]]
       159 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=item localName=image attributes=[href]]
        14 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=feed localName=author attributes=[]]
         6 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=image attributes=[]]
         3 	--> http://www.google.com/schemas/play-podcasts/1.0 level=item localName=email attributes=[]]
         2 	--> http://www.google.com/schemas/play-podcasts/1.0/play-podcasts.xsd level=item localName=block attributes=[]]
         1 	--> http://www.google.com/schemas/play-podcasts/1.0 level=feed localName=category attributes=[]]
	
*/

