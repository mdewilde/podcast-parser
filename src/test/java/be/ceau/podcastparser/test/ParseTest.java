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
package be.ceau.podcastparser.test;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import be.ceau.podcastparser.PodcastParser;
import be.ceau.podcastparser.models.EncounteredElement;

public class ParseTest {

	private static final Logger logger = LoggerFactory.getLogger(ParseTest.class);

	private static final FilesProvider FILES_PROVIDER = new FilesProvider();
	
	private static final Random RANDOM = new SecureRandom();
	
	// @Test
	public void stax() {
		handleStax("983394039.txt");
	}
	
	@Test
	public void staxTest() throws IOException, SAXException, ParserConfigurationException {

		PodcastParser parser = new PodcastParser();
		
		FILES_PROVIDER.stream()
		//	.limit(50000)
			.forEach(wrap -> {
				// Bench c = new Bench();
				//logger.info("{}{}{}", System.lineSeparator(), wrap.getXml(), System.lineSeparator());
				try {
					parser.parse(wrap.getXml())
						// .ifPresent(feed -> logger.info("{}", feed))
					
						;
				} catch (Exception e) {
					// logger.error("{} -> ", wrap.getDescription(), e);
				}
				// c.stop().log(wrap.getDescription());
			});

		String report = PodcastParser.UNMAPPED.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().sum()))
				.entrySet()
				.stream()
				.sorted(Map.Entry.<EncounteredElement, Long>comparingByValue().reversed())
				.map(e -> String.format("%10d", e.getValue()) + " \t--> " + e.getKey())
				.collect(Collectors.joining(System.lineSeparator()));

		logger.info("{} {}", System.lineSeparator(), report);

		logger.info("DATE STRINGS");
		logger.info("{}", PodcastParser.DATE_STRINGS.stream().collect(Collectors.joining(System.lineSeparator())));
		logger.info("DURATION STRINGS");
		logger.info("{}", PodcastParser.DURATION_STRINGS.stream().collect(Collectors.joining(System.lineSeparator())));
	}
	
	// @Test
	public void randomlyParseFeeds() throws IOException, SAXException, ParserConfigurationException {
		
		//PodcastXmlParser parser = new PodcastXmlParser();
		PodcastParser parser = new PodcastParser();

		FILES_PROVIDER.parallelStream()
//				.limit(10000)
				.forEach(wrap -> {
					Bench c = new Bench();
					try {
						parser.parse(wrap.getXml());
//							.ifPresent(feed -> logger.info("{}", feed));

					//	Optional<Feed> feed = parser.parse(wrap.getXml());
					//	logger.info("{}", feed.get());
					} catch (Exception e) {
						logger.error("{} -> {}", wrap.getDescription(), e.getMessage());
					}
					c.stop().log(wrap.getDescription());
				});

		Bench b = new Bench();
		//logger.warn(parser.getStatistics().forPrint());
		b.stop().log("prepped stats for print");
	}
	
	private List<String> list = Arrays.asList("261623322.txt", "136732182.txt", "257164947.txt", "309562478.txt");

	
	// @Test
	public void atomTest() {
		for (int i = 0; i < 1; i++) {
			handleStax("418166339.txt");
		}
	}
	
	// @Test
	public void mediaTest() {
		handleStax("334638865.txt");
	}

	private void handleStax(String filename) {
		
		WrappedXml wrap = new FileProvider(filename).get();
		Bench bench = new Bench();
		try {
			new PodcastParser().parse(wrap.getXml())
			.ifPresent(f -> 
				logger.info("{}", f)
				);
		} catch (Exception e) {
			logger.error("{} -> {}", wrap.getDescription(), e.getMessage());
		}
		bench.stop().log(filename);

	}

	// @Test
	public void uhh() throws IOException {

//	    File uhh = new File("src/test/resources/uhh.txt");
//		String xml = FileProvider.toString(uhh);
//		PodcastXmlParser parser = new PodcastXmlParser();
//		try {
//			logger.debug(parser.parse(xml).toString());
//		} catch (Exception e) {
//			logger.error("uhh()", e);
//		}
		
	}
	
}
