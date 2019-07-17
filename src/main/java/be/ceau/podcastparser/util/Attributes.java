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
package be.ceau.podcastparser.util;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * Utility class for extracting and cleaning attribute values.
 */
public class Attributes {

	private static final Map<String, Attributes> CACHE = new ConcurrentHashMap<>();
	
	public static Attributes get(String localName) {
		return CACHE.computeIfAbsent(localName, x -> new Attributes(localName));
	}

	private final String localName;

	private Attributes(String localName) {
		this.localName = localName;
	}
	
	public Optional<String> from(XMLStreamReader reader) {
		String value = reader.getAttributeValue(null, localName);
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty()) {
				return Optional.of(value);
			}
		}
		return Optional.empty();
	}

	public static String toString(XMLStreamReader reader) {
		if (reader.getEventType() == XMLStreamConstants.ATTRIBUTE || reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < reader.getAttributeCount(); i++) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(reader.getAttributeLocalName(i)).append("=").append(reader.getAttributeValue(i));
			}
			return sb.toString();
		}
		return "";
	}

}
