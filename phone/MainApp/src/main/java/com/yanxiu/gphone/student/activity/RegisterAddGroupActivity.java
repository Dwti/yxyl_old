package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ClassDetailBean;
import com.yanxiu.gphone.student.bean.ClassInfoBean;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.bean.WXUserInfoBean;
import com.yanxiu.gphone.student.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestAddClassTask;
import com.yanxiu.gphone.student.requestTask.RequestNewAddClassTask;
import com.yanxiu.gphone.student.requestTask.ThirdRequestNewAddClassTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/1.
 */

public class RegisterAddGroupActivity extends YanxiuBaseActivity implements View.OnClickListener, TextWatcher {

    private PublicLoadLayout rootView;
    private Object messageFromIntent;
    private ClassDetailBean detailBean;
    private EditText name;
    private RequestBean requestBean;
    private TextView group_info_click;

    private String openid;
    private String uniqid;
    private String platform;
    private WXUserInfoBean wxUserInfoBean;


    public static void launchActivity(Activity context, int type, ClassInfoBean classInfoBean) {
        Intent intent = new Intent(context, RegisterAddGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("classinfo", classInfoBean);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }


    public static void launchActivity(Activity context, int type, ClassInfoBean classInfoBean, String openid, String uniqid, String platform, WXUserInfoBean wxUserInfoBean) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("classinfo", classInfoBean);
        intent.putExtra("bundle", bundle);
        intent.putExtra("openid", openid);
        intent.putExtra("uniqid", uniqid);
        intent.putExtra("platform", platform);
        intent.putExtra("wxUserInfoBean", wxUserInfoBean);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_registeraddgroup);
        setContentView(rootView);
        getMessageFromIntent();
        initview();
    }

    private void initview() {
        TextView school_name = (TextView) findViewById(R.id.school_name);
        school_name.setText("学校：" + detailBean.getSchoolname());
        TextView class_name = (TextView) findViewById(R.id.class_name);
        class_name.setText("班级：" + detailBean.getName());
        TextView teacher_name = (TextView) findViewById(R.id.teacher_name);
        teacher_name.setText("老师：" + detailBean.getAdminName());
        name = (EditText) findViewById(R.id.name);
        name.addTextChangedListener(this);
        TextView pub_top_mid = (TextView) findViewById(R.id.pub_top_mid);
        pub_top_mid.setText(R.string.class_add);
        TextView pub_top_left = (TextView) findViewById(R.id.pub_top_left);
        pub_top_left.setOnClickListener(this);
        group_info_click = (TextView) findViewById(R.id.group_info_click);
        group_info_click.setOnClickListener(this);
        group_info_click.setTextColor(getResources().getColor(R.color.color_e4b62e));
    }

    public void getMessageFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        ClassInfoBean classInfoBean = (ClassInfoBean) bundle.getSerializable("classinfo");
        detailBean = classInfoBean.getData().get(0);

        if (intent != null) {
            openid = intent.getStringExtra("openid");
            uniqid = intent.getStringExtra("uniqid");
            platform = intent.getStringExtra("platform");
            if ("weixin".equals(platform)) {
                wxUserInfoBean = (WXUserInfoBean) intent.getSerializableExtra("wxUserInfoBean");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_info_click:
                String user_name = name.getText().toString().trim();
                if (TextUtils.isEmpty(user_name)) {
//                    Util.showToast("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(platform)) {
                    AddGroup(user_name);
                } else {
                    ThirdAddGroup(user_name);
                }
                break;
            case R.id.pub_top_left:
                this.finish();
                break;
        }
    }

    private void ThirdAddGroup(String user_name) {
        rootView.loading(true);
        int sex = 0;
        String headimg = "";
        if (wxUserInfoBean != null) {
            sex = wxUserInfoBean.getSex() == 1 ? 2 : 1;
            headimg = wxUserInfoBean.getHeadimgurl();
        }
        ThirdRequestNewAddClassTask thirdRequestNewAddClassTask = new ThirdRequestNewAddClassTask(this,
                detailBean.getId(), detailBean.getStageid(), detailBean.getAreaid(),
                detailBean.getCityid(), openid, user_name, detailBean.getSchoolid(), platform,
                detailBean.getSchoolname(), headimg, sex, detailBean.getProvinceid(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                UserInfoBean userInfoBean = (UserInfoBean) result;
                if (userInfoBean != null && userInfoBean.getStatus() != null) {
                    if (userInfoBean.getStatus().getCode() == 0) {
                        userInfoBean.setIsThridLogin(true);
                        MainActivity.LaunchActivityToIndex(RegisterAddGroupActivity.this, 1);
                        RegisterAddGroupActivity.this.finish();
                        registerStatistics();
                    } else {
                        if (!StringUtils.isEmpty(userInfoBean.getStatus().getDesc())) {
                            Util.showToast(userInfoBean.getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.login_fail);
                        }
                    }
                } else {
                    Util.showToast(R.string.login_fail);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    Util.showUserToast(R.string.net_null, -1, -1);
                } else {
                    Util.showUserToast(R.string.data_error, -1, -1);
                }

            }
        });
        thirdRequestNewAddClassTask.start();
    }

    private void AddGroup(String user_name) {
        rootView.loading(true);
        RequestNewAddClassTask newAddClassTask = new RequestNewAddClassTask(this, detailBean.getId(),
                detailBean.getStageid(), detailBean.getAreaid(), detailBean.getCityid(),
                LoginModel.getMobile(), user_name, detailBean.getSchoolid(), detailBean.getSchoolname(),
                detailBean.getProvinceid(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                UserInfoBean userInfoBean = (UserInfoBean) result;
                if (userInfoBean.getStatus().getCode() == 0) {
                    userInfoBean.setIsThridLogin(false);
                    MainActivity.LaunchActivityToIndex(RegisterAddGroupActivity.this, 1);
                    RegisterAddGroupActivity.this.finish();
                    registerStatistics();
                } else {
                    Util.showUserToast(userInfoBean.getStatus().getDesc(), null, null);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    Util.showUserToast(R.string.net_null, -1, -1);
                } else {
                    Util.showUserToast(R.string.data_error, -1, -1);
                }
            }
        });
        newAddClassTask.start();

