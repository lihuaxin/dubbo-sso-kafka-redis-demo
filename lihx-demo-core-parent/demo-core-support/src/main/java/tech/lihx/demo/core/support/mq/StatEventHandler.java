package tech.lihx.demo.core.support.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;

@SuppressWarnings( { "rawtypes", "unchecked" } )
public class StatEventHandler implements EventHandler<MQEvent> {

	Logger logger = LoggerFactory.getLogger(StatEventHandler.class);


	@Override
	public void onEvent( MQEvent event, long sequence, boolean endOfBatch ) {
		try {
			Object data = event.getData();
			if ( data != null ) {
				event.getHandler().handle(data);
			}
		} catch ( Exception e ) {
			logger.error("异步处理出错", e);
		}

	}
}
