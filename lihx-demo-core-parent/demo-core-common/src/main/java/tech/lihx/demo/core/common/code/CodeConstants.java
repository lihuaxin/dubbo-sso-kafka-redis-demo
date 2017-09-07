package tech.lihx.demo.core.common.code;


/**
 * 这里定义所有的返回异常的代码
 * 
 * @author lihx
 * @date 2015-11-22
 * @version 1.0.0
 */
public class CodeConstants {

	// 正确返回数据时的应答码
	public static String SUCCESS = "000000";

	// 异常时返回的应答码
	public static String EXCEPTION = "000001";

	// 参数校验未通过时的应答码
	public static String PARAM_ERROR = "000002";

	// 校验登陆信息异常时的应答码
	public static String AUTH_ERROR = "000003";

	// 无权限访问时的应答码
	public static String NO_PERMISSION = "000004";

}
