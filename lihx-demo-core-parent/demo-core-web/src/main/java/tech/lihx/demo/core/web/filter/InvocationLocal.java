package tech.lihx.demo.core.web.filter;

/**
 * 
 * @author LHX
 * 
 */
public interface InvocationLocal extends Invocation {

	/**
	 * 
	 * @return
	 */
	public Invocation getCurrent( boolean required );
}
