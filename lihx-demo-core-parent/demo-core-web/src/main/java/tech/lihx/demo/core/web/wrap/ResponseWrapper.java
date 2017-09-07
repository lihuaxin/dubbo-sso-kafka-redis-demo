package tech.lihx.demo.core.web.wrap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {


	public ResponseWrapper( HttpServletResponse response ) {
		super(response);
	}


	// @Override
	// public void flushBuffer() throws IOException {
	// //disabled
	// throw new RuntimeException("不支持清空缓冲区!");
	// }
	@Override
	public void addCookie( Cookie cookie ) {
		if ( cookie == null ) { return; }
		// 检测是否允许添加cookies
		// if(){
		//
		// }
		super.addCookie(cookie);
	}
}
