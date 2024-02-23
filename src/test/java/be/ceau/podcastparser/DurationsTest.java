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
package be.ceau.podcastparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.util.Durations;

public class DurationsTest {

	private static final Logger logger = LoggerFactory.getLogger(DurationsTest.class);

	@Test
	public void test() {
		
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("durations.txt")) {
			try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
				try (BufferedReader b = new BufferedReader(reader)) {
					String line;
					while ((line = b.readLine()) != null) {
						logger.info("{} --> {} milliseconds", line, Durations.parse(line));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
