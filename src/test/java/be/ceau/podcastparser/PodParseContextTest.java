package be.ceau.podcastparser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Assert;
import org.junit.Test;

import be.ceau.podcastparser.namespace.callback.NoOpNamespaceCallback;
import be.ceau.podcastparser.test.provider.ClasspathFileProvider;

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
					PodParseContext ctx = new PodParseContext("rss", streamReader, new NoOpNamespaceCallback());
					
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
