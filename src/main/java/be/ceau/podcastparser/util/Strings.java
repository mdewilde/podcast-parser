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
package be.ceau.podcastparser.util;

import java.util.regex.Pattern;

/**
 * Utility methods for working with {@link String} objects
 */
public class Strings {

	private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
	
	public static boolean isNotBlank(final String string) {
		return !isBlank(string);
	}

	public static boolean isBlank(final String string) {
		int strLen;
		if (string == null || (strLen = string.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(string.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static void requireNonBlank(final String string) {
		if (isBlank(string)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Reduces all whitespace in the given {@link String} to a single space
	 * 
	 * @param string
	 *            a {@link String}, can be {@code null}
	 * @return a {@link String} with all whitespace reduced, or {@code null} if input {@code null}
	 */
	public static String reduceWhitespace(final String string) {
		if (string == null) {
			return null;
		}
		return WHITE_SPACE.matcher(string).replaceAll(" ");
	}

}
