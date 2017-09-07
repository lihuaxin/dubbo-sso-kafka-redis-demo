package tech.lihx.demo.core.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import tech.lihx.demo.core.common.util.Base58;


/**
 * 自动将压缩的主键解压缩为Long类型
 * <p>
 * 
 * @author LHX
 * @date 2016年11月15日
 * @version 1.0.0
 */
public class IdentityHandlerMethodArgumentResolver extends PathVariableMethodArgumentResolver {

	@Override
	public boolean supportsParameter( MethodParameter parameter ) {
		if ( !parameter.hasParameterAnnotation(Identity.class) ) { return false; }
		return true;

	}


	@SuppressWarnings( "synthetic-access" )
	@Override
	protected NamedValueInfo createNamedValueInfo( MethodParameter parameter ) {
		Identity annotation = parameter.getParameterAnnotation(Identity.class);
		return new PathVariableNamedValueInfo(annotation);

	}


	@Override
	protected Object resolveName( String name, MethodParameter parameter, NativeWebRequest request ) throws Exception {
		Object obj = super.resolveName(name, parameter, request);
		if ( obj != null ) {
			try {
				return Base58.decode(obj.toString());
			} catch ( Exception e ) {
				// ignore
			}
		}
		return obj;

	}

	public static class PathVariableNamedValueInfo extends NamedValueInfo {

		private PathVariableNamedValueInfo( Identity annotation ) {
			super(annotation.value(), true, ValueConstants.DEFAULT_NONE);
		}
	}

}
