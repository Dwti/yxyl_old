package com.yanxiu.gphone.studentold.utils.statistics;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.bean.statistics.InstantUploadErrorData;
import com.yanxiu.gphone.studentold.bean.statistics.UploadInstantPointDataBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.utils.statistics.interf.DataStatisticsUploadCallBack;
import com.yanxiu.gphone.studentold.utils.statistics.requestAsycn.UploadInstantPointDataTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FengRongCheng on 2016/5/29 10:02.
 * powered by yanxiu.com
 * 数据统计上传
 */
public class DataStatisticsUploadManager {
    private static final String TAG = DataStatisticsUploadManager.class.getSimpleName();
    private DataStatisticsUploadCallBack dataStatisticsUploadCallBack;
    private Context mContext;


    public DataStatisticsUploadManager() {

    }

    /**
     * 获取单列
     *
     * @return
     */
    public static DataStatisticsUploadManager getInstance() {
        return DataStatisticsUploadHolder.instance;
    }

    private static class DataStatisticsUploadHolder {
        private static final DataStatisticsUploadManager instance = new DataStatisticsUploadManager();
    }

    /**
     * 上传统计数据
     *
     * @param type
     * @param params
     * @param uploadTime
     * @param errorRetryType
     */
    public void UploadDataStatistics(final Context context, final int type, final HashMap<String, String> params, final DataStatisticsConfig.uploadTime uploadTime, final DataStatisticsConfig.errorRetryType errorRetryType) {
        Message message = new Message();
        mContext = context;
        switch (type) {
            case DataStatisticsConfig.TYPE_SINGLE_POINT_UPLOAD:
                message.what = ADD_PARAMS_TO_INSTANT_UPLOAD;
                message.obj = params;
                handler.sendMessage(message);
                break;
            case DataStatisticsConfig.TYPE_MORE_POINT_UPLOAD:
                List<InstantUploadErrorData> dataList = DataBaseManager.getInstance().findUploadErrorData();
                message.what = ADD_UPLOAD_ERROR_DATA_TO_INSTANT_UPLOAD;
                message.obj = dataList;
                handler.sendMessage(message);
                break;
            case DataStatisticsConfig.TYPE_FILE_UPLOAD:
                //获取文件上传
                uploadAllPointFile();
                break;
            case DataStatisticsConfig.TYPE_DEV_LOG_UPLOAD:
                //  uploadDevLog();
                break;
            case DataStatisticsConfig.TYPE_CRASH_LOG_UPLOAD:
                // uploadCrashLog();
                break;
        }

    }

