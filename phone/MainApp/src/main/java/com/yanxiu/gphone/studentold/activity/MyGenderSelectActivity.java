package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/10.
 */
public class MyGenderSelectActivity extends YanxiuBaseActivity implements View.OnClickListener{
    public final static int GENDER_REQUEST_CODE = 0x101;

    private PublicLoadLayout rootView;
    private View top_layout, male_layout, female_layout;
    private TextView topTitle, maleTxt, femaleTxt;
    private ImageView maleLeftIcon,maleIv,femaleRightIcon, femaleIv;
    private View backView;
    private int type;
    public static void launch(Activity activity, int type){
        Intent intent = new Intent(activity, MyGenderSelectActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, GENDER_REQUEST_CODE);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_gender_select_my_layout);
        setContentView(rootView);
        type = getIntent().getIntExtra("type",  YanXiuConstant.Gender.GENDER_TYPE_MALE);
        findView();
    }

    private void findView(){
        top_layout = findViewById(R.id.top_layout);
        backView = top_layout.findViewById(R.id.pub_top_left);
        topTitle = (TextView) top_layout.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.user_gender_str);
        backView.setOnClickListener(this);
        male_layout = findViewById(R.id.male_layout);
        female_layout = findViewById(R.id.female_layout);

        male_layout.setOnClickListener(this);
        female_layout.setOnClickListener(this);

        maleTxt = (TextView) male_layout.findViewById(R.id.name);
        femaleTxt = (TextView) female_layout.findViewById(R.id.name);

        maleTxt.setText(R.string.male_txt);
        femaleTxt.setText(R.string.female_txt);

        maleIv = (ImageView) male_layout.findViewById(R.id.right_arrow);

        maleLeftIcon=(ImageView)male_layout.findViewById(R.id.left_icon);
        maleLeftIcon.setImageResource(R.drawable.male_icon);
        femaleIv = (ImageView) female_layout.findViewById(R.id.right_arrow);

        femaleRightIcon=(ImageView)female_layout.findViewById(R.id.left_icon);
        femaleRightIcon.setImageResource(R.drawable.female_icon);
        if(type ==YanXiuConstant.Gender. GENDER_TYPE_MALE){
            maleIv.setVisibility(View.VISIBLE);
            femaleIv.setVisibility(View.GONE);
        } else if(type == YanXiuConstant.Gender. GENDER_TYPE_FEMALE){
            femaleIv.setVisibility(View.VISIBLE);
            maleIv.setVisibility(View.GONE);
        }
    }
    private void setSelectedView(View view, int type){
        maleIv.setVisibility(View.GONE);
        femaleIv.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        updateInfo(type);

    }
    private void updateInfo(final int type){
        rootView.loading(true);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if(type == YanXiuConstant.Gender. GENDER_TYPE_MALE){
            hashMap.put("sex", String.valueOf(YanXiuConstant.Gender. GENDER_TYPE_MALE));
        } else if(type == YanXiuConstant.Gender. GENDER_TYPE_FEMALE){
            hashMap.put("sex", String.valueOf(YanXiuConstant.Gender. GENDER_TYPE_FEMALE));
        } else {
            hashMap.put("sex", String.valueOf(YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN));
        }
        new RequestUpdateUserInfoTask(this, hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                if(type == YanXiuConstant.Gender.GENDER_TYPE_MALE){
                    LoginModel.getUserinfoEntity().setSex( String.valueOf(YanXiuConstant.Gender. GENDER_TYPE_MALE));
                } else if(type == YanXiuConstant.Gender.GENDER_TYPE_FEMALE){
                    LoginModel.getUserinfoEntity().setSex(String.valueOf(YanXiuConstant.Gender. GENDER_TYPE_FEMALE));
                } else {
                    LoginModel.getUserinfoEntity().setSex(String.valueOf(YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN));
                }
                LoginModel.savaCacheData();
                Intent intent = new Intent();
                intent.putExtra("type", type);
                setResult(RESULT_OK, intent);
                finish();
            }
            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (!TextUtils.isEmpty(msg)) {
                    Util.showToast(msg);
                } else {
                     Util.showToast(R.string.data_uploader_failed);
                }
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        if(v == male_layout){
            setSelectedView(maleIv,YanXiuConstant.Gender.  GENDER_TYPE_MALE);
        } else if(v == female_layout){
            setSelectedView(femaleIv, YanXiuConstant.Gender. GENDER_TYPE_FEMALE);
        }else if(v == backView){
            finish();
        }
    }
}
