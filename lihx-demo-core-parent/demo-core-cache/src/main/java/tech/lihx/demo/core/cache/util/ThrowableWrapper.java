package tech.lihx.demo.core.cache.util;

public class ThrowableWrapper extends RuntimeException {

	private static final long serialVersionUID = -2097808601351737146L;

	public final Throwable original;


	public ThrowableWrapper( Throwable original ) {
		this.original = original;
	}
}
