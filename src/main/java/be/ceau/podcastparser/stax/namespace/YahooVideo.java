package be.ceau.podcastparser.stax.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

public class YahooVideo implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("http://video.yahooapis.com/v2/video/");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}

/*
	corpus stats

	  6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=value attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adInfo attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adTargeting attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=id attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=adData attributes=[]]
      6281 	--> http://video.yahooapis.com/v2/video/ level=item localName=name attributes=[]]

*/