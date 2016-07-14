package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.UserInfoActivity;
import com.yanxiu.gphone.hd.student.adapter.SearchAdapter;
import com.yanxiu.gphone.hd.student.bean.School;
import com.yanxiu.gphone.hd.student.bean.SchoolListBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.requestTask.SearchSchoolTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/2.
 */
public class SchoolSearchFragment extends  TopBaseFragment {
    private final static String AREA_ID_KEY="area_id";
    private final static String TYPE_KEY="type_key";

    private EditText searchEditView;

    private TextView searchAddView;

    private ListView searchListView;

    private SearchAdapter adapter;

    private School mSchool;

    private String areaId;

    private int type;

    private ArrayList<School> schoolList = new ArrayList<>();

    private SearchSchoolTask mSearchSchoolTask;

    private MyUserInfoContainerFragment fg;


    private static SchoolSearchFragment schoolSearchFragment;

    public static Fragment newInstance(String areaId,int type){
        if(schoolSearchFragment==null){
            schoolSearchFragment=new SchoolSearchFragment();
            Bundle bundle=new Bundle();
            bundle.putString(AREA_ID_KEY,areaId);
            bundle.putInt(TYPE_KEY, type);
            schoolSearchFragment.setArguments(bundle);
        }
        return  schoolSearchFragment;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
        titleText.setText(R.string.school_name_title);
    }

    @Override
    protected View getContentView() {
        fg= (MyUserInfoContainerFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.fragment_search_school_layout);
        mPublicLayout.setBackgroundColor(getActivity().getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);

        areaId=getArguments().getString(AREA_ID_KEY);
        type=getArguments().getInt(TYPE_KEY);

        initView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {

    }

    private void initView(){

        searchEditView = (EditText)mPublicLayout.findViewById(R.id.search_edit);

        searchAddView = (TextView)mPublicLayout.findViewById(R.id.search_add_text);
        searchListView = (ListView)mPublicLayout.findViewById(R.id.search_list);
        searchEditView.addTextChangedListener(new SearchTextWatcher());


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

        View headView =View.inflate(getActivity(), R.layout.search_head_view_layout, null);

        searchListView.addHeaderView(headView);

        adapter = new SearchAdapter(schoolList,getActivity());
        showListView();
        searchListView.setAdapter(adapter);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    position = position - searchListView.getHeaderViewsCount();
                    if (position < 0) {
                        return;
                    }
                    if (schoolList != null && schoolList.size() > position) {
                        mSchool = schoolList.get(position);
                        upLoadSchool(mSchool.getName(), mSchool.getId());
                    }
                } catch (Exception e) {
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
            EventBus.getDefault().post(mSchool);
        }else{
            mPublicLayout.loading(true);
            HashMap<String, String> hashMap = new HashMap<>();
            if(!TextUtils.isEmpty(schoolId)){
                hashMap.put("schoolid", schoolId);
            }
            hashMap.put("schoolName", schoolName);
            new RequestUpdateUserInfoTask(getActivity(), hashMap, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    mPublicLayout.finish();
                    LoginModel.getUserinfoEntity().setSchoolName(schoolName);
                    LoginModel.getUserinfoEntity().setSchoolid(schoolId);
                    LoginModel.savaCacheData();
                    EventBus.getDefault().post(mSchool);
                    finish();
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
        }
    }

    @Override
    public void onReset() {
        destoryData();
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
            mPublicLayout.finish();
            if(result!=null){
                SchoolListBean schoolListBean = (SchoolListBean)result;
                if(schoolListBean.getStatus().getCode() == 0){
                    schoolList = schoolListBean.getData();
                    adapter.setList(schoolList);
                    showListView();
                }else{
                    Util.showToast(schoolListBean.getStatus().getDesc());
                }
            }else{
                Util.showToast(R.string.data_null);
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
        mSearchSchoolTask = new SearchSchoolTask(getActivity(), keyWord, areaId,
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
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {
        cancelTask();
        finish();
    }

    private void  finish(){
        CommonCoreUtil.hideSoftInput(searchAddView);
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }
        schoolSearchFragment=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchEditView=null;

        searchAddView=null;

        searchListView=null;

        adapter=null;

        mSchool=null;

        areaId=null;

        schoolList=null;

        fg=null;
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            CommonCoreUtil.hideSoftInput(searchAddView);
        }
    }
}
