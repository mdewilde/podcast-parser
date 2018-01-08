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
package be.ceau.podcastparser.models.support;

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

	public void setHref(String href) {
		this.href = href;
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

	public void setType(String type) {
		this.type = type;
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

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("License [");
		if (href != null)
			builder.append("href=").append(href).append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
		if (label != null)
			builder.append("label=").append(label);
		builder.append("]");
		return builder.toString();
	}

}
