package tech.lihx.demo.core.web.appversion;

import java.io.Serializable;


/**
 * app 版本
 * <p>
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class AppVersion implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;// 类型 apk 安卓设备 ， ipa 苹果设备

	private String name;// 名称

	private String version;// 版本

	private String downurl;// 下载地址

	private String description;// 描述


	public String getType() {
		return type;
	}


	public void setType( String type ) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName( String name ) {
		this.name = name;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion( String version ) {
		this.version = version;
	}


	public String getDownurl() {
		return downurl;
	}


	public void setDownurl( String downurl ) {
		this.downurl = downurl;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription( String description ) {
		this.description = description;
	}

}
