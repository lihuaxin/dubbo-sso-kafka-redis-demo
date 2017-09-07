package tech.lihx.demo.core.common.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.lihx.demo.core.common.util.DateUtil;


/**
 * 应用辅助类（Web公共的方法）
 * <p>
 * 
 * @author lihx
 * @date 2014-11-26
 * @version 1.0.0
 */
public class AppHelper {

	private static List<String[]> yal = null;

	static {
		/**
		 * 类加载初始化
		 */
		getTermList();
	}


	/**
	 * 9月1日 至次年 2月25日【上学期】 其他时间【下学期】
	 */
	public static String getTerm() {
		return getTerm(new Date());
	}


	public static String getTerm( Date date ) {
		StringBuffer term = new StringBuffer();
		term.append(getStudyYear(date));
		int month = DateUtil.getMonthOfDate(date);
		if ( month >= 9 || (month <= 2 && DateUtil.getDayOfDate(date) <= 25) ) {
			term.append("01");
		} else {
			term.append("02");
		}
		return term.toString();
	}


	/**
	 * 自然学年 9月1日 至次年 2月25日【上学期】，其他时间【下学期】
	 */
	public static String getStudyYear() {
		return getStudyYear(new Date());
	}


	public static String getStudyYear( Date date ) {
		StringBuffer term = new StringBuffer();
		int year = DateUtil.getYearOfDate(date);
		int month = DateUtil.getMonthOfDate(date);
		// 小于9月份，去年入学【年份 -1】
		if ( month <= 8 ) {
			year = year - 1;
		}
		term.append(year);
		return term.toString();
	}


	public static void main( String[] args ) {
		System.out.println(getStudyYear());
	}


	/**
	 * 动态获取学期数据 2015年开始计算
	 * <p>
	 *
	 * @return
	 */
	public static List<String[]> getTermList() {
		if ( yal == null ) {
			yal = new ArrayList<String[]>();
			Date date = new Date();
			int year = DateUtil.getYearOfDate(date);
			int month = DateUtil.getMonthOfDate(date);
			// 小于9月份，去年入学【年份 -1】
			if ( month <= 8 ) {
				int temp = year - 1;
				yal.add(new String[ ] { temp + "02", temp + "下学期" });
			} else {
				yal.add(new String[ ] { year + "01", year + "上学期" });
			}

			// 之前所有学期计算
			int size = year - 1;
			for ( int i = size ; i >= 2015 ; i-- ) {
				int temp = i - 1;
				yal.add(new String[ ] { temp + "02", temp + "下学期" });
				yal.add(new String[ ] { i + "01", i + "上学期" });
			}
		}
		return yal;
	}


	/**
	 * 根据key值获取完整学期信息
	 * <p>
	 *
	 * @return
	 */
	public static String[] getTermByKey( String key ) {
		for ( String[] strings : yal ) {
			if ( strings[0].equals(key) ) { return strings; }
		}
		return yal.get(0);
	}


	/**
	 * 根据学期返回该学期开始时间 9月1日 至次年 2月25日【上学期】
	 *
	 * @return term 201401
	 */
	public static Date getStartDate( String term ) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date starDate = null;
		String termDate = null;
		if ( term.substring(4) == "01" ) {
			termDate = term.substring(0, 4) + "-09-01";
		} else if ( term.substring(4) == "02" ) {
			termDate = Integer.parseInt(term.substring(0, 4)) + 1 + "-02-25";
		}
		try {
			starDate = df.parse(termDate);
		} catch ( ParseException e ) {
			return null;
		}
		return starDate;
	}


	/**
	 * 根据学期返回该学期结束时间 9月1日 至次年 2月25日【上学期】
	 *
	 * @return term 201401
	 */
	public static Date getEndDate( String term ) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date endDate = null;
		String termDate = null;
		if ( term.substring(4) == "01" ) {
			termDate = Integer.parseInt(term.substring(0, 4)) + 1 + "-02-25";

		} else if ( term.substring(4) == "02" ) {
			termDate = Integer.parseInt(term.substring(0, 4)) + 1 + "-09-01";
		}
		try {
			endDate = df.parse(termDate);
		} catch ( ParseException e ) {
			return null;
		}
		return endDate;
	}
}
