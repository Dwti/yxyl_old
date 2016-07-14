package com.yanxiu.basecore.impl;

/**
 * 请求参数组装�?
 * 封装�?
 * 请求地址
 * 参数
 * 解析�?
 * 刷新ID
 * */

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.parse.YanxiuBaseParser;

/**
 * 请求参数组装�?
 * 封装�?
 * 请求地址
 * 参数
 * 解析�?
 * 刷新ID
 * */
public abstract class YanxiuHttpBaseParameter<T extends YanxiuBaseBean, D , P> {
	/**
	 * 请求类型
	 * */
	public interface Type {
		public int POST = 0x2001;
		public int GET = 0x2002;
	}
	/**
	 * baseUrl请求地址
	 * */
	private String baseUrl;
	
	/**
	 * 参数
	 * */
	private P params ;

	/**
	 * 请求完成后的更新ID
	 * */
	private int updataId = -1;
	
	/**
	 * 请求方式 post �?  get
	 * */
	private int type;
	/**
	 * 请求重试次数
	 */
	private int mMaxNumRetries = 3;
	/**
	 * 请求结束后的解析�?
	 * */
	private YanxiuBaseParser<T , D> parser ;
	
	/**
	 * 请求回调，会在请求之前与请求之后回调相应函数
	 * */
	private YanxiuHttpParameterCallback callback ;

	protected String end;

	private boolean isEncode = false;

	public boolean isEncode() {
		return isEncode;
	}

	public void setIsEncode(boolean isEncode) {
		this.isEncode = isEncode;
	}

	public YanxiuHttpBaseParameter(String baseUrl, P params, int type, YanxiuBaseParser<T, D> parser, int updataId) {
		this.baseUrl = baseUrl;
		this.params = params;
		this.type = type;
		this.parser = parser ;
		this.updataId = updataId ;
	}
	public YanxiuHttpBaseParameter(String baseUrl, String end, P params, int type, YanxiuBaseParser<T, D> parser, int updataId) {
		this.baseUrl = baseUrl;
		this.params = params;
		this.type = type;
		this.parser = parser ;
		this.updataId = updataId ;
		this.end = end;
	}

	/**
	 * 得到baseUrl请求地址
	 * */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * 设置baseUrl请求地址
	 * */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	/**
	 * 得到参数
	 * */
	public P getParams() {
		return params;
	}

	/**
	 * 设置参数
	 * */
	public void setParams(P params) {
		this.params = params;
	}

	/**
	 * 得到请求完成后的更新ID
	 * */
	public int getUpdataId() {
		return updataId;
	}

	/**
	 * 设置请求完成后的更新ID
	 * */
	public void setUpdataId(int updataId) {
		this.updataId = updataId;
	}

	/**
	 * 得到请求结束后的解析�?
	 * */
	public YanxiuBaseParser<T , D> getParser() {
		return parser;
	}

	/**
	 * 设置请求结束后的解析�?
	 * */
	public void setParser(YanxiuBaseParser<T , D> parser) {
		this.parser = parser;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public YanxiuHttpParameterCallback getCallback() {
		return callback;
	}

	public void setCallback(YanxiuHttpParameterCallback callback) {
		this.callback = callback;
	}

	public void setMaxNumRetries(int mMaxNumRetries){
		this.mMaxNumRetries =mMaxNumRetries;
	}

	public int getMaxNumRetries() {
		return mMaxNumRetries;
	}

	/**
	 * 将参数组装成字符�?
	 * */
	public abstract StringBuilder encodeUrl() ;
}
