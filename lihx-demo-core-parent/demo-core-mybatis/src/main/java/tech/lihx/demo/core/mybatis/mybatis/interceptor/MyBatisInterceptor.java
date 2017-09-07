package tech.lihx.demo.core.mybatis.mybatis.interceptor;

public interface MyBatisInterceptor {

	public Object invoke( MyBatisInvocation handler ) throws Throwable;
}
