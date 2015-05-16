package com.aegisql.util.function;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Eval <TH extends Throwable> {
	public boolean eval() throws TH;

	public default Supplier<Result<Boolean>> wrap(Eval<?> ev) {
		return () -> {
			try {
				return new Result<>(ev.eval());
			} catch(Throwable t) {
				return new Result<>(t);
			}
		};
		
	}
	
	public default Eval<RuntimeException> wrapThrowable() {
		return wrapThrowable(RuntimeException.class);
	}
	
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

	public default CodeBlock toCodeBlock(AtomicBoolean res) {
		return () -> res.set( this.eval() );
	}

	public default CodeBlock toCodeBlock() {
		return () -> this.eval();
	}

}
