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
package be.ceau.podcastparser.models.core;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import be.ceau.podcastparser.models.support.Category;
import be.ceau.podcastparser.models.support.Copyright;
import be.ceau.podcastparser.models.support.Credit;
import be.ceau.podcastparser.models.support.GeoPoint;
import be.ceau.podcastparser.models.support.Image;
import be.ceau.podcastparser.models.support.Link;
import be.ceau.podcastparser.models.support.OtherValueKey;
import be.ceau.podcastparser.models.support.Person;
import be.ceau.podcastparser.models.support.Rating;
import be.ceau.podcastparser.models.support.TypedString;
import be.ceau.podcastparser.models.support.UpdateInfo;
import be.ceau.podcastparser.util.Strings;

public class Feed {

	private String id;
	private String title;
	private final Set<Link> links = new HashSet<>();

	private TypedString description;
	private String subtitle;
	private String summary;

	private final List<Item> items = new ArrayList<>();
	private String language;

	private Copyright copyright;
	private String managingEditor;
	private String webMaster;
	private ZonedDateTime pubDate;
	private ZonedDateTime lastBuildDate;
	private final Set<Category> categories = new HashSet<>();
	private String generator;
	private String docs;
	private String cloud;
	private Duration ttl;
	private final List<Image> images = new ArrayList<>();
	private String textInput;
	private final List<Integer> skipHours = new ArrayList<>();
	private final List<String> skipDays = new ArrayList<>();
	private final List<Person> authors = new ArrayList<>();
	private Person owner;
	private final List<Person> contributors = new ArrayList<>();
	private final Set<String> keywords = new HashSet<>();
	private UpdateInfo updateInfo;
	private final Rating rating = new Rating();
	private String location;
	private final Map<OtherValueKey, String> values = new EnumMap<>(OtherValueKey.class);
	private GeoPoint geoPoint;
	private boolean block;
	private String type;
	private String email;
	private Credit credit;
	private boolean complete;
	private TypedString browserFriendly;

	/**
	 * <p>
	 * The {@code id} element conveys a permanent, universally unique identifier for an entry or feed.
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
	 * The name of the channel or feed
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * Collection of all links defined at the feed or channel level.
	 * </p>
	 * 
	 * <p>
	 * Required in RSS specification. Listed in Atom specification as element {@code link}.
	 * </p>
	 * 
	 * @return a {@link Set}, never {@code null}
	 */
	public Set<Link> getLinks() {
		return links;
	}

	public void addLink(Link link) {
		if (link != null) {
			this.links.add(link);
		}
	}

	/**
	 * Phrase or sentence describing the channel.
	 * 
	 * @return a {@link TypedString} or {@code null}
	 */
	public TypedString getDescription() {
		return description;
	}

	public void setDescription(TypedString description) {
		this.description = description;
	}

