package tech.lihx.demo.core.common.exception.support;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * 异常发邮件
 * <p>
 * 
 * @author lihx
 * @date 2015年2月9日
 * @version 1.0.0
 */
public class ExceptionMail implements Mail {

	protected static final Logger log = LoggerFactory.getLogger(ExceptionMail.class);

	private JavaMailSender javaMailSender;

	private String email = "testfor15@vko.cn";

	private String passwd = "vko123456";

	private String[] users;


	@Override
	public void sendMail( String subject, String htmlText, String siteName ) {
		try {
			if ( javaMailSender == null ) {
				JavaMailSenderImpl sender = new JavaMailSenderImpl();
				Properties javaMailProperties = new Properties();
				javaMailProperties.setProperty("mail.smtp.aut", "true");
				javaMailProperties.setProperty("mail.smtp.timeout", "25000");
				sender.setJavaMailProperties(javaMailProperties);
				sender.setHost("smtp.exmail.qq.com");
				sender.setUsername(email);
				sender.setPassword(passwd);
				javaMailSender = sender;
			}

			MimeMessage msg = javaMailSender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(msg, "UTF-8");
			msgHelper.setFrom(email, siteName);
			msgHelper.setTo(users);
			msgHelper.setSubject(subject);
			msgHelper.setText(htmlText, true);

			javaMailSender.send(msg);
		} catch ( Exception e ) {
			log.error("发送邮件异常", e);
		}
	}


	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}


	public void setJavaMailSender( JavaMailSender javaMailSender ) {
		this.javaMailSender = javaMailSender;
	}


	public String getEmail() {
		return email;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setEmail( String email ) {
		this.email = email;
	}


	public void setPasswd( String passwd ) {
		this.passwd = passwd;
	}


	public String[] getUsers() {
		return users;
	}


	public void setUsers( String[] users ) {
		this.users = users;
	}


}
