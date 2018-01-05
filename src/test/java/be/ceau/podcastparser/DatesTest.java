package be.ceau.podcastparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.util.Dates;

public class DatesTest {

	private static final Logger logger = LoggerFactory.getLogger(DatesTest.class);

	@Test
	public void test() throws IOException {

		Files.readAllLines(Paths.get(System.getProperty("user.home"), "dates.txt"))
				.forEach(line -> {
					if (Strings.isNotBlank(line)) {
						try {
							ZonedDateTime temporal = Dates.parse(line);
							if (temporal == null) {
								logger.info("{}", line);
							}
						} catch (Exception e) {
							logger.error("{}", line, e);
						}
					}
				});

	}

	// @Test
	public void teste() throws IOException {
		final String line = "May, 12 2016 09:16:04 -0600";
		try {
			ZonedDateTime temporal = Dates.parse(line);
			if (temporal == null) {
				logger.info("{}", line);
			} else {
				logger.info("{} -> {}", line, temporal);
			}
		} catch (Exception e) {
			logger.error("{}", line, e);
		}

		
	}

}
