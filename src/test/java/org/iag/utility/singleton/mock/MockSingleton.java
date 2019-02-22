package org.iag.utility.singleton.mock;

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
		   nullBuild(){
		return build( (O) null );
	}

	public static < O >
		   @NonNull LooseSingleton<O>
		   build( @Nullable O obj ){
		@SuppressWarnings( "unchecked" )
		final LooseSingleton<O> singleton = Mockito.mock( LooseSingleton.class,
														  Mockito.CALLS_REAL_METHODS );
		Mockito.doReturn( obj )
			   .when( singleton )
			   .get();
		Mockito.doReturn( false )
			   .when( singleton )
			   .set( ArgumentMatchers.any() );
		Mockito.doReturn( false )
			   .when( singleton )
			   .unsafeSet( ArgumentMatchers.any() );
		return singleton;
	}
}
