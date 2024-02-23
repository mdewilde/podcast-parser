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
package be.ceau.podcastparser.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * Helper class for fluent creation of unmodifiable {@link Set} instances.
 * </p>
 * 
 * @param <T> the type of elements in the target set
 */
public class UnmodifiableSet<T> {

	public static <T> Set<T> of(T t) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(t)));
	}

	public static <T> Set<T> of(T t1, T t2) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(t1, t2)));
	}

	@SafeVarargs
	public static <T> Set<T> of(T... s) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(s)));
	}

	public static <T> Set<T> of(Collection<T> s) {
		return Collections.unmodifiableSet(new LinkedHashSet<>(s));
	}

}
