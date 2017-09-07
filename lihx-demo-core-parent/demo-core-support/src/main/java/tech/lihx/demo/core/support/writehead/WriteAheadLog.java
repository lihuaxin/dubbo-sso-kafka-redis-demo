package tech.lihx.demo.core.support.writehead;

import java.lang.reflect.Method;

/**
 * 先写日志接口
 * <p>
 * 
 * @author lihx
 * @Date 2017年9月5日
 */
public interface WriteAheadLog {

	// 先写日志
	public void before( String uuid, Method method, Object[] arguments );


	// 操作结束后执行
	public void after( String uuid, Method method, Object[] arguments, Object result );
}
