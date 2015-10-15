/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import java.io.PrintStream;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;

/**
 * The Interface ExceptionBlock.
 * @author Mikhail Teplitskiy
 *
 * @param <T> the generic type
 */
@FunctionalInterface
public interface ExceptionBlock <T extends Throwable> {
	
	/**
	 * Accept.
	 *
	 * @param error the error
	 * @throws Throwable the throwable
	 */
	public void accept(T error) throws Throwable;

	/**
	 * Identity.
	 *
	 * @param it the it
	 * @return the exception block
	 */
	public default ExceptionBlock<T> identity(ExceptionBlock<T> it) {
		Objects.requireNonNull(it);
		return it;
	}
	
	/**
	 * And then.
	 *
	 * @param after the after
	 * @return the exception block
	 */
	public default ExceptionBlock<T> andThen(ExceptionBlock<T> after) {
		Objects.requireNonNull(after);
		return (T t) -> {
			this.accept(t);
			after.accept(t);
		};
	}

	/**
	 * Compose.
	 *
	 * @param after the after
	 * @return the exception block
	 */
	public default ExceptionBlock<T> compose(ExceptionBlock<T> after) {
		Objects.requireNonNull(after);
		return (T t) -> {
			after.accept(t);
			this.accept(t);
		};
	}

	/**
	 * And die.
	 *
	 * @return the exception block
	 */
	public default ExceptionBlock<T> andDie() {
		return (T t) -> {
			this.accept(t);
			throw t;
		};
	}

	/**
	 * And die as.
	 *
	 * @param wrapper the wrapper
	 * @param errMessage the err message
	 * @return the exception block
	 */
	public default ExceptionBlock<T> andDieAs(Class<? extends Throwable> wrapper,  String errMessage) {
		Function<Throwable,Throwable> f = ExceptionHandler.wrapCommentedThrowable().apply((Class<Throwable>) wrapper).apply(errMessage);
		return (T t) -> {
			this.accept(t);
			Throwable wrapped = f.apply(t);
			throw wrapped;
		};
	}
	
	/**
	 * Prints the stack trace.
	 *
	 * @return the exception block
	 */
	public default ExceptionBlock<T> printStackTrace() {
		return andThen( t -> t.printStackTrace() );
	}

	/**
	 * Prints the stack trace.
	 *
	 * @param pStr the str
	 * @return the exception block
	 */
	public default ExceptionBlock<T> printStackTrace(PrintStream pStr) {
		return andThen( t -> t.printStackTrace(pStr) );
	}

	/**
	 * Prints the error message.
	 *
	 * @return the exception block
	 */
	public default ExceptionBlock<T> printErrorMessage() {
		return andThen( t -> System.out.println(t.getMessage()) );
	}

	/**
	 * Log debug stack trace.
	 *
	 * @param preffix the preffix of the log message
	 * @param log the SLF4J logger
	 * @param params - variables supported by the SLF4J 
	 * @return the exception block
	 */
	public default ExceptionBlock<T> logError(Logger log, String preffix, Object... params) {
		return (T t) -> {
			if(log.isErrorEnabled()) {
				log.error(preffix + ExceptionHandler.toString(t), params);
			}
			this.accept(t);
		};
	}

	/**
	 * Log warn stack trace.
	 *
	 * @param preffix the preffix of the log message
	 * @param log the SLF4J logger
	 * @param params - variables supported by the SLF4J 
	 * @return the exception block
	 */
	public default ExceptionBlock<T> logWarn(Logger log, String preffix, Object... params) {
		return (T t) -> {
			if(log.isWarnEnabled()) {
				log.warn(preffix + ExceptionHandler.toString(t), params);
			}
			this.accept(t);
		};
	}

	/**
	 * Log info stack trace.
	 *
	 * @param preffix the preffix of the log message
	 * @param log the SLF4J logger
	 * @param params - variables supported by the SLF4J 
	 * @return the exception block
	 */
	public default ExceptionBlock<T> logInfo(Logger log, String preffix, Object... params) {
		return (T t) -> {
			if(log.isInfoEnabled()) {
				log.info(preffix + ExceptionHandler.toString(t), params);
			}
			this.accept(t);
		};
	}
	
	/**
	 * Log debug stack trace.
	 *
	 * @param preffix the preffix of the log message
	 * @param log the SLF4J logger
	 * @param params - variables supported by the SLF4J 
	 * @return the exception block
	 */
	public default ExceptionBlock<T> logDebug(Logger log, String preffix, Object... params) {
		return (T t) -> {
			if(log.isDebugEnabled()) {
				log.debug(preffix + ExceptionHandler.toString(t), params);
			}
			this.accept(t);
		};
	}

	/**
	 * Log trace stack trace.
	 *
	 * @param preffix the preffix of the log message
	 * @param log the SLF4J logger
	 * @param params - variables supported by the SLF4J 
	 * @return the exception block
	 */
	public default ExceptionBlock<T> logTrace(Logger log, String preffix, Object... params) {
		return (T t) -> {
			if(log.isTraceEnabled()) {
				log.trace(preffix + ExceptionHandler.toString(t), params);
			}
			this.accept(t);
		};
	}

	
}
