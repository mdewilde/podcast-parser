/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.test.provider;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import be.ceau.podcastparser.WrappedXml;

public class FilesProvider implements TestXmlProvider {

	private static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.home"), "podcastfinder", "xml");
	private static final Path CORPUS_2017_04_15 = Paths.get(System.getProperty("user.home"), "podcastxml", "corpus");

	private static final List<File> FILES;
	
	static {
		File directory = CORPUS_2017_04_15.toFile();
		if (!directory.isDirectory()) {
			throw new IllegalStateException(
					"base directory containing XML samples not found at " + BASE_DIRECTORY.toString());
		}
		FILES = Collections.unmodifiableList(Arrays.asList(directory.listFiles()));
	}

	private final Stream<WrappedXml> xmlStream;

	public FilesProvider() {
		List<File> files = new ArrayList<>(FILES);
		this.xmlStream = files.stream().map(file -> {
			try {
				byte[] bytes = Files.readAllBytes(file.toPath());
				return new WrappedXml(file.getName(), new String(bytes, StandardCharsets.UTF_8));
			} catch (IOException e) {
				return new WrappedXml(e.getMessage(), null);
			}
		});
	}

	public WrappedXml get() {
		return xmlStream.findAny().orElse(new WrappedXml("stream empty", null));
	}

	public Stream<WrappedXml> stream() {
		return xmlStream;
	}
	
	public Stream<WrappedXml> parallelStream() {
		return new ArrayList<>(FILES).parallelStream().map(file -> {
			try {
				byte[] bytes = Files.readAllBytes(file.toPath());
				return new WrappedXml(file.getName(), new String(bytes, StandardCharsets.UTF_8));
			} catch (IOException e) {
				return new WrappedXml(e.getMessage(), null);
			}
		});
	}

}
