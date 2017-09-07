package tech.lihx.demo.core.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * <pre>
 * 完成于&lt;jsp:include page''/>相同的功能
 * 用于include其它页面以用于布局,可以用于在freemarker,velocity的servlet环境应用中直接include其它http请求
 * </pre>
 * 
 * <br />
 * <b>Freemarker及Velocity示例使用:</b>
 * 
 * <pre>
 * ${httpInclude.include("http://www.google.com")};
 * ${httpInclude.include("/servlet/head?p1=v1&p2=v2")};
 * ${httpInclude.include("/head.jsp")};
 * ${httpInclude.include("/head.do?p1=v1&p2=v2")};
 * ${httpInclude.include("/head.htm")};
 * </pre>
 * 
 * @author LHX
 *
 */
public class HttpInclude {

	private HttpServletRequest requestLocal;

	private HttpServletResponse responseLocal;


	public HttpInclude( HttpServletRequest request, HttpServletResponse response ) {
		this.requestLocal = request;
		this.responseLocal = response;
	}


	public HttpInclude() {
	}


	public String include( String includePath ) {
		return include(includePath, requestLocal, responseLocal);
	}


	public String include( String includePath, HttpServletRequest request, HttpServletResponse response ) {
		try {
			StringWriter writer = new StringWriter(8192);
			getLocalContent(includePath, writer, request, response);
			return writer.toString();
		} catch ( Exception e ) {
			throw new RuntimeException("include error,path:" + includePath + " cause:" + e, e);
		}
	}


	private void getLocalContent(
			String includePath, Writer writer, HttpServletRequest request, HttpServletResponse response )
		throws ServletException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);

		CustomOutputHttpServletResponseWrapper customResponse = new CustomOutputHttpServletResponseWrapper(
				response, writer, outputStream);
		request.getRequestDispatcher(includePath).include(request, customResponse);

		customResponse.flushBuffer();
		if ( customResponse.useOutputStream ) {
			String encoding = response.getCharacterEncoding();
			if ( encoding == null ) {
				encoding = "utf-8";
			}
			writer.write(outputStream.toString(encoding));
		}
		writer.flush();
	}

	public static class CustomOutputHttpServletResponseWrapper extends HttpServletResponseWrapper {

		public boolean useWriter = false;

		public boolean useOutputStream = false;

		//
		private final PrintWriter printWriter;

		private final ServletOutputStream servletOutputStream;


		public CustomOutputHttpServletResponseWrapper(
				HttpServletResponse response,
				final Writer customWriter,
				final OutputStream customOutputStream ) {
			super(response);
			this.printWriter = new PrintWriter(customWriter);
			this.servletOutputStream = new ServletOutputStream() {

				@Override
				public void write( int b ) throws IOException {
					customOutputStream.write(b);
				}


				@Override
				public void write( byte[] b ) throws IOException {
					customOutputStream.write(b);
				}


				@Override
				public void write( byte[] b, int off, int len ) throws IOException {
					customOutputStream.write(b, off, len);
				}
			};
		}


		@Override
		public PrintWriter getWriter() throws IOException {
			if ( useOutputStream ) { throw new IllegalStateException(
					"getOutputStream() has already been called for this response"); }
			useWriter = true;
			return printWriter;
		}


		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if ( useWriter ) { throw new IllegalStateException("getWriter() has already been called for this response"); }
			useOutputStream = true;
			return servletOutputStream;
		}


		@Override
		public void flushBuffer() throws IOException {
			if ( useWriter ) {
				printWriter.flush();
			}
			if ( useOutputStream ) {
				servletOutputStream.flush();
			}
		}

	}

}
