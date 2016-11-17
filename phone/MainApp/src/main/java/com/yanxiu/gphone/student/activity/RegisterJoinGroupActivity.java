package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ClassInfoBean;
import com.yanxiu.gphone.student.bean.WXUserInfoBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestClassInfoTask;
import com.yanxiu.gphone.student.utils.DoubleKillUtils;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.passwordview.GridPasswordView;

/**
 * Created by Administrator on 2016/10/31.
 */

public class RegisterJoinGroupActivity extends YanxiuBaseActivity implements View.OnClickListener, GridPasswordView.OnTextChangedListener {

    private GridPasswordView groupNumView;
    private PublicLoadLayout rootView;
    private RequestClassInfoTask requestClassInfoTask;
    private ClassInfoBean classInfoBean;
    private TextView group_bottom_submit;
    private DoubleKillUtils killUtils=DoubleKillUtils.getInstence();

    private String openid;
    private String uniqid;
    private String platform;
    private WXUserInfoBean wxUserInfoBean;

    public static void launchActivity(Activity context){
        Intent intent = new Intent(context,RegisterJoinGroupActivity.class);
        context.startActivity(intent);
    }

    public static void launchActivity(Activity context, String openid, String uniqid, String platform, WXUserInfoBean wxUserInfoBean) {
        Intent intent = new Intent(context, RegisterJoinGroupActivity.class);
        intent.putExtra("openid", openid);
        intent.putExtra("uniqid", uniqid);
        intent.putExtra("platform", platform);
        intent.putExtra("wxUserInfoBean", wxUserInfoBean);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_register_joingroup);
        setContentView(rootView);
        Intent intent = getIntent();
        if (intent != null) {
            openid = intent.getStringExtra("openid");
            uniqid = intent.getStringExtra("uniqid");
            platform = intent.getStringExtra("platform");
            if ("weixin".equals(platform)) {
                wxUserInfoBean = (WXUserInfoBean) intent.getSerializableExtra("wxUserInfoBean");
            }
        }
        initview();
    }

    private void initview() {
        killUtils.setTime(500);
        groupNumView = (GridPasswordView)findViewById(R.id.group_edit_number);
        groupNumView.setVisibility(View.VISIBLE);
        groupNumView.setBackgroundResource(R.drawable.group_input_bg);
        groupNumView.setPasswordVisibility(true);
        groupNumView.setTextChangedListener(this);
        TextView group_add_tips= (TextView) findViewById(R.id.group_add_tips);
        group_add_tips.setOnClickListener(this);
        group_bottom_submit= (TextView) findViewById(R.id.group_bottom_submit);
        group_bottom_submit.setOnClickListener(this);
        group_bottom_submit.setTextColor(getResources().getColor(R.color.color_e4b62e));
        TextView pub_top_left= (TextView) findViewById(R.id.pub_top_left);
        pub_top_left.setOnClickListener(this);
        YanxiuTypefaceTextView pub_top_mid= (YanxiuTypefaceTextView) findViewById(R.id.pub_top_mid);
        pub_top_mid.setText(R.string.class_add);
        TextView pub_top_right= (TextView) findViewById(R.id.pub_top_right);
        pub_top_right.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_add_tips:
                if (killUtils.getFlag()) {
                    UserInfoActivity.launchActivity(RegisterJoinGroupActivity.this, openid, uniqid, platform, wxUserInfoBean);
                }
                break;
            case R.id.group_bottom_submit:
                String groupNum=groupNumView.getPassWord();
                if (groupNum.length()<8){
//                       Util.showUserToast(R.string.group_num_no_right, -1, -1);
                    return;
                }
                CommonCoreUtil.hideSoftInput(groupNumView);
                JoinGroup(groupNum);
                break;
            case R.id.pub_top_left:
                this.finish();
                break;
        }
    }

    private void JoinGroup(String groupNum) {
        rootView.loading(true);
        try {
            requestClassInfoTask = new RequestClassInfoTask(this, groupNum, new AsyncCallBack() {
                @Override public void update(YanxiuBaseBean result) {
                    rootView.finish();
                    if(result != null){
                        classInfoBean = (ClassInfoBean)result;
                        if(classInfoBean.getStatus().getCode() == YanXiuConstant.SERVER_0){
                            if (classInfoBean.getData().size() > 0 && classInfoBean.getData().get(0).getStatus() == YanXiuConstant.SERVER_2) {
                                //Util.showUserToast(R.string.group_add_no_right, -1, -1);
                                //该班级不允许加入
                                Util.showCustomTipToast(R.string.group_add_no_right);
                            } else {
//                                CommonCoreUtil.hideSoftInput(groupNumView);
                                if (TextUtils.isEmpty(platform)){
                                    RegisterAddGroupActivity.launchActivity(RegisterJoinGroupActivity.this,0, classInfoBean);
                                }else {
                                    RegisterAddGroupActivity.launchActivity(RegisterJoinGroupActivity.this,0, classInfoBean,openid,uniqid,platform,wxUserInfoBean);
                                }
                            }
                        }else{
                            Util.showUserToast(classInfoBean.getStatus().getDesc(), null, null);
                        }
                    }
                }

                @Override public void dataError(int type, String msg) {
                    rootView.finish();
                    if(!StringUtils.isEmpty(msg)){
                        Util.showUserToast(msg, null, null);
                    } else{
                        Util.showUserToast(R.string.class_number_error, -1, -1);
                    }
                }
            });
            requestClassInfoTask.start();
        }catch (Exception e){
            rootView.finish();
            LogInfo.log("填写的群组号码为非数字");
        }
    }

    @Override
    public void onTextNumberChanged(int number) {
        if (number==8){
            group_bottom_submit.setTextColor(getResources().getColor(R.color.color_805500));
        }else {
            group_bottom_submit.setTextColor(getResources().getColor(R.color.color_e4b62e));
        }
    }
}
