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

/**
 * Threadsafe for updates.
 */
public class Counter {

	public final Map<String, LongAdder> map = new ConcurrentHashMap<>();

	public void count(String string) {
		map.computeIfAbsent(string, x -> new LongAdder()).increment();
	}
	
	public void addCount(String string, long count) {
		map.computeIfAbsent(string, x -> new LongAdder()).add(count);
	}

	public boolean isNotEmpty() {
		return !map.isEmpty();
	}

	@Override
	public String toString() {
		return map.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey(), e-> e.getValue().sum()))
				.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.map(e -> String.format("%10d", e.getValue()) + " \t--> " + e.getKey())
				.collect(Collectors.joining(System.lineSeparator()));
	}

	public void add(Counter counter) {
		counter.map.entrySet()
			.forEach(e -> addCount(e.getKey(), e.getValue().sum()));
	}

}
