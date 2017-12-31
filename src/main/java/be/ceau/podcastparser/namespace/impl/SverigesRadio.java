package be.ceau.podcastparser.namespace.impl;

import be.ceau.podcastparser.namespace.Namespace;

public class SverigesRadio implements Namespace {

	private static final String NAME = "http://www.sverigesradio.se/podrss";

	@Override
	public String getName() {
		return NAME;
	}

}
