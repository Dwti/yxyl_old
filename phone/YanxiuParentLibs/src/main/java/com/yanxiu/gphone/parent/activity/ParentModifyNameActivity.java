package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.bean.ParentNameEventBusModel;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestParentUpdateInfoTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by lidongming on 16/3/18.
 * 家长端-------修改name
 */
public class ParentModifyNameActivity extends TopViewBaseActivity {

//    private View mView;

    public final static int MAX_NUMBERS = 12;

    private EditText etCurrentName;

    private RequestParentUpdateInfoTask requestParentUpdateInfoTask;

    public static void launch(Activity context) {
        Intent intent = new Intent(context, ParentModifyNameActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.parent_modity_name_layout);
        mPublicLayout.finish();
//        mView= LayoutInflater.from(this).inflate(R.layout.parent_modity_name_layout,null);
        initView();
        initData();
        return mPublicLayout;
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


    private void initView(){

        etCurrentName = (EditText) mPublicLayout.findViewById(R.id.input_current_name);
        etCurrentName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_NUMBERS)});
        if(LoginModel.getRoleUserInfoEntity() != null){
            String parentName = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getRealname();
            if(TextUtils.isEmpty(parentName)){
                etCurrentName.setText(this.getResources().getString(R.string.user_name_txt));
            }else{
                etCurrentName.setText(parentName);
            }
        }else{
            etCurrentName.setText(this.getResources().getString(R.string.user_name_txt));
        }

        //EditText中文本的末尾处
        Editable etext = etCurrentName.getText();
        Selection.setSelection(etext, etext.length());

    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.parent_name_txt));
        rightText.setText(this.getResources().getString(R.string.parent_save));
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
//        ParentUtils.showToast(R.string.user_edit_info_name_is_null);
        if(view == rightText){
            CommonCoreUtil.hideSoftInput(view);
            checkParams();
        }
    }


    private void checkParams(){
        String msg = etCurrentName.getEditableText().toString();
        if(TextUtils.isEmpty(msg)){
            ParentUtils.showToast(R.string.user_edit_info_name_is_null);
            return;
        } else {
            updateDate(msg);
        }
    }

    private void cancelTask(){

        if(requestParentUpdateInfoTask != null){
            requestParentUpdateInfoTask.cancel();
        }

    }

    private void updateDate(final String nameInfo){

        mPublicLayout.loading(true);

        cancelTask();
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("parentId", "88888888");
        hashMap.put("realname", nameInfo);
        requestParentUpdateInfoTask = new RequestParentUpdateInfoTask(this, hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                ParentUtils.showToast(R.string.pwd_modify_succeed);
                if(LoginModel.getRoleUserInfoEntity() != null){
                    ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setRealname(nameInfo);
                    LoginModel.savaCacheData();
                }
                ParentNameEventBusModel parentNameEventBusModel = new ParentNameEventBusModel();
                parentNameEventBusModel.setName(nameInfo);
                EventBus.getDefault().post(parentNameEventBusModel);

                ParentModifyNameActivity.this.finish();
            }
            @Override
            public void dataError(int type, String msg) {
                mPublicLayout.finish();
                if (!TextUtils.isEmpty(msg)) {
                    ParentUtils.showToast(msg);
                } else {
                    ParentUtils.showToast(R.string.data_uploader_failed);
                }
            }
        });
        requestParentUpdateInfoTask.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTask();
        requestParentUpdateInfoTask = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ParentUtils.hideSoftInput(etCurrentName);
    }


    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected boolean isAttach() {
        return false;
    }


    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }



}
