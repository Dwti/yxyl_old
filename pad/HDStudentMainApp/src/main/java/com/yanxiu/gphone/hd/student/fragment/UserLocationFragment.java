package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.YanxiuUserLocationAdapter;
import com.yanxiu.gphone.hd.student.bean.CityModel;
import com.yanxiu.gphone.hd.student.bean.DistrictModel;
import com.yanxiu.gphone.hd.student.bean.ProvinceModel;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/1.
 */
public class UserLocationFragment extends TopBaseFragment {
    private static final String DATA_BEAN_KEY="dataBase_key";
    private static final String TYPE_KEY="type_key";

    private final static int LOCATION_CONSTANT_PROVINCE = 0x101;
    private final static int LOCATION_CONSTANT_CITY = 0x102;
    private final static int LOCATION_CONSTANT_DISTRICT = 0x103;

    private ListView locationList;
    private int type = LOCATION_CONSTANT_PROVINCE;

    private ArrayList<YanxiuBaseBean> dataBean;
    private YanxiuUserLocationAdapter adapter;

    private DistrictModel districtModel;

    private MyUserInfoContainerFragment mFg;


    public static Fragment newInstance(ArrayList dataBean,int type){

            UserLocationFragment mUserLocationFg=new UserLocationFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable(DATA_BEAN_KEY,dataBean);
            bundle.putInt(TYPE_KEY, type);
            mUserLocationFg.setArguments(bundle);

        return  mUserLocationFg;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }


    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.user_area_str);
    }

    @Override
    protected View getContentView() {
        mFg= (MyUserInfoContainerFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.select_location_user_layout);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);

        type=getArguments().getInt(TYPE_KEY);
        dataBean= (ArrayList<YanxiuBaseBean>) getArguments().getSerializable(DATA_BEAN_KEY);
        if (dataBean == null || dataBean.size() <= 0) {
            Util.showToast(R.string.select_location_data_error);
            finish();
        }
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {

    }

    private void findView() {

        locationList = (ListView) mPublicLayout.findViewById(R.id.location_list);

        View headView=View.inflate(getActivity(),R.layout.search_head_view_layout,null);
        ImageView topFlag=(ImageView)headView.findViewById(R.id.head_flag);
        topFlag.setVisibility(View.VISIBLE);
        TextView title = (TextView) headView.findViewById(R.id.headTitleText);
        rightText.setVisibility(View.GONE);
        if (type == LOCATION_CONSTANT_PROVINCE) {
            title.setText(R.string.select_location_province_txt);
        } else if (type == LOCATION_CONSTANT_CITY) {
            title.setText(R.string.select_location_city_txt);
        } else {
            rightText.setVisibility(View.VISIBLE);
            rightText.setText(R.string.user_name_edit_save);

            RelativeLayout.LayoutParams saveViewParams= (RelativeLayout.LayoutParams) rightText.getLayoutParams();
            saveViewParams.width=getResources().getDimensionPixelOffset(R.dimen.dimen_47);
            saveViewParams.height=getResources().getDimensionPixelOffset(R.dimen.dimen_31);
            rightText.setLayoutParams(saveViewParams);
            rightText.setGravity(Gravity.CENTER);
            rightText.setTextColor(getResources().getColor(R.color.color_006666));
            rightText.setBackgroundResource(R.drawable.edit_save_selector);

            title.setText(R.string.select_location_district_txt);
        }



        locationList.addHeaderView(headView);
        headView.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.dimen_5), 0, getResources().getDimensionPixelOffset(R.dimen.dimen_5));
        adapter = new YanxiuUserLocationAdapter(getActivity(), type);
        locationList.setAdapter(adapter);
        adapter.setList(dataBean);
        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!CommonCoreUtil.checkClickEvent(800)) {
                        return;
                    }
                    position = position - locationList.getHeaderViewsCount();
                    if (position < 0) {
                        return;
                    }
                    LogInfo.log("Lee: ", "position: " + position + "locationList.getHeaderViewsCount(): " + locationList.getHeaderViewsCount());
                    if (type == LOCATION_CONSTANT_PROVINCE) {
                        mFg.mIFgManager.addFragment(UserLocationFragment.newInstance(((ProvinceModel) adapter.getItem(
                                position)).getCityList(), LOCATION_CONSTANT_CITY), true, UserLocationFragment.class.getName());
                    } else if (type == LOCATION_CONSTANT_CITY) {
                        mFg.mIFgManager.addFragment(UserLocationFragment.newInstance(((CityModel) adapter.getItem(position)).getDistrictList(), LOCATION_CONSTANT_DISTRICT), true, UserLocationFragment.class
                                .getName());
                    } else if (type == LOCATION_CONSTANT_DISTRICT) {
                        adapter.setSelectItemIndex(position);
                        districtModel = (DistrictModel) adapter.getItem(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }




    private void finish() {
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }


    }


    private void finishMutiFg(){
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popBackStackImmediate(UserLocationFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }




    @Override
    protected void setContentListener() {
        rightText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.pub_top_right:
                upLoadAddress(districtModel);
                break;
        }
    }

    private void upLoadAddress(final DistrictModel districtModel) {
        if(districtModel != null) {
            mPublicLayout.loading(true);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("provinceName", districtModel.getProvinceName());
            hashMap.put("provinceid", districtModel.getProvinceId());
            hashMap.put("cityName", districtModel.getCityName());
            hashMap.put("cityid", districtModel.getCityId());
            hashMap.put("areaName", districtModel.getName());
            hashMap.put("areaid", districtModel.getZipcode());
            new RequestUpdateUserInfoTask(getActivity(), hashMap, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    mPublicLayout.finish();
                    if (LoginModel.getUserinfoEntity()!=null) {
                        if(!StringUtils.isEmpty(districtModel.getProvinceName())){
                            LoginModel.getUserinfoEntity().setProvinceName(districtModel.getProvinceName());
                        }
                        if(!StringUtils.isEmpty(districtModel.getCityName())){
                            LoginModel.getUserinfoEntity().setCityName(districtModel.getCityName());
                        }
                        if(!StringUtils.isEmpty(districtModel.getName())){
                            LoginModel.getUserinfoEntity().setAreaName(districtModel.getName());
                        }

                        if(!StringUtils.isEmpty(districtModel.getProvinceId())){
                            LoginModel.getUserinfoEntity().setProvinceid(districtModel.getProvinceId());
                        }
                        if(!StringUtils.isEmpty(districtModel.getCityId())){
                            LoginModel.getUserinfoEntity().setCityid(districtModel.getCityId());
                        }
                        if(!StringUtils.isEmpty(districtModel.getZipcode())){

                            LoginModel.getUserinfoEntity().setAreaid(districtModel.getZipcode());
                        }


                        LoginModel.savaCacheData();

                        EventBus.getDefault().post(districtModel);
                    }
                    finishMutiFg();

                }

                @Override
                public void dataError(int type, String msg) {
                    mPublicLayout.finish();
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


    @Override
    protected void destoryData() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        locationList=null;
        dataBean=null;
        adapter=null;

        districtModel=null;

        mFg=null;
    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }

    @Override
    public void onReset() {
        destoryData();
    }
}
