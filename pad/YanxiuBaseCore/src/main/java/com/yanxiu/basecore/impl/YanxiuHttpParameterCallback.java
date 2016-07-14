package com.yanxiu.basecore.impl;

import java.net.HttpURLConnection;

public interface YanxiuHttpParameterCallback {

	public void proRequest(HttpURLConnection connection);
	
	public void laterRequest(HttpURLConnection connection);
}
