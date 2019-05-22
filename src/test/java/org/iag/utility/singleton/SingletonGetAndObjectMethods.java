package org.iag.utility.singleton;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.hamcrest.CoreMatchers;
import org.iag.utility.singleton.mock.IdObject;
import org.iag.utility.singleton.mock.MockSingleton;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Double checks the patch from {@link #LooseSingleton(Supplier) creation} to
 * {@link LooseSingleton#get() get() }
 * 
 * @author Isaac
 *
 */
@RunWith( Parameterized.class )
public class SingletonGetAndObjectMethods {

	@Parameter( 0 )
	@NonNull
	public String caseName;

	@Parameter( 1 )
	@NonNull
	public Supplier<Object> getter;

	@Parameter( 2 )
	@NonNull
	public boolean same;

	@NonNull
	private LooseSingleton<Object> mock;

	@Parameters( name = "{index}:{0}" )
	public static List<Object[]>
		   testCases(){
		List<Object[]> list = new LinkedList<>();
		Collections.addAll( list,
							new Object[] { "Null, Always empty",
										   (Supplier<Object>) ()-> null,
										   true },
							new Object[] { "Integer/Same",
										   (Supplier<Integer>) ()-> 1,
										   true },
							new Object[] { "Id Generated/Clearable/Unique",
										   (Supplier<IdObject>) IdObject::new,
										   false } );
		return list;
	}

	@Before
	public void
		   build(){
		this.mock = new MockSingleton<>( this.getter );
	}

	@Test
	public void
		   get(){
		Object value = this.mock.get();
		Assert.assertSame( "Different Object found, while Strong Pointer in use",
						   value,
						   this.mock.get() );
	}

	@Test
	public void
		   mutateThenGet(){
		Object value = this.mock.get();
		this.mock.clear();
		Assert.assertThat( same ? "Different Object found after cleared, expected same/similar object"
								: "Same Object found after cleared, expected new object",
						   this.mock.get(),
						   same ? CoreMatchers.is( value )
								: CoreMatchers.not( value ) );
	}

	@Test
	public void
		   similarToString(){
		Object value = this.mock.get();
		Assert.assertThat( "toString() not depended on value",
						   this.mock.toString(),
						   CoreMatchers.containsString( Objects.toString( value ) ) );
	}

	@Test
	public void
		   sameHashCode(){
		Object value = this.mock.get();
		Assert.assertEquals( Objects.hashCode( value ),
							 this.mock.hashCode() );
	}

	@Test
	public void
		   sameObjEquals(){
		Assert.assertTrue( this.mock.equals( this.mock ) );
	}

	@Test
	public void
		   objEquals(){
		Object value = this.mock.get();
		Assert.assertTrue( this.mock.equals( value ) );
	}

	@Test
	public void
		   valEquals(){
		Object value = this.mock.get();
		Assert.assertTrue( this.mock.equals( new MockSingleton<>( value ) ) );
	}

	@Test
	public void
		   randomObjEquals(){
		Assert.assertFalse( this.mock.equals( "" ) );
	}
}
