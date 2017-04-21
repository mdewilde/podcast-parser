package be.ceau.podcastparser.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

/**
 * 
 * 
 * @see http://support.iono.fm/knowledgebase/articles/559560-iono-fm-rss-namespace
 */
public class Iono implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://iono.fm/rss-namespace-1.0");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}
/*

	corpus stats

      8621 	--> http://iono.fm/rss-namespace-1.0 level=item localName=thumbnail attributes=[href]]
       111 	--> http://iono.fm/rss-namespace-1.0 level=feed localName=thumbnail attributes=[href]]

*/
/*
	Image thumbnails
	
	<ionofm:thumbnail href="http://url.to/thumbnail.png">
	
	The thumbnail element provides a link to a small thumbnail of the related image. This applies to both the RSS <channel> element and it's child <item> elements. It's href attribute will contain a URL pointing to a 128x128 or 64x64 PNG image.
	
	
	Object live-stream
	
	<ionofm:stream>
	
	The stream element provides links to any live-streams associated with a provider or a channel. It is valid within the RSS <channel> element or within <item> elements.
	
	Note live-stream URL's may change due to ongoing infrastructure changes, for switching to a redundant stream or other requirements. Client applications should refresh their URLs when retrieving an updated provider RSS feed.
	
	Child elements of this field is the list of available live-stream formats. Currently supported are:
	
	    streamname : the name of the live stream, to be used for retrieving other stream related metadata. 
	
	    hls-aac : HLS stream containing AAC encoded audio.
	
	    ice-mp3 : Icecast stream containing MP3 encoded audio.
	
	    ice-aac : Icecast stream containing AAC encoded audio.
	
	
	Each format element can specify 1 to 4 URL's for the low, medium, high and hifi quality version of the stream. Note that not all streams might not support all quality levels! Example:
	
	<ionofm:stream>
	      <ionofm:streamname>stream_identifier</ionofm:streamname>
	      <ionofm:hls_aac>
	             <ionofm:low src="http://location.to.low.m3u8"/>
	             <ionofm:medium src="http://location.to.medium.m3u8"/>
	             <ionofm:high src="http://location.to.high.m3u8"/>
	      </ionofm:hls_aac>
	      <ionofm:ice_aac>
	              <ionofm:low src="http://location.to.low.aac"/>
	              <ionofm:medium src="http://location.to.medium.aac"/>
	              <ionofm:high src="http://location.to.high.aac"/>
	      </ionofm:ice_aac>
	      <ionofm:ice_mp3>
	             <ionofm:low src="http://location.to.low.mp3"/>
	             <ionofm:medium src="http://location.to.medium.mp3"/>
	             <ionofm:high src="http://location.to.high.mp3"/>
	      </ionofm:ice_mp3>
	</ionofm:hls>
	
	
	Show end-time
	
	<ionofm:endDate>Sun, 19 May 2002 15:21:00 GMT</ionofm:endDate>
	
	This is related to the duration of an item and is used for mainly in the Stream metadata RSS to indicate the running time of upcoming items. This complies to the same RFC 822 value that is used for the standard RSS <pubDate> field.
	
	
	Currently playing metadata
	
	<ionofm:metadata_url>https://edge.iono.fm/xmetadata/stream_name</ionofm:metadata_url>
	
	This is an optional field that is populated when the stream has currently playing metadata associated with it. See this article for information on how to interpret data from this URL.
	*/