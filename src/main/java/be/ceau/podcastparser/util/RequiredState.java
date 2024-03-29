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
package be.ceau.podcastparser.util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class RequiredState {

	private final int type;
	private final String namespaceURI;
	private final String localName;
	
	public RequiredState(int type, String namespaceURI, String localName) {
		this.type = type;
		this.namespaceURI = namespaceURI;
		this.localName = localName;
	}
	
	public static RequiredState from(XMLStreamReader reader) {
		return new RequiredState(reader.getEventType(), reader.getNamespaceURI(), reader.getLocalName());
	}
	
	public boolean test(XMLStreamReader reader) throws XMLStreamException {
		return type == reader.getEventType()
				&& namespaceURI.equals(reader.getNamespaceURI())
				&& localName.equals(reader.getLocalName());
	}

	public void validate(XMLStreamReader reader) throws XMLStreamException {
		reader.require(type, namespaceURI, localName);
	}

}
