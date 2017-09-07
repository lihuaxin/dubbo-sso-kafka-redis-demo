package tech.lihx.demo.core.cache.interfaces;

import tech.lihx.demo.core.cache.util.Invoker;


/**
 * 对象实体缓存
 * <p>
 *
 * @author lihx
 * @Date 2014-7-9
 */
public interface EntityCache {

	public Object get( String table, Object id, Invoker invoker, boolean compress );


	public void remove( String table, Object id );

}
