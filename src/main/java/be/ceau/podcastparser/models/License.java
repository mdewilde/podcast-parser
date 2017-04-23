package be.ceau.podcastparser.models;

/**
 * License information.
 */
public class License {

	private String href;
	private String type;
	private String label;

	/**
	 * <p>
	 * A link to a long-form version of the license.
	 * </p>
	 * <p>
	 * Listed in Media RSS namespace specification.
	 * </p>
	 * 
	 * @return a {@code String}, or {@code null}
	 */
	public String getHref() {
		return href;
	}

	public License setHref(String href) {
		this.href = href;
		return this;
	}

	/**
	 * <p>
	 * The document type of the href.
	 * </p>
	 * <p>
	 * Listed in Media RSS namespace specification.
	 * </p>
	 * 
	 * @return a {@code String}, or {@code null}
	 */
	public String getType() {
		return type;
	}

	public License setType(String type) {
		this.type = type;
		return this;
	}

	/**
	 * <p>
	 * Label to display license information.
	 * </p>
	 * <p>
	 * Listed in Media RSS namespace specification.
	 * </p>
	 * 
	 * @return a {@code String}, or {@code null}
	 */
	public String getLabel() {
		return label;
	}

	public License setLabel(String label) {
		this.label = label;
		return this;
	}

}
