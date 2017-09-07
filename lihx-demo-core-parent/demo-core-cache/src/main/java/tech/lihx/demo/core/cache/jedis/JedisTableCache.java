package tech.lihx.demo.core.cache.jedis;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import tech.lihx.demo.core.cache.interfaces.TableCache;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.ProxyImpl;
import tech.lihx.demo.core.common.util.SerializeUtil;
import tech.lihx.demo.core.support.mq.Handler;

/**
 * <p>
 * 
 * @author lihx
 * @Date 2014年9月16日
 */
public class JedisTableCache extends JedisBase implements TableCache, FactoryBean<TableCache> {

	@Override
	public Object get( String table, String key, Invoker invoker, boolean compress ) {
		byte[] encodeTable = encode(table);
		byte[] encodeKey = encode(key);
		String lockKey = new StringBuffer(table).append(":").append(key).toString();
		Object value = getValue(encodeTable, encodeKey, compress);
		if ( value != null ) { return value; }
		// 获取锁
		if ( lock(lockKey, 2000L) ) {
			value = getValue(encodeTable, encodeKey, compress);
			if ( value != null ) {
				unlock(lockKey);
				return value;
			}
			value = invoker.invoke();
			if ( value == null ) {
				unlock(lockKey);
				return null;
			}
			if ( compress ) {
				getCommands().hset(encodeTable, encodeKey, serializeCompress(value));
				unlock(lockKey);
				return value;
			}
			getCommands().hset(encodeTable, encodeKey, SerializeUtil.fastSerialize(value));
			unlock(lockKey);
			return value;
		}
		return invoker.invoke();
	}


	private Object getValue( byte[] encodeTable, byte[] encodeKey, boolean compress ) {
		byte[] value = getCommands().hget(encodeTable, encodeKey);
		if ( value != null ) {
			if ( compress ) { return this.uncompressDeserialize(value); }
			return SerializeUtil.fastDeserialize(value);
		}
		return null;
	}


	@Override
	public void removeAll( final String table ) {
		byte[] key = encode(table);
		if ( mqService == null ) {
			getCommands().del(key);
			return;
		}
		mqService.putData(key, handler);
	}

	Handler<byte[]> handler = new Handler<byte[]>() {

		@Override
		public void handle( byte[] data ) {
			getCommands().del(data);
		}
	};


	@Override
	public TableCache getObject() throws Exception {
		TableCache cacheProxy = (TableCache) Proxy.newProxyInstance(
			TableCache.class.getClassLoader(), new Class<?>[ ] { TableCache.class }, new ProxyImpl(this));
		return cacheProxy;
	}


	@Override
	public Class<?> getObjectType() {
		return TableCache.class;
	}


	@Override
	public boolean isSingleton() {
		return true;
	}

}
