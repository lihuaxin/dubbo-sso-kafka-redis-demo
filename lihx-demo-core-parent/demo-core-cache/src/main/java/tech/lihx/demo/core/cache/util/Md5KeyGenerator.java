package tech.lihx.demo.core.cache.util;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import tech.lihx.demo.core.common.util.CacheKeyPrefix;

public class Md5KeyGenerator implements KeyGenerator {

	private static final Logger logger = LoggerFactory.getLogger(Md5KeyGenerator.class);

	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	private static ThreadLocal<MessageDigest> messageDigestThreadLocal = new ThreadLocal<MessageDigest>() {

		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch ( NoSuchAlgorithmException e ) {
				throw new RuntimeException(e);
			}
		}
	};


	@Override
	public Object generate( Object target, Method method, Object... params ) {
		return toString(method, params);
	}


	private String toString( Method m, Object... params ) {
		StringBuilder sb = HashCodeKeyGenerator.getString(m.getDeclaringClass().getName(), m.toString(), params);
		return new StringBuilder(CacheKeyPrefix.get()).append(MD5(sb.toString())).toString();
	}


	public static String getKey( String className, String methodName, Object... objs ) {
		StringBuilder sb = HashCodeKeyGenerator.getString(className, methodName, objs);
		return new StringBuilder(CacheKeyPrefix.get()).append(MD5(sb.toString())).toString();
	}


	public final static String MD5( String s ) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = messageDigestThreadLocal.get();
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for ( int i = 0 ; i < j ; i++ ) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}


	public static void main( String[] args ) {
		logger.debug(MD5("000000"));
		logger.debug(MD5("123"));
		logger.debug(MD5("123"));
	}

}
