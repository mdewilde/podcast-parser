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
package be.ceau.podcastparser.test.wrappedxml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ZipXml implements WrappedXml{

	private final String path;
	private final String name;
	private final String xml;

	public static WrappedXml instance(ZipFile zipFile, ZipEntry zipEntry) {
		try {
			String xml = IOUtils.readLines(zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8)
				.stream()
				.collect(Collectors.joining(System.lineSeparator()));
			return new ZipXml(zipFile.getName(), zipEntry.getName(), xml);
		} catch (IOException e) {
			return EmptyXml.INSTANCE;
		}
	}

	private ZipXml(String path, String name, String xml) throws IOException {
		this.path = path;
		this.name = name;
		this.xml = xml;
	}

	@Override
	public String getFullPath() {
		return path;
	}

	@Override
	public String getName() {
		return name;
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
		return false;
	}
	
}
