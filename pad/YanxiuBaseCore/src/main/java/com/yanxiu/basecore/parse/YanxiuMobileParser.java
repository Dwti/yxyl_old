package com.yanxiu.basecore.parse;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 移动端接口，解析器父类 ｛ header:{status:"x"}, body:{...} ｝ 针对返回模式这样的解析
 * */
public abstract class YanxiuMobileParser<T extends YanxiuBaseBean> extends YanxiuMainParser<T, JSONObject> {

	/**
	 * 接口信息节点
	 * */
	protected final String HEADER = "header";
	/**
	 * 接口返回状态：1-正常，2-无数据，3-服务异常
	 * */
	protected final String STATUS = "status";
	/**
	 * 接口返回状态 code
	 * */
	protected final String CODE = "code";
	/**
	 * 接口时间轴
	 * */
	protected final String MARKID = "markid";
	/**
	 * 接口返回数据节点
	 * */
	protected final String BODY = "body";
	protected final String MSG = "msg";

	public interface STATE {
		public int NORMAL = 0;
		public int NODATA = 1;
		public int NORIGHT = 2;
		public int NOTOKEN = 3;
		public int NOUPDATE = 4;
	}

	/**
	 * 接口状态
	 * */
	public int status;

	/**
	 * 接口时间轴
	 * */
	private String markid;

	public YanxiuMobileParser() {
		super();
	}

	public YanxiuMobileParser(int from) {
		super(from);
	}

	@Override
	protected final boolean canParse(String data) {
		try {
			JSONObject object = new JSONObject(data);
			if(object.has("openid")){ //第三方登录特殊情况
				return true;
			}
			if (!object.has(STATUS) && !object.has(CODE)) {
				return false;
			}
			if(object.has(STATUS)){
				JSONObject statueObj = getJSONObject(object, STATUS);
				setErrorMsg(getInt(statueObj,"code"));
				setMessage(getString(statueObj, "desc"));
			}else if(object.has(CODE)){
				setErrorMsg(getInt(object, "code"));
				setMessage(getString(object, "desc"));
			}
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean hasUpdate() {
		return status != STATE.NOUPDATE;
	}

	@Override
	protected JSONObject getData(String data) throws JSONException {
		JSONObject object = new JSONObject(data);
		return object;
	}

	/**
	 * 加载本地数据，需要缓存数据的解析器需要实现
	 * */
	protected String getLocationData() {
		return null;
	};

	public String getMarkId() {
		return markid;
	}

	public boolean isNewData() {
		return status == STATE.NORMAL;
	}

	public boolean isNoUpdate() {
		return status == STATE.NOUPDATE;
	}
}
