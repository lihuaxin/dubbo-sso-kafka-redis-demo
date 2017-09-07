package tech.lihx.demo.core.web.velocity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.activation.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下载图片专用
 * <p>
 * 
 * @author 宋汝波
 * @Date 2014-6-24
 */
public class AttachmentDataSource implements DataSource {

	private static final Logger logger = LoggerFactory.getLogger(AttachmentDataSource.class);

	private byte[] data;

	String strUrl;


	public AttachmentDataSource( String strUrl ) {
		this.strUrl = strUrl.trim();
		this.strUrl = this.strUrl.replace(" ", "%20");
		data = downBinaryFile(strUrl);

	}


	public static byte[] downBinaryFile( String s ) {
		InputStream input = null;
		try {

			URL url = new URL(s);

			input = url.openStream();

			return IOUtils.toByteArray(input);

		} catch ( Exception e ) {
			return null;
		} finally {
			close(input);
		}

	}


	public static void close( InputStream input ) {
		if ( input != null ) {
			try {
				input.close();
			} catch ( IOException e ) {
				logger.error(e.getMessage(), e);
			}
		}
	}


	private String getMimeType( String fileName ) {
		String type = null;
		if ( fileName.endsWith(".png") ) {
			type = "image/png";
		} else if ( fileName.endsWith(".jpg") ) {
			type = "image/jpeg";
		} else if ( fileName.endsWith(".bmp") ) {
			type = "image/bmp";
		} else if ( fileName.endsWith(".gif") ) {
			type = "image/gif";
		}
		if ( type == null ) {
			type = "application/octet-stream";
		}
		return type;

	}


	@Override
	public String getContentType() {

		return getMimeType(strUrl.toLowerCase());

	}


	@Override
	public InputStream getInputStream() throws IOException {
		if ( data == null ) {
			data = new byte[0];
		}
		return new ByteArrayInputStream(data);

	}


	@Override
	public String getName() {
		char separator = '/';
		if ( strUrl.lastIndexOf(separator) >= 0 ) { return strUrl.substring(strUrl.lastIndexOf(separator) + 1); }
		return strUrl;

	}


	@Override
	public OutputStream getOutputStream() throws IOException {

		return new ByteArrayOutputStream();

	}

}
