package tech.lihx.demo.core.web.velocity;

import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

import tech.lihx.demo.core.common.util.ApplicationUtil;
import tech.lihx.demo.core.web.filter.Invocation;
import tech.lihx.demo.core.web.filter.InvocationUtils;
import tech.lihx.demo.core.web.html.ResponseHtmlWrapper;


/**
 * <p>
 * 
 * @author LHX
 * @date 2014年11月17日
 * @version 1.0.0
 */
public class VelocityHtmlUtil {

	final static Pattern VALUE = Pattern.compile("url\\s*\\((.*)\\)");

	final static Pattern STYLE = Pattern.compile("(?i)background\\s*:\\s*(?i)url\\s*\\(.*\\)");

	final static Logger logger = LoggerFactory.getLogger(VelocityHtmlUtil.class);

	private static VelocityEngine velocityEngine;

	private static ViewResolver viewResolver;

	private static WebApplicationContext context;


	static {
		init();
	}


	/**
	 * 根据传入的模板和数据生成html
	 * <p>
	 *
	 * @param template
	 * @param map
	 * @return html代码
	 */
	public static String getHtml( String template, Map<Object, Object> map ) {
		StringWriter write = new StringWriter();
		velocityEngine.getTemplate(template).merge(new VelocityContext(map), write);
		return write.toString();
	}


	/**
	 * 使用velocity 替换模板字符串中的变量
	 * <p>
	 * 
	 * @param templateStr
	 * @param map
	 * @return
	 */
	public static String getTemplateString( String templateStr, Map<Object, Object> map ) {
		StringWriter write = new StringWriter();
		velocityEngine.evaluate(new VelocityContext(map), write, "", templateStr);
		return write.toString();
	}


	/**
	 * 初始化
	 * <p>
	 *
	 * @param
	 */
	private static synchronized void init() {
		context = ApplicationUtil.getContext();
		viewResolver = context.getBean(ViewResolver.class);
		velocityEngine = context.getBean(VelocityConfigurer.class).getVelocityEngine();
	}


	/**
	 * 生成html
	 * <p>
	 *
	 * @param template
	 * @param model
	 * @return html内容
	 */
	public static String getHtml( String template, Model model ) {
		try {
			Invocation invocation = InvocationUtils.getCurrentThreadInvocation();
			View view = viewResolver.resolveViewName(template, Locale.SIMPLIFIED_CHINESE);
			if ( view == null ) { throw new RuntimeException("未找到对应模板,请检查路径"); }
			ResponseHtmlWrapper htmlWrapper = new ResponseHtmlWrapper(invocation.getResponse());
			view.render(model.asMap(), invocation.getRequest(), htmlWrapper);
			return htmlWrapper.getHtml();
		} catch ( Exception e ) {
			logger.error("生成html错误", e);
		}
		return "";
	}


	public static void toWordMHT( String html, OutputStream out ) throws Exception {
		Document doc = Jsoup.parse(html, "http://tiku.vko.cn");
		// 处理图片img和style里面的样式
		Elements imageElements = doc.select("img");
		List<String> imageUrl = new ArrayList<String>();
		for ( int i = 0 , size = imageElements.size() ; i < size ; i++ ) {
			imageUrl.add(imageElements.get(i).absUrl("src"));
		}
		Elements elements = doc.getElementsByAttributeValueMatching("style", STYLE);
		for ( int i = 0 ; i < elements.size() ; i++ ) {
			String attr = elements.get(i).attr("style");
			Matcher matcher = VALUE.matcher(attr);
			if ( matcher.find() ) {
				String value = (matcher.group(1).replace("'", "").replace("\"", "")).trim();
				imageUrl.add(value);
			}
		}
		createWordArchive(html, out, imageUrl);
	}


	/**
	 * 
	 * 将给定内容保存为doc或mht文件
	 * 
	 * 
	 * 
	 * @param strContent
	 *            正文内容
	 * 
	 * @param strEncoding
	 *            编码，默认GBK
	 * 
	 * @param strFilePath
	 *            保存位置
	 * 
	 * @param listImgAbsURL
	 *            图片绝对地址集合
	 */

	private static void createWordArchive( String htmlContent, OutputStream out, List<String> listImgAbsURL )
		throws Exception {
		MimeMultipart mp = new MimeMultipart("related");
		Properties props = new Properties();
		// props.put("mail.smtp.host", "mail.vko.cn");
		Session session = Session.getInstance(props);
		MimeMessage msg = new MimeMessage(session);
		// msg.setHeader("X-Mailer", "Code Manager .SWT");
		msg.setHeader("Copyright", "www.vko.cn");
		// 设置网页正文
		MimeBodyPart bp = new MimeBodyPart();
		bp.setText(htmlContent, "UTF-8");
		bp.addHeader("Content-Type", "text/html;charset=UTF-8");
		bp.addHeader("Content-Location", "http://tiku.vko.cn");
		mp.addBodyPart(bp);
		int urlCount = listImgAbsURL.size();
		for ( int i = 0 ; i < urlCount ; i++ ) {
			bp = new MimeBodyPart();
			String strImgAbsURL = listImgAbsURL.get(i).toString();
			bp.addHeader("Content-Location", MimeUtility.encodeWord(URLDecoder.decode(strImgAbsURL, "UTF-8")));
			DataSource source = new AttachmentDataSource(strImgAbsURL);
			bp.setDataHandler(new DataHandler(source));
			mp.addBodyPart(bp);
		}
		msg.setContent(mp);
		msg.writeTo(out);

	}
}
