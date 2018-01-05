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
 * Additional meta information that may complement the enclosure of an item and/or may be used
 * during the playback of the enclosure’s media.
 * </p>
 * <p>
 * Part of the RawVoice namespace
 * </p>
 * <p>
 * {@code 
 * <rawvoice:metamark type=”tag” position=
”480″>This is where I discuss how to properly beat eggs</rawvoice:metamark>
 * }
 * </p>
 */
public class Metamark {

	private String text;
	private String link;
	private String type;
	private Integer position;
	private Integer duration;

	/**
	 * The text to display
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * The URL to the media, web page or image
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * The URL to the media, web page or image
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * The position in seconds where the metamark should be handled during playback
	 * 
	 * @return {@link Integer} or {@code null}
	 */
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * The duration in seconds the metamark should be displayed
	 * 
	 * @return {@link Integer} or {@code null}
	 */
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Metamark [");
		if (text != null)
			builder.append("text=").append(text).append(", ");
		if (link != null)
			builder.append("link=").append(link).append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
		if (position != null)
			builder.append("position=").append(position).append(", ");
		if (duration != null)
			builder.append("duration=").append(duration);
		builder.append("]");
		return builder.toString();
	}

}
