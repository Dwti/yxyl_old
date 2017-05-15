package com.yanxiu.gphone.studentold.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.igexin.sdk.PushConsts;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.bean.PushMsgBean;

import java.util.Random;

/**
 * Created by JS-00 on 2016/9/27.
 */

public class YanxiuPushReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_FLAG = 1;
    private Random mRandom = new Random();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传（payload）数据
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null)
                {
                    String data = new String(payload);
                    Log.d("GetuiSdkDemo", "Got Payload:" + data);
                    onTextMessage(context, data);
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                String cid = bundle.getString("clientid");
                Log.d("GetuiSdkDemo", "Got ClientID:" + cid);
                /* 第三方应用需要将ClientID上传到第三方服务器，并且将当前用户帐号和ClientID进行关联，
                以便以后通过用户帐号查找ClientID进行消息推送。有些情况下ClientID可能会发生变化，为保证获取最新的ClientID，
                请应用程序在每次获取ClientID广播后，都能进行一次关联绑定 */
                break;
            /*case PushConsts.BIND_CELL_STATUS:
                String cell = bundle.getString("cell");
                Log.d("GetuiSdkDemo", "BIND_CELL_STATUS:" + cell);
                if (GexinSdkDemoActivity.tLogView != null)
                    GexinSdkDemoActivity.tLogView.append("BIND_CELL_STATUS:" + cell + "\n");
                break;*/
            default:
                break;
        }

    }

    public void onTextMessage(Context context, String content) {
        if(LoginModel.getLoginBean() == null || YanxiuApplication.getInstance().isForceUpdate()){
            LogInfo.log("haitian", "-----------isForceUpdate------LoginBeanIsNull-----------" );
            return;
        }
//        String text = "收到消息:" + message.toString();
        PushMsgBean mPushMsgBean = null;
        try {
            mPushMsgBean = JSON.parseObject(content, PushMsgBean.class);
            LogInfo.log("haitian", "-----------onTextMessage-----------------bean.toString=" +
                    mPushMsgBean.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
        if(mPushMsgBean == null){
//            mPushMsgBean=new PushMsgBean();
//            mPushMsgBean.setId(1);
//            mPushMsgBean.setMsg_title("asd");
//            mPushMsgBean.setMsg_type(2);
//            mPushMsgBean.setName("ss");
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
        contentView.setImageViewResource(R.id.notifi_icon, R.mipmap.app_icon);
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
            actNotification.tickerText = content;
            actNotification.flags = Notification.FLAG_AUTO_CANCEL;
            actNotification.defaults |= Notification.DEFAULT_SOUND;
            actNotification.contentView = contentView;
            //actNotification.setLatestEventInfo(context, appName,
            //message.getTitle(), actPendingIntent);
        }
        notificationManager.notify(requestCode, actNotification);
    }


}
