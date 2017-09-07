package tech.lihx.demo.core.sso.client;


/**
 * redis 缓存状态
 * 
 * @author LHX
 * @date 2017-9-6
 * @version 1.0.0
 */
public enum CacheType {

	NORMAL(0, "redis缓存正常"), SHUTDOWN(1, "redis缓存宕机");

	private final int type;

	private final String desc;


	CacheType( final int type, final String desc ) {
		this.type = type;
		this.desc = desc;
	}


	public int getType() {
		return this.type;
	}


	public String getDesc() {
		return this.desc;
	}
}
