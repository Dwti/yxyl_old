package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.SearchAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.RegisiterDistrictModel;
import com.yanxiu.gphone.student.bean.School;
import com.yanxiu.gphone.student.bean.SchoolListBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.student.requestTask.SearchSchoolTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/6/12.
 */
public class SchoolSearchActivity extends YanxiuBaseActivity{

    private RegisiterDistrictModel model;

    public static void launch(Activity activity, String areaId, int requestCode){
        Intent intent = new Intent(activity,SchoolSearchActivity.class);
        intent.putExtra("areaId",areaId);
        intent.putExtra("type",requestCode);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void launch(Activity activity, RegisiterDistrictModel model, int requestCode){
        Intent intent = new Intent(activity,SchoolSearchActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("model",model);
        intent.putExtra("bundle",bundle);
        intent.putExtra("areaId","bundle");
        intent.putExtra("type",requestCode);
        activity.startActivityForResult(intent,requestCode);
    }

    public PublicLoadLayout rootView;

    private EditText searchEditView;



    private TextView searchAddView;

    private ListView searchListView;

    private SearchAdapter adapter;

    private School mSchool;

    private String areaId;

    private View topView;
    private View backView;
    private TextView topTitle;

    private ArrayList<School> schoolList = new ArrayList<School>();
    private SearchSchoolTask mSearchSchoolTask;

    private static int type = 0;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_search_school_layout);
        setContentView(rootView);
        Intent intent = getIntent();
        if(intent!=null){
            areaId = intent.getStringExtra("areaId");
            type = intent.getIntExtra("type",0);
            if (areaId.equals("bundle")){
                Bundle bundle=intent.getBundleExtra("bundle");
                model= (RegisiterDistrictModel) bundle.getSerializable("model");
                areaId=model.getZipcode();
            }
        }
        initView();
        requestTask("");
    }

    private void initView(){
        topView = rootView.findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        topTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.school_name_title);

        searchEditView = (EditText)rootView.findViewById(R.id.search_edit);

        searchAddView = (TextView)rootView.findViewById(R.id.search_add_text);
        searchListView = (ListView)rootView.findViewById(R.id.search_list);
        searchEditView.addTextChangedListener(new SearchTextWatcher());

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonCoreUtil.hideSoftInput(searchAddView);
                finish();
            }
        });

        searchAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSchool = new School();
                if (TextUtils.isEmpty(searchEditView.getText().toString())) {
                    Util.showToast(R.string.add_school_name_null);
                } else {
                    mSchool.setName(searchEditView.getText().toString());
                    upLoadSchool(mSchool.getName(), null);
                }
            }
        });

        View headView =View.inflate(this, R.layout.search_head_view_layout, null);

        searchListView.addHeaderView(headView);

        adapter = new SearchAdapter(schoolList,this);
        showListView();
        searchListView.setAdapter(adapter);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try{
                    position = position - searchListView.getHeaderViewsCount();
                    if(position<0){
                        return;
                    }
                    if (schoolList != null && schoolList.size() > position) {
                        School school = schoolList.get(position);
                        mSchool = school;
                        upLoadSchool(mSchool.getName(), mSchool.getId());
                    }
                }catch (Exception e){
                    e.fillInStackTrace();
                }
            }
        });
    }

    private void showListView(){
        if(adapter.getCount()>0){
            searchListView.setVisibility(View.VISIBLE);
        }else{
            searchListView.setVisibility(View.GONE);
        }
    }

    private void upLoadSchool(final String schoolName, final String schoolId) {
        if(type == UserInfoActivity.LAUNCHER_FROM_USERINFO_TO_SCHOOL){
            mSchool.setType("school");
            EventBus.getDefault().post(mSchool);
            forResult();
        }else{
            rootView.loading(true);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            if(!TextUtils.isEmpty(schoolId)){
                hashMap.put("schoolid", schoolId);
            }
            if (model!=null) {
                hashMap.put("provinceName", model.getProvinceName());
                hashMap.put("provinceid", model.getProvinceId());
                hashMap.put("cityName", model.getCityName());
                hashMap.put("cityid", model.getCityId());
                hashMap.put("areaName", model.getName());
                hashMap.put("areaid", model.getZipcode());
            }
            hashMap.put("schoolName", schoolName);
            new RequestUpdateUserInfoTask(this, hashMap, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    rootView.finish();
                    LoginModel.getUserinfoEntity().setSchoolName(schoolName);
                    LoginModel.getUserinfoEntity().setSchoolid(schoolId);
                    LoginModel.savaCacheData();
                    mSchool.setType("schools");
                    EventBus.getDefault().post(mSchool);
                    forResult();
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
    }
    private void forResult(){
        CommonCoreUtil.hideSoftInput(searchAddView);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("school",mSchool);
        intent.putExtra("data", bundle);
        setResult(RESULT_OK, intent);
        SchoolSearchActivity.this.finish();
    }

    private class SearchTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start,
                int count, int after) {

        }
        @Override public void onTextChanged(CharSequence s, int start,
                int before, int count) {

        }

        @Override public void afterTextChanged(final Editable s) {
            int length = s.length();
            if(length>0){
                requestTask(s.toString());
            }
        }
    }
    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override public void update(YanxiuBaseBean result) {
            rootView.finish();
            if(result!=null){
                SchoolListBean schoolListBean = (SchoolListBean)result;
                if(schoolListBean.getStatus().getCode() == 0){
                    schoolList = schoolListBean.getData();
                    adapter.setList(schoolList);
                    showListView();
                }else{
                    Util.showToast(R.string.search_school_no);
                }
            }else{
                Util.showToast(R.string.search_school_no);
            }
        }

        @Override public void dataError(int type,
                                        String msg) {
            if(type == ErrorCode.NETWORK_NOT_AVAILABLE){
                Util.showToast(R.string.net_null);
            }else{
               Util.showToast(R.string.server_exception);
            }
        }
    };
    private void requestTask(String keyWord){
        cancelTask();
        mSearchSchoolTask = new SearchSchoolTask(SchoolSearchActivity.this, keyWord, areaId,
                mAsyncCallBack);
        mSearchSchoolTask.start();
    }
    private void cancelTask(){
        if(mSearchSchoolTask != null){
            mSearchSchoolTask.cancel();
        }
        mSearchSchoolTask = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTask();
        CommonCoreUtil.hideSoftInput(searchEditView);
    }
}
