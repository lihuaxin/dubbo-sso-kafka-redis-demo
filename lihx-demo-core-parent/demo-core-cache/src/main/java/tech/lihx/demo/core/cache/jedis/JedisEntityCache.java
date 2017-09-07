package tech.lihx.demo.core.cache.jedis;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import tech.lihx.demo.core.cache.interfaces.EntityCache;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.ProxyImpl;
import tech.lihx.demo.core.common.util.SerializeUtil;
import tech.lihx.demo.core.support.mq.Handler;

/**
 * tedis实现实体缓存
 * <p>
 * 
 * @author lihx
 * @Date 2014年9月16日
 */
public class JedisEntityCache extends JedisBase implements EntityCache, FactoryBean<EntityCache> {

	@Override
	public Object get( String table, Object id, Invoker invoker, boolean compress ) {
		String lockKey = new StringBuffer(table).append(":").append(id).toString();
		byte[] key = encode(lockKey);
		Object value = getValue(key, compress);
		if ( value != null ) { return value; }
		// 获取锁
		if ( lock(lockKey, 2000L) ) {
			value = getValue(key, compress);
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
				getCommands().set(key, serializeCompress(value));
				unlock(lockKey);
				return value;
			}
			getCommands().set(key, SerializeUtil.fastSerialize(value));
			unlock(lockKey);
			return value;
		}
		return invoker.invoke();
	}


	private Object getValue( byte[] key, boolean compress ) {
		byte[] value = getCommands().get(key);
		if ( value != null ) {
			if ( compress ) { return this.uncompressDeserialize(value); }
			return SerializeUtil.fastDeserialize(value);
		}
		return null;
	}


	@Override
	public void remove( String table, Object id ) {
		String lockKey = new StringBuffer(table).append(":").append(id).toString();
		byte[] key = encode(lockKey);
		if ( mqService == null ) {
			getCommands().del(key);
			return;
		}
		mqService.putData(key, handler);
	}

	private final Handler<byte[]> handler = new Handler<byte[]>() {

		@Override
		public void handle( byte[] data ) {
			getCommands().del(data);
		}
	};


	@Override
	public EntityCache getObject() throws Exception {
		EntityCache cacheProxy = (EntityCache) Proxy.newProxyInstance(
			EntityCache.class.getClassLoader(), new Class<?>[ ] { EntityCache.class }, new ProxyImpl(this));
		return cacheProxy;
	}


	@Override
	public Class<?> getObjectType() {
		return EntityCache.class;
	}


	@Override
	public boolean isSingleton() {
		return true;
	}

}
