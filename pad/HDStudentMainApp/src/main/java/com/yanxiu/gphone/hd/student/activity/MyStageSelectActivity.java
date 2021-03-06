package com.yanxiu.gphone.hd.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/10.
 */
public class MyStageSelectActivity extends YanxiuBaseActivity implements View.OnClickListener {
    public final static int STAGE_REQUEST_CODE = 0x101;
    public final static int STAGE_TYPE_PRIM = YanXiuConstant.STAGE.PRIM;
    public final static int STAGE_TYPE_JUIN = YanXiuConstant.STAGE.JUIN;
    public final static int STAGE_TYPE_HIGH = YanXiuConstant.STAGE.HIGH;
    private final static int STAGE_CONSTANT_NUM = 3;
    private PublicLoadLayout mRootView;
    private View top_layout;
    private TextView topTitle;

    private View[] stageLayout = new View[STAGE_CONSTANT_NUM];
    private TextView[] stageTxt = new TextView[STAGE_CONSTANT_NUM];
    private ImageView[] stageTv = new ImageView[STAGE_CONSTANT_NUM];
    private int[] stageTxtId = {R.string.primary_txt,R.string.juinor_txt,R.string.high_txt};

    private View backView;
    private int type;
    private int registerType;

    public static void launch(Activity activity, int type) {
        Intent intent = new Intent(activity, MyStageSelectActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, STAGE_REQUEST_CODE);
    }

    public static void launch(Activity activity, int type,int registerType) {
        Intent intent = new Intent(activity, MyStageSelectActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("registerType", registerType);
        activity.startActivityForResult(intent, STAGE_REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_select_stage_my_layout);
        setContentView(mRootView);
        type = getIntent().getIntExtra("type", 0);
        registerType = getIntent().getIntExtra("registerType", 0);
        findView();
    }

    private void findView() {
        top_layout = findViewById(R.id.top_layout);
        backView = top_layout.findViewById(R.id.pub_top_left);
        topTitle = (TextView) top_layout.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.select_subject_txt);
        backView.setOnClickListener(this);

        stageLayout[0] = findViewById(R.id.primary_stage_layout);
        stageLayout[1] = findViewById(R.id.juinor_stage_layout);
        stageLayout[2] = findViewById(R.id.high_stage_layout);

        for(int i=0; i< STAGE_CONSTANT_NUM; i++){
            stageLayout[i].setOnClickListener(this);
            stageTxt[i] = (TextView) stageLayout[i].findViewById(R.id.name);
            stageTxt[i].setText(stageTxtId[i]);
            stageTv[i] = (ImageView) stageLayout[i].findViewById(R.id.right_arrow);
            stageTv[i].setVisibility(View.GONE);
            stageLayout[i].findViewById(R.id.left_icon).setVisibility(View.GONE);
        }
        setSelectStageDisp(type);
    }
    private void setSelectStageDisp(int type){
        int index = 0;
        if (type == STAGE_TYPE_PRIM) {
            index = 0;
        } else if (type == STAGE_TYPE_JUIN) {
            index = 1;
        } else if (type == STAGE_TYPE_HIGH) {
            index = 2;
        }
        for(int i=0; i< STAGE_CONSTANT_NUM; i++) {
            if(i == index) {
                stageTv[i].setVisibility(View.VISIBLE);
            } else {
                stageTv[i].setVisibility(View.GONE);
            }
        }
    }
    private void setSelectedView(int type) {
        setSelectStageDisp(type);
        upLoadStageId(type);
    }
    private void forResult(int type) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        LogInfo.log("haitian", "type =" + type);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void  upLoadStageId(final int stageId) {
        if(registerType == UserInfoActivity.LAUNCHER_FROM_USERINFO_TO_STAGE){
            forResult(stageId);
        }else{
            mRootView.loading(true);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("stageid", stageId+"");
            new RequestUpdateUserInfoTask(this, hashMap, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    mRootView.finish();
                    LogInfo.log("haitian", "type update");
                    LoginModel.getUserinfoEntity().setStageid(stageId);
                    String myStage = null;
                    if(stageId == STAGE_TYPE_PRIM){
                        myStage = getResources().getString(R.string.primary_txt);
                    } else if(stageId == STAGE_TYPE_JUIN){
                        myStage = getResources().getString(R.string.juinor_txt);
                    } else if(stageId == STAGE_TYPE_HIGH){
                        myStage = getResources().getString(R.string.high_txt);
                    }
                    LoginModel.getUserinfoEntity().setStageName(myStage);
                    LoginModel.savaCacheData();
                    PreferencesManager.getInstance().clearSubjectSection();
                    ActivityManager.destoryAllActivityExceptStageActivity();
                    MainActivity.launchActivity(MyStageSelectActivity.this);
                    finish();
//                    forResult(stageId);
                }
                @Override
                public void dataError(int type, String msg) {
                    mRootView.finish();
                    LogInfo.log("haitian", "type ="+type+" msg="+msg);
                    if (!TextUtils.isEmpty(msg)) {
                        Util.showToast(msg);
                    } else {
                        Util.showToast(R.string.data_uploader_failed);
                    }
                }
            }).start();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == stageLayout[0]) {
            setSelectedView(STAGE_TYPE_PRIM);
        } else if (v == stageLayout[1]) {
            setSelectedView(STAGE_TYPE_JUIN);
        } else if (v == stageLayout[2]) {
            setSelectedView(STAGE_TYPE_HIGH);
        } else if (v == backView) {
            finish();
        }
    }
}
