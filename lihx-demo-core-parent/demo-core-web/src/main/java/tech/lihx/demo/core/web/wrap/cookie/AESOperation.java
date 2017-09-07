package tech.lihx.demo.core.web.wrap.cookie;

import tech.lihx.demo.core.common.util.AESUtils;

public class AESOperation implements ObjectSerialize {

	private ObjectSerialize ser = null;


	public AESOperation( ObjectSerialize ser ) {
		this.ser = ser;
	}


	@Override
	public Object deSerialize( Object obj ) {
		byte[] byteArray = AESUtils.decryptData((byte[]) obj);
		return ser.deSerialize(byteArray);
	}


	@Override
	public Object serialize( Object obj ) {
		// 加密
		return AESUtils.encryptData((byte[]) ser.serialize(obj));
	}
}
