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
package be.ceau.podcastparser.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class URLUtil {

	/** Regex used to parse content-disposition headers */
	private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern
			.compile("attachment;\\s*filename\\s*=\\s*(\"?)([^\"]*)\\1\\s*$", Pattern.CASE_INSENSITIVE);

	/*
	 * Parse the Content-Disposition HTTP Header. The format of the header is
	 * defined here: http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html This
	 * header provides a filename for content that is going to be downloaded to
	 * the file system. We only support the attachment type. Note that RFC 2616
	 * specifies the filename value must be double-quoted. Unfortunately some
	 * servers do not quote the value so to maintain consistent behaviour with
	 * other browsers, we allow unquoted values too.
	 */
	static String parseContentDisposition(String contentDisposition) {
		try {
			Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
			if (m.find()) {
				return m.group(2);
			}
		} catch (IllegalStateException ex) {
			// This function is defined as returning null when it can't parse
			// the header
		}
		return null;
	}
}
