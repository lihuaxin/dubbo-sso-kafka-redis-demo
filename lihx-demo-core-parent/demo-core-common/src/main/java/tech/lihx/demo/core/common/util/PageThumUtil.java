package tech.lihx.demo.core.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 网页截图工具类
 * 
 * @author LHX
 * @since 2016-07-05
 *
 */
public class PageThumUtil {

	private static final Logger logger = LoggerFactory.getLogger(PageThumUtil.class);


	/**
	 * 截取网页图片，并且生成缩略图
	 * 
	 * /usr/local/cutycapt/xvfb-run.sh --server-args="-screen 0, 1024x768x24"
	 * /usr/local/cutycapt/CutyCapt --url=http://miyoo.cn --out=163.jpg
	 * 
	 * //convert截取第一屏 convert -crop 1024x768+0+0 /home/163.jpg /home/163_fs.jpg
	 * 
	 * //convert缩放第一屏 convert -resize 20%x20% /home/163.jpg
	 * /home/163a4._20_20.jpg
	 * 
	 * @param run
	 *            linux下的执行脚本 /usr/local/cutycapt/xvfb-run.sh
	 * @param cutycapt
	 *            linux下的截图命令 /usr/local/cutycapt/CutyCapt
	 * @param site
	 *            网站地址: http://miyoo.cn/
	 * @param dest
	 *            目标图片文件保存目录 /home/163.jpg
	 */
	public static void transfer( String run, String cutycapt, String site, String dest ) {
		// 生成转换命令
		StringBuffer command = new StringBuffer();
		command.append(run).append(" ");
		command.append("--server-args=\"-screen 0, 1024x768x24\"").append(" ");
		command.append(cutycapt).append(" ");
		command.append("--url=").append(site).append(" ");
		command.append("--out=").append(dest);

		if ( logger.isInfoEnabled() ) {
			logger.info("网页截图命令：" + command.toString());
		}

		try {
			String[] cmd = new String[ ] { "/bin/sh", "-c", command.toString() };
			Process process = Runtime.getRuntime().exec(cmd);
			if ( logger.isInfoEnabled() ) {
				logger.info("[1.2]正在截取网页图片....");
			}

			int result = process.waitFor();
			if ( logger.isInfoEnabled() ) {
				logger.info("[1.3]截取结束 *********result:" + result);
			}
			InputStream is = process.getInputStream();
			byte[] b = new byte[1024];
			is.read(b);
			if ( logger.isInfoEnabled() ) {
				logger.info("[1.4]截取结束 后*********b:" + b);
			}
			is.close();

		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		} catch ( InterruptedException e ) {
			logger.error(e.getMessage(), e);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 截取网页第一屏
	 * 
	 * @param sourceFile
	 * @param destFile
	 */
	public static void covertFirst( String sourceFile, String destFile ) {
		// 生成转换命令
		StringBuffer command = new StringBuffer();
		command.append("convert -crop  1024x768+0+0 ");
		command.append(sourceFile).append(" ");
		command.append(destFile);

		if ( logger.isInfoEnabled() ) {
			logger.info("截取网页第一屏命令：" + command.toString());
		}

		try {
			String[] cmd = new String[ ] { "/bin/sh", "-c", command.toString() };
			Process process = Runtime.getRuntime().exec(cmd);
			if ( logger.isInfoEnabled() ) {
				logger.info("[2.2]截取第一屏....");
			}
			int result = process.waitFor();
			if ( logger.isInfoEnabled() ) {
				logger.info("[2.3]截取第一屏结束 *********result:" + result);
			}
			InputStream is = process.getInputStream();
			byte[] b = new byte[1024];
			is.read(b);
			if ( logger.isInfoEnabled() ) {
				logger.info("[2.4]截取第一屏结束 后*********b:" + b);
			}
			is.close();

		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		} catch ( InterruptedException e ) {
			logger.error(e.getMessage(), e);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 生成20%x20%缩略图
	 * 
	 * @param sourceFile
	 * @param destFile
	 */
	public static void genSnapShot( String sourceFile, String destFile ) {
		// 生成转换命令
		StringBuffer command = new StringBuffer();
		command.append("convert -resize 20%x20% ");
		command.append(sourceFile).append(" ");
		command.append(destFile);


		if ( logger.isInfoEnabled() ) {
			logger.info("生成网页缩略图命令：" + command.toString());
		}

		try {
			String[] cmd = new String[ ] { "/bin/sh", "-c", command.toString() };
			Process process = Runtime.getRuntime().exec(cmd);
			if ( logger.isInfoEnabled() ) {
				logger.info("[3.2]生成网页缩略图....");
			}

			int result = process.waitFor();
			if ( logger.isInfoEnabled() ) {
				logger.info("[3.3]生成网页缩略图结束 *********result:" + result);
			}
			InputStream is = process.getInputStream();
			byte[] b = new byte[1024];
			is.read(b);
			if ( logger.isInfoEnabled() ) {
				logger.info("[3.4]生成网页缩略图结束 后*********b:" + b);
			}
			is.close();

		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		} catch ( InterruptedException e ) {
			logger.error(e.getMessage(), e);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	/**
	 * 生成45%x45%缩略图
	 * 
	 * @param sourceFile
	 * @param destFile
	 */
	public static void genSnapShotA( String sourceFile, String destFile ) {
		// 生成转换命令
		StringBuffer command = new StringBuffer();
		command.append("convert -resize 45%x45% ");
		command.append(sourceFile).append(" ");
		command.append(destFile);


		if ( logger.isInfoEnabled() ) {
			logger.info("生成网页缩略图命令：" + command.toString());
		}

		try {
			String[] cmd = new String[ ] { "/bin/sh", "-c", command.toString() };
			Process process = Runtime.getRuntime().exec(cmd);
			if ( logger.isInfoEnabled() ) {
				logger.info("[3.2]生成网页缩略图....");
			}

			int result = process.waitFor();
			if ( logger.isInfoEnabled() ) {
				logger.info("[3.3]生成网页缩略图结束 *********result:" + result);
			}
			InputStream is = process.getInputStream();
			byte[] b = new byte[1024];
			is.read(b);
			if ( logger.isInfoEnabled() ) {
				logger.info("[3.4]生成网页缩略图结束 后*********b:" + b);
			}
			is.close();

		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		} catch ( InterruptedException e ) {
			logger.error(e.getMessage(), e);
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
	}


	public static void main( String[] args ) {
		PageThumUtil.transfer(
			"/usr/local/cutycapt/xvfb-run.sh", "/usr/local/cutycapt/CutyCapt", "http://miyoo.cn", "/home/163_fs.jpg");
	}
}
