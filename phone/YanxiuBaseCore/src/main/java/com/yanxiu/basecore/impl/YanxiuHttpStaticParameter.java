package com.yanxiu.basecore.impl;

import android.text.TextUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.parse.YanxiuBaseParser;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * 请求参数组装�?
 * 封装�?
 * 请求地址
 * 参数
 * 解析�?
 * 刷新ID
 * */
public class YanxiuHttpStaticParameter<T extends YanxiuBaseBean, D> extends YanxiuHttpBaseParameter<T, D, ArrayList<BasicNameValuePair>> {

	/**
	 * 静�?�请求结�?
	 * */
	private String end ; 
	
	public YanxiuHttpStaticParameter(String head, String end, ArrayList<BasicNameValuePair> params, YanxiuBaseParser<T, D> parser, int updataId) {
		super(head, params, Type.GET, parser, updataId);
		this.end = end ;
	}

	@Override
	public StringBuilder encodeUrl() {
		
		ArrayList<BasicNameValuePair> params = getParams() ;
		StringBuilder sb = new StringBuilder();
		if (params == null || params.isEmpty()) {
			return sb ;
		}
		for (BasicNameValuePair key : params) {
			if(!TextUtils.isEmpty(key.getName()) && !TextUtils.isEmpty(key.getValue())){
				sb.append("/");
				sb.append(key.getName());
				sb.append("/");
				sb.append(key.getValue());
			}
		}
		sb.append(end);
		return sb;
	}
}
