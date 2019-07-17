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

import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Dates {

	private Dates() {
		// static methods only
	}

	private static final Pattern TIMESTAMP_SECONDS = Pattern.compile("[0-9]{10}");
	private static final Pattern TIMESTAMP_MILLISECONDS = Pattern.compile("[0-9]{13}");

	private static final Pattern COMMA = Pattern.compile(",");

	private static final Pattern ORDINAL_ND = Pattern.compile("([0-9])nd ");
	private static final Pattern ORDINAL_RD = Pattern.compile("([0-9])rd ");
	private static final Pattern ORDINAL_ST = Pattern.compile("([0-9])st ");
	private static final Pattern ORDINAL_TH = Pattern.compile("([0-9])th ");

	private static final Pattern WHITE_SPACE = Pattern.compile("\\s+");

	private static final Pattern MON = Pattern.compile("Mon,", Pattern.CASE_INSENSITIVE);
	private static final Pattern TUE = Pattern.compile("Tue,", Pattern.CASE_INSENSITIVE);
	private static final Pattern WED = Pattern.compile("Wed,", Pattern.CASE_INSENSITIVE);
	private static final Pattern THU = Pattern.compile("Thu,", Pattern.CASE_INSENSITIVE);
	private static final Pattern FRI = Pattern.compile("Fri,", Pattern.CASE_INSENSITIVE);
	private static final Pattern SAT = Pattern.compile("Sat,", Pattern.CASE_INSENSITIVE);
	private static final Pattern SUN = Pattern.compile("Sun,", Pattern.CASE_INSENSITIVE);
	
	private static final Set<String> ZONED_DATE_TIME_PATTERNS = UnmodifiableSet.of(
			"EEE, dd MMM uuuu H:mm:ss xx",
			"EEE, dd MMM uuuu H:mm:ss z",
			"EEEE, dd MMMM uuuu H:mm:ss xx",
			"EEEE, dd MMMM uuuu H:mm:ss z",
			"EEEE, dd MMM uuuu H:mm:ss xx",
			"EEEE, dd MMM uuuu H:mm:ss z",
			"EEE, dd MMMM uuuu H:mm:ss xx",
			"EEE, dd MMMM uuuu H:mm:ss z",
			"EEE, dd MMM uuuu H:mm xx",
			"EEE, dd MMM uuuu H:mm z",
			"dd MMM uuuu HH:mm:ss xx",
			"dd MMM uuuu HH:mm:ss z",
			"uuuu-MM-dd'T'HH:mm:ssxx",
			"MMMM dd uuuu HH:mm:ss Z",
			"MMMM dd uuuu hh:mm a z",
			"MMMM, dd uuuu HH:mm:ss Z"
			);

	private static final Set<String> LOCAL_DATE_TIME_PATTERNS = UnmodifiableSet.of(
			"uuuu-MM-dd HH:mm:ss",
			"EEE, dd MMM uuuu HH:mm:ss",
			"uuuu/MM/dd HH:mm:ss",
			"dd MMMM uuuu HH:mm:ss");

	private static final Set<String> LOCAL_DATE_PATTERNS = UnmodifiableSet.of(
			"uuuu-MM-dd",
			"MMM dd, uuuu",
			"MMM dd uuuu",
			"dd MMM uuuu",
			"dd/MM/uuuu",
			"uuuu/MM/dd");

	private static final Set<DateTimeFormatter> ZONED_DATE_TIME_FORMATTERS;
	private static final Set<DateTimeFormatter> LOCAL_DATE_TIME_FORMATTERS;
	private static final Set<DateTimeFormatter> LOCAL_DATE_FORMATTERS;

	private static DateTimeFormatter prepare(String pattern) {
		return new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.parseLenient()
				.appendPattern(pattern)
				.toFormatter(Locale.ENGLISH);
	}

	static {
		List<DateTimeFormatter> formatters1 = ZONED_DATE_TIME_PATTERNS.stream()
				.map(Dates::prepare)
				.collect(Collectors.toList());
		ZONED_DATE_TIME_FORMATTERS = UnmodifiableSet.of(formatters1);
		List<DateTimeFormatter> formatters2 = LOCAL_DATE_TIME_PATTERNS.stream()
				.map(Dates::prepare)
				.collect(Collectors.toList());
		LOCAL_DATE_TIME_FORMATTERS = UnmodifiableSet.of(formatters2);
		List<DateTimeFormatter> formatters3 = LOCAL_DATE_PATTERNS.stream()
				.map(Dates::prepare)
				.collect(Collectors.toList());
		LOCAL_DATE_FORMATTERS = UnmodifiableSet.of(formatters3);
	}

	/**
	 * <p>
	 * Parse the given input {@link String} to a {@link ZonedDateTime} instance.
	 * </p>
	 * <p>
	 * The aim of this method is to be as lenient as possible, so very few assumptions are made about
	 * the input format. However, if no possible date can be parsed from the input, {@code null} is
	 * returned.
	 * </p>
	 * <p>
	 * If time is missing from the input, start of day will be set instead.
	 * </p>
	 * <p>
	 * If timezone is missing from the input, UTC will be substituted.
	 * </p>
	 * 
	 * @param string
	 *            a {@link String}
	 * @return parsed {@link ZonedDateTime}, or {@code null}
	 */
	public static ZonedDateTime parse(final String string) {
		if (Strings.isBlank(string)) {
			return null;
		}
		final String cleaned = cleanup(string);
		ZonedDateTime zonedDateTime = parse(cleaned, true);
		if (zonedDateTime == null) {
			// last try
			final String deepCleaned = deepClean(cleaned);
			zonedDateTime = parse(deepCleaned, true);
		}
		return zonedDateTime;
	}

	private static ZonedDateTime parse(String string, boolean recurse) {

		final ParsePosition position = new ParsePosition(0);

		for (DateTimeFormatter formatter : ZONED_DATE_TIME_FORMATTERS) {
			try {
				formatter.parseUnresolved(string, position);
				if (position.getErrorIndex() < 0) {
					return ZonedDateTime.parse(string, formatter);
				}
			} catch (DateTimeParseException ex) {
				if (recurse && isDayOfWeekMismatch(ex)) {
					string = removeDayOfWeek(string);
					return parse(string, false);
				} else if (recurse && ex.getErrorIndex() > 0) {
					string = string.substring(0, ex.getErrorIndex());
					return parse(string, false);
				}
			}
			position.setIndex(0);
			position.setErrorIndex(-1);
		}
		for (DateTimeFormatter formatter : LOCAL_DATE_TIME_FORMATTERS) {
			try {
				formatter.parseUnresolved(string, position);
				if (position.getErrorIndex() < 0) {
					return LocalDateTime.parse(string, formatter).atZone(ZoneId.of("UTC"));
				}
			} catch (DateTimeParseException ex) {
				if (recurse && isDayOfWeekMismatch(ex)) {
					string = removeDayOfWeek(string);
					return parse(string, false);
				} else if (recurse && ex.getErrorIndex() > 0) {
					string = string.substring(0, ex.getErrorIndex());
					return parse(string, false);
				}
			}
			position.setIndex(0);
			position.setErrorIndex(-1);
		}
		for (DateTimeFormatter formatter : LOCAL_DATE_FORMATTERS) {
			try {
				formatter.parseUnresolved(string, position);
				if (position.getErrorIndex() < 0) {
					return LocalDate.parse(string, formatter).atStartOfDay().atZone(ZoneId.of("UTC"));
				}
			} catch (DateTimeParseException ex) {
				if (recurse && isDayOfWeekMismatch(ex)) {
					string = removeDayOfWeek(string);
					return parse(string, false);
				} else if (recurse && ex.getErrorIndex() > 0) {
					string = string.substring(0, ex.getErrorIndex());
					return parse(string, false);
				}
			}
			position.setIndex(0);
			position.setErrorIndex(-1);
		}
		if (TIMESTAMP_MILLISECONDS.matcher(string).matches()) {
			return Instant.ofEpochMilli(Long.parseLong(string)).atZone(ZoneId.of("UTC"));
		}
		if (TIMESTAMP_SECONDS.matcher(string).matches()) {
			return Instant.ofEpochSecond(Long.parseLong(string)).atZone(ZoneId.of("UTC"));
		}

		return null;
	}

	private static String deepClean(String string) {
		string = chompFirstPart(string);
		string = removeComma(string);
		string = removeOrdinal(string);
		string = removeExtraWhitespace(string);
		return string.trim();
	}

	private static String removeComma(String string) {
		return COMMA.matcher(string).replaceAll(" ");
	}

	private static String removeOrdinal(String string) {
		string = ORDINAL_ND.matcher(string).replaceAll("$1 ");
		string = ORDINAL_RD.matcher(string).replaceAll(" ");
		string = ORDINAL_ST.matcher(string).replaceAll("$1 ");
		string = ORDINAL_TH.matcher(string).replaceAll(" ");
		return string;
	}

	private static boolean isDayOfWeekMismatch(DateTimeException e) {
		return e.getMessage().contains("DayOfWeek") && e.getMessage().contains("differs");
	}

	private static String removeDayOfWeek(String string) {
		string = MON.matcher(string).replaceAll("");
		string = TUE.matcher(string).replaceAll("");
		string = WED.matcher(string).replaceAll("");
		string = THU.matcher(string).replaceAll("");
		string = FRI.matcher(string).replaceAll("");
		string = SAT.matcher(string).replaceAll("");
		string = SUN.matcher(string).replaceAll("");
		return string;
	}

	private static String chompFirstPart(String string) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]*(,| )");
		return pattern.matcher(string).replaceFirst("");
	}

	private static String cleanup(final String string) {
		return removeExtraWhitespace(string).trim();
	}

	private static String removeExtraWhitespace(String string) {
		return WHITE_SPACE.matcher(string).replaceAll(" ");
	}

}
