package tech.lihx.demo.core.web.wrap.cookie;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class LZ4Operation implements ObjectSerialize {

	private ObjectSerialize ser = null;

	private static LZ4Factory factory = LZ4Factory.fastestInstance();


	public LZ4Operation( ObjectSerialize ser ) {
		this.ser = ser;
	}


	@Override
	public Object serialize( Object obj ) {
		Object objtemp = ser.serialize(obj);
		byte[] parameter = (byte[]) objtemp;
		// 保存原始大小
		byte[] length = intToByteArray(parameter.length);
		LZ4Compressor compressor = factory.fastCompressor();
		byte[] preCompress = compressor.compress(parameter);
		byte[] compressed = new byte[preCompress.length + 4];
		System.arraycopy(length, 0, compressed, 0, length.length);
		System.arraycopy(preCompress, 0, compressed, 4, preCompress.length);
		return compressed;
	}


	public static int byteArrayToInt( byte[] b ) {
		int value = 0;
		for ( int i = 0 ; i < 4 ; i++ ) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}


	public static byte[] intToByteArray( int i ) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}


	@Override
	public Object deSerialize( Object obj ) {
		byte[] result = (byte[]) obj;
		byte[] length = new byte[4];
		System.arraycopy(result, 0, length, 0, length.length);
		int size = byteArrayToInt(length);
		byte[] restored = new byte[size];
		LZ4FastDecompressor decompressor = factory.fastDecompressor();
		decompressor.decompress(result, 4, restored, 0, size);
		return ser.deSerialize(restored);
	}

}
