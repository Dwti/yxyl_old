package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.DistrictModel;
import com.yanxiu.gphone.studentold.bean.ProvinceModel;
import com.yanxiu.gphone.studentold.bean.RegisiterDistrictModel;
import com.yanxiu.gphone.studentold.bean.School;
import com.yanxiu.gphone.studentold.bean.WXUserInfoBean;
import com.yanxiu.gphone.studentold.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestNewRegisterTask;
import com.yanxiu.gphone.studentold.requestTask.RequestRegisterTask;
import com.yanxiu.gphone.studentold.requestTask.RequestThirdRegisterTask;
import com.yanxiu.gphone.studentold.utils.DoubleKillUtils;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.XmlParserHandler;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.studentold.view.MyTextWatcher;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/6
 */
public class UserInfoActivity extends YanxiuBaseActivity implements View.OnClickListener {

    public static void launchActivity(Activity context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    public static void launchActivity(Activity context, String openid, String uniqid, String platform, WXUserInfoBean wxUserInfoBean) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("openid", openid);
        intent.putExtra("uniqid", uniqid);
        intent.putExtra("platform", platform);
        intent.putExtra("wxUserInfoBean", wxUserInfoBean);
        context.startActivity(intent);
    }

    public final static int LAUNCHER_FROM_USERINFO_TO_AREA = 0x01;
    public final static int LAUNCHER_FROM_USERINFO_TO_SCHOOL = 0x02;
    public final static int LAUNCHER_FROM_USERINFO_TO_STAGE = 0x03;

    private PublicLoadLayout rootView;

    private TextView backView;

    private TextView titleView;

    private EditText nameView;

    private EditText nickNameView;

    private TextView areaView;

    private TextView schoolView;

    private TextView stageView;

    private TextView nextView;

    private View delUserName, delNickName, delArea, delSchool, delStage;
    private ArrayList<ProvinceModel> provinceList;

    private RequestRegisterTask requestRegisterTask;

    private String provinceId = "";
    private String cityId = "";
    private String zipcode = "";

    private int stageId = 0;
    private String openid;
    private String uniqid;
    private String platform;
    private WXUserInfoBean wxUserInfoBean;
    //匹配非表情符号的正则表达式
    private final String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
    private Pattern pattern = Pattern.compile(reg);

    private boolean IsNameReady=false;
    private boolean IsZipcodeReady=false;
    private boolean IsSchoolReady=false;
    private boolean IsStageReady=false;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (IsNameReady&&IsSchoolReady&&IsStageReady&&IsZipcodeReady){
                nextView.setTextColor(getResources().getColor(R.color.color_805500));
            }else {
                nextView.setTextColor(getResources().getColor(R.color.color_e4b62e));
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.edit_message_layout);
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
        EventBus.getDefault().register(this);
        findView();
        initProvinceDatas();
    }

    private void findView() {
        backView = (TextView) findViewById(R.id.pub_top_left);
        titleView = (TextView) findViewById(R.id.pub_top_mid);
        nameView = (EditText) findViewById(R.id.edit_name);
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())){
                    IsNameReady=true;
                }else {
                    IsNameReady=false;
                }
                handler.sendEmptyMessage(0);
            }
        });
        nickNameView = (EditText) findViewById(R.id.edit_nickname);

        areaView = (TextView) findViewById(R.id.edit_area);
        LinearLayout area_layout_view = (LinearLayout) findViewById(R.id.area_layout_view);
        schoolView = (TextView) findViewById(R.id.edit_school);
        LinearLayout school_layout_view = (LinearLayout) findViewById(R.id.school_layout_view);
        stageView = (TextView) findViewById(R.id.edit_stageId);
        LinearLayout stage_layout_view = (LinearLayout) findViewById(R.id.stage_layout_view);

        nextView = (TextView) findViewById(R.id.edit_next);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, nextView);

        delUserName = findViewById(R.id.del_username);
        delNickName = findViewById(R.id.del_nickname);
        delArea = findViewById(R.id.del_editarea);
        delSchool = findViewById(R.id.del_editschool);
        delStage = findViewById(R.id.del_editstage);

        delUserName.setOnClickListener(this);
        delNickName.setOnClickListener(this);
//        delArea.setOnClickListener(this);
//        delSchool.setOnClickListener(this);
//        delStage.setOnClickListener(this);

        nameView.addTextChangedListener(new MyTextWatcher());
        nickNameView.addTextChangedListener(new MyTextWatcher());

        titleView.setText(R.string.edit_title_new);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideWindowSoft();
                UserInfoActivity.this.finish();
            }
        });

        final DoubleKillUtils killUtils=DoubleKillUtils.getInstence();
        killUtils.setTime(500);

        school_layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (killUtils.getFlag()) {
                    UserLocationSelectActivity
                            .launch(UserInfoActivity.this, provinceList,
                                    UserLocationSelectActivity.LOCATION_CONSTANT_PROVINCE,
                                    UserLocationSelectActivity.LOCATION_CONSTANT_REGISTER_TYPE);
                }
