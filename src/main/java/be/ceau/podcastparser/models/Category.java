/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		https://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.podcastparser.models;

/**
 * <p>
 * Provide an indication of the content of a feed or item.
 * </p>
 * <p>
 * Used in iTunes and Media namespaces.
 * </p>
 */
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
