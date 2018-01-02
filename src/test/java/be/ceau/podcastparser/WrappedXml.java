package be.ceau.podcastparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class WrappedXml {

	private final String description;
	private final String xml;

	public WrappedXml(String description, String xml) {
		this.description = description == null ? "" : description;
		this.xml = xml;
	}

	public WrappedXml(String description, InputStream in) throws IOException {
		this.description = description == null ? "" : description;
		this.xml = IOUtils.toString(in, StandardCharsets.UTF_8);
	}

	/**
	 * A description for the XML contained in this {@link WrappedXml}, eg. the
	 * filename in the case of XML extracted from a local file.
	 * 
	 * @return a {@link String}, never {@code null}
	 */
	public String getDescription() {
		return description;
	}

	public String getXml() {
		return xml;
	}

	public Reader getReader() {
		return new StringReader(xml);
	}
	
}
