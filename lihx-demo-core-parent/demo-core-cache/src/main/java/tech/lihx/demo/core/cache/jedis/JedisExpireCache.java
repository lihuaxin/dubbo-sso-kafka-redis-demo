package tech.lihx.demo.core.cache.jedis;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import tech.lihx.demo.core.cache.interfaces.ExpireCache;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.ProxyImpl;
import tech.lihx.demo.core.common.util.SerializeUtil;

/**
 * <p>
 * 
 * @author lihx
 * @Date 2014年9月16日
 */
public class JedisExpireCache extends JedisBase implements ExpireCache, FactoryBean<ExpireCache> {

	@Override
	public Object get( String key, Invoker invoker, int expire, boolean compress ) {
		byte[] encodeKey = encode(key);
		Object value = getValue(encodeKey, compress);
		if ( value != null ) { return value; }
		if ( lock(key, 2000L) ) {
			value = getValue(encodeKey, compress);
			if ( value != null ) {
				unlock(key);
				return value;
			}
			value = invoker.invoke();
			if ( value == null ) {
				unlock(key);
				return null;
			}
			if ( compress ) {
				getCommands().set(encodeKey, serializeCompress(value));
				if ( expire > 0 ) {
					getCommands().expire(encodeKey, expire);
				}
				unlock(key);
				return value;
			}
			if ( expire > 0 ) {
				getCommands().setex(encodeKey, expire, SerializeUtil.fastSerialize(value));
			} else {
				getCommands().set(encodeKey, SerializeUtil.fastSerialize(value));
			}
			unlock(key);
			return value;
		}
		return invoker.invoke();
	}


	private Object getValue( byte[] encodeKey, boolean compress ) {
		byte[] value = getCommands().get(encodeKey);
		if ( value != null ) {
			if ( compress ) { return this.uncompressDeserialize(value); }
			return SerializeUtil.fastDeserialize(value);
		}
		return null;
	}


	// @Override
	// public void remove(String key) {
	// if (mqService == null) {
	// getCommands().del(encode(key));
	// return;
	// }
	// mqService.putData(key, handler);
	// }

	// Handler<String> handler = new Handler<String>() {
	// @Override
	// public void handle(String data) {
	// getCommands().del(encode(data));
	// }
	// };

	@Override
	public ExpireCache getObject() throws Exception {
		ExpireCache cacheProxy = (ExpireCache) Proxy.newProxyInstance(
			ExpireCache.class.getClassLoader(), new Class<?>[ ] { ExpireCache.class }, new ProxyImpl(this));
		return cacheProxy;
	}


	@Override
	public Class<?> getObjectType() {
		return ExpireCache.class;
	}


	@Override
	public boolean isSingleton() {
		return true;
	}
}
