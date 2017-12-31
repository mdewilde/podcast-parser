/*
	Copyright 2018 Marceau Dewilde <m@ceau.be>
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		https://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package be.ceau.podcastparser.namespace;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import be.ceau.podcastparser.namespace.impl.ARD;
import be.ceau.podcastparser.namespace.impl.Atom;
import be.ceau.podcastparser.namespace.impl.AtomPublishing;
import be.ceau.podcastparser.namespace.impl.AtomThreading;
import be.ceau.podcastparser.namespace.impl.BBC;
import be.ceau.podcastparser.namespace.impl.Blip;
import be.ceau.podcastparser.namespace.impl.Blogger;
import be.ceau.podcastparser.namespace.impl.BnrNl;
import be.ceau.podcastparser.namespace.impl.Content;
import be.ceau.podcastparser.namespace.impl.CreativeCommons;
import be.ceau.podcastparser.namespace.impl.DiscoveryCdn;
import be.ceau.podcastparser.namespace.impl.DublinCore;
import be.ceau.podcastparser.namespace.impl.DublinCoreTerms;
import be.ceau.podcastparser.namespace.impl.Feedburner;
import be.ceau.podcastparser.namespace.impl.Geo;
import be.ceau.podcastparser.namespace.impl.GeoRSS;
import be.ceau.podcastparser.namespace.impl.GoogleData;
import be.ceau.podcastparser.namespace.impl.GooglePlay;
import be.ceau.podcastparser.namespace.impl.ITunes;
import be.ceau.podcastparser.namespace.impl.Iono;
import be.ceau.podcastparser.namespace.impl.ItunesU;
import be.ceau.podcastparser.namespace.impl.Longtailvideo;
import be.ceau.podcastparser.namespace.impl.Media;
import be.ceau.podcastparser.namespace.impl.OpenSearch;
import be.ceau.podcastparser.namespace.impl.Oupod;
import be.ceau.podcastparser.namespace.impl.PodfmRu;
import be.ceau.podcastparser.namespace.impl.PromoDeejay;
import be.ceau.podcastparser.namespace.impl.RSS;
import be.ceau.podcastparser.namespace.impl.RTE;
import be.ceau.podcastparser.namespace.impl.RadioFrance;
import be.ceau.podcastparser.namespace.impl.RawVoice;
import be.ceau.podcastparser.namespace.impl.ResourceDescriptionFramework;
import be.ceau.podcastparser.namespace.impl.SimpleChapters;
import be.ceau.podcastparser.namespace.impl.Slash;
import be.ceau.podcastparser.namespace.impl.SverigesRadio;
import be.ceau.podcastparser.namespace.impl.Syndication;
import be.ceau.podcastparser.namespace.impl.UserlandCreativeCommons;
import be.ceau.podcastparser.namespace.impl.WellFormedWeb;
import be.ceau.podcastparser.namespace.impl.Welt;
import be.ceau.podcastparser.namespace.impl.Wordpress;
import be.ceau.podcastparser.namespace.impl.YahooVideo;

public class NamespaceFactory {

	private static final Map<String, Namespace> NAMESPACES;
	
	private static void add(Map<String, Namespace> map, Namespace namespace) {
		{ // TODO tighten
			Namespace previous = map.put(namespace.getName(), namespace);
			if (previous != null) {
				String message = new StringBuilder("names can be mapped to one namespace only but ")
						.append(namespace.getName())
						.append(" is associated with both ")
						.append(namespace.getClass().getCanonicalName())
						.append(" and with ")
						.append(previous.getClass().getCanonicalName())
						.toString();
				throw new IllegalStateException(message);
			}
		}
		namespace.getAlternativeNames()
			.forEach(name -> {
				Namespace previous = map.put(name, namespace);
				if (previous != null) {
					String message = new StringBuilder("names can be mapped to one namespace only but ")
							.append(name)
							.append(" is associated with both ")
							.append(namespace.getClass().getCanonicalName())
							.append(" and with ")
							.append(previous.getClass().getCanonicalName())
							.toString();
					throw new IllegalStateException(message);
				}
			});
		// also put with fully qualified name for specific programmatic lookup
		map.put(namespace.getClass().getCanonicalName(), namespace);
	}
	
	static {
		
		Map<String, Namespace> map = new HashMap<>();
		add(map, new ARD());
		add(map, Atom.instance());
		add(map, new AtomPublishing());
		add(map, new AtomThreading());
		add(map, new BBC());
		add(map, new Blip());
		add(map, new Blogger());
		add(map, new BnrNl());
		add(map, new Content());
		add(map, new CreativeCommons());
		add(map, new DiscoveryCdn());
		add(map, new DublinCore());
		add(map, new DublinCoreTerms());
		add(map, new Feedburner());
		add(map, new Geo());
		add(map, new GeoRSS());
		add(map, new GoogleData());
		add(map, new GooglePlay());
		add(map, new Iono());
		add(map, new ITunes());
		add(map, new ItunesU());
		add(map, new Longtailvideo());
		add(map, new Media());
		add(map, new RadioFrance());
		add(map, new OpenSearch());
		add(map, new Oupod());
		add(map, new PodfmRu());
		add(map, new PromoDeejay());
		add(map, new RawVoice());
		add(map, new ResourceDescriptionFramework());
		add(map, RSS.instance());
		add(map, new RTE());
		add(map, new SimpleChapters());
		add(map, new Slash());
		add(map, new SverigesRadio());
		add(map, new Syndication());
		add(map, new UserlandCreativeCommons());
		add(map, new WellFormedWeb());
		add(map, new Welt());
		add(map, new Wordpress());
		add(map, new YahooVideo());
		NAMESPACES = Collections.unmodifiableMap(map);
	}
	
	/**
	 * Static factory method that instantiates an appropriate {@code Namespace}
	 * implementation for the given prefix and url
	 * 
	 * @param uri
	 *            a uri, not {@code null}
	 * @return a {@code Namespace} implementation, or {@code null}
	 */
	public static Namespace getInstance(String uri) {

		if (uri == null) {
			return null;
		}
		
		Namespace namespace = NAMESPACES.get(uri);
		
		if (namespace == null) {
			namespace = NAMESPACES.get(uri.toLowerCase(Locale.ENGLISH).trim());
		}	
		
		return namespace;
	}

	/**
	 * @return collection with all {@link Namespace} instances in this factory
	 */
	public static Collection<Namespace> getNamespaces() {
		return NAMESPACES.values();
	}
	
	public static Namespace getRSS() {
		return NAMESPACES.get(RSS.class.getCanonicalName());
	}
	
	public static Namespace getAtom() {
		return NAMESPACES.get(Atom.class.getCanonicalName());
	}

	public static boolean isHTMLNamespace(String uri) {
		return "http://www.w3.org/1999/xhtml".equals(uri);
	}

}
