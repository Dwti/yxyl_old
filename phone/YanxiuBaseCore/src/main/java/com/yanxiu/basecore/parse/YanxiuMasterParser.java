package com.yanxiu.basecore.parse;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主站接口，解析器父类
 * �?
 *	bean:{...},
 *	action:"...",
 *	responseType:"...",
 *	status:"...",
 *	errorCode:"...",
 *	message:"..."
 * �?
 * 针对返回模式这样的解�?
 * */
public abstract class YanxiuMasterParser<T extends YanxiuBaseBean> extends YanxiuMainParser<T , JSONObject> {

	/**
	 * 接口信息节点
	 * */
	protected final String ACTION = "action" ;
	/**
	 * 接口返回状�?�：1-正常�?2-无数据，3-服务异常�?4-数据无变�?
	 * */
	protected final String RESPONSETYPE = "responseType" ;
	/**
	 * 接口返回数据节点
	 * */
	protected final String STATUS = "status" ;
	/**
	 * 接口返回数据节点
	 * */
	protected final String ERRORCODE = "code" ;
	/**
	 * 接口返回数据节点
	 * */
	protected final String MESSAGE = "desc" ;
	
	protected final String BEAN = "bean" ;
	
	public YanxiuMasterParser(){
		super();
	}
	
	public YanxiuMasterParser(int from){
		super(from);
	}
	
	@Override
	protected final boolean canParse(String data) {
		try {
			JSONObject object = new JSONObject(data);
			if(!object.has(STATUS)) {
				return false ;
			}
			int status = object.getInt(STATUS);
			int errorCode = object.optInt(ERRORCODE);
			String message = object.optString(MESSAGE);
			
			setMessage(message);
			if(status == 1 && errorCode == 0){
				return true ;
			}else{
				setErrorMsg(errorCode);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected JSONObject getData(String data) throws JSONException {
		JSONObject object = new JSONObject(data);
		object = getJSONObject(object, BEAN);
		return object;
	}
}
