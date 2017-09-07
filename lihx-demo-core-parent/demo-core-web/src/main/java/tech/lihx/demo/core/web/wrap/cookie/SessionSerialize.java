package tech.lihx.demo.core.web.wrap.cookie;

import java.util.Map;

public interface SessionSerialize {

	public Map<String, Object> decode( String str );


	public String encode( Map<String, Object> map );
}
