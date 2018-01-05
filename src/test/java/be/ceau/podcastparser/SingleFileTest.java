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
package be.ceau.podcastparser;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.json.JsonFactory;
import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.test.wrappedxml.FileXml;
import be.ceau.podcastparser.test.wrappedxml.WrappedXml;

public class SingleFileTest {

	private static final Logger logger = LoggerFactory.getLogger(SingleFileTest.class);

	private static final Path PATH = Paths.get(System.getProperty("user.home"), "podcastfinder", "corpus", "38998.xml");

	@Test
	public void singleFileTest() {

		WrappedXml wrap = FileXml.instance(PATH);
		PodcastParser parser = new PodcastParser();

		try {
			Feed feed = parser.parse(wrap.getXml());
			logger.info("{} -> {}", PATH, JsonFactory.write(feed));
		} catch (Exception e) {
			logger.error("{}", wrap.getFullPath(), e);
		}

	}

}
