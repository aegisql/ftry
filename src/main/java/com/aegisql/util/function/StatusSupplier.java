/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

/**
 * The Interface StatusSupplier.
 * @author Mikhail Teplitskiy
 */
@FunctionalInterface
public interface StatusSupplier {
	
	/**
	 * Eval.
	 *
	 * @return the eval status
	 */
	EvalStatus eval();
}
