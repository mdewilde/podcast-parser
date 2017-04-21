package be.ceau.podcastparser.models;

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
		builder.append("\n\tPerson [\n\t\t");
		if (name != null)
			builder.append("name=").append(name).append(", \n\t\t");
		if (uri != null)
			builder.append("uri=").append(uri).append(", \n\t\t");
		if (email != null)
			builder.append("email=").append(email);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
