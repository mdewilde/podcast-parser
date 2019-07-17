/*
	Copyright 2019 Marceau Dewilde <m@ceau.be>
	
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
package be.ceau.podcastparser.filter;

import java.util.Objects;

import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.namespace.NamespaceFactory;

/**
 * {@link ElementFilter} instance that skips any element for a given {@link Namespace}
 */
public final class ExcludeNamespaceFilter implements ElementFilter {

	private final Namespace namespace;
	
	public ExcludeNamespaceFilter(String namespaceURI) {
		this(NamespaceFactory.getInstance(namespaceURI));
	}

	public ExcludeNamespaceFilter(Namespace namespace) {
		Objects.requireNonNull(namespace);
		this.namespace = namespace;
	}

	@Override
	public boolean skip(String namespaceURI, String localName) {
		return namespace.isMatch(namespaceURI);
	}

	@Override
	public int hashCode() {
		return 31 + namespace.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExcludeNamespaceFilter other = (ExcludeNamespaceFilter) obj;
		return namespace.equals(other.namespace);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("NamespaceFilter [")
				.append("namespace=")
				.append(namespace)
				.append("]")
				.toString();
	}

}
