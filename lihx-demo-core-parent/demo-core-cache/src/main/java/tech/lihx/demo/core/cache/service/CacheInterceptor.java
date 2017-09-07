package tech.lihx.demo.core.cache.service;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import tech.lihx.demo.core.cache.support.CacheOperationContext;
import tech.lihx.demo.core.cache.util.Invoker;
import tech.lihx.demo.core.cache.util.ThrowableWrapper;

public class CacheInterceptor extends ServiceCacheAspectSupport implements MethodInterceptor, Serializable {

	private static final long serialVersionUID = 7591792052835724545L;


	@Override
	public Object invoke( final MethodInvocation invocation ) throws Throwable {
		Method method = invocation.getMethod();
		Invoker aopAllianceInvoker = new Invoker() {

			@Override
			public Object invoke() {
				try {
					return invocation.proceed();
				} catch ( Throwable ex ) {
					throw new ThrowableWrapper(ex);
				}
			}
		};

		try {
			return execute(aopAllianceInvoker, method, invocation.getArguments());
		} catch ( ThrowableWrapper th ) {
			throw th.original;
		} finally {
			CacheOperationContext.removeCache();
		}
	}

}
