package be.ceau.podcastparser.models;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Item {

	private String title;
	private String subtitle;
	private final List<Link> links = new ArrayList<>();
	private String description;
	private final List<Person> authors = new ArrayList<>();
	private final List<Category> categories = new ArrayList<>();
	private String copyright;
	private String comments;
	private Enclosure enclosure;
	private String guid;
	private Temporal pubDate;
	private Temporal updated;
	private Temporal validity;
	private String source;
	private Rating rating;
	private Duration duration;
	private final List<MediaContent> mediaContents = new ArrayList<>();
	private final List<Image> images = new ArrayList<>();
	private final List<String> keywords = new ArrayList<>();

	/**
	 * <p>
	 * The title of the item.
	 * </p>
	 * <p>
	 * Optional as per RSS specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * Subtitle of the item.
	 * </p>
	 * <p>
	 * Part of the iTunes specification.
	 * </p>
	 * 
	 * @return a {@code String} or {@code null}
	 */
	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * <p>
	 * The URL to the item (RSS spec).
	 * </p>
	 * 
	 * <p>
	 * Required in RSS specification. Listed in Atom specification as element
	 * {@code link}.
	 * </p>
	 * 
	 * @return {@link List}
	 */
	public List<Link> getLinks() {
		return links;
	}

	public void addLink(Link link) {
		this.links.add(link);
	}

	/**
	 * The item synopsis.
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Email address of the author of the item.
	 */
	public List<Person> getAuthors() {
		return authors;
	}

	public void addAuthor(Person author) {
		this.authors.add(author);
	}

	/**
	 * Includes the item in one or more categories.
	 */
	public List<Category> getCategories() {
		return categories;
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}

	/**
	 * <p>
	 * The {@code rights} element is a Text construct that conveys information
	 * about rights held in and over an entry or feed.
	 * </p>
	 * <p>
	 * Listed as optional element {@code rights} in Atom specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * URL of a page for comments relating to the item.
	 */
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * <p>
	 * Describes a media object that is attached to the item.
	 * </p>
	 * <p>
	 * Optional for {@code item} elements in RSS specification.
	 * </p>
	 * 
	 * @return {@link Enclosure} or {@code null}
	 */
	public Enclosure getEnclosure() {
		return enclosure;
	}

	public void setEnclosure(Enclosure enclosure) {
		this.enclosure = enclosure;
	}

	/**
	 * <p>
	 * guid stands for globally unique identifier. It's a string that uniquely
	 * identifies the item. When present, an aggregator may choose to use this
	 * string to determine if an item is new.
	 * 
	 * <guid>http://some.server.com/weblogItem3207</guid>
	 * 
	 * There are no rules for the syntax of a guid. Aggregators must view them
	 * as a string. It's up to the source of the feed to establish the
	 * uniqueness of the string.
	 * 
	 * If the guid element has an attribute named "isPermaLink" with a value of
	 * true, the reader may assume that it is a permalink to the item, that is,
	 * a url that can be opened in a Web browser, that points to the full item
	 * described by the <item> element. An example:
	 * 
	 * <guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
	 * 
	 * isPermaLink is optional, its default value is true. If its value is
	 * false, the guid may not be assumed to be a url, or a url to anything in
	 * particular.
	 * </p>
	 * <p>
	 * Optional for {@code item} elements in RSS specification. Required item
	 * {@code id} as per Atom specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * <p>
	 * Date when the item was published.
	 * </p>
	 * <p>
	 * Optional for {@code entry} elements in Atom specification. Not part of
	 * RSS specification.
	 * </p>
	 * 
	 * @return a {@link Temporal} or {@code null}
	 */
	public Temporal getPubDate() {
		return pubDate;
	}

	public void setPubDate(Temporal pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * <p>
	 * The {@code updated} element is a Date construct indicating the most
	 * recent instant in time when an entry or feed was modified in a way the
	 * publisher considers significant.
	 * </p>
	 * <p>
	 * Optional for {@code item} elements in RSS specification. Optional for
	 * {@code entry} elements in Atom specification.
	 * </p>
	 * 
	 * @return a {@link Temporal} or {@code null}
	 */
	public Temporal getUpdated() {
		return updated;
	}

	public void setUpdated(Temporal updated) {
		this.updated = updated;
	}

	/**
	 * <p>
	 * Date (often a range) of validity of a resource.
	 * </p>
	 * <p>
	 * Specified in Dublin Core Terms RSS specification.
	 * </p>
	 * 
	 * @return a {@link Temporal} or {@code null}
	 */
	public Temporal getValidity() {
		return validity;
	}

	public void setValidity(Temporal validity) {
		this.validity = validity;
	}

	/**
	 * <p>
	 * The RSS channel that the item came from.
	 * </p>
	 * <p>
	 * Optional for {@code item} elements in RSS specification.
	 * </p>
	 * 
	 * @return String or {@code null}
	 */
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * <p>
	 * The rating for this item.
	 * </p>
	 * <p>
	 * Combines optional elements from iTunes RSS and Media RSS spec.
	 * </p>
	 * 
	 * @return {@Optional} {@link Rating} or {@code null}
	 */
	public Optional<Rating> getRating() {
		return Optional.ofNullable(rating);
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	/**
	 * <p>
	 * The duration of the media in the {@link Enclosure} of this {@link Item}.
	 * </p>
	 * <p>
	 * Not in RSS specification. Part of iTunes RSS spec.
	 * </p>
	 * 
	 * @return {@link Duration} or {@code null}
	 */
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	/**
	 * Parse and set duration.
	 * 
	 * @param duration
	 *            {@link String} formatted HH:MM:SS, H:MM:SS, MM:SS, or M:SS (H
	 *            = hours, M = minutes, S = seconds). If an integer is provided
	 *            (no colon present), the value is assumed to be in seconds. If
	 *            one colon is present, the number to the left is assumed to be
	 *            minutes, and the number to the right is assumed to be seconds.
	 *            If more than two colons are present, the numbers furthest to
	 *            the right are ignored.
	 */
	public void setDuration(String duration) {
		if (duration != null) {
			String[] split = duration.trim().split(":");
			try {
				switch (split.length) {
				case 0:
					return;
				case 1: {
					int seconds = Integer.parseInt(split[0]);
					this.duration = Duration.ofSeconds(seconds);
					return;
				}
				case 2: {
					int minutes = Integer.parseInt(split[0]);
					int seconds = Integer.parseInt(split[1]);
					this.duration = Duration.ofSeconds(minutes * 60 + seconds);
					return;
				}
				case 3:
				default: {
					int hours = Integer.parseInt(split[0]);
					int minutes = Integer.parseInt(split[1]);
					int seconds = Integer.parseInt(split[2]);
					this.duration = Duration.ofSeconds(hours * 3600 + minutes * 60 + seconds);
					return;
				}
				}
			} catch (NumberFormatException e) {
				this.duration = null;
			}
		} else {
			this.duration = null;
		}
	}

	/**
	 * <p>
	 * A more expressive alternative to {@link #getEnclosure()}. Not always
	 * present.
	 * </p>
	 * <p>
	 * Part of the Media RSS specification. There are no limitations on the
	 * number of {@link content} elements per {@code item}, though it is
	 * recommended to have only one.
	 * </p>
	 * 
	 * @return a {@link List}, never {@code null}
	 * @see MediaContent
	 */
	public List<MediaContent> getMediaContents() {
		return mediaContents;
	}

	public void addMediaContent(MediaContent mediaContent) {
		this.mediaContents.add(mediaContent);
	}

	public void addMediaContents(List<MediaContent> mediaContents) {
		this.mediaContents.addAll(mediaContents);
	}

	public List<Image> getImages() {
		return images;
	}

	public void addImage(Image image) {
		this.images.add(image);
	}

	/**
	 * <p>
	 * Not in RSS specification. Listed in iTunes RSS spec.
	 * </p>
	 * 
	 * @return a {@link List}, not {@code null}
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tItem [\n\t\t");
		if (title != null)
			builder.append("title=").append(title).append(", \n\t\t");
		if (links != null)
			builder.append("links=").append(links).append(", \n\t\t");
		if (description != null)
			builder.append("description=").append(description).append(", \n\t\t");
		if (authors != null)
			builder.append("authors=").append(authors).append(", \n\t\t");
		if (categories != null)
			builder.append("categories=").append(categories).append(", \n\t\t");
		if (copyright != null)
			builder.append("copyright=").append(copyright).append(", \n\t\t");
		if (comments != null)
			builder.append("comments=").append(comments).append(", \n\t\t");
		if (enclosure != null)
			builder.append("enclosure=").append(enclosure).append(", \n\t\t");
		if (guid != null)
			builder.append("guid=").append(guid).append(", \n\t\t");
		if (pubDate != null)
			builder.append("pubDate=").append(pubDate).append(", \n\t\t");
		if (updated != null)
			builder.append("updated=").append(updated).append(", \n\t\t");
		if (source != null)
			builder.append("source=").append(source).append(", \n\t\t");
		if (rating != null)
			builder.append("rating=").append(rating).append(", \n\t\t");
		if (duration != null)
			builder.append("duration=").append(duration).append(", \n\t\t");
		if (mediaContents != null)
			builder.append("mediaContents=").append(mediaContents).append(", \n\t\t");
		if (images != null)
			builder.append("images=").append(images);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
