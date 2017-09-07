/**
 * SSOAuthenticationToken.java cn.vko.support.test.service.shiro Copyright (c)
 * 2014, 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.support.privilege.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 基于cookie的验证
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:16:12
 */
@SuppressWarnings( "serial" )
public class SSOAuthToken implements AuthenticationToken {

	/** 用户id */
	private final Long userId;

	private final String token;


	/**
	 * 用于验证是否登录的cookie名称 和所有的cookie
	 * 
	 * @param cookieName
	 * @param cookies
	 */
	public SSOAuthToken( Long userId, String token ) {
		this.userId = userId;
		this.token = token;
	}


	@Override
	public Object getPrincipal() {
		return userId;
	}


	@Override
	public Object getCredentials() {
		return token;
	}


}
