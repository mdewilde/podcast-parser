package be.ceau.podcastparser.stax.models;

public class Enclosure {

	private String url;
	private long length;
	private String type;

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
