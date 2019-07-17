/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.namespace;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;

/**
 * Ensure that {@link NamespaceFactory} can provide an instance of every {@link Namespace} in this
 * library
 */
public class NamespaceFactoryTest {

	@Test
	public void namespaceFactoryCanProvideAllImplementations() {
		Reflections reflections = new Reflections("be.ceau.podcastparser");
		for (Class<? extends Namespace> clazz : reflections.getSubTypesOf(Namespace.class)) {
			Assert.assertNotNull(clazz.getCanonicalName(), NamespaceFactory.getByCanonicalName(clazz.getCanonicalName()));
		}
	}

}
