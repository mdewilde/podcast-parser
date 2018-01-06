package be.ceau.podcastparser;

import org.junit.Assert;
import org.junit.Test;

import be.ceau.podcastparser.exceptions.NotPodcastFeedException;
import be.ceau.podcastparser.test.provider.ClasspathFileProvider;
import be.ceau.podcastparser.test.wrappedxml.WrappedXml;

public class QuietResolverTest {

	@Test
	public void test() {
		WrappedXml xml = ClasspathFileProvider.read("/quiet_resolver_issue.xml");
		try {
			new PodcastParser().parse(xml.getXml());
		} catch (Exception e) {
			Assert.assertEquals(NotPodcastFeedException.class, e.getClass());
			Assert.assertEquals("the input appears to be HTML", e.getMessage());
		}
	}
	
}