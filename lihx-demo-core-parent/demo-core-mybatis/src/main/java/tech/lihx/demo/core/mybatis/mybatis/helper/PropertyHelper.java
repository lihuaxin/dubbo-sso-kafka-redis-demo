package tech.lihx.demo.core.mybatis.mybatis.helper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.reflection.MetaClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 保留list和bean的某些属性值
 * <p>
 * 
 * @author lihx
 * @date 2017-9-5
 * @version 1.0.0
 */
public class PropertyHelper {

	private static Logger logger = LoggerFactory.getLogger(PropertyHelper.class);


	/**
	 * 保留属性值
	 * <p>
	 *
	 * @param list
	 * @param property
	 */
	public static void reserveProperty( List<? extends Serializable> list, String... property ) {
		if ( list == null || list.size() == 0 ) { return; }
		try {
			Set<String> setProperty = new HashSet<String>(Arrays.asList(property));
			MetaClass metaClass = MetaClass.forClass(list.get(0).getClass());
			String[] setter = metaClass.getSetterNames();
			for ( String set : setter ) {
				// 不保留这些属性
				if ( !setProperty.contains(set) ) {
					for ( Object obj : list ) {
						metaClass.getSetInvoker(set).invoke(obj, new Object[ ] { null });
					}
				}
			}
		} catch ( Exception e ) {
			logger.error("", e);
		}

	}


	/**
	 * 保留属性值
	 * <p>
	 *
	 * @param object
	 * @param property
	 */
	public static void reserveProperty( Serializable object, String... property ) {
		if ( object == null ) { return; }
		try {
			Set<String> setProperty = new HashSet<String>(Arrays.asList(property));
			MetaClass metaClass = MetaClass.forClass(object.getClass());
			String[] setter = metaClass.getSetterNames();
			for ( String set : setter ) {
				// 不保留这些属性
				if ( !setProperty.contains(set) ) {
					metaClass.getSetInvoker(set).invoke(object, new Object[ ] { null });
				}
			}
		} catch ( Exception e ) {
			logger.error("", e);
		}
	}


	/**
	 * 对象数据拷贝,可以全部赋值,包括null
	 * <p>
	 *
	 * @param oldObj
	 * @param newObj
	 * @param isAll
	 */
	public static void copyProperty( Serializable oldObj, Serializable newObj, boolean isAll ) {
		if ( oldObj == null || newObj == null ) { throw new RuntimeException("对象不能为null"); }
		try {
			MetaClass oldClass = MetaClass.forClass(oldObj.getClass());
			MetaClass newClass = MetaClass.forClass(newObj.getClass());
			String[] getter = oldClass.getGetterNames();
			String[] setter = newClass.getSetterNames();
			Set<String> getProperty = new HashSet<String>(Arrays.asList(getter));
			for ( String set : setter ) {
				if ( getProperty.contains(set) ) {
					Object result = oldClass.getGetInvoker(set).invoke(oldObj, null);
					if ( isAll ) {
						newClass.getSetInvoker(set).invoke(newObj, new Object[ ] { result });
					} else {
						if ( result != null ) {
							newClass.getSetInvoker(set).invoke(newObj, new Object[ ] { result });
						}
					}

				}
			}
		} catch ( Exception e ) {
			logger.error("", e);
		}
	}


	/**
	 * 数据拷贝,默认只拷贝不是null的值,只要有相同属性就可以进行赋值操作
	 * <p>
	 *
	 * @param oldObj
	 * @param newObj
	 */
	public static void copyProperty( Serializable oldObj, Serializable newObj ) {
		copyProperty(oldObj, newObj, false);
	}


	public static void main( String[] args ) {

	}
}
