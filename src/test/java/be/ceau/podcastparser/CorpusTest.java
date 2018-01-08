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

import be.ceau.podcastparser.namespace.callback.UnhandledNamespaceCounter;
import be.ceau.podcastparser.test.provider.FilesProvider;

public class CorpusTest {

	private static final Logger logger = LoggerFactory.getLogger(CorpusTest.class);

	@Test
	public void corpusParseTest() {

		UnhandledNamespaceCounter callback = new UnhandledNamespaceCounter();
		PodcastParser parser = new PodcastParser(callback);
		new FilesProvider().parallelStream()
				.forEach(wrap -> {
					try {
						parser.parse(wrap.getXml());
					} catch (Exception e) {
						logger.error("{} -> {}", wrap.getFullPath(), e.getMessage());
					}
				});
		
		logger.debug("{}", callback);

	}
	
}
