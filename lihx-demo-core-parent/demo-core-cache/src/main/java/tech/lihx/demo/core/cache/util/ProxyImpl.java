package tech.lihx.demo.core.cache.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyImpl implements InvocationHandler {

	private final Object obj;

	private final Logger logger;


	public ProxyImpl( Object obj ) {
		this.obj = obj;
		logger = LoggerFactory.getLogger(obj.getClass());
	}


	@Override
	public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
		try {
			return method.invoke(obj, args);
		} catch ( Throwable ex ) {
			if ( ex instanceof SQLException ) { throw ex; }
			logger.error("缓存异常", ex);
			// 出现异常,如果有数据库访问
			if ( args != null ) {
				for ( int i = 0 ; i < args.length ; i++ ) {
					if ( args[i] instanceof Invoker ) { return ((Invoker) args[i]).invoke(); }
				}
			}
		}
		return null;
	}
}
