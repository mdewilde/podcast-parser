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
package be.ceau.podcastparser;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bench {

	private static final Logger logger = LoggerFactory.getLogger(Bench.class);

	private static final String minutes = "m";
	private static final String seconds = "s";
	private static final String milliseconds = "ms";
	private static final String microseconds = "µs";
	private static final String nanoseconds = "ns";
	private static final String pattern = "%3d";
	private static final String space = " ";

	private UUID uuid = UUID.randomUUID();
	private long start = System.nanoTime();
	private long stop;

	public Bench reset() {
		this.start = System.nanoTime();
		return this;
	}
	
	public Bench stop() {
		this.stop = System.nanoTime();
		return this;
	}

	public Bench log() {
		logger.info(timingString(stop - start));
		return this;
	}

	public Bench log(String message) {
		logger.info("[{}] {} ---> {}", uuid.toString(), timingString(stop - start), message);
		return this;
	}

	public static String timingString(long nanos) {
		long totalseconds = nanos / 1000000000;
		return new StringBuilder()
			.append(String.format(pattern, totalseconds / 60))
			.append(space)
			.append(minutes)
			.append(space)
			.append(String.format(pattern, totalseconds % 60))
			.append(space)
			.append(seconds)
			.append(space)
			.append(String.format(pattern, (nanos % 1000000000) / 1000000))
			.append(space)
			.append(milliseconds)
			.append(space)
			.append(String.format(pattern, (nanos % 1000000) / 1000))
			.append(space)
			.append(microseconds)
			.append(space)
			.append(String.format(pattern, (nanos % 1000)))
			.append(space)
			.append(nanoseconds)
			.toString();
	}

	@Override
	public String toString() {
		return new StringBuilder("[")
				.append(uuid.toString())
				.append("]")
				.append(" ")
				.append(timingString(stop - start))
				.toString();
	}
	
}
