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

import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

/**
 * Utility class for handling MIME-Types of enclosures
 */
public class EnclosureUtils {

	private final static Pattern VALID_MIMETYPE = Pattern.compile("audio/.*|video/.*|application/ogg");

	private EnclosureUtils() {}

	public static boolean enclosureTypeValid(String type) {
		if (type == null) {
			return false;
		}
		return VALID_MIMETYPE.matcher(type).matches();
	}

	/**
	 * Should be used if mime-type of enclosure tag is not supported. This
	 * method will check if the mime-type of the file extension is supported. If
	 * the type is not supported, this method will return null.
	 */
	public static String getValidMimeTypeFromUrl(String url) {
		if (url != null) {
			String extension = FilenameUtils.getExtension(url);
			if (extension != null) {
				String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
				if (type != null && enclosureTypeValid(type)) {
					return type;
				}
			}
		}
		return null;
	}
}
