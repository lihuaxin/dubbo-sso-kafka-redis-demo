package tech.lihx.demo.core.mq.internal;


import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.mq.handler.MqHandler;

/**
 * kafka 消费者
 * 
 * @author lihx
 * @date 2017-9-5 09:02:06
 * @version
 */
public class KafkaConsumerThread implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerThread.class);

	private final KafkaStream<byte[], byte[]> kafkaStream;

	private final MqHandler mqHandler;


	public KafkaConsumerThread( KafkaStream<byte[], byte[]> kafkaStream, MqHandler mqHandler ) {
		this.kafkaStream = kafkaStream;
		this.mqHandler = mqHandler;
	}


	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> it = this.kafkaStream.iterator();
		while ( it.hasNext() ) {
			try {
				this.mqHandler.callback(it.next().message());
			} catch ( Exception e ) {
				logger.error("执行消费报错", e);
			}
		}
	}
}
