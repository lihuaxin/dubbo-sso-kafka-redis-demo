package tech.lihx.demo.core.web.cache;


/**
 * redis 缓存控制器
 * <p>
 * 
 * @author LHX
 * @date 2015年1月4日
 * @version v1.0.0
 */
public abstract class RedisHandler<K, V> {

	/**
	 * 查询参数
	 */
	private K[] param;


	public K[] getParam() {
		return param;
	}


	@SuppressWarnings( "unchecked" )
	public void setParam( K... param ) {
		this.param = param;
	}


	@SuppressWarnings( "unchecked" )
	public RedisHandler( K... param ) {
		this.param = param;
	}


	/**
	 * 执行方法
	 */
	public abstract V load();
}
