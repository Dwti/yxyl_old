package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.eventbusbean.UserNameEditBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/1.
 */
public class UserNameEdFragment extends TopBaseFragment {

    public final static int USER_EDIT_NAME_TYPE = 0x101;
    public final static int USER_EDIT_NICK_NAME_TYPE = 0x102;

    private static final String TYPE="type";
    private static final String CONTENT="content";

    private RequestUpdateUserInfoTask mTask;
    private EditText editText;
    private int type;
    private String editMsg;
    private ImageView clearIcon;
    private MyUserInfoContainerFragment mFg;
    private static final String NICK_NAME_KEY="nickname";
    private static final String REAL_NAME_KEY="realname";

    public static Fragment newInstance(int type,String content){
        UserNameEdFragment  myserNameEdFragment =new UserNameEdFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(TYPE, type);
            bundle.putString(CONTENT, content);
        myserNameEdFragment.setArguments(bundle);

        return myserNameEdFragment;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        RelativeLayout.LayoutParams saveViewParams= (RelativeLayout.LayoutParams)rightText.getLayoutParams();
        saveViewParams.width=getResources().getDimensionPixelOffset(R.dimen.dimen_47);
        saveViewParams.height=getResources().getDimensionPixelOffset(R.dimen.dimen_31);
        rightText.setLayoutParams(saveViewParams);
        rightText.setGravity(Gravity.CENTER);
        rightText.setTextColor(getResources().getColor(R.color.color_006666));
        rightText.setBackgroundResource(R.drawable.edit_save_selector);
        rightText.setText(R.string.user_name_edit_save);
    }


    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }

    @Override
    protected View getContentView() {
        mFg= (MyUserInfoContainerFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.activity_edit_name_user_layout);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        type=getArguments().getInt(TYPE,USER_EDIT_NAME_TYPE);
        editMsg=getArguments().getString(CONTENT);
        if(type == USER_EDIT_NICK_NAME_TYPE){
            titleText.setText(R.string.user_nick_name);
        } else {
            titleText.setText(R.string.user_name_str);
        }
        if(!TextUtils.isEmpty(editMsg) && editMsg.length() > 25){
            editMsg = editMsg.substring(0, 24);
        }
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {

    }

    private void findView(){

        clearIcon=(ImageView)mPublicLayout.findViewById(R.id.clearContentIcon);


        editText = (EditText) mPublicLayout.findViewById(R.id.user_name_edit);
        if(!TextUtils.isEmpty(editMsg)){
            editText.setText(editMsg);
            editText.setSelection(editMsg.length());
        }


    }



    @Override
    protected void setContentListener() {
        clearIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.pub_top_right:
                String msg = editText.getEditableText().toString();
                if(TextUtils.isEmpty(msg)){
                    Util.showToast(getString(R.string.user_edit_info_is_null, titleText.getText()));
                    return;
                } else {
                    updateInfo(msg);
                }
                break;
            case R.id.clearContentIcon:
                editText.setText("");
                break;
        }

    }

    private void updateInfo(final String nameInfo){
        mPublicLayout.loading(true);
        HashMap<String, String> hashMap = new HashMap<>();
        if(type == USER_EDIT_NICK_NAME_TYPE){
            hashMap.put(NICK_NAME_KEY, nameInfo);
        } else {
            hashMap.put(REAL_NAME_KEY, nameInfo);
        }
        cancelTask();
        mTask=new RequestUpdateUserInfoTask(getActivity(),hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                if(type == USER_EDIT_NICK_NAME_TYPE){
                    LoginModel.getUserinfoEntity().setNickname(nameInfo);
                } else {
                    LoginModel.getUserinfoEntity().setRealname(nameInfo);
                }
                LoginModel.savaCacheData();
                UserNameEditBean userNameEditBean=new UserNameEditBean();
                userNameEditBean.setType(type);
                userNameEditBean.setEditMsg(nameInfo);
                EventBus.getDefault().post(userNameEditBean);
                UserNameEdFragment.this.finish();
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
        });
        mTask.start();
    }

    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        hideSoftInput();
    }

    @Override
    protected void destoryData() {
        cancelTask();
        finish();
    }

    private void hideSoftInput(){
        CommonCoreUtil.hideSoftInput(editText);
    }

    private void finish(){
        hideSoftInput();
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        editText=null;
        editMsg=null;
        clearIcon=null;
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


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            hideSoftInput();
        }
    }
}
