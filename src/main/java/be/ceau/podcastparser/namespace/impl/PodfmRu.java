package be.ceau.podcastparser.namespace.impl;

import javax.xml.stream.XMLStreamException;

import be.ceau.podcastparser.PodParseContext;
import be.ceau.podcastparser.models.Item;
import be.ceau.podcastparser.models.OtherValueKey;
import be.ceau.podcastparser.namespace.Namespace;

public class PodfmRu implements Namespace {

	private static final String NAME = "http://podfm.ru/RSS/extension";

	@Override
	public String getName() {
		return NAME;
	}

}
