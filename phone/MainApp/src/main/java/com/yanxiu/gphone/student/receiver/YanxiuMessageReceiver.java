package com.yanxiu.gphone.student.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.bean.PushMsgBean;

import java.util.Random;

public class YanxiuMessageReceiver extends XGPushBaseReceiver {
    private Intent intent = new Intent(YanxiuPushUpdateReceiver.PUSH_RECEIVER_INTENT_ACTION);
    public static final String LogTag = "TPushReceiver";
    private Random mRandom = new Random();
    private void show(Context context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        LogInfo.log("haitian", "-----------onNotifactionShowedResult-----------------");
//		Intent actIntent = new Intent(context, YanxiuPushUpdateReceiver.class);
//		PendingIntent actPendingIntent = PendingIntent.getBroadcast(context, 7894567, actIntent,
//				PendingIntent.FLAG_UPDATE_CURRENT);
//		NotificationManager notificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		Notification actNotification = new Notification();
//		actNotification.icon = R.mipmap.ic_launcher;
//		actNotification.tickerText = notifiShowedRlt.getContent();
//		actNotification.flags = Notification.FLAG_AUTO_CANCEL;
//		actNotification.defaults |= Notification.DEFAULT_SOUND;
//		actNotification.setLatestEventInfo(context, context.getResources().getString(R.string.app_name),
//				notifiShowedRlt.getTitle(), actPendingIntent);
//		notificationManager.notify(7894567, actNotification);
//		context.sendBroadcast(intent);
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        LogInfo.log("haitian", "-----------onUnregisterResult-----------------");
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        LogInfo.log(LogTag, text);
        show(context, text);

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        LogInfo.log("haitian", "-----------onSetTagResult-----------------");
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        LogInfo.log(LogTag, text);
        show(context, text);

    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        LogInfo.log("haitian", "-----------onDeleteTagResult-----------------");
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        LogInfo.log(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        LogInfo.log("haitian", "-----------onNotifactionClickedResult-----------------");
        if (context == null || message == null) {
            return;
        }
//        String text = "";
//        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
//            // 通知在通知栏被点击啦。。。。。
//            // APP自己处理点击的相关动作
//            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
//            text = "通知被打开 :" + message;
//        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
//            // 通知被清除啦。。。。
//            // APP自己处理通知被清除后的相关动作
//            text = "通知被清除 :" + message;
//        }
//		Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
//				Toast.LENGTH_SHORT).show();
        // 获取自定义key-value
//        String customContent = message.getCustomContent();
//        if (customContent != null && customContent.length() != 0) {
//            try {
//                JSONObject obj = new JSONObject(customContent);
//                // key1为前台配置的key
//                if (!obj.isNull("key")) {
//                    String value = obj.getString("key");
//                    LogInfo.log(LogTag, "get custom value:" + value);
//                }
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        // APP自主处理的过程。。。
//        LogInfo.log(LogTag, text);

        if(LoginModel.getLoginBean() == null || YanxiuApplication.getInstance().isForceUpdate()){
            LogInfo.log("haitian", "-----------isForceUpdate------LoginBeanIsNull-----------" );
            return;
        }
//        String text = "收到消息:" + message.toString();
        String content = message.getCustomContent();
        PushMsgBean mPushMsgBean = null;
        try {
            mPushMsgBean = JSON.parseObject(content, PushMsgBean.class);

        } catch (Exception e){
            e.printStackTrace();
        }
        if(mPushMsgBean == null){
            return;
        }
        Intent actIntent = new Intent(context, YanxiuPushUpdateReceiver.class);
        actIntent.putExtra("mPushMsgBean", mPushMsgBean);
        context.sendBroadcast(actIntent);
    }

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        LogInfo.log("haitian", "-----------onRegisterResult-----------------");
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
            LogInfo.log(LogTag, "account = " + message.getAccount());
            LogInfo.log(LogTag, "token = " + token);
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
        LogInfo.log(LogTag, text);
        show(context, text);
    }

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        if(LoginModel.getLoginBean() == null || YanxiuApplication.getInstance().isForceUpdate()){
            LogInfo.log("haitian", "-----------isForceUpdate------LoginBeanIsNull-----------" );
            return;
        }
//        String text = "收到消息:" + message.toString();
        String content = message.getContent();
        PushMsgBean mPushMsgBean = null;
        try {
            mPushMsgBean = JSON.parseObject(content, PushMsgBean.class);
            LogInfo.log("haitian", "-----------onTextMessage-----------------bean.toString=" +
                    mPushMsgBean.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
        if(mPushMsgBean == null){
            return;
        }
        LogInfo.log("haitian", "-----------onTextMessage-----------------content=" + content);

//        // 获取自定义key-value
//        String customContent = message.getCustomContent();
//        if (customContent != null && customContent.length() != 0) {
//            try {
//                JSONObject obj = new JSONObject(customContent);
//                // key1为前台配置的key
//                if (!obj.isNull("key")) {
//                    String value = obj.getString("key");
//                    LogInfo.log(LogTag, "get custom value:" + value);
//                }
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        // APP自主处理消息的过程...
        String appName = context.getResources().getString(R.string.app_name);
        int requestCode = mRandom.nextInt(79865437);
        Intent actIntent = new Intent(context, YanxiuPushUpdateReceiver.class);
        actIntent.putExtra("mPushMsgBean", mPushMsgBean);
        PendingIntent actPendingIntent = PendingIntent.getBroadcast(context, requestCode, actIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification actNotification = null;
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.yanxiu_notification_layout);
        contentView.setImageViewResource(R.id.notifi_icon, R.mipmap.notifi_icon);
        contentView.setTextViewText(R.id.notifi_title, appName);
        contentView.setTextViewText(R.id.notifi_content, mPushMsgBean.getMsg_title());
        contentView.setTextViewText(R.id.notifi_time, CommonCoreUtil.getNowHMDate());
        if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 16) {
            Notification.Builder builder = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle(appName)
                    .setContentText("describe")
                    .setContentIntent(actPendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true);
            actNotification = builder.getNotification();
            actNotification.contentView = contentView;
        } else if (Build.VERSION.SDK_INT >= 16) {
            actNotification = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle(appName)
                    .setContentText("describe")
                    .setContentIntent(actPendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setWhen(System.currentTimeMillis())
                    .build();
            actNotification.contentView = contentView;
        } else {
            actNotification = new Notification();
            actNotification.icon = R.mipmap.app_icon;
            actNotification.tickerText = message.getContent();
            actNotification.flags = Notification.FLAG_AUTO_CANCEL;
            actNotification.defaults |= Notification.DEFAULT_SOUND;
            actNotification.contentView = contentView;
            //actNotification.setLatestEventInfo(context, appName,
                    //message.getTitle(), actPendingIntent);
        }
        notificationManager.notify(requestCode, actNotification);
    }

}
