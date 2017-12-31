package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class Longtailvideo implements Namespace {

	private static final String NAME = "http://developer.longtailvideo.com/";

	@Override
	public String getName() {
		return NAME;
	}

}
