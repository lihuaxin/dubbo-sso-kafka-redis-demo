package tech.lihx.demo.core.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式工具类
 * <p>
 * 
 * @author LHX
 * @date 2015-12-4
 * @version 1.0.0
 */
public class MatcherUtil {

	/**
	 * 验证字符串是否为数字或字母
	 * <p>
	 * 
	 * @param str
	 *            匹配的字符串
	 * @return boolean
	 */
	public static boolean isLetterAndNum( String str ) {
		return match("[A-Za-z0-9]*", str);
	}


	/**
	 * 判断学籍号 长度：12，14，16或19位 ,字符：数字、字母
	 * <p>
	 * 
	 * @param str
	 *            匹配的字符串
	 * @return boolean
	 */
	public static boolean isStudentNumber( String str ) {
		if ( MatcherUtil.isLetterAndNum(str) ) {
			if ( str.length() == 12 || str.length() == 14 || str.length() == 16 || str.length() == 19 ) { return true; }
		}
		return false;
	}


	/**
	 * 验证字符串是否为数字
	 * <p>
	 * 
	 * @param str
	 *            匹配的字符串
	 * @return boolean
	 */
	public static boolean isNumber( String str ) {
		return match("[0-9]*", str);
	}


	/**
	 * 验证邮箱格式是否正确
	 * <p>
	 * 
	 * @param str
	 *            匹配的字符串
	 * @return boolean
	 */
	public static boolean isEmail( String str ) {
		return match(
			"^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$",
			str);
	}


	/**
	 * 验证字符串是否为手机号码
	 * <p>
	 * 
	 * @param str
	 *            匹配的字符串
	 * @return boolean
	 */
	public static boolean isMobile( String str ) {
		return match("^(1[0-9])\\d{9}$", str);
	}


	/**
	 * 正则表达式匹配
	 * 
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match( String regex, String str ) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}


	/**
	 * 验证字符串是否匹配yyyy/MM/dd
	 * <p>
	 * 
	 * @param date
	 * @return
	 */
	public static boolean matchDate( String date ) {
		String eL = "[0-9]{4}/[0-9]{2}/[0-9]{2}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(date);
		boolean dateFlag = m.matches();
		return dateFlag;
	}


	/**
	 * 验证字符串是否匹配yyyy-MM-dd
	 * <p>
	 * 
	 * @param date
	 * @return
	 */
	public static boolean matchyyyyMMddDate( String date ) {
		String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(date);
		boolean dateFlag = m.matches();
		return dateFlag;
	}


	public static boolean matchStudentCode( String studentCode ) {
		String eL = "(^[a-zA-Z0-9]{12}$)|(^[a-zA-Z0-9]{14}$)|(^[a-zA-Z0-9]{16}$)|(^[a-zA-Z0-9]{19}$)";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(studentCode);
		boolean dateFlag = m.matches();
		return dateFlag;
	}


	public static boolean matchName( String name ) {
		String eL = "[\u4E00-\u9FA5]{2,15}(?:·[\u4E00-\u9FA5]{2,15})*";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(name);
		boolean dateFlag = m.matches();
		return dateFlag;
	}


	public static boolean isNum( String str ) {

		String eL = "\\d+(\\.\\d+)?$";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(str);
		boolean dateFlag = m.matches();
		return dateFlag;
	}


	public static void main( String[] args ) {
		System.out.println(isNum("0"));
	}
}
