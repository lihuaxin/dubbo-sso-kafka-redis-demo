package tech.lihx.demo.core.web.wrap.cookie;

import tech.lihx.demo.core.common.util.Base64URLSafe;

public class Base64Operation implements ObjectSerialize {

	private ObjectSerialize ser = null;


	public Base64Operation( ObjectSerialize ser ) {
		this.ser = ser;
	}


	private static byte[] decryptBASE64( String key ) {
		return Base64URLSafe.decodeFast(key);
	}


	private static String encryptBASE64( byte[] key ) {
		return Base64URLSafe.encodeToString(key, false);
	}


	@Override
	public Object deSerialize( Object obj ) {
		byte[] byteArray = decryptBASE64((String) obj);
		return ser.deSerialize(byteArray);
	}


	@Override
	public Object serialize( Object obj ) {
		return encryptBASE64((byte[]) ser.serialize(obj));
	}
}
