package tech.lihx.demo.core.common.util;

import java.util.Random;

public class RandomGenerator {

	private static String range = "0123456789abcdefghijklmnopqrstuvwxyz";

	private final Random random = new Random();


	public static synchronized String getRandomString() {
		Random random = new Random();

		StringBuffer result = new StringBuffer();

		for ( int i = 0 ; i < 8 ; i++ ) {
			result.append(range.charAt(random.nextInt(range.length())));
		}

		return result.toString();
	}


	/**
	 * 生成seqId，12位随机数
	 * 
	 * @return
	 */
	public String generateSeqId() {
		String result = "";
		for ( int i = 0 ; i < 2 ; i++ ) {
			result = String.valueOf(random.nextInt(1000000)) + result;
			for ( ; result.length() < (i + 1) * 6 ; result = "0" + result ) {
				;
			}
		}
		return result;
	}
}
