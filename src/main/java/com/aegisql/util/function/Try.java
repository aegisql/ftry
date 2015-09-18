/*
 *Copyright (c) 2015, AEGIS DATA SOLUTIONS, All rights reserved. 
 */
package com.aegisql.util.function;

import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Try.
 * @author Mikhail Teplitskiy
 */
public class Try {
	
	/** The Constant log. */
	public static final Logger log = LoggerFactory.getLogger(Try.class);
	
	/** The code block. */
	private final CodeBlock codeBlock;
	
	/** The exception handler. */
	private ExceptionHandler<?> exceptionHandler;
	
	/** The final block. */
	private CodeBlock finalBlock = () -> {};
	
	/**
	 * Instantiates a new try.
	 *
	 * @param codeBlock the code block
	 */
	public Try(CodeBlock codeBlock) {
		Objects.requireNonNull(codeBlock);
		this.codeBlock = codeBlock;
	}
	
	/**
	 * Or catch.
	 *
	 * @param <T> the generic type
	 * @param exceptionClass the exception class
	 * @param exceptionBlock the exception block
	 * @return the try
	 */
	public <T extends Throwable> Try orCatch(Class<T> exceptionClass, ExceptionBlock<T> exceptionBlock) {
		Objects.requireNonNull(exceptionClass);
		Objects.requireNonNull(exceptionBlock);
		if(exceptionHandler == null) {
			if(exceptionBlock instanceof ExceptionHandler) {
				exceptionHandler = (ExceptionHandler<T>)exceptionBlock;
			} else {
				exceptionHandler = new ExceptionHandler<T>(exceptionClass, exceptionBlock);
			}
		} else {
			exceptionHandler = exceptionHandler.orCatch(exceptionClass, exceptionBlock);
		}
		return this;
	}
	
	/**
	 * With final.
	 *
	 * @param finalBlock the final block
	 * @return the try
	 */
	public Try withFinal(CodeBlock finalBlock) {
		Objects.requireNonNull(finalBlock);
		this.finalBlock = finalBlock;
		return this;
	}
	
	/**
	 * Evaluator builder.
	 *
	 * @param <TH> the generic type
	 * @return the throwing function
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <TH extends Throwable> 
		ThrowingFunction<CodeBlock,         //Final
			ThrowingFunction<ExceptionBlock,//Exception
				ThrowingFunction<CodeBlock, //Code
					Eval<?>,TH>,TH>,TH>     //Eval 
		evaluatorBuilder() {
		return fBlock->exBlock->cBlock->(/*eval*/)->{
			boolean status = true;
			try {
				cBlock.eval();
			} catch (Throwable t) {
				if(exBlock == null) {
					throw t;
				}
				status = false;
				exBlock.accept(t);
			} finally {
				fBlock.eval();
			}
			return status;
		};
	}
	
	/**
	 * Evaluator.
	 *
	 * @return the eval
	 * @throws Throwable the throwable
	 */
	Eval<?> evaluator() throws Throwable {
		return Try.<Throwable>evaluatorBuilder()
			.apply(finalBlock)
			.apply(exceptionHandler)
			.apply(codeBlock);
	}

	/**
	 * Runtime evaluator.
	 *
	 * @return the eval
	 */
	Eval<RuntimeException> runtimeEvaluator() {
		return (Eval<RuntimeException>) Try.<RuntimeException>evaluatorBuilder()
			.apply(finalBlock)
			.apply(exceptionHandler)
			.apply(codeBlock).wrapThrowable();
	}

	/**
	 * Checked evaluator.
	 *
	 * @param <TH> the generic type
	 * @param eClass the e class
	 * @return the eval
	 */
	<TH extends Throwable> Eval<TH> checkedEvaluator(Class<TH> eClass) {
		try {
			Eval<TH> ev = Try.<TH>evaluatorBuilder()
				.apply(finalBlock)
				.apply(exceptionHandler)
				.apply(codeBlock).wrapThrowable(eClass);
			return ev;
		} catch (Throwable e) {
			assert false : e;
			return () -> {throw new RuntimeException("Unexpected failure while building checkedEvaluator: ",e);};
		}
	}

	/**
	 * Wrapped evaluator.
	 *
	 * @return the status supplier
	 */
	StatusSupplier wrappedEvaluator() {
		try {
			final Eval<?> j = evaluatorBuilder()
				.apply(finalBlock)
				.apply(exceptionHandler)
				.apply(codeBlock);
			return () -> {
				try {
					return new EvalStatus(j.eval());
				} catch (Throwable e) {
					return new EvalStatus(e);
				}
			};
		} catch (Throwable e) {
			assert false : e;
			return () -> {throw new RuntimeException("Unexpected failure while building wrapedEvaluator: ",e);};
		}
	}

	/**
	 * Gets the registered error types.
	 *
	 * @return the registered error types
	 */
	public Set<Class<? extends Throwable>> getRegisteredErrorTypes() {
		return exceptionHandler.getRegisteredExceptionClasses();
	}
	
}
