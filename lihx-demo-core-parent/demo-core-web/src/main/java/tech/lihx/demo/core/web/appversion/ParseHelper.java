/**
 * ParseHelper.java cn.vko.core.web.app Copyright (c) 2015, 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.web.appversion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tech.lihx.demo.core.sso.common.util.EnvUtil;
import tech.lihx.demo.core.sso.web.WebSSOConfigurer;
import tech.lihx.demo.core.web.appversion.web.AppVersionConfigurer;

/**
 * appVersionXml 解析
 * <p>
 * 
 * @author LHX
 * @date 2015年4月28日
 * @version 1.0.0
 */
public class ParseHelper {

	private static final Logger logger = LoggerFactory.getLogger(ParseHelper.class);

	private static VersionComprator cmp = new VersionComprator();


	/**
	 * 
	 * <p>
	 * 
	 * @param type
	 *            类型
	 * @param name
	 *            名称
	 * @param version
	 *            版本
	 * @return AppVersion
	 */
	public static AppVersion getAppVersion( String type, String name, String version ) {
		String location = AppVersionConfigurer.CONFIG_LOCATION;
		AppVersion appVersion = null;
		if ( location != null ) {
			InputStream in = null;
			try {
				if ( location.indexOf("classpath") >= 0 ) {
					String[] cfg = location.split(":");
					if ( cfg.length == 2 ) {
						in = WebSSOConfigurer.class.getClassLoader().getResourceAsStream(cfg[1]);
					}
				} else {
					/**
					 * 测试环境配置目录 c 盘根目录 app_version.xml
					 */
					if ( !EnvUtil.isLinux() ) {
						location = "c://app_version.xml";
					}

					File file = new File(location);
					if ( file.isFile() ) {
						in = new FileInputStream(file);
					}
				}

				if ( in != null ) {
					HashMap<String, AppVersion> avm = parseXml(in);
					AppVersion av = avm.get(type + "#" + name);
					if ( av != null ) {
						if ( cmp.compare(version, av.getType()) >= 1 ) {
							appVersion = av;
						}
					}
				}
			} catch ( Exception e ) {
				logger.error(e.getMessage(), e);
			} finally {
				if ( in != null ) {
					try {
						in.close();
					} catch ( IOException e ) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
		return appVersion;
	}


	/**
	 * HTTP GET 获取远程APK版本文件流
	 * 
	 * @param urlPath
	 *            version.xml文件地址
	 * @return
	 */
	protected static InputStream httpGetXml( String urlPath ) {
		if ( urlPath == null ) { return null; }
		try {
			URL url = new URL(urlPath.trim());
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setConnectTimeout(10000);
			urlCon.setReadTimeout(30000);
			byte[] cxt = readStream(urlCon.getInputStream());
			logger.debug(" app_version.xml content:{}", new String(cxt));
			return new ByteArrayInputStream(cxt);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}


	/**
	 * 解析appVersionXml版本信息
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	private static HashMap<String, AppVersion> parseXml( InputStream inStream ) throws Exception {
		HashMap<String, AppVersion> avm = new HashMap<String, AppVersion>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(inStream);
		NodeList childNodes = document.getChildNodes();
		for ( int i = 0 ; i < childNodes.getLength() ; i++ ) {
			Node childNode = childNodes.item(i);
			NodeList nodeList = childNode.getChildNodes();
			for ( int j = 0 ; j < nodeList.getLength() ; j++ ) {
				Node node = nodeList.item(j);
				if ( node instanceof Element ) {
					AppVersion appVersion = new AppVersion();
					// 解析节点属性
					NamedNodeMap attrs = node.getAttributes();
					appVersion.setType(attrs.getNamedItem("type").getNodeValue());
					appVersion.setVersion(attrs.getNamedItem("version").getNodeValue());

					// 解析子节点
					NodeList nodeMeta = node.getChildNodes();
					for ( int k = 0 ; k < nodeMeta.getLength() ; k++ ) {
						Node meta = nodeMeta.item(k);
						if ( "name".equals(meta.getNodeName()) ) {
							appVersion.setName(meta.getTextContent());
						} else if ( "downurl".equals(meta.getNodeName()) ) {
							appVersion.setDownurl(meta.getTextContent());
						} else if ( "description".equals(meta.getNodeName()) ) {
							appVersion.setDescription(meta.getTextContent());
						}
					}
					avm.put(appVersion.getType() + "#" + appVersion.getName(), appVersion);
				}
			}
		}
		return avm;
	}


	/**
	 * 读取流
	 * 
	 * @param inStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] readStream( InputStream inStream ) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ( (len = inStream.read(buffer)) != -1 ) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}


	/**
	 * 测试
	 */
	public static void main( String[] args ) {
		try {
			InputStream in = null;
			File file = new File("c://app_version.xml");
			if ( file.isFile() ) {
				in = new FileInputStream(file);
			}
			HashMap<String, AppVersion> avm = parseXml(in);
			Iterator<Entry<String, AppVersion>> it = avm.entrySet().iterator();
			while ( it.hasNext() ) {
				System.out.println(it.next().getValue().getDescription());
			}
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}
}
