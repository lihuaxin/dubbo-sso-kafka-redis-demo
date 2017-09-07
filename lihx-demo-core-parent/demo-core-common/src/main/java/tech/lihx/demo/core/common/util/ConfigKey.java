package tech.lihx.demo.core.common.util;


/**
 * 配置的关键key
 * 
 * @author LHX
 * @Date 2015-11-8
 */
public enum ConfigKey {
	STATIC, LOGIN_PAGE, LOGOUT_URL, LOGOUT_TO, SSO_COOKIE_KEY, SSO_COOKIE_DOMAIN;

	public String key() {
		return name().toLowerCase();
	}


	public String value() {
		return name().toLowerCase();
	}
}
