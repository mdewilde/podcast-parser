package be.ceau.podcastparser.test;

public class WrappedXml {

	private final String description;
	private final String xml;

	public WrappedXml(String description, String xml) {
		this.description = description == null ? "" : description;
		this.xml = xml;
	}

	public String getDescription() {
		return description;
	}

	public String getXml() {
		return xml;
	}
	
}
