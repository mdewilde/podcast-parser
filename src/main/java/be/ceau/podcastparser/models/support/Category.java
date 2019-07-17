/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.models.support;

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

	public void setName(String name) {
		this.name = name;
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

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
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

	public void setScheme(String scheme) {
		this.scheme = scheme;
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

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
		result = prime * result + ((subcategory == null) ? 0 : subcategory.hashCode());
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
		Category other = (Category) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		if (subcategory == null) {
			if (other.subcategory != null)
				return false;
		} else if (!subcategory.equals(other.subcategory))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Category [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (subcategory != null)
			builder.append("subcategory=").append(subcategory).append(", ");
		if (scheme != null)
			builder.append("scheme=").append(scheme).append(", ");
		if (label != null)
			builder.append("label=").append(label);
		builder.append("]");
		return builder.toString();
	}

}
