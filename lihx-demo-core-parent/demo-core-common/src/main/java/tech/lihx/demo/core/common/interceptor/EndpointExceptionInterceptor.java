package tech.lihx.demo.core.common.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.exception.EndpointException;
import tech.lihx.demo.core.common.exception.WebException;
import tech.lihx.demo.core.common.response.Response;
import tech.lihx.demo.core.common.response.ResponseHead;
import tech.lihx.demo.core.common.service.AbstractRequest;


/**
 * Endpoint层的拦截器
 * <p>
 * 自动处理参数校验和业务异常
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class EndpointExceptionInterceptor extends AbstractRequest implements MethodInterceptor {

	private static Logger logger = LoggerFactory.getLogger(EndpointExceptionInterceptor.class);


	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {

		Object obj = null;
		try {
			before(invocation);
			obj = invocation.proceed();
		} catch ( Exception ex ) {
			handleException(ex, invocation);
		}
		return obj;
	}


	/**
	 * 执行前校验
	 * <p>
	 * 
	 * 1.校验请求简单参数
	 * <p>
	 * 2.校验返回类型
	 * 
	 */
	public void before( final MethodInvocation invocation ) throws Exception {
		// 校验参数
		checkParameter(invocation.getArguments(), new EndpointException());

		// 校验返回数据类型
		boolean isResponse = (Response.class == invocation.getMethod().getReturnType());
		if ( !isResponse ) {
			logger.error(
				"您的Endpoint层返回值不是Response,请立刻马上修改返回值类型!谢谢:{}.{}", invocation.getThis().getClass().getName(), invocation
						.getMethod().getName());
			throw new EndpointException(CodeConstants.EXCEPTION, "您调用的Endpoint层返回值不是Response,请联系后端人员修改返回值类型，谢谢！");
		}
	}


	/**
	 * 处理业务异常
	 * 
	 * @param ex
	 *            业务层抛出的异常
	 * @param invocation
	 *            实际应该执行的对象
	 */
	private Response<Object> handleException( final Exception ex, final MethodInvocation invocation ) throws Throwable {
		// 业务异常处理
		String msg = ex.getMessage();
		if ( ex instanceof EndpointException ) {
			EndpointException sex = (EndpointException) ex;
			return new Response<Object>(sex.getCode(), msg, null);
		} else if ( ex instanceof WebException ) {
			WebException sex = (WebException) ex;
			return new Response<Object>(sex.getCode(), msg, null);
		} else {
			logger.error("模块层出现异常:", ex);
			Object[] params = invocation.getArguments();
			send(ex, "模块层异常", null, params);
			ResponseHead head = new ResponseHead(CodeConstants.EXCEPTION, "系统异常");
			return new Response<Object>(head, null);
		}
	}

}
