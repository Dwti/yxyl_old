package com.yanxiu.gphone.hd.student.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.common.login.model.UserInfo;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.MyStageSelectActivity;
import com.yanxiu.gphone.hd.student.eventbusbean.StageBean;
import com.yanxiu.gphone.hd.student.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.upgrade.utils.PublicUpgradeUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/2.
 */
public class SetFragment extends TopBaseFragment {

    private static final String TAG = SetFragment.class.getSimpleName();

    private SetContainerFragment mFg;

    private int stageId = StageFragment.STAGE_TYPE_JUIN;

    private View modifyPwdView, stageView, bookVersionView, adviceFeedView,updateView, aboutUsView, privacyView, loginOutView;

    private TextView stageTv;

    private ScalpelFrameLayout mScalpelView;

    private View view;

    private BaseFragment mCurFg;


    public static Fragment newInstance () {

        return new SetFragment();
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        leftView.setVisibility(View.GONE);
        titleText.setText(R.string.my_setting_name);
    }

    @Override
    protected void setRootView () {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected boolean isAttach () {
        return true;
    }

    @Override
    protected View getContentView () {
        EventBus.getDefault().register(this);
        mFg = (SetContainerFragment) getParentFragment();
        view = getAttachView(R.layout.activity_setting_user_layout);
        findView();
        updateStageInfo();
        return view;
    }

    @Override
    protected void initLoadData () {

    }


    private void updateStageInfo () {
        UserInfo mUserinfoEntity = LoginModel.getUserinfoEntity();
        if (mUserinfoEntity != null) {
            stageId = mUserinfoEntity.getStageid();
            setStageTxt(stageId);
        }
    }

    private void setStageTxt (int type) {
        switch (type) {
            case StageFragment.STAGE_TYPE_PRIM:
                stageTv.setText(R.string.primary_txt);
                break;
            case StageFragment.STAGE_TYPE_JUIN:
                stageTv.setText(R.string.juinor_txt);
                break;
            case StageFragment.STAGE_TYPE_HIGH:
                stageTv.setText(R.string.high_txt);
                break;
            default:
                stageTv.setText("");
                break;
        }

    }

    private void findView () {
        modifyPwdView = view.findViewById(R.id.setting_modify_pwd_layout);

        stageView = view.findViewById(R.id.setting_stage_layout);

        bookVersionView = view.findViewById(R.id.book_version_layout);

        adviceFeedView = view.findViewById(R.id.advicefeedback_layout);

        privacyView = view.findViewById(R.id.privacy_layout);

        updateView=view.findViewById(R.id.setting_update_layout);

        aboutUsView = view.findViewById(R.id.setting_about_us_layout);

        loginOutView = view.findViewById(R.id.login_out_layout);


        TextView modifyTV = (TextView) modifyPwdView.findViewById(R.id.name);

        stageTv = (TextView) stageView.findViewById(R.id.name);

        TextView bookVersionTv = (TextView) bookVersionView.findViewById(R.id.name);

        TextView adviceFeedTv = (TextView) adviceFeedView.findViewById(R.id.name);

        TextView privacyTv = (TextView) privacyView.findViewById(R.id.name);

        TextView updateTv = (TextView) updateView.findViewById(R.id.name);

        TextView aboutUsTv = (TextView) aboutUsView.findViewById(R.id.name);

        TextView loginOutTv = (TextView) loginOutView.findViewById(R.id.name);


        ImageView modifyLeftIcon = (ImageView) modifyPwdView.findViewById(R.id.left_icon);
        modifyLeftIcon.setVisibility(View.GONE);

        ImageView updateIcon=(ImageView)updateView.findViewById(R.id.left_icon);
        updateIcon.setVisibility(View.GONE);

        ImageView aboutUsLeftIcon = (ImageView) aboutUsView.findViewById(R.id.left_icon);
        aboutUsLeftIcon.setVisibility(View.GONE);


        ImageView stageLeftIcon = (ImageView) stageView.findViewById(R.id.left_icon);
        stageLeftIcon.setVisibility(View.GONE);

        ImageView bookVersionLeftIcon = (ImageView) bookVersionView.findViewById(R.id.left_icon);
        bookVersionLeftIcon.setVisibility(View.GONE);


        ImageView adviceFeedLeftIcon = (ImageView) adviceFeedView.findViewById(R.id.left_icon);
        adviceFeedLeftIcon.setVisibility(View.GONE);

        ImageView privacyLeftIcon = (ImageView) privacyView.findViewById(R.id.left_icon);
        privacyLeftIcon.setVisibility(View.GONE);


        ImageView loginOutImgage = (ImageView) loginOutView.findViewById(R.id.left_icon);
        loginOutImgage.setVisibility(View.GONE);


        ImageView modifyRightIcon = (ImageView) modifyPwdView.findViewById(R.id.right_arrow);
        modifyRightIcon.setVisibility(View.VISIBLE);

        ImageView aboutUsRightIcon = (ImageView) aboutUsView.findViewById(R.id.right_arrow);
        aboutUsRightIcon.setVisibility(View.VISIBLE);

        ImageView updateRIcon=(ImageView)updateView.findViewById(R.id.right_arrow);
        updateRIcon.setVisibility(View.VISIBLE);

        ImageView stageRightIcon = (ImageView) stageView.findViewById(R.id.right_arrow);
        stageRightIcon.setVisibility(View.VISIBLE);

        ImageView bookVersionRightIcon = (ImageView) bookVersionView.findViewById(R.id.right_arrow);
        bookVersionRightIcon.setVisibility(View.VISIBLE);


        ImageView adviceFeedRightIcon = (ImageView) adviceFeedView.findViewById(R.id.right_arrow);
        adviceFeedRightIcon.setVisibility(View.VISIBLE);

        ImageView privacyRightIcon = (ImageView) privacyView.findViewById(R.id.right_arrow);
        privacyRightIcon.setVisibility(View.VISIBLE);


        ImageView loginOutRightIcon = (ImageView) loginOutView.findViewById(R.id.right_arrow);
        loginOutRightIcon.setVisibility(View.VISIBLE);

        modifyTV.setText(R.string.modify_pwd_txt);

        updateTv.setText(R.string.update_version_txt);

        aboutUsTv.setText(R.string.about_us_txt);

        stageTv.setText(R.string.edit_stage);

        bookVersionTv.setText(R.string.teaching_material_name);

        adviceFeedTv.setText(R.string.feedback_title);

        privacyTv.setText(R.string.privacy_policy_txt);

        loginOutTv.setText(getResources().getString(R.string.login_out_txt));

        if (PreferencesManager.getInstance().getIsThirdLogIn()) {
            modifyPwdView.setVisibility(View.GONE);
            view.findViewById(R.id.modify_pwd_divider_line).setVisibility(View.GONE);
        }
    }

    @Override
    protected void setContentListener () {

        modifyPwdView.setOnClickListener(this);

        aboutUsView.setOnClickListener(this);

        stageView.setOnClickListener(this);

        bookVersionView.setOnClickListener(this);

        adviceFeedView.setOnClickListener(this);

        privacyView.setOnClickListener(this);

        loginOutView.setOnClickListener(this);

        updateView.setOnClickListener(this);
    }

    @Override
    protected void destoryData () {
        PublicUpgradeUtils.getInstance().cancelUpgrade();
    }

    @Override
    public void onDestroy () {
        EventBus.getDefault().unregister(this);
        PublicUpgradeUtils.getInstance().cancelUpgrade();
        super.onDestroy();
        mFg=null;
        modifyPwdView=null;
        stageView=null;
        bookVersionView=null;
        adviceFeedView=null;
        updateView=null;
        aboutUsView=null;
        privacyView=null;
        loginOutView=null;
        stageTv=null;
        mScalpelView=null;
        view=null;
        mCurFg=null;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.setting_modify_pwd_layout:
                mCurFg = (BaseFragment) ModifyPwFragment.newInstance();
                mFg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.setting_stage_layout:
                mCurFg = (BaseFragment) StageFragment.newInstance(stageId);
                mFg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.book_version_layout:
                mCurFg = (BaseFragment) TeachingMaterialFragment.newInstance();
                mFg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.advicefeedback_layout:
                mCurFg = (BaseFragment) FeedBackFragment.newInstance(AbstractFeedBack.ADVICE_FEED_BAck);
                mFg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.privacy_layout:
                mCurFg = (BaseFragment) WebFragment.newInstance(YanXiuConstant.PRIVACY_POLICY_URL);
                if (mCurFg == null) {
                    return;
                }
                mFg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.setting_about_us_layout:
                mCurFg = (BaseFragment) AboutFragment.newInstance();
                mFg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.login_out_layout:
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                LoginModel.loginOut();
                break;
            case R.id.setting_update_layout:
//                UpgradeUtils.requestInitialize(true, getActivity());
                PublicUpgradeUtils.getInstance().requestInitialize(true, getActivity(), LoginModel
                                .getToken(),
                        LoginModel.getMobile(), R.layout.update_popupwindow);
                break;
        }
    }
    public void onEventMainThread (StageBean stageBean) {
        updateStageInfo();
        //更新主页面
//        EventBus.getDefault().post(new ExerciseEventBusBean());
    }
    @Override
    protected IFgManager getFragmentManagerFromSubClass () {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass () {
        return 0;
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        LogInfo.log(TAG, "onKeyDown");
        if (mCurFg != null) {
            return mCurFg.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(mCurFg!=null){
            mCurFg.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onReset() {
        LogInfo.log(TAG,"onReset");
        destoryData();
    }


}
