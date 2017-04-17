package be.ceau.podcastparser.stax.models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Feed {

	private String id;
	private String title;
	private final List<Link> links = new ArrayList<>();
	private String description;
	private final List<Item> items = new ArrayList<>();
	private String language;
	private String copyright;
	private String managingEditor;
	private String webMaster;
	private String pubDate;
	private String lastBuildDate;
	private final List<Category> categories = new ArrayList<>();
	private String generator;
	private String docs;
	private String cloud;
	private Duration ttl;
	private final List<Image> images = new ArrayList<>();
	private String textInput;
	private String skipHours;
	private String skipDays;
	private String explicit;
	private String subtitle;
	private final List<Person> authors = new ArrayList<>();
	private Person owner;
	private final List<Person> contributors = new ArrayList<>();
	private final List<String> keywords = new ArrayList<>();

	/**
	 * <p>
	 * The {@code id} element conveys a permanent, universally unique identifier
	 * for an entry or feed.
	 * </p>
	 * <p>
	 * Part of Atom specification. Not listed in RSS specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The name of the channel. It's how people refer to your service. If you
	 * have an HTML website that contains the same information as your RSS file,
	 * the title of your channel should be the same as the title of your
	 * website.
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * The URL to the HTML website corresponding to the channel in RSS spec.
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
	 * Phrase or sentence describing the channel.
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * An item may represent a "story" -- much like a story in a newspaper or
	 * magazine; if so its description is a synopsis of the story, and the link
	 * points to the full story. An item may also be complete in itself, if so,
	 * the description contains the text (entity-encoded HTML is allowed), and
	 * the link and title may be omitted. All elements of an item are optional,
	 * however at least one of title or description must be present.
	 */
	public List<Item> getItems() {
		return items;
	}

	// TODO needed by Atom?
	public void setItems(List<Item> items) {
		this.items.clear();
		if (items != null) {
			this.items.addAll(items);
		}
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	/**
	 * The language the channel is written in. This allows aggregators to group
	 * all Italian language sites, for example, on a single page. A list of
	 * allowable values for this element, as provided by Netscape, is here. You
	 * may also use values defined by the W3C.
	 */
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Copyright notice for content in the channel.
	 */
	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * Email address for person responsible for editorial content.
	 */
	public String getManagingEditor() {
		return managingEditor;
	}

	public void setManagingEditor(String managingEditor) {
		this.managingEditor = managingEditor;
	}

	/**
	 * Email address for person responsible for technical issues relating to
	 * channel.
	 */
	public String getWebMaster() {
		return webMaster;
	}

	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}

	/**
	 * The publication date for the content in the channel. For example, the New
	 * York Times publishes on a daily basis, the publication date flips once
	 * every 24 hours. That's when the pubDate of the channel changes. All
	 * date-times in RSS conform to the Date and Time Specification of RFC 822,
	 * with the exception that the year may be expressed with two characters or
	 * four characters (four preferred).
	 */
	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * <p>
	 * The last time the content of the channel changed.
	 * </p>
	 * 
	 * <p>
	 * Optional in RSS specification. Listed in Atom specification as element
	 * {@code updated}.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(String lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	/**
	 * Specify one or more categories that the channel belongs to. Follows the
	 * same rules as the {@link Item}-level category element.
	 */
	public List<Category> getCategories() {
		return categories;
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}

	/**
	 * A string indicating the program used to generate the channel.
	 */
	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	/**
	 * A URL that points to the documentation for the format used in the RSS
	 * file. It's probably a pointer to this page. It's for people who might
	 * stumble across an RSS file on a Web server 25 years from now and wonder
	 * what it is.
	 */
	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	/**
	 * Allows processes to register with a cloud to be notified of updates to
	 * the channel, implementing a lightweight publish-subscribe protocol for
	 * RSS feeds.
	 */
	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
	}

	/**
	 * <p>
	 * TTL stands for time to live. It's a number of minutes that indicates how
	 * long a channel can be cached before refreshing from the source.
	 * </p>
	 * <p>
	 * Optional in RSS specification.
	 * </p>
	 * 
	 * @return {@link Duration} or {@code null}
	 */
	public Duration getTtl() {
		return ttl;
	}

	public void setTtl(Duration ttl) {
		this.ttl = ttl;
	}

	public void setTtl(String ttl) {
		if (ttl != null) {
			try {
				int minutes = Integer.parseInt(ttl.trim());
				this.ttl = Duration.ofMinutes(minutes);
			} catch (NumberFormatException e) {
			}
		}
	}

	/**
	 * <p>
	 * Specifies a GIF, JPEG or PNG image that can be displayed with the
	 * channel.
	 * </p>
	 * 
	 * @return a {@link List}, never {@code null}
	 */
	public List<Image> getImages() {
		return images;
	}

	public void addImage(Image image) {
		this.images.add(image);
	}

	/**
	 * Specifies a text input box that can be displayed with the channel.
	 */
	public String getTextInput() {
		return textInput;
	}

	public void setTextInput(String textInput) {
		this.textInput = textInput;
	}

	/**
	 * A hint for aggregators telling them which hours they can skip.
	 */
	public String getSkipHours() {
		return skipHours;
	}

	public void setSkipHours(String skipHours) {
		this.skipHours = skipHours;
	}

	/**
	 * A hint for aggregators telling them which days they can skip.
	 */
	public String getSkipDays() {
		return skipDays;
	}

	public void setSkipDays(String skipDays) {
		this.skipDays = skipDays;
	}

	/**
	 * <p>
	 * This tag should be used to indicate whether or not your podcast contains
	 * explicit material. The three values for this tag are "yes", "no", and
	 * "clean".
	 * </p>
	 * <p>
	 * Not in RSS specification. Listed in iTunes RSS spec.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getExplicit() {
		return explicit;
	}

	public void setExplicit(String explicit) {
		this.explicit = explicit;
	}

	/**
	 * <p>
	 * Description of the feed.
	 * </p>
	 * 
	 * <p>
	 * Not in RSS specification. Listed in iTunes RSS spec.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * <p>
	 * Artist(s) or author(s) of the feed.
	 * </p>
	 * 
	 * <p>
	 * Not in RSS specification. Listed in Atom spec & iTunes RSS spec.
	 * </p>
	 * 
	 * @return {@link String} or {@code null}
	 */
	public List<Person> getAuthors() {
		return authors;
	}

	public void addAuthor(Person author) {
		this.authors.add(author);
	}

	/**
	 * <p>
	 * Information that can be used to contact the owner of the podcast for
	 * communication specifically about their podcast.
	 * </p>
	 * 
	 * <p>
	 * Not in RSS specification. Listed in iTunes RSS spec.
	 * </p>
	 * 
	 * @return {@link Person} or {@code null}
	 */
	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	public List<Person> getContributors() {
		return contributors;
	}

	public void addContributor(Person contributor) {
		this.contributors.add(contributor);
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
		builder.append("\n\tFeed [\n\t\t");
		if (id != null)
			builder.append("id=").append(id).append(", \n\t\t");
		if (title != null)
			builder.append("title=").append(title).append(", \n\t\t");
		if (links != null)
			builder.append("links=").append(links).append(", \n\t\t");
		if (description != null)
			builder.append("description=").append(description).append(", \n\t\t");
		if (items != null)
			builder.append("items=").append(items).append(", \n\t\t");
		if (language != null)
			builder.append("language=").append(language).append(", \n\t\t");
		if (copyright != null)
			builder.append("copyright=").append(copyright).append(", \n\t\t");
		if (managingEditor != null)
			builder.append("managingEditor=").append(managingEditor).append(", \n\t\t");
		if (webMaster != null)
			builder.append("webMaster=").append(webMaster).append(", \n\t\t");
		if (pubDate != null)
			builder.append("pubDate=").append(pubDate).append(", \n\t\t");
		if (lastBuildDate != null)
			builder.append("lastBuildDate=").append(lastBuildDate).append(", \n\t\t");
		if (categories != null)
			builder.append("category=").append(categories).append(", \n\t\t");
		if (generator != null)
			builder.append("generator=").append(generator).append(", \n\t\t");
		if (docs != null)
			builder.append("docs=").append(docs).append(", \n\t\t");
		if (cloud != null)
			builder.append("cloud=").append(cloud).append(", \n\t\t");
		if (ttl != null)
			builder.append("ttl=").append(ttl).append(", \n\t\t");
		if (images != null)
			builder.append("images=").append(images).append(", \n\t\t");
		if (textInput != null)
			builder.append("textInput=").append(textInput).append(", \n\t\t");
		if (skipHours != null)
			builder.append("skipHours=").append(skipHours).append(", \n\t\t");
		if (skipDays != null)
			builder.append("skipDays=").append(skipDays).append(", \n\t\t");
		if (explicit != null)
			builder.append("explicit=").append(explicit).append(", \n\t\t");
		if (subtitle != null)
			builder.append("subtitle=").append(subtitle).append(", \n\t\t");
		if (authors != null)
			builder.append("authors=").append(authors).append(", \n\t\t");
		if (owner != null)
			builder.append("owner=").append(owner).append(", \n\t\t");
		if (contributors != null)
			builder.append("contributors=").append(contributors);
		builder.append("\n\t]\n");
		return builder.toString();
	}

}
