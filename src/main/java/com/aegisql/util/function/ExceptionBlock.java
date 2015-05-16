package com.aegisql.util.function;

import java.io.PrintStream;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;

@FunctionalInterface
public interface ExceptionBlock <T extends Throwable> {
	
	public void accept(T error) throws Throwable;

	public default ExceptionBlock<T> identity(ExceptionBlock<T> it) {
		Objects.requireNonNull(it);
		return it;
	}
	
	public default ExceptionBlock<T> andThen(ExceptionBlock<T> after) {
		Objects.requireNonNull(after);
		return (T t) -> {
			this.accept(t);
			after.accept(t);
		};
	}

	public default ExceptionBlock<T> compose(ExceptionBlock<T> after) {
		Objects.requireNonNull(after);
		return (T t) -> {
			after.accept(t);
			this.accept(t);
		};
	}

	public default ExceptionBlock<T> andDie() {
		return (T t) -> {
			this.accept(t);
			throw t;
		};
	}

	public default ExceptionBlock<T> andDieAs(Class<? extends Throwable> wrapper,  String errMessage) {
		Function<Throwable,Throwable> f = ExceptionHandler.wrapCommentedThrowable().apply((Class<Throwable>) wrapper).apply(errMessage);
		return (T t) -> {
			this.accept(t);
			Throwable wrapped = f.apply(t);
			throw wrapped;
		};
	}
	
	public default ExceptionBlock<T> printStackTrace() {
		return andThen( t -> t.printStackTrace() );
	}

	public default ExceptionBlock<T> printStackTrace(PrintStream pStr) {
		return andThen( t -> t.printStackTrace(pStr) );
	}

	public default ExceptionBlock<T> printErrorMessage() {
		return andThen( t -> System.out.println(t.getMessage()) );
	}

	
	public default ExceptionBlock<T> logStackTrace(String preffix, Logger log, String logLevel) {
		return (T t) -> {
			switch(logLevel.toLowerCase()) {
			case "error":
				if(log.isErrorEnabled()) {
					log.error(preffix + ExceptionHandler.toString(t));
				}
				break;
			case "warn":
				if(log.isWarnEnabled()) {
					log.warn(preffix + ExceptionHandler.toString(t));
				}
				break;
			case "info":
				if(log.isInfoEnabled()) {
					log.info(preffix + ExceptionHandler.toString(t));
				}
				break;
			case "debug":
				if(log.isDebugEnabled()) {
					log.debug(preffix + ExceptionHandler.toString(t));
				}
				break;
			case "trace":
				if(log.isTraceEnabled()) {
					log.trace(preffix + ExceptionHandler.toString(t));
				}
				break;
			default:
				break;
			}
			this.accept(t);
		};
	}
	
	
}
