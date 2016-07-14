package com.yanxiu.gphone.student.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.UserInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MyStageSelectActivity;
import com.yanxiu.gphone.student.activity.MyUserInfoActivity;
import com.yanxiu.gphone.student.activity.TeachingMaterialActivity;
import com.yanxiu.gphone.student.activity.UserSettingActivity;
import com.yanxiu.gphone.student.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;


/**
 * Created by Administrator on 2015/7/7.
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    private static final String TAG=MyFragment.class.getSimpleName();
    private View rootView;
    private RoundedImageView userHeadIv;
    private View userInfoLayout;
    private View myFavouriteLayout;
    private View practiceHistoryLayout;
    private View errorCollectionLayout;
    private View myStageLayout;
    private TextView myStage;
    private TextView userName;
    private View teachingMaterialLayout;
    private View settingLayout;
    private View feedbackLayout;
    private TextView stdUidTv;
    private UserInfo mUserinfoEntity = LoginModel.getUserinfoEntity();
    private int stageId = MyStageSelectActivity.STAGE_TYPE_JUIN;
    private TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_fragment, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
    }

//    private void setListener() {
//        myStage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
//
//
//
//    }

    private void findView() {
        titleView = (TextView) rootView.findViewById(R.id.main_public_top_my).findViewById(R.id
                .public_layout_top_tv);
        titleView.setText(R.string.navi_tbm_my);
        userName = (TextView) rootView.findViewById(R.id.user_name);
        myStage = (TextView) rootView.findViewById(R.id.my_stage_content);

        stdUidTv=(TextView)rootView.findViewById(R.id.stdUidTv);
        UserInfo userInfo= (UserInfo) LoginModel.getRoleUserInfoEntity();
        stdUidTv.setText(String.format(getResources().getString(R.string.std_uid_text),String.valueOf(userInfo.getMobile())));

        userHeadIv = (RoundedImageView) rootView.findViewById(R.id.user_icon);
        userHeadIv.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_12));
        if(mUserinfoEntity != null){
            LogInfo.log(TAG, "mUserinfoEntity.getHead(): " + mUserinfoEntity.getHead());
            UniversalImageLoadTool.disPlay(mUserinfoEntity.getHead(),
                    new RotateImageViewAware(userHeadIv, mUserinfoEntity.getHead()), R.drawable.user_info_default_bg);
            userName.setText(mUserinfoEntity.getNickname());
            stageId = mUserinfoEntity.getStageid();
            setStageTxt(stageId);
        }
        userInfoLayout = rootView.findViewById(R.id.user_info_layout);
        myFavouriteLayout = rootView.findViewById(R.id.my_favourite_layout);
        practiceHistoryLayout = rootView.findViewById(R.id.practice_history_layout);
        errorCollectionLayout = rootView.findViewById(R.id.error_collection_layout);
        myStageLayout = rootView.findViewById(R.id.my_stage_layout);
        teachingMaterialLayout = rootView.findViewById(R.id.teaching_material_layout);
        settingLayout = rootView.findViewById(R.id.my_setting_layout);
        feedbackLayout=rootView.findViewById(R.id.feedback_layout);

        userInfoLayout.setOnClickListener(this);
        myFavouriteLayout.setOnClickListener(this);
        practiceHistoryLayout.setOnClickListener(this);
        errorCollectionLayout.setOnClickListener(this);
        myStageLayout.setOnClickListener(this);
        teachingMaterialLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.user_info_layout: //用户信息
                MyUserInfoActivity.launchActivity(getActivity());
                break;
            case R.id.my_favourite_layout: //我的收藏
                TeachingMaterialActivity.launchActivity(getActivity(), TeachingMaterialActivity.MY_FAVOURITE_COLLECTION_ACTIVITY);
                break;
            case R.id.practice_history_layout://练习历史
                TeachingMaterialActivity.launchActivity(getActivity(), TeachingMaterialActivity.PRACTICE_HISTORY_ACTIVITY);
                break;
            case R.id.error_collection_layout://错题集
                TeachingMaterialActivity.launchActivity(getActivity(), TeachingMaterialActivity.PRACTICE_ERROR_COLLECTION_ACTIVITY);
                break;
            case R.id.my_stage_layout://学段
                MyStageSelectActivity.launch(getActivity(), stageId);
                break;
            case R.id.teaching_material_layout: //教材版本
                TeachingMaterialActivity.launchActivity(getActivity(), TeachingMaterialActivity.TEACHING_MATERIAL_ACTIVITY);
                break;
            case R.id.my_setting_layout: //设置
                UserSettingActivity.launchActivity(getActivity());
                break;
            case R.id.feedback_layout:  //意见反馈
                ActivityJumpUtils.jumpToFeedBackActivity(getActivity(), AbstractFeedBack.ADVICE_FEED_BAck);
                break;
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogInfo.log("haitian", ">>>>>>>>>>>------requestCode="+requestCode);
        mUserinfoEntity = LoginModel.getUserinfoEntity();
        if (requestCode == MyStageSelectActivity.STAGE_REQUEST_CODE) {
            if (data != null && resultCode == Activity.RESULT_OK) {
                stageId = data.getIntExtra("type", 0);
                LogInfo.log("haitian", "stageId ="+stageId);
                setStageTxt(stageId);
            }
        } else if(requestCode == MyUserInfoActivity.MY_USERINFO_REQUESTCODE){
            if (data != null && resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("name");
                String urlicon = data.getStringExtra("urlicon");
                String headPath = data.getStringExtra("headPath");
                LogInfo.log("haitian", "name="+name);
                if(!TextUtils.isEmpty(name)){
                    userName.setText(name);
                }
                if(!TextUtils.isEmpty(urlicon)){

                    UniversalImageLoadTool.disPlay(urlicon,
                            new RotateImageViewAware(userHeadIv, urlicon), R.drawable.user_info_default_bg);
                } else if(!TextUtils.isEmpty(headPath)){
                    userHeadIv.setImageBitmap(CommonCoreUtil.getImage(headPath));
                }
            }
        }
    }

    private void setStageTxt(int type) {
        switch (type){
            case MyStageSelectActivity.STAGE_TYPE_PRIM:
                myStage.setText(R.string.primary_txt);
                break;
            case MyStageSelectActivity.STAGE_TYPE_JUIN:
                myStage.setText(R.string.juinor_txt);
                break;
            case MyStageSelectActivity.STAGE_TYPE_HIGH:
                myStage.setText(R.string.high_txt);
                break;
            default:
                myStage.setText("");
                break;
        }

    }
}
