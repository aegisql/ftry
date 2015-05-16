package com.aegisql.util.function;

import java.util.Objects;

@FunctionalInterface
public interface CodeBlock {
	public void eval() throws Throwable;
	
	public default CodeBlock identity(CodeBlock it) {
		return it;
	}
	
	public default CodeBlock andThen(CodeBlock after) {
		Objects.requireNonNull(after);
		return () -> {
			this.eval();
			after.eval();
		};
	}

	public default CodeBlock compose(CodeBlock after) {
		Objects.requireNonNull(after);
		return () -> {
			after.eval();
			this.eval();
		};
	}

}
