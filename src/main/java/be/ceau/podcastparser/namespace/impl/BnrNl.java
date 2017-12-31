package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class BnrNl implements Namespace {

	private static final String NAME = "http://www.bnr.nl/rss/podcast";

	@Override
	public String getName() {
		return NAME;
	}

}
