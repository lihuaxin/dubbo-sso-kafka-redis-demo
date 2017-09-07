package tech.lihx.demo.core.support.optlog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录日志
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:14:41
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface Logger {

	// 此方法作用描述，用来记录日志，必须填写
	String value();

}
