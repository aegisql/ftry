package com.aegisql.util.function;

import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Try {
	
	public static final Logger log = LoggerFactory.getLogger(Try.class);
	
	private final CodeBlock codeBlock;
	private ExceptionHandler<?> exceptionHandler;
	private CodeBlock finalBlock = () -> {};
	
	public Try(CodeBlock codeBlock) {
		Objects.requireNonNull(codeBlock);
		this.codeBlock = codeBlock;
	}
	
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
	
	public Try withFinal(CodeBlock finalBlock) {
		Objects.requireNonNull(finalBlock);
		this.finalBlock = finalBlock;
		return this;
	}
	
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
	
	Eval<?> evaluator() throws Throwable {
		return Try.<Throwable>evaluatorBuilder()
			.apply(finalBlock)
			.apply(exceptionHandler)
			.apply(codeBlock);
	}

	Eval<RuntimeException> runtimeEvaluator() {
		return (Eval<RuntimeException>) Try.<RuntimeException>evaluatorBuilder()
			.apply(finalBlock)
			.apply(exceptionHandler)
			.apply(codeBlock).wrapThrowable();
	}

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

	StatusSupplier wrapedEvaluator() {
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

	public Set<Class<? extends Throwable>> getRegisteredErrorTypes() {
		return exceptionHandler.getRegisteredExceptionClasses();
	}
	
}
