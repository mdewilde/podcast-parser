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
package be.ceau.podcastparser.models;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ceau.podcastparser.util.Strings;

public class Item {

	private TypedString title;
	private String subtitle;
	private final List<Link> links = new ArrayList<>();
	private TypedString description;
	private final List<Person> authors = new ArrayList<>();
	private final List<Category> categories = new ArrayList<>();
	private String copyright;
	private Enclosure enclosure;
	private String guid;
	private Temporal pubDate;
	private Temporal updated;
	private Temporal validity;
	private Temporal edited;
	private String source;
	private final Rating rating = new Rating();
	private Duration duration;
	private final List<MediaContent> mediaContents = new ArrayList<>();
	private final List<Image> images = new ArrayList<>();
	private final List<String> keywords = new ArrayList<>();
	private String content;
	private String subject;
	private Comments comments;
	private final List<Chapter> chapters = new ArrayList<>();
	private final List<Hash> hashes = new ArrayList<>();
	private License license;
	private final List<Credit> credits = new ArrayList<>();
	private String language;
	private final Map<OtherValueKey, String> values = new EnumMap<>(OtherValueKey.class);
	private final Map<String, Enclosure> otherEnclosures = new HashMap<>();
	private GeoPoint geoPoint;
	private int order;
	private String summary;
	private boolean block;
	private String episodeType;
	private String episode;
	private String season;
	private MediaPlayer mediaPlayer;
	private final List<Scene> scenes = new ArrayList<>();
	private final List<Transcript> transcripts = new ArrayList<>();
	private boolean hd;
	private TypedString embed;
	private final List<Metamark> metamarks = new ArrayList<>();

	// TODO -> merge with copyright?
	private Copyright mediaCopyright;

	/**
	 * <p>
	 * The title of the item.
	 * </p>
	 * <p>
	 * Optional as per RSS specification.
	 * </p>
	 * 
	 * @return {@code TypedString} or {@code null}
	 */
	public TypedString getTitle() {
		return title;
	}

	public void setTitle(String title) {
		TypedString typedString = new TypedString();
		typedString.setText(title);
		setTitle(typedString);
	}

