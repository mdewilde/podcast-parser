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
