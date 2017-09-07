/**
 * VersionTest.java cn.vko.core.web.app Copyright (c) 2015, 北京微课创景教育科技有限公司版权所有.
 */

package tech.lihx.demo.core.web.appversion;


/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * 
 * @author LHX
 * @date 2015年4月28日
 * @version 1.0.0
 */
public class VersionTest {

	private static VersionComprator cmp;


	public static void main( String[] args ) {
		cmp = new VersionComprator();
		Test(new String[ ] { "1.1.2", "1.2", "1.2.0", "1.2.1", "1.12" });
		Test(new String[ ] { "1.3", "1.3a", "1.3b", "1.3-SNAPSHOT" });

		// 版本对比
		System.out.println(cmp.compare("1.3", "1.4"));
	}


	private static void Test( String[] versions ) {
		for ( int i = 0 ; i < versions.length ; i++ ) {
			for ( int j = i ; j < versions.length ; j++ ) {
				Test(versions[i], versions[j]);
			}
		}
	}


	private static void Test( String v1, String v2 ) {
		int result = cmp.compare(v1, v2);
		String op = "==";
		if ( result < 0 ) {
			op = "<";
		}
		if ( result > 0 ) {
			op = ">";
		}
		System.out.printf("%s %s %s\n", new Object[ ] { v1, op, v2 });
	}
}
