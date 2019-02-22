package org.iag.utility.singleton;

import java.lang.ref.SoftReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A {@link LooseSingleton} the relies on {@link SoftReference}. This is a
 * thread-safe singleton that allows the garage-collector to remove the value
 * when memory space is running low and no other strong reference is present.
 * 
 * @author Isaac
 *
 * @param <V>
 */
public class SoftSingleton< V >
						  extends
						  LooseSingleton<V> {

	/**
	 * Makes a {@link SoftSingleton} that calls the argument when-ever the
	 * previous value is collected and a new value is needed.
	 * 
	 * @param source
	 */
	public SoftSingleton( @NonNull Supplier<? extends V> source ){
		super( source );
	}

	/**
	 * Makes a {@link SoftSingleton} that calls the {@link Supplier} argument
	 * when-ever a new value is needed. Either when the garage-collector
	 * collects, or when the {@link Predicate} argument is {@code true}
	 * 
	 * @param source
	 *            the source for new value after the previous has been removed
	 * @param replaceWhen
	 *            an alternate case to replace the value
	 * 
	 */
	public SoftSingleton( @NonNull Supplier<? extends V> source,
						  @NonNull Predicate<? super V> replaceWhen ){
		super( source,
			   replaceWhen );
	}

	/**
	 * Makes a {@link SoftSingleton} that calls the {@link Supplier} argument
	 * when the {@link Predicate} argument is {@code true}
	 * 
	 * @param source
	 *            the source for new value after the previous has been removed
	 * @param replaceWhen
	 *            an alternate case to replace the value
	 * @param predicateNullSafe
	 *            if replaceWhen is {@code null} safe
	 */
	public SoftSingleton( @NonNull Supplier<? extends V> source,
						  @NonNull Predicate<? super V> replaceWhen,
						  boolean predicateNullSafe ){
		super( source,
			   replaceWhen,
			   predicateNullSafe );
	}

	/**
	 * Wraps the argument into a {@link SoftReference}
	 * 
	 * @see org.iag.utility.singleton.LooseSingleton#makeReference(java.lang.Object)
	 * @param value
	 * @return a {@code new} {@link SoftReference} whose value it the argument.
	 */
	@Override
	protected final @NonNull SoftReference<V>
			  makeReference( @Nullable V value ){
		return new SoftReference<>( value );
	}

	public @NonNull String
		   toString(){
		return super.toString();
	}

	public int
		   hashCode(){
		return super.hashCode();
	}

	public boolean
		   equals( @Nullable Object o ){
		return super.equals( o );
	}
}
