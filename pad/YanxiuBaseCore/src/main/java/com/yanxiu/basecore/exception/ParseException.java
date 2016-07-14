package com.yanxiu.basecore.exception;

import com.yanxiu.basecore.utils.BaseCoreLogInfo;

/**
 * 解析异常
 * */

public class ParseException extends Exception{

	private static final long serialVersionUID = 1L;
	private String logmsg ;
	
	public ParseException(String logmsg) {
		this.logmsg = logmsg ;
	}
	
	@Override
	public void printStackTrace() {
		BaseCoreLogInfo.err(logmsg);
	}
}
