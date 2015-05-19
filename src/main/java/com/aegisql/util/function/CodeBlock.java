/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import java.util.Objects;

/**
 * The Interface CodeBlock.
 * @author Mikhail Teplitskiy
 */
@FunctionalInterface
public interface CodeBlock {
	
	/**
	 * Represents Code Block evaluator
	 *
	 * @throws Throwable the throwable
	 */
	public void eval() throws Throwable;
	
    /**
     * Returns a CodeBlock that always returns its input argument.
     *
     * @return a CodeBlock that always returns its input argument
     */
	public default CodeBlock identity(CodeBlock it) {
		return it;
	}
	
    /**
     * Returns a composed CodeBlock that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the CodeBlock to apply after this function is applied
     * @return a composed CodeBlock that first applies this function and then
     * applies the {@code after} CodeBlock
     * @throws NullPointerException if after is null
     * @throws Throwable
     *
     * @see #compose(CodeBlock)
     */	public default CodeBlock andThen(CodeBlock after) {
		Objects.requireNonNull(after);
		return () -> {
			this.eval();
			after.eval();
		};
	}

     /**
      * Returns a composed CodeBlock that first applies the {@code before}
      * CodeBlock to its input, and then applies this CodeBlock to the result.
      * If evaluation of either CodeBlock throws an exception, it is relayed to
      * the caller of the composed CodeBlock.
      *
      * @param before the CodeBlock to apply before this CodeBlock is applied
      * @return a composed CodeBlock that first applies the {@code before}
      * CodeBlock and then applies this CodeBlock
      * @throws NullPointerException if before is null
      * @throws Throwable
      *
      * @see #andThen(CodeBlock)
      */
	public default CodeBlock compose(CodeBlock before) {
		Objects.requireNonNull(before);
		return () -> {
			before.eval();
			this.eval();
		};
	}

}
