package org.iag.utility.singleton.mock;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.iag.utility.singleton.LooseSingleton;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class MockSingleton {
	private MockSingleton(){
	};

	public static < O >
		   @NonNull LooseSingleton<O>
		   emptyBuild(){
		return build( (O) null );
	}

	/**
	 * Spy Mock, where only the setters are disabled.
	 * 
	 * @param obj
	 * @return
	 */
	public static < O >
		   @NonNull LooseSingleton<O>
		   build( @Nullable O obj ){
		return build( ()-> obj );
	}

	/**
	 * Spy Mock, where only the setters are disabled.
	 * 
	 * @param obj
	 * @return
	 */
	public static < O >
		   @NonNull LooseSingleton<O>
		   build( @Nullable Supplier<? extends O> getter ){
		@SuppressWarnings( "unchecked" )
		final LooseSingleton<O> singleton = Mockito.mock( LooseSingleton.class,
														  Mockito.withSettings()
																 .useConstructor( getter )
																 .defaultAnswer( Mockito.CALLS_REAL_METHODS ) );
		Mockito.doReturn( false )
			   .when( singleton )
			   .set( ArgumentMatchers.any() );
		Mockito.doReturn( false )
			   .when( singleton )
			   .unsafeSet( ArgumentMatchers.any() );
		return singleton;
	}
}
