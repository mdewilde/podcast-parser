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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import be.ceau.podcastparser.exceptions.NotPodcastFeedException;
import be.ceau.podcastparser.namespace.callback.NamespaceCountingCallbackHandler;
import be.ceau.podcastparser.namespace.callback.UnhandledElementCounter;
import be.ceau.podcastparser.test.provider.ZipFilesProvider;

public class ParseTest {

	private static final Logger logger = LoggerFactory.getLogger(ParseTest.class);

	
	public void countEverything() throws IOException, SAXException, ParserConfigurationException {

		NamespaceCountingCallbackHandler callback = new NamespaceCountingCallbackHandler();
		PodcastParser parser = new PodcastParser(callback);

		try (ZipFilesProvider provider = new ZipFilesProvider()) {
			provider
			.parallelStream()
//			.stream()
			.limit(50000)
			.forEach(wrap -> {
				try {
					parser.parse(wrap.getXml());
				} catch (NotPodcastFeedException e) {
					if ("root element must be rss or feed but it is html".equals(e.getMessage())) {
						try {
							if (!wrap.delete()) {
								logger.warn("delete failed -> {}", wrap.getFullPath());
							}
						} catch (Exception ioe) {
							logger.error("{} -> {}", wrap.getFullPath(), ioe.getMessage());
						}
					}
				} catch (Exception e) {
					logger.error("{} -> {}", wrap.getFullPath(), e.getMessage());
				}
				// c.stop().log(wrap.getDescription());
			});
		}
		System.out.println(callback);
	//	logger.info("{} {}", System.lineSeparator(), handler.toString());

	}
	
	@Test
	public void countUnhandledElements() throws IOException, SAXException, ParserConfigurationException {

		UnhandledElementCounter counter = new UnhandledElementCounter();
		PodcastParser parser = new PodcastParser(counter);
		final AtomicLong adder = new AtomicLong(0L);
		try (ZipFilesProvider provider = new ZipFilesProvider()) {
			provider
			.parallelStream()
			.limit(50000)
			.forEach(wrap -> {
				long l = adder.incrementAndGet();
				if (l % 100 == 0) {
					logger.info("instance {}", l);
				}
				try {
					parser.parse(wrap.getXml());
				} catch (Exception e) {
					// logger.error("{} -> {}", wrap.getDescription(), e.getMessage());
				}
			});
		}
		System.out.println(counter);

	}

	
}
