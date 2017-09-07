package tech.lihx.demo.core.common.exception.support;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.environment.EnvironmentDetect;
import tech.lihx.demo.core.common.util.MurMurHash;

import com.alibaba.fastjson.JSON;


/**
 * <p>
 * 
 * @author lihx
 * @date 2015年2月9日
 * @version 1.0.0
 */
public class MailSupport {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private Mail mail;

	private final ConcurrentSkipListSet<Long> cache = new ConcurrentSkipListSet<Long>();


	public void send( Exception ex, String subject, Object token, Object[] params ) {
		if ( mail != null ) {
			if ( !EnvironmentDetect.detectEnvironment().isProduct() ) { return; }
			try {
				if ( ex == null ) { return; }
				StringWriter writer = new StringWriter(30);
				if ( token != null ) {
					writer.write("UserToken:");
					writer.write(JSON.toJSONString(token));
					writer.write("<br>");
					writer.write("<br>");
				}
				if ( params != null ) {
					writer.write("Method Parameter:");
					writer.write(JSON.toJSONString(params));
					writer.write("<br>");
					writer.write("<br>");
				}
				StringWriter exception = new StringWriter(500);
				ex.printStackTrace(new PrintWriter(exception));
				String ex_str = exception.toString();
				exception.close();
				Long hash = MurMurHash.hash(ex_str);
				// 设置相同异常只发一次
				if ( !cache.contains(hash) ) {
					cache.add(hash);
					writer.write(ex_str);

					String content = writer.toString();
					writer.close();
					content = content.replace(System.getProperty("line.separator"), "<br>").replace(
						"\t", "<span style='margin-left: 20px;'></span>");
					mail.sendMail(subject, content, "云培训");
				}

			} catch ( Exception e ) {
				log.error("", e);
			}
		}
	}


	public Mail getMail() {
		return mail;
	}


	public void setMail( Mail mail ) {
		this.mail = mail;
	}
}
