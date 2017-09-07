package tech.lihx.demo.core.web.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Parse;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import tech.lihx.demo.core.common.util.ApplicationUtil;
import tech.lihx.demo.core.common.util.Lz4Compress;
import tech.lihx.demo.core.sso.SSOToken;
import tech.lihx.demo.core.sso.client.SSOHelper;
import tech.lihx.demo.core.web.filter.InvocationUtils;


/**
 * 缓存html代码片段自定义标签
 * <p>
 * #cache('vvv/vv.vm','key',123) 参数1模板路径 参数2提供的key.默认用户id 参数3缓存时间,默认600s
 * 
 * @author LHX
 * @date 2016年1月24日
 * @version 1.0.0
 */
public class CacheDirective extends Parse {

	private static final String CACHE_NAME = "include_html_cache";

	private Cache cache;


	private Cache getCache() {
		if ( cache == null ) {
			synchronized ( this ) {
				if ( cache != null ) { return cache; }
				EhCacheManager manager = (EhCacheManager) ApplicationUtil.getBean(CacheManager.class);
				net.sf.ehcache.CacheManager ehManager = manager.getCacheManager();
				cache = ehManager.getCache(CACHE_NAME);
				if ( cache == null ) {
					ehManager.addCache(CACHE_NAME);
				}
			}
		}
		return cache;
	}


	public void put( String key, int cacheTime, String html ) {
		byte[] compress = Lz4Compress.compress(html.getBytes());
		Element element = new Element(key, compress, false, 0, cacheTime);
		getCache().put(element);
	}


	public String get( String key ) {
		Element element = getCache().get(key);
		if ( element != null ) {
			byte[] compress = (byte[]) element.getObjectValue();
			if ( compress != null ) { return new String(Lz4Compress.uncompress(compress)); }
		}
		return null;
	}


	public String getKey( Object path, String supplyKey ) {
		return new StringBuilder("html_user_cache:").append(supplyKey).append(":").append(path).toString();
	}


	@Override
	public String getName() {

		return "cache";

	}


	public String getSupplyKey( InternalContextAdapter context, Node node ) {
		String key = null;
		int number = node.jjtGetNumChildren();
		if ( number > 1 ) {
			SimpleNode time_value = (SimpleNode) node.jjtGetChild(1);
			if ( time_value != null ) {
				Object value = time_value.value(context);
				if ( value != null ) {
					key = value.toString();
				}
			}
		}
		if ( key == null ) {
			SSOToken token = (SSOToken) SSOHelper.getToken(InvocationUtils.getCurrentThreadRequest());
			key = String.valueOf(token.getUserId());
		}
		return key;
	}


	public int getCachedTime( InternalContextAdapter context, Node node ) {
		int cacheTime = 600;
		int number = node.jjtGetNumChildren();
		if ( number > 2 ) {
			SimpleNode time_value = (SimpleNode) node.jjtGetChild(2);
			if ( time_value != null ) {
				Object value = time_value.value(context);
				if ( value != null ) {
					cacheTime = Integer.parseInt(value.toString());
				}
			}
		}
		return cacheTime;
	}


	@Override
	public boolean render( InternalContextAdapter context, Writer writer, Node node ) throws IOException,
		ResourceNotFoundException, ParseErrorException, MethodInvocationException {

		boolean flag = true;
		Object path = node.jjtGetChild(0).value(context);
		String key = getKey(path, getSupplyKey(context, node));
		String html = get(key);
		if ( html == null ) {
			int cacheTime = getCachedTime(context, node);
			StringWriter cacheHtml = new StringWriter(1000);
			flag = super.render(context, cacheHtml, node);
			if ( flag ) {
				html = cacheHtml.toString();
				// 放入缓存
				put(key, cacheTime, html);
				writer.write(html);
			}
		} else {
			// 写入客户端
			writer.write(html);
		}

		return flag;
	}

}
