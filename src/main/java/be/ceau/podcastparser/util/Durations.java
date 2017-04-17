package be.ceau.podcastparser.util;

import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creation {@link Duration} instances.
 */
public class Durations {

	private static final Logger logger = LoggerFactory.getLogger(Durations.class);

	private Durations() {
		// static methods only
	}

	public static Optional<Duration> of(String duration) {
		if (duration == null) {
			return Optional.empty();
		}
		String[] split = duration.trim().split(":");
		try {
			switch (split.length) {
			case 0:
				return Optional.empty();
			case 1: {
				int millis = toMillis(split[0]);
				return Optional.of(Duration.ofMillis(millis));
			}
			case 2: {
				int minutes = Integer.parseInt(split[0]);
				int millis = toMillis(split[1]);
				return Optional.of(Duration.ofMillis(minutes * 60 * 1000 + millis));
			}
			case 3:
			default: {
				int hours = Integer.parseInt(split[0]);
				int minutes = Integer.parseInt(split[1]);
				int millis = toMillis(split[2]);
				return Optional.of(Duration.ofMillis(hours * 60 * 60 * 1000 + minutes * 60 * 1000 + millis));
			}
			}
		} catch (NumberFormatException e) {
			logger.warn("of(String {}) {}", duration, e.getMessage());
			return Optional.empty();
		}
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
				case 2 : 
					milli = Integer.parseInt(fraction) * 10;
				case 3 : 
					milli = Integer.parseInt(fraction);
				default : 
					milli = Integer.parseInt(fraction.substring(0, 3));
				}
				return Integer.parseInt(split[0]) * 1000 + milli;
			}
		}
		return 0;
	}

}
