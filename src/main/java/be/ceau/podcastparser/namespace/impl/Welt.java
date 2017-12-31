package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class Welt implements Namespace {

	private static final String NAME = "https://www.welt.de/spec";

	@Override
	public String getName() {
		return NAME;
	}

}
