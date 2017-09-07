package tech.lihx.demo.core.mybatis.mybatis;
// package cn.vko.mybatis;
//
// import java.util.List;
// import java.util.Properties;
//
// import org.apache.ibatis.executor.Executor;
// import org.apache.ibatis.mapping.BoundSql;
// import org.apache.ibatis.mapping.MappedStatement;
// import org.apache.ibatis.mapping.ParameterMapping;
// import org.apache.ibatis.plugin.Interceptor;
// import org.apache.ibatis.plugin.Intercepts;
// import org.apache.ibatis.plugin.Invocation;
// import org.apache.ibatis.plugin.Plugin;
// import org.apache.ibatis.plugin.Signature;
// import org.apache.ibatis.reflection.MetaObject;
// import org.apache.ibatis.session.Configuration;
// import org.apache.ibatis.session.ResultHandler;
// import org.apache.ibatis.session.RowBounds;
// import org.apache.ibatis.type.TypeHandlerRegistry;
//
// @Intercepts({
// @Signature(type = Executor.class, method = "update", args = {
// MappedStatement.class, Object.class }),
// @Signature(type = Executor.class, method = "query", args = {
// MappedStatement.class, Object.class,
// RowBounds.class, ResultHandler.class }) })
// public class ParameterValidateInterceptor implements Interceptor {
//
// //Logger logger =
// LoggerFactory.getLogger(ParameterValidateInterceptor.class);
//
//
// @Override
// public Object intercept( Invocation invocation ) throws Throwable {
// MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
// Object parameter = null;
// if ( invocation.getArgs().length > 1 ) {
// parameter = invocation.getArgs()[1];
// }
// BoundSql boundSql = mappedStatement.getBoundSql(parameter);
// Configuration configuration = mappedStatement.getConfiguration();
//
// validateParameter(configuration, boundSql);
// return invocation.proceed();
//
// }
//
//
// public static void validateParameter( Configuration configuration, BoundSql
// boundSql ) {
// Object parameterObject = boundSql.getParameterObject();
// List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
// if ( parameterMappings.size() > 0 && parameterObject != null ) {
// TypeHandlerRegistry typeHandlerRegistry =
// configuration.getTypeHandlerRegistry();
// if ( typeHandlerRegistry.hasTypeHandler(parameterObject.getClass()) ) {
//
// return parameterObject;
// } else {
// MetaObject metaObject = configuration.newMetaObject(parameterObject);
// for ( ParameterMapping parameterMapping : parameterMappings ) {
// String propertyName = parameterMapping.getProperty();
// if ( metaObject.hasGetter(propertyName) ) {
// Object obj = metaObject.getValue(propertyName);
// return obj;
// } else if ( boundSql.hasAdditionalParameter(propertyName) ) {
// Object obj = boundSql.getAdditionalParameter(propertyName);
// return obj;
// }
// }
// }
// }
// }
//
//
// @Override
// public Object plugin( Object target ) {
// return Plugin.wrap(target, this);
// }
//
//
// @Override
// public void setProperties( Properties properties0 ) {
// }
// }
