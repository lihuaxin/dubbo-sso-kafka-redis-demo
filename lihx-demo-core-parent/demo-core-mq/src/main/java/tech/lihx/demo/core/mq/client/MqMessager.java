package tech.lihx.demo.core.mq.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.mq.internal.KafkaMessage;

/**
 * mq消息内容
 * <p>
 *
 * @author hubin
 * @date 2014-11-28
 * @version 1.0.0
 */
public class MqMessager {

	private static final Logger logger = LoggerFactory.getLogger( MqMessager.class );


	/**
	 * 创建一个消息
	 * <p>
	 *
	 * @param topic
	 *        订阅主题
	 * @param obj
	 *        对象可以是List
	 * @return AbstractMessage
	 */
	public static AbstractMessage newMsg( MqTopic topic, Object obj ) {

		return new KafkaMessage( topic, obj );
	}

}
