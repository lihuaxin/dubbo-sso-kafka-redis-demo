package tech.lihx.demo.core.cache.ehcache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import tech.lihx.demo.core.cache.interfaces.TableCache;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.LockSupport;
import tech.lihx.demo.core.common.util.ObjectCompressUtil;

/**
 * 表级缓存
 * <p>
 * 
 * @author lihx
 * @Date 2014年9月15日
 */
public class EhTableCache implements TableCache {

	private CacheManager manager;


	public CacheManager getManager() {
		return manager;
	}


	public void setManager( CacheManager manager ) {
		this.manager = manager;
	}


	@Override
	public Object get( String table, String key, Invoker invoker, boolean compress ) {
		Object result = null;
		Cache cache = getCache(table);
		Element element = cache.get(key);
		if ( element != null ) {
			result = element.getObjectValue();
			if ( compress ) {
				result = ObjectCompressUtil.uncompress((byte[]) result);
			}
			return result;
		}
		Object aLock = LockSupport.getLock(key);
		synchronized ( aLock ) {
			element = cache.get(key);
			if ( element != null ) {
				result = element.getObjectValue();
				if ( compress ) {
					result = ObjectCompressUtil.uncompress((byte[]) result);
				}
				return result;
			}
			result = invoker.invoke();
			if ( result != null ) {
				if ( compress ) {
					element = new Element(key, ObjectCompressUtil.compress(result));
				} else {
					element = new Element(key, result);

				}
				cache.put(element);
			}
		}
		return result;
	}


	@Override
	public void removeAll( String table ) {
		Cache cache = getCache(table);
		cache.removeAll();
	}


	private Cache getCache( String table_key ) {
		Cache cache = null;
		String cacheName = "table_cache:" + table_key;
		if ( !manager.cacheExists(cacheName) ) {
			cache = new Cache(cacheName, 100000, false, true, 0, 0);
			manager.addCacheIfAbsent(cache);

		} else {
			cache = manager.getCache(cacheName);
		}
		return cache;
	}

}
