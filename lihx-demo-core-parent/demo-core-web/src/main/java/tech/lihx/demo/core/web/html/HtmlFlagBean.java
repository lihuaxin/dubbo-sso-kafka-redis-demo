package tech.lihx.demo.core.web.html;

public class HtmlFlagBean {

	// 是否被缓存
	private boolean cache = true;

	// 缓存时间s
	private int expire = 60;

	// 是否gzip压缩
	private boolean compress = true;

	private String contentType = "text/html; charset=UTF-8";


	public HtmlFlagBean( int expire ) {
		this.expire = expire;
	}


	public boolean isCache() {
		return cache;
	}


	public void setCache( boolean cache ) {
		this.cache = cache;
	}


	public int getExpire() {
		return expire;
	}


	public void setExpire( int expire ) {
		this.expire = expire;
	}


	public boolean isCompress() {
		return compress;
	}


	public void setCompress( boolean compress ) {
		this.compress = compress;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType( String contentType ) {
		this.contentType = contentType;
	}

}
