package tech.lihx.demo.core.mq.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.mq.client.MqConsumer;
import tech.lihx.demo.core.mq.client.MqTopic;
import tech.lihx.demo.core.mq.handler.MqHandler;


/**
 * kafka 消费者
 * 
 * @author LHX
 * @date 2015-11-12
 * @version 1.0.0
 */
public class KafkaConsumer extends MqConsumer {

	private final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	private ConsumerConnector consumer = null;

	private ExecutorService executor = null;

	/** kafka配置参数 */
	private String zkConnection;

	private String groupId = "mqkafkagroup";

	private String zkSessionTimeout = "5000";

	private String zkSyncTime = "350";

	private String autoCommitInterval = "1000";


	/** 业务参数 */
	private MqHandler mqHandler;// 事件控制器

	private MqTopic topic;// 订阅主题

	private int nThreads = 3;// 线程数（默认 3）


	public KafkaConsumer() {
	}


	public KafkaConsumer( String zkConnect, MqTopic topic, MqHandler mqHandler ) {
		this.zkConnection = zkConnect;
		this.topic = topic;
		this.mqHandler = mqHandler;
	}


	@Override
	public KafkaConsumer init() {
		if ( consumer == null ) {
			Properties props = new Properties();
			props.put("zookeeper.connect", this.zkConnection);
			props.put("group.id", this.groupId);
			props.put("zookeeper.session.timeout.ms", this.zkSessionTimeout);
			props.put("zookeeper.sync.time.ms", this.zkSyncTime);
			props.put("auto.commit.interval.ms", this.autoCommitInterval);
			consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
			logger.info(" kafka Consumer init..");
		}

		if ( executor == null ) {
			// 创建线程池
			executor = Executors.newFixedThreadPool(this.nThreads);
		}
		return this;
	}


	@Override
	public void run() {
		if ( consumer == null || executor == null ) {
			init();
		}
		try {
			logger.info(" kafka Consumer run topic {}", this.topic.getDesc());
			Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
			topicCountMap.put(this.topic.getTopic(), new Integer(this.nThreads));
			Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
			List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(this.topic.getTopic());
			for ( final KafkaStream<byte[], byte[]> stream : streams ) {
				// 线程消费消息
				executor.submit(new KafkaConsumerThread(stream, this.mqHandler));
			}
		} catch ( Exception e ) {
			logger.error("消费者出错", e);
		}
	}


	@Override
	public void destory() {
		if ( consumer != null ) {
			consumer.shutdown();
		}
		if ( executor != null ) {
			executor.shutdown();
		}
		logger.info(" kafka Consumer destory..");
	}


	public MqHandler getMqHandler() {
		return mqHandler;
	}


	public void setMqHandler( MqHandler mqHandler ) {
		this.mqHandler = mqHandler;
	}


	public MqTopic getTopic() {
		return topic;
	}


	public void setTopic( MqTopic topic ) {
		this.topic = topic;
	}


	public int getnThreads() {
		return nThreads;
	}


	public void setnThreads( int nThreads ) {
		this.nThreads = nThreads;
	}


	public String getZkConnection() {
		return zkConnection;
	}


	public void setZkConnection( String zkConnection ) {
		this.zkConnection = zkConnection;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId( String groupId ) {
		this.groupId = groupId;
	}


	public String getZkSessionTimeout() {
		return zkSessionTimeout;
	}


	public void setZkSessionTimeout( String zkSessionTimeout ) {
		this.zkSessionTimeout = zkSessionTimeout;
	}


	public String getZkSyncTime() {
		return zkSyncTime;
	}


	public void setZkSyncTime( String zkSyncTime ) {
		this.zkSyncTime = zkSyncTime;
	}


	public String getAutoCommitInterval() {
		return autoCommitInterval;
	}


	public void setAutoCommitInterval( String autoCommitInterval ) {
		this.autoCommitInterval = autoCommitInterval;
	}


}
