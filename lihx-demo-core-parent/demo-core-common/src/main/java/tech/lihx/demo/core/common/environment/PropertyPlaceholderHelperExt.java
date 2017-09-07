/**
 * PropertyPlaceholderHelperExt.java package tech.lihx.demo.core.common.environment; Copyright
 * 
 */

package tech.lihx.demo.core.common.environment;

import java.util.Set;

import org.springframework.util.PropertyPlaceholderHelper;


/**
 * 处理不赋值的情况
 * <p>
 * 
 * @author lihx
 * @date 2017-9-4 17:50:06
 * @version 1.0.0
 */
public class PropertyPlaceholderHelperExt extends PropertyPlaceholderHelper {

	public PropertyPlaceholderHelperExt(
			String placeholderPrefix,
			String placeholderSuffix,
			String valueSeparator,
			boolean ignoreUnresolvablePlaceholders ) {

		super(placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);

	}


	@Override
	protected String parseStringValue(
			String strVal, PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders ) {
		String value = super.parseStringValue(strVal, placeholderResolver, visitedPlaceholders);
		if ( value.startsWith("$") ) { return ""; }
		return value;

	}

}
