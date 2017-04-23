package be.ceau.podcastparser.models;

public class Rating {

	private String text;
	private String scheme;
	private String explicit;
	private String adultContent;

	/**
	 * <p>
	 * The actual rating.
	 * </p>
	 * <p>
	 * Optional in the Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 * @see Rating#getScheme()
	 */
	public String getText() {
		return text;
	}

	public Rating setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * <p>
	 * The content rating scheme used in the {@code text} property of this
	 * {@link Rating}.
	 * </p>
	 * <p>
	 * Optional attribute specified in the Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 * @see https://en.wikipedia.org/wiki/Category:Media_content_ratings_systems
	 */
	public String getScheme() {
		return scheme;
	}

	public Rating setScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * <p>
	 * This tag should be used to indicate whether or not your podcast contains
	 * explicit material. The three values for this tag are "yes", "no", and
	 * "clean".
	 * </p>
	 * <p>
	 * Not in RSS specification. Part of iTunes RSS spec.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getExplicit() {
		return explicit;
	}

	public Rating setExplicit(String explicit) {
		this.explicit = explicit;
		return this;
	}

	/**
	 * <p>
	 * Not in RSS specification. Part of Blogger RSS namespace.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getAdultContent() {
		return adultContent;
	}

	public Rating setAdultContent(String adultContent) {
		this.adultContent = adultContent;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tRating [\n\t\t");
		if (text != null)
			builder.append("text=").append(text).append(", \n\t\t");
		if (scheme != null)
			builder.append("scheme=").append(scheme).append(", \n\t\t");
		if (explicit != null)
			builder.append("explicit=").append(explicit).append("\n");
		builder.append("\t]\n");
		return builder.toString();
	}

}
