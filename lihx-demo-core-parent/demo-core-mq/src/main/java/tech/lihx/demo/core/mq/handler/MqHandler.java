package tech.lihx.demo.core.mq.handler;


/**
 * mq 事件控制抽象类
 * 
 * @author lihx
 * @date 2017-9-5 09:21:49
 * @version 1.0.0
 */
public abstract class MqHandler {

	public abstract void callback( byte[] data );
}
