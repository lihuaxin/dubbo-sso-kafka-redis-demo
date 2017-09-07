
package tech.lihx.demo.core.mq.logback.kafka.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;


/**
 * 格式化 String
 * <p>
 * 
 * @author hubin
 * @date 2015年6月1日
 * @version 1.0.0
 */
public interface Formatter {

	String format( ILoggingEvent event );
}
