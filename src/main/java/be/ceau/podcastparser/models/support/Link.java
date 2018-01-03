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
 * atom:feed elements SHOULD contain one atom:link element with a rel attribute
 * value of "self". This is the preferred URI for retrieving Atom Feed Documents
 * representing this Atom feed. The "atom:link" element defines a reference from
 * an entry or feed to a Web resource. This specification assigns no meaning to
 * the content (if any) of this element.
 * </p>
 * atomLink = element atom:link { atomCommonAttributes, attribute href { atomUri
 * }, attribute rel { atomNCName | atomUri }?, attribute type { atomMediaType
 * }?, attribute hreflang { atomLanguageTag }?, attribute title { text }?,
 * attribute length { text }?, undefinedContent } *
 */
public class Link {

	private String href;
	private String rel;
	private String type;
	private String hreflang;
	private String title;
	private String length;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHreflang() {
		return hreflang;
	}

	public void setHreflang(String hreflang) {
		this.hreflang = hreflang;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Link [");
		if (href != null)
			builder.append("href=").append(href).append(", ");
		if (rel != null)
			builder.append("rel=").append(rel).append(", ");
		if (type != null)
			builder.append("type=").append(type).append(", ");
		if (hreflang != null)
			builder.append("hreflang=").append(hreflang).append(", ");
		if (title != null)
			builder.append("title=").append(title).append(", ");
		if (length != null)
			builder.append("length=").append(length);
		builder.append("]");
		return builder.toString();
	}

}
