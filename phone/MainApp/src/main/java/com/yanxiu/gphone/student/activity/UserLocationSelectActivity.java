package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.YanxiuUserLocationAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.CityModel;
import com.yanxiu.gphone.student.bean.DistrictModel;
import com.yanxiu.gphone.student.bean.ProvinceModel;
import com.yanxiu.gphone.student.bean.RegisiterDistrictModel;
import com.yanxiu.gphone.student.bean.School;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.manager.ActivityManager;
import com.yanxiu.gphone.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.activity.UserInfoActivity.LAUNCHER_FROM_USERINFO_TO_SCHOOL;

/**
 * Created by Administrator on 2015/7/13.
 */
public class UserLocationSelectActivity extends YanxiuBaseActivity implements View.OnClickListener {
    public final static int LOCATION_REQUEST_CODE = 0x10;
    public final static int LOCATION_CONSTANT_PROVINCE = 0x101;
    public final static int LOCATION_CONSTANT_CITY = 0x102;
    public final static int LOCATION_CONSTANT_DISTRICT = 0x103;

    public final static int LOCATION_CONSTANT_REGISTER_DEFAULT = 0x11;
    /**
     * 注册
     */
    public final static int LOCATION_CONSTANT_REGISTER_TYPE = 0x12;
    private PublicLoadLayout mRootView;
    private ListView locationList;
    private View topView;
    private View backView;
    private TextView topTitle, topRight;

    private int type = LOCATION_CONSTANT_PROVINCE;
    private int registerType = LOCATION_CONSTANT_REGISTER_DEFAULT;
    private ArrayList<YanxiuBaseBean> dataBean;
    private YanxiuUserLocationAdapter adapter;
    private DistrictModel districtModel;
    private TextView title;

