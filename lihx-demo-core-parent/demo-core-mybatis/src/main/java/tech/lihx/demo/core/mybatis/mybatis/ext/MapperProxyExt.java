package tech.lihx.demo.core.mybatis.mybatis.ext;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;

import tech.lihx.demo.core.mybatis.mybatis.PagingInterceptor;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInterceptor;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInvocation;

/**
 * @author lihx
 * @version 2017-9-5
 */
public class MapperProxyExt<T> implements InvocationHandler, Serializable {

	private static final long serialVersionUID = -6424540398559729838L;

	private final SqlSession sqlSession;

	private final Class<T> mapperInterface;

	private final Map<Method, MapperMethod> methodCache;

	private final MyBatisInterceptor[] interceptors;

	private final static ThreadLocal<MyBatisInvocation> myBatisInvocationHolder = new ThreadLocal<MyBatisInvocation>();


	public MapperProxyExt(
			SqlSession sqlSession,
			Class<T> mapperInterface,
			Map<Method, MapperMethod> methodCache,
			MyBatisInterceptor[] interceptors ) {
		this.sqlSession = sqlSession;
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
		this.interceptors = interceptors;
	}


	@Override
	public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
		if ( Object.class.equals(method.getDeclaringClass()) ) { return method.invoke(this, args); }
		try {

			MyBatisInvocation invocation = new MyBatisInvocation(
					mapperInterface, sqlSession, method, args, interceptors, methodCache);
			myBatisInvocationHolder.set(invocation);
			return invocation.execute();
		} finally {
			myBatisInvocationHolder.remove();
			PagingInterceptor.removePage();
		}

	}


	public static MyBatisInvocation getMyBatisInvocation() {
		return myBatisInvocationHolder.get();
	}

}
