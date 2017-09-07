package tech.lihx.demo.core.mq.client;


/**
 * MqMessage 消息
 * <p>
 * 
 * @author hubin
 * @date lihx
 * @version 1.0.0
 */
public abstract class AbstractMessage {

	/**
	 * 订阅主题
	 */
	public abstract String getTopic();


	/**
	 * 消息内容
	 */
	public abstract String getMessage();
}
