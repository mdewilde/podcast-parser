/*
	
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
 * A person construct is an element that describes a person, corporation, or
 * similar entity.
 * </p>
 * <p>
 * As listed in Atom specification.
 * </p>
 */
public class Person {

	private String name;
	private String uri;
	private String email;

	/**
	 * <p>
	 * The "atom:name" element's content conveys a human-readable name for the
	 * person. The content of atom:name is Language-Sensitive.
	 * </p>
	 * <p>
	 * Required as per Atom specified.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * The "atom:uri" element's content conveys an IRI (RFC3987) associated with
	 * the person.
	 * </p>
	 * <p>
	 * Optional as per Atom specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * <p>
	 * The "atom:email" element's content conveys an e-mail address (RFC2822)
	 * associated with the person.
	 * </p>
	 * <p>
	 * Optional as per Atom specified.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Person [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (uri != null)
			builder.append("uri=").append(uri).append(", ");
		if (email != null)
			builder.append("email=").append(email);
		builder.append("]");
		return builder.toString();
	}

}
