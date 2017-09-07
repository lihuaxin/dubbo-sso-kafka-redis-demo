package tech.lihx.demo.core.web.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import tech.lihx.demo.core.web.interceptor.ShiroInterceptor;


/**
 * 权限控制自定义标签
 * <p>
 * 
 * @author LHX
 * @date 2014-12-29
 * @version 1.0.0
 */
public class PrivilegeDirective extends Directive {

	@Override
	public String getName() {

		return "privilege";

	}


	@Override
	public int getType() {

		return BLOCK;

	}


	@Override
	public boolean render( InternalContextAdapter context, Writer writer, Node node ) throws IOException,
		ResourceNotFoundException, ParseErrorException, MethodInvocationException {

		// 获取权限值code
		SimpleNode codeNode = (SimpleNode) node.jjtGetChild(0);
		String code = (String) codeNode.value(context);
		// 判断用户权限code集合中是否包含 code
		if ( ShiroInterceptor.isPermitted(code) ) {
			Node body = node.jjtGetChild(1);
			StringWriter sw = new StringWriter();
			body.render(context, writer);
			writer.write(sw.toString());
			return true;
		}
		return true;

	}

}
