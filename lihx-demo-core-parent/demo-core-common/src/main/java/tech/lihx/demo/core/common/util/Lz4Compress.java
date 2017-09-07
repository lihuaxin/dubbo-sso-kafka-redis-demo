package tech.lihx.demo.core.common.util;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

/**
 * lz4压缩算法工具类
 * <p>
 * 
 * @author lihx
 * @Date 2014-7-9
 */
public class Lz4Compress {

	private static LZ4Factory factory = LZ4Factory.fastestInstance();


	public static byte[] compress( byte[] parameter ) {
		// 保存原始大小
		byte[] length = intToByteArray(parameter.length);
		LZ4Compressor compressor = factory.fastCompressor();
		byte[] preCompress = compressor.compress(parameter);
		byte[] compressed = new byte[preCompress.length + 4];
		System.arraycopy(length, 0, compressed, 0, length.length);
		System.arraycopy(preCompress, 0, compressed, 4, preCompress.length);
		return compressed;
	}


	private static int byteArrayToInt( byte[] b ) {
		int value = 0;
		for ( int i = 0 ; i < 4 ; i++ ) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}


	private static byte[] intToByteArray( int i ) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}


	public static byte[] uncompress( byte[] result ) {
		if ( result == null ) { return null; }
		byte[] length = new byte[4];
		System.arraycopy(result, 0, length, 0, length.length);
		int size = byteArrayToInt(length);
		byte[] restored = new byte[size];
		LZ4FastDecompressor decompressor = factory.fastDecompressor();
		decompressor.decompress(result, 4, restored, 0, size);
		return restored;
	}
}
