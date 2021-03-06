package com.yanxiu.gphone.hd.student;

import android.content.Context;

import com.common.core.CommonCoreApplication;
import com.common.core.manage.CommonActivityManager;
import com.common.core.utils.ContextProvider;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.utils.BaseCoreLogInfo;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.bean.statistics.InstantUploadErrorData;
import com.yanxiu.gphone.hd.student.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.hd.student.bean.statistics.UploadInstantPointDataBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.utils.Configuration;
import com.yanxiu.gphone.hd.student.utils.CrashHandler;
import com.yanxiu.gphone.hd.student.utils.HeadInfoObserver;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.utils.statistics.DataBaseManager;
import com.yanxiu.gphone.hd.student.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.hd.student.utils.statistics.requestAsycn.UploadInstantPointDataTask;
import com.yanxiu.gphone.upgrade.utils.UpgradeConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class YanxiuApplication extends CommonCoreApplication {
//    public LruDiskCache discCache;
    private static YanxiuApplication instance;
    private SubjectVersionBean subjectVersionBean;
    private CrashHandler crashHandler;
    private boolean isForceUpdate = false;

    @Override
    public void onCreate () {
        super.onCreate();
        /**
         * 注册全局异常捕获
         * */
        instance = this;
        BaseCoreLogInfo.setIsDebug(Configuration.isDebug());
        this.init(Configuration.isDebug(), Configuration.getPcode(), Configuration.isErrorCatch(), Configuration.isForTest(), Configuration.isAnalyLayout());
        initImageLoader(getApplicationContext());
        if (Configuration.isErrorCatch()) {
            crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        initUpgradeConstant();
       // LeakCanary.install(this);
        startStatistics();
    }

    //启动打点
    private void startStatistics() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_2");//2:启动APP
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> staratAppHashMap = new HashMap<>();
        staratAppHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(this, staratAppHashMap);
    }
    /**
     * 升级环境配置
     */
    private void initUpgradeConstant(){
        UpgradeConstant.setAppName(getString(R.string.app_name));
        UpgradeConstant.setOsType(YanXiuConstant.OS_TYPE);
        UpgradeConstant.setDEVICEID(YanXiuConstant.DEVICEID);
        UpgradeConstant.setIsForTest(Configuration.isDebug());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ContextProvider.destoryContext();
    }

    public SubjectVersionBean getSubjectVersionBean () {
        return subjectVersionBean;
    }

    public void setSubjectVersionBean (SubjectVersionBean subjectVersionBean) {
        this.subjectVersionBean = subjectVersionBean;
    }

    public static YanxiuApplication getInstance () {
        return instance;
    }

    public static void initImageLoader (Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//				.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建


        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }


    public void exitProcess(){
        exitStatistics();

    }

    //退出打点
    private void exitStatistics() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_8");//9:退出APP
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> exitAppHashMap = new HashMap<>();
        exitAppHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        uploadSinglePoint(this, exitAppHashMap);
    }

    private UploadInstantPointDataTask uploadInstantPointDataTask;
    /**
     * 上传点数据
     *
     * @param params 需要上传的数据集合
     */
    private void uploadSinglePoint(Context context, final HashMap<String, String> params) {
        if (uploadInstantPointDataTask != null) {
            uploadInstantPointDataTask.cancel();
        }
        uploadInstantPointDataTask = new UploadInstantPointDataTask(context, params, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                if (result != null) {
                    LogInfo.log("frc", "file upload result " + result);
                    UploadInstantPointDataBean uploadInstantPointDataBean = (UploadInstantPointDataBean) result;
                    if (uploadInstantPointDataBean.getResult().equals("ok")) {

                        LogInfo.log("frc", "file upload success ");
                    } else {
                        //LogInfo.log("frc", "file upload error :::" + uploadInstantPointDataBean.getDesc());
                        //TODO 将数据保存到数据库 具体数据看server
                        addDataToSQL(params);
                    }
                    HeadInfoObserver.getInstance().resetHeadInfoObserver();
                    ActivityManager.destory();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("frc", "dataError:  " + msg);
                //TODO  将数据保存到数据库
                addDataToSQL(params);
                HeadInfoObserver.getInstance().resetHeadInfoObserver();
                ActivityManager.destory();
                android.os.Process.killProcess(android.os.Process.myPid());
            }

        });
        uploadInstantPointDataTask.start();
    }

    /**
     * 将数据保存到数据库 具体数据看server
     *
     * @param params
     * @return
     */
    private boolean addDataToSQL(HashMap<String, String> params) {
        InstantUploadErrorData instantUploadErrorData = new InstantUploadErrorData();
        instantUploadErrorData.setDataContent(params.get("data_content"));
        instantUploadErrorData.setDataName(params.get("data_name"));
        instantUploadErrorData.setUploadTime(new Date());
        return DataBaseManager.getInstance().addData(instantUploadErrorData);

    }

    public boolean isForceUpdate () {
        return isForceUpdate;
    }

    public void setIsForceUpdate (boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

//	@Override
//	protected void attachBaseContext (Context base) {//让应用支持多DEX文件
//		super.attachBaseContext(base);
//		MultiDex.install(this);
//	}
}
