package tech.lihx.demo.core.common.environment;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.util.StringValueResolver;

/**
 * 初始化系统配置文件
 * 
 * @author lihx
 * @date 2017-9-4 17:43:27
 * @version 1.0.0
 */
@SuppressWarnings( "synthetic-access" )
public class ConfigPropertyConfigurer extends PropertyPlaceholderConfigurer {

	// ApplicationContext context;
	RunEnvironment environment;


	// public ConfigPropertyConfigurer(ApplicationContext context) {
	// this.context = context;
	// }

	public ConfigPropertyConfigurer() {
		super();
	}


	@Override
	protected Properties mergeProperties() throws IOException {
		// RunConfig config = this.context.getBean(RunConfig.class);
		setProperties(environment.getRunConfig().getProperties());
		setNullValue("");
		return super.mergeProperties();
	}


	@Override
	protected void processProperties( ConfigurableListableBeanFactory beanFactoryToProcess, Properties props )
		throws BeansException {

		StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(props);
		doProcessProperties(beanFactoryToProcess, valueResolver);

	}


	private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

		private final PropertyPlaceholderHelper helper;

		private final PlaceholderResolver resolver;


		public PlaceholderResolvingStringValueResolver( Properties props ) {
			this.helper = new PropertyPlaceholderHelperExt(
					placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
			this.resolver = new PropertyPlaceholderConfigurerResolver(props);
		}


		@Override
		public String resolveStringValue( String strVal ) throws BeansException {
			String value = this.helper.replacePlaceholders(strVal, this.resolver);
			return (value.equals(nullValue) ? null : value);
		}
	}

	private class PropertyPlaceholderConfigurerResolver implements PlaceholderResolver {

		private final Properties props;


		private PropertyPlaceholderConfigurerResolver( Properties props ) {
			this.props = props;
		}


		@Override
		public String resolvePlaceholder( String placeholderName ) {
			String value = ConfigPropertyConfigurer.this.resolvePlaceholder(
				placeholderName, props, PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK);
			return value;
		}
	}


	public RunEnvironment getEnvironment() {
		return environment;
	}


	public void setEnvironment( RunEnvironment environment ) {
		this.environment = environment;
	}

}
