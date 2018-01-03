package be.ceau.podcastparser;

import org.junit.Assert;
import org.junit.Test;

import be.ceau.podcastparser.exceptions.NotPodcastFeedException;
import be.ceau.podcastparser.test.provider.ClasspathFileProvider;

public class QuietResolverTest {

	@Test
	public void test() {
		WrappedXml xml = ClasspathFileProvider.read("/quiet_resolver_issue.xml");
		try {
			new PodcastParser().parse(xml.getXml());
		} catch (Exception e) {
			Assert.assertEquals(NotPodcastFeedException.class, e.getClass());
			Assert.assertEquals("the document appears to be HTML", e.getMessage());
		}
	}
	
}
