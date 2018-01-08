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
package be.ceau.podcastparser.filter;

import java.util.Objects;

import be.ceau.podcastparser.namespace.Namespace;
import be.ceau.podcastparser.namespace.NamespaceFactory;
import be.ceau.podcastparser.util.Strings;

/**
 * {@link ElementFilter} instance that skips a specific element in a specific {@link Namespace}
 */
public final class ExcludeElementFilter implements ElementFilter {

	private final Namespace namespace;
	private final String localName;

	public ExcludeElementFilter(String namespaceURI, String localName) {
		this(NamespaceFactory.getInstance(namespaceURI), localName);
	}

	public ExcludeElementFilter(Namespace namespace, String localName) {
		Objects.requireNonNull(namespace);
		Strings.requireNonBlank(localName);
		this.namespace = namespace;
		this.localName = localName;
	}

	@Override
	public boolean skip(String namespaceURI, String localName) {
		return namespace.isMatch(namespaceURI) && this.localName.equals(localName);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localName == null) ? 0 : localName.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExcludeElementFilter other = (ExcludeElementFilter) obj;
		if (localName == null) {
			if (other.localName != null)
				return false;
		} else if (!localName.equals(other.localName))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("SingleElementFilter [")
				.append("namespace=")
				.append(namespace)
				.append(", ")
				.append("localName=")
				.append(localName)
				.append("]")
				.toString();
	}

}
