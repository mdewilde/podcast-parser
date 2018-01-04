package be.ceau.podcastparser;

import org.junit.Test;

import be.ceau.podcastparser.models.core.Feed;
import be.ceau.podcastparser.test.provider.ClasspathFileProvider;
import be.ceau.podcastparser.test.wrappedxml.FileXml;

public class AttributeIssueTest {

	@Test
	public void test() {
		FileXml xml = ClasspathFileProvider.read("/attribute_issue.xml");
		
		Feed feed = new PodcastParser().parse(xml.getXml());

	}
	
}
