package com.yanxiu.gphone.parent.utils;


import com.yanxiu.gphone.parent.BuildConfig;

public class ParentConfiguration {


	public static boolean isDebug() {
		return BuildConfig.YANXIU_DEBUG;
	}

	public static String getPcode() {

		return BuildConfig.YANXIU_PCODE;
	}

	public static boolean isErrorCatch() {

		return BuildConfig.YANXIU_ERROR_CATCH;
	}

	public static boolean isForTest() {

		return BuildConfig.YANXIU_FORTEST;
	}

	public static boolean isAnalyLayout(){
		return BuildConfig.YANXIU_ANALYLAYOUTHIERARCHY;
	}
}
