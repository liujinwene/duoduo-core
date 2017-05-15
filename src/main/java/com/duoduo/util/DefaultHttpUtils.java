package com.duoduo.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class DefaultHttpUtils {

	protected static Logger logger = LoggerFactory.getLogger(DefaultHttpUtils.class);

	/**
	 * post请求 ，超时默认30秒
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> params) {
		try {
			return post(url, params, 20, false);
		} catch (IOException e) {
			logger.debug("post request error: ", e);
		}
		return "{\"errorCode\":500,\"errorDescription\":\"request error\"}";
	}

	/**
	 * 带签名的post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String postWithAuthority(String url, Map<String, String> params) throws IOException {
		return post(url, params, 20, false);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String post(String url, Map<String, String> params, int timeout) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			retVal = new String(httpclient.execute(httppost, responseHandler).getBytes(HTTP.ISO_8859_1), HTTP.UTF_8);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @param isParseReturn
	 *            是否转码返回值
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String post(String url, Map<String, String> params, int timeout, boolean isParseReturn)
			throws IOException {
		StopWatch clock = new StopWatch();
		clock.start();

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, HTTP.UTF_8);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			
			printHttpInfo(httppost);
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (isParseReturn) {
				retVal = new String(httpclient.execute(httppost, responseHandler).getBytes(HTTP.ISO_8859_1),
						HTTP.UTF_8);
			} else {
				retVal = httpclient.execute(httppost, responseHandler).toString();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		logger.info(String.format("===request info===[waiting:%s ms;url:%s;params:%s;reponse:%s]", clock.getTime(), url,
				params, retVal));
		return retVal;
	}

	private static void printHttpInfo(HttpPost httppost) {
		Header[] headers = httppost.getAllHeaders();
		if(headers != null) {
			for(Header header : headers) {
				logger.info(header.getName() + "=" + header.getValue());
			}
		}
		
	}

	@SuppressWarnings("resource")
	public static String postJson(String url, String json, int timeout, Object... objects) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			String encoding = HTTP.UTF_8;
			if (objects != null && objects.length > 0) {
				encoding = objects[0].toString();
			}
			HttpPost httppost = new HttpPost(url);
			StringEntity params = new StringEntity(json, encoding);
			httppost.addHeader("content-type", "application/json");
			httppost.setEntity(params);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (objects == null || objects.length == 0) {
				retVal = new String(httpclient.execute(httppost, responseHandler).getBytes(HTTP.ISO_8859_1),
						HTTP.UTF_8);
			} else if (objects != null && objects[0].equals(HTTP.UTF_8)) {
				retVal = httpclient.execute(httppost, responseHandler);
			} else if (objects != null && objects[0].equals("gb2312")) {
				retVal = new String(httpclient.execute(httppost, responseHandler).getBytes("iso-8859-1"), "gb2312");
			} else {
				retVal = new String(httpclient.execute(httppost, responseHandler).getBytes(), HTTP.UTF_8);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug(retVal);
		return retVal;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String get(String url, Map<String, String> params) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", 100000);
		String retVal = "";
		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			String paramstr = URLEncodedUtils.format(qparams, HTTP.UTF_8);
			if (StringUtils.isNotEmpty(paramstr)) {
				url = url + "?" + paramstr;
			}
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			retVal = httpclient.execute(httpget, responseHandler);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}
	
	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String get(String url, Map<String, String> params, String charset) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", 100000);
		String retVal = "";
		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			String paramstr = URLEncodedUtils.format(qparams, charset);
			if (StringUtils.isNotEmpty(paramstr)) {
				url = url + "?" + paramstr;
			}
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			retVal = httpclient.execute(httpget, responseHandler);
			retVal = new String(retVal.getBytes(HTTP.ISO_8859_1), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * 得到访问url的返回状态(200正常)
	 * 
	 * @param url
	 *            :访问地址
	 * @param timeOut
	 *            :设置超时时间秒
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@SuppressWarnings("resource")
	public static int getPostRetStatu(String url, Integer timeOut) {
		HttpResponse response = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setIntParameter("http.socket.timeout", timeOut * 1000);
			httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
			/**
			 * 此处使用get请求,因为一些网站屏蔽了post请求eg:baidu
			 */
			// HttpPost httpost = new HttpPost(url);
			HttpGet httpget = new HttpGet(url);
			response = httpclient.execute(httpget);
		} catch (Exception e) {
			return 404;
		}
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * @Description:获取参数路径
	 * @param params
	 * @return
	 * @author Alvin.zengqi
	 * @date 2011-6-27 下午08:55:16
	 */
	public static String getParamsPath(Map<String, String> params) {
		String url = "";
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}
		String paramstr = URLEncodedUtils.format(qparams, HTTP.UTF_8);
		if (StringUtils.isNotEmpty(paramstr)) {
			url = url + "?" + paramstr;
		}
		return url;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @param charset
	 *            编码方式
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String get(String url, Map<String, String> params, int timeout, String charset) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			String paramstr = URLEncodedUtils.format(qparams, charset);
			if (StringUtils.isNotEmpty(paramstr)) {
				url = url + "?" + paramstr;
			}
			HttpGet httpget = new HttpGet(url);

			HttpResponse resp = httpclient.execute(httpget);
			retVal = EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String post(String url, Map<String, String> params, int timeout, String charset) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			
			HttpResponse resp = httpclient.execute(httppost);
			retVal = EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * put请求
	 * 
	 * @param url
	 * @param json
	 * @param timeout
	 * @param objects
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String putJson(String url, String json, int timeout, Object... objects) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			String encoding = HTTP.UTF_8;
			if (objects != null && objects.length > 0) {
				encoding = objects[0].toString();
			}
			HttpPut httpput = new HttpPut(url);
			StringEntity params = new StringEntity(json, encoding);
			httpput.addHeader("content-type", "application/json");
			httpput.setEntity(params);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (objects == null || objects.length == 0) {
				retVal = new String(httpclient.execute(httpput, responseHandler).getBytes(HTTP.ISO_8859_1), HTTP.UTF_8);
			} else if (objects != null && objects[0].equals(HTTP.UTF_8)) {
				retVal = httpclient.execute(httpput, responseHandler);
			} else if (objects != null && objects[0].equals("gb2312")) {
				retVal = new String(httpclient.execute(httpput, responseHandler).getBytes("iso-8859-1"), "gb2312");
			} else {
				retVal = new String(httpclient.execute(httpput, responseHandler).getBytes(), HTTP.UTF_8);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * delete请求
	 * 
	 * @param url
	 * @param timeout
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String delete(String url, int timeout, String charset) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			HttpDelete httpDelete = new HttpDelete(url);
			HttpResponse resp = httpclient.execute(httpDelete);
			retVal = EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * delete请求
	 * 
	 * @param url
	 * @param json
	 * @param timeout
	 * @param objects
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String deleteJson(String url, String json, int timeout, Object... objects) throws IOException {

		/**
		 * 没有现成的delete可以带json的，自己实现一个，参考HttpPost的实现
		 */
		class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
			public static final String METHOD_NAME = "DELETE";

			@SuppressWarnings("unused")
			public HttpDeleteWithBody() {
			}

			@SuppressWarnings("unused")
			public HttpDeleteWithBody(URI uri) {
				setURI(uri);
			}

			public HttpDeleteWithBody(String uri) {
				setURI(URI.create(uri));
			}

			public String getMethod() {
				return METHOD_NAME;
			}
		}

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			String encoding = HTTP.UTF_8;
			if (objects != null && objects.length > 0) {
				encoding = objects[0].toString();
			}
			HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(url);
			StringEntity params = new StringEntity(json, encoding);
			httpdelete.addHeader("content-type", "application/json");
			httpdelete.setEntity(params);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			if (objects == null || objects.length == 0) {
				retVal = new String(httpclient.execute(httpdelete, responseHandler).getBytes(HTTP.ISO_8859_1),
						HTTP.UTF_8);
			} else if (objects != null && objects[0].equals(HTTP.UTF_8)) {
				retVal = httpclient.execute(httpdelete, responseHandler);
			} else if (objects != null && objects[0].equals("gb2312")) {
				retVal = new String(httpclient.execute(httpdelete, responseHandler).getBytes("iso-8859-1"), "gb2312");
			} else {
				retVal = new String(httpclient.execute(httpdelete, responseHandler).getBytes(), HTTP.UTF_8);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		logger.debug(retVal);
		return retVal;
	}

}
