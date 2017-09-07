package tech.lihx.demo.core.support.mq;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:13:56
 */
@SuppressWarnings( "rawtypes" )
public class MQService {

	private int bufferSize = 8192;

	private StatEventProducer producer;

	private Disruptor<MQEvent> disruptor;


	@SuppressWarnings( "unchecked" )
	public void init() {
		Executor executor = Executors.newCachedThreadPool();
		// The factory for the event
		StatEventFactory factory = new StatEventFactory();
		// Construct the Disruptor
		disruptor = new Disruptor<MQEvent>(
				factory, bufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());
		// Connect the handler
		disruptor.handleEventsWith(new StatEventHandler());
		// Start the Disruptor, starts all threads running
		RingBuffer<MQEvent> ringBuffer = disruptor.start();
		// Get the ring buffer from the Disruptor to be used for publishing.
		// RingBuffer<StatEvent> ringBuffer = disruptor.getRingBuffer();
		producer = new StatEventProducer(ringBuffer);
	}


	public <T> void putData( T data, Handler<T> handler ) {
		producer.onData(data, handler);
	}


	public int getBufferSize() {
		return bufferSize;
	}


	public void setBufferSize( int bufferSize ) {
		this.bufferSize = bufferSize;
	}


	public void destroy() {
		if ( disruptor != null ) {
			disruptor.shutdown();
		}
	}
}
