package com.duoduo.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

@SuppressWarnings({"deprecation","resource"})
public class HttpUtil {
	
	public static String get(String url) throws IOException {
		return get(url, null, null);
	}

	public static String get(String url, Map<String, String> params) {
		return get(url, null, params);
	}
	
	public static String get(String url, Map<String, String> headers, Map<String, String> params) {
		HttpClient httpclient = new SSLClient();
		try {
			//parameter
			if (params != null) {
				List<NameValuePair> qparams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : params.entrySet()) {
					qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
				//splice url
				if(qparams != null && !qparams.isEmpty()) {
					url += "?" + URLEncodedUtils.format(qparams, HTTP.UTF_8);
				}
			}
			//request
			HttpGet httpget = new HttpGet(url);
			//header
			if(headers != null && !headers.isEmpty()) {
				for(String headerKey :headers.keySet()) {
					httpget.addHeader(headerKey, headers.get(headerKey));
				}
			}
			return httpclient.execute(httpget, new BasicResponseHandler()); 
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	public static String post(String url, Map<String, String> params) {
		return post(url, null, params);
	}
	
	public static String post(String url, Map<String, String> headers, Map<String, String> params) {
		String charset = "utf-8";
		HttpClient httpclient = new SSLClient();
		HttpPost httppost = new HttpPost(url);
		try {
			//headers
			if(headers != null && !headers.isEmpty()) {
				for(String headerKey : headers.keySet()) {
					httppost.addHeader(headerKey, headers.get(headerKey));
				}
			}
			//parameter
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : params.entrySet()) {
					formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
				httppost.setEntity(new UrlEncodedFormEntity(formparams, charset));
			}
			//request
			HttpResponse resp = httpclient.execute(httppost);
			return EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

}
