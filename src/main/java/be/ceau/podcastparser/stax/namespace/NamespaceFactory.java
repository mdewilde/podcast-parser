/*
	Copyright 2017 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.stax.namespace;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NamespaceFactory {

	private static final Map<String, Namespace> NAMESPACES;
	
	private static void add(Map<String, Namespace> map, Namespace namespace) {
		namespace.getNames()
			.forEach(name -> {
				Namespace previous = map.put(name, namespace);
				if (previous != null) {
					String message = new StringBuilder()
							.append("names can be mapped to one namespace only - ")
							.append(name)
							.append(" is associated with ")
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
		add(map, Atom.instance());
		add(map, new AtomPublishing());
		add(map, new AtomThreading());
		add(map, new Blip());
		add(map, new Blogger());
		add(map, new Content());
		add(map, new CreativeCommons());
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
		add(map, new Media());
		add(map, new OpenSearch());
		add(map, new RawVoice());
		add(map, new ResourceDescriptionFramework());
		add(map, RSS.instance());
		add(map, new RTE());
		add(map, new SimpleChapters());
		add(map, new Slash());
		add(map, new Syndication());
		add(map, new UserlandCreativeCommons());
		add(map, new WellFormedWeb());
		add(map, new Wordpress());
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