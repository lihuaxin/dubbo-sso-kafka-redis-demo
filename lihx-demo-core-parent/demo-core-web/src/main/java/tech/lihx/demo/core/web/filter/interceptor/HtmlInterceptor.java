package tech.lihx.demo.core.web.filter.interceptor;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tech.lihx.demo.core.web.filter.FilterInvocation;
import tech.lihx.demo.core.web.html.HtmlController;
import tech.lihx.demo.core.web.html.HtmlFlag;
import tech.lihx.demo.core.web.html.ResponseHtmlWrapper;

/**
 * 获取生成的静态html代码
 * 
 * @author LHX 
 * 
 */
public class HtmlInterceptor implements FilterInterceptor {

	HtmlController htmlController;


	@Override
	public void invoke( final FilterInvocation filterInvocation ) throws IOException, ServletException {
		HttpServletResponse response = filterInvocation.getResponse();
		HttpServletRequest request = filterInvocation.getRequest();
		ResponseHtmlWrapper wrapper = new ResponseHtmlWrapper(response);
		filterInvocation.setResponse(wrapper);
		try {
			if ( htmlController != null ) {
				// 从缓存获取写
				boolean success = htmlController.writeHtml(request, response);
				// 缓存没有或过期
				if ( !success ) {
					filterInvocation.doInterceptor();
					htmlController.storeHtml(request, wrapper);
				}
			} else {
				filterInvocation.doInterceptor();
			}
		} finally {
			HtmlFlag.removeFlag();
		}

	}


	public HtmlController getHtmlController() {
		return htmlController;
	}


	public void setHtmlController( HtmlController htmlController ) {
		this.htmlController = htmlController;
	}


	@Override
	public void init( FilterConfig filterConfig ) {

		// TODO Auto-generated method stub

	}


	@Override
	public void destroy() {

		// TODO Auto-generated method stub

	}

}
