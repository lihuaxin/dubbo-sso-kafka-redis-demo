package tech.lihx.demo.core.common.util;

import javax.servlet.ServletContext;

/**
 * 初始化工具类
 * 
 * @author LHX
 * @Date 2015-11-16
 */
public final class InitUtil {

	/**
	 * Logger for this class
	 */

	public static final String GLOBAL_CONFIG_KEY = "vgc";


	private InitUtil() {

	}


	/**
	 * 初始化web全局的配置
	 *
	 * @param config
	 *            配置
	 * @param sc
	 *            全局上下文
	 */
	public static void initWebGlobalConfig( final KvConfig config, final ServletContext sc ) {
		if ( config == null || sc == null ) { return; }
		sc.setAttribute(GLOBAL_CONFIG_KEY, config.getValues());
		sc.setAttribute(ConfigKey.STATIC.key(), config.getValue(ConfigKey.STATIC));
	}

	/**
	 * 初始化ftl全局的配置
	 *
	 * @param config
	 *            配置
	 * @param ftlConf
	 *            ftl的配置
	 */
	// public static void initFtlGlobalConfig(final KvConfig config, final
	// Configuration ftlConf) {
	// if (config == null || ftlConf == null) {
	// return;
	// }
	// try {
	// ftlConf.setSharedVariable(GLOBAL_CONFIG_KEY, config.getValues());
	// ftlConf.setSharedVariable(ConfigKey.STATIC.key(),
	// config.getValue(ConfigKey.STATIC));
	// } catch (TemplateModelException e) {
	//			logger.error("设置ftl公用属性时出错！", e); //$NON-NLS-1$
	// }
	// }
}
