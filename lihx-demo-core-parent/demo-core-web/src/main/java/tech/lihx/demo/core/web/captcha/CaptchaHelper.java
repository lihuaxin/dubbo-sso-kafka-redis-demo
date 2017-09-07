package tech.lihx.demo.core.web.captcha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.patchca.background.BackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.ConfigurableFilterFactory;
import org.patchca.filter.library.AbstractImageOp;
import org.patchca.filter.library.WobbleImageOp;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Captcha 验证码帮助类
 * <p>
 * 
 * @author LHX
 * @date 2017年9月6日
 * @version 1.0.0
 */
public class CaptchaHelper {

	private static final Logger logger = LoggerFactory.getLogger(CaptchaHelper.class);


	/**
	 * 保存验证码 HttpSession
	 * <p>
	 * 
	 * @param request
	 * @param response
	 * @return CheckCode 验证码对象
	 */
	public static CheckCode saveCaptcha( HttpServletRequest request, HttpServletResponse response ) {
		CheckCode checkCode = null;
		// 随机数
		String range = request.getParameter("range");
		if ( StringUtils.isNotEmpty(range) ) {
			// 验证票据
			String token = request.getParameter("token");
			if ( StringUtils.isNotEmpty(token) ) {
				try {
					checkCode = new CheckCode(token, range, generateCaptcha(response));
				} catch ( Exception e ) {
					logger.error(" generateCaptcha error: {}", e.toString());
				}
			} else {
				logger.debug(" saveCaptcha ctoken is empty.");
			}
		} else {
			logger.debug(" saveCaptcha range is empty.");
		}
		return checkCode;
	}


	/**
	 * @Description 验证码是否正确
	 * @param checkCode
	 *            验证码对象
	 * @param value
	 *            验证码内容
	 * @return
	 */
	public static boolean verifyCaptcha( CheckCode checkCode, String value ) {

		if ( checkCode != null && StringUtils.isNotEmpty(value) ) {
			logger.debug(
				" verify captcha . token:{} , checkcode:{}, input value:{}", checkCode.getToken(), checkCode.getCode(),
				value);
			/**
			 * 忽略验证大小写 正确返回 true
			 */
			if ( value.equalsIgnoreCase(checkCode.getCode()) ) {
				return true;
			} else {
				logger.debug(" verify captcha failed .");
			}
		} else {
			logger.debug(" verify captcha . checkCode is null or value is empty. ");
		}

		return false;
	}


	/**
	 * 生成验证码返回字符串内容
	 * <p>
	 * 
	 * @param response
	 * @return String 验证码内容
	 * @throws Exception
	 */
	private static String generateCaptcha( HttpServletResponse response ) throws Exception {
		// 验证码宽高设置
		ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
		cs.setWidth(119);
		cs.setHeight(42);

		// 字符颜色设置
		ColorFactory colorFactory = new SingleColorFactory(Color.blue);
		cs.setColorFactory(colorFactory);

		// 字符大小设置
		RandomFontFactory rf = new RandomFontFactory();
		rf.setMinSize(28);
		rf.setMaxSize(32);
		cs.setFontFactory(rf);

		// 图片滤镜设置
		ConfigurableFilterFactory filterFactory = new ConfigurableFilterFactory();
		List<BufferedImageOp> filters = new ArrayList<BufferedImageOp>();
		WobbleImageOp wobbleImageOp = new WobbleImageOp();
		wobbleImageOp.setEdgeMode(AbstractImageOp.EDGE_CLAMP);
		wobbleImageOp.setxAmplitude(2.0);
		wobbleImageOp.setyAmplitude(1.0);
		filters.add(wobbleImageOp);

		// 生成干扰线
		// CurvesImageOp curvesImageOp = new CurvesImageOp();
		// curvesImageOp.setColorFactory(colorFactory);
		// curvesImageOp.setEdgeMode(AbstractImageOp.EDGE_ZERO);
		// filters.add(curvesImageOp);
		filterFactory.setFilters(filters);
		cs.setFilterFactory(filterFactory);

		// 设置随机4个字符串
		MyRandomWordFactory rw = new MyRandomWordFactory();
		rw.setMinLength(4);
		rw.setMaxLength(4);
		rw.setCharacters("abcdefhjkmnpqrstuvwxyz235678");
		cs.setWordFactory(rw);

		// 验证码渲染设置
		BestFitTextRenderer bft = new BestFitTextRenderer();
		bft.setTopMargin(5);
		bft.setBottomMargin(5);
		bft.setRightMargin(5);
		bft.setLeftMargin(5);
		cs.setTextRenderer(bft);

		// 自定义验证码图片背景
		MyCustomBackgroundFactory backgroundFactory = new MyCustomBackgroundFactory();
		cs.setBackgroundFactory(backgroundFactory);

		// 输出图片流
		OutputStream os = response.getOutputStream();
		String val = EncoderHelper.getChallangeAndWriteImage(cs, "png", os);
		logger.debug(" captcha checkcode {} ,  thread id:{}", val, Thread.currentThread().getId());
		os.flush();// 输入完毕、清除缓冲
		os.close();// 关闭流
		return val;
	}
}

/**
 * 自定义随机字符获取工厂
 * <p>
 * 
 * @author hubin
 * @date 2014-12-25
 * @version 1.0.0
 */
class MyRandomWordFactory extends RandomWordFactory {

	/**
	 * 重载父类获取字符方法 支持随机大小写字符
	 */
	@Override
	public String getNextWord() {
		Random rnd = new Random();
		StringBuffer sb = new StringBuffer();
		int l = this.minLength + (this.maxLength > this.minLength ? rnd.nextInt(this.maxLength - this.minLength) : 0);
		for ( int i = 0 ; i < l ; i++ ) {
			int j = rnd.nextInt(this.characters.length());
			if ( rnd.nextBoolean() ) {
				sb.append(this.characters.toUpperCase().charAt(j));
			} else {
				sb.append(this.characters.charAt(j));
			}
		}
		return sb.toString();
	}
}

/**
 * 自定义验证码图片背景,主要画一些噪点和干扰线
 * <p>
 * 
 * @author hubin
 * @date 2014-12-25
 * @version 1.0.0
 */
class MyCustomBackgroundFactory implements BackgroundFactory {

	private final Random random = new Random();


	@Override
	public void fillBackground( BufferedImage image ) {
		Graphics graphics = image.getGraphics();

		// 验证码图片的宽高
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		// 填充为白色背景
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, imgWidth, imgHeight);

		// 画100个噪点(颜色及位置随机)
		for ( int i = 0 ; i < 100 ; i++ ) {
			// 随机颜色
			int rInt = random.nextInt(255);
			int gInt = random.nextInt(255);
			int bInt = random.nextInt(255);

			graphics.setColor(new Color(rInt, gInt, bInt));

			// 随机位置
			int xInt = random.nextInt(imgWidth - 3);
			int yInt = random.nextInt(imgHeight - 2);

			// 随机旋转角度
			int sAngleInt = random.nextInt(60);
			int eAngleInt = random.nextInt(360);

			// 随机大小
			int wInt = random.nextInt(6);
			int hInt = random.nextInt(6);

			graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);

			// 画5条干扰线
			if ( i % 20 == 0 ) {
				int xInt2 = random.nextInt(imgWidth);
				int yInt2 = random.nextInt(imgHeight);
				graphics.drawLine(xInt, yInt, xInt2, yInt2);
			}
		}
	}
}
