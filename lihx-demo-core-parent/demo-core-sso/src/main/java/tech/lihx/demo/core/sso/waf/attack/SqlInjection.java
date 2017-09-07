package tech.lihx.demo.core.sso.waf.attack;

/**
 * SQL注入攻击
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-8
 */
public class SqlInjection {

	/**
	 * @Description SQL注入内容剥离
	 * @param value
	 *            待处理内容
	 * @return
	 */
	public static String strip( String value ) {

		// 剥离SQL注入部分代码
		return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
	}
}
