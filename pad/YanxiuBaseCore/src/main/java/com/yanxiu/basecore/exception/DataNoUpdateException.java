package com.yanxiu.basecore.exception;

import com.yanxiu.basecore.utils.BaseCoreLogInfo;

/**
 * 请求数据为空的异常
 * */

public class DataNoUpdateException extends Exception{

	private static final long serialVersionUID = 1L;
	private String logmsg ;
	
	public DataNoUpdateException(String logmsg) {
		this.logmsg = logmsg ;
	}
	
	@Override
	public void printStackTrace() {
		BaseCoreLogInfo.err(logmsg);
	}
}
