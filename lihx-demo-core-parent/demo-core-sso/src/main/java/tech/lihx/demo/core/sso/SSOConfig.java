package tech.lihx.demo.core.sso;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.common.util.PropertiesUtil;
import tech.lihx.demo.core.sso.common.util.ReflectUtil;
import tech.lihx.demo.core.sso.exception.KissoException;

/**
 * SSO 配置文件解析
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-12
 */
public class SSOConfig {

	private final static Logger logger = LoggerFactory.getLogger(SSOConfig.class);

	private static PropertiesUtil prop = null;


	/**
	 * SSO 资源文件初始化
	 */
	public static void init( Properties props ) {
		if ( props != null ) {
			prop = new PropertiesUtil(props);
			logger.info("loading sso config.");
			// 初始化反射配置
			ReflectUtil.init();
		} else {
			throw new KissoException(" cannot load sso config. ");
		}
	}


	/**
	 * PropertiesUtil
	 */
	public static PropertiesUtil getSSOProperties() {
		return prop;
	}


	/**
	 * 编码格式默认 UTF-8
	 */
	public static String getEncoding() {
		if ( prop == null ) { return SSOConstant.ENCODING; }
		return prop.get("sso.encoding", SSOConstant.ENCODING);
	}


	/**
	 * 密钥
	 */
	public static String getSecretKey() {
		return prop.get("sso.secretkey", SSOConstant.SSO_SECRET_KEY);
	}


	/**
	 * Cookie 只允许https协议传输
	 */
	public static boolean getCookieSecure() {
		return prop.getBoolean("sso.cookie.secure", SSOConstant.SSO_COOKIE_SECURE);
	}


	/**
	 * Cookie 只读,不允许 Js访问
	 */
	public static boolean getCookieHttponly() {
		return prop.getBoolean("sso.cookie.httponly", SSOConstant.SSO_COOKIE_HTTPONLY);
	}


	/**
	 * Cookie 超时时间
	 */
	public static int getCookieMaxage() {
		return prop.getInt("sso.cookie.maxage", SSOConstant.SSO_COOKIE_MAXAGE);
	}


	/**
	 * Cookie 名称
	 */
	public static String getCookieName() {
		return prop.get("sso.cookie.name", SSOConstant.SSO_COOKIE_NAME);
	}


	/**
	 * Cookie 所在域
	 */
	public static String getCookieDomain() {
		return prop.get("sso.cookie.domain", SSOConstant.SSO_COOKIE_DOMAIN);
	}


	/**
	 * Cookie 域路径
	 */
	public static String getCookiePath() {
		return prop.get("sso.cookie.path", SSOConstant.SSO_COOKIE_PATH);
	}


	/**
	 * Cookie 开启浏览器版本校验
	 */
	public static boolean getCookieBrowser() {
		return prop.getBoolean("sso.cookie.browser", SSOConstant.SSO_COOKIE_BROWSER);
	}


	/**
	 * Cookie 开启IP校验
	 */
	public static boolean getCookieCheckip() {
		return prop.getBoolean("sso.cookie.checkip", SSOConstant.SSO_COOKIE_CHECKIP);
	}


	/**
	 * Cookie 开启缓存 Token
	 */
	public static boolean getCookieCache() {
		return prop.getBoolean("sso.cookie.cache", SSOConstant.SSO_COOKIE_CACHE);
	}


	/**
	 * 自定义Encrypt Class
	 */
	public static String getEncryptClass() {
		return prop.get("sso.encrypt.class", SSOConstant.SSO_ENCRYPT_CLASS);
	}


	/**
	 * 自定义Token Class
	 */
	public static String getTokenClass() {
		return prop.get("sso.token.class", SSOConstant.SSO_TOKEN_CLASS);
	}


	/**
	 * 自定义TokenCache Class
	 */
	public static String getTokenCacheClass() {
		return prop.get("sso.tokencache.class", SSOConstant.SSO_TOKENCACHE_CLASS);
	}


	/**
	 * SSO 登录地址
	 */
	public static String getLoginUrl() {
		return prop.get("sso.login.url", SSOConstant.SSO_LOGIN_URL);
	}


	/**
	 * SSO 退出地址
	 */
	public static String getLogoutUrl() {
		return prop.get("sso.logout.url", SSOConstant.SSO_LOGOUT_URL);
	}


	/**
	 * SSO 登录超时时间
	 */
	public static int getLoginLoseTime() {
		if ( prop == null ) { return SSOConstant.SSO_LOGIN_LOSETIME; }
		return prop.getInt("sso.login.losetime", SSOConstant.SSO_LOGIN_LOSETIME);
	}


	/**
	 * 跨域信任 Cookie 名称
	 */
	public static String getAuthCookieName() {
		return prop.get("sso.crossdomain.cookie.name", SSOConstant.SSO_AUTH_COOKIE_NAME);
	}


	/**
	 * 跨域信任 Cookie 超时时间
	 */
	public static int getAuthCookieMaxage() {
		return prop.getInt("sso.crossdomain.cookie.maxage", SSOConstant.SSO_AUTH_COOKIE_MAXAGE);
	}
}