	/**
	 * An item may represent a "story" -- much like a story in a newspaper or magazine; if so its
	 * description is a synopsis of the story, and the link points to the full story. An item may also
	 * be complete in itself, if so, the description contains the text (entity-encoded HTML is allowed),
	 * and the link and title may be omitted. All elements of an item are optional, however at least one
	 * of title or description must be present.
	 * 
	 * @return {@link List} of {@link Item} instances, never {@code null}
	 */
	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		if (item != null) {
			this.items.add(item);
		}
	}

	/**
	 * The language the channel is written in.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		if (Strings.isBlank(language)) {
			this.language = null;
		} else {
			language = language.trim().toLowerCase(Locale.ENGLISH);
			if (language.length() > 2) {
				language = language.substring(0, 2);
			}
			if (RFC639.LANGUAGES.contains(language)) {
				this.language = language;
			} else {
				this.language = null;
			}
		}
	}

	/**
	 * Copyright notice for content in the channel.
	 * 
	 * @return {@link Copyright} instance or {@code null}
	 */
	public Copyright getCopyright() {
		return copyright;
	}

	public void setCopyright(Copyright copyright) {
		this.copyright = copyright;
	}

	/**
	 * Email address for person responsible for editorial content.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getManagingEditor() {
		return managingEditor;
	}

	public void setManagingEditor(String managingEditor) {
		this.managingEditor = managingEditor;
	}

	/**
	 * Email address for person responsible for technical issues relating to channel.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getWebMaster() {
		return webMaster;
	}

	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}

	/**
	 * The publication date for the content in the channel. For example, the New York Times publishes on
	 * a daily basis, the publication date flips once every 24 hours. That's when the pubDate of the
	 * channel changes. All date-times in RSS conform to the Date and Time Specification of RFC 822,
	 * with the exception that the year may be expressed with two characters or four characters (four
	 * preferred).
	 * 
	 * @return {@link ZonedDateTime} or {@code null}
	 */
	public ZonedDateTime getPubDate() {
		return pubDate;
	}

	public void setPubDate(ZonedDateTime pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * <p>
	 * The last time the content of the channel changed.
	 * </p>
	 * 
	 * <p>
	 * Optional in RSS specification. Listed in Atom specification as element {@code updated}.
	 * </p>
	 * 
	 * @return {@link ZonedDateTime} or {@code null}
	 */
	public ZonedDateTime getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(ZonedDateTime lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	/**
	 * Specify one or more categories that the channel belongs to. Follows the same rules as the
	 * {@link Item}-level category element.
	 * 
	 * @return a {@link Set} of {@link Category} instances, never {@code null}, never containing
	 *         {@code null}
	 */
	public Set<Category> getCategories() {
		return categories;
	}

	public void addCategory(Category category) {
		if (category != null) {
			this.categories.add(category);
		}
	}

	/**
	 * A {@link String} indicating the program used to generate the channel.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	/**
	 * A URL that points to the documentation for the format used in the RSS file. It's probably a
	 * pointer to this page. It's for people who might stumble across an RSS file on a Web server 25
	 * years from now and wonder what it is.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	/**
	 * Allows processes to register with a cloud to be notified of updates to the channel, implementing
	 * a lightweight publish-subscribe protocol for RSS feeds.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
	}

	/**
	 * <p>
	 * TTL stands for time to live. It's a number of minutes that indicates how long a channel can be
	 * cached before refreshing from the source.
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

	/**
	 * <p>
	 * Specifies a GIF, JPEG or PNG image that can be displayed with the channel.
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
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getTextInput() {
		return textInput;
	}

	public void setTextInput(String textInput) {
		this.textInput = textInput;
	}

	/**
	 * A hint for aggregators telling them which hours they can skip.
	 * 
	 * @return a {@link List}, never {@code null}
	 */
	public List<Integer> getSkipHours() {
		return skipHours;
	}

	public void addSkipHour(int skipHour) {
		this.skipHours.add(skipHour);
	}

	/**
	 * A hint for aggregators telling them which days they can skip.
	 * 
	 * @return a {@link List}, never {@code null}
	 */
	public List<String> getSkipDays() {
		return skipDays;
	}

	public void addSkipDay(String skipDay) {
		this.skipDays.add(skipDay);
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
	 * Not in RSS specification. Listed in Atom spec and iTunes spec.
	 * </p>
	 * 
	 * @return a {@link List} of {@link Person} instances, never {@code null}
	 */
	public List<Person> getAuthors() {
		return authors;
	}

	public void addAuthor(Person author) {
		this.authors.add(author);
	}

	/**
	 * <p>
	 * Information that can be used to contact the owner of the podcast for communication specifically
	 * about their podcast.
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

	/**
	 * @return a {@link List} of {@link Person} instances, never {@code null}
	 */
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
	 * @return a {@link Set}, never {@code null}
	 */
	public Set<String> getKeywords() {
		return this.keywords;
	}

	public void addKeyword(String keyword) {
		if (Strings.isNotBlank(keyword)) {
			this.keywords.add(keyword);
		}
	}

	public void addKeywords(Collection<String> keywords) {
		if (keywords != null) {
			keywords.forEach(this::addKeyword);
		}
	}

	/**
	 * <p>
	 * Update information for this feed.
	 * </p>
	 * 
	 * @return a {@link UpdateInfo} instance, or {@code null}
	 */
	public UpdateInfo getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(UpdateInfo updateInfo) {
		this.updateInfo = updateInfo;
	}

	public void setUpdatePeriod(String period) {
		if (updateInfo == null) {
			updateInfo = new UpdateInfo();
		}
		updateInfo.setPeriod(period);
	}

	public void setUpdateFrequency(Integer frequency) {
		if (updateInfo == null) {
			updateInfo = new UpdateInfo();
		}
		updateInfo.setFrequency(frequency);
	}

	public void setUpdateBase(ZonedDateTime base) {
		if (updateInfo == null) {
			updateInfo = new UpdateInfo();
		}
		updateInfo.setBase(base);
	}

	/**
	 * <p>
	 * The rating for this item.
	 * </p>
	 * <p>
	 * Combines optional elements from iTunes RSS, Media RSS and Google Play spec.
	 * </p>
	 * 
	 * @return a {@link Rating}, not {@code null}
	 */
	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		if (rating != null) {
			if (Strings.isNotBlank(rating.getExplicit())) {
				this.rating.setExplicit(rating.getExplicit());
			}
			if (Strings.isNotBlank(rating.getScheme())) {
				this.rating.setScheme(rating.getScheme());
			}
			if (Strings.isNotBlank(rating.getText())) {
				this.rating.setText(rating.getText());
			}
			if (Strings.isNotBlank(rating.getAdultContent())) {
				this.rating.setAdultContent(rating.getAdultContent());
			}
		}
	}

	/**
	 * <p>
	 * The geographic location for this feed.
	 * </p>
	 * <p>
	 * Specified in RawVoice namespace specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * <p>
	 * A {@link Map} containing any value present in the podcast XML that is not mapped to any of the
	 * other values present here.
	 * </p>
	 * <p>
	 * See {@link OtherValueKey} for a full listing of possible values in this map.
	 * </p>
	 * 
	 * @return a {@link Map}, not {@code null}
	 */
	public Map<OtherValueKey, String> getOtherValues() {
		return values;
	}

	public void addOtherValue(OtherValueKey key, String value) {
		// we only add if not blank
		if (Strings.isNotBlank(value)) {
			values.put(key, value.trim());
		}
	}

	/**
	 * <p>
	 * {@link GeoPoint} for this {@link Item}
	 * </p>
	 * 
	 * @return a {@link GeoPoint} or {@code null}
	 */
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public void setLatitude(BigDecimal latitude) {
		if (latitude != null) {
			if (this.geoPoint == null) {
				this.geoPoint = new GeoPoint();
			}
			this.geoPoint.setLatitude(latitude);
		} else if (this.geoPoint != null) {
			this.geoPoint.setLatitude(null);
		}
	}

	public void setLongitude(BigDecimal longitude) {
		if (longitude != null) {
			if (this.geoPoint == null) {
				this.geoPoint = new GeoPoint();
			}
			this.geoPoint.setLongitude(longitude);
		} else if (this.geoPoint != null) {
			this.geoPoint.setLongitude(null);
		}
	}

	public boolean getBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	/**
	 * A phrase or sentence summarizing the podcast, part of the iTunes RSS specification.
	 * 
	 * Use the {@code <itunes:summary>} tag to summarize your podcast for potential listeners. You can
	 * specify up to 4000 characters.
	 * 
	 * If you don’t specify a {@code <itunes:summary>} tag, Apple Podcasts uses the information in the
	 * {@code <description>} tag.
	 * 
	 * Note: You can view {@code <itunes:summary>} information in the Apple Podcasts page for your
	 * podcast. The information also appears in a separate window if a users clicks the Information icon
	 * (Information icon) in the Description column.
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p>
	 * {@link String} email for this {@link Feed}
	 * </p>
	 * 
	 * <p>
	 * Specified in Google Play spec
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public TypedString getBrowserFriendly() {
		return browserFriendly;
	}

	public void setBrowserFriendly(TypedString browserFriendly) {
		this.browserFriendly = browserFriendly;
	}

	/**
	 * Find and return the date of last change as {@link LocalDate}
	 * 
	 * @return {@link LocalDate} or {@code null}
	 */
	public LocalDate getLastLocalDate() {
		LocalDate localDate;
		if (lastBuildDate != null) {
			localDate = lastBuildDate.toLocalDate();
		} else if (pubDate != null) {
			localDate = pubDate.toLocalDate();
		} else {
			ZonedDateTime z = items.stream()
					.sorted(new Item.ItemComparator().reversed())
					.findFirst()
					.map(Item::getPubDate)
					.orElse(null);
			localDate = z == null ? null : z.toLocalDate();
		}
		return localDate;
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
