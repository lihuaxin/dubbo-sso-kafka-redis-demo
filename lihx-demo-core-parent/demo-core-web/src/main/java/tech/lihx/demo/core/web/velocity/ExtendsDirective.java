package tech.lihx.demo.core.web.velocity;

import org.apache.velocity.runtime.directive.Parse;

/**
 * @author LHX
 */
public class ExtendsDirective extends Parse {

	@Override
	public String getName() {
		return "extends";
	}

}
