package be.ceau.podcastparser.stax.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

public class UnmappedElement {

	private final String namespaceUri;
	private final String localName;
	private final Set<String> attributes; 
	private final String level;
	
	public UnmappedElement(XMLStreamReader reader, String level) {
		this.namespaceUri = reader.getNamespaceURI();
		this.localName = reader.getLocalName();
		this.attributes = Collections.unmodifiableSet(extractAttributes(reader));
		this.level = level;
	}

	public String getNamespaceUri() {
		return namespaceUri;
	}

	public String getLocalName() {
		return localName;
	}

	public Set<String> getAttributes() {
		return attributes;
	}

	public String getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(namespaceUri)
				.append(" level=").append(level)
				.append(" localName=").append(localName)
				.append(" attributes=").append(attributes)
				.append("]")
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((localName == null) ? 0 : localName.hashCode());
		result = prime * result + ((namespaceUri == null) ? 0 : namespaceUri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnmappedElement other = (UnmappedElement) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (localName == null) {
			if (other.localName != null)
				return false;
		} else if (!localName.equals(other.localName))
			return false;
		if (namespaceUri == null) {
			if (other.namespaceUri != null)
				return false;
		} else if (!namespaceUri.equals(other.namespaceUri))
			return false;
		return true;
	}

	private static Set<String> extractAttributes(XMLStreamReader reader) {
		Set<String> attributes = new HashSet<>();
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			attributes.add(reader.getAttributeLocalName(i));
		}
		return attributes;
	}
	

}
