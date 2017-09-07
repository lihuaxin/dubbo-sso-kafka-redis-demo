package tech.lihx.demo.core.mq.logback.kafka.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;


/**
 * 消息格式
 * <p>
 * 
 * @author hubin
 * @date 2015年6月1日
 * @version 1.0.0
 */
public class MessageFormatter implements Formatter {

	@Override
	public String format( ILoggingEvent event ) {
		return event.getFormattedMessage();
	}

}
