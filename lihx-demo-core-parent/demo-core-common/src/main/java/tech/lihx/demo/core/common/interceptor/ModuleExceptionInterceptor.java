package tech.lihx.demo.core.common.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.code.CodeConstants;
import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.exception.ModuleException;
import tech.lihx.demo.core.common.exception.WebException;
import tech.lihx.demo.core.common.response.Response;
import tech.lihx.demo.core.common.response.ResponseHead;
import tech.lihx.demo.core.common.service.AbstractRequest;


/**
 * module层的拦截器
 * <p>
 * 自动处理参数校验和业务异常
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class ModuleExceptionInterceptor extends AbstractRequest implements MethodInterceptor {

	private static Logger logger = LoggerFactory.getLogger(ModuleExceptionInterceptor.class);


	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		Object obj = null;
		try {
			before(invocation);
			obj = invocation.proceed();
		} catch ( Exception ex ) {
			return handleException(ex, invocation);
		}
		return obj;
	}


	/**
	 * 方法调用前执行
	 * <p>
	 * 1.先校验参数是否为空
	 * <p>
	 * 2.判断返回数据类型
	 * 
	 * @param invocation
	 *            实际对象
	 */
	private void before( MethodInvocation invocation ) throws Exception {

		// 校验参数是否为空
		checkParameter(invocation.getArguments(), new ModuleException());

		// 校验返回对象类型
		boolean isResponse = (Response.class == invocation.getMethod().getReturnType());
		if ( !isResponse && !EnvironmentDetect.detectEnvironment().isProduct() ) {
			logger.error(
				"您的Module层返回值不是Response,请立刻马上修改返回值类型!谢谢:{}.{}", invocation.getThis().getClass().getName(), invocation
						.getMethod().getName());

			throw new ModuleException(CodeConstants.EXCEPTION, "调用的Module层返回值不是Response,请立刻马上其修改返回值类型，谢谢!");
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
		if ( ex instanceof ModuleException ) {
			ModuleException sex = (ModuleException) ex;
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