    public static void launch(Activity activity, ArrayList dataBean, int type) {
        Intent intent = new Intent(activity, UserLocationSelectActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("baseBean", dataBean);
        activity.startActivityForResult(intent, LOCATION_REQUEST_CODE);
    }

    public static void launchForward(Activity activity, ArrayList dataBean, int type) {
        Intent intent = new Intent(activity, UserLocationSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("type", type);
        intent.putExtra("baseBean", dataBean);
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, ArrayList dataBean, int type, int registerType) {
        Intent intent = new Intent(activity, UserLocationSelectActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("registerType", registerType);
        intent.putExtra("baseBean", dataBean);
        activity.startActivityForResult(intent, LOCATION_REQUEST_CODE);
    }

    public static void launchForward(Activity activity, ArrayList dataBean, int type, int registerType) {
        Intent intent = new Intent(activity, UserLocationSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("type", type);
        intent.putExtra("registerType", registerType);
        intent.putExtra("baseBean", dataBean);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_select_location_user_layout);
        setContentView(mRootView);
        type = getIntent().getIntExtra("type", LOCATION_CONSTANT_PROVINCE);
        registerType = getIntent().getIntExtra("registerType", LOCATION_CONSTANT_REGISTER_DEFAULT);
        dataBean = (ArrayList<YanxiuBaseBean>) getIntent().getSerializableExtra("baseBean");
        if (dataBean == null || dataBean.size() <= 0) {
            Util.showToast(R.string.select_location_data_error);
            finish();
        }
        findView();
    }

    private void findView() {
        topView = mRootView.findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        backView.setOnClickListener(this);
        topTitle = (TextView) topView.findViewById(R.id.pub_top_mid);

        topRight = (TextView) mRootView.findViewById(R.id.pub_top_right);
        topTitle.setText(R.string.user_area_str);

        topRight.setVisibility(View.VISIBLE);
        topRight.setText(R.string.user_name_edit_save);
        topRight.setOnClickListener(this);

        locationList = (ListView) mRootView.findViewById(R.id.location_list);


        View headView = View.inflate(this, R.layout.search_head_view_layout, null);
        ImageView topFlag = (ImageView) headView.findViewById(R.id.head_flag);
        topFlag.setVisibility(View.VISIBLE);
        title = (TextView) headView.findViewById(R.id.headTitleText);
        topRight.setVisibility(View.GONE);
        if (type == LOCATION_CONSTANT_PROVINCE) {
            title.setText(R.string.select_location_province_txt);
        } else if (type == LOCATION_CONSTANT_CITY) {
            title.setText(R.string.select_location_city_txt);
        } else {

//            if (registerType == LOCATION_CONSTANT_REGISTER_TYPE){
//
//            }else {
//                topRight.setVisibility(View.VISIBLE);
//                topRight.setText(R.string.user_name_edit_save);
//                topRight.setOnClickListener(this);
//
//                RelativeLayout.LayoutParams saveViewParams = (RelativeLayout.LayoutParams) topRight.getLayoutParams();
//                saveViewParams.width = getResources().getDimensionPixelOffset(R.dimen.dimen_47);
//                saveViewParams.height = getResources().getDimensionPixelOffset(R.dimen.dimen_31);
//                topRight.setLayoutParams(saveViewParams);
//                topRight.setGravity(Gravity.CENTER);
//                topRight.setTextColor(getResources().getColor(R.color.color_006666));
//                topRight.setBackgroundResource(R.drawable.edit_save_selector);
//            }
            title.setText(R.string.select_location_district_txt);
        }
        locationList.addHeaderView(headView);
        headView.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.dimen_5), 0, getResources().getDimensionPixelOffset(R.dimen.dimen_5));
        adapter = new YanxiuUserLocationAdapter(this, type);
        locationList.setAdapter(adapter);
        adapter.setList(dataBean);
        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    position = position - locationList.getHeaderViewsCount();
                    if (position < 0) {
                        return;
                    }
                    LogInfo.log("Lee: ", "position: " + position + "locationList.getHeaderViewsCount(): " + locationList.getHeaderViewsCount());
                    if (type == LOCATION_CONSTANT_PROVINCE) {
                        if (registerType == LOCATION_CONSTANT_REGISTER_TYPE) {
                            launchForward(UserLocationSelectActivity.this, ((ProvinceModel) adapter.getItem(position)).getCityList(), LOCATION_CONSTANT_CITY, registerType);
                        } else {
                            launchForward(UserLocationSelectActivity.this, ((ProvinceModel) adapter.getItem(
                                    position)).getCityList(), LOCATION_CONSTANT_CITY);
                        }
                    } else if (type == LOCATION_CONSTANT_CITY) {
                        if (registerType == LOCATION_CONSTANT_REGISTER_TYPE) {
                            launchForward(UserLocationSelectActivity.this, ((CityModel) adapter.getItem(position)).getDistrictList(), LOCATION_CONSTANT_DISTRICT, registerType);
                        } else {
                            launchForward(UserLocationSelectActivity.this, ((CityModel) adapter.getItem(position)).getDistrictList(), LOCATION_CONSTANT_DISTRICT);
                        }
                    } else if (type == LOCATION_CONSTANT_DISTRICT) {
                        if (registerType == LOCATION_CONSTANT_REGISTER_TYPE) {
                            RegisiterDistrictModel mRegisiterDistrictModel = CommonCoreUtil.copyBean((DistrictModel) adapter.getItem(position), RegisiterDistrictModel.class);
                            EventBus.getDefault().post(mRegisiterDistrictModel);
                            SchoolSearchActivity.launch(UserLocationSelectActivity.this, mRegisiterDistrictModel.getZipcode(),LAUNCHER_FROM_USERINFO_TO_SCHOOL);
                        } else {
                            RegisiterDistrictModel mRegisiterDistrictModel = CommonCoreUtil.copyBean((DistrictModel) adapter.getItem(position), RegisiterDistrictModel.class);
//                            EventBus.getDefault().post(mRegisiterDistrictModel);
//                            asafs
                            SchoolSearchActivity.launch(UserLocationSelectActivity.this, mRegisiterDistrictModel,MyUserInfoActivity.SEARCH_SCHOOL_REQUESTCODE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
//            if (requestCode==LAUNCHER_FROM_USERINFO_TO_SCHOOL){
                ActivityManager.destoryAllUserLocationSelectActivity();
//            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backView) {
            finish();
        } else if (v == topRight) {
            if (registerType == LOCATION_CONSTANT_REGISTER_TYPE) {
                if (districtModel != null) {
                    LogInfo.log("haitian", "to string =" + districtModel.toString());
                    RegisiterDistrictModel mRegisiterDistrictModel = CommonCoreUtil.copyBean(districtModel, RegisiterDistrictModel.class);
//                    Intent intent = new Intent();
//                    intent.putExtra("districtModel", districtModel);
//                    setResult(Activity.RESULT_OK, intent);
                    EventBus.getDefault().post(mRegisiterDistrictModel);
                    finish();
                    ActivityManager.destoryAllUserLocationSelectActivity();
                }
            } else {
                upLoadAddress(districtModel);
            }
        }
    }

    private void upLoadAddress(final DistrictModel districtModel) {
        if (districtModel != null) {
            mRootView.loading(true);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("provinceName", districtModel.getProvinceName());
            hashMap.put("provinceid", districtModel.getProvinceId());
            hashMap.put("cityName", districtModel.getCityName());
            hashMap.put("cityid", districtModel.getCityId());
            hashMap.put("areaName", districtModel.getName());
            hashMap.put("areaid", districtModel.getZipcode());
            new RequestUpdateUserInfoTask(this, hashMap, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    mRootView.finish();
                    if (districtModel != null) {
                        LoginModel.getUserinfoEntity().setProvinceName(districtModel.getProvinceName());
                        LoginModel.getUserinfoEntity().setCityName(districtModel.getCityName());
                        LoginModel.getUserinfoEntity().setAreaName(districtModel.getName());

                        LoginModel.getUserinfoEntity().setProvinceid(districtModel.getProvinceId());
                        LoginModel.getUserinfoEntity().setCityid(districtModel.getCityId());
                        LoginModel.getUserinfoEntity().setAreaid(districtModel.getZipcode());

                        LoginModel.savaCacheData();
//                        Intent intent = new Intent();
//                        intent.putExtra("districtModel", districtModel);
//                        setResult(Activity.RESULT_OK, intent);
                        EventBus.getDefault().post(districtModel);
                    }
                    finish();
                    ActivityManager.destoryAllUserLocationSelectActivity();
                }

                @Override
                public void dataError(int type, String msg) {
                    mRootView.finish();
                    if (!TextUtils.isEmpty(msg)) {
                        Util.showToast(msg);
                    } else {
                        Util.showToast(R.string.data_uploader_failed);
                    }
                }
            }).start();
        } else {
            Util.showToast(R.string.select_location_district_txt);
        }
    }
}
