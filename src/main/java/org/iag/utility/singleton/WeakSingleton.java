package org.iag.utility.singleton;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A {@link LoosSingleton} that relies on {@link WeakReference}. This is a
 * thread-safe singleton that allows the garage-collector to remove the value
 * when no other reference to this value is present.
 * 
 * @author Isaac
 *
 * @param <V>
 */
public class WeakSingleton< V >
						  extends
						  LooseSingleton<V> {

	/**
	 * Makes a {@link WeakSingleton} that calls the argument when-ever the
	 * previous value is collected and a new value is needed.
	 * 
	 * @param source
	 */
	public WeakSingleton( @NonNull Supplier<? extends V> source ){
		super( source );
	}

	/**
	 * Makes a {@link WeakSingleton} that calls the {@link Supplier} argument
	 * when-ever a new value is needed. Either when the garage-collector
	 * collects, or when the {@link Predicate} argument is {@code true}
	 * 
	 * @param source
	 *            the source for new value after the previous has been removed
	 * @param replaceWhen
	 *            an alternate case to replace the value
	 * 
	 */
	public WeakSingleton( @NonNull Supplier<? extends V> source,
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
	public WeakSingleton( @NonNull Supplier<? extends V> source,
						  @NonNull Predicate<? super V> replaceWhen,
						  boolean predicateNullSafe ){
		super( source,
			   replaceWhen,
			   predicateNullSafe );
	}

	/**
	 * Wraps the argument into a {@link WeakReference}
	 * 
	 * @see org.iag.utility.singleton.LooseSingleton#makeReference(java.lang.Object)
	 * @param value
	 * @return a {@code new} {@link WeakReference} whose value it the argument.
	 */
	@Override
	protected final @NonNull WeakReference<V>
			  makeReference( @Nullable V value ){
		return new WeakReference<>( value );
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
