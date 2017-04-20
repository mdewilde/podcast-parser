package be.ceau.podcastparser.util;

import java.time.Duration;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Factory class for creation {@link Duration} instances.
 */
public class Durations {

	private Durations() {
		// static methods only
	}

	public static Duration parse(String string) {
		if (string == null) {
			return null;
		}
		string = string.trim().toLowerCase(Locale.ENGLISH);
		String[] split = string.split(":");
		try {
			switch (split.length) {
			case 0:
				return null;
			case 1: {
				int millis = toMillis(split[0]);
				return Duration.ofMillis(millis);
			}
			case 2: {
				int minutes = Integer.parseInt(split[0]);
				int millis = toMillis(split[1]);
				return Duration.ofMillis(minutes * 60 * 1000 + millis);
			}
			case 3:
			default: {
				int hours = Integer.parseInt(split[0]);
				int minutes = Integer.parseInt(split[1]);
				int millis = toMillis(split[2]);
				return Duration.ofMillis(hours * 60 * 60 * 1000 + minutes * 60 * 1000 + millis);
			}
			}
		} catch (NumberFormatException e) {
			return extractMinutes(string);
		//	logger.warn("of(String {}) {}", duration, e.getMessage());
		// 	return null;
		}
	}

	private static final Pattern MIN_PATTERN = Pattern.compile("min");
	
	/**
	 * Fallback parsing for input like "26 minutes" or "44 mins"
	 * @param string
	 * @return
	 */
	private static Duration extractMinutes(String string) {
		String[] split = MIN_PATTERN.split(string);
		if (split.length > 0 && split[0].length() < string.length()) {
			try {
				return Duration.ofMinutes(Integer.parseInt(split[0].trim()));
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}
	
	private static int toMillis(String millis) {
		if (millis != null) {
			String[] split = millis.trim().split("\\.");
			switch (split.length) {
			case 0:
				return 0;
			case 1:
				return Integer.parseInt(split[0]) * 1000;
			case 2:
				String fraction = split[1];
				int milli = 0;
				switch (fraction.length()) {
				case 0 : 
					break;
				case 1 : 
					milli = Integer.parseInt(fraction) * 100;
					break;
				case 2 : 
					milli = Integer.parseInt(fraction) * 10;
					break;
				case 3 : 
					milli = Integer.parseInt(fraction);
					break;
				default : 
					milli = Integer.parseInt(fraction.substring(0, 3));
					break;
				}
				return Integer.parseInt(split[0]) * 1000 + milli;
			}
		}
		return 0;
	}

}
