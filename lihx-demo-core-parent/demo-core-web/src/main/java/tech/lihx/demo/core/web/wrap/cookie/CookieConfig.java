package tech.lihx.demo.core.web.wrap.cookie;

public class CookieConfig {

	private String name;

	private String value;

	private String comment;

	private String domain;

	// 默认会话时间
	private int maxAge = -1;

	private String path = "/";

	private boolean secure = false;

	private int version = 0;

	private boolean isHttpOnly = false;

	// session过期时间
	private int sessionExpireTime = 30 * 60;

	// cookie字节,分割长度
	private long sizeToSplit = 4000;

	// 允许的cookie名字和session名字
	private String[] allowCookieNames;

	private String[] allowSessionNames;


	public int getSessionExpireTime() {
		return sessionExpireTime;
	}


	public long getSizeToSplit() {
		return sizeToSplit;
	}


	public void setSizeToSplit( long sizeToSplit ) {
		this.sizeToSplit = sizeToSplit;
	}


	public void setSessionExpireTime( int sessionExpireTime ) {
		this.sessionExpireTime = sessionExpireTime;
	}


	public String getComment() {
		return comment;
	}


	public void setComment( String comment ) {
		this.comment = comment;
	}


	public String getDomain() {
		return domain;
	}


	public void setDomain( String domain ) {
		this.domain = domain;
	}


	public boolean isHttpOnly() {
		return isHttpOnly;
	}


	public void setHttpOnly( boolean isHttpOnly ) {
		this.isHttpOnly = isHttpOnly;
	}


	public int getMaxAge() {
		return maxAge;
	}


	public void setMaxAge( int maxAge ) {
		this.maxAge = maxAge;
	}


	public String getName() {
		return name;
	}


	public void setName( String name ) {
		this.name = name;
	}


	public String getPath() {
		return path;
	}


	public void setPath( String path ) {
		this.path = path;
	}


	public boolean isSecure() {
		return secure;
	}


	public void setSecure( boolean secure ) {
		this.secure = secure;
	}


	public String getValue() {
		return value;
	}


	public void setValue( String value ) {
		this.value = value;
	}


	public int getVersion() {
		return version;
	}


	public void setVersion( int version ) {
		this.version = version;
	}


	public String[] getAllowCookieNames() {
		return allowCookieNames;
	}


	public void setAllowCookieNames( String[] allowCookieNames ) {
		this.allowCookieNames = allowCookieNames;
	}


	public String[] getAllowSessionNames() {
		return allowSessionNames;
	}


	public void setAllowSessionNames( String[] allowSessionNames ) {
		this.allowSessionNames = allowSessionNames;
	}

}