//        RequestAddClassTask requestAddGroupTask = new RequestAddClassTask(this, detailBean.getId(), user_name, new AsyncCallBack() {
//            @Override public void update(YanxiuBaseBean result) {
//                rootView.finish();
//                if(result != null){
//                    CommonCoreUtil.hideSoftInput(name);
//                    requestBean = (RequestBean)result;
//                    if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_3){
//                        Util.showUserToast(R.string.group_add_success, -1, -1);
//                    }else if(requestBean.getStatus().getCode() == YanXiuConstant.SERVER_4){
//                        Util.showUserToast(R.string.group_add_needcheck_one, R.string.group_add_needcheck_two, -1);
//                       }else{
//                        Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
//                    }
////                    Intent intent = new Intent();
////                    setResult(RESULT_OK,intent);
////                    RegisterAddGroupActivity.this.finish();
//                }
//            }
//
//            @Override public void dataError(int type, String msg) {
//                rootView.finish();
//                if(!StringUtils.isEmpty(msg)){
//                    Util.showUserToast(msg, null, null);
//                } else{
//                    Util.showUserToast(R.string.net_null_one, -1, -1);
//                }
//            }
//        });
//        requestAddGroupTask.start();
    }

    //注册打点
    private void registerStatistics() {

        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_1");//1:注册成功
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> registeHashMap = new HashMap<>();
        registeHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(this, registeHashMap);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            group_info_click.setTextColor(getResources().getColor(R.color.color_805500));
        } else {
            group_info_click.setTextColor(getResources().getColor(R.color.color_e4b62e));
        }
    }
}