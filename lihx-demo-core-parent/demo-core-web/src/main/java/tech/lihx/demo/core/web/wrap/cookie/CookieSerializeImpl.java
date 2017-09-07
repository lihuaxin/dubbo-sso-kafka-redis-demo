package tech.lihx.demo.core.web.wrap.cookie;

import java.util.HashMap;
import java.util.Map;

public class CookieSerializeImpl implements SessionSerialize {

	private ObjectSerialize encodeAndDecode = null;

	private static final CookieSerializeImpl me = new CookieSerializeImpl();


	public static CookieSerializeImpl me() {
		return me;
	}


	private CookieSerializeImpl() {
		encodeAndDecode = new Base64Operation(new AESOperation(new LZ4Operation(new FstOperation())));
	}


	@Override
	@SuppressWarnings( "unchecked" )
	public Map<String, Object> decode( String str ) {
		return (Map<String, Object>) encodeAndDecode.deSerialize(str);
	}


	@Override
	public String encode( Map<String, Object> map ) {
		return (String) encodeAndDecode.serialize(map);
	}


	// public static void main(String[] args) {
	// ObjectSerialize encodeAndDecode = (new Base64Operation(new
	// GZipOperation(new AESOperation(new KryoOperation()))));
	// Rad62 rr = new Rad62();
	// String se = (String) encodeAndDecode.serialize(rr);
	// System.out.println(se);
	// System.out.println(encodeAndDecode.deSerialize(se));
	// try {
	// System.out.println(URLDecoder.decode("%3D", "UTF-8"));
	// System.out.println(URLDecoder.decode("%3B", "UTF-8"));
	// } catch (UnsupportedEncodingException e) {
	// logger.error(e.getMessage(),e);
	// }
	// }
	public static void main( String[] args ) {
		// ObjectSerialize encodeAndDecode = (new Base64Operation(new
		// TeaOperation(new LZ4Operation(new JSONOperation()))));
		// String fff = "Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf"
		// +
		// "文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文"
		// +
		// "件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系"
		// +
		// "统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录"
		// +
		// "中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只"
		// +
		// "需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hos"
		// +
		// "ts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/"
		// +
		// "hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubu"
		// +
		// "ntu系统的Hosts只需修改/etc/hosts文件，在目录中还有一个hosts.conf文件Ubuntu系统的Hosts只需修改/etc/hosts文件，在目"
		// + "录中还有一个hosts.conf文件;";
		// HashMap<String, Object> sf = new HashMap<String, Object>();
		// sf.put("key", fff);
		// //LZ4Operation
		// long start=System.currentTimeMillis();
		// String ff=(String)encodeAndDecode.serialize(sf);
		// long end=System.currentTimeMillis();
		// System.out.println(end-start);
		// HashMap<String, Object> ddd =(HashMap<String,
		// Object>)encodeAndDecode.deSerialize(ff);
		//
		// System.out.println(ff.length());
		// System.out.println(ddd.get("key"));
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("teststring", "test");
		// 76
		System.out.println(CookieSerializeImpl.me().encode(map).length());
	}

}
