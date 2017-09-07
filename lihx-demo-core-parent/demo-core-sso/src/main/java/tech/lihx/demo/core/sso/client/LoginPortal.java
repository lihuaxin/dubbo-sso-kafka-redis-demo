package tech.lihx.demo.core.sso.client;


/**
 * 登录入口
 * <p>
 * 
 * @author LHX
 * @date 2015年1月12日
 * @version v1.0.0
 */
public enum LoginPortal {
	/**
	 * 包含 restrict_ 表示限制一个账号同时登录
	 */
	WEB("restrict_web", "web端登录"), // web
	WEB_SCHOOL("restrict_web_other", "第三方登录"), // web_school
	WEBAPP_HTML5("webapp_html5", "webapp_html5端登录"), // webapp_html5
	WEBAPP_OCS("webapp_ocs", "webapp_ocs端登录"), // webapp_ocs
	WEBAPP_EE("webapp_ee", "webapp_ee端登录"), // webapp_ee
	ANDROID("android", "android端登录"), // android
	IPHONE("iphone", "iphone端登录"); // iphone

	private final String type;

	private final String desc;


	LoginPortal( final String type, final String desc ) {
		this.type = type;
		this.desc = desc;
	}


	public String getType() {
		return this.type;
	}


	public String getDesc() {
		return this.desc;
	}
}
