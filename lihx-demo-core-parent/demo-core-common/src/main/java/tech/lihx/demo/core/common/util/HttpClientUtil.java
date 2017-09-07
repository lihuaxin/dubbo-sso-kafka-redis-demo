package tech.lihx.demo.core.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * apache HttpClient 工具类
 * <p>
 * 
 * @author LHX
 * @date 2015年5月7日
 * @version 1.0.0
 */
public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final String USER_AGENT = "Mozilla/5.0";

	private static final HttpClient client = HttpClientBuilder.create().build();


	/**
	 * Http Get 请求
	 * 
	 * @param url
	 *            请求地址
	 * @return
	 * @throws Exception
	 */
	public static String executeGet( String url ) throws Exception {
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);
		logger.debug("Response Code : {}", response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ( (line = rd.readLine()) != null ) {
			result.append(line);
		}
		return result.toString();
	}


	/**
	 * Http Post 请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public static String executePost( String url, Map<String, String> params ) throws Exception {
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		if ( params != null ) {
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for ( Map.Entry<String, String> param : params.entrySet() ) {
				urlParameters.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				logger.debug("key:{} , value:{}", param.getKey(), param.getValue());
			}
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		}
		HttpResponse response = client.execute(post);
		logger.debug("Response Code : {}", response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ( (line = rd.readLine()) != null ) {
			result.append(line);
		}
		return result.toString();
	}
}
