package tech.lihx.demo.core.sso;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.sso.common.IpHelper;
import tech.lihx.demo.core.sso.common.encrypt.RSA;
import tech.lihx.demo.core.sso.common.util.RandomUtil;
import tech.lihx.demo.core.sso.exception.KissoException;

/**
 * SSO 跨域信任 Token
 * <p>
 * 
 * @author LHX
 * @Date 2016-06-27
 */
public class AuthToken extends Token {

	private static final Logger logger = LoggerFactory.getLogger(AuthToken.class);

	private static final long serialVersionUID = 1L;

	private String userId;// 用户ID

	private String uuid;// 32 uuid

	private String randomId;// random number

	private String rsaSign;// ras sign


	@SuppressWarnings( "unused" )
	private AuthToken() {
	}


	/**
	 * 有参构造函数
	 * 
	 * @param request
	 * @param privateKey
	 *            RSA 密钥
	 */
	public AuthToken( HttpServletRequest request, String privateKey ) {
		uuid = RandomUtil.get32UUID();
		setUserIp(IpHelper.getIpAddr(request));
		sign(privateKey);
	}


	/**
	 * 生成签名字节数组
	 * 
	 * @return byte[]
	 */
	public byte[] signByte() {
		StringBuffer sb = new StringBuffer(getUuid());
		sb.append("-").append(getUserIp());
		return sb.toString().getBytes();
	}


	/**
	 * 设置签名 rsaSign
	 * 
	 * @param privateKey
	 *            RSA 密钥
	 */
	public void sign( String privateKey ) {
		try {
			rsaSign = RSA.sign(signByte(), privateKey);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new KissoException(" AuthToken RSA sign error. ");
		}
	}


	/**
	 * 验证 AuthToken 签名是否合法
	 * 
	 * @param publicKey
	 *            RSA 公钥
	 * @return
	 */
	public AuthToken verify( String publicKey ) {
		try {
			/**
			 * RSA 验证摘要 是否合法
			 */
			if ( RSA.verify(signByte(), publicKey, getRsaSign()) ) { return this; }
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new KissoException(" AuthToken RSA verify error. ");
		}
		return null;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId( String userId ) {
		this.userId = userId;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid( String uuid ) {
		this.uuid = uuid;
	}


	public String getRandomId() {
		return randomId;
	}


	public void setRandomId( String randomId ) {
		this.randomId = randomId;
	}


	public String getRsaSign() {
		return rsaSign;
	}


	public void setRsaSign( String rsaSign ) {
		this.rsaSign = rsaSign;
	}
}
