package org.iag.utility.singleton;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

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
public class SingletonImmutableValueGet {

	@Parameter( 0 )
	public String name;

	@Parameter( 1 )
	public Supplier<Object> getter;

	private LooseSingleton<Object> mock;

	private Object value;

	@Parameters( name = "{index}:{0}" )
	public static List<Object[]>
		   testCases(){
		List<Object[]> list = new LinkedList<>();
		Collections.addAll( list,
							new Object[] { "Null",
										   (Supplier<Object>) ()-> null },
							new Object[] { "Integer",
										   (Supplier<Integer>) ()-> 1 },
							new Object[] { "Id Generated",
										   (Supplier<IdObject>) IdObject::new } );
		return list;
	}

	@Before
	public void
		   build(){
		this.mock = MockSingleton.build( this.getter.get() );
		this.value = this.mock.get();
	}

	@Test
	public void
		   get(){
		Assert.assertSame( "Different Object found, while Strong Pointer in use",
						   this.value,
						   this.mock.get() );
	}
}
