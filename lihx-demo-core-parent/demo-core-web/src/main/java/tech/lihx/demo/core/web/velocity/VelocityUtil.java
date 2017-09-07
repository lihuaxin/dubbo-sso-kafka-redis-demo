package tech.lihx.demo.core.web.velocity;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import tech.lihx.demo.core.common.util.Base58;
import tech.lihx.demo.core.web.filter.InvocationUtils;

/**
 * 页面相应函数操作
 * <p>
 *
 * @author 宋汝波
 * @Date 2014-5-13
 */
public class VelocityUtil {

	protected final static Logger logger = LoggerFactory.getLogger(VelocityUtil.class);


	/**
	 * 数字,字符转化
	 * <p>
	 *
	 * @param number
	 * @return
	 */
	public String Int2Char( int number ) {
		return Character.toString((char) number);
	}


	/**
	 * 请求url,带参数
	 * <p>
	 *
	 */
	public String getUrl() {
		HttpServletRequest request = InvocationUtils.getCurrentThreadRequest();
		if ( request == null ) { throw new IllegalStateException("invocation"); }
		String url = request.getRequestURL().toString();
		String query = request.getQueryString();
		StringBuilder str = new StringBuilder(url);
		if ( query != null ) {
			if ( query.contains("_index=") ) {
				query = query.substring(0, query.lastIndexOf("_index="));
			}
			str.append("?");
			str.append(query);
			if ( !query.endsWith("&") ) {
				str.append("&");
			}
		} else {
			str.append("?");
		}
		return str.toString().replace("?&", "?");
	}


	/**
	 * 判断是否为真的null
	 * <p>
	 *
	 * @param obj
	 */
	public boolean isNull( Object obj ) {
		if ( obj == null ) { return true; }

		return false;
	}


	/**
	 * 判断是否为真的null或者""
	 * <p>
	 *
	 * @param obj
	 */
	public boolean isBlank( String obj ) {
		return StringUtils.isBlank(obj);
	}


	public boolean isZero( Object obj ) {
		if ( obj == null || Integer.parseInt(obj.toString()) == 0 ) { return true; }

		return false;
	}


	@SuppressWarnings( "rawtypes" )
	public boolean contains( Object src, Object dest ) {
		if ( src instanceof String ) {
			String str = (String) src;
			return str.contains((String) dest);
		}
		if ( src instanceof Map ) {
			Map map = (Map) src;
			return map.containsKey(dest);
		}
		return false;
	}


	public String formatDate( Date date ) {
		if ( date == null ) { return ""; }

		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}


	public String formatDateTime( Date date ) {
		if ( date == null ) { return ""; }

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}


	public String formatTime( Object allTime ) {
		if ( allTime == null ) { return "0分钟"; }

		long time = new Long(allTime.toString());
		StringBuilder str = new StringBuilder();
		long hour = time / 3600;
		long minute = time % 3600 / 60;
		long second = time % 3600 % 60 % 60;
		if ( hour > 0 ) {
			str.append(hour);
			str.append("小时");
		}
		if ( minute > 0 ) {
			str.append(minute);
			str.append("分钟");
		}
		if ( second > 0 ) {
			str.append(second);
			str.append("秒");
		}
		if ( str.length() == 0 ) {
			str.append("0分钟");
		}

		return str.toString();

	}


	public String idEncode( Object id ) {
		if ( id == null ) { return "0"; }
		if ( id.equals(new Long(0)) ) { return "0"; }
		return Base58.encode(new Long(id.toString()));
	}


	public String idEncode( Object id, String suffix ) {
		return idEncode(id) + suffix;
	}

	public boolean contains( List<?> src, Object value, String property ) {
		if ( src == null ) { return false; }
		for ( int i = 0 ; i < src.size() ; i++ ) {
			Object obj = src.get(i);
			Field field = ReflectionUtils.findField(obj.getClass(), property);
			ReflectionUtils.makeAccessible(field);
			Object newValue = ReflectionUtils.getField(field, obj);
			if ( newValue != null && value != null ) {
				if ( value.equals(newValue) ) { return true; }
			}
		}
		return false;
	}


	public static String getMinute( Integer second ) {
		int h = 0;
		int m = 0;
		int s = 0;
		int temp = second % 3600;
		if ( second > 3600 ) {
			h = second / 3600;
			if ( temp != 0 ) {
				if ( temp > 60 ) {
					m = temp / 60;
					if ( temp % 60 != 0 ) {
						s = temp % 60;
					}
				} else {
					s = temp;
				}
			}
		} else {
			m = second / 60;
			if ( second % 60 != 0 ) {
				s = second % 60;
			}
		}
		String result = "";
		result += h > 0 && h < 10 ? "0" + h + ":" : h == 0 ? "" : h + ":";
		result += m >= 0 && m < 10 ? "0" + m + ":" : m + ":";
		result += s >= 0 && s < 10 ? "0" + s : s;
		return result;
	}
}
