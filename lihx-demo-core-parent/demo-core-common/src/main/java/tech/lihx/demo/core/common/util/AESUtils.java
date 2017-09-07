package tech.lihx.demo.core.common.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESUtils {

	private static final Logger logger = LoggerFactory.getLogger(AESUtils.class);

	final static String KEY = "~!@#$%^&*()QWERTYUIOPASDFGHJKLZXCVBNM<>";

	final static SecretKey SECRET_KEY;
	static {
		SECRET_KEY = getKey(KEY);
	}


	/**
	 * 加密
	 *
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encryptData( byte[] byteContent ) {
		try {
			byte[] enCodeFormat = SECRET_KEY.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}


	/**
	 * 解密
	 *
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decryptData( byte[] content ) {
		try {
			byte[] enCodeFormat = SECRET_KEY.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}


	public static SecretKey getKey( String strKey ) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(strKey.getBytes());
			_generator.init(128, secureRandom);
			return _generator.generateKey();
		} catch ( Exception e ) {
			throw new RuntimeException(" 初始化密钥出现异常 ");
		}
	}

}
