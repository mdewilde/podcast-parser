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
 * A text transcript as specified in the Media namespace
 */
public class Transcript {
	/*
	 * <media:text type="plain" lang="en" start="00:00:03.000" end="00:00:10.000"> Oh, say, can you
	 * see</media:text>
	 */

	private String text;
	private String type;
	private String lang;
	private Long start;
	private Long end;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transcript [");
		if (text != null)
			builder.append("text=").append(text).append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
		if (lang != null)
			builder.append("lang=").append(lang).append(", ");
		if (start != null)
			builder.append("start=").append(start).append(", ");
		if (end != null)
			builder.append("end=").append(end);
		builder.append("]");
		return builder.toString();
	}

}
