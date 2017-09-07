package tech.lihx.demo.core.common.util;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LHX
 *
 *         记录时间的sessionid
 */
public class RandomGUID extends Object {

	private static final Logger logger = LoggerFactory.getLogger(RandomGUID.class);

	public static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	private long timeField = 0;

	private long randomField = 0;

	private long threadField = 0;

	private final long inetAddress;

	private static Random myRand;

	private static SecureRandom mySecureRand;

	private static long inetField = 0;
	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			// inetField = InetAddress.getLocalHost().getAddress().hashCode();
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while ( e.hasMoreElements() ) {
				NetworkInterface ni = e.nextElement();
				sb.append(ni.toString());
			}
			inetField = sb.toString().hashCode();
		} catch ( Exception e ) {
			inetField = myRand.nextLong();
		}
	}


	public RandomGUID() {
		inetAddress = inetField;
		// threadField = Thread.currentThread().hashCode();
		threadField = System.identityHashCode(Thread.currentThread());
		timeField = System.currentTimeMillis();
		randomField = mySecureRand.nextLong();
	}


	public RandomGUID( long inetAddress, long threadField, long timeField, long randomField ) {
		this.inetAddress = inetAddress;
		this.threadField = threadField;
		this.randomField = randomField;
		this.timeField = timeField;
	}


	public static String toHex( byte[] md ) {
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for ( int i = 0 ; i < j ; i++ ) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}


	public static byte[] longToBytes( long n ) {
		byte[] b = new byte[8];
		b[7] = (byte) (n & 0xff);
		b[6] = (byte) (n >> 8 & 0xff);
		b[5] = (byte) (n >> 16 & 0xff);
		b[4] = (byte) (n >> 24 & 0xff);
		b[3] = (byte) (n >> 32 & 0xff);
		b[2] = (byte) (n >> 40 & 0xff);
		b[1] = (byte) (n >> 48 & 0xff);
		b[0] = (byte) (n >> 56 & 0xff);
		return b;
	}


	public static long bytesToLong( byte[] array ) {
		return ((long) array[0] & 0xff) << 56
				| ((long) array[1] & 0xff) << 48 | ((long) array[2] & 0xff) << 40 | ((long) array[3] & 0xff) << 32
				| ((long) array[4] & 0xff) << 24 | ((long) array[5] & 0xff) << 16 | ((long) array[6] & 0xff) << 8
				| ((long) array[7] & 0xff) << 0;
	}


	public static byte[] intToBytes( int n ) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}


	public static int bytesToInt( byte b[] ) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}


	public static RandomGUID fromString( String name ) {
		String[] strArray = name.split("-");
		if ( strArray.length != 4 ) { throw new IllegalArgumentException("参数不对"); }
		return new RandomGUID(
				CompressEncodeing.unCompressNumber(strArray[0]), CompressEncodeing.unCompressNumber(strArray[1]),
				CompressEncodeing.unCompressNumber(strArray[2]), CompressEncodeing.unCompressNumber(strArray[3]));
	}


	public static byte[] toBytes( String hex ) {
		int length = hex.length();
		byte[] b = new byte[length / 2];
		int k = 0;
		for ( int i = 0 ; i < length ; i++ ) {
			char c1 = hex.charAt(i);
			i++;
			char c2 = hex.charAt(i);
			int a = Integer.parseInt(String.valueOf(c1) + String.valueOf(c2), 16);
			b[k++] = (byte) a;
		}
		return b;
	}


	public long getInetAddress() {
		return inetAddress;
	}


	public long getRandomField() {
		return randomField;
	}


	public long getTimeField() {
		return timeField;
	}


	/*
	 * 时间-网络地址-随机数
	 */
	@Override
	public String toString() {
		return new StringBuilder().append(CompressEncodeing.compressNumber(inetAddress)).append("-")
				.append(CompressEncodeing.compressNumber(threadField)).append("-")
				.append(CompressEncodeing.compressNumber(timeField)).append("-")
				.append(CompressEncodeing.compressNumber(randomField)).toString();
	}


	public static void main( String args[] ) {
		long start = System.currentTimeMillis();
		for ( int i = 0 ; i < 1 ; i++ ) {
			RandomGUID myGUID = new RandomGUID();
			String fff = myGUID.toString();
			System.out.println(fff);
			System.out.println("from:" + RandomGUID.fromString(fff));
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);

		try {
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while ( e.hasMoreElements() ) {
				NetworkInterface ni = e.nextElement();
				sb.append(ni.toString());
			}
			// int machinePiece = sb.toString().hashCode() << 16;

			System.out.println(sb.toString().hashCode());
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
		}
		// System.out.println(System.nanoTime());
		// System.out.println(Long.toHexString(System.currentTimeMillis()));
	}

}
