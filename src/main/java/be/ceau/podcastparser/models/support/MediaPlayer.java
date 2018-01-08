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
 * <p>
 * Instances represent a reference to a web browser media player console
 * </p>
 * <p>
 * As specified in the Media namespace.
 * </p>
 */
public class MediaPlayer {

	private String url;
	private Integer height;
	private Integer width;

	/**
	 * Url is the URL of the player console that plays the media. It is a required attribute.
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Height is the height of the browser window that the URL should be opened in. It is an optional
	 * attribute.
	 */
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * Width is the width of the browser window that the URL should be opened in. It is an optional
	 * attribute.
	 */
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MediaPlayer [");
		if (url != null)
			builder.append("url=").append(url).append(", ");
		builder.append("height=").append(height).append(", width=").append(width).append("]");
		return builder.toString();
	}

}
