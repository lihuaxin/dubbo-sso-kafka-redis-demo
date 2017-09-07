package tech.lihx.demo.core.web.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseHtmlWrapper extends HttpServletResponseWrapper implements ResponseHtml {

	private ServletOutputStream stream;

	private StringWriter htmlWriter;


	public ResponseHtmlWrapper( HttpServletResponse response ) {
		super(response);
	}


	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if ( stream != null ) { return stream; }
		stream = super.getOutputStream();
		return stream;
	}


	@Override
	public PrintWriter getWriter() throws IOException {
		htmlWriter = new StringWriter(1000);
		return new PrintWriter(htmlWriter);
	}


	@Override
	public String getHtml() {
		if ( htmlWriter != null ) { return htmlWriter.toString(); }
		return null;
	}
}
