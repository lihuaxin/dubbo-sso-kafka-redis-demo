package tech.lihx.demo.core.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * 
 */
public class InvocationUtils {

	// 存放当前线程所处理的请求对象
	private final static ThreadLocal<HttpServletRequest> currentRequests = new ThreadLocal<HttpServletRequest>();


	//
	public static void bindInvocationToRequest( Invocation inv, HttpServletRequest request ) {
		request.setAttribute(CoreFilter.INVOCATION_KEY, inv);
	}


	public static void unbindInvocationFromRequest( HttpServletRequest request ) {
		request.removeAttribute(CoreFilter.INVOCATION_KEY);
	}


	/**
	 * 获取请求对象设置的Invocation对象
	 */
	public static Invocation getInvocation( HttpServletRequest request ) {
		if ( request == null ) { return null; }
		return (Invocation) request.getAttribute(CoreFilter.INVOCATION_KEY);
	}


	public static void unindRequestFromCurrentThread() {
		currentRequests.remove();
	}


	public static void bindRequestToCurrentThread( HttpServletRequest request ) {
		if ( request == null ) {
			unindRequestFromCurrentThread();
		} else {
			currentRequests.set(request);
		}
	}


	/**
	 * 获取当前线程的请求对象
	 */
	public static HttpServletRequest getCurrentThreadRequest() {
		HttpServletRequest request = currentRequests.get();
		if ( request != null ) { return request; }
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}


	public static Invocation getCurrentThreadInvocation() {
		return getInvocation(getCurrentThreadRequest());
	}
}
