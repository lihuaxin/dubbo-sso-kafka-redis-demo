package tech.lihx.demo.core.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 过期缓存
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface Cacheable {

	public static int EXPIRE = 1, ENTITY = 2, HTML = 3;


	// expire过期缓存,entity实体缓存基于主键,table对于某个表的基于sql的缓存(加在Mapper类上,对于单表查询,子查询和连接查询不管)

	int type() default EXPIRE;


	// 只对过期缓存有用,秒
	int expire() default 5 * 60;


	// 实体唯一标示,过期缓存不用提供,可以用于自定义缓存键值
	String key() default "";


	// 表名称,对实体缓存有用,可以用于自定义缓存键值
	String table() default "";


	// 是否清除缓存,对实体缓存有用,过期缓存无法提供key,只能等待过期
	boolean evict() default false;


	// 数据是否压缩
	boolean compress() default true;
}
