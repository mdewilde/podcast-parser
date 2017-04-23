package be.ceau.podcastparser.models;

/**
 * <p>
 * Notable entity and the contribution to the creation of the media object.
 * </p>
 * 
 * <p>
 * Specified in Media RSS namespace specification.
 * </p>
 */
public class Credit {

	/*
	 * <media:credit role="producer" scheme="urn:ebu">entity name</media:credit>
	 * 
	 * <media:credit role="owner" scheme="urn:yvs">copyright holder of the
	 * entity</media:credit>
	 * 
	 * role specifies the role the entity played. Must be lowercase. It is an
	 * optional attribute.
	 * 
	 * scheme is the URI that identifies the role scheme. It is an optional
	 * attribute and possible values for this attribute are ( urn:ebu | urn:yvs
	 * ) . The default scheme is "urn:ebu". The list of roles supported under
	 * urn:ebu scheme can be found at European Broadcasting Union Role Codes.
	 * The roles supported under urn:yvs scheme are ( uploader | owner ).
	 */
	private String entity;
	private String role;
	private String scheme;

	/**
	 * <p>
	 * The entity that the credit is assigned to.
	 * </p>
	 * <p>
	 * Specified in Media RSS namespace specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * <p>
	 * The role the entity played.
	 * </p>
	 * <p>
	 * Specified in Media RSS namespace specification (optional).
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * <p>
	 * URN that identifies the role scheme (urn:ebu|urn:yvs).
	 * </p>
	 * <p>
	 * Specified in Media RSS namespace specification (optional).
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 * @see https://tools.ietf.org/html/rfc5174
	 */
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

}
