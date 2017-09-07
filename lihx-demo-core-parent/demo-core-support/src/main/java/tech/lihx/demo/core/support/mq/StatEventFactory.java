package tech.lihx.demo.core.support.mq;

import com.lmax.disruptor.EventFactory;

@SuppressWarnings( "rawtypes" )
public class StatEventFactory implements EventFactory<MQEvent> {

	@Override
	public MQEvent newInstance() {
		return new MQEvent();
	}
}
