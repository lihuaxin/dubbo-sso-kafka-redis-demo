package tech.lihx.demo.core.common.util;


/**
 * 对象需实现序列化接口才能被序列化和压缩
 * <p>
 * 
 * @author lihx
 * @Date 2014-7-9
 */
public class ObjectCompressUtil {

	public static byte[] compress( Object obj ) {
		byte[] byteArray = SerializeUtil.fastSerialize(obj);
		return Lz4Compress.compress(byteArray);
	}


	public static Object uncompress( byte[] byteArray ) {
		byte[] data = Lz4Compress.uncompress(byteArray);
		return SerializeUtil.fastDeserialize(data);
	}
}
