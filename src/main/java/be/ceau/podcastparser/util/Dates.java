package be.ceau.podcastparser.util;

import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Dates {

	private Dates() {
		// static methods only
	}
	
	private static final Pattern TIMESTAMP_SECONDS = Pattern.compile("[0-9]{10}");
	private static final Pattern TIMESTAMP_MILLISECONDS = Pattern.compile("[0-9]{13}");

	private static final Pattern COMMA = Pattern.compile(",");

	private static final Pattern ORDINAL_ND = Pattern.compile("nd ");
	private static final Pattern ORDINAL_TH = Pattern.compile("th ");
	private static final Pattern ORDINAL_RD = Pattern.compile("rd ");

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
			"uuuu-MM-dd'T'HH:mm:ssxx");
	
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
	
	private static final Pattern PATTERN = Pattern.compile("\\s+");

	public static Temporal parse(String string) {
		if (string == null) {
			return null;
		}
		string = cleanup(string);
		Temporal t = parse(string, true);
		if (t == null) {
			// last try
			string = deepClean(string);
			t = parse(string, true);
		}
		return t;
	}
	
	private static Temporal parse(String string, boolean recurse) {
		if (string == null) {
			return null;
		}
		string = cleanup(string);
		ParsePosition position = new ParsePosition(0);

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
					return LocalDateTime.parse(string, formatter);
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
					return LocalDate.parse(string, formatter);
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
			return Instant.ofEpochMilli(Long.parseLong(string));
		}
		if (TIMESTAMP_SECONDS.matcher(string).matches()) {
			return Instant.ofEpochSecond(Long.parseLong(string));
		}

		return null;
	}

	private static String deepClean(String string) {
		string = chompFirstPart(string);
		string = removeComma(string);
		string = removeOrdinal(string);
		string = removeExtraWhitespace(string);
		return string;
	}

	private static String removeComma(String string) {
		return COMMA.matcher(string).replaceAll(" ");
	}

	private static String removeOrdinal(String string) {
		string = ORDINAL_ND.matcher(string).replaceAll(" ");
		string = ORDINAL_RD.matcher(string).replaceAll(" ");
		string = ORDINAL_TH.matcher(string).replaceAll(" ");
		return string;
	}
	
	private static boolean isDayOfWeekMismatch(DateTimeException e) {
		return e.getMessage().contains("DayOfWeek") && e.getMessage().contains("differs");
	}
	
	private static String removeDayOfWeek(String string) {
		string = StringUtils.removeIgnoreCase(string, "Mon,");
		string = StringUtils.removeIgnoreCase(string, "Tue,");
		string = StringUtils.removeIgnoreCase(string, "Wed,");
		string = StringUtils.removeIgnoreCase(string, "Thu,");
		string = StringUtils.removeIgnoreCase(string, "Fri,");
		string = StringUtils.removeIgnoreCase(string, "Sat,");
		string = StringUtils.removeIgnoreCase(string, "Sun,");
		return string;
	}
	
	private static String chompFirstPart(String string) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]*(,| )");
		return pattern.matcher(string).replaceFirst("");
	}
	
	private static String cleanup(String string) {
		string = string.trim();
		string = removeExtraWhitespace(string);
		return string;
	}

	private static String removeExtraWhitespace(String string) {
		return PATTERN.matcher(string).replaceAll(" ");
	}

}
