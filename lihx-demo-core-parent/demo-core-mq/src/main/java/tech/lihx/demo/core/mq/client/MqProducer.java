package tech.lihx.demo.core.mq.client;


/**
 * mq 生产者抽象类
 * 
 * @author lihx
 * @date 2017-9-5 09:06:54
 * @version 1.0.0
 */
public abstract class MqProducer {

	/**
	 * 初始化
	 */
	public abstract void init();


	/**
	 * 发送主题消息
	 * 
	 * @param msg
	 *            消息内容
	 */
	public abstract void send( AbstractMessage msg );


	/**
	 * 销毁
	 */
	public abstract void destory();
}
