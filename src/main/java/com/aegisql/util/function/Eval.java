/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO: Auto-generated Javadoc
/**
 * The Functional Interface Eval.
 * @author Mikhail Teplitskiy
 *
 * @param <TH> the generic type
 */
public interface Eval <TH extends Throwable> {
	
	/**
	 * Represents code evaluator
	 * 
	 * <p>There is no requirement that a new or distinct result be returned each
	 * time the supplier is invoked.
	 *
	 * @return true, if successful
	 * @throws TH the th
	 */
	public boolean eval() throws TH;

	/**
	 * Wrapper for the Eval code
	 * which is Throwable safe. i.e. intercepts Exceptions and Errors
	 * Possible exceptions saved in the Result object
	 *
	 * @param ev the ev
	 * @return the Result Supplier
	 */
	public default Supplier<Result<Boolean>> wrap(Eval<?> ev) {
		return () -> {
			try {
				return new Result<>(ev.eval());
			} catch(Throwable t) {
				return new Result<>(t);
			}
		};
		
	}
	
	/**
	 * Wrap throwable unto a RuntimeException.
	 *
	 * @return the Eval
	 */
	public default Eval<RuntimeException> wrapThrowable() {
		return wrapThrowable(RuntimeException.class);
	}
	
	/**
	 * Wrap throwable into a provided Throwable object.
	 *
	 * @param <TH2> the generic type
	 * @param eClass the e class
	 * @return the Eval throwing TH2
	 */
	public default <TH2 extends Throwable> Eval<TH2> wrapThrowable(Class<TH2> eClass) {
		Function<Throwable, TH2> ef = ExceptionHandler.<TH2>wrapThrowable().apply(eClass);
		return () -> {
			try {
				return this.eval();
			} catch(Throwable e) {
				if(eClass.isAssignableFrom(e.getClass()))
				if(e.getClass() == eClass) { 
					throw (TH2)e;
				}
				throw ef.apply(e);
			}
		};
	}

	/**
	 * To CodeBlock.
	 *
	 * @param res Return result of evaluation via mutable AtomicBoolean parameter.
	 * @return the CodeBlock
	 */
	public default CodeBlock toCodeBlock(AtomicBoolean res) {
		return () -> res.set( this.eval() );
	}

	/**
	 * To CodeBlock.
	 *
	 * @return the CodeBlock
	 */
	public default CodeBlock toCodeBlock() {
		return () -> this.eval();
	}

}
