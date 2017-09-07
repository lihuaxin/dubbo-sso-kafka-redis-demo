/**
 * RequestDataUtil.java cn.vko.core.web.util Copyright (c) 2015,
 * 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.web.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;


/**
 * web请求工具类
 * <p>
 * 
 * @author LHX
 * @date 2016-3-11
 * @version 1.0.0
 */
public class RequestDataUtil {

	/**
	 * 取得请求中的body数据
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestData( HttpServletRequest request ) {
		// 从body中读取流数据
		StringBuilder sb = new StringBuilder();

		try {
			String line = null;

			Charset cs = Charset.forName("UTF-8");
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), cs));

			while ( (line = br.readLine()) != null ) {
				sb.append(line);
				sb.append("\n");
			}

			BufferedReader bufReader = request.getReader();
			while ( (line = bufReader.readLine()) != null ) {
				sb.append(line);
				sb.append("\n");

			}

		} catch ( Exception e ) {

		}

		return sb.toString();
	}

}
