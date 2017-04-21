package be.ceau.podcastparser.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * Helper class for fluent creation of unmodifiable {@link Set} instances.
 * </p>
 * 
 * @param <T>
 */
public class UnmodifiableSet<T> extends HashSet<T> {

	private static final long serialVersionUID = 1492194402964L;

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
