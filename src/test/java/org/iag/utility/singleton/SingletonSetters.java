package org.iag.utility.singleton;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingletonSetters {
	private LooseSingleton<Integer> loose;

	@Before
	public void
		   buildSingleton(){
		this.loose = new SoftSingleton<>( ()-> 0,
										  i-> i < 0 );
	}

	@Test
	public void
		   setNull(){
		this.loose.set( null );
		Assert.assertNotNull( this.loose.get() );
	}

	@Test
	public void
		   setInvalid(){
		this.loose.set( -1 );
		Assert.assertEquals( (Integer) 0,
							 this.loose.get() );
	}

	@Test
	public void
		   set(){
		this.loose.set( 1 );
		Assert.assertEquals( (Integer) 1,
							 this.loose.get() );
	}

	@Test
	public void
		   unsafeSetNull(){
		this.loose.unsafeSet( null );
		Assert.assertNotNull( this.loose.get() );
	}

	@Test
	public void
		   unsafeSetInvalid(){
		this.loose.unsafeSet( -1 );
		Assert.assertNotNull( this.loose.get() );
	}

	@Test
	public void
		   unsafeSet(){
		this.loose.unsafeSet( 1 );
		Assert.assertEquals( (Integer) 1,
							 this.loose.get() );
	}
}
