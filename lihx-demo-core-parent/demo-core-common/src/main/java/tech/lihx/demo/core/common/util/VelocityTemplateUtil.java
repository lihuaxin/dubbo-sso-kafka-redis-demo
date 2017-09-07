package tech.lihx.demo.core.common.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 处理配置文件信息专用
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-4 17:48:18
 */
public class VelocityTemplateUtil {

	static {
		Properties p = new Properties();
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
		p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		Velocity.init(p);
	}


	public static String getConfiguration( Map<Object, Object> map, String templateName ) {
		VelocityContext context = new VelocityContext(map);
		StringWriter writer = new StringWriter();
		Velocity.getTemplate(templateName).merge(context, writer);
		return writer.toString();
	}


	@SuppressWarnings( "deprecation" )
	public static String getConfiguration( Map<Object, Object> map, InputStream input ) {
		VelocityContext context = new VelocityContext(map);
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "VelocityTemplateUtil", input);
		return writer.toString();
	}


	/**
	 * 使用velocity 替换模板字符串中的变量
	 * <p>
	 * 
	 * @param templateStr
	 * @param map
	 * @return
	 */
	public static String getTemplateString( String templateStr, Map<Object, Object> map ) {
		StringWriter write = new StringWriter();
		Velocity.evaluate(new VelocityContext(map), write, "", templateStr);
		return write.toString();
	}

}
