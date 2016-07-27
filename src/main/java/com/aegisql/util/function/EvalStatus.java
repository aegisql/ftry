/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

// TODO: Auto-generated Javadoc
/**
 * The Class EvalStatus.
 * @author Mikhail Teplitskiy
 */
public final class EvalStatus extends Result<Boolean> {

	/**
	 * Instantiates a new eval status.
	 *
	 * @param t the t
	 */
	public EvalStatus(Boolean t) {
		super(t,null);
	}

	/**
	 * Instantiates a new eval status.
	 *
	 * @param t the t
	 */
	public EvalStatus(Throwable t) {
		super(false,t);
	}

	/**
	 * Success.
	 *
	 * @return true, if successful
	 */
	public boolean success() {
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.aegisql.util.function.Result#toString()
	 */
	@Override
	public String toString() {
		return "EvalStatus [success=" + success() + (th==null ? "" : "; unhandled error found: "+th.getMessage() ) + "]";
	}
	
}

