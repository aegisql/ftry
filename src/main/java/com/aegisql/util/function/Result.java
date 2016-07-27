/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

// TODO: Auto-generated Javadoc
/**
 * The Class Result.
 * @author Mikhail Teplitskiy
 *
 * @param <T> the generic type of the stored value
 */
public class Result <T> {
	
	/** The result value. */
	protected final T res;
	
	/**  The error object. */
	protected final Throwable th;
	
	/**
	 * Instantiates a new result.
	 *
	 * @param t the t
	 * @param th the th
	 */
	protected Result(T t,Throwable th) {
		this.res = t;
		this.th  = th;
	}

	
	/**
	 * Instantiates a new Result.
	 *
	 * @param t the t
	 */
	public Result(T t) {
		this(t, null);
	}
	
	/**
	 * Instantiates a new Result.
	 *
	 * @param th the th
	 */
	public Result(Throwable th) {
		this((T)null, th);
	}

	/**
	 * Gets the result value.
	 *
	 * @return the t
	 */
	public T get() {
		return res;
	}

	/**
	 * Get Error object.
	 *
	 * @return the throwable
	 */
	public Throwable error() {
		return th;
	}

	/**
	 * Checks for error.
	 *
	 * @return true, if has error
	 */
	public boolean hasError() {
		return th != null;
	}

	/**
	 * Checks if is present.
	 *
	 * @return true, if result is not null
	 */
	public boolean isPresent() {
		return res != null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((res == null) ? 0 : res.hashCode());
		result = prime * result + (hasError() ? 1 : 0);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result<?> other = (Result<?>) obj;
		if (res == null) {
			if (other.res != null)
				return false;
		} else if (!res.equals(other.res))
			return false;
		if ( hasError() != other.hasError() )
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [val=" + res + "; error=" + (th==null?"null":th.getMessage()) + "]";
	}
	
}
