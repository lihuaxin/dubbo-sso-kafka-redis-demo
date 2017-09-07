package tech.lihx.demo.core.common.web;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 序列化到缓存系统的对象封装
 * <p>
 * 
 * @author lihx
 * @date 2014年11月21日
 * @version 1.0.0
 */
@SuppressWarnings( "serial" )
public class SessionStore implements Serializable {


	private Map<String, Object> data = new HashMap<String, Object>();

	private String sessionId;

	private Date creationTime = new Date();


	public SessionStore( String sessionId ) {
		this.sessionId = sessionId;
	}


	public Map<String, Object> getData() {
		return data;
	}


	public void setData( Map<String, Object> data ) {
		this.data = data;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId( String sessionId ) {
		this.sessionId = sessionId;
	}


	public Date getCreationTime() {
		return creationTime;
	}


	public void setCreationTime( Date creationTime ) {
		this.creationTime = creationTime;
	}


}
