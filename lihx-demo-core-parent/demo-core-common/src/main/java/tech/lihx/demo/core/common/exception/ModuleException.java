package tech.lihx.demo.core.common.exception;


/**
 * 定义Module层应该抛出的异常类型
 * <p>
 * 所有Module层如果出现异常，对外应该抛出此异常
 * 
 * @author lihx
 * @date 2015年11月24日
 * @version 1.0.0
 */
@SuppressWarnings( "serial" )
public class ModuleException extends RuntimeException {

	// 异常代码
	private String code;

	// 异常说明
	private String desc;


	public ModuleException() {
		super();
	}


	public ModuleException( String message ) {
		super(message);
		this.desc = message;
	}


	public ModuleException( String code, String desc ) {
		this.code = code;
		this.desc = desc;
	}


	public ModuleException( String code, String desc, Throwable cause ) {
		super(cause);
		this.code = code;
		this.desc = desc;
	}


	public ModuleException( String code, String desc, String message ) {
		super(message);
		this.code = code;
		this.desc = desc;
	}


	public String getCode() {
		return code;
	}


	public String getDesc() {
		return desc;
	}


	@Override
	public String getMessage() {
		if ( super.getMessage() == null ) { return desc; }
		return super.getMessage();
	}
}
