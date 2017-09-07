package tech.lihx.demo.core.common.util;

/**
 * 64进制和10进制的转换类
 * 
 * @author LHX Cookie的内容中不能包含空格，方括号，圆括号，等于号（=），逗号，双引号，斜杠，问号，@符号，冒号，分号。
 */
public final class CompressEncodeing {

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '#', '_', };


	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		// logger.debug(compressNumber(1000, 6));
		// logger.debug(unCompressNumber(compressNumber(Long.MAX_VALUE,
		// 6)));
		// logger.debug(Math.random());
		// for (int i = 0; i < 10000; i++) {
		// String sss=compressNumber(System.currentTimeMillis());
		// logger.debug(compressNumber(System.currentTimeMillis()));
		// }
		// while (true) {
		// long time = System.currentTimeMillis();
		// String sss = compressNumber(time);
		// if (sss.contains("\"")) {
		// logger.debug(time);
		// logger.debug(sss);
		// break;
		// }
		// try {
		// Thread.sleep(1);
		// } catch (InterruptedException e) {
		// logger.error(e.getMessage(),e);
		// }
		// }
		// logger.debug(1 * 1000);
		// logger.debug(1 << 202);
		// Cookie的内容中不能包含空格，方括号，圆括号，等于号（=），逗号，双引号，斜杠，问号，@符号，冒号，分号。
		// logger.debug((int)'_');

	}


	/**
	 * 把10进制的数字转换成64进制
	 * 
	 * @param number
	 * @param shift
	 * @return
	 */
	public static String compressNumber( long number, int shift ) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int) (number & mask)];
			number >>>= shift;
		} while ( number != 0 );
		return new String(buf, charPos, (64 - charPos));
	}


	public static String compressNumber( long number ) {
		return compressNumber(number, 6);
	}


	/**
	 * 把64进制的字符串转换成10进制
	 * 
	 * @param decompStr
	 * @return
	 */
	public static long unCompressNumber( String decompStr ) {
		long result = 0;
		int length = decompStr.length();
		for ( int i = length - 1 ; i >= 0 ; i-- ) {
			char ch = decompStr.charAt(i);
			if ( i == length - 1 ) {
				result += getCharIndexNum(ch);
				continue;
			}
			for ( int j = 0 ; j < digits.length ; j++ ) {
				if ( ch == digits[j] ) {
					result += ((long) j) << 6 * (length - 1 - i);
					break;
				}
			}
		}
		return result;
	}


	/**
	 * 
	 * @param ch
	 * @return
	 */
	private static long getCharIndexNum( char ch ) {
		int num = (ch);
		if ( num >= 48 && num <= 57 ) {
			return num - 48;
		} else if ( num >= 97 && num <= 122 ) {
			return num - 87;
		} else if ( num >= 65 && num <= 90 ) {
			return num - 29;
		} else if ( num == 35 ) {
			return 62;
		} else if ( num == 95 ) { return 63; }
		return 0;
	}

}
