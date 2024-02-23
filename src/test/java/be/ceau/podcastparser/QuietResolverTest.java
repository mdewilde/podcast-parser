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
