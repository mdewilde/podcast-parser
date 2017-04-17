/*
	Copyright 2017 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.stax.models;

import java.time.Duration;

/**
 * Image instance, based on RSS spec {@code channel} level image.
 */
public class Image {

	private String url;
	private String title;
	private String link;
	private Integer width;
	private Integer height;
	private String description;
	private Duration time;

	/**
	 * <p>
	 * The URL of a GIF, JPEG or PNG image that represents the channel.
	 * </p>
	 * <p>
	 * Required for {@code image} elements in RSS specification.
	 * </p>
	 * 
	 * @return String or {@code null}
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>
	 * Describes the image, it's used in the ALT attribute of the HTML
	 * {@code <img>} tag when the channel is rendered in HTML
	 * </p>
	 * <p>
	 * Required for {@code image} elements in RSS specification.
	 * </p>
	 * 
	 * @return String or {@code null}
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * The URL of the site, when the channel is rendered, the image is a link to
	 * the site. (Note, in practice the image {@code <title>} and {@code <link>}
	 * should have the same value as the channel's {@code <title>} and
	 * {@code <link>}.
	 * </p>
	 * <p>
	 * Required for {@code image} elements in RSS specification.
	 * </p>
	 * 
	 * @return String or {@code null}
	 */
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * <p>
	 * Indicates the width of the image in pixels.
	 * </p>
	 * <p>
	 * Optional for {@code image} elements in RSS specification.
	 * </p>
	 * 
	 * @return Integer or {@code null}
	 */
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * <p>
	 * Indicates the height of the image in pixels.
	 * </p>
	 * <p>
	 * Optional for {@code image} elements in RSS specification.
	 * </p>
	 * 
	 * @return Integer or {@code null}
	 */
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * <p>
	 * Text that is included in the TITLE attribute of the link formed around
	 * the image in the HTML rendering.
	 * </p>
	 * <p>
	 * Optional for {@code image} elements in RSS specification.
	 * </p>
	 * 
	 * @return String or {@code null}
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>
	 * The time offset in relation to the media object. Typically this is used
	 * when creating multiple keyframes within a single video. The format for
	 * this attribute should be in the DSM-CC's Normal Play Time (NTP) as used
	 * in RTSP [RFC 2326 3.6 Normal Play Time]. It is an optional attribute.
	 * </p>
	 * <p>
	 * Optional for {@code thumbnail} elements in Media RSS specification.
	 * </p>
	 * 
	 * @return {@code Duration} or {@code null}
	 */
	public Duration getTime() {
		return time;
	}

	public void setTime(Duration time) {
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tImage [\n\t\t");
		if (url != null)
			builder.append("url=").append(url).append(", \n\t\t");
		if (title != null)
			builder.append("title=").append(title).append(", \n\t\t");
		if (link != null)
			builder.append("link=").append(link).append(", \n\t\t");
		if (width != null)
			builder.append("width=").append(width).append(", \n\t\t");
		if (height != null)
			builder.append("height=").append(height).append(", \n\t\t");
		if (description != null)
			builder.append("description=").append(description).append(", \n\t\t");
		if (time != null)
			builder.append("time=").append(time);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
