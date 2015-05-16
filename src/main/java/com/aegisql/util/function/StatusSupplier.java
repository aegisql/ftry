package com.aegisql.util.function;

@FunctionalInterface
public interface StatusSupplier {
	EvalStatus eval();
}
