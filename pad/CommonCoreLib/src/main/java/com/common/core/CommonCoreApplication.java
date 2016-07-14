package com.common.core;


import com.common.core.utils.ContextProvider;
import com.common.core.utils.imageloader.UniversalImageLoadTool;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2016/1/14.
 */
public class CommonCoreApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.initIfNotInited(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        exitApp();
    }

    private void exitApp() {
        UniversalImageLoadTool.destroy();
    }

    protected void init(boolean debug,String pcode,boolean errorCatch,boolean isForTest,boolean isAnalyLayout) {
        ContextProvider.init(this);
        CoreConfiguration.initConfig(debug,pcode,errorCatch,isForTest,isAnalyLayout);
    }
}
