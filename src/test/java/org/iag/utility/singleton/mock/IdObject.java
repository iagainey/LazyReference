package org.iag.utility.singleton.mock;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Min;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * POJO for tracking construction calls during testing.
 * 
 * @author Isaac G.
 *
 */
public class IdObject implements
					  Serializable {

	private static final long serialVersionUID = 3567100217496046548L;

	@Min( 0 )
	private final int id;

	@Min( 0 )
	private static volatile int MAX_ID = 0;

	/**
	 * a {@link IdObject} whose {@link #getId() unique id} if the greatest
	 * currently.
	 */
	public IdObject(){
		this.id = getAndIncMaxId();
	}

	/**
	 * Copies the id from the argument.
	 * 
	 * @param obj
	 *            the idObj to clone
	 */
	protected IdObject( @NonNull IdObject obj ){
		this.id = Objects.requireNonNull( obj )
						 .getId();
	}

	/**
	 * 
	 * @return this' unique id
	 */
	public final int
		   getId(){
		return this.id;
	}

	/**
	 * @return this as json
	 */
	public @NonNull String
		   toString(){
		return String.format( "{id:%s}",
							  getId() );
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * @return {@link getId()}
	 */
	public int
		   hashCode(){
		return getId();
	}

	/**
	 * @see Object#equals(Object)
	 * @param o
	 *            the object to test equality against
	 * @return {@code true} if and only if the argument inherits
	 *         {@link IdObject} and has the same {@link #getId() unique id}
	 */
	public boolean
		   equals( @Nullable Object o ){
		return o instanceof IdObject
			   && ((IdObject) o).getId() == this.getId();
	}

	/**
	 * Will increment the id tracker.
	 * 
	 * @return an unique id from every other {@link IdObject}
	 */
	protected static synchronized int
			  getAndIncMaxId(){
		return MAX_ID++;
	}
}
