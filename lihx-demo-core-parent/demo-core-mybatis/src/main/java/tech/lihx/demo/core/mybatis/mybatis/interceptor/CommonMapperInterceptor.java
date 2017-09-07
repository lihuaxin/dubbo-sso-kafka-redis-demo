package tech.lihx.demo.core.mybatis.mybatis.interceptor;

import java.lang.reflect.Method;

import org.apache.ibatis.session.Configuration;

import tech.lihx.demo.core.mybatis.mybatis.annotation.TableName;
import tech.lihx.demo.core.mybatis.mybatis.ext.mapper.MapperGenerate;


/**
 * 用于支持共通的mapper增删改查操作
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class CommonMapperInterceptor implements MyBatisInterceptor {

	@Override
	public Object invoke( MyBatisInvocation handler ) throws Throwable {
		Method method = handler.getMethod();
		Class<?> mapperClass = handler.getMapperInterface();
		TableName tableName = mapperClass.getAnnotation(TableName.class);
		if ( tableName != null ) {
			String methodName = method.getName();
			String className = mapperClass.getName();
			String statementName = className + "." + methodName;
			if ( !handler.getConfiguration().hasStatement(statementName, false) ) {
				addStatement(handler.getConfiguration(), statementName, mapperClass, tableName);
			}
		}
		return handler.execute();

	}


	/**
	 * 添加公共增删改查
	 * <p>
	 *
	 * @param configuration
	 * @param method
	 * @param mapperClass
	 * @param tableName
	 */
	private synchronized void addStatement(
			Configuration configuration, String statementName, Class<?> mapperClass, TableName tableName ) {
		if ( configuration.hasStatement(statementName, false) ) { return; }
		MapperGenerate generate = new MapperGenerate(
				configuration, tableName.entity(), mapperClass, tableName.table(), tableName.id());
		generate.build();

	}


}
