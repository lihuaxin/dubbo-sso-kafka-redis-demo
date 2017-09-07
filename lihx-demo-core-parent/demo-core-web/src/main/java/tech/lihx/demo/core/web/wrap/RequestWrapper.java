package tech.lihx.demo.core.web.wrap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class RequestWrapper extends HttpServletRequestWrapper {

	private HttpSessionWrapper wrapper;

	private final ServletContext context;

	private final PersistenceSession session;


	public RequestWrapper( HttpServletRequest request, ServletContext context, PersistenceSession session ) {
		super(request);
		this.context = context;
		this.session = session;
	}


	@Override
	public HttpSession getSession() {
		if ( wrapper == null ) {
			wrapper = new HttpSessionWrapper(context, session);
			session.setWrappedSession(wrapper);
			session.init();
		}

		return wrapper;
	}


	@Override
	public HttpSession getSession( boolean create ) {
		return getSession();
	}

}
