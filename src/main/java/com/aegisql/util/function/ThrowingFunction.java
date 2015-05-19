/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The Interface ThrowingFunction.
 * @author Mikhail Teplitskiy
 *
 * @param <T> the generic type
 * @param <R> the generic type
 * @param <E> the element type
 */
@FunctionalInterface
public interface ThrowingFunction <T,R,E extends Throwable> {
	
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
	 * @throws E the e
	 */
	public R apply(T t) throws E;

	/**
	 * Wrap.
	 *
	 * @return the function
	 */
	public default Function <T,Result<R>> wrap() {
		return t->{
			try {
				return new Result<R>(this.apply(t));
			} catch (Throwable e) {
				return new Result<R>(e);
			}
		};
	}
	
	/**
	 * Wrap exception.
	 *
	 * @return the throwing function
	 */
	public default ThrowingFunction <T,R,RuntimeException> wrapException() {
		return this.wrapException(RuntimeException.class);
	}

	/**
	 * Wrap exception.
	 *
	 * @param message the message
	 * @return the throwing function
	 */
	public default ThrowingFunction <T,R,RuntimeException> wrapException(String message) {
		return this.wrapException(RuntimeException.class,message);
	}

	/**
	 * Wrap exception.
	 *
	 * @param message the message
	 * @return the throwing function
	 */
	public default ThrowingFunction <T,R,RuntimeException> wrapException(Supplier<String> message) {
		return this.wrapException(RuntimeException.class,message);
	}

	/**
	 * Wrap exception.
	 *
	 * @param <E1> the generic type
	 * @param eClass the e class
	 * @return the throwing function
	 */
	public default <E1 extends Throwable> ThrowingFunction <T,R,E1> wrapException(Class<E1> eClass) {
		Function<Throwable, E1> ef = ExceptionHandler.<E1>wrapThrowable().apply(eClass);
		ThrowingFunction<T,R,E> THIS = this;
		return t->{
			try {
				return THIS.apply( t );
			} catch(Throwable e) {
				if(eClass.isAssignableFrom(e.getClass())) 
					throw (E1)e;
				else
					throw ef.apply(e);
			}
		};
	}

	/**
	 * Wrap exception.
	 *
	 * @param <E1> the generic type
	 * @param eClass the e class
	 * @param message the message
	 * @return the throwing function
	 */
	public default <E1 extends Throwable> ThrowingFunction <T,R,E1> wrapException(Class<E1> eClass, String message) {
		Function<Throwable, E1> ef = ExceptionHandler.<E1>wrapCommentedThrowable().apply(eClass).apply(message);
		return t->{
			try {
				return this.apply(t);
			} catch(Throwable e) {
				if(eClass.isAssignableFrom(e.getClass())) 
					throw (E1)e;
				else
					throw ef.apply(e);
			}
		};
	}

	/**
	 * Wrap exception.
	 *
	 * @param <E1> the generic type
	 * @param eClass the e class
	 * @param message the message
	 * @return the throwing function
	 */
	public default <E1 extends Throwable> ThrowingFunction <T,R,E1> wrapException(Class<E1> eClass, Supplier<String> message) {
		Function<Throwable, E1> ef = ExceptionHandler.<E1>wrapCommentedThrowable().apply(eClass).apply(message.get());
		return t->{
			try {
				return this.apply(t);
			} catch(Throwable e) {
				if(eClass.isAssignableFrom(e.getClass())) 
					throw (E1)e;
				else 
					throw ef.apply(e);
			}
		};
	}

}
