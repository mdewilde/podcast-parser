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
package be.ceau.podcastparser.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * <h1>creativeCommons RSS Module</h1>
 * <p>
 * An RSS module that adds an element at the {@code <channel>} or {@code <item>}
 * level that specifies which Creative Commons license applies.
 * </p>
 */
public class UserlandCreativeCommons implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://backend.userland.com/creativecommonsrssmodule");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}

/*
	corpus stats
	
      4775 	--> http://backend.userland.com/creativeCommonsRssModule level=item localName=license attributes=[]]
      2096 	--> http://backend.userland.com/creativeCommonsRssModule level=feed localName=license attributes=[]]
         2 	--> http://backend.userland.com/blogChannelModule level=feed localName=blink attributes=[]]
         2 	--> http://backend.userland.com/blogChannelModule level=feed localName=blogRoll attributes=[]]

*/