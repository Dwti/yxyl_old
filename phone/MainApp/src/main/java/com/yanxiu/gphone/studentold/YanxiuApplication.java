package com.yanxiu.gphone.studentold;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.common.core.CommonCoreApplication;
import com.common.core.manage.CommonActivityManager;
import com.common.core.utils.ContextProvider;
import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tendcloud.tenddata.TCAgent;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.utils.BaseCoreLogInfo;
//import com.yanxiu.gphone.parent.utils.ParentConfigConstant;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;
import com.yanxiu.gphone.studentold.bean.UrlBean;
import com.yanxiu.gphone.studentold.bean.statistics.InstantUploadErrorData;
import com.yanxiu.gphone.studentold.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.studentold.bean.statistics.UploadInstantPointDataBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.utils.Configuration;
import com.yanxiu.gphone.studentold.utils.CrashHandler;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.utils.statistics.DataBaseManager;
import com.yanxiu.gphone.studentold.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.studentold.utils.statistics.requestAsycn.UploadInstantPointDataTask;
//import com.yanxiu.gphone.upgrade.utils.UpgradeConstant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class YanxiuApplication extends CommonCoreApplication {
    public LruDiskCache discCache;
    private static YanxiuApplication instance;
    protected RequestQueue volleryRequest;
    public static boolean hasShowed=false;  //播放听力的时候，非wifi网络下面是否提示过（APP运行期间只提示一次）

    /**
     * 图片下载器
     */
    protected ImageLoader imageLoader;
    private SubjectVersionBean subjectVersionBean;
    private CrashHandler crashHandler;
    private boolean isForceUpdate = false;
    private Object urlJson;

    @Override
    public void onCreate() {

        super.onCreate();
//		LeakCanary.install(this);
        /**
         * 注册全局异常捕获
         * */

        TCAgent.LOG_ON = true;
        TCAgent.init(this, "A3A47ECEBAB1C56762367CE55629C402", "all android markets");
        TCAgent.setReportUncaughtExceptions(true);

        instance = this;
        volleryRequest = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(volleryRequest);
        BaseCoreLogInfo.setIsDebug(Configuration.isDebug());
        this.init(Configuration.isDebug(), Configuration.getPcode(), Configuration.isErrorCatch(), Configuration.isForTest(), Configuration.isAnalyLayout());
        initImageLoader(getApplicationContext());
        if (Configuration.isErrorCatch()) {
            crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        LogInfo.log("haitian", "DevoceId=" + YanXiuConstant.DEVICEID);
        initParentConstant();
        initUpgradeConstant();
        getUrlJson();


    }

    //启动打点
    private void startStatistics() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_7");//2:启动APP
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> staratAppHashMap = new HashMap<>();
        staratAppHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(this, staratAppHashMap);
    }

    private void uploadInitInfo(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put(YanXiuConstant.uid, String.valueOf(LoginModel.getUid()));
        hashMap.put(YanXiuConstant.appkey, "20001");
        hashMap.put(YanXiuConstant.timestamp, String.valueOf(System.currentTimeMillis()));
        hashMap.put(YanXiuConstant.source, String.valueOf(0));// 来源：source（0，移动端，1，页面）
        hashMap.put(YanXiuConstant.clientType, String.valueOf(1));// 客户端类型：client（0，iOS，1，android）
        hashMap.put(YanXiuConstant.ip, "");
        hashMap.put(YanXiuConstant.url, "www.yanxiu.com");
        hashMap.put(YanXiuConstant.resID, "");
        hashMap.put(YanXiuConstant.eventID, "20:event_10");//   首次启动APP
        hashMap.put(YanXiuConstant.mobileModel, Build.MODEL);   //手机型号
        hashMap.put(YanXiuConstant.brand,Build.MANUFACTURER);         //手机品牌
        hashMap.put(YanXiuConstant.system,Build.VERSION.RELEASE);        //手机系统
        hashMap.put(YanXiuConstant.resolution,YanXiuConstant.displayMetrics.heightPixels + " * " +YanXiuConstant.displayMetrics.widthPixels);    //屏幕分辨率
        hashMap.put(YanXiuConstant.netModel, NetWorkTypeUtils.getNetType());      //连网类型

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        arrayList.add(hashMap);

        HashMap<String,String> hashMap1 = new HashMap<>();
        hashMap1.put(YanXiuConstant.content,Util.listToJson(arrayList));

        DataStatisticsUploadManager.getInstance().NormalUpLoadData(this, hashMap1);
    }
    /**
     * 升级环境配置
     */
    private void initUpgradeConstant(){
//        UpgradeConstant.setDEVICEID(YanXiuConstant.DEVICEID);
//        UpgradeConstant.setIsForTest(Configuration.isDebug());
    }
    /**
     * 家长端初始化
     */
    private void initParentConstant () {
        /*ParentConfigConstant.setDEVICEID(YanXiuConstant.DEVICEID);
        ParentConfigConstant.setDisplayMetrics(YanXiuConstant.displayMetrics);
        ParentConfigConstant.setVERSION(YanXiuConstant.VERSION);
        ParentConfigConstant.setVersionCode(YanXiuConstant.VERSION_CODE);*/
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
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.

//		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//		config.threadPriority(Thread.NORM_PRIORITY - 2);
//		config.denyCacheImageMultipleSizesInMemory();
//		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//		config.tasksProcessingOrder(QueueProcessingType.LIFO);
//		config.memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
//				.threadPoolSize(3)//线程池内加载的数量
//				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//				.memoryCacheSize(2 * 1024 * 1024)
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.discCacheFileCount(100) //缓存的文件数量
//				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//				.writeDebugLogs();// Remove for releaseapp

//		config.writeDebugLogs(); // Remove for release app


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

    /**
     * 退出应用处理
     */
    public void exitProcess() {
        exitStatistics();
    }

    //退出打点
    private void exitStatistics() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_8");//8:退出APP
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
                        addDataToSQL(params);
                    }
                    CommonActivityManager.destory();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("frc", "dataError:  " + msg);
                addDataToSQL(params);
                CommonActivityManager.destory();
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

    public Object getUrlJson() {
        AssetManager assetManager = getAssets();
        String urlBeanJson=getFromAssets("env_config.json");
        UrlBean urlBean=JSON.parseObject(urlBeanJson, UrlBean.class);
        YanxiuHttpApi.setBaseURL(urlBean);
        return urlJson;
    }
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
//	@Override
//	protected void attachBaseContext (Context base) {//让应用支持多DEX文件
//		super.attachBaseContext(base);
//		MultiDex.install(this);
//	}
}
