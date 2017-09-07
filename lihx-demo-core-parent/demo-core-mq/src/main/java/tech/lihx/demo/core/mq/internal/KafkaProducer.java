package tech.lihx.demo.core.mq.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.mq.client.AbstractMessage;
import tech.lihx.demo.core.mq.client.MqProducer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


/**
 * kafka 生产者
 * 
 * @author lihx
 * @date 2017-9-5 09:03:23
 * @version 1.0.0
 */
public class KafkaProducer extends MqProducer {

	private final static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	private Producer<Integer, String> producer = null;

	private String metadataBrokerList;

	private String serializerClass = "kafka.serializer.StringEncoder";

	private String requestRequiredAcks = "1";


	public KafkaProducer() {
	}


	@Override
	public void init() {
		if ( producer == null ) {
			if ( metadataBrokerList == null || "".equals(metadataBrokerList) ) {
				throw new RuntimeException("mqclient configure no find brokerList.");
			} else {
				// 初始化 kafka
				Properties props = new Properties();
				props.put("metadata.broker.list", metadataBrokerList);
				props.put("serializer.class", serializerClass);
				props.put("request.required.acks", requestRequiredAcks);
				producer = new Producer<Integer, String>(new ProducerConfig(props));
				logger.info(" kafka mq client init..");
			}
		}
	}


	@Override
	public void destory() {
		if ( producer != null ) {
			producer.close();
			logger.info(" kafka mq client destory..");
		}
	}


	/**
	 * 发送消息至 kafka
	 * <p>
	 * 
	 * @param msg
	 *            消息内容
	 */
	@Override
	public void send( AbstractMessage msg ) {
		if ( msg != null ) {
			try {
				send(msg.getTopic(), msg.getMessage());
			} catch ( Exception e ) {
				logger.error(
					"kafka send msg error. topic:{}, message:{} \n{}", new Object[ ] { msg.getTopic(),
							msg.getMessage(), e.toString() });
			}
		}
	}


	/**
	 * 发送主题消息
	 * 
	 * @param topic
	 *            订阅主题
	 * @param jsonObj
	 *            消息内容，约定json格式（支持对象list）
	 */
	protected void send( String topic, String jsonObj ) {
		Object obj = JSON.parse(jsonObj);
		if ( obj instanceof JSONArray ) {
			/**
			 * json内容判断是否批量发消息
			 */
			JSONArray ja = (JSONArray) obj;
			List<KeyedMessage<Integer, String>> kl = new ArrayList<KeyedMessage<Integer, String>>();
			for ( Object oj : ja ) {
				kl.add(new KeyedMessage<Integer, String>(topic, oj.toString()));
			}
			producer.send(kl);
		} else {
			producer.send(new KeyedMessage<Integer, String>(topic, jsonObj));
		}
	}


	public String getMetadataBrokerList() {
		return metadataBrokerList;
	}


	public void setMetadataBrokerList( String metadataBrokerList ) {
		this.metadataBrokerList = metadataBrokerList;
	}


	public String getSerializerClass() {
		return serializerClass;
	}


	public void setSerializerClass( String serializerClass ) {
		this.serializerClass = serializerClass;
	}


	public String getRequestRequiredAcks() {
		return requestRequiredAcks;
	}


	public void setRequestRequiredAcks( String requestRequiredAcks ) {
		this.requestRequiredAcks = requestRequiredAcks;
	}

}
