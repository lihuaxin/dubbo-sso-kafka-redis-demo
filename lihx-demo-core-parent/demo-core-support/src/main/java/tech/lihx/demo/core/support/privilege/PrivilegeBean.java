package tech.lihx.demo.core.support.privilege;


/**
 * 传递权限数据
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:17:04
 */
public class PrivilegeBean {

	private String className;

	private String methodName;

	private String parent;

	private String key;

	private String desc;

	private String[] uri;

	private boolean control;


	public String getClassName() {
		return className;
	}


	public void setClassName( String className ) {
		this.className = className;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName( String methodName ) {
		this.methodName = methodName;
	}


	public String getParent() {
		return parent;
	}


	public void setParent( String parent ) {
		this.parent = parent;
	}


	public String getKey() {
		return key;
	}


	public void setKey( String key ) {
		this.key = key;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc( String desc ) {
		this.desc = desc;
	}


	public String[] getUri() {
		return uri;
	}


	public void setUri( String[] uri ) {
		this.uri = uri;
	}


	public boolean isControl() {
		return control;
	}


	public void setControl( boolean control ) {
		this.control = control;
	}

}