//                SchoolSearchActivity.launch(UserInfoActivity.this, zipcode,LAUNCHER_FROM_USERINFO_TO_SCHOOL);
            }
        });

        area_layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sdsss
                UserLocationSelectActivity
                        .launch(UserInfoActivity.this, provinceList,
                                UserLocationSelectActivity.LOCATION_CONSTANT_PROVINCE,
                                UserLocationSelectActivity.LOCATION_CONSTANT_REGISTER_TYPE);
            }
        });

        stage_layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStageSelectActivity.launch(UserInfoActivity.this, stageId,
                        LAUNCHER_FROM_USERINFO_TO_STAGE);
            }
        });
        nextView.setTextColor(getResources().getColor(R.color.color_e4b62e));
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(nameView.getText().toString())) {
//                    Util.showUserToast(R.string.edit_name_null, -1, -1);
                    return;
                }
                if (StringUtils.isEmpty(zipcode)) {
//                    Util.showUserToast(R.string.edit_area_null, -1, -1);
                    return;
                }
                if (schoolView.getTag() == null && StringUtils
                        .isEmpty(schoolView.getText().toString())) {
//                    Util.showUserToast(R.string.edit_school_null, -1, -1);
                    return;
                }
                if (stageView.getTag() == null
                        || ((Integer) stageView.getTag()) == 0) {
//                    Util.showUserToast(R.string.edit_stage_null, -1, -1);
                    return;
                }
                hideWindowSoft();
                if (StringUtils.isEmpty(platform)) {
                    register();
                } else {
                    requestRegisterTask();
                }
            }
        });

        if (wxUserInfoBean != null) {
            //正则匹配是否是表情符号
            try {
                LogInfo.log("king", "wxNickName = " + wxUserInfoBean.getNickname().toString());
                char nicks[] = wxUserInfoBean.getNickname().toString().toCharArray();
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < nicks.length; i++) {
                    Matcher matcher = pattern.matcher(String.valueOf(nicks[i]));
                    if (!matcher.matches()) {
                        str.append(nicks[i]);
                    }
                }
                nickNameView.setText(str.toString());
            } catch (Exception e) {

            }
        }
    }

    private void register() {
        String schoolId = "";
        if (schoolView.getTag() != null) {
            schoolId = (String) schoolView.getTag();
        }
        int stageId = (Integer) stageView.getTag();
        String schoolName = schoolView.getText().toString();
        String realname = nameView.getText().toString().trim();
        rootView.loading(true);
        RequestNewRegisterTask newRegisterTask = new RequestNewRegisterTask(this, LoginModel.getMobile(),
                realname, provinceId, cityId, zipcode, schoolId, stageId, schoolName, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                UserInfoBean userInfoBean = (UserInfoBean) result;
                if (userInfoBean.getStatus().getCode() == 0) {
                    userInfoBean.setIsThridLogin(false);
                    MainActivity.launchActivity(UserInfoActivity.this);
                    UserInfoActivity.this.finish();
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
        newRegisterTask.start();

//        requestRegisterTask = new RequestRegisterTask(this, LoginModel.getBindMobile(),
//                LoginModel.getPassword(),
//                nameView.getText().toString(), nickNameView.getText().toString(),provinceId,cityId,
//                zipcode, schoolId, stageId, schoolName,new AsyncCallBack() {
//            @Override public void update(YanxiuBaseBean result) {
//                rootView.finish();
//                UserInfoBean userInfoBean = (UserInfoBean)result;
//                if(userInfoBean.getStatus().getCode() == 0){
//                    userInfoBean.setIsThridLogin(false);
//                    MainActivity.launchActivity(UserInfoActivity.this);
//                    UserInfoActivity.this.finish();
//                    registerStatistics();
//                }else{
//                    Util.showUserToast(userInfoBean.getStatus().getDesc(), null,
//                            null);
//                }
//            }
//
//            @Override public void dataError(int type, String msg) {
//                rootView.finish();
//                if(!StringUtils.isEmpty(msg)){
//                    Util.showUserToast(msg, null, null);
//                } else{
//                   Util.showUserToast(R.string.net_null, -1, -1);
//                }
//            }
//        });
//        requestRegisterTask.start();
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


    protected void initProvinceDatas() {
        provinceList = null;
        provinceList = new ArrayList<ProvinceModel>();
        AssetManager asset = this.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList.addAll(handler.getDataList());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(RegisiterDistrictModel districtModel) {
        if (districtModel != null) {
            areaView.setText(districtModel.getProvinceName() + "/" + districtModel.getCityName() + "/" + districtModel.getName());
            provinceId = districtModel.getProvinceId();
            cityId = districtModel.getCityId();
            zipcode = districtModel.getZipcode();
            if (!StringUtils.isEmpty(zipcode)){
                IsZipcodeReady=true;
            }else {
                IsZipcodeReady=false;
            }
            handler.sendEmptyMessage(0);
        }
    }

    public void onEventMainThread(School mSchool) {
        if (mSchool != null && mSchool.getType().equals("school")) {
            schoolView.setText(mSchool.getName().toString());
            schoolView.setTag(mSchool.getId());
            if (!StringUtils.isEmpty(schoolView.getText().toString())){
                IsSchoolReady=true;
            }else {
                IsSchoolReady=false;
            }
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UserLocationSelectActivity.LOCATION_REQUEST_CODE:
                    DistrictModel districtModel = (DistrictModel) data.getSerializableExtra("districtModel");
                    if (districtModel != null) {
                        areaView.setText(districtModel.getProvinceName() + "/" + districtModel.getCityName() + "/" + districtModel.getName());
                        provinceId = districtModel.getProvinceId();
                        cityId = districtModel.getCityId();
                        zipcode = districtModel.getZipcode();
                    }
                    break;
                case LAUNCHER_FROM_USERINFO_TO_SCHOOL:
                    Bundle bundle = data.getBundleExtra("data");
                    if (bundle != null) {
                        School school = (School) bundle.getSerializable("school");
                        if (school != null) {
                            schoolView.setText(school.getName().toString());
                            schoolView.setTag(school.getId());
                        }
                    }
                    break;
                case MyStageSelectActivity.STAGE_REQUEST_CODE:
                    stageId = data.getIntExtra("type", MyStageSelectActivity.STAGE_TYPE_JUIN);
                    stageView.setTag(stageId);
                    if (stageId == MyStageSelectActivity.STAGE_TYPE_PRIM) {
                        stageView.setText(getResources().getString(R.string.primary_txt));
                    } else if (stageId == MyStageSelectActivity.STAGE_TYPE_JUIN) {
                        stageView.setText(getResources().getString(R.string.juinor_txt));
                    } else if (stageId == MyStageSelectActivity.STAGE_TYPE_HIGH) {
                        stageView.setText(getResources().getString(R.string.high_txt));
                    }
                    if (!StringUtils.isEmpty(stageView.getText().toString())){
                        IsStageReady=true;
                    }else {
                        IsStageReady=false;
                    }
                    handler.sendEmptyMessage(0);
                    break;
            }
        }
    }


    private void requestRegisterTask() {
        String schoolId = "";
        if (schoolView.getTag() != null) {
            schoolId = (String) schoolView.getTag();
        }
        int stageId = (Integer) stageView.getTag();
        String schoolName = schoolView.getText().toString();
        String sex = "0";
        String head = "";
        if (wxUserInfoBean != null) {
            sex = wxUserInfoBean.getSex() == 1 ? "2" : "1";
            head = wxUserInfoBean.getHeadimgurl();
        }
        rootView.loading(true);
        new RequestThirdRegisterTask(UserInfoActivity.this, openid, uniqid, platform, head,
                sex, nickNameView.getText().toString().trim(), CommonCoreUtil.getAppDeviceId(UserInfoActivity.this),
                nameView.getText().toString().trim(), provinceId, schoolName, schoolId, cityId, zipcode, stageId + "", new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                UserInfoBean userInfoBean = (UserInfoBean) result;
                if (userInfoBean != null && userInfoBean.getStatus() != null) {
                    if (userInfoBean.getStatus().getCode() == 0) {
                        userInfoBean.setIsThridLogin(true);
                        MainActivity.launchActivity(UserInfoActivity.this);
                        UserInfoActivity.this.finish();
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
        }).start();
    }

    private void hideWindowSoft() {
        CommonCoreUtil.hideSoftInput(nameView);
        CommonCoreUtil.hideSoftInput(nickNameView);
        if (requestRegisterTask != null) {
            requestRegisterTask.cancel();
            requestRegisterTask = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_username:
                nameView.setText("");
                break;
            case R.id.del_nickname:
                nickNameView.setText("");
                break;
            case R.id.del_editarea:
                areaView.setText("");
                break;
            case R.id.del_editschool:
                schoolView.setText("");
                schoolView.setTag(null);
                IsSchoolReady=false;
                handler.sendEmptyMessage(0);
                break;
            case R.id.del_editstage:
                stageView.setText("");
                stageView.setTag(null);
                IsStageReady=false;
                handler.sendEmptyMessage(0);
                break;
        }
    }
}
