package tech.lihx.demo.core.mq.logback.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import tech.lihx.demo.core.mq.logback.kafka.formatter.Formatter;
import tech.lihx.demo.core.mq.logback.kafka.formatter.MessageFormatter;


/**
 * <p>
 * 
 * @author hubin
 * @date 2015年6月1日
 * @version 1.0.0
 */
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

	private String topic;

	private String zookeeperHost;

	private Producer<String, String> producer;

	private Formatter formatter;


	public String getTopic() {
		return topic;
	}


	public void setTopic( String topic ) {
		this.topic = topic;
	}


	public String getZookeeperHost() {
		return zookeeperHost;
	}


	public void setZookeeperHost( String zookeeperHost ) {
		this.zookeeperHost = zookeeperHost;
	}


	public Formatter getFormatter() {
		return formatter;
	}


	public void setFormatter( Formatter formatter ) {
		this.formatter = formatter;
	}


	@Override
	public void start() {
		if ( this.formatter == null ) {
			this.formatter = new MessageFormatter();
		}
		super.start();
		Properties props = new Properties();
		props.put("zk.connect", this.zookeeperHost);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		ProducerConfig config = new ProducerConfig(props);
		this.producer = new Producer<String, String>(config);
	}


	@Override
	public void stop() {
		super.stop();
		this.producer.close();
	}


	@Override
	protected void append( ILoggingEvent event ) {
		// KeyedMessage<String, String> msg = new KeyedMessage<String,
		// String>(topic, this.formatter.format(event));
		// this.producer.send(msg);
	}

}
