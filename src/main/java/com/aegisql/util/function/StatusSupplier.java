/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

// TODO: Auto-generated Javadoc
/**
 * Represents a supplier of Result Status.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the StatusSupplier is invoked.
 *
 * @author Mikhail Teplitskiy
 */
@FunctionalInterface
public interface StatusSupplier {
	
	/**
	 * gets Result Status.
	 *
	 * @return the eval status
	 */
	EvalStatus eval();
}
