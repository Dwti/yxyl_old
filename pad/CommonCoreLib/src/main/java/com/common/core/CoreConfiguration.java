package com.common.core;

/**
 * Created by Administrator on 2016/1/14.
 */
public class CoreConfiguration {
    private static boolean debug;

    private static String pcode;

    private static boolean errorCatch;

    private static boolean isForTest;

    private static boolean isAnalyLayout;
    public static void initConfig(boolean debug,String pcode,boolean errorCatch,boolean isForTest,boolean isAnalyLayout){
        CoreConfiguration.setDebug(debug);
        CoreConfiguration.setPcode(pcode);
        CoreConfiguration.setIsForTest(isForTest);
        CoreConfiguration.setErrorCatch(errorCatch);
        CoreConfiguration.setIsAnalyLayout(isAnalyLayout);
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        CoreConfiguration.debug = debug;
    }

    public static String getPcode() {
        return pcode;
    }

    public static void setPcode(String pcode) {
        CoreConfiguration.pcode = pcode;
    }

    public static boolean isErrorCatch() {
        return errorCatch;
    }

    public static void setErrorCatch(boolean errorCatch) {
        CoreConfiguration.errorCatch = errorCatch;
    }

    public static boolean isForTest() {
        return isForTest;
    }

    public static void setIsForTest(boolean isForTest) {
        CoreConfiguration.isForTest = isForTest;
    }

    public static boolean isAnalyLayout() {
        return isAnalyLayout;
    }

    public static void setIsAnalyLayout(boolean isAnalyLayout) {
        CoreConfiguration.isAnalyLayout = isAnalyLayout;
    }
}
