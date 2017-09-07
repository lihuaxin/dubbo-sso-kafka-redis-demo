package tech.lihx.demo.core.sso.common.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.SSOConfig;
import tech.lihx.demo.core.sso.common.util.Base64Util;

/**
 * AES encrypt util
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class AES extends Encrypt {

	private static final Logger logger = LoggerFactory.getLogger(AES.class);

	private static final String ALGORITHM = "AES";

	SecretKeySpec secretKey;


	public AES() {
	}


	public AES( String str ) {
		setKey(str);// generate secret key
	}


	public SecretKey getSecretKey() {
		return secretKey;
	}


	/**
	 * generate KEY
	 */
	public void setKey( String strKey ) {
		try {
			byte[] bk = MD5.md5Raw(strKey.getBytes(SSOConfig.getEncoding()));
			this.secretKey = new SecretKeySpec(bk, ALGORITHM);
		} catch ( Exception e ) {
			logger.error("Encrypt setKey is exception:", e.getMessage());
		}
	}


	/**
	 * @Description AES encrypt
	 * @param str
	 * @return
	 */
	public String encryptAES( String str ) {
		byte[] encryptBytes = null;
		String encryptStr = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
			encryptBytes = cipher.doFinal(str.getBytes());
			if ( encryptBytes != null ) {
				encryptStr = Base64Util.encryptBASE64(encryptBytes);
			}
		} catch ( Exception e ) {
			logger.error("Encrypt encryptAES is exception:", e.getMessage());
		}
		return encryptStr;
	}


	/**
	 * @Description AES decrypt
	 * @param str
	 * @return
	 */
	public String decryptAES( String str ) {
		byte[] decryptBytes = null;
		String decryptStr = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
			byte[] scrBytes = Base64Util.decryptBASE64(str);
			decryptBytes = cipher.doFinal(scrBytes);
		} catch ( Exception e ) {
			logger.error("Encrypt decryptAES is exception:", e.getMessage());
		}
		if ( decryptBytes != null ) {
			decryptStr = new String(decryptBytes);
		}
		return decryptStr;
	}


	/**
	 * AES encrypt
	 */
	@Override
	public String encrypt( String value, String key ) throws Exception {
		setKey(key);
		return encryptAES(value);
	}


	/**
	 * AES decrypt
	 */
	@Override
	public String decrypt( String value, String key ) throws Exception {
		setKey(key);
		return decryptAES(value);

	}


	/**
	 * test
	 */
	public static void main( String[] args ) {
		String password = "100010\n1w#E#测试\nssAASASSC\n127.0.0.1\nlif123gsjkdsgvjxeh\n";
		AES en = new AES("lifgnfdfg216958134gsjkdsgvjxeh");
		String encryptPwd = en.encryptAES(password);
		logger.debug(encryptPwd);
		String decryptPwd = en.decryptAES(encryptPwd);
		logger.debug(decryptPwd);
	}
}
