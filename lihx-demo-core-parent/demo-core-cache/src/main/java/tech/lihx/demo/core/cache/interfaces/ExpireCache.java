package tech.lihx.demo.core.cache.interfaces;

import tech.lihx.demo.core.cache.util.Invoker;


public interface ExpireCache {

	public Object get( String key, Invoker invoker, int expire, boolean compress );

	// public void remove(String key);

}
