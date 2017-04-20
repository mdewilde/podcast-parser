/*
http://www.rte.ie/applications/ipad/schemas	Copyright 2017 Marceau Dewilde <m@ceau.be>
	
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

/**
 * 
 * @see http://www.opensearch.org/Specifications/OpenSearch/1.1
 *
 */
public class OpenSearch implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://a9.com/-/spec/opensearchrss/1.0/",
			"http://a9.com/-/spec/opensearch/1.1/");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}

/*

	corpus stats
	
      6567 	--> http://a9.com/-/spec/opensearchrss/1.0/ level=feed localName=totalResults attributes=[]]
      6567 	--> http://a9.com/-/spec/opensearchrss/1.0/ level=feed localName=startIndex attributes=[]]
      6563 	--> http://a9.com/-/spec/opensearchrss/1.0/ level=feed localName=itemsPerPage attributes=[]]
       104 	--> http://a9.com/-/spec/opensearch/1.1/ level=feed localName=startIndex attributes=[]]
       100 	--> http://a9.com/-/spec/opensearch/1.1/ level=feed localName=totalResults attributes=[]]
        97 	--> http://a9.com/-/spec/opensearch/1.1/ level=feed localName=itemsPerPage attributes=[]]

*/