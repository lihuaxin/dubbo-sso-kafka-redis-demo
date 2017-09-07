package tech.lihx.demo.core.web.resolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标志主键从字符串转换为Long类型
 * <p>
 */
@Target( { ElementType.PARAMETER } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface Identity {

	String value() default "";
}
