package tech.lihx.demo.core.web.filter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author LHX
 * 
 */
public class InvocationLocalImpl implements InvocationLocal {

	@Override
	public Invocation getCurrent( boolean required ) {
		Invocation inv = InvocationUtils.getInvocation(InvocationUtils.getCurrentThreadRequest());
		if ( inv == null && required ) { throw new IllegalStateException("please install \"Invocation\" Object!"); }
		return inv;
	}


	private Invocation required() {
		return getCurrent(true);
	}


	@Override
	public WebApplicationContext getApplicationContext() {
		return required().getApplicationContext();
	}


	@Override
	public Object getAttribute( String name ) {
		return required().getAttribute(name);
	}


	@Override
	public String getParameter( String name ) {
		return required().getParameter(name);
	}


	@Override
	public HttpServletRequest getRequest() {
		return required().getRequest();
	}


	@Override
	public HttpServletResponse getResponse() {
		return required().getResponse();
	}


	@Override
	public ServletContext getServletContext() {
		return required().getServletContext();
	}


	@Override
	public void removeAttribute( String name ) {
		required().removeAttribute(name);

	}


	@Override
	public Invocation setAttribute( String name, Object value ) {
		return required().setAttribute(name, value);
	}


	@Override
	public HttpSession getSession() {
		return required().getRequest().getSession();

	}

}
