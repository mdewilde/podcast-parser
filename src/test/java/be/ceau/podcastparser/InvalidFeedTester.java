package be.ceau.podcastparser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class InvalidFeedTester {

	private static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.home"), "LocalLinkSaver");

	private static final PodcastParser PODCAST_PARSER = new PodcastParser();
	
	@Test
	public void test() throws IOException {
		Files.list(BASE_DIRECTORY)
			.sorted()
			.forEach(path -> {
				try {
					String xml = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
					PODCAST_PARSER.parse(xml);
				} catch (Exception e) {
					System.out.println(path);
					e.printStackTrace(System.out);
					System.out.println("--------------------------------------------------------------------------------------------");
				}
				
			});
	}
	
}
