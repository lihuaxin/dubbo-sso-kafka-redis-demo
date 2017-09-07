package tech.lihx.demo.core.mq.client;


/**
 * mq 消费者
 * 
 * @author lihx
 * @date 2017-9-5 09:15:32
 * @version 1.0.0
 */
public abstract class MqConsumer {

	/**
	 * 初始化
	 */
	public abstract MqConsumer init();


	/**
	 * 运行
	 */
	public abstract void run();


	/**
	 * 销毁
	 */
	public abstract void destory();
}
