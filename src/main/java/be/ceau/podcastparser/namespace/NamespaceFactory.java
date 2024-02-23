/*
	
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

import be.ceau.podcastparser.namespace.custom.impl.ARD;
import be.ceau.podcastparser.namespace.custom.impl.AtomPublishing;
import be.ceau.podcastparser.namespace.custom.impl.AtomThreading;
import be.ceau.podcastparser.namespace.custom.impl.BBC;
import be.ceau.podcastparser.namespace.custom.impl.Blip;
import be.ceau.podcastparser.namespace.custom.impl.Blogger;
import be.ceau.podcastparser.namespace.custom.impl.BnrNl;
import be.ceau.podcastparser.namespace.custom.impl.Category5;
import be.ceau.podcastparser.namespace.custom.impl.Channel9;
import be.ceau.podcastparser.namespace.custom.impl.Content;
import be.ceau.podcastparser.namespace.custom.impl.CreativeCommons;
import be.ceau.podcastparser.namespace.custom.impl.DiscoveryCdn;
import be.ceau.podcastparser.namespace.custom.impl.DublinCore;
import be.ceau.podcastparser.namespace.custom.impl.DublinCoreTerms;
import be.ceau.podcastparser.namespace.custom.impl.Feedburner;
import be.ceau.podcastparser.namespace.custom.impl.Geo;
import be.ceau.podcastparser.namespace.custom.impl.GeoRSS;
import be.ceau.podcastparser.namespace.custom.impl.GoogleData;
import be.ceau.podcastparser.namespace.custom.impl.GooglePlay;
import be.ceau.podcastparser.namespace.custom.impl.ITunes;
import be.ceau.podcastparser.namespace.custom.impl.Iono;
import be.ceau.podcastparser.namespace.custom.impl.ItunesU;
import be.ceau.podcastparser.namespace.custom.impl.Longtailvideo;
import be.ceau.podcastparser.namespace.custom.impl.Media;
import be.ceau.podcastparser.namespace.custom.impl.Mlb;
import be.ceau.podcastparser.namespace.custom.impl.OpenSearch;
import be.ceau.podcastparser.namespace.custom.impl.Oupod;
import be.ceau.podcastparser.namespace.custom.impl.PodfmRu;
import be.ceau.podcastparser.namespace.custom.impl.PromoDeejay;
import be.ceau.podcastparser.namespace.custom.impl.RTE;
import be.ceau.podcastparser.namespace.custom.impl.RadioFrance;
import be.ceau.podcastparser.namespace.custom.impl.RawVoice;
import be.ceau.podcastparser.namespace.custom.impl.ResourceDescriptionFramework;
import be.ceau.podcastparser.namespace.custom.impl.SimpleChapters;
import be.ceau.podcastparser.namespace.custom.impl.Slash;
import be.ceau.podcastparser.namespace.custom.impl.SverigesRadio;
import be.ceau.podcastparser.namespace.custom.impl.Syndication;
import be.ceau.podcastparser.namespace.custom.impl.UserlandCreativeCommons;
import be.ceau.podcastparser.namespace.custom.impl.WellFormedWeb;
import be.ceau.podcastparser.namespace.custom.impl.Welt;
import be.ceau.podcastparser.namespace.custom.impl.Wordpress;
import be.ceau.podcastparser.namespace.custom.impl.YahooVideo;
import be.ceau.podcastparser.namespace.root.impl.Atom;
import be.ceau.podcastparser.namespace.root.impl.RSS;
import be.ceau.podcastparser.util.Strings;

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
		add(map, new Category5());
		add(map, new Channel9());
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
		add(map, new Mlb());
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
	 * Static factory method that returns the appropriate {@code Namespace}
	 * implementation for the given uri
	 * 
	 * @param uri
	 *            a uri, not {@code null}
	 * @return a {@code Namespace} implementation, or {@code null}
	 */
	public static Namespace getInstance(String uri) {
		if (Strings.isBlank(uri)) {
			return null;
		}

		Namespace namespace = NAMESPACES.get(uri);
		
		if (namespace == null) {
			namespace = NAMESPACES.get(uri.toLowerCase(Locale.ENGLISH).trim());
		}	
		
		return namespace;
	}

	/**
	 * Static factory method that returns an instance of the {@code Namespace} with the given canoncial
	 * class name implementation for the given uri
	 * 
	 * @param canonicalName
	 *            a {@link String}, not {@code null}
	 * @return a {@code Namespace} implementation, or {@code null}
	 */
	public static Namespace getByCanonicalName(String canonicalName) {
		if (Strings.isBlank(canonicalName)) {
			return null;
		}
		return NAMESPACES.get(canonicalName);
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
