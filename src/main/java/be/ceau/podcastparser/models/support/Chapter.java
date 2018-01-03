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

public class Chapter implements Comparable<Chapter> {

	private long start;
	private String title;
	private Link href;
	private Image image;

	/**
	 * <p>
	 * The number of milliseconds into the media this chapter starts.
	 * </p>
	 * <p>
	 * Required in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return a {@code long}
	 */
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * <p>
	 * The title of this chaper.
	 * </p>
	 * <p>
	 * Required in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return a {@code String}, or {@code null}
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * A link providing additional information for this chapter.
	 * </p>
	 * <p>
	 * Optional in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return a {@code Link}, or {@code null}
	 */
	public Link getHref() {
		return href;
	}

	public void setHref(Link href) {
		this.href = href;
	}

	/**
	 * <p>
	 * An image associated with this chapter.
	 * </p>
	 * <p>
	 * Optional in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return an {@code Image}, or {@code null}
	 */
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public int compareTo(Chapter o) {
		return (int) (start - o.start);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Chapter [start=").append(start).append(", ");
		if (title != null)
			builder.append("title=").append(title).append(", ");
		if (href != null)
			builder.append("href=").append(href).append(", ");
		if (image != null)
			builder.append("image=").append(image);
		builder.append("]");
		return builder.toString();
	}

}
