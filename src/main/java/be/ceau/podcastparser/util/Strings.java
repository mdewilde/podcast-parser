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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility methods for working with {@link String} objects
 */
public class Strings {

	private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");
	private static final Pattern COMMA = Pattern.compile(",");

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

	public static boolean isNotBlank(final String string) {
		return !isBlank(string);
	}

	public static void requireNonBlank(final String string) {
		if (isBlank(string)) {
			throw new IllegalArgumentException();
		}
	}

	public static List<String> splitOnWhitespace(final String string) {
		if (Strings.isBlank(string)) {
			return Collections.emptyList();
		}
		return WHITE_SPACE.splitAsStream(string.trim())
				.map(String::trim)
				.filter(Strings::isNotBlank)
				.collect(Collectors.toList());
	}

	public static List<String> splitOnComma(final String string) {
		if (Strings.isBlank(string)) {
			return Collections.emptyList();
		}
		return COMMA.splitAsStream(string.trim())
				.map(String::trim)
				.filter(Strings::isNotBlank)
				.collect(Collectors.toList());
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

	/**
	 * Check if string starts with prefix, case insensitive
	 *
	 * @param string
	 *            {@link String} to test
	 * @param prefix
	 *            {@link String} to check, not {@code null}
	 * @return {@code true} if {@code string} starts with {@code prefix}
	 * @throws NullPointerException
	 *             if {@code prefix} is {@code null}
	 */
	public static boolean startsWithIgnoreCase(String string, String prefix) {
		if (string == null) {
			return false;
		}
		if (string.length() < prefix.length()) {
			return false;
		}
		String substring = string.substring(0, prefix.length());
		return prefix.equalsIgnoreCase(substring);
	}

	/**
	 * Create a {@link String} consisting of {@code count} repetitions of {@code character}
	 *
	 * @param character
	 *            {@code char} to repeat
	 * @param count
	 *            {@code int}
	 * @return {@code String}, not {@code null}
	 */
	public static String repeat(char character, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(character);
		}
		return sb.toString();
	}

	public static String rightPad(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String leftPad(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}

}
