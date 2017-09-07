package tech.lihx.demo.core.mybatis.mybatis.annotation;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用操作注解
 * <p>
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TableName {

	//表名称
	String table();


	//主键名称,默认id
	String id() default "id";


	//映射的实体名称
	Class<? extends Serializable> entity();
}
