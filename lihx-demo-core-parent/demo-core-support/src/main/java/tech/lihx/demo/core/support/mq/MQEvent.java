package tech.lihx.demo.core.support.mq;


/**
 * 数据和处理的封装
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:13:40
 */
public class MQEvent<T> {

	private T data;

	private Handler<T> handler;


	public T getData() {
		return data;
	}


	public void setData( T data ) {
		this.data = data;
	}


	public Handler<T> getHandler() {
		return handler;
	}


	public void setHandler( Handler<T> handler ) {
		this.handler = handler;
	}

}
