package tech.lihx.demo.core.mybatis.mybatis;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import tech.lihx.demo.core.mybatis.mybatis.annotation.Batch;
import tech.lihx.demo.core.mybatis.mybatis.ext.MapperProxyExt;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInvocation;

/**
 * 批量处理，如果遇见Batch注解 批量查询,批量更新 参数是list或数组
 */
@Intercepts( {
		@Signature( type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class } ),
		@Signature( type = Executor.class, method = "update", args = { MappedStatement.class, Object.class } ) } )
public class BatchInterceptor implements Interceptor {

	@Override
	public Object intercept( Invocation invocation ) throws Throwable {
		MyBatisInvocation inv = MapperProxyExt.getMyBatisInvocation();
		Batch batch = inv.getMethod().getAnnotation(Batch.class);
		if ( batch != null ) {
			Object[] args = inv.getArgs();
			Iterator<Object> iterator = getIterator(args);
			if ( iterator == null ) { return invocation.proceed(); }
			// 对list进行批量处理
			if ( "update".equals(invocation.getMethod().getName()) ) {
				return update(invocation, iterator, inv);
			} else {
				// 查询
				return query(invocation, iterator, inv);
			}
		}
		return invocation.proceed();

	}


	private Iterator<Object> getIterator( Object[] args ) {
		Object parameter = null;
		if ( args != null && args.length == 1 ) {
			parameter = args[0];
		}
		if ( parameter != null && parameter instanceof List ) {
			return new IteratorImpl<Object>((List<?>) parameter);
		} else if ( parameter != null && parameter.getClass().isArray() ) { return new IteratorImpl<Object>(parameter); }
		return null;
	}


	private int update( Invocation invocation, Iterator<Object> iter, MyBatisInvocation inv ) throws Throwable {
		Object statment = invocation.getArgs()[0];
		Executor target = (Executor) invocation.getTarget();
		Object[] param = new Object[ ] { statment, null };
		Method method = invocation.getMethod();
		int count = 0;
		while ( iter.hasNext() ) {
			count++;
			inv.batchIndexIncrease();
			param[1] = iter.next();
			method.invoke(target, param);
			if ( count % 100 == 0 ) {
				target.flushStatements();
			}

		}
		target.flushStatements();
		return count;
	}


	@SuppressWarnings( { "unchecked", "rawtypes" } )
	private List<Object> query( Invocation invocation, Iterator<Object> iter, MyBatisInvocation inv ) throws Throwable {
		Object statment = invocation.getArgs()[0];
		Object rowBounds = invocation.getArgs()[2];
		Object resultHandler = invocation.getArgs()[3];
		Object target = invocation.getTarget();
		Object[] param = new Object[ ] { statment, null, rowBounds, resultHandler };
		Method method = invocation.getMethod();
		List<Object> resultList = new ArrayList<Object>();
		while ( iter.hasNext() ) {
			inv.batchIndexIncrease();
			param[1] = iter.next();
			Object result = method.invoke(target, param);
			if ( result instanceof Collection ) {
				resultList.addAll((Collection) result);
			} else {
				resultList.add(result);

			}
		}
		return resultList;
	}


	@Override
	public Object plugin( Object target ) {
		// if (target instanceof Executor) {
		// Executor exe = (Executor) target;
		// MyBatisInvocation invocation = MapperProxyExt.getMyBatisInvocation();
		// Executor newtarget = BatchExecutorExt.wrap(exe,
		// invocation.getConfiguration());
		// return Plugin.wrap(newtarget, this);
		// }

		return Plugin.wrap(target, this);
	}


	@Override
	public void setProperties( Properties properties ) {
	}

	private class IteratorImpl<E> implements Iterator<Object> {

		private boolean isArray = false;

		private final Object data;

		private int index = 0;

		private int size = 0;


		public IteratorImpl( List<?> list ) {
			data = list;
			size = list.size();
		}


		public IteratorImpl( Object array ) {
			isArray = true;
			data = array;
			size = Array.getLength(data);
		}


		@Override
		public boolean hasNext() {
			return index < size;

		}


		@Override
		public Object next() {
			if ( isArray ) {
				Object obj = Array.get(data, index);
				index++;
				return obj;
			} else {
				List<?> list = (List<?>) data;
				Object obj = list.get(index);
				index++;
				return obj;
			}
		}


		@Override
		public void remove() {
			throw new RuntimeException("not support");
		}

	}
}
