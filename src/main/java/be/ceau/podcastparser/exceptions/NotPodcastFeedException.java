package be.ceau.podcastparser.exceptions;

/**
 * {@link RuntimeException} indicating that the parsed file is not RSS or Atom.
 */
public class NotPodcastFeedException extends PodcastParserException {

	private static final long serialVersionUID = 1514315686879L;

	public NotPodcastFeedException(String message) {
		super(message);
	}

	public NotPodcastFeedException(String message, String arg0) {
		super(message.replaceFirst("\\{\\}", arg0));
	}

	public NotPodcastFeedException(Throwable cause) {
		super(cause);
	}

}
