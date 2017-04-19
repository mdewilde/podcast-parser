package be.ceau.podcastparser.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.temporal.Temporal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.util.Dates;

public class DatesTest {

	private static final Logger logger = LoggerFactory.getLogger(DatesTest.class);

	@Test
	public void test() {
		Set<String> set = new HashSet<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("dates.txt")) {
			try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
				try (BufferedReader b = new BufferedReader(reader)) {
					String line;
					int count = 0;
					while ((line = b.readLine()) != null) {
						if (StringUtils.isNotBlank(line)) {
						try {
							Temporal temporal = Dates.parse(line);
							if (temporal == null)
								logger.info("{}", line);
//							logger.info("{}", line);
						} catch (StackOverflowError e) {
							logger.error("{}", line, e);
						}
						}
//						count++;
//						if (count > 10000) {
//							break;
//						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(set.stream().collect(Collectors.joining(System.lineSeparator())));
	}

}
