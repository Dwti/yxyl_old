package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by Administrator on 2015/7/14.
 */
public class SettingFeedBackActivity extends YanxiuBaseActivity implements View.OnClickListener{
    private View topView, backView;
    private TextView topTitle;
    private TextView submitView;
    private EditText feedBackEt;

    public static void launchActivity(Activity context) {
        Intent intent = new Intent(context, SettingFeedBackActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_setting_layout);
        findView();
    }
    private void findView(){
        topView = findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        submitView = (TextView) topView.findViewById(R.id.pub_top_right);
        submitView.setText(R.string.send_feedback_txt);
        topTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.feedback_txt);
        feedBackEt = (EditText) findViewById(R.id.feedback_edit_view);

        backView.setOnClickListener(this);
        submitView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonCoreUtil.hideSoftInput(feedBackEt);
    }

    @Override
    public void onClick(View v) {
        if(v == backView){
            finish();
        } else if(v == submitView){
            String feedback = feedBackEt.getEditableText().toString();
            if(TextUtils.isEmpty(feedback)){
                Util.showToast(R.string.feedback_is_null);
                return;
            } else {
                Util.showToast(R.string.feedback_submit_succeed);
                finish();
            }
        }
    }
}
