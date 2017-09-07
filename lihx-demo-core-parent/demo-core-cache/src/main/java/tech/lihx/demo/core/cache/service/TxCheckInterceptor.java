package tech.lihx.demo.core.cache.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.exception.ModuleException;
import tech.lihx.demo.core.mybatis.mybatis.tx.TxServiceHelper;


/**
 * 事物中方法命名检测, 提示没有按规定命名的方法名称,杜绝事务问题
 * <p>
 * 
 * @author lihx
 * @date 2015年1月8日
 * @version 1.0.0
 */
@SuppressWarnings( "serial" )
public class TxCheckInterceptor implements MethodInterceptor, Serializable {

	TransactionAspectSupport txAdvice;


	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		// 排除其他调用的影响
		Set<SqlCommandType> set = TxServiceHelper.get();
		TxServiceHelper.removeAll();
		try {
			// 执行
			Object result = invocation.proceed();
			if ( EnvironmentDetect.detectEnvironment().isRelease() ) { return result; }
			Method method = invocation.getMethod();
			TransactionAttributeSource ggg = txAdvice.getTransactionAttributeSource();
			TransactionAttribute attr = ggg.getTransactionAttribute(method, method.getDeclaringClass());
			if ( attr.isReadOnly() ) {
				Set<SqlCommandType> list = TxServiceHelper.get();
				if ( list.contains(SqlCommandType.DELETE)
						|| list.contains(SqlCommandType.INSERT) || list.contains(SqlCommandType.UPDATE) ) { throw new ModuleException(
						"您的方法标志为只读,但执行了增删改查操作,请修改方法的名称定义:" + method.getDeclaringClass() + "." + method.getName()); }
			}
			// if ( attr.getPropagationBehavior() ==
			// TransactionDefinition.PROPAGATION_REQUIRED ) {
			// TxModuleEndpointHelper.add();
			// }
			return result;
		} finally {
			TxServiceHelper.removeAll();
			if ( set != null ) {
				// 最后原样放入
				TxServiceHelper.addAll(set);
			}
		}
	}


	public TransactionAspectSupport getTxAdvice() {
		return txAdvice;
	}


	public void setTxAdvice( TransactionAspectSupport txAdvice ) {
		this.txAdvice = txAdvice;
	}


}
