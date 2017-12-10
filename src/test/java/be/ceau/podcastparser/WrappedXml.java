package be.ceau.podcastparser;

public class WrappedXml {

	private final String description;
	private final String xml;

	public WrappedXml(String description, String xml) {
		this.description = description == null ? "" : description;
		this.xml = xml;
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

}
