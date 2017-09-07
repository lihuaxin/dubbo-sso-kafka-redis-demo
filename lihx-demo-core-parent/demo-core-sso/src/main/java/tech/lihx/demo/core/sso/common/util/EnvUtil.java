package tech.lihx.demo.core.sso.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 获取系统环境变量工具类
 * <p>
 * 
 * @author LHX
 * @date 2016年1月13日
 * @version v1.0.0
 */
public class EnvUtil {

	private final static Logger logger = LoggerFactory.getLogger(EnvUtil.class);

	private static Boolean OS_LINUX = null;


	/**
	 * 判断当前系统是否为 linux
	 * 
	 * @return true linux, false windows
	 */
	public static boolean isLinux() {
		if ( OS_LINUX == null ) {
			String OS = System.getProperty("os.name").toLowerCase();
			logger.info("os.name: {}", OS);
			if ( OS != null && OS.contains("windows") ) {
				OS_LINUX = false;
			} else {
				OS_LINUX = true;
			}
		}
		return OS_LINUX;
	}


	/**
	 * 返回当前系统变量的函数 结果放至 Properties
	 */
	public static Properties getEnv() throws Exception {
		Properties prop = new Properties();
		Process p = null;
		if ( isLinux() ) {
			p = Runtime.getRuntime().exec("sh -c set");
		} else {
			// windows
			p = Runtime.getRuntime().exec("cmd /c set");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ( (line = br.readLine()) != null ) {
			int i = line.indexOf("=");
			if ( i > -1 ) {
				String key = line.substring(0, i);
				String value = line.substring(i + 1);
				prop.setProperty(key, value);
			}
		}
		br.close();
		return prop;
	}


	/**
	 * LINUX 编辑用法： ############################################# vi /etc/profile
	 * --------------------------------------------- SSO_LOGIN=0 export
	 * SSO_LOGIN --------------------------------------------- source
	 * /etc/profile ############################################# 程序输入结果：0
	 */
	public static void main( String[] args ) {
		try {
			Properties p = EnvUtil.getEnv();
			for ( Iterator<Object> iter = p.keySet().iterator() ; iter.hasNext() ; ) {
				String key = (String) iter.next();
				System.out.println(key + " = " + p.getProperty(key));
			}
			// 注意大小写，比如读取PATH，Linux下为PATH；Windows为Path。
			System.out.println(p.getProperty("SSO_LOGIN"));
		} catch ( Exception e ) {
			System.out.println(e);
		}
	}
}
