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
package be.ceau.podcastparser.models;

import java.time.Duration;

/**
 * Media RSS is a new RSS module that supplements the {@link enclosure}
 * capabilities of RSS 2.0. RSS enclosures are already being used to syndicate
 * audio files and images. Media RSS extends enclosures to handle other media
 * types, such as short films or TV, as well as provide additional metadata with
 * the media.
 */
public class MediaContent {

	private String url;
	private Long fileSize;
	private String type;
	private String medium;
	private String isDefault;
	private String expression = "full";
	private Long bitrate;
	private Long framerate;
	private String samplingrate;
	private String channels;
	private Duration duration;
	private Integer height;
	private Integer width;
	private String lang;

	/**
	 * The direct URL to the media object.
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * <p>
	 * The number of bytes of the media object.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link Long} or {@code null}
	 */
	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * <p>
	 * The standard MIME type of the object.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link Long} or {@code null}
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p>
	 * The type of object (image | audio | video | document | executable).
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	/**
	 * <p>
	 * Whether this is the default {@link MediaContent} object that should be
	 * used for the group.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * <p>
	 * Whether this is a sample or the full version or a continuous stream of
	 * this object. Default value is {@code full}. Possible values (sample |
	 * full | nonstop).
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * <p>
	 * The kilobits per second rate of media.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link Long} or {@code null}
	 */
	public Long getBitrate() {
		return bitrate;
	}

	public void setBitrate(long bitrate) {
		this.bitrate = bitrate;
	}

	/**
	 * <p>
	 * The number of frames per second for the media object.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link Long} or {@code null}
	 */
	public Long getFramerate() {
		return framerate;
	}

	public void setFramerate(long framerate) {
		this.framerate = framerate;
	}

	/**
	 * <p>
	 * The number of samples per second taken to create the media object. It is
	 * expressed in thousands of samples per second (kHz).
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getSamplingrate() {
		return samplingrate;
	}

	public void setSamplingrate(String samplingrate) {
		this.samplingrate = samplingrate;
	}

	/**
	 * <p>
	 * The number of audio channels in the media object.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	/**
	 * <p>
	 * The number of seconds the media object plays.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link Duration} or {@code null}
	 */
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	/**
	 * <p>
	 * The height of the media object.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return an {@link Integer} or {@code null}
	 */
	public Integer getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * <p>
	 * The width of the media object.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return an {@link Integer} or {@code null}
	 */
	public Integer getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * <p>
	 * The primary language encapsulated in the media object. Language codes
	 * possible are detailed in RFC 3066.
	 * </p>
	 * <p>
	 * Optional in Media RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 * @see http://www.i18nguy.com/unicode/language-identifiers.html
	 * @see https://www.ietf.org/rfc/rfc3066.txt
	 */
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tMediaContent [\n\t\t");
		if (url != null)
			builder.append("url=").append(url).append(", \n\t\t");
		builder.append("fileSize=").append(fileSize).append(", \n\t\t");
		if (type != null)
			builder.append("type=").append(type).append(", \n\t\t");
		if (medium != null)
			builder.append("medium=").append(medium).append(", \n\t\t");
		if (isDefault != null)
			builder.append("isDefault=").append(isDefault).append(", \n\t\t");
		if (expression != null)
			builder.append("expression=").append(expression).append(", \n\t\t");
		if (bitrate != null)
			builder.append("bitrate=").append(bitrate).append(", \n\t\t");
		if (framerate != null)
			builder.append("framerate=").append(framerate).append(", \n\t\t");
		if (samplingrate != null)
			builder.append("samplingrate=").append(samplingrate).append(", \n\t\t");
		if (channels != null)
			builder.append("channels=").append(channels).append(", \n\t\t");
		if (duration != null)
			builder.append("duration=").append(duration).append(", \n\t\t");
		if (height != null)
			builder.append("height=").append(height).append(", \n\t\t");
		if (width != null)
			builder.append("width=").append(width).append(", \n\t\t");
		if (lang != null)
			builder.append("lang=").append(lang).append(", \n");
		builder.append("]\n");
		return builder.toString();
	}

}
