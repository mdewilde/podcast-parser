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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.exceptions.NotPodcastFeedException;
import be.ceau.podcastparser.namespace.callback.UnhandledElementCounter;
import be.ceau.podcastparser.test.provider.FilesProvider;
import be.ceau.podcastparser.test.wrappedxml.WrappedXml;

public class CorpusTest {

	private static final Logger logger = LoggerFactory.getLogger(CorpusTest.class);

	@Test
	public void corpusParseTest() {

		UnhandledElementCounter counter = new UnhandledElementCounter();
		PodcastParser parser = new PodcastParser(counter);
		new FilesProvider().stream()
				.limit(1999)
				.forEach(wrap -> {
					try {
						parser.parse(wrap.getXml());
					} catch (NotPodcastFeedException e) {
						handleHtml(wrap, e.getMessage());
					} catch (Exception e) {
						if (e.getMessage().contains("elementGetText() function expects")) {
							logger.error("{}", wrap.getFullPath(), e);
						} else {
							logger.error("{} -> {}", wrap.getFullPath(), e.getMessage());
						}
					}
				});
		
		logger.debug("{}", counter);
		logger.debug("end corpusParseTest()");

	}

	private void handleHtml(WrappedXml xml, String message) {
		if ("the input appears to be HTML".equals(message)) {
			if (!xml.delete()) {
				logger.warn("delete failed -> {}", xml.getFullPath());
			}
		}
		if ("root element must be rss or feed but it is html".equals(message)) {
			if (!xml.delete()) {
				logger.warn("delete failed -> {}", xml.getFullPath());
			}
		}
	}
	
}
