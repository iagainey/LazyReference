package org.iag.utility.singleton;

import java.lang.ref.Reference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * LooseSingleton's goal is to be a lazy container that will not generate the
 * object till needed, but allow the garage collector to possible remove the
 * object.
 * 
 * <p>
 * There are two notable parts of {@link LooseSingleton}.
 * <ul>
 * <li>The Double Wrap - a {@link Reference} (either {@link WeakReference} or
 * {@link SoftReference}) held by an {@link AtomicReference}. The
 * {@link Reference} holding the value.</li>
 * <li>The Source - using {@link Supplier} to fetch a {@code new} value after
 * the previous has been collected, or {@link LooseSingleton#set(Object)} is
 * passed {@code null}.</li>
 * </ul>
 * 
 * <p>
 * LooseSingleton is thread-safe, as the value is updated via
 * {@link AtomicReference}.
 * 
 * @author Isaac G.
 *
 * @param <V>
 */
public abstract class LooseSingleton< V > {

	@NonNull
	private static final Predicate<Object> isNull = Objects::isNull;

	@NonNull
	private final AtomicReference<Reference<V>> value = new AtomicReference<>();

	@NonNull
	private final Supplier<V> source;

	@NonNull

	private final Predicate<V> replaceWhen;

	/**
	 * Generates {@code this} to only call argument {@code valueSource} when the
	 * internal value is {@code null}.
	 * 
	 * @param valueSource
	 * @param valueWrapper
	 */
	protected LooseSingleton( @NonNull Supplier<? extends V> valueSource ){
		this( valueSource,
			  isNull,
			  true );
	}

	/**
	 * Generates {@code this} to only call argument {@code valueSource} when the
	 * internal value is {@code null} or when argument {@code replaceWhen} is
	 * true.
	 * 
	 * 
	 * @param valueSource
	 * @param valueWrapper
	 * @param replaceWhen
	 */
	protected LooseSingleton( @NonNull Supplier<? extends V> valueSource,
							  @NonNull Predicate<? super V> replaceWhen ){
		this( valueSource,
			  replaceWhen,
			  false );
	}

	/**
	 * Generates {@code this} to only call argument {@code valueSource} when the
	 * internal value is {@code null} or when argument {@code predicateNullSafe}
	 * is true.
	 * 
	 * @param valueSource
	 * @param valueWrapper
	 * @param replaceWhen
	 * @param predicateNullSafe
	 */
	@SuppressWarnings( "unchecked" )
	protected LooseSingleton( @NonNull Supplier<? extends V> valueSource,
							  @NonNull Predicate<? super V> replaceWhen,
							  boolean predicateNullSafe ){
		this.source = (Supplier<V>) Objects.requireNonNull( valueSource );
		final Predicate<V> solidCast = (Predicate<V>) Objects.requireNonNull( replaceWhen );
		this.replaceWhen = predicateNullSafe ? solidCast
											 : ((Predicate<V>) isNull).or( solidCast );
	}

	/**
	 * Is only (initially) call by the @see methods
	 * 
	 * @see #get()
	 * @see #set(Object)
	 * @see #unsafeSet(Object)
	 * 
	 * @param value
	 * @return {@code new} {@link Reference} that contains the argument.
	 */
	protected abstract @NonNull Reference<V>
			  makeReference( @Nullable V value );

	/**
	 * If the internal value is {@code null} (or is {@code true} when passed to
	 * {@link replaceWhen}), then replaces it with a {@code new} value via the
	 * internal {@link Supplier}.
	 * 
	 * @return either the cache value, or a {@code new} object from the
	 *         {@link Supplier} use at the creation of {@code this}. Can be
	 *         {@code null} if any only if internal value is {@code null}
	 *         <b>and</b> the internal {@link Supplier} generates a {@code null}
	 *         value.
	 */
	public V
		   get(){
		// Could chain call {@link AtomicReference#updateAndGet(UnaryOperator)}
		// into {@link Reference#get()}. However, there would be a race
		// condition with the garage collector between those two calls.
		final AtomicReference<V> atomic = new AtomicReference<>();
		this.value.updateAndGet( ref-> updateReference( ref,
														atomic ) );
		return atomic.get();
	}

	private @NonNull Reference<V>
			updateReference( @Nullable Reference<V> reference,
							 @NonNull AtomicReference<V> atomic ){
		final V value = reference != null ? reference.get()
										  : null;
		if( this.replaceWhen.test( value ) ){
			atomic.set( this.source.get() );
			return makeReference( atomic.get() );
		}
		atomic.set( value );
		return reference;
	}

	/**
	 * If the argument passes {@link this#replaceIf}, sets the reference to the
	 * argument value, else does nothing.
	 * 
	 * <p>
	 * This call is thread-safe.
	 * </p>
	 * 
	 * <p>
	 * Warning, if there is no strong pointer to the parameter object, it will
	 * be replaced after the garage collector removes it.
	 * </p>
	 * 
	 * @param value
	 * @return if the internal value was altered by this call.
	 */
	public boolean
		   set( @Nullable V value ){
		if( !this.replaceWhen.test( value ) ){
			this.value.set( makeReference( value ) );
			return true;
		}
		return false;
	}

	/**
	 * If the argument passes {@link this#replaceIf}, sets the reference to the
	 * argument value, else nothing.
	 * 
	 * <p>
	 * This call is <b>not thread-safe</b>, only the calling thread is
	 * guaranteed to retrieve the same value.
	 * </p>
	 * 
	 * <p>
	 * Warning, if there is no strong pointer to the parameter option, it will
	 * be replaced after the garage collector removes it.
	 * </p>
	 * 
	 * @see AtomicReference#lazySet(Object)
	 * 
	 * @param value
	 * @return if the internal value was altered by this call.
	 */
	public boolean
		   unsafeSet( @Nullable V value ){
		if( !this.replaceWhen.test( value ) ){
			this.value.lazySet( makeReference( value ) );
			return true;
		}
		return false;
	}

	/**
	 * Removes the local value. On the next {@link #get()}, {@link #toString()},
	 * {@link #hashCode()}, or {@link #equals(Object)} call, it will replaced.
	 * Other threads about to use the {@link Reference} will still have the same
	 * object for a (very) short time.
	 * 
	 * @see AtomicReference#lazySet(Object)
	 */
	public void
		   clear(){
		this.value.lazySet( null );
	}

	/**
	 * @see java.lang.Object#toString()
	 * @return [{get()}]
	 */
	public @NonNull String
		   toString(){
		return String.format( "[%s]",
							  this.get()
								  .toString() );
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * @see Objects#hashCode(Object)
	 * 
	 * @return chain calls {@link #get()} into {@link Object#hashCode()}. If
	 *         {@code null}, then {@code 0}.
	 */
	public int
		   hashCode(){
		return Objects.hashCode( this.get() );
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param o
	 * @return Calls {@link Object#equals(Object)} of the internal value to the
	 *         argument. Only if argument {@code o} is the same to this, will
	 *         {@link #get()} not be called..
	 */
	public boolean
		   equals( @Nullable Object o ){
		if( o instanceof LooseSingleton ){
			return this == o
				   || Objects.equals( this.get(),
									  ((LooseSingleton<?>) o).get() );
		}
		return Objects.equals( this.get(),
							   o );
	}
}
