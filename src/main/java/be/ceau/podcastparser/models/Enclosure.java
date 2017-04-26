package be.ceau.podcastparser.models;

import be.ceau.podcastparser.util.Strings;

public class Enclosure {

	private String url;
	private long length;
	private String type;
	private String description;
	
	/**
	 * <p>
	 * Url says where the enclosure is located.
	 * </p>
	 * <p>
	 * Required for {@code enclosure} elements in RSS specification.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>
	 * Length says how big the enclosure is in bytes.
	 * </p>
	 * <p>
	 * Required for {@code enclosure} elements in RSS specification.
	 * </p>
	 * 
	 * @return {@link Integer} or {@code null}
	 */
	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setLength(String length) {
		if (Strings.isNotBlank(length)) {
			try {
				this.length = Long.parseLong(length.trim());
			} catch (NumberFormatException e) {	}
		}
	}

	/**
	 * <p>
	 * The standard MIME type of the enclosure..
	 * </p>
	 * <p>
	 * Required for {@code enclosure} elements in RSS specification.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p>
	 * A short descriptor, used primarily for alternative enclosures.
	 * </p>
	 * <p>
	 * Not part of the RSS specification.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tEnclosure [\n\t\t");
		if (url != null)
			builder.append("url=").append(url).append(", \n\t\t");
		builder.append("length=").append(length).append(", \n\t\t");
		if (type != null)
			builder.append("type=").append(type);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
