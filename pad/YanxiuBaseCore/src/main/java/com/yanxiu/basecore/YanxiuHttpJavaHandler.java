package com.yanxiu.basecore;

import com.yanxiu.basecore.bean.YanxiuRetryPolicy;
import com.yanxiu.basecore.impl.YanxiuHttpBaseParameter;
import com.yanxiu.basecore.impl.YanxiuHttpParameterCallback;
import com.yanxiu.basecore.utils.BaseCoreLogInfo;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * 网络请求处理类，java实现
 * */

public final class YanxiuHttpJavaHandler implements YanxiuHttpBaseHandler {

    private  String requestUrl;
    private  String statueCode;
    private  String requestTime;
	/**
	 * 读取超时时间
	 * */
	public static final int READ_TIMEOUT = 10 * 1000 ;

	/**
	 * 连接超时处理
	 * */
	private YanxiuRetryPolicy retryPolicy;
	/**t
	 * GET 请求
	 * */
	public final String doGet(YanxiuHttpBaseParameter<?,?,?> params) throws IOException {
		if(params == null){
			return null;
		}
		retryPolicy = new YanxiuRetryPolicy();
		retryPolicy.setMaxNumRetries(params.getMaxNumRetries());
		String baseUrl ;
		URL url;
		String response = null ;
		HttpURLConnection httpConnection = null;
		InputStream is = null ;
		IOException onError = new IOException("connect Network Times out");
        while(retryPolicy.hasAttemptRemaining()) {
			try {
				baseUrl = params.getBaseUrl() + params.encodeUrl();
				requestUrl = baseUrl;
				BaseCoreLogInfo.err("GET  " + baseUrl);
				url = new URL(baseUrl);
				httpConnection = (HttpURLConnection) url.openConnection();

				YanxiuHttpParameterCallback callback = params.getCallback();
				if (callback != null) {
					callback.proRequest(httpConnection);
				}
				httpConnection.setRequestMethod("GET");
				httpConnection.setReadTimeout(READ_TIMEOUT);
				httpConnection.setConnectTimeout(retryPolicy.getCurrentTimeout());
				try {
					httpConnection.connect();
				} catch (SocketTimeoutException timeOutException){
					BaseCoreLogInfo.err(timeOutException.getMessage());
					retryPolicy.retry(onError);
					{
						httpConnection.disconnect();
						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						httpConnection = null;
						is = null;
					}
					continue;
				}
				int responseCode = httpConnection.getResponseCode();
				statueCode = responseCode + "";
				if (responseCode == HttpStatus.SC_OK) {
					is = httpConnection.getInputStream();
				} else {
					if (BaseCoreLogInfo.isDebug) {
						is = httpConnection.getErrorStream();
						response = read(is);
						BaseCoreLogInfo.err(response);
					}
					retryPolicy.retry(onError);
					{
						httpConnection.disconnect();
						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						httpConnection = null;
						is = null;
					}
					continue;
				}
				response = read(is);
				BaseCoreLogInfo.err(response);

				if (callback != null) {
					callback.laterRequest(httpConnection);
				}
				return response;
			} finally {
				if(httpConnection != null ){
					httpConnection.disconnect();
				}
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				httpConnection = null;
				is = null;
			}
		}
		return null;
	}

	
	/**
	 * POST 请求
	 * */
	public final String doPost(YanxiuHttpBaseParameter<?,?,?> params) throws IOException {
		if(params == null){
			return null;
		}
		URL url;
		String response = null ;
		HttpURLConnection httpConnection = null;
		InputStream is = null ;
		retryPolicy = new YanxiuRetryPolicy();
		retryPolicy.setMaxNumRetries(params.getMaxNumRetries());
		YanxiuHttpParameterCallback callback = params.getCallback() ;
		IOException onError = new IOException("connect Network Times out");
		while(retryPolicy.hasAttemptRemaining()) {
			try {
				url = new URL(params.getBaseUrl());
				requestUrl = params.getBaseUrl();
				httpConnection = (HttpURLConnection) url.openConnection();

				if (callback != null) {
					callback.proRequest(httpConnection);
				}

				httpConnection.setRequestMethod("POST");
				httpConnection.setReadTimeout(READ_TIMEOUT);
				httpConnection.setConnectTimeout(retryPolicy.getCurrentTimeout());
				httpConnection.setDoOutput(true);
				try {
					httpConnection.connect();
				} catch (SocketTimeoutException timeOutException){
					BaseCoreLogInfo.err("POST  " + "SocketTimeoutException");
					BaseCoreLogInfo.err(timeOutException.getMessage());
					retryPolicy.retry(onError);
					{
						httpConnection.disconnect();
						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						httpConnection = null;
						is = null;
					}
					continue;
				}
				String p = params.encodeUrl().toString();
				BaseCoreLogInfo.err("POST  " + params.getBaseUrl() + "\n--------|" + p);
				httpConnection.getOutputStream().write(p.toString().getBytes());

				int responseCode = httpConnection.getResponseCode();
				statueCode = responseCode + "";
				if (responseCode == HttpStatus.SC_OK) {
					is = httpConnection.getInputStream();
				} else {
					if (BaseCoreLogInfo.isDebug) {
						is = httpConnection.getErrorStream();
						response = read(is);
						BaseCoreLogInfo.err(response);
					}
					BaseCoreLogInfo.err("POST  " + "responseCode is error");
					retryPolicy.retry(onError);
					{
						httpConnection.disconnect();
						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						httpConnection = null;
						is = null;
					}
					continue;
//					throw new IOException("responseCode is not 200");
				}

				response = read(is);
				BaseCoreLogInfo.err(response);

				if (callback != null) {
					callback.laterRequest(httpConnection);
				}
				return response;
			} finally {
				if(httpConnection != null ){
					httpConnection.disconnect();
				}
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				httpConnection = null;
				is = null;
			}
		}
		return null;
	}
	
	/**
	 * 将回来的IO流转化为字符�?
	 * */
	private final String read(InputStream in) throws IOException {
		if(in == null){
			return null ;
		}
		InputStreamReader r = null ;
		String result = null ;
		char[] buf = new char[100] ;
		try{
			StringBuilder sb = new StringBuilder();
			r = new InputStreamReader(in);
			int len = 0 ;
			while ((len = r.read(buf)) != -1) {
				sb.append(buf , 0 , len);
			}
			
			result = sb.toString();
			
			return result ;
		} finally{
			if(r != null){r.close();}
			r = null ;
			result = null ;
			buf = null ;
			in = null ;
		}
	}
}
