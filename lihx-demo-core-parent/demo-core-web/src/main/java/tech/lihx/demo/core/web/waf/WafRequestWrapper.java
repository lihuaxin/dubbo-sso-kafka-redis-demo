package tech.lihx.demo.core.web.waf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.exception.WebException;

/**
 * Request请求过滤包装
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class WafRequestWrapper extends HttpServletRequestWrapper {

	private boolean filterXSS = true;

	private boolean filterSQL = true;

	private final boolean json;

	protected static Logger logger = LoggerFactory.getLogger(WafRequestWrapper.class);


	public WafRequestWrapper( HttpServletRequest request, boolean filterXSS, boolean filterSQL, boolean json ) {
		super(request);
		this.filterXSS = filterXSS;
		this.filterSQL = filterSQL;
		this.json = json;
	}


	/**
	 * @Description 数组参数过滤
	 * @param parameter
	 *            过滤参数
	 * @return
	 */
	@Override
	public String[] getParameterValues( String parameter ) {
		String[] values = super.getParameterValues(parameter);
		if ( values == null ) { return null; }

		int count = values.length;
		String[] encodedValues = new String[count];
		for ( int i = 0 ; i < count ; i++ ) {
			encodedValues[i] = filterParamString(parameter, values[i]);
		}

		return encodedValues;
	}


	@Override
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Map getParameterMap() {
		Map<String, String[]> primary = super.getParameterMap();
		Map<String, String[]> result = new HashMap<String, String[]>(primary.size());
		for ( Map.Entry<String, String[]> entry : primary.entrySet() ) {
			// entry.setValue(filterParamString(entry.getValue()));
			result.put(entry.getKey(), filterParamString(entry.getKey(), entry.getValue()));
		}
		return result;

	}


	/**
	 * @Description 参数过滤
	 * @param parameter
	 *            过滤参数
	 * @return
	 */
	@Override
	public String getParameter( String parameter ) {
		return filterParamString(parameter, super.getParameter(parameter));
	}


	/**
	 * @Description 过滤字符串内容
	 * @param rawValue
	 *            待处理内容
	 * @return
	 */
	protected String filterParamString( String parameter, String rawValue ) {
		if ( rawValue == null ) { return null; }
		String tmpStr = rawValue;
		if ( this.filterXSS ) {
			tmpStr = XSS.strip(tmpStr);
		}
		if ( this.filterSQL ) {
			tmpStr = tmpStr.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
		}
		// 如果是app端返回json,且有非法字符
		if ( json && !rawValue.equals(tmpStr) ) {
			if ( "token".equals(parameter) ) { return tmpStr; }
			logger.error(parameter + ":" + rawValue + ":您提交的数据有非法字符:" + tmpStr);
			throw new WebException(CodeConstants.PARAM_ERROR, "您提交的数据有非法字符");
		}
		return tmpStr;
	}


	protected String[] filterParamString( String parameter, String[] rawValue ) {
		for ( int i = 0 ; i < rawValue.length ; i++ ) {
			rawValue[i] = filterParamString(parameter, rawValue[i]);
		}
		return rawValue;
	}


	public static void main( String[] args ) {
		String tmpStr = "";
		tmpStr = XSS.strip(tmpStr);
		tmpStr = tmpStr.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
	}
}