    private final static int ADD_PARAMS_TO_INSTANT_UPLOAD = 0x001;
    private final static int ADD_UPLOAD_ERROR_DATA_TO_INSTANT_UPLOAD = 0x002;
    private final static int GO_ON_TO_UPLOAD_LIST_DATA = 0x003;


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
                        handler.sendEmptyMessage(GO_ON_TO_UPLOAD_LIST_DATA);

                    }
                }
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("frc", "dataError:  " + msg);
                addDataToSQL(params);
                handler.sendEmptyMessage(GO_ON_TO_UPLOAD_LIST_DATA);
            }

        });
        uploadInstantPointDataTask.start();


    }

    /**
     * 上传包含所有点的文件
     */
    private void uploadAllPointFile() {
        MakeDataTOFileUtil makeDataTOFileUtil = new MakeDataTOFileUtil();
        List<File> fileList = makeDataTOFileUtil.getAllPointFile();
        Map<String, File> hashMap = new HashMap<>();
        //hashMap.put("allPointFile", fileList.get(fileList.size() - 1));
        for (int i = 0; i < fileList.size(); i++) {
            hashMap.put("allPointFile" + i, fileList.get(i));
        }
        UploadFile(YanxiuHttpApi.UPLOAD_ALL_POINT_FILE, hashMap);

    }

    /**
     * 上传开发者调式文件
     *
     * @param uploadDevFile
     */
    private void uploadDevFile(Map uploadDevFile) {
        UploadFile(YanxiuHttpApi.UPLOAD_DEV_FILE, uploadDevFile);

    }

    /**
     * 上传崩溃日志文件
     *
     * @param uplaodCrashFile
     */
    private void uploadCrashFile(Map uplaodCrashFile) {
        UploadFile(YanxiuHttpApi.UPLOAD_CRASH_FILE, uplaodCrashFile);

    }

    /**
     * 设置数据上传回掉监听
     *
     * @param dataStatisticsUploadCallBack
     */
    private void setOnDataStatisicsUploadListner(DataStatisticsUploadCallBack dataStatisticsUploadCallBack) {
        this.dataStatisticsUploadCallBack = dataStatisticsUploadCallBack;
    }

    /**
     * 常用的提交数据方式
     *
     * @param params
     */
    public void NormalUpLoadData(Context context, HashMap<String, String> params) {
        UploadDataStatistics(context, DataStatisticsConfig.TYPE_SINGLE_POINT_UPLOAD, params, DataStatisticsConfig.uploadTime.UPLOAD_NOW, DataStatisticsConfig.errorRetryType.GIVE_UP);
    }

    /**
     * 提交打点数据统计文件
     *
     * @param filePath 文件路径
     */
    public void UpLoadDataStatisticsFile(Context context, String filePath) {
        HashMap<String, String> params = new HashMap<>();
        params.put("fileName", filePath);
        UploadDataStatistics(context, DataStatisticsConfig.TYPE_FILE_UPLOAD, params, DataStatisticsConfig.uploadTime.UPLOAD_WHEN_OPEN_APP, DataStatisticsConfig.errorRetryType.TRY_THREE_TIMES);

    }

    /**
     * 提交开发者日志文件
     *
     * @param filePath 文件路径
     */
    public void UpLoadDevFile(Context context, String filePath) {
        HashMap<String, String> params = new HashMap<>();
        params.put("fileName", filePath);
        UploadDataStatistics(context, DataStatisticsConfig.TYPE_DEV_LOG_UPLOAD, params, DataStatisticsConfig.uploadTime.UPLOAD_WHEN_OPEN_APP, DataStatisticsConfig.errorRetryType.TRY_THREE_TIMES);

    }

    /**
     * 提交app崩溃日志文件
     *
     * @param filePath 文件路径
     */
    public void UpLoadCrashFile(Context context, String filePath) {
        HashMap<String, String> params = new HashMap<>();
        params.put("fileName", filePath);
        UploadDataStatistics(context, DataStatisticsConfig.TYPE_CRASH_LOG_UPLOAD, params, DataStatisticsConfig.uploadTime.UPLOAD_WHEN_OPEN_APP, DataStatisticsConfig.errorRetryType.TRY_THREE_TIMES);

    }

    /**
     * 上传文件方法
     *
     * @param uploadFileUrl
     * @param map
     */
    private void UploadFile(final String uploadFileUrl, final Map map) {
        AsyncTask asyncTask = new AsyncTask<Object, Void, String>() {


            @Override
            protected String doInBackground(Object... objects) {
                String result = YanxiuHttpApi.reuqestUploadFile(uploadFileUrl, new HashMap<String, String>(), map, new YanxiuHttpApi.UploadListener() {
                    @Override
                    public void onProgress(int progress) {


                    }
                }, false);

                return result;
            }
        };
        asyncTask.execute();
    }

    List<HashMap<String, String>> instantUploadErrorDataList = new ArrayList<>();

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_UPLOAD_ERROR_DATA_TO_INSTANT_UPLOAD:
                    if (msg.obj != null) {
                        List dataList = (List) msg.obj;
                        instantUploadErrorDataList.addAll(dataList);
                    }

                    break;
                case ADD_PARAMS_TO_INSTANT_UPLOAD:
                    if (msg.obj != null) {
                        HashMap<String, String> param = (HashMap<String, String>) msg.obj;
                        instantUploadErrorDataList.add(param);
                    }

                    break;
                case GO_ON_TO_UPLOAD_LIST_DATA:
                    LogInfo.log("frc", "再次上传");
                    break;
            }
            if (instantUploadErrorDataList.size() > 0) {
                HashMap params = instantUploadErrorDataList.get(instantUploadErrorDataList.size() - 1);
                instantUploadErrorDataList.remove(instantUploadErrorDataList.size() - 1);
                uploadSinglePoint(mContext, params);

            } else {
                LogInfo.log("frc", "埋点数据已经上传完成");
                uploadInstantPointDataTask.cancel();
            }


        }
    };

}
