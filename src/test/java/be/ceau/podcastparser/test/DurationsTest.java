package be.ceau.podcastparser.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.ceau.podcastparser.util.Durations;

public class DurationsTest {

	private static final Logger logger = LoggerFactory.getLogger(DurationsTest.class);

	@Test
	public void test() {
		
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("durations.txt")) {
			try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
				try (BufferedReader b = new BufferedReader(reader)) {
					String line;
					while ((line = b.readLine()) != null) {
						logger.info("{} --> {} milliseconds", line, Durations.parse(line));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
