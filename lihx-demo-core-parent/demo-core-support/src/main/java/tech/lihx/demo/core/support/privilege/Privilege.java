package tech.lihx.demo.core.support.privilege;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器上面加权限控制 描述用来记录操作日志
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:16:47
 */
@Target( { ElementType.TYPE, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface Privilege {

	// 是否加入权限控制
	// boolean control() default false;


	// 此方法作用描述
	String[] desc() default {};


	// 标示权限的key，
	String[] value();

	// 能访问的角色
	// String[] roles() default {};
}
