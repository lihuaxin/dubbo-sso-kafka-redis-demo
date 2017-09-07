package tech.lihx.demo.core.sso.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.SSOConfig;
import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.Token;
import tech.lihx.demo.core.sso.TokenCache;
import tech.lihx.demo.core.sso.TokenCacheMap;
import tech.lihx.demo.core.sso.common.encrypt.AES;
import tech.lihx.demo.core.sso.common.encrypt.Encrypt;
import tech.lihx.demo.core.sso.exception.KissoException;

/**
 * 反射工具类
 * <p>
 * 
 * @author hubin
 * @Date 2014-6-27
 */
public class ReflectUtil {

	private final static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	private static Encrypt encrypt = null;

	private static TokenCache tokenCache = null;


	/**
	 * 反射初始化
	 */
	public static void init() {
		getConfigEncrypt();
		getConfigTokenCache();
	}


	/**
	 * 反射获取自定义Encrypt
	 * 
	 * @return
	 */
	public static Encrypt getConfigEncrypt() {

		if ( encrypt != null ) { return encrypt; }

		/**
		 * 判断是否自定义 Encrypt 默认 AES
		 */
		if ( "".equals(SSOConfig.getEncryptClass()) ) {
			encrypt = new AES();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getEncryptClass());
				try {
					if ( tc.newInstance() instanceof Encrypt ) {
						encrypt = (Encrypt) tc.newInstance();
					} else {
						throw new KissoException(SSOConfig.getEncryptClass() + " not instanceof Encrypt.");
					}
				} catch ( InstantiationException e ) {
					logger.error(e.getMessage(), e);
				} catch ( IllegalAccessException e ) {
					logger.error(e.getMessage(), e);
				}
			} catch ( ClassNotFoundException e ) {
				logger.error(e.getMessage(), e);
				logger.error("sso.encrypt.class. error..! " + SSOConfig.getEncryptClass());
			}
		}
		return encrypt;
	}


	/**
	 * 反射获取自定义Token
	 * 
	 * @return
	 */
	public static Token getConfigToken() {
		/**
		 * 判断是否自定义 Token 默认 SSOToken
		 */
		Token token = null;
		if ( "".equals(SSOConfig.getTokenClass()) ) {
			token = new SSOToken();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getTokenClass());
				try {
					if ( tc.newInstance() instanceof Token ) {
						token = (Token) tc.newInstance();
					} else {
						throw new KissoException(SSOConfig.getTokenClass() + " not instanceof Token.");
					}
				} catch ( InstantiationException e ) {
					logger.error(e.getMessage(), e);
				} catch ( IllegalAccessException e ) {
					logger.error(e.getMessage(), e);
				}
			} catch ( ClassNotFoundException e ) {
				logger.error(e.getMessage(), e);
				logger.error("sso.token.class. error..! " + SSOConfig.getTokenClass());
			}
		}
		return token;
	}


	/**
	 * 反射获取自定义TokenCache
	 * 
	 * @return
	 */
	public static TokenCache getConfigTokenCache() {

		if ( tokenCache != null ) { return tokenCache; }

		/**
		 * 判断是否自定义 TokenCache 默认 TokenCacheMap
		 */
		if ( "".equals(SSOConfig.getTokenCacheClass()) ) {
			tokenCache = new TokenCacheMap();
		} else {
			try {
				Class<?> tc = Class.forName(SSOConfig.getTokenCacheClass());
				try {
					if ( tc.newInstance() instanceof TokenCache ) {
						tokenCache = (TokenCache) tc.newInstance();
					} else {
						throw new KissoException(SSOConfig.getTokenCacheClass() + " not instanceof TokenCache.");
					}
				} catch ( InstantiationException e ) {
					logger.error(e.getMessage(), e);
				} catch ( IllegalAccessException e ) {
					logger.error(e.getMessage(), e);
				}
			} catch ( ClassNotFoundException e ) {
				logger.error(e.getMessage(), e);
				logger.error("sso.tokencache.class. error..! " + SSOConfig.getTokenCacheClass());
			}
		}
		return tokenCache;
	}
}
