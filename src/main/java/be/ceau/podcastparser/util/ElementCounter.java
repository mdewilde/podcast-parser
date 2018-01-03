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
package be.ceau.podcastparser.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import be.ceau.podcastparser.ParseLevel;

/**
 * Utility class to count elements
 */
public class ElementCounter {

	private static final int COUNT_WIDTH = 8;
	private static final int LEVEL_WIDTH = 8;
	private static final int NAME_WIDTH = 50;
	private static final int ATTRIBUTES_WIDTH = 42;
	private static final int NAMESPACE_WIDTH = 80;

	private final Map<EncounteredElement, LongAdder> map = new ConcurrentHashMap<>();

	public void count(EncounteredElement encounteredElement) {
		map.computeIfAbsent(encounteredElement, x -> new LongAdder()).increment();
	}
	
	@Override
	public String toString() {
		String rows = map.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().sum()))
				.entrySet()
				.stream()
				.sorted(Map.Entry.<EncounteredElement, Long>comparingByValue().reversed())
				.map(e -> row(e.getValue(), e.getKey().getLevel(), e.getKey().getLocalName(), e.getKey().getAttributesString(), e.getKey().getNamespaceUri()))
				.collect(Collectors.joining(System.lineSeparator()));
		return header() + rows;
	}
	
	private static String header() {
		String line = new StringBuilder()
				.append(System.lineSeparator())
				.append(StringUtils.repeat("-", COUNT_WIDTH))
				.append(StringUtils.repeat("-", LEVEL_WIDTH))
				.append(StringUtils.repeat("-", NAME_WIDTH))
				.append(StringUtils.repeat("-", ATTRIBUTES_WIDTH))
				.append(StringUtils.repeat("-", NAMESPACE_WIDTH))
				.append(System.lineSeparator())
				.toString();
		return new StringBuilder()
				.append(line)
				.append(row("COUNT", "LEVEL", "LOCALNAME", "ATTRIBUTES", "NAMESPACE"))
				.append(line)
				.toString();
	}
	
	private static String row(long count, ParseLevel level, String localName, String attributes, String namespace) {
		return row(String.valueOf(count), level.name(), localName, attributes, namespace);
	}

	private static String row(String count, String level, String localName, String attributes, String namespace) {
		return new StringBuilder()
				.append(StringUtils.leftPad(count, COUNT_WIDTH))
				.append(" | ")
				.append(StringUtils.rightPad(level, LEVEL_WIDTH))
				.append(" | ")
				.append(StringUtils.rightPad(localName, NAME_WIDTH))
				.append(" | ")
				.append(StringUtils.rightPad(attributes, ATTRIBUTES_WIDTH))
				.append(" | ")
				.append(StringUtils.rightPad(namespace, NAMESPACE_WIDTH))
				.toString();
	}
	
}
