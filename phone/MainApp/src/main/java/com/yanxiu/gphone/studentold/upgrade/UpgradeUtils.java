package com.yanxiu.gphone.studentold.upgrade;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.http.HttpTaskManager;
//import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.NewInitializeBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.manager.ActivityManager;
import com.yanxiu.gphone.studentold.preference.PreferencesManager;
import com.yanxiu.gphone.studentold.requestTask.InitializeTask;

import com.yanxiu.gphone.studentold.utils.UpdataUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.UpgradeDialog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by king on 2015/5/14.
 */
public class UpgradeUtils {

    private static UpgradeDialog mDialogWindow;

    private static Context mContext;

    private static OnUpgradeCallBack mOnUpgradeCallBack;

    public static final int NOTIFICATION_ID = 0x11;

    public static NotificationManager notificationManager;

    public static Notification notification;

    private static InitializeTask updateTask;

    /**
     *
     * @param nowVersion
     * @param serverVersion
     */
    public static boolean onUpdate(String nowVersion, String serverVersion) {
        if (!StringUtils.isEmpty(nowVersion) && !StringUtils
                .isEmpty(serverVersion)) {
            if (!nowVersion.equals(serverVersion)) {
                return true;
            }
        }
        return false;
    }
    /**
     * show popupwindow
     */
    @TargetApi(Build.VERSION_CODES.KITKAT) public static void showUpdateDialogWin(
            Context context,final NewInitializeBean initializeBean,
            OnUpgradeCallBack onUpgradeCallBack) {
        mContext = context;
        if(onUpgradeCallBack!=null){
            mOnUpgradeCallBack = onUpgradeCallBack;
        }
        if(mDialogWindow!=null){
            mDialogWindow.dismiss();
            mDialogWindow = null;
        }
        mDialogWindow = new UpgradeDialog(mContext, initializeBean, new UpgradeDialog.UpgradeDialogCallBack() {
            @Override public void upgrade() {
                notificationManager =(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notification = new Notification();
                downloadApk(initializeBean.getFileURL());
            }
            @Override public void cancel() {
            }
            @Override public void exit() {
                if (mOnUpgradeCallBack != null) {
                    mOnUpgradeCallBack.onExit();
                }
            }
        });
        mDialogWindow.setCanceledOnTouchOutside(false);
        mDialogWindow.show();
        LogInfo.log("king", "showUpdateDialogWin");
    }

    /**
     * download apk
     */
    public static void downloadApk(String path) {
//        path = "http://down.m.letv.com/android/static/apk/2015/0605/LetvClient_V5.9_From_102.apk";
        HttpTaskManager.getInstance().submit(new Loader(path,Loader.STUDENT_UPLOAD, mLoaderListener));
    }

    /**
     * @param filePath apk file path
     * @return true
     * install success
     * false
     * install false
     */
    public static void installApk(String filePath) {
        LogInfo.log("king", "filePath == " + filePath);
        //FileUtils.RecursionDeleteFile(new File(YanxiuParentConstants.SHARE_ICON_PATH));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PreferencesManager.getInstance().setFristApp(true);
        String type = "application/vnd.android.package-archive";
        File file = new File(filePath);
        intent.setDataAndType(Uri.fromFile(file), type);
        mContext.startActivity(intent);
    }

    /**
     * uninstall apk
     *
     * @param packageName
     */
    public static boolean uninstall(String packageName) {
        try {
            Process process = Runtime.getRuntime().exec("/system/xbin/su");
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("adb remount \n");
            os.writeBytes("adb uninstall " + packageName + " \n");
            os.writeBytes("sync \n");
            os.writeBytes("exit \n");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println("line == " + line);
                if (line.indexOf("Success") != -1) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public interface OnUpgradeCallBack {
        void onExit();

        void onDownloadApk(boolean isSuccess);

        void onInstallApk(boolean isSuccess);
    }

    private static Loader.LoaderListener mLoaderListener = new Loader.LoaderListener() {

        @Override public void onStart() {
            LogInfo.log("king","onStart ");
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            //notification.icon = R.anim.notification_download;
            notification.tickerText = mContext.getResources().getString(
                    R.string.update_asynctask_downloading);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.icon=R.drawable.notification04;
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_updata_layout);
            remoteViews.setTextViewText(R.id.app_name, mContext.getResources().getString(
                    R.string.app_name));
            remoteViews.setTextViewText(R.id.progress_text, "0%");
            remoteViews.setProgressBar(R.id.progress_value, 100, 0, false);
            notification.contentView = remoteViews;
            notification.contentIntent = pendingIntent;
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        @Override public void onError(String message, int code) {
            LogInfo.log("king",
                    "onError code = " + code + " ,message = " + message);
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.notification01;
            notification.tickerText = mContext.getResources().getString(R.string.update_asynctask_downloading_fail);
            if (mOnUpgradeCallBack != null) {
                mOnUpgradeCallBack.onDownloadApk(false);
            }
        }

        @Override public void onFailure(String message, int code) {
            LogInfo.log("king","onFailure code = " + code + " ,message = " + message);
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.notification01;
            notification.tickerText = mContext.getResources().getString(R.string.update_asynctask_downloading_fail);
            if (mOnUpgradeCallBack != null) {
                mOnUpgradeCallBack.onDownloadApk(false);
            }
        }

        @Override public void onComplete(String path) {
            LogInfo.log("king", "onComplete path = " + path);
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.notification01;
            notification.tickerText = mContext.getResources().getString(R.string.update_asynctask_downloading_success);
            notification.contentView.setProgressBar(R.id.progress_value, 100,
                    100, false);
            notification.contentView.setViewVisibility(R.id.progress_value,
                    View.GONE);
            if (mOnUpgradeCallBack != null) {
                installApk(path);
            }
        }

        @Override public void onProgress(double progress) {
            LogInfo.log("king","onProgress progress = " + progress);
            mDialogWindow.setProgress((int) progress);
            notification.contentView.setProgressBar(R.id.progress_value, 100, (int) progress, false);
            notification.contentView.setTextViewText(R.id.progress_text, (int)progress + "%");
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
        @Override
        public void onCancel() {
            LogInfo.log("king","onCancel");
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.notification01;
            notification.tickerText = mContext.getResources().getString(R.string.update_asynctask_downloading_cel);
        }
        @Override
        public void onUnAvailable() {

        }
    };



    public static void cancelUpgrade(){
        if(updateTask!=null){
            updateTask.cancel();
            updateTask=null;
        }
    }


    /**
     * 初始化接口
     */
    public static void requestInitialize(final boolean fromUser,final Activity activity) {
        if(true)
            return;   //屏蔽升级功能
        if (!fromUser&&YanXiuConstant.updata == 1){
            return;
        }
        YanXiuConstant.updata = 1;
        cancelUpgrade();
        String channel=UpdataUtils.getChannelName(activity);
        updateTask=new InitializeTask(activity, "",channel, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                if (result != null) {
                    NewInitializeBean initializeBean = (NewInitializeBean) result;
                        boolean isUpgrade = UpgradeUtils
                                .onUpdate(YanXiuConstant.VERSION,
                                        initializeBean.getVersion());
                        if (isUpgrade) {
                            UpgradeUtils.showUpdateDialogWin(activity,
                                    initializeBean,
                                    new UpgradeUtils.OnUpgradeCallBack() {
                                        @Override
                                        public void onExit() {
                                            ActivityManager.destory();
                                            android.os.Process.killProcess(android.os.Process.myPid());
                                        }

                                        @Override
                                        public void onDownloadApk(
                                                boolean isSuccess) {
                                            LogInfo.log("king",
                                                    "onDownloadApk isSuccess = "
                                                            + isSuccess);
                                        }

                                        @Override
                                        public void onInstallApk(
                                                boolean isSuccess) {
                                            LogInfo.log("king",
                                                    "onInstallApk isSuccess = "
                                                            + isSuccess);
                                        }
                                    });
                        } else {
                            if (fromUser) {
                                Util.showToast(R.string.update_new);
                            }
                        }
                        updateTask = null;
                        LogInfo.log("king", initializeBean.toString());
                    }
            }

            @Override
            public void dataError(int type, String msg) {
                if (fromUser) {
                    if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        Util.showToast(R.string.net_null);
                    } else {
                        Util.showToast(R.string.server_exception);
                    }
                }
                updateTask=null;
            }
        });
        updateTask.start();
    }
}
