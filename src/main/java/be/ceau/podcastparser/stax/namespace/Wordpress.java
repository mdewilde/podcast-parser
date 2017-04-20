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

/*
	corpus stats
	
    193028 	--> com-wordpress:feed-additions:1 level=item localName=post-id attributes=[]]
      5263 	--> com-wordpress:feed-additions:1 level=feed localName=site attributes=[]]

*/