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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

import be.ceau.podcastparser.WrappedXml;

public class ZipFilesProvider implements TestXmlProvider, AutoCloseable {

	private static final Path CORPUS_2017_04_15 = Paths.get(System.getProperty("user.home"), "podcastfinder", "corpus_2017-04-15.zip");

	private final ZipFile zipFile;
	private final Stream<WrappedXml> xmlStream;

	public ZipFilesProvider() {
		try {
			zipFile = new ZipFile(CORPUS_2017_04_15.toFile());
			xmlStream = zipFile.stream()
				.map(e -> {
					try {
						return new WrappedXml(e.getName(), zipFile.getInputStream(e));
					} catch (IOException e1) {
						return new WrappedXml("stream empty", "");
					}
				});
				
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public WrappedXml get() {
		return xmlStream.findAny().orElse(new WrappedXml("stream empty", ""));
	}

	public Stream<WrappedXml> stream() {
		return xmlStream;
	}
	
	public Stream<WrappedXml> parallelStream() {
		return xmlStream.parallel();
	}

	@Override
	public void close() {
		try {
			this.zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
