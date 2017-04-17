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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import be.ceau.podcastparser.stax.models.Item;
import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * Metadata terms maintained by the Dublin Core Metadata Initiative:
 * 
 * <pre>
 * {@code abstract ,  accessRights , accrualMethod , accrualPeriodicity , 
 * accrualPolicy , alternative , audience , available , bibliographicCitation , 
 * conformsTo , contributor , coverage , created , creator , date , dateAccepted ,
 * dateCopyrighted , dateSubmitted , description , educationLevel , extent ,
 * format , hasFormat , hasPart , hasVersion , identifier , instructionalMethod
 * , isFormatOf , isPartOf , isReferencedBy , isReplacedBy , isRequiredBy ,
 * issued , isVersionOf , language , license , mediator , medium , modified ,
 * provenance , publisher , references , relation , replaces , requires , rights
 * , rightsHolder , source , spatial , subject , tableOfContents , temporal ,
 * title , type , valid
 * }
 * </pre>
 * 
 * @see http://dublincore.org/documents/2012/06/14/dcmi-terms/
 */
public class DublinCoreTerms implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://purl.org/dc/terms/");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

	@Override
	public void process(Item item, XMLStreamReader reader) throws XMLStreamException {
		switch (reader.getLocalName()) {
		case "modified": {
			// Date on which the resource was changed.
			item.setUpdated(reader.getElementText());
			break;
		}
		case "created": {
			// Date of creation of the resource.
			item.setPubDate(reader.getElementText());
			break;
		}
		case "valid": {
			// Date (often a range) of validity of a resource.
			item.setValidity(reader.getElementText());
			break;
		}
		case "subject": {
			// The topic of the resource.
			item.setDescription(reader.getElementText());
		}
		}
		Namespace.super.process(item, reader);
	}

}
