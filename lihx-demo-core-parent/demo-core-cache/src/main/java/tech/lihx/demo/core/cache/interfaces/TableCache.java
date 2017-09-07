package tech.lihx.demo.core.cache.interfaces;

import tech.lihx.demo.core.cache.util.Invoker;


/**
 * 对某个表的缓存,单表查询进行缓存,如果有数据变化,清空表的缓存
 * <p>
 *
 * @author lihx
 * @Date 2014-7-9
 */
public interface TableCache {

	public Object get( String table, String key, Invoker invoker, boolean compress );


	public void removeAll( String table );

}
