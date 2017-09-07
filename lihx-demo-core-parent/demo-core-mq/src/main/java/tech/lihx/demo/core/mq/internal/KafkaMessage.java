package tech.lihx.demo.core.mq.internal;

import java.util.List;

import tech.lihx.demo.core.mq.client.AbstractMessage;
import tech.lihx.demo.core.mq.client.MqTopic;

import com.alibaba.fastjson.JSON;


/**
 * kafka 消息
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5 09:02:36
 * @version 1.0.0
 */
public class KafkaMessage extends AbstractMessage {

	/**
	 * 订阅主题
	 */
	private String topic;

	/**
	 * 消息内容
	 */
	private String message;


	public KafkaMessage( MqTopic topic, Object obj ) {
		this.topic = topic.getTopic();
		this.message = JSON.toJSONString(obj);
	}


	public KafkaMessage( MqTopic topic, List<Object> obj ) {
		this.topic = topic.getTopic();
		this.message = JSON.toJSONString(obj);
	}


	@Override
	public String getTopic() {
		return topic;
	}


	public void setTopic( String topic ) {
		this.topic = topic;
	}


	@Override
	public String getMessage() {
		return message;
	}


	public void setMessage( String message ) {
		this.message = message;
	}
}
