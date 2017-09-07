package tech.lihx.demo.core.web.captcha;


import java.io.Serializable;


/**
 * 验证码对象
 * <p>
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class CheckCode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 验证票据
	 */
	private String token;

	/**
	 * 验证随机数
	 */
	private String range;

	/**
	 * 验证码内容
	 */
	private String code;


	public CheckCode() {

	}


	public CheckCode( String token, String range, String code ) {
		this.token = token;
		this.range = range;
		this.code = code;
	}


	public String getToken() {
		return token;
	}


	public void setToken( String token ) {
		this.token = token;
	}


	public String getRange() {
		return range;
	}


	public void setRange( String range ) {
		this.range = range;
	}


	public String getCode() {
		return code;
	}


	public void setCode( String code ) {
		this.code = code;
	}

}
