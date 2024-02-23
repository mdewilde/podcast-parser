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

	public void setHash(String hash) {
		this.hash = hash;
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

	public void setAlgo(String algo) {
		this.algo = algo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Hash [");
		if (hash != null)
			builder.append("hash=").append(hash).append(", ");
		if (algo != null)
			builder.append("algo=").append(algo);
		builder.append("]");
		return builder.toString();
	}

}
