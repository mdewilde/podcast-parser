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
package be.ceau.podcastparser.filter;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.PodcastParser;
import be.ceau.podcastparser.json.JsonFactory;
import be.ceau.podcastparser.namespace.callback.NamespaceCountingCallbackHandler;
import be.ceau.podcastparser.test.provider.FilesProvider;
import be.ceau.podcastparser.util.EncounteredElement;

public class FilterTest {

	private static final Logger logger = LoggerFactory.getLogger(FilterTest.class);

	private static final String NS = "http://www.itunes.com/dtds/podcast-1.0.dtd";
	
	@Test
	public void namespaceFilterTest() {

		ExcludeNamespaceFilter nsFilter = new ExcludeNamespaceFilter(NS);
		
		NamespaceCountingCallbackHandler callback = new NamespaceCountingCallbackHandler();
		PodcastParser parser = new PodcastParser(callback, nsFilter);
		new FilesProvider().parallelStream()
				.limit(10000)
				.forEach(wrap -> {
					try {
						parser.parse(wrap.getXml());
					} catch (Exception e) {
					}
				});

		EncounteredElement en = callback.getCounter().getMap().keySet().stream().filter(e -> NS.equals(e.getNamespaceUri())).findFirst().orElse(null);
		
		if (en != null) {
			JsonFactory.write(en);
		}
		
		Assert.assertNull(en);
		logger.info("{}", callback.getCounter());

	}

}
