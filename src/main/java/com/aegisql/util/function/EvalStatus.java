package com.aegisql.util.function;

public final class EvalStatus extends Result<Boolean> {

	public EvalStatus(Boolean t) {
		super(t,null);
	}

	public EvalStatus(Throwable t) {
		super(false,t);
	}

	public boolean success() {
		return res;
	}
	
	@Override
	public String toString() {
		return "EvalStatus [success=" + success() + (th==null ? "" : "; unhandled error found: "+th.getMessage() ) + "]";
	}
	
}

