package tech.lihx.demo.core.mybatis.mybatis.ext;

import java.util.List;

import tech.lihx.demo.core.mybatis.mybatis.interceptor.MyBatisInterceptor;

public class DefaultMyBatisExtFactory implements MyBatisInterceptorFactory {

	private List<MyBatisInterceptor> interceptors;


	@Override
	public MyBatisInterceptor[] getMyBatisInterceptor() {

		return getInterceptors().toArray(new MyBatisInterceptor[0]);
	}


	public List<MyBatisInterceptor> getInterceptors() {
		return interceptors;
	}


	public void setInterceptors( List<MyBatisInterceptor> interceptors ) {
		this.interceptors = interceptors;
	}

}
