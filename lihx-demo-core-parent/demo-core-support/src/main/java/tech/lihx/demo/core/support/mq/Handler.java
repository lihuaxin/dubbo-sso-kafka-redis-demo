package tech.lihx.demo.core.support.mq;

/**
 * 提供消费者
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:13:18
 */
public interface Handler<T> {

	/**
	 * 传递数据,然后处理
	 * 
	 * @param bean
	 */
	public void handle( T data );
}
