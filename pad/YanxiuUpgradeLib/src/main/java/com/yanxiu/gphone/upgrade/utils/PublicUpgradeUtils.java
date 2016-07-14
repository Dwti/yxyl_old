package com.yanxiu.gphone.upgrade.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.common.core.manage.CommonActivityManager;
import com.common.core.utils.ContextProvider;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.http.HttpTaskManager;
import com.yanxiu.gphone.upgrade.R;
import com.yanxiu.gphone.upgrade.bean.InitializeBean;
import com.yanxiu.gphone.upgrade.bean.UpdateDelShareIconsBean;
import com.yanxiu.gphone.upgrade.request.AsyncCallBack;
import com.yanxiu.gphone.upgrade.request.InitializeTask;
import com.yanxiu.gphone.upgrade.view.UpgradeDialog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import de.greenrobot.event.EventBus;

/**
 * Created by king on 2015/5/14.
 */
public class PublicUpgradeUtils {
    private static PublicUpgradeUtils instance;

    private UpgradeDialog mDialogWindow;

    private Context mContext;

    private OnUpgradeCallBack mOnUpgradeCallBack;

    public final int NOTIFICATION_ID = 0x11;

    public NotificationManager notificationManager;

    public Notification notification;

    private InitializeTask updateTask;

    private int dialogLayout = -1;

    private PublicUpgradeUtils () {
    }

    public static PublicUpgradeUtils getInstance () {
        if (instance == null) {
            instance = new PublicUpgradeUtils();
        }
        return instance;
    }

