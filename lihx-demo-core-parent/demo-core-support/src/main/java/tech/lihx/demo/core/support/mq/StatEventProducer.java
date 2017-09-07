package tech.lihx.demo.core.support.mq;

import com.lmax.disruptor.RingBuffer;

@SuppressWarnings( "rawtypes" )
public class StatEventProducer {

	private final RingBuffer<MQEvent> ringBuffer;


	public StatEventProducer( RingBuffer<MQEvent> ringBuffer ) {
		this.ringBuffer = ringBuffer;
	}


	@SuppressWarnings( "unchecked" )
	public void onData( Object data, Handler handler ) {
		long sequence = ringBuffer.next(); // Grab the next sequence
		try {
			MQEvent event = ringBuffer.get(sequence); // Get the entry in the
														// Disruptor
			event.setData(data); // Fill with data
			event.setHandler(handler);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
