package com.common.login.model;

import android.content.ContentValues;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LocalCacheBean extends SrtBaseBean{
	private static final String CACHE_ID="cacheId";
	private static final String CACHE_DATA="cacheData";
	private static final String CACHE_TIME="cacheTime";
	private static final String POLE_ID="poleId";
	private String cacheId;
	private String cacheData;
	private long cacheTime;
	private int poleId;
	public String getCacheId() {
		return cacheId;
	}
	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}
	public String getCacheData() {
		return cacheData;
	}
	public void setCacheData(String cacheData) {
		this.cacheData = cacheData;
	}
	public long getCacheTime() {
		return cacheTime;
	}
	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}

	public int getPoleId() {
		return poleId;
	}

	public void setPoleId(int poleId) {
		this.poleId = poleId;
	}

	public LocalCacheBean(String cacheId, String cacheData, long cacheTime) {
		super();
		this.cacheId = cacheId;
		this.cacheData = cacheData;
		this.cacheTime = cacheTime;
	}
	
	public LocalCacheBean() {
	}
	@Override
	public String toString() {
		return "LocalCacheBean ["+CACHE_ID+"=" + cacheId + ", "+CACHE_DATA+"="
				+ cacheData.length() + ", "+CACHE_TIME+"=" + cacheTime + "]";
	}
	
	public static boolean saveData(LocalCacheBean mBean){
		if(has(mBean)){
			ContentValues values = new ContentValues();
			values.put(CACHE_ID, mBean.getCacheId());
			values.put(CACHE_DATA, mBean.getCacheData());
			values.put(CACHE_TIME, mBean.getCacheTime());
			values.put(POLE_ID,mBean.getPoleId());
			DataSupport.updateAll(LocalCacheBean.class, values, CACHE_ID+" = ?",
					mBean.getCacheId());
		} else {
			mBean.save();
		}
		return true;
	}
	public static void deleteData(LocalCacheBean mBean){
		DataSupport.deleteAll(LocalCacheBean.class, CACHE_ID+" = ?",
				mBean.getCacheId());
	}
	public static void deleteAllData(){
		DataSupport.deleteAll(LocalCacheBean.class, CACHE_ID+" <> ?",
				"abc");
	}
	public static LocalCacheBean findDataById(String cacheId){
		List<LocalCacheBean> newsList = DataSupport
				.where(CACHE_ID+" = ?", cacheId).find(LocalCacheBean.class);
		if(newsList != null && newsList.size()>0){
			return newsList.get(0);
		} else {
			return null;
		}
	}
	public static boolean has(LocalCacheBean mBean){
		return DataSupport.where(CACHE_ID+" = ?", mBean.getCacheId()).count(LocalCacheBean.class)>0;
	}
}
