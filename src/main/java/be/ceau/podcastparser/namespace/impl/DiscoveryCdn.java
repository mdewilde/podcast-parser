package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class DiscoveryCdn implements Namespace {

	private static final String NAME = "http://discoverydn.com/about";

	@Override
	public String getName() {
		return NAME;
	}

}
