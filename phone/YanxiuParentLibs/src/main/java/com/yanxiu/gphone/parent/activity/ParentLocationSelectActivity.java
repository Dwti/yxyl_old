package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.adapter.ParentLocationAdapter;
import com.yanxiu.gphone.parent.bean.ParentCityModel;
import com.yanxiu.gphone.parent.bean.ParentDistrictModel;
import com.yanxiu.gphone.parent.bean.ParentProvinceModel;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.manager.ParentActivityManager;
import com.yanxiu.gphone.parent.requestTask.RequestParentUpdateInfoTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.view.ParentLoadingLayout;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/13.
 */
public class ParentLocationSelectActivity extends TopViewBaseActivity implements View.OnClickListener {
    public final static int LOCATION_REQUEST_CODE = 0x10;
    public final static int LOCATION_CONSTANT_PROVINCE = 0x101;
    public final static int LOCATION_CONSTANT_CITY = 0x102;
    public final static int LOCATION_CONSTANT_DISTRICT = 0x103;

    public final static int LOCATION_CONSTANT_REGISTER_DEFAULT = 0x11;
    public final static int LOCATION_CONSTANT_REGISTER_TYPE= 0x12;
//    private PublicLoadLayout mRootView;
    private ListView locationList;

    private View mView;

    private int type = LOCATION_CONSTANT_PROVINCE;
    private int registerType = LOCATION_CONSTANT_REGISTER_DEFAULT;
    private ArrayList<YanxiuBaseBean> dataBean;
    private ParentLocationAdapter adapter;
    private ParentDistrictModel districtModel;
    private TextView tvLocationTips;

    public static void launch(Activity activity, ArrayList dataBean, int type) {
        Intent intent = new Intent(activity, ParentLocationSelectActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("baseBean", dataBean);
        activity.startActivityForResult(intent, LOCATION_REQUEST_CODE);
    }
    public static void launchForward(Activity activity, ArrayList dataBean, int type) {
        Intent intent = new Intent(activity, ParentLocationSelectActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("type", type);
        intent.putExtra("baseBean", dataBean);
        activity.startActivity(intent);
    }

//    public static void launch(Activity activity, ArrayList dataBean, int type,int registerType) {
//        Intent intent = new Intent(activity, ParentLocationSelectActivity.class);
//        intent.putExtra("type", type);
//        intent.putExtra("registerType", registerType);
//        intent.putExtra("baseBean", dataBean);
//        activity.startActivityForResult(intent, LOCATION_REQUEST_CODE);
//    }
    public static void launchForward(Activity activity, ArrayList dataBean, int type, int registerType) {
        Intent intent = new Intent(activity, ParentLocationSelectActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra("type", type);
        intent.putExtra("registerType", registerType);
        intent.putExtra("baseBean", dataBean);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mView= LayoutInflater.from(this).inflate(R.layout.activity_select_location_parent_layout, null);
        initLoadingBar(this,INDEX_SECOND);
//        mView = PublicLoadUtils.createPage(this, R.layout.activity_select_location_parent_layout);
//        mView.finish();
//        mView.setContentBackground(getResources().getColor(android.R.color.transparent));
        type = getIntent().getIntExtra("type", LOCATION_CONSTANT_PROVINCE);
        registerType = getIntent().getIntExtra("registerType", LOCATION_CONSTANT_REGISTER_DEFAULT);
        dataBean = (ArrayList<YanxiuBaseBean>) getIntent().getSerializableExtra("baseBean");
        if (dataBean == null || dataBean.size() <= 0) {
            ParentUtils.showToast(R.string.select_location_data_error);
            finish();
        }
        findView();
        initData();
        return mView;
    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.user_area_str));

        if (type == LOCATION_CONSTANT_PROVINCE) {
            tvLocationTips.setText(R.string.select_location_province_txt);
        } else if (type == LOCATION_CONSTANT_CITY) {
            tvLocationTips.setText(R.string.select_location_city_txt);
        } else {
            tvLocationTips.setText(R.string.select_location_district_txt);
            rightText.setText(this.getResources().getString(R.string.parent_save));
        }
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }



    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }

    private void findView() {

        locationList = (ListView) mView.findViewById(R.id.location_list);

        tvLocationTips = (TextView) mView.findViewById(R.id.tv_location_tip);

        adapter = new ParentLocationAdapter(this, type);
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
                            launchForward(ParentLocationSelectActivity.this, ((ParentProvinceModel) adapter.getItem(position)).getCityList(), LOCATION_CONSTANT_CITY, registerType);
                        } else {
                            launchForward(ParentLocationSelectActivity.this, ((ParentProvinceModel) adapter.getItem(position)).getCityList(), LOCATION_CONSTANT_CITY);
                        }
                    } else if (type == LOCATION_CONSTANT_CITY) {
                        if (registerType == LOCATION_CONSTANT_REGISTER_TYPE) {
                            launchForward(ParentLocationSelectActivity.this, ((ParentCityModel) adapter.getItem(position)).getDistrictList(), LOCATION_CONSTANT_DISTRICT, registerType);
                        } else {
                            launchForward(ParentLocationSelectActivity.this, ((ParentCityModel) adapter.getItem(position)).getDistrictList(), LOCATION_CONSTANT_DISTRICT);
                        }
                    } else if (type == LOCATION_CONSTANT_DISTRICT) {
                        adapter.setSelectItemIndex(position);
                        districtModel = (ParentDistrictModel) adapter.getItem(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v == rightText){
            upLoadAddress(districtModel);
        }
    }

    private void upLoadAddress(final ParentDistrictModel districtModel) {
        if(districtModel != null) {
            loading.setViewType(ParentLoadingLayout.LoadingType.LAODING_COMMON);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("provinceName", districtModel.getProvinceName());
            hashMap.put("provinceid", districtModel.getProvinceId());
            hashMap.put("cityName", districtModel.getCityName());
            hashMap.put("cityid", districtModel.getCityId());
            hashMap.put("areaName", districtModel.getName());
            hashMap.put("areaid", districtModel.getZipcode());
            new RequestParentUpdateInfoTask(this, hashMap, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    loading.setViewGone();
                    ParentUtils.showToast(R.string.pwd_modify_succeed);
                    if (districtModel != null) {
                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setProvinceidName(districtModel.getProvinceName());
                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setCityidName(districtModel.getCityName());
                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setAreaidName(districtModel.getName());

                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setProvinceid(districtModel.getProvinceId());
                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setCityid(districtModel.getCityId());
                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setAreaid(districtModel.getZipcode());

                        LoginModel.savaCacheData();
//
                        EventBus.getDefault().post(districtModel);
                    }
                    finish();
                    ParentActivityManager.destoryAllUserLocationSelectActivity();
                }

                @Override
                public void dataError(int type, String msg) {
                    loading.setViewGone();
                    if (!TextUtils.isEmpty(msg)) {
                        ParentUtils.showToast(msg);
                    } else {
                        ParentUtils.showToast(R.string.data_uploader_failed);
                    }
                }
            }).start();
        } else {
            ParentUtils.showToast(R.string.select_location_district_txt);
        }
    }
}
