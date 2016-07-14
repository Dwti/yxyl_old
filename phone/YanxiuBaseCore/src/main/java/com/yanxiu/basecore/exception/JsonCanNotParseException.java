package com.yanxiu.basecore.exception;

import com.yanxiu.basecore.utils.BaseCoreLogInfo;

/**
 * json数据在canParse方法判断为false的异常
 * */

public class JsonCanNotParseException extends Exception{

	private static final long serialVersionUID = 1L;
	private String logmsg ;
	
	public JsonCanNotParseException(String logmsg) {
		this.logmsg = logmsg ;
	}
	
	@Override
	public void printStackTrace() {
		BaseCoreLogInfo.err(logmsg);
	}
}
