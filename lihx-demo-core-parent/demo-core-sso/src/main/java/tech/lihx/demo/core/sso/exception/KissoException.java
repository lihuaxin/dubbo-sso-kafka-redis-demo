package tech.lihx.demo.core.sso.exception;

/**
 * SSO 异常
 * <p>
 * 
 * @author LHX
 * @Date 2016-5-9
 */
public class KissoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public KissoException( String message ) {
		super("Kisso exception.." + message);
	}

}
