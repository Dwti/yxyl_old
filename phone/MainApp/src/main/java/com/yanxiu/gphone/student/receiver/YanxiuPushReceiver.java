package com.yanxiu.gphone.student.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.igexin.sdk.PushConsts;
import com.yanxiu.gphone.student.R;

/**
 * Created by JS-00 on 2016/9/27.
 */

public class YanxiuPushReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_FLAG = 1;
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
                    // TODO:接收处理透传（payload）数据
                    showNotification(context, data);
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                String cid = bundle.getString("clientid");
                Log.d("GetuiSdkDemo", "Got ClientID:" + cid);
                // TODO:
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

    /**
     *
     * @param context   上下文
     * @param data  穿透过来的json数据
     */
    private void showNotification(Context context, String data) {
        // Notification myNotify = new Notification(R.drawable.message,
        // "自定义通知：您有新短信息了，请注意查收！", System.currentTimeMillis());
        Notification myNotify = new Notification();
        myNotify.icon = R.mipmap.ic_launcher;
        myNotify.tickerText = data;
        myNotify.when = System.currentTimeMillis();
        myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.yanxiu_notification_layout);
        rv.setTextViewText(R.id.notifi_title, data);
        rv.setTextViewText(R.id.notifi_content, data);
        myNotify.contentView = rv;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                intent, 0);
        myNotify.contentIntent = contentIntent;
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_FLAG, myNotify);

    }


}
