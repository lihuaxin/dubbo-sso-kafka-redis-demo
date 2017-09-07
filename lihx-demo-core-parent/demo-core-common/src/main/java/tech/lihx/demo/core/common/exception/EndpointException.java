package tech.lihx.demo.core.common.exception;


/**
 * 定义Endpoint层抛出异常类型
 * <p>
 * 若Endpoint层抛出异常，请抛出此异常
 * 
 * @author lihx
 * @date 2015年11月24日
 * @version 1.0.0
 */
@SuppressWarnings( "serial" )
public class EndpointException extends RuntimeException {

	// 异常代码
	private String code;

	// 异常说明
	private String desc;


	public EndpointException() {
		super();
	}


	public EndpointException( String message ) {
		super(message);
		this.desc = message;
	}


	public EndpointException( Throwable cause ) {
		super(cause);
	}


	public EndpointException( String code, String desc ) {
		this.code = code;
		this.desc = desc;
	}


	public EndpointException( String code, String desc, Throwable cause ) {
		super(cause);
		this.code = code;
		this.desc = desc;
	}


	public EndpointException( String code, String desc, String message ) {
		super(message);
		this.code = code;
		this.desc = desc;
	}


	@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
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
