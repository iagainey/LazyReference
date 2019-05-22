package org.iag.utility.singleton.mock;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.iag.utility.singleton.LooseSingleton;

public class MockSingleton< T >
						  extends
						  LooseSingleton<T> {

	public MockSingleton( T obj ){
		this( ()-> obj );
	}

	public MockSingleton( @NonNull Supplier<? extends T> getter ){
		super( getter );
	}

	@Override
	protected @NonNull Reference<T>
			  makeReference( @Nullable T value ){
		return new SoftReference<>( value );
	}

	@Override
	public boolean
		   set( @Nullable T value ){
		return false;
	}

	@Override
	public boolean
		   unsafeSet( @Nullable T value ){
		return false;
	}

	@Override
	public void
		   clear(){
		super.clear();
	}

	/**
	 * 
	 * @return a {@link LooseSingleton}, where the setters are disabled and
	 *         can never have a value.
	 */
	public static < O >
		   @NonNull LooseSingleton<O>
		   emptyBuild(){
		return new MockSingleton<>( (O) null );
	}
}
