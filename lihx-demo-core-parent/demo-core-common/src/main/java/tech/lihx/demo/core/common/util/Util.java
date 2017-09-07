package tech.lihx.demo.core.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;


/**
 * 常用的工具类的集合
 * <p>
 * 包含最常用工具类，比如判空、判断相等等与具体对象无关的方法
 * 
 * @author LHX
 * @Date 2016-4-27
 * @version 5.0.1
 */
public class Util {

	/**
	 * 判断一个对象是否为空。它支持如下对象类型：
	 * <ul>
	 * <li>null : 一定为空
	 * <li>字符串 : ""为空,多个空格也为空
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>其他对象 : 一定不为空
	 * </ul>
	 * 
	 * @param obj
	 *            任意对象
	 * @return 是否为空
	 */
	public final static boolean isEmpty( final Object obj ) {
		if ( obj == null ) { return true; }
		if ( obj instanceof String ) { return "".equals(String.valueOf(obj).trim()); }
		if ( obj.getClass().isArray() ) { return Array.getLength(obj) == 0; }
		if ( obj instanceof Collection<?> ) { return ((Collection<?>) obj).isEmpty(); }
		if ( obj instanceof Map<?, ?> ) { return ((Map<?, ?>) obj).isEmpty(); }
		return false;
	}

	/**
	 * 判断2个对象是否相等
	 * <ul>
	 * <li>可以容忍 null
	 * <li>可以容忍不同类型的 Number
	 * <li>对数组，集合， Map 会深层比较
	 * </ul>
	 * 当然，如果你重写的 equals 方法会优先
	 * 
	 * @param a
	 *            待判定对象
	 * @param b
	 *            待判定对象
	 * @return 对象是否相等
	 */
	/*public final static boolean eq(final Object a, final Object b) {
		return Lang.equals(a, b);
	}*/

	/**
	 * 如果是数组或集合取得第一个对象。 否则返回自身
	 * 
	 * @param o
	 *            任意对象
	 * @return 第一个代表对象
	 */
	/*public final static Object first(final Object o) {
		return Lang.first(o);
	}*/
}
