package tech.lihx.demo.core.web.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * 
 * @author LHX
 * @date 2016-4-8
 * @version 1.0.0
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface ReSubmitToken {

	boolean save() default false;


	boolean remove() default false;
}
