package be.ceau.podcastparser.models;

public class Comments {

	private Integer number;
	private Link link;
	
	/**
	 * <p>
	 * The number of comments on this item.
	 * </p>
	 * <p>
	 * Specified in the Slash namespace specification.
	 * </p>
	 * 
	 * @return an {@code Integer} or {@code null}
	 */
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * <p>
	 * URL of a page for comments relating to the item.
	 * </p>
	 * <p>
	 * Optional in RSS specification.
	 * </p>
	 * 
	 * @return a {@code Link} or {@code null}
	 */
	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

}
