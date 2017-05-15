package com.yanxiu.gphone.studentold.utils;


import com.yanxiu.gphone.studentold.BuildConfig;

public class Configuration {


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

	public static boolean isTestData() {
		return BuildConfig.YANXIU_MOKE;
	}
}
