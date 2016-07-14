package com.yanxiu.basecore.impl;

import android.os.Bundle;
import android.text.TextUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.parse.YanxiuBaseParser;

import java.net.URLEncoder;

/**
 * 请求参数组装�?
 * 封装�?
 * 请求地址
 * 参数
 * 解析�?
 * 刷新ID
 * */
public class YanxiuHttpParameter<T extends YanxiuBaseBean, D> extends YanxiuHttpBaseParameter<T, D, Bundle> {

	public YanxiuHttpParameter(String baseUrl, Bundle params, int type, YanxiuBaseParser<T, D> parser, int updataId) {
		super(baseUrl, params, type, parser, updataId);
	}
	public YanxiuHttpParameter(String baseUrl, String end, Bundle params, int type, YanxiuBaseParser<T, D> parser, int updataId) {
		super(baseUrl, end, params, type, parser, updataId);
	}

	@Override
	public StringBuilder encodeUrl() {
		StringBuilder sb = new StringBuilder();
		if (getParams() == null) {
			return sb ;
		}
		boolean first = true;
		for (String key : getParams().keySet()) {
			if (first) {
				if(getType() == Type.GET){
					sb.append("?");
				}
				first = false;
			} else {
				sb.append("&");
			}
			String pa = getParams().getString(key);
			if(pa != null){
				if (key.equals("yxyl_statistic")) {
					sb.append(pa);
				} else {
					sb.append(key + "=" + URLEncoder.encode(pa));
				}

			}else{
				sb.append(key + "=");
			}
		}
		if(!TextUtils.isEmpty(end)){
			sb.append(end);
		}
		return sb;
	}
}
