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
