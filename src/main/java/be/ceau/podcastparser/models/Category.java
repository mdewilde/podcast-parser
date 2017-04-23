package be.ceau.podcastparser.models;

public class Category {

	private String name;
	private String subcategory;
	private String scheme;
	private String label;

	/**
	 * <p>
	 * The actual category.
	 * </p>
	 * <p>
	 * Required in iTunes namespace specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getName() {
		return name;
	}

	public Category setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * <p>
	 * A subcategory.
	 * </p>
	 * <p>
	 * Optional in iTunes namespace specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getSubcategory() {
		return subcategory;
	}

	public Category setSubcategory(String subcategory) {
		this.subcategory = subcategory;
		return this;
	}

	/**
	 * <p>
	 * The URI that identifies the categorization scheme.
	 * </p>
	 * <p>
	 * Optional in Media RSS namespaces specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getScheme() {
		return scheme;
	}

	public Category setScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * <p>
	 * Human readable label that can be displayed in end user applications.
	 * </p>
	 * <p>
	 * Optional in Media RSS namespaces specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getLabel() {
		return label;
	}

	public Category setLabel(String label) {
		this.label = label;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tCategory [\n\t\t");
		if (name != null)
			builder.append("name=").append(name).append(", \n\t\t");
		if (subcategory != null)
			builder.append("subcategory=").append(subcategory).append("\n");
		builder.append("\t]\n");
		return builder.toString();
	}

}
