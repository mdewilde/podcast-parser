/*
	
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import be.ceau.podcastparser.test.wrappedxml.EmptyXml;
import be.ceau.podcastparser.test.wrappedxml.FileXml;
import be.ceau.podcastparser.test.wrappedxml.WrappedXml;

public class FilesProvider implements TestXmlProvider {

	private static final Path BASE_DIRECTORY = Paths.get(System.getProperty("user.home"), "podcastfinder", "corpus");
	private static final File DIRECTORY;
	
	static {
		DIRECTORY = BASE_DIRECTORY.toFile();
		if (!DIRECTORY.isDirectory()) {
			throw new IllegalStateException(
					"base directory containing XML samples not found at " + BASE_DIRECTORY.toString());
		}
	}

	private final Stream<WrappedXml> xmlStream;

	public FilesProvider() {
		this.xmlStream = Arrays.stream(DIRECTORY.listFiles()).map(FileXml::instance);
	}

	public WrappedXml get() {
		return xmlStream.findAny().orElse(EmptyXml.INSTANCE);
	}

	public Stream<WrappedXml> stream() {
		return xmlStream;
	}

	public Stream<WrappedXml> parallelStream() {
		return xmlStream.parallel();
	}

}
