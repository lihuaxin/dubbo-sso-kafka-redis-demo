package tech.lihx.demo.core.mybatis.mybatis.tx;


/**
 * 探测事务的工具类
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class TxModuleEndpointHelper {

	// 有事务操作的次数
	private static ThreadLocal<Integer> transactionAttribute = new ThreadLocal<Integer>();


	public static void add() {
		Integer list = transactionAttribute.get();
		if ( list == null ) {
			list = 1;
			transactionAttribute.set(list);
		}
	}


	public static Integer get() {
		Integer list = transactionAttribute.get();
		if ( list == null ) { return 0; }
		return list;
	}


	public static void remove() {
		transactionAttribute.remove();
	}
}
