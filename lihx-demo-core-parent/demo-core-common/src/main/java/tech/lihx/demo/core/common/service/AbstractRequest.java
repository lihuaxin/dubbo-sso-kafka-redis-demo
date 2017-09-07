package tech.lihx.demo.core.common.service;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.exception.EndpointException;
import tech.lihx.demo.core.common.exception.ModuleException;
import tech.lihx.demo.core.common.exception.support.MailSupport;


/**
 * 默认的请求校验接口实现类
 * <p>
 * 实现接口中定义的校验方法
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class AbstractRequest extends MailSupport implements Request {

	/**
	 * 简单的判断传入的参数是否为空的方法
	 * <p>
	 * 目前支持String、Long、Integer、Array、Collection、Map的校验，可自行扩展类型。
	 * 
	 * @param obj
	 *            要判断的对象
	 * 
	 * @return 空true、非空false
	 */
	@Override
	@SuppressWarnings( "null" )
	public boolean isEmpty( final Object obj ) {
		if ( obj instanceof String ) { return "".equals(String.valueOf(obj).trim()); }
		if ( obj instanceof Long ) { return (null == obj || ((Long) obj) < 0) ? true : false; }
		if ( obj instanceof Integer ) { return (null == obj || ((Integer) obj) < 0) ? true : false; }
		if ( obj.getClass().isArray() ) { return Array.getLength(obj) == 0; }
		if ( obj instanceof Collection<?> ) { return ((Collection<?>) obj).isEmpty(); }
		if ( obj instanceof Map<?, ?> ) { return ((Map<?, ?>) obj).isEmpty(); }
		return false;
	}


	/**
	 * 对传入的参数判断是否为空
	 * <p>
	 * 参数若为空则抛出指定异常类型
	 * 
	 * @param obj
	 *            待判断参数
	 * 
	 * @param ex
	 *            待抛异常类型
	 */
	@Override
	public void checkParameter( final Object[] obj, final Exception ex ) {
		if ( isEmpty(obj) ) {
			if ( ex instanceof ModuleException ) { throw new ModuleException(
					CodeConstants.PARAM_ERROR, "参数组中有必要参数为空，请检查！"); }
			if ( ex instanceof EndpointException ) { throw new EndpointException(
					CodeConstants.PARAM_ERROR, "参数组中有必要参数为空，请检查！"); }
		}
	}
}
