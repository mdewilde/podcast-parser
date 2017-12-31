package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class Oupod implements Namespace {

	private static final String NAME = "http://purl.org/net/oupod/";

	@Override
	public String getName() {
		return NAME;
	}

}
