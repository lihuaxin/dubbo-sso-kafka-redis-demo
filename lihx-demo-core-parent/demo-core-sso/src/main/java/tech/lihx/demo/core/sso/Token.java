package tech.lihx.demo.core.sso;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * SSO票据顶级父类
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-9
 */
@SuppressWarnings( "serial" )
public class Token implements Serializable {

	/**
	 * 登录 IP
	 */
	private String userIp;


	/**
	 * Token转为JSON格式
	 * <p>
	 * 
	 * @return JSON格式Token值
	 */
	public String jsonToken() {
		return JSON.toJSONString(this);
	}


	/**
	 * JSON格式Token值转为Token对象
	 * <p>
	 * 
	 * @param jsonToken
	 *            JSON格式Token值
	 * @return Token对象
	 */
	public Token parseToken( String jsonToken ) {
		return JSON.parseObject(jsonToken, this.getClass());
	}


	public String getUserIp() {
		return userIp;
	}


	public void setUserIp( String userIp ) {
		this.userIp = userIp;
	}
}
