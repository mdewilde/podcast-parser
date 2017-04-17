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
