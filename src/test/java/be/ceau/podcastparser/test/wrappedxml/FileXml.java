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
package be.ceau.podcastparser.test.wrappedxml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileXml implements WrappedXml {

	private final File file;
	private final String xml;

	public static WrappedXml instance(Path path) {
		try {
			return new FileXml(path.toFile());
		} catch (IOException e) {
			return EmptyXml.INSTANCE;
		}
	}

	public static WrappedXml instance(File file) {
		try {
			return new FileXml(file);
		} catch (IOException e) {
			return EmptyXml.INSTANCE;
		}
	}

	private FileXml(File file) throws IOException {
		this.file = file;
		this.xml = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining(System.lineSeparator()));
	}

	@Override
	public String getFullPath() {
		return file.getAbsolutePath();
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String getXml() {
		return xml;
	}

	@Override
	public Reader getReader() {
		return new StringReader(xml);
	}

	@Override
	public boolean delete() {
		return file.delete();
	}
	
}
