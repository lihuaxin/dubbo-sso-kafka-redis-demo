/**
 * DbAuthRealm.java cn.vko.support.test.service.shiro Copyright (c) 2014,
 * 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.support.privilege.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:15:59
 */
public class SSOAuthRealm extends AuthorizingRealm {

	IFetchPermission permission;

	public static final ThreadLocal<String> currentToken = new ThreadLocal<String>();


	public SSOAuthRealm() {
		super(new AllowAllCredentialsMatcher());
	}


	/*
	 * 表示根据用户身份获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo( PrincipalCollection principals ) {
		if ( principals == null ) { throw new AuthorizationException(
				"PrincipalCollection method argument cannot be null."); }
		Long userId = (Long) getAvailablePrincipal(principals);
		// 根据userid查找相关信息
		// 根据用户信息查找对应权限信息,角色信息,和通配符信息,根据预先的定义,url和权限的关系
		// 存储角色信息
		List<String> roles = permission.fetchRoles(userId);
		List<String> permissions = permission.fetchPermissions(userId);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		if ( roles != null ) {
			authorizationInfo.addRoles(roles);
		}
		if ( permissions != null ) {
			authorizationInfo.addStringPermissions(permissions);
		}
		return authorizationInfo;

	}


	@Override
	public boolean supports( AuthenticationToken token ) {
		return token != null && SSOAuthToken.class.isAssignableFrom(token.getClass());
	}


	@Override
	public Class<?> getAuthenticationTokenClass() {
		return SSOAuthToken.class;
	}


	public IFetchPermission getPermission() {
		return permission;
	}


	public void setPermission( IFetchPermission permission ) {
		this.permission = permission;
	}


	@Override
	protected Object getAuthorizationCacheKey( PrincipalCollection principals ) {
		// Long userId = (Long) getAvailablePrincipal(principals);

		return currentToken.get();
	}


	/*
	 *  表示获取身份验证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token ) throws AuthenticationException {
		Long userId = (Long) token.getPrincipal();
		String tokenStr = (String) token.getCredentials();

		return new SimpleAuthenticationInfo(userId, tokenStr, getName());
	}
}
