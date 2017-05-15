package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;

/**
 * Created by Administrator on 2015/6/17.
 */
public class UserAboutUsActivity extends YanxiuBaseActivity implements View.OnClickListener{
    private View backView;
    private View topView;
    private TextView title;
    private TextView versionTextView;
    private View privacyPolicyTxt;
    private RelativeLayout officialweixin,officialQQ,officialPhone;

    private TextView weixinText,phoneText,qqText;

    public static void launch(Activity activity){
        Intent intent = new Intent(activity, UserAboutUsActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_layout);
        officialweixin=(RelativeLayout)findViewById(R.id.official_weixin_layout);
        officialQQ=(RelativeLayout)findViewById(R.id.services_qq_layout);
        officialPhone=(RelativeLayout)findViewById(R.id.services_tel_num_layout);


        weixinText=(TextView)findViewById(R.id.official_weixin_content);
        qqText=(TextView)findViewById(R.id.services_qq_content_txt);
        phoneText=(TextView)findViewById(R.id.services_tel_num_content_txt);

        topView = findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        title = (TextView) topView.findViewById(R.id.pub_top_mid);
        title.setText(R.string.about_us_txt);
        privacyPolicyTxt = findViewById(R.id.privacy_policy_txt);
        versionTextView = (TextView) findViewById(R.id.yanxiu_version_desc);
        versionTextView.setText(String.format(getResources().getString(R.string.system__version_des), CommonCoreUtil.getClientVersionName(this)));

        backView.setOnClickListener(this);
        privacyPolicyTxt.setOnClickListener(this);
        officialQQ.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.copyToClipData(UserAboutUsActivity.this, qqText.getText().toString());
                return true;
            }
        });
        officialPhone.setOnClickListener(this);
        officialweixin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Util.copyToClipData(UserAboutUsActivity.this, weixinText.getText().toString());
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.privacy_policy_txt:
                WebViewActivity.launch(this,YanXiuConstant.PRIVACY_POLICY_URL);
            break;
            case R.id.pub_top_left:
                finish();
            break;
            case R.id.services_tel_num_layout:
                CommonCoreUtil.callPhone(UserAboutUsActivity.this, phoneText.getText().toString());
            break;
        }

    }
}
