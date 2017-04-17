package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.view.MyBoldTextView;

/**
 * Created by sunpeng on 2017/4/17.
 */

public class ModifyMobileActivity extends Activity {
    private TextView tv_mobile;
    private View modify_mobile_item;
    private String mMobile;
    public static final String MOBILE_NUM = "mobileNum";
    private View backView;
    private MyBoldTextView tv_title;

    public static void launch(Activity activity,String mobileNum){
        Intent intent = new Intent(activity,ModifyMobileActivity.class);
        intent.putExtra(MOBILE_NUM,mobileNum);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_mobile);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        backView = findViewById(R.id.pub_top_left);
        tv_title = (MyBoldTextView) findViewById(R.id.pub_top_mid);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        modify_mobile_item = findViewById(R.id.setting_modify_bind_mobile_layout);

        tv_title.setText(R.string.bind_mobile);
    }

    private void initData() {
        mMobile = getIntent()==null?"":getIntent().getStringExtra(MOBILE_NUM);
        if(mMobile.length() == 11){
            String str = mMobile.substring(0,3)+"*****"+mMobile.substring(7,11);
            tv_mobile.setText(str);
        }
    }

    private void initListener() {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        modify_mobile_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyMobileActivity.launch(ModifyMobileActivity.this,mMobile);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case VerifyMobileActivity.REQUEST_VERIFY_MOBILE:
                if(resultCode == RESULT_OK){
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
