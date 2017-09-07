package tech.lihx.demo.core.common.exception.support;


/**
 * <p>
 * 
 * @author lihx
 * @date 2015年2月9日
 * @version 1.0.0
 */
public interface Mail {

	public void sendMail( String subject, String htmlText, String siteName );
}
