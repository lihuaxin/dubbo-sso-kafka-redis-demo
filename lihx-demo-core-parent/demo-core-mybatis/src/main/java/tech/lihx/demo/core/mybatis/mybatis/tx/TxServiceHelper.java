package tech.lihx.demo.core.mybatis.mybatis.tx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.ibatis.mapping.SqlCommandType;


/**
 * 探测事务的工具类
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class TxServiceHelper {

	// 用户在一个方法中所有的增删改查操作历史
	private static ThreadLocal<Set<SqlCommandType>> transactionAttribute = new ThreadLocal<Set<SqlCommandType>>();


	public static void add( SqlCommandType type ) {
		Set<SqlCommandType> list = transactionAttribute.get();
		if ( list == null ) {
			list = new HashSet<SqlCommandType>();
			transactionAttribute.set(list);
		}
		list.add(type);
	}


	public static void addAll( Set<SqlCommandType> sets ) {
		Set<SqlCommandType> list = transactionAttribute.get();
		if ( list == null ) {
			list = new HashSet<SqlCommandType>();
			transactionAttribute.set(list);
		}
		list.addAll(sets);
	}


	public static Set<SqlCommandType> get() {
		Set<SqlCommandType> list = transactionAttribute.get();
		if ( list == null ) { return Collections.emptySet(); }
		return list;
	}


	public static void removeAll() {
		transactionAttribute.remove();
	}
}
