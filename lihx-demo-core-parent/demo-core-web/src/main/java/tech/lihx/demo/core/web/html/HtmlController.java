package tech.lihx.demo.core.web.html;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HtmlController {

	/**
	 * 持久化html内容
	 */
	public void storeHtml( HttpServletRequest request, ResponseHtml responseHtml ) throws IOException, ServletException;


	/**
	 * 从持久化中获取html并写入
	 */
	public boolean writeHtml( HttpServletRequest request, HttpServletResponse response ) throws IOException,
		ServletException;
}
