/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

/**
 * <h1>Resource Description Framework (RDF)</h1>
 * 
 * @see https://www.w3.org/TR/1999/REC-rdf-syntax-19990222/
 */
public class ResourceDescriptionFramework implements Namespace {

	private static final String NAME = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	@Override
	public String getName() {
		return NAME;
	}

}

/*

	corpus stats
	
         9 	--> http://www.w3.org/1999/02/22-rdf-syntax-ns# level=feed localName=Description attributes=[]]
         9 	--> http://www.w3.org/1999/02/22-rdf-syntax-ns# level=feed localName=value attributes=[]]
         7 	--> http://www.w3.org/1999/02/22-rdf-syntax-ns# level=item localName=RDF attributes=[]]

*/