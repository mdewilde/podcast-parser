package be.ceau.podcastparser.models;

public class Category {

	private String name;
	private String subcategory;

	public Category() {

	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, String subcategory) {
		this.name = name;
		this.subcategory = subcategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
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
