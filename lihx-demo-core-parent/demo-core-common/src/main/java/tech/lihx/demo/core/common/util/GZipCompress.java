package tech.lihx.demo.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZipCompress {

	private static final Logger logger = LoggerFactory.getLogger(GZipCompress.class);


	public static byte[] doCompress( byte[] inFileName ) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			GZIPOutputStream out = new GZIPOutputStream(outputStream);
			out.write(inFileName);
			out.finish();
			out.close();
			return outputStream.toByteArray();
		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		}
		return new byte[0];
	}


	public static byte[] doUncompress( byte[] inFileName ) {

		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(inFileName);
			GZIPInputStream in = new GZIPInputStream(inputStream);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while ( (len = in.read(buf)) > 0 ) {
				outputStream.write(buf, 0, len);
			}
			in.close();
			outputStream.close();
			return outputStream.toByteArray();
		} catch ( IOException e ) {
			logger.error(e.getMessage(), e);
		}
		return new byte[0];
	}

}
