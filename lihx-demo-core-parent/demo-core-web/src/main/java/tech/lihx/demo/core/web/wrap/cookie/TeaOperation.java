package tech.lihx.demo.core.web.wrap.cookie;

import tech.lihx.demo.core.common.util.Tea;

public class TeaOperation implements ObjectSerialize {

	private ObjectSerialize ser = null;


	public TeaOperation( ObjectSerialize ser ) {
		this.ser = ser;
	}


	@Override
	public Object deSerialize( Object obj ) {
		byte[] byteArray = Tea.decryptByTea((byte[]) obj);
		return ser.deSerialize(byteArray);
	}


	@Override
	public Object serialize( Object obj ) {
		// 加密
		Object objtemp = ser.serialize(obj);
		return Tea.encryptByTea((byte[]) objtemp);
	}
}
