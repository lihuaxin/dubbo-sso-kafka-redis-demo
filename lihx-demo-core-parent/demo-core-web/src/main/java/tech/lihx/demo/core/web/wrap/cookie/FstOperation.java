package tech.lihx.demo.core.web.wrap.cookie;

import tech.lihx.demo.core.common.util.SerializeUtil;

/**
 * <p>
 * 
 * @author LHX
 * @Date 2016-7-7
 */
public class FstOperation implements ObjectSerialize {

	@Override
	public Object deSerialize( Object obj ) {
		return SerializeUtil.fastDeserialize((byte[]) obj);
	}


	@Override
	public Object serialize( Object obj ) {
		return SerializeUtil.fastSerialize(obj);
	}

}
