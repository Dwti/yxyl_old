package com.yanxiu.basecore;

import com.yanxiu.basecore.impl.YanxiuHttpBaseParameter;

import java.io.IOException;

public interface YanxiuHttpBaseHandler {
	
	public String doGet(YanxiuHttpBaseParameter<?, ?, ?> params) throws IOException;
	
	public String doPost(YanxiuHttpBaseParameter<?, ?, ?> params) throws IOException;
	
}
