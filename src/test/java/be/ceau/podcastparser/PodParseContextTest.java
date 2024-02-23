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
package be.ceau.podcastparser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Assert;
import org.junit.Test;

import be.ceau.podcastparser.test.provider.ClasspathFileProvider;
import be.ceau.podcastparser.test.wrappedxml.WrappedXml;

public class PodParseContextTest {

	@Test
	public void skip() throws XMLStreamException {
		
		WrappedXml xml = ClasspathFileProvider.read("/uyd.xml");
		
		XMLInputFactory factory = XMLInputFactory.newFactory();
		factory.setXMLResolver(new QuietResolver());

		XMLStreamReader streamReader = factory.createXMLStreamReader(xml.getReader());
		while (streamReader.hasNext()) {
			switch (streamReader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				switch (streamReader.getLocalName()) {
				case "rss": {
					PodcastParserContext ctx = new PodcastParserContext("rss", streamReader);
					
					while (streamReader.hasNext()) {
						if (streamReader.next() == XMLStreamConstants.START_ELEMENT) {
							if ("image".equals(streamReader.getLocalName())) {
								ctx.skip();
								Assert.assertEquals("image", streamReader.getLocalName());
								Assert.assertTrue(streamReader.isEndElement());
							}
						}
					}
				}
			}
		}
			}

	}
	
}
