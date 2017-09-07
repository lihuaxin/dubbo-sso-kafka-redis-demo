package tech.lihx.demo.core.support.optlog;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.method.HandlerMethod;

/**
 * 存储日志接口
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:15:04
 */
public interface OptLog {

	// 请求开始时调用
	public void preHandle( HttpServletRequest request, HandlerMethod hm );


	// 请求结束时调用
	public void afterCompletion( HttpServletRequest request, HandlerMethod hm, Exception ex );
}
