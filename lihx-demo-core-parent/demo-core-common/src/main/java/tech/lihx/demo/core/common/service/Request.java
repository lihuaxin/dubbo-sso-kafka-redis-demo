package tech.lihx.demo.core.common.service;


/**
 * 定义一个请求校验接口
 * <p>
 * 此接口提供校验请求参数的方法，详见下方定义
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public interface Request {

	/**
	 * 简单判断传入的请求参数是否为空
	 * 
	 * @param obj
	 *            待校验对象
	 * 
	 * @return 返回参数情况，空true，非空false
	 */
	boolean isEmpty( final Object obj );


	/**
	 * 对传入的参数进行校验
	 * <p>
	 * 若传入的参数校验未通过，则抛出异常
	 * 
	 * @param obj
	 *            待校验参数
	 * 
	 * @param ex
	 *            要抛出异常类型
	 */
	void checkParameter( final Object obj[], final Exception ex );

}