	public void setTitle(TypedString title) {
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
	 * Required in RSS specification. Listed in Atom specification as element {@code link}.
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
	 * <p>
	 * The item synopsis.
	 * </p>
	 * <p>
	 * Part of the iTunes specification.
	 * </p>
	 * 
	 * @return a {@code TypedString} or {@code null}
	 */
	public TypedString getDescription() {
		return description;
	}

	public void setDescription(String description) {
		TypedString typedString = new TypedString();
		typedString.setText(description);
		setDescription(typedString);
	}

	public void setDescription(TypedString description) {
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
	 * The {@code rights} element is a Text construct that conveys information about rights held in and over an entry or
	 * feed.
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

	/**list
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
	 * guid stands for globally unique identifier. It's a string that uniquely identifies the item. When present, an
	 * aggregator may choose to use this string to determine if an item is new.
	 * 
	 * <guid>http://some.server.com/weblogItem3207</guid>
	 * 
	 * There are no rules for the syntax of a guid. Aggregators must view them as a string. It's up to the source of the
	 * feed to establish the uniqueness of the string.
	 * 
	 * If the guid element has an attribute named "isPermaLink" with a value of true, the reader may assume that it is a
	 * permalink to the item, that is, a url that can be opened in a Web browser, that points to the full item described by
	 * the <item> element. An example:
	 * 
	 * <guid isPermaLink="true">http://inessential.com/2002/09/01.php#a2</guid>
	 * 
	 * isPermaLink is optional, its default value is true. If its value is false, the guid may not be assumed to be a url,
	 * or a url to anything in particular.
	 * </p>
	 * <p>
	 * Optional for {@code item} elements in RSS specification. Required item {@code id} as per Atom specification.
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
	 * Optional for {@code entry} elements in Atom specification. Not part of RSS specification.
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
	 * The {@code updated} element is a Date construct indicating the most recent instant in time when an entry or feed was
	 * modified in a way the publisher considers significant.
	 * </p>
	 * <p>
	 * Optional for {@code item} elements in RSS specification. Optional for {@code entry} elements in Atom specification.
	 * </p>
	 * 
	 * @return a {@link TeepisodeTypemporal} or {@code null}
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
	 * Date when an item was edited.
	 * </p>
	 * <p>
	 * Specified in Atom Publishing namespace specification.
	 * </p>
	 * 
	 * @return a {@link Temporal} or {@code null}
	 */
	public Temporal getEdited() {
		return edited;
	}

	public void setEdited(Temporal edited) {
		this.edited = edited;
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
	 * Combines optional elements from iTunes RSS, Media RSS and Google Play spec.
	 * </p>
	 * 
	 * @return a {@link Rating}, never {@code null}
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
	 *            {@link String} formatted HH:MM:SS, H:MM:SS, MM:SS, or M:SS (H = hours, M = minutes, S = seconds). If an
	 *            integer is provided (no colon present), the value is assumed to be in seconds. If one colon is present,
	 *            the number to the left is assumed to be minutes, and the number to the right is assumed to be seconds. If
	 *            more than two colons are present, the numbers furthest to the right are ignored.
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
	 * A more expressive alternative to {@link #getEnclosure()}. Not always present.
	 * </p>
	 * <p>
	 * Part of the Media RSS specification. There are no limitations on the number of {@link content} elements per
	 * {@code item}, though it is recommended to have only one.
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
		return this.keywords;
	}

	public void addKeyword(String keyword) {
		if (Strings.isNotBlank(keyword)) {
			this.keywords.add(keyword.trim());
		}
	}

	public void addKeywords(Collection<String> keywords) {
		if (keywords != null) {
			keywords.forEach(this::addKeyword);
		}
	}

	/**
	 * <p>list
	 * Not in RSS specification. Part of Content namespace.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * <p>
	 * Not in RSS specification. Part of DublinCore namespace.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * <p>
	 * All information regarding comments for this {@link Item}
	 * </p>
	 * 
	 * @return a {@link Comments} instance, or {@code null}
	 */
	public Comments getComments() {
		return comments;
	}

	/**
	 * @return a {@link Comments} instance, never {@code null}
	 */
	public Comments computeCommentsIfAbsent() {
		if (comments == null) {
			comments = new Comments();
		}
		return comments;
	}

	public void setComments(Comments comments) {
		this.comments = comments;
	}

	/**
	 * <p>
	 * Not in RSS specification. Part of SimpleChapters namespace specifcation.
	 * </p>
	 * 
	 * @return a {@link List}, not {@code null}
	 */
	public List<Chapter> getChapters() {
		return chapters;
	}

	public void addChapter(Chapter chapter) {
		chapters.add(chapter);
	}

	/**
	 * <p>
	 * Part of Media RSS namespace specification.
	 * </p>
	 * 
	 * @return a {@link List}, not {@code null}
	 */
	public List<Hash> getHashes() {
		return hashes;
	}

	public void addHash(Hash hash) {
		hashes.add(hash);
	}

	/**
	 * <p>
	 * License information for this {@link Item}
	 * </p>
	 * 
	 * @return a {@link License} or {@code null}
	 */
	public License getLicense() {
		return license;
	}

	public License computeLicenseIfAbsent() {
		if (license == null) {
			license = new License();
		}
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	/**
	 * <p>
	 * Credits for this {@link Item}
	 * </p>
	 * 
	 * @return a {@link List}, not {@code null}
	 */
	public List<Credit> getCredits() {
		return credits;
	}

	public void addCredit(Credit credit) {
		credits.add(credit);
	}

	/**
	 * <p>
	 * Language of this {@link Item}
	 * </p>
	 * <p>
	 * Part of the DublinCore namespace specification.
	 * </p>
	 * 
	 * @return a {@link String} or {@code null}
	 */
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * <p>
	 * A {@link Map} containing any value present in the podcast XML that is not mapped to any of the other values present
	 * here.
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
	 * A {@link Map} containing any alternative {@link Enclosure} in the podcast XML that is not the main enclosure.
	 * </p>
	 * setFull
	 * 
	 * @return a {@link Map}, not {@code null}
	 */
	public Map<String, Enclosure> getOtherEnclosures() {
		return otherEnclosures;
	}

	public void addOtherEnclosure(String key, Enclosure enclosure) {
		// we only add if not null
		if (key != null && enclosure != null) {
			otherEnclosures.put(key, enclosure);
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

	public GeoPoint computeGeoPointIfAbsent() {
		if (geoPoint == null) {
			geoPoint = new GeoPoint();
		}
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public boolean getBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	/**
	 * Whether this is a full episode, a trailer or a preview.
	 * 
	 * @return {@link String} or {@code null}
	 */
	public String getEpisodeType() {
		return episodeType;
	}

	public void setEpisodeType(String episodeType) {
		this.episodeType = episodeType;
	}

	public String getEpisode() {
		return episode;
	}

	public void setEpisode(String episode) {
		this.episode = episode;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public Copyright getMediaCopyright() {
		return mediaCopyright;
	}

	public void setMediaCopyright(Copyright mediaCopyright) {
		this.mediaCopyright = mediaCopyright;
	}

	public List<Scene> getScenes() {
		return scenes;
	}

	public void addScene(Scene scene) {
		// we only add if not null
		if (scene != null) {
			scenes.add(scene);
		}
	}

	public List<Transcript> getTranscripts() {
		return transcripts;
	}

	public void addTranscript(Transcript transcript) {
		// we only add if not null
		if (transcript != null) {
			transcripts.add(transcript);
		}
	}

	/**
	 * As specified in the RawVoice namespace
	 * 
	 * @return {@code true} if the video media specified in the child {@code <enclosure>} is in High
	 *         Definition (ie. any video that is widescreen (16:9 aspect ratio) with a 720p, 720i, 1080p
	 *         or 1080i resolution, or better)
	 */
	public boolean isHd() {
		return hd;
	}

	public void setHd(boolean hd) {
		this.hd = hd;
	}

	/**
	 * As specified in the RawVoice namespace
	 * 
	 * @return {@code TypedString} block of embed HTML markup that corresponds to the item’s media
	 *         content, or {@code null} if not part of the feed
	 */
	public TypedString getEmbed() {
		return embed;
	}

	public void setEmbed(TypedString embed) {
		this.embed = embed;
	}

	public List<Metamark> getMetamarks() {
		return metamarks;
	}

	public void addMetamark(Metamark metamark) {
		// we only add if not null
		if (metamark != null) {
			metamarks.add(metamark);
		}
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
