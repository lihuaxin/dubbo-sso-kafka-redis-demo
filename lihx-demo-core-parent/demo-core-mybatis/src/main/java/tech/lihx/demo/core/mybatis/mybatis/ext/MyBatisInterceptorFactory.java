package tech.lihx.demo.core.mybatis.mybatis.ext;

import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInterceptor;

public interface MyBatisInterceptorFactory {

	// 拦截器,用于缓存
	public MyBatisInterceptor[] getMyBatisInterceptor();
	// orm映射关系
}
