package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;


/**
 * Created by Administrator on 2015/7/9.
 * 2.3 获得教材版本信息：
 */
public class SubjectVersionActivity extends YanxiuBaseActivity implements View.OnClickListener {

    public final static int LAUNCHER_SUBJECT_VERSION_ACTIVITY = 0x11;


    public static void launch (Activity context, String title, int stageId, String subjectId) {
        Intent intent = new Intent(context, SubjectVersionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("stageId", stageId);
        intent.putExtra("subjectId", subjectId);
        context.startActivityForResult(intent, LAUNCHER_SUBJECT_VERSION_ACTIVITY);
    }

    public static void launch (Activity context, String title, int stageId, String subjectId, String editionId) {
        Intent intent = new Intent(context, SubjectVersionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("stageId", stageId);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("editionId", editionId);
        context.startActivityForResult(intent, LAUNCHER_SUBJECT_VERSION_ACTIVITY);
    }

    private TextView tvTopTitle;

    @Override
    public void onClick (View v) {

    }
}
