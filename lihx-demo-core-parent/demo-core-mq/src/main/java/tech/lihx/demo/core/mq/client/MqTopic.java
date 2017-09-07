package tech.lihx.demo.core.mq.client;


/**
 * kafka mq 订阅主题
 * 
 * @author lihx
 * @date 2017-9-5 09:05:33
 * @version 1.0.0
 */
public enum MqTopic {
	
	EXAMPLE("example", "例子");

	private final String topic;// 主题

	private final String desc;// 描述


	MqTopic( final String topic, final String desc ) {
		this.topic = topic;
		this.desc = desc;
	}


	public String getTopic() {
		return this.topic;
	}


	public String getDesc() {
		return this.desc;
	}
}
