package org.iag.utility.singleton.mock;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class IdObject {
	private final int id;

	private static volatile int MAX_ID = 0;

	public IdObject(){
		this.id = getAndIncMaxId();
	}

	public final int
		   getId(){
		return this.id;
	}

	public @NonNull String
		   toString(){
		return String.format( "[%s]",
							  getId() );
	}

	public int
		   hashCode(){
		return getId();
	}

	public boolean
		   equals( @Nullable Object o ){
		return o instanceof IdObject
			   && ((IdObject) o).getId() == this.getId();
	}

	protected static synchronized int
			  getAndIncMaxId(){
		return MAX_ID++;
	}
}
