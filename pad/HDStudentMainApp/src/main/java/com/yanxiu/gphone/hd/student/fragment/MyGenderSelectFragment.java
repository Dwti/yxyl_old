package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.eventbusbean.MyGenderSelectBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestUpdateUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/1.
 */
public class MyGenderSelectFragment extends  TopBaseFragment {

    private static final String TAG=MyGenderSelectFragment.class.getSimpleName();

    private static final String SEX_TYPE_KEY="sex_type";

    private MyUserInfoContainerFragment fg;

    private View  male_layout, female_layout;
    private ImageView maleIv;
    private ImageView femaleIv;

    private RequestUpdateUserInfoTask mTask;

    private int type;

    private static MyGenderSelectFragment myGenderSelectFragment;

    public static Fragment newInstance(int type){
        if(myGenderSelectFragment==null){
            myGenderSelectFragment=new MyGenderSelectFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SEX_TYPE_KEY,type);
            myGenderSelectFragment.setArguments(bundle);
        }
        return myGenderSelectFragment;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.user_gender_str);
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        fg= (MyUserInfoContainerFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.activity_gender_select_my_layout);
        type=getArguments().getInt(SEX_TYPE_KEY);
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {

    }

    private void findView(){
        male_layout = mPublicLayout.findViewById(R.id.male_layout);
        female_layout = mPublicLayout.findViewById(R.id.female_layout);

        TextView maleTxt = (TextView) male_layout.findViewById(R.id.name);
        TextView femaleTxt = (TextView) female_layout.findViewById(R.id.name);

        maleTxt.setText(R.string.male_txt);
        femaleTxt.setText(R.string.female_txt);

        maleIv = (ImageView) male_layout.findViewById(R.id.right_arrow);

        ImageView maleLeftIcon = (ImageView) male_layout.findViewById(R.id.left_icon);
        maleLeftIcon.setImageResource(R.drawable.male_icon);
        femaleIv = (ImageView) female_layout.findViewById(R.id.right_arrow);

        ImageView femaleRightIcon = (ImageView) female_layout.findViewById(R.id.left_icon);
        femaleRightIcon.setImageResource(R.drawable.female_icon);
        if(type == YanXiuConstant.Gender.GENDER_TYPE_MALE){
            maleIv.setVisibility(View.VISIBLE);
            femaleIv.setVisibility(View.GONE);
        } else if(type == YanXiuConstant.Gender.GENDER_TYPE_FEMALE){
            femaleIv.setVisibility(View.VISIBLE);
            maleIv.setVisibility(View.GONE);
        }
    }

    private void setSelectedView(View view, int type){
        maleIv.setVisibility(View.GONE);
        femaleIv.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        updateInfo(type);

    }

    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }


    private void updateInfo(final int type){
        mPublicLayout.loading(true);
        HashMap<String, String> hashMap = new HashMap<>();
        if(type == YanXiuConstant.Gender. GENDER_TYPE_MALE){
            hashMap.put("sex", String.valueOf(YanXiuConstant.Gender. GENDER_TYPE_MALE));
        } else if(type == YanXiuConstant.Gender. GENDER_TYPE_FEMALE){
            hashMap.put("sex", String.valueOf(YanXiuConstant.Gender. GENDER_TYPE_FEMALE));
        } else {
            hashMap.put("sex", String.valueOf(YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN));
        }
        cancelTask();
        mTask=new RequestUpdateUserInfoTask(getActivity(), hashMap, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                if(type == YanXiuConstant.Gender.GENDER_TYPE_MALE){
                    if( LoginModel.getUserinfoEntity()!=null){
                        LoginModel.getUserinfoEntity().setSex( String.valueOf(YanXiuConstant.Gender.GENDER_TYPE_MALE));
                    }
                } else if(type == YanXiuConstant.Gender.GENDER_TYPE_FEMALE){
                    if(LoginModel.getUserinfoEntity()!=null){
                        LoginModel.getUserinfoEntity().setSex(String.valueOf(YanXiuConstant.Gender.GENDER_TYPE_FEMALE));
                    }

                } else {
                    if( LoginModel.getUserinfoEntity()!=null){
                        LoginModel.getUserinfoEntity().setSex(String.valueOf(YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN));
                    }
                }
                LoginModel.savaCacheData();
                MyGenderSelectBean myGenderSelectBean=new MyGenderSelectBean();
                LogInfo.log(TAG, "---type =" + type);
                myGenderSelectBean.setType(type);
                EventBus.getDefault().post(myGenderSelectBean);
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
        });
        mTask.start();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.male_layout:
                setSelectedView(maleIv,YanXiuConstant.Gender.GENDER_TYPE_MALE);
                break;
            case R.id.female_layout:
                setSelectedView(femaleIv, YanXiuConstant.Gender.GENDER_TYPE_FEMALE);
                break;
        }
    }

    @Override
    protected void setContentListener() {
        male_layout.setOnClickListener(this);
        female_layout.setOnClickListener(this);
    }

    @Override
    protected void destoryData() {
        cancelTask();
        finish();
    }

    private void finish(){
        if(fg!=null&&fg.mIFgManager!=null){
            fg.mIFgManager.popStack();
        }
        myGenderSelectFragment=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fg=null;
        male_layout=null;
        female_layout=null;
        maleIv=null;
        femaleIv=null;
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
