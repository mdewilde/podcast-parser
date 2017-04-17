package be.ceau.podcastparser.stax.namespace;

import java.util.Set;

import be.ceau.podcastparser.util.UnmodifiableSet;

public class Wordpress implements Namespace {

	public static final Set<String> NAMES = UnmodifiableSet.of("com-wordpress:feed-additions:1");

	@Override
	public Set<String> getNames() {
		return NAMES;
	}

}
