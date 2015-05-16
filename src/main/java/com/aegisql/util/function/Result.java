package com.aegisql.util.function;

public class Result <T> {
	
	protected final T res;
	protected final Throwable th;
	
	protected Result(T t,Throwable th) {
		this.res = t;
		this.th  = th;
	}

	
	public Result(T t) {
		this(t, null);
	}
	
	public Result(Throwable th) {
		this((T)null, th);
	}

	public T get() {
		return res;
	}

	public Throwable error() {
		return th;
	}

	public boolean hasError() {
		return th != null;
	}

	public boolean isPresent() {
		return res != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((res == null) ? 0 : res.hashCode());
		result = prime * result + (hasError() ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result<?> other = (Result<?>) obj;
		if (res == null) {
			if (other.res != null)
				return false;
		} else if (!res.equals(other.res))
			return false;
		if ( hasError() != other.hasError() )
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Result [val=" + res + "; error=" + (th==null?"null":th.getMessage()) + "]";
	}
	
}
