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

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Factory class for parsing duration patterns into {@code long} millisecond values.
 */
public class Durations {

	private Durations() {
		// static methods only
	}

	/**
	 * Parse the given {@link String} into a millisecond timestamp.
	 * 
	 * @param string
	 *            a {@link String}, or {@code null}
	 * @return millisecond timestamp {@link Long}, or {@code null} if parse problem
	 */
	public static Long parse(String string) {
		if (Strings.isBlank(string)) {
			return null;
		}
		string = string.trim().toLowerCase(Locale.ENGLISH);
		String[] split = string.split(":");
		try {
			switch (split.length) {
			case 0:
				return null;
			case 1: {
				return toMillis(split[0]);
			}
			case 2: {
				long minutes = Long.parseLong(split[0]);
				long millis = toMillis(split[1]);
				return minutes * 60 * 1000 + millis;
			}
			case 3:
			default: {
				long hours = Long.parseLong(split[0]);
				long minutes = Long.parseLong(split[1]);
				long millis = toMillis(split[2]);
				return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + millis;
			}
			}
		} catch (NumberFormatException e) {
			return extractMinutes(string);
			// logger.warn("of(String {}) {}", duration, e.getMessage());
			// return null;
		}
	}
	
	private static final Pattern MIN_PATTERN = Pattern.compile("min");

	/**
	 * Fallback parsing for input like "26 minutes" or "44 mins"
	 */
	private static Long extractMinutes(String string) {
		String[] split = MIN_PATTERN.split(string);
		if (split.length > 0 && split[0].length() < string.length()) {
			try {
				return Long.parseLong(split[0].trim());
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}

	private static long toMillis(String millis) {
		if (millis != null) {
			String[] split = millis.trim().split("\\.");
			switch (split.length) {
			case 0:
				return 0l;
			case 1:
				return Long.parseLong(split[0]) * 1000;
			case 2:
				String fraction = split[1];
				long milli = 0;
				switch (fraction.length()) {
				case 0:
					break;
				case 1:
					milli = Long.parseLong(fraction) * 100;
					break;
				case 2:
					milli = Long.parseLong(fraction) * 10;
					break;
				case 3:
					milli = Long.parseLong(fraction);
					break;
				default:
					milli = Long.parseLong(fraction.substring(0, 3));
					break;
				}
				return Long.parseLong(split[0]) * 1000 + milli;
			}
		}
		return 0;
	}

}
