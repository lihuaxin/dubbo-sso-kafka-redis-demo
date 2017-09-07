package tech.lihx.demo.core.support.writehead;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:17:34
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
@Documented
public @interface WriteAhead {

	// 已类名，方法名，参数类型做键值缓存由class创建的对象
	Class<? extends WriteAheadLog> value();
}
