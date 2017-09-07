package tech.lihx.demo.core.cache.support;

import org.springframework.util.Assert;

import tech.lihx.demo.core.cache.annotation.Cacheable;

public class CacheOperation {

	private String key;

	private int type = Cacheable.EXPIRE;

	private int expire = 5 * 60;

	private String table;

	private boolean evict = false;

	private boolean compress = false;


	public int getExpire() {
		return expire;
	}


	public int getType() {
		return type;
	}


	public void setType( int type ) {
		this.type = type;
	}


	public void setExpire( int expire ) {
		this.expire = expire;
	}


	public String getTable() {
		return table;
	}


	public void setTable( String table ) {
		this.table = table;
	}


	public boolean isCompress() {
		return compress;
	}


	public void setCompress( boolean compress ) {
		this.compress = compress;
	}


	public String getKey() {
		return key;
	}


	public void setKey( String key ) {
		Assert.notNull(key);
		this.key = key;
	}


	public boolean isEvict() {
		return evict;
	}


	public void setEvict( boolean evict ) {
		this.evict = evict;
	}


	@Override
	public boolean equals( Object other ) {
		return other instanceof CacheOperation && toString().equals(other.toString());
	}


	@Override
	public int hashCode() {
		return toString().hashCode();
	}


	@Override
	public String toString() {
		return getOperationDescription().toString();
	}


	protected StringBuilder getOperationDescription() {
		StringBuilder result = new StringBuilder();
		result.append(getClass().getSimpleName());
		result.append("[");
		result.append("] key=");
		result.append(key);
		result.append("' | id='");
		result.append("table_key:");
		result.append(table);
		result.append("evict:");
		result.append(evict);
		return result;
	}
}
