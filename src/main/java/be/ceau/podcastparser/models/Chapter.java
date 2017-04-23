package be.ceau.podcastparser.models;

public class Chapter implements Comparable<Chapter> {

	private long start;
	private String title;
	private Link href;
	private Image image;

	/**
	 * <p>
	 * The number of milliseconds into the media this chapter starts.
	 * </p>
	 * <p>
	 * Required in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return a {@code long}
	 */
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * <p>
	 * The title of this chaper.
	 * </p>
	 * <p>
	 * Required in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return a {@code String}, or {@code null}
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * A link providing additional information for this chapter.
	 * </p>
	 * <p>
	 * Optional in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return a {@code Link}, or {@code null}
	 */
	public Link getHref() {
		return href;
	}

	public Link computeLinkIfAbsent() {
		if (href == null) {
			href = new Link();
		}
		return href;
	}

	public void setHref(Link href) {
		this.href = href;
	}

	/**
	 * <p>
	 * An image associated with this chapter.
	 * </p>
	 * <p>
	 * Optional in Simple Chapters namespaces specification.
	 * </p>
	 * 
	 * @return an {@code Image}, or {@code null}
	 */
	public Image getImage() {
		return image;
	}

	public Image computeImageIfAbsent() {
		if (image == null) {
			image = new Image();
		}
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public int compareTo(Chapter o) {
		return (int) (start - o.start);
	}

}
