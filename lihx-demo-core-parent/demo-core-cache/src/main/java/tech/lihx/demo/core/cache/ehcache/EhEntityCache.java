package tech.lihx.demo.core.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import tech.lihx.demo.core.cache.interfaces.EntityCache;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.LockSupport;
import tech.lihx.demo.core.common.util.ObjectCompressUtil;

/**
 * ehcache实现的实体缓存
 * <p>
 * 
 * @author lihx
 * @Date 2014-7-9
 */
public class EhEntityCache implements EntityCache {

	private CacheManager manager;


	private String getkey( String table_key, Object id ) {
		StringBuilder key = new StringBuilder(table_key);
		key.append("#");
		key.append(id.toString());
		return key.toString();
	}


	@Override
	public Object get( String table_key, Object id, Invoker invoker, boolean compress ) {
		String key = getkey(table_key, id);
		Cache cache = getCache(table_key);
		Object result = null;
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
					element = new Element(key, ObjectCompressUtil.compress(result), true, null, null);
				} else {
					element = new Element(key, result, true, null, null);
				}
				// 永久有效,直到被删除
				cache.put(element);
			}
		}
		return result;

	}


	@Override
	public void remove( String table_key, Object id ) {
		Cache cache = getCache(table_key);
		cache.remove(getkey(table_key, id));
	}


	private Cache getCache( String table_key ) {
		Cache cache = null;
		String cacheName = "entity_cache:" + table_key;
		if ( !manager.cacheExists(cacheName) ) {
			cache = new Cache(cacheName, 100000, false, true, 0, 0);
			manager.addCacheIfAbsent(cache);

		} else {
			cache = manager.getCache(cacheName);
		}
		return cache;
	}


	public CacheManager getManager() {
		return manager;
	}


	public void setManager( CacheManager manager ) {
		this.manager = manager;
	}

}
