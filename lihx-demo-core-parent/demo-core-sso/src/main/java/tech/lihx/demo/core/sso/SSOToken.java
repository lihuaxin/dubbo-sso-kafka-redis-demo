package tech.lihx.demo.core.sso;

import javax.servlet.http.HttpServletRequest;

import tech.lihx.demo.core.sso.common.IpHelper;

/**
 * SSO登录标记Cookie基本信息对象
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
@SuppressWarnings( "serial" )
public class SSOToken extends Token {

	private long userId;// 用户 ID

	private String loginType;// 登录类型 0、普通登录

	private int cacheType;// 缓存状态 0、正常 1、宕机

	private long loginTime;// 登录时间


	public SSOToken() {
	}


	public SSOToken( HttpServletRequest request ) {
		setUserIp(IpHelper.getIpAddr(request));
		this.loginType = "0";
		this.loginTime = System.currentTimeMillis();
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId( long userId ) {
		this.userId = userId;
	}


	public String getLoginType() {
		return loginType;
	}


	public void setLoginType( String loginType ) {
		this.loginType = loginType;
	}


	public int getCacheType() {
		return cacheType;
	}


	public void setCacheType( int cacheType ) {
		this.cacheType = cacheType;
	}


	public long getLoginTime() {
		return loginTime;
	}


	public void setLoginTime( long loginTime ) {
		this.loginTime = loginTime;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.userId).append("_");
		sb.append(this.loginTime);
		return sb.toString();
	}
}
