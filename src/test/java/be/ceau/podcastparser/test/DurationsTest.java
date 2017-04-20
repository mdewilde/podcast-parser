package be.ceau.podcastparser.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.util.Durations;

public class DurationsTest {

	private static final Logger logger = LoggerFactory.getLogger(DurationsTest.class);

	@Test
	public void test() {
		
		Set<String> set = new HashSet<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("durations.txt")) {
			try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
				try (BufferedReader b = new BufferedReader(reader)) {
					String line;
					while ((line = b.readLine()) != null) {
						if (StringUtils.isNotBlank(line)) {
						try {
							Duration d = Durations.parse(line);
							if (d == null)
								logger.info("{}", line);
						} catch (StackOverflowError e) {
							logger.error("{}", line, e);
						}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(set.stream().collect(Collectors.joining(System.lineSeparator())));
	}

}
