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
package be.ceau.podcastparser.stax.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

public class ItunesU implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://www.itunesu.com/feed");
	
	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}

/*

	corpus stats

     12553 	--> http://www.itunesu.com/feed level=item localName=category attributes=[code]]
        90 	--> http://www.itunesu.com/feed level=item localName=category attributes=[]]
        14 	--> http://www.itunesu.com/feed level=feed localName=category attributes=[code]]
         7 	--> http://www.itunesu.com/feed level=feed localName=category attributes=[code, text]]
         6 	--> http://www.itunesu.com/feed level=item localName=category attributes=[text]]

*/