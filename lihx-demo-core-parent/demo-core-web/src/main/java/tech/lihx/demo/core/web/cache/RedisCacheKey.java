package tech.lihx.demo.core.web.cache;

/**
 * redis 缓存 key
 * <p>
 * 
 * @author LHX
 * @date 2015年1月4日
 * @version v1.0.0
 */
public class RedisCacheKey {

	/**
	 * IKey 缓存主键相关配置
	 */
	public enum IKey {
		USER("user_", 3600, "用户缓存"), // 用户缓存
		SCHOOL_DOMAIN("school_domain_", 3600, "学校域名信息查询缓存"), // 学校域名信息查询缓存
		APP_VERSION("app_version_", 600, "app版本号缓存");// app版本号缓存

		private final String key;// 缓存主键

		private final int seconds;// 缓存时间单位（秒）

		private final String desc;// 描述


		IKey( final String key, final int seconds, final String desc ) {
			this.key = key;
			this.seconds = seconds;
			this.desc = desc;
		}


		public String getKey() {
			return this.key;
		}


		public int getSeconds() {
			return this.seconds;
		}


		public String getDesc() {
			return this.desc;
		}
	}


	private String key;

	/**
	 * 缓存时间单位（秒）
	 */
	private int seconds;

	private IKey iKey;


	protected RedisCacheKey() {
	}


	public RedisCacheKey( String randStr, IKey iKey ) {
		this(randStr, 0, iKey);
	}


	public RedisCacheKey( Long randStr, IKey iKey ) {
		this(String.valueOf(randStr), 0, iKey);
	}


	public RedisCacheKey( String randStr, int seconds, IKey iKey ) {
		StringBuffer _key = new StringBuffer("ck_");
		_key.append(iKey.getKey());
		_key.append(randStr);
		this.key = _key.toString();
		this.seconds = seconds;
		this.iKey = iKey;
	}


	public String getKey() {
		return key;
	}


	public void setKey( String key ) {
		this.key = key;
	}


	public int getSeconds() {
		return seconds;
	}


	public void setSeconds( int seconds ) {
		this.seconds = seconds;
	}


	public IKey getiKey() {
		return iKey;
	}


	public void setiKey( IKey iKey ) {
		this.iKey = iKey;
	}

}
