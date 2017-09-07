package tech.lihx.demo.core.web.wrap.cookie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLOperation implements ObjectSerialize {

	private static final Logger logger = LoggerFactory.getLogger(URLOperation.class);

	private ObjectSerialize ser = null;


	public URLOperation( ObjectSerialize ser ) {
		this.ser = ser;
	}


	@Override
	public Object deSerialize( Object obj ) {
		String code = null;
		try {
			code = URLDecoder.decode((String) obj, "UTF-8");
		} catch ( UnsupportedEncodingException e ) {
			logger.error(e.getMessage(), e);
		}
		return ser.deSerialize(code);
	}


	@Override
	public Object serialize( Object obj ) {
		Object objTemp = ser.serialize(obj);
		String code = null;
		try {
			code = URLEncoder.encode((String) objTemp, "UTF-8");
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		return code;
	}
}
