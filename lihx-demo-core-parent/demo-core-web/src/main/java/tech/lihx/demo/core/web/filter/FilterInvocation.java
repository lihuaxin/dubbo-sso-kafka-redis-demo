package tech.lihx.demo.core.web.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FilterInvocation extends Invocation {

	public void doInterceptor() throws IOException, ServletException;


	public void skipInterceptor() throws IOException, ServletException;


	public void setRequest( HttpServletRequest request );


	public void setResponse( HttpServletResponse response );

}
