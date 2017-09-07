package tech.lihx.demo.core.support.writehead;

import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import tech.lihx.demo.core.common.util.CompressedUUID;
import tech.lihx.demo.core.support.mq.Handler;
import tech.lihx.demo.core.support.mq.MQService;

/**
 * 拦截器实现
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5
 */
public class WriteAheadInterceptor implements MethodInterceptor {

	/*
	 * 缓存执行对象
	 */
	private final ConcurrentHashMap<Class<?>, WriteAheadLog> cache = new ConcurrentHashMap<Class<?>, WriteAheadLog>();

	/*
	 * 异步队列
	 */
	private MQService mqService;

	/*
	 * 异步处理器
	 */
	private final Handler<Object[]> handler = new Handler<Object[]>() {

		@Override
		public void handle( Object[] data ) {
			WriteAheadLog log = (WriteAheadLog) data[0];
			MethodInvocation inv = (MethodInvocation) data[1];
			String uuid = (String) data[2];
			Boolean before = (Boolean) data[3];
			if ( before ) {
				log.before(uuid, inv.getMethod(), inv.getArguments());
			} else {
				Object result = data[4];
				log.after(uuid, inv.getMethod(), inv.getArguments(), result);
			}
		}

	};


	@Override
	public Object invoke( final MethodInvocation invocation ) throws Throwable {
		// 没有注解跳过
		WriteAhead writeAhead = invocation.getMethod().getAnnotation(WriteAhead.class);
		if ( writeAhead == null ) { return invocation.proceed(); }
		// 唯一标识
		String uuid = CompressedUUID.compressedUUID();
		// 缓存键值
		WriteAheadLog service = cache.get(writeAhead.value());
		if ( service == null ) {
			// 创建操作对象
			service = writeAhead.value().newInstance();
			cache.putIfAbsent(writeAhead.value(), service);
		}
		// 异步处理操作之前的日志
		mqService.putData(new Object[ ] { service, invocation, uuid, true }, handler);
		try {
			// 执行service操作
			Object result = invocation.proceed();
			// 执行结束后的操作,出异常下面是不执行的
			mqService.putData(new Object[ ] { service, invocation, uuid, false, result }, handler);
			return result;
		} finally {}
	}


	public MQService getMqService() {
		return mqService;
	}


	public void setMqService( MQService mqService ) {
		this.mqService = mqService;
	}

}
