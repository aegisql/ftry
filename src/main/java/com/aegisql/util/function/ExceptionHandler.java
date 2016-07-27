/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class ExceptionHandler.
 * @author Mikhail Teplitskiy
 *
 * @param <T> the generic type
 */
public class ExceptionHandler<T extends Throwable> implements ExceptionBlock<T> {
	
	/** The Constant log. */
	public static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
	
	/** The exception class. */
	private final Class<T> exceptionClass;
	
	/** The registered classes. */
	private final Set<Class<? extends Throwable>> registeredClasses = new LinkedHashSet<>();
	
	/** The exception block. */
	private final ExceptionBlock<T> exceptionBlock;
	
	/**
	 * Instantiates a new exception handler.
	 *
	 * @param exceptionClass the exception class
	 * @param exceptionBlock the exception block
	 */
	public ExceptionHandler(Class<T> exceptionClass, ExceptionBlock<T> exceptionBlock) {
		this.exceptionClass = exceptionClass;
		this.exceptionBlock = exceptionBlock;
		this.registeredClasses.add(exceptionClass);
	}
	
	/* (non-Javadoc)
	 * @see com.aegisql.util.function.ExceptionBlock#accept(java.lang.Throwable)
	 */
	@Override
	public void accept(T error) throws Throwable {
		exceptionBlock.accept(error);
	}
	
	/**
	 * Gets the exception class.
	 *
	 * @return the exception class
	 */
	public Class<T> getExceptionClass() {
		return exceptionClass;
	}

	/**
	 * Gets the registered exception classes.
	 *
	 * @return the registered exception classes
	 */
	public Set<Class<? extends Throwable>> getRegisteredExceptionClasses() {
		return Collections.unmodifiableSet(registeredClasses);
	}

	/**
	 * Or catch.
	 *
	 * @param <T2> the generic type
	 * @param t2Class the t2 class
	 * @param eb the eb
	 * @return the exception handler
	 */
	public <T2 extends Throwable> ExceptionHandler<T2> orCatch(final Class<T2> t2Class,ExceptionBlock<T2> eb) {
		Objects.requireNonNull(eb);
		
		if(this.registeredClasses.contains(t2Class)) {
			throw new RuntimeException("Class already registered: "+t2Class.getName());
		}
		
		Class<T> tClass = this.getExceptionClass();
		boolean tFo = tClass.isAssignableFrom(t2Class);
		boolean oFt = t2Class.isAssignableFrom(tClass);
		
		ExceptionHandler<T2> eh = new ExceptionHandler<T2>(t2Class,(T2 t)->{
			Class<?> errClass = t.getClass();
			
			boolean tFe = tClass.isAssignableFrom(errClass);
			boolean eFt = errClass.isAssignableFrom(tClass);

			boolean oFe = t2Class.isAssignableFrom(errClass);
			boolean eFo = errClass.isAssignableFrom(t2Class);
			
			log.trace("this:<"+tClass.getSimpleName()+"> other:<"+t2Class.getSimpleName()+"> actual:<"+errClass.getSimpleName()+">");
			log.trace("t<->o:{}<->{}; t<->e:{}<->{}; o<->e:{}<->{};",tFo,oFt,tFe,eFt,oFe,eFo);
			
			try {
				
				if(tFe && eFt) {
					log.trace("It's this:<{}>",errClass.getSimpleName());
					this.accept((T)t);
					return;
				}

				if(oFe && eFo) {
					log.trace("It's other:<{}>",errClass.getSimpleName());
					eb.accept(t);
					return;
				}

				try {
					if(!tFe && !eFt) {
						log.trace("Try <{}> anyway:<{}>",tClass.getSimpleName(),errClass.getSimpleName());
						this.accept((T)t);
						return;
					}
				} catch(Throwable x) {
					if(x == t) {
						if(!oFe && !eFo) {
							log.trace("Try <{}> anyway:<{}>",t2Class.getSimpleName(),errClass.getSimpleName());
							eb.accept(t);
							return;
						}
					} else {
						throw x;
					}
				}

				T tt = tClass.cast(t);
				log.trace("sending to <{}> :<{}>",tClass.getSimpleName(),errClass.getSimpleName());
				this.accept(tt);
			} catch(ClassCastException t2) {
				try {
					t2Class.cast(t);
				}  catch(ClassCastException t4) {
					log.trace("give up with <{}>",errClass.getSimpleName());
					throw t;
				}
				log.trace("sending to <{}> :<{}>",t2Class.getSimpleName(),errClass.getSimpleName());
				eb.accept(t);
			} catch (Throwable t3) {
				throw t3;
			}
		});
		
		eh.registeredClasses.addAll(this.registeredClasses);
		
		return eh;
	}

	/**
	 * Wrap throwable.
	 *
	 * @param <R> the generic type
	 * @return the function
	 */
	public static <R extends Throwable> Function<Class<R>,Function<Throwable,R>> wrapThrowable() {
		return wr->thr->{
			try {
				Constructor<R> con = wr.getConstructor(Throwable.class);
				return con.newInstance(thr);
			} catch (Exception e) {
				throw new RuntimeException("String,Throwable constructor is unavailable for "+wr.getName(),e);
			}
		};
	}

	
	/**
	 * Wrap commented throwable.
	 *
	 * @param <R> the generic type
	 * @return the function
	 */
	public static <R extends Throwable> Function<Class<R>,Function<String,Function<Throwable,R>>> wrapCommentedThrowable() {
		return wr->str->thr->{
			try {
				Constructor<R> con = wr.getConstructor(String.class,Throwable.class);
				return con.newInstance(str,thr);
			} catch (Exception e) {
				throw new RuntimeException("String,Throwable constructor is unavailable for "+wr.getName(),e);
			}
		};
	}
	
	/**
	 * To string.
	 *
	 * @param t the t
	 * @return the string
	 */
	public static String toString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		return exceptionAsString;
	}
	
}