    /**
     * @param nowVersion
     * @param serverVersion
     */
    public boolean onUpdate (String nowVersion, String serverVersion) {
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
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showUpdateDialogWin (
            Context context, final InitializeBean initializeBean,
            OnUpgradeCallBack onUpgradeCallBack) {
        mContext = context;
        if (onUpgradeCallBack != null) {
            mOnUpgradeCallBack = onUpgradeCallBack;
        }
        if (mDialogWindow != null) {
            mDialogWindow.dismiss();
            mDialogWindow = null;
        }
        mDialogWindow = new UpgradeDialog(mContext, initializeBean, dialogLayout, new UpgradeDialog
                .UpgradeDialogCallBack() {
            @Override
            public void upgrade () {
                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notification = new Notification();
                downloadApk(initializeBean.getFileURL());
            }

            @Override
            public void cancel () {
            }

            @Override
            public void exit () {
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
    public void downloadApk (String path) {
//        path = "http://down.m.letv.com/android/static/apk/2015/0605/LetvClient_V5.9_From_102.apk";
        HttpTaskManager.getInstance().submit(new Loader(path, Loader.STUDENT_UPLOAD, mLoaderListener));
    }

    /**
     * @param filePath apk file path
     * @return true
     * install success
     * false
     * install false
     */
    public void installApk (String filePath) {
        LogInfo.log("king", "filePath == " + filePath);
        //更新版本同时删除旧的分享ICON文件，如果有更新会在欢迎界面获取最新的图片并保存
//        FileUtils.deleteShareBitmap(UpgradeConstant.SHARE_ICON_PATH + UpgradeConstant.SHARE_LOGO_NAME);
        EventBus.getDefault().post(new UpdateDelShareIconsBean());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    public boolean uninstall (String packageName) {
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
        void onExit ();

        void onDownloadApk (boolean isSuccess);

        void onInstallApk (boolean isSuccess);
    }

    private Loader.LoaderListener mLoaderListener = new Loader.LoaderListener() {

        @Override
        public void onStart () {
            LogInfo.log("king", "onStart ");
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            notification.icon = R.anim.public_notification_download;
            notification.tickerText = mContext.getResources().getString(
                    R.string.public_update_asynctask_downloading);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.public_notification_updata_layout);
            if(TextUtils.isEmpty(UpgradeConstant.getAppName())) {
                remoteViews.setTextViewText(R.id.app_name, mContext.getResources().getString(
                        R.string.public_app_name));
            } else {
                remoteViews.setTextViewText(R.id.app_name, UpgradeConstant
                        .getAppName());
            }
            remoteViews.setTextViewText(R.id.progress_text, "0%");
            remoteViews.setProgressBar(R.id.progress_value, 100, 0, false);
            notification.contentView = remoteViews;
            notification.contentIntent = pendingIntent;
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        @Override
        public void onError (String message, int code) {
            LogInfo.log("king",
                    "onError code = " + code + " ,message = " + message);
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.upgrade_notification01;
            notification.tickerText = mContext.getResources().getString(R.string.public_update_asynctask_downloading_fail);
            if (mOnUpgradeCallBack != null) {
                mOnUpgradeCallBack.onDownloadApk(false);
            }
        }

        @Override
        public void onFailure (String message, int code) {
            LogInfo.log("king", "onFailure code = " + code + " ,message = " + message);
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.upgrade_notification01;
            notification.tickerText = mContext.getResources().getString(R.string.public_update_asynctask_downloading_fail);
            if (mOnUpgradeCallBack != null) {
                mOnUpgradeCallBack.onDownloadApk(false);
            }
        }

        @Override
        public void onComplete (String path) {
            LogInfo.log("king", "onComplete path = " + path);
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.upgrade_notification01;
            notification.tickerText = mContext.getResources().getString(R.string.public_update_asynctask_downloading_success);
            notification.contentView.setProgressBar(R.id.progress_value, 100,
                    100, false);
            notification.contentView.setViewVisibility(R.id.progress_value,
                    View.GONE);
            if (mOnUpgradeCallBack != null) {
                installApk(path);
            }
        }

        @Override
        public void onProgress (double progress) {
            LogInfo.log("king", "onProgress progress = " + progress);
            notification.contentView.setProgressBar(R.id.progress_value, 100, (int) progress, false);
            notification.contentView.setTextViewText(R.id.progress_text, (int) progress + "%");
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        @Override
        public void onCancel () {
            LogInfo.log("king", "onCancel");
            notificationManager.cancel(NOTIFICATION_ID);
            notification.icon = R.drawable.upgrade_notification01;
            notification.tickerText = mContext.getResources().getString(R.string.public_update_asynctask_downloading_cel);
        }

        @Override
        public void onUnAvailable () {

        }
    };


    public void cancelUpgrade () {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
    }

    public void requestInitialize (final boolean fromUser, final Activity activity, String token,
                                   String mobile, int dialogLayout){
        this.dialogLayout = dialogLayout;
        requestInitialize(fromUser, activity, token, mobile);
    }
    /**
     * 初始化接口
     */
    public void requestInitialize (final boolean fromUser, final Activity activity, String token, String mobile) {
        cancelUpgrade();
        updateTask = new InitializeTask(activity, "", token, mobile, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                if (result != null) {
                    InitializeBean initializeBean = (InitializeBean) result;
                    boolean isUpgrade = onUpdate(UpgradeConstant.VERSION, initializeBean.getVersion());
                    if (isUpgrade) {
                        showUpdateDialogWin(activity,
                                initializeBean,
                                new PublicUpgradeUtils.OnUpgradeCallBack() {
                                    @Override
                                    public void onExit () {
                                        CommonActivityManager.destory();
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }

                                    @Override
                                    public void onDownloadApk (
                                            boolean isSuccess) {
                                        LogInfo.log("king",
                                                "onDownloadApk isSuccess = "
                                                        + isSuccess);
                                    }

                                    @Override
                                    public void onInstallApk (
                                            boolean isSuccess) {
                                        LogInfo.log("king",
                                                "onInstallApk isSuccess = "
                                                        + isSuccess);
                                    }
                                });
                    } else {
                        if (fromUser) {
                            showToast(R.string.public_update_new);
                        }
                    }
                    updateTask = null;
                    LogInfo.log("king", initializeBean.toString());
                }
            }

            @Override
            public void dataError (int type, String msg) {
                if (fromUser) {
                    if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        showToast(R.string.public_net_null);
                    } else {
                        showToast(R.string.public_server_exception);
                    }
                }
                updateTask = null;
            }
        });
        updateTask.start();
    }

    private Toast mToast = null;

    private void showToast (String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        if (!TextUtils.isEmpty(text)) {
            mToast = Toast.makeText(ContextProvider.getApplicationContext(), text, Toast.LENGTH_SHORT);
            mToast.setText(text);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    private void showToast (int txtId) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(ContextProvider.getApplicationContext(), txtId, Toast.LENGTH_SHORT);
        mToast.setText(txtId);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}
