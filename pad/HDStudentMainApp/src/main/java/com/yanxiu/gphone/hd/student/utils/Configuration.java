package com.yanxiu.gphone.hd.student.utils;


import com.yanxiu.gphone.hd.student.BuildConfig;

public class Configuration {
//	private static boolean debug;
//
//	private static String pcode;
//
//	private static boolean errorCatch;
//
//	private static boolean isForTest;
//
//	private static boolean isAnalyLayout;


//	static {
//		try {
//			InputStream in = YanxiuApplication.getInstance().getResources().openRawResource(R.raw.yanxiu);
//			Properties properties = new Properties();
//			properties.load(in);
//			debug = Boolean.parseBoolean(properties.getProperty("yanxiu.debug"));
//			pcode = properties.getProperty("yanxiu.pcode");
//			String errorCatchStr = properties.getProperty("yanxiu.error.catch");
//			errorCatch = Boolean.parseBoolean(errorCatchStr);
//			String forTest = properties.getProperty("yanxiu.fortest");
//			isForTest = Boolean.parseBoolean(forTest);
//			String analyLayout=properties.getProperty("yanxiu.analylayouthierarchy");
//			isAnalyLayout=Boolean.parseBoolean(analyLayout);
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}aaaaa

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
