package be.ceau.podcastparser;

import org.junit.Test;

import be.ceau.podcastparser.models.Feed;
import be.ceau.podcastparser.test.provider.ClasspathFileProvider;

public class AttributeIssueTest {

	@Test
	public void test() {
		WrappedXml xml = ClasspathFileProvider.read("/attribute_issue.xml");
		
		Feed feed = new PodcastParser().parse(xml.getXml());

	}
	
}
