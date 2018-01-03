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
package be.ceau.podcastparser.models.support;

import java.io.Serializable;
import java.util.Objects;

import be.ceau.podcastparser.namespace.Namespace;

/**
 * <p>
 * Instances represent an element that should be ignored during feed XML parsing.
 * </p>
 * <p>
 * Instances are immutable and threadsafe.
 * </p>
 */
public final class SkippableElement implements Serializable {

	private static final long serialVersionUID = 1514316457096L;

	private final String namespace;
	private final String localName;

	public SkippableElement(String localName) {
		this(null, localName);
	}

	public SkippableElement(Class<? extends Namespace> namespace, String localName) {
		Objects.requireNonNull(localName);
		this.namespace = namespace == null ? "" : namespace.getCanonicalName();
		this.localName = localName;
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
		SkippableElement other = (SkippableElement) obj;
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

}
