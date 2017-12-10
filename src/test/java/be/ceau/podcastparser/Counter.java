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
package be.ceau.podcastparser;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Counter {

	public final Map<String, Integer> map = new HashMap<>();

	public void count(String string) {
		map.compute(string, (k, v) -> (v == null ? 1 : v + 1));
	}
	
	@Override
	public String toString() {
		return map.entrySet()
					.stream()
				    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				    .map(e -> String.format("%10d", e.getValue()) + " \t--> " + e.getKey())
					.collect(Collectors.joining(System.lineSeparator()));
	}

	
}
