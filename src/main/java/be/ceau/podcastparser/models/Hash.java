package be.ceau.podcastparser.models;

/**
 * <p>
 * Contains a hashcode, as well as information about the algorithm used to
 * calculate the hash.
 * </p>
 *
 * <p>
 * Specified in the Media RSS namespace specification.
 * </p>
 */
public class Hash {

	private String hash;
	private String algo;

	/**
	 * <p>
	 * The hashcode for the related media item.
	 * </p>
	 * <p>
	 * Specified in the Media RSS namespace specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getHash() {
		return hash;
	}

	public Hash setHash(String hash) {
		this.hash = hash;
		return this;
	}

	/**
	 * <p>
	 * The algorithm used to create the hash. Possible values are "md5" and
	 * "sha-1". Default value is "md5".
	 * </p>
	 * <p>
	 * Specified in the Media RSS namespace specification, optional.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getAlgo() {
		return algo;
	}

	public Hash setAlgo(String algo) {
		this.algo = algo;
		return this;
	}

}
