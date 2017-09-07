package tech.lihx.demo.core.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁用缓存,一般用在service层
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface CacheDisable {

}
