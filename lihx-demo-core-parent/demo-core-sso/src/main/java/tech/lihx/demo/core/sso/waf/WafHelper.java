package tech.lihx.demo.core.sso.waf;

import tech.lihx.demo.core.sso.waf.attack.SqlInjection;
import tech.lihx.demo.core.sso.waf.attack.XSS;

/**
 * Web防火墙工具类
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class WafHelper {

	/**
	 * @Description 过滤XSS脚本内容
	 * @param value
	 *            待处理内容
	 * @return
	 */
	public static String stripXSS( String value ) {
		if ( value == null ) { return null; }

		return XSS.strip(value);
	}


	/**
	 * @Description 过滤SQL注入内容
	 * @param value
	 *            待处理内容
	 * @return
	 */
	public static String stripSqlInjection( String value ) {
		if ( value == null ) { return null; }

		return SqlInjection.strip(value);
	}


	/**
	 * @Description 过滤SQL/XSS注入内容
	 * @param value
	 *            待处理内容
	 * @return
	 */
	public static String stripSqlXSS( String value ) {
		if ( value == null ) { return null; }

		return XSS.strip(SqlInjection.strip(value));
	}

}
