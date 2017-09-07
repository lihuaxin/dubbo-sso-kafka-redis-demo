package tech.lihx.demo.core.cache.jedis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import tech.lihx.demo.core.common.util.Lz4Compress;
import tech.lihx.demo.core.common.util.SerializeUtil;
import tech.lihx.demo.core.support.mq.MQService;

/**
 * 父类
 * <p>
 * 
 * @author lihx
 * @Date 2014年9月16日
 */
public class JedisBase implements InitializingBean {

	protected RedisCommands commands;

	protected MQService mqService;


	public MQService getMqService() {
		return mqService;
	}


	public void setMqService( MQService mqService ) {
		this.mqService = mqService;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(commands, "commands不能为null");
	}


	public RedisCommands getCommands() {
		return commands;
	}


	public void setCommands( RedisCommands commands ) {
		this.commands = commands;
	}


	protected byte[] encode( String value ) {
		if ( value == null ) { return null; }
		return value.getBytes();
	}


	protected String decode( byte[] value ) {
		if ( value == null ) { return null; }
		return new String(value);
	}


	protected byte[] serializeCompress( Object obj ) {
		if ( obj == null ) { return null; }
		byte[] value = SerializeUtil.fastSerialize(obj);
		return Lz4Compress.compress(value);
	}


	@SuppressWarnings( "unchecked" )
	protected <T> T uncompressDeserialize( byte[] value ) {
		if ( value == null ) { return null; }
		return (T) SerializeUtil.fastDeserialize(Lz4Compress.uncompress(value));
	}


	protected boolean lock( Object key, Long expireMsecs ) {
		String lockKey = generateLockKey(key);
		long expires = System.currentTimeMillis() + expireMsecs + 1;
		String expiresStr = String.valueOf(expires);
		boolean locked = false;
		int timeout = 1000;
		while ( timeout >= 0 ) {
			if ( getCommands().setnx(lockKey, expiresStr) == 1 ) {
				locked = true;
				return locked;
			}
			String currentValueStr = getCommands().get(lockKey);
			if ( currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis() ) {
				// lock is expired
				String oldValueStr = getCommands().getSet(lockKey, expiresStr);
				if ( oldValueStr != null && oldValueStr.equals(currentValueStr) ) {
					// lock acquired
					locked = true;
					return locked;
				}
			}
			timeout -= 100;
			try {
				Thread.sleep(100);
			} catch ( InterruptedException e ) {
				return false;
			}
		}
		return locked;
	}


	protected void unlock( Object key ) {
		getCommands().del(encode(generateLockKey(key)));
	}


	private String generateLockKey( Object key ) {
		if ( null == key ) { throw new IllegalArgumentException("key must not be null"); }
		return key.toString() + ".lock";
	}
}
