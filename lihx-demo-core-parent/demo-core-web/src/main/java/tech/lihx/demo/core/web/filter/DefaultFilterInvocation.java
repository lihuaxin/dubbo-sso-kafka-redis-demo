package tech.lihx.demo.core.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import tech.lihx.demo.core.web.filter.interceptor.FilterInterceptor;

public final class DefaultFilterInvocation implements FilterInvocation {

	HttpServletRequest request;

	HttpServletResponse response;

	final FilterChain chain;

	final FilterInterceptor[] interceptor;

	final ServletContext context;

	int index = 0;


	public DefaultFilterInvocation(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			FilterInterceptor[] interceptor,
			ServletContext context ) {
		this.request = request;
		this.response = response;
		this.chain = chain;
		this.interceptor = interceptor;
		this.context = context;
	}


	@Override
	public void doInterceptor() throws IOException, ServletException {
		if ( interceptor == null ) {
			skipInterceptor();
			return;
		}
		if ( interceptor.length > index ) {
			interceptor[index++].invoke(this);
		} else {
			skipInterceptor();
		}

	}


	@Override
	public void skipInterceptor() throws IOException, ServletException {
		// chain.doFilter(wrappedFilter.getWrappedRequest(),
		// wrappedFilter.getWrappedResponse());
		// return;
		// for (int i = 0, size = wrappedFilterList.size(); i < size; i++) {
		// wrappedFilterList.get(i).getWrappedRequest();
		// }
		chain.doFilter(request, response);
	}


	@Override
	public HttpServletRequest getRequest() {
		return request;
	}


	@Override
	public HttpServletResponse getResponse() {
		return response;
	}


	@Override
	public ServletContext getServletContext() {
		return context;
	}


	@Override
	public void setRequest( HttpServletRequest request ) {
		this.request = request;
	}


	@Override
	public void setResponse( HttpServletResponse response ) {
		this.response = response;
	}


	@Override
	public String getParameter( String name ) {

		return this.request.getParameter(name);

	}


	@Override
	public Invocation setAttribute( String name, Object value ) {
		this.request.setAttribute(name, value);
		return this;

	}


	@Override
	public Object getAttribute( String name ) {
		return this.request.getAttribute(name);

	}


	@Override
	public void removeAttribute( String name ) {
		this.request.removeAttribute(name);

	}


	@Override
	public WebApplicationContext getApplicationContext() {

		return WebApplicationContextUtils.getWebApplicationContext(context);

	}


	@Override
	public HttpSession getSession() {

		return this.request.getSession();

	}
}
