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

public class Comments {

	private Integer number;
	private Link link;
	
	/**
	 * <p>
	 * The number of comments on this item.
	 * </p>
	 * <p>
	 * Specified in the Slash namespace specification.
	 * </p>
	 * 
	 * @return an {@code Integer} or {@code null}
	 */
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * <p>
	 * URL of a page for comments relating to the item.
	 * </p>
	 * <p>
	 * Optional in RSS specification.
	 * </p>
	 * 
	 * @return a {@code Link} or {@code null}
	 */
	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Comments [");
		if (number != null)
			builder.append("number=").append(number).append(", ");
		if (link != null)
			builder.append("link=").append(link);
		builder.append("]");
		return builder.toString();
	}

}
