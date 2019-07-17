/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser;

import java.time.Duration;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;

import be.ceau.podcastparser.exceptions.PodcastParserException;
import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.test.provider.ClasspathFileProvider;
import be.ceau.podcastparser.test.wrappedxml.WrappedXml;

/**
 * <p>
 * Parse and validate feed for Uhh Yeah Dude.
 * </p>
 * <p>
 * Retrieved from https://feeds.feedburner.com/uhhyeahdude/podcast on December 10, 2017
 * </p>
 */
public class UydTest {

	@Test
	public void parseUhh() throws XMLStreamException, PodcastParserException {
		WrappedXml xml = ClasspathFileProvider.read("/uyd.xml");

		Feed feed = new PodcastParser().parse(xml.getXml());

		Assert.assertEquals(Duration.ofMinutes(60), feed.getTtl());
		Assert.assertEquals("Uhh Yeah Dude", feed.getTitle());

 
		Assert.assertEquals("A weekly roundup of America through the eyes of two American Americans.", feed.getDescription().getText());
		Assert.assertEquals("en", feed.getLanguage());
		Assert.assertEquals("A weekly round up of America through the eyes of two American Americans", feed.getSubtitle());

		feed.getAuthors();
		feed.getCategories();
		feed.getCloud();
		feed.getContributors();
		feed.getCopyright();
		feed.getDescription();
		feed.getDocs();
		feed.getGenerator();
		Assert.assertNull(feed.getGeoPoint());
		feed.getId();
		feed.getImages();
		feed.getItems();
		feed.getKeywords();
		feed.getLanguage();
		feed.getLastBuildDate();
		feed.getLinks();
		feed.getLocation();
		feed.getManagingEditor();
		feed.getOtherValues();
		feed.getOwner();
		feed.getPubDate();
		feed.getRating();
		feed.getSkipDays();
		feed.getSkipHours();
		feed.getTextInput();
		feed.getUpdateInfo();
		feed.getWebMaster();

	}

}
