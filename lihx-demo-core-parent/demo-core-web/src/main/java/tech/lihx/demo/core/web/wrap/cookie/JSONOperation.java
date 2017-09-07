package tech.lihx.demo.core.web.wrap.cookie;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONOperation implements ObjectSerialize {

	@Override
	public Object deSerialize( Object obj ) {
		return JSON.parse((byte[]) obj);
	}


	@Override
	public Object serialize( Object obj ) {
		return JSON.toJSONBytes(obj, SerializerFeature.WriteClassName);
	}

}
