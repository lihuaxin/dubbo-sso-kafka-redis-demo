package tech.lihx.demo.core.web.html;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemoryHtmlController implements HtmlController {

	private final Map<String, String> cache = new HashMap<String, String>();


	@Override
	public void storeHtml( HttpServletRequest request, ResponseHtml responseHtml ) throws IOException, ServletException {
		if ( HtmlFlag.getFlag() != null && HtmlFlag.getFlag().isCache() ) {
			String uri = request.getRequestURI();
			String param = request.getQueryString();
			String html = responseHtml.getHtml();
			if ( html != null ) {
				cache.put(uri + param, html);
			}
		}
	}


	@Override
	public boolean writeHtml( HttpServletRequest request, HttpServletResponse response ) throws IOException,
		ServletException {
		String uri = request.getRequestURI();
		String param = request.getQueryString();
		String html = cache.get(uri + param);
		if ( html != null ) {
			response.setContentType(HtmlFlag.getFlag().getContentType());
			response.getWriter().write(html);
			return true;
		}
		return false;
	}

}
