package be.ceau.podcastparser.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UnmodifiableSet<T> extends HashSet<T> {

	private static final long serialVersionUID = 1492194402964L;

	public static <T> Set<T> of(T t) {
		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(t)));
	}
	
	public static <T> Set<T> of(T t1, T t2) {
		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(t1, t2)));
	}

	public static <T> Set<T> of(T t1, T t2, T t3) {
		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(t1, t2, t3)));
	}

	public static <T> Set<T> of(T t1, T t2, T t3, T t4) {
		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(t1, t2, t3, t4)));
	}

	@SafeVarargs
	public static <T> Set<T> of(T... s) {
		return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(s)));
	}

}
