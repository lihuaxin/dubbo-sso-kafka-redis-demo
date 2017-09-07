/**
 * ReadWriteSqlSessionTemplate.java com.jiajiao.core.mybatis.ext Copyright (c)
 * 2014, 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.mybatis.mybatis.ext;

import static java.lang.reflect.Proxy.*;
import static org.apache.ibatis.reflection.ExceptionUtil.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.mvel2.MVEL;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import tech.lihx.demo.core.mybatis.mybatis.annotation.Batch;
import tech.lihx.demo.core.mybatis.mybatis.annotation.Write;
import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInvocation;
import tech.lihx.demo.core.mybatis.mybatis.rw.IDataSourceService;

/**
 * 读写分离实现
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5
 */
public class ReadWriteSqlSessionTemplate extends SqlSessionTemplate {

	protected final SqlSession sqlSessionProxy;

	protected IDataSourceService dataSourceService;


	public ReadWriteSqlSessionTemplate( SqlSessionFactory sqlSessionFactory ) {
		this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}


	public ReadWriteSqlSessionTemplate( SqlSessionFactory sqlSessionFactory, ExecutorType executorType ) {
		this(sqlSessionFactory, executorType, new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration()
				.getEnvironment().getDataSource(), true));
	}


	@SuppressWarnings( "synthetic-access" )
	public ReadWriteSqlSessionTemplate(
			SqlSessionFactory sqlSessionFactory,
			ExecutorType executorType,
			PersistenceExceptionTranslator exceptionTranslator ) {
		super(sqlSessionFactory, executorType, exceptionTranslator);
		this.sqlSessionProxy = (SqlSession) newProxyInstance(
			SqlSessionFactory.class.getClassLoader(), new Class[ ] { SqlSession.class }, new SqlSessionInterceptor());
	}


	public IDataSourceService getDataSourceService() {
		return dataSourceService;
	}


	public void setDataSourceService( IDataSourceService dataSourceService ) {
		this.dataSourceService = dataSourceService;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T selectOne( String statement ) {
		return this.sqlSessionProxy.<T> selectOne(statement);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T selectOne( String statement, Object parameter ) {
		return this.sqlSessionProxy.<T> selectOne(statement, parameter);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap( String statement, String mapKey ) {
		return this.sqlSessionProxy.<K, V> selectMap(statement, mapKey);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap( String statement, Object parameter, String mapKey ) {
		return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V> Map<K, V> selectMap( String statement, Object parameter, String mapKey, RowBounds rowBounds ) {
		return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey, rowBounds);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList( String statement ) {
		return this.sqlSessionProxy.<E> selectList(statement);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList( String statement, Object parameter ) {
		return this.sqlSessionProxy.<E> selectList(statement, parameter);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> List<E> selectList( String statement, Object parameter, RowBounds rowBounds ) {
		return this.sqlSessionProxy.<E> selectList(statement, parameter, rowBounds);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select( String statement, ResultHandler handler ) {
		this.sqlSessionProxy.select(statement, handler);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select( String statement, Object parameter, ResultHandler handler ) {
		this.sqlSessionProxy.select(statement, parameter, handler);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void select( String statement, Object parameter, RowBounds rowBounds, ResultHandler handler ) {
		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert( String statement ) {
		return this.sqlSessionProxy.insert(statement);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insert( String statement, Object parameter ) {
		return this.sqlSessionProxy.insert(statement, parameter);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update( String statement ) {
		return this.sqlSessionProxy.update(statement);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update( String statement, Object parameter ) {
		return this.sqlSessionProxy.update(statement, parameter);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete( String statement ) {
		return this.sqlSessionProxy.delete(statement);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int delete( String statement, Object parameter ) {
		return this.sqlSessionProxy.delete(statement, parameter);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearCache() {
		this.sqlSessionProxy.clearCache();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection getConnection() {
		return this.sqlSessionProxy.getConnection();
	}


	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.2
	 *
	 */
	@Override
	public List<BatchResult> flushStatements() {
		return this.sqlSessionProxy.flushStatements();
	}


	protected SqlSession getSqlSession( String methodName ) {
		MyBatisInvocation inv = MapperProxyExt.getMyBatisInvocation();
		if ( dataSourceService == null ) { return getDefaultSqlSession(inv); }
		List<DataSource> readDS = dataSourceService.getReadDataSources();
		List<DataSource> writeDS = dataSourceService.getWriteDataSources();
		if ( methodName.startsWith("select") ) {
			Write write = inv.getMethod().getAnnotation(Write.class);
			if ( write != null ) {
				Boolean bool = (Boolean) MVEL.eval(write.value(), inv.getParamsMap());
				// 读取的时候要求从写库读取
				if ( bool && !writeDS.isEmpty() ) { return getSqlSession(inv, writeDS.get(0)); }
			}
			if ( !readDS.isEmpty() ) { return getSqlSession(inv, readDS.get(0)); }
		} else {
			if ( !writeDS.isEmpty() ) { return getSqlSession(inv, writeDS.get(0)); }
		}
		return getDefaultSqlSession(inv);
	}


	private SqlSession getDefaultSqlSession( MyBatisInvocation inv ) {
		DataSource ds = getConfiguration().getEnvironment().getDataSource();
		// SqlSession sqlSession =
		// SqlSessionUtils.getSqlSession(this.getSqlSessionFactory(),
		// this.getExecutorType(),
		// this.getPersistenceExceptionTranslator());
		return getSqlSession(inv, ds);
	}


	protected SqlSession getSqlSession( MyBatisInvocation inv, DataSource ds ) {
		SpringManagedTransaction tran = new SpringManagedTransaction(ds);
		Configuration config = getConfiguration();
		Executor exe = null;
		Batch batch = inv.getMethod().getAnnotation(Batch.class);
		if ( batch != null ) {
			exe = new BatchExecutor(config, tran);
		} else {
			exe = new SimpleExecutor(config, tran);
		}
		for ( Interceptor interceptor : config.getInterceptors() ) {
			exe = (Executor) interceptor.plugin(exe);
		}
		DefaultSqlSession session = new DefaultSqlSession(config, exe);
		return session;
	}

	/**
	 * Proxy needed to route MyBatis method calls to the proper SqlSession got
	 * from Spring's Transaction Manager It also unwraps exceptions thrown by
	 * {@code Method#invoke(Object, Object...)} to pass a
	 * {@code PersistenceException} to the
	 * {@code PersistenceExceptionTranslator}.
	 */
	private class SqlSessionInterceptor implements InvocationHandler {

		@Override
		public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
			SqlSession sqlSession = null;
			try {
				sqlSession = getSqlSession(method.getName());
				Object result = method.invoke(sqlSession, args);

				if ( !SqlSessionUtils.isSqlSessionTransactional(sqlSession, getSqlSessionFactory()) ) {
					// force commit even on non-dirty sessions because some
					// databases require
					// a commit/rollback before calling close()
					sqlSession.commit(true);
				}
				return result;
			} catch ( Throwable t ) {
				Throwable unwrapped = unwrapThrowable(t);
				if ( getPersistenceExceptionTranslator() != null && unwrapped instanceof PersistenceException ) {
					Throwable translated = getPersistenceExceptionTranslator().translateExceptionIfPossible(
						(PersistenceException) unwrapped);
					if ( translated != null ) {
						unwrapped = translated;
					}
				}
				throw unwrapped;
			} finally {
				SqlSessionUtils.closeSqlSession(sqlSession, getSqlSessionFactory());
			}
		}
	}

}
