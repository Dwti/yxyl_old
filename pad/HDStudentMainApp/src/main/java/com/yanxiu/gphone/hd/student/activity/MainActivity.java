package com.yanxiu.gphone.hd.student.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.FileUtils;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.eventbusbean.TokenInviateBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.bean.GroupEventRefresh;
import com.yanxiu.gphone.hd.student.bean.GroupHwwaitFinishBean;
import com.yanxiu.gphone.hd.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.hd.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.hd.student.bean.PushMsgBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.fragment.BaseFragment;
import com.yanxiu.gphone.hd.student.fragment.LeftTitleFragment;
import com.yanxiu.gphone.hd.student.fragment.NaviFragmentFactory;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.push.PushManagerProxy;
import com.yanxiu.gphone.hd.student.requestTask.RequestGetQReportTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestGroupHwDotNumTask;
import com.yanxiu.gphone.hd.student.upgrade.UpgradeUtils;
import com.yanxiu.gphone.hd.student.utils.QuestionUtils;
import com.yanxiu.gphone.hd.student.utils.ResetManager;
import com.yanxiu.gphone.hd.student.utils.RightContainerUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.upgrade.bean.UpdateDelShareIconsBean;
import com.yanxiu.gphone.upgrade.utils.PublicUpgradeUtils;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/1.
 */
public class MainActivity extends TopViewBaseActivity implements View.OnClickListener,LeftTitleFragment.TitleSelectedListener {
    private static final String TAG=MainActivity.class.getSimpleName();
    public static final int NOTIFICATION_ACTION_HOMEWORK_CORRECTING = 0;//批改作业
    public static final int NOTIFICATION_ACTION_ASSIGN_HOMEWORK = 1;//布置作业
    public static final int NOTIFICATION_ACTION_JOIN_THE_CLASS = 2;//班级加入成功||班级审核未通过
    public static final int NOTIFICATION_ACTION_OPEN_WEBVIEW = 3;//调用内置webView

    private static final String PUSH_MSG_BEAN="mPushMsgBean";

    public static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    // private RequestUserInfoTask mRequestUserInfoTask;

    //-------modified by zengsonghai 2015-12-02-----start
    private int normalNavTxtColor, selNavTxtColor;

    //默认是练习
    private YanXiuConstant.TITLE_ENUM lastSelectIndex = YanXiuConstant.TITLE_ENUM.NO_ENUM;
    //-------modified by zengsonghai 2015-12-02-----end

    private int msg_type = 0;
    private FrameLayout contentMain;
    public FragmentManager fragmentManager;
    /**
     * MainActivity 实体
     */
    private static MainActivity mainInstance;
    public NaviFragmentFactory mNaviFragmentFactory;
    private PushManagerProxy mPushManager;
    private StudentLoadingLayout mLoading;
    private LeftTitleFragment mLeftTitleFragment;
    private RequestGroupHwDotNumTask mRequestGroupHwDotNumTask;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        LogInfo.log(TAG, "-----------onNewIntent----push----");
        judgeToJump(getIntent());
    }

    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected View getContentView() {
        LogInfo.log(TAG, "onCreate");
        EventBus.getDefault().register(this);
        View view=getAttachView(R.layout.activity_btm_navi);
        mLoading=(StudentLoadingLayout)view.findViewById(R.id.loading);
        mainInstance = this;
        fragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        initXGPush();
        ActivityManager.destoryAllActivity();
        findView();
        PublicUpgradeUtils.getInstance().requestInitialize(false, this, LoginModel.getToken(),
                LoginModel.getMobile(), R.layout.update_popupwindow);
        //UpgradeUtils.requestInitialize(false, this);
        LogInfo.log(TAG, "-----------onCreate----push----");
        judgeToJump(getIntent());
        return view;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setVisibility(View.GONE);
    }

    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        contentContainer.setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void setContentListener() {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogInfo.log(TAG,"onDestroy");
        destoryData();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void destoryData() {
        PublicUpgradeUtils.getInstance().cancelUpgrade();
        ResetManager.getInstance().clearInstance();

        mNaviFragmentFactory.resetFragmentContainer();
        fragmentManager=null;
        cancelRequestQuestionListTask();
        cancelRequestGroupHwDotNumTask();
        mNaviFragmentFactory=null;
        mPushManager=null;
        mLeftTitleFragment=null;
        mainInstance = null;
        if(mLoading!=null){
            mLoading.removeAllViews();
            mLoading=null;
        }
        System.gc();
    }

    public void judgeToJump(Intent intent) {
        LogInfo.log("haitian", "----judgeToJump-----0");
        if (intent != null) {
            LogInfo.log("haitian", "----judgeToJump-----1");
            YanXiuConstant.TITLE_ENUM currIndex = mNaviFragmentFactory.getCurrentItem();
            PushMsgBean mPushMsgBean = (PushMsgBean) intent.getSerializableExtra(PUSH_MSG_BEAN);
            if(mPushMsgBean != null) {
                LogInfo.log("haitian", "mPushMsgBean =" + mPushMsgBean.toString());
                msg_type = mPushMsgBean.getMsg_type();//msg_type：0为作业报告，1为学科作业列表，2为作业首页；
                LogInfo.log("haitian", "msg_type =" + msg_type);
                switch (msg_type) {
                    case NOTIFICATION_ACTION_HOMEWORK_CORRECTING:
                        requestQReportList(String.valueOf(mPushMsgBean.getId()));
                        break;
                    case NOTIFICATION_ACTION_ASSIGN_HOMEWORK:
                        //TODO
                        GroupHwActivity.launchActivity(MainActivity.this, mPushMsgBean.getId(), mPushMsgBean.getName());
                        break;
                    case NOTIFICATION_ACTION_JOIN_THE_CLASS:
                        if (currIndex == YanXiuConstant.TITLE_ENUM.HOMEWORK_ENUM) {
                                EventBus.getDefault().post(new GroupEventRefresh());
//                                ((GroupInfoContainerFragment) mNaviFragmentFactory.getItem(YanXiuConstant.TITLE_ENUM
//                                        .HOMEWORK_ENUM)).requestGroupData(false,true);
                        } else {
                            ActivityManager.destoryAllActivity();
                            mLeftTitleFragment.tPushUpdate(YanXiuConstant.TITLE_ENUM.HOMEWORK_ENUM);
                        }
                        break;
                    case NOTIFICATION_ACTION_OPEN_WEBVIEW:
                        ActivityManager.destoryWebviewActivity();
                        LogInfo.log("haitian", "mPushMsgBean.getName()="+mPushMsgBean.getName());
                        WebViewActivity.launch(MainActivity.this, mPushMsgBean.getName(),
                                getString(R.string.app_name));
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public static MainActivity getInstance() {
        return mainInstance;
    }

    public static void launch(Context context, PushMsgBean mPushMsgBean) {
        Intent i = new Intent();
        if (MainActivity.mainInstance != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        i.setClass(context, MainActivity.class);
        if (!(context instanceof Activity)) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        i.putExtra(PUSH_MSG_BEAN, mPushMsgBean);
        context.startActivity(i);
    }



    private void initXGPush() {
        mPushManager=new PushManagerProxy();
        mPushManager.initXGPush();
        mPushManager.setPushNotifyStyle(null);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findView() {
        contentMain = (FrameLayout) findViewById(R.id.content_main);
        contentMain.getViewTreeObserver().addOnGlobalLayoutListener(onGlobal);
        mLeftTitleFragment = (LeftTitleFragment) getSupportFragmentManager().findFragmentById(R.id.titleFragment);
        initNvaBar();
        showCurrentFragment(YanXiuConstant.TITLE_ENUM.EXIST_ENUM);
    }
    ViewTreeObserver.OnGlobalLayoutListener onGlobal=new ViewTreeObserver.OnGlobalLayoutListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            RightContainerUtils.getInstance().measureRightContainer(contentMain);
            if(RightContainerUtils.getInstance().getContainerWidth()>0||RightContainerUtils.getInstance().getContainerHeight()>0){
                LogInfo.log(TAG, "RightContainer w: " + RightContainerUtils.getInstance().getContainerWidth() + "h: " + RightContainerUtils.getInstance().getContainerHeight());
                if(CommonCoreUtil.getSDK()>=16){
                    contentMain.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobal);
                }else{
                    contentMain.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobal);
                }
            }
        }
    };


    private void initNvaBar(){
        selNavTxtColor = getResources().getColor(R.color.color_805500);
        normalNavTxtColor = getResources().getColor(R.color.color_006666);

    }


    private void showCurrentFragment(YanXiuConstant.TITLE_ENUM title_enum) {
        if(title_enum == lastSelectIndex) {
            return;
        }
        requestGroupHwDotNumTask();
        lastSelectIndex = title_enum;

        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        mNaviFragmentFactory.hideAndShowFragment(fragmentManager, title_enum);
    }



    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    private Handler handler = new Handler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        BaseFragment mBaseFg= (BaseFragment) mNaviFragmentFactory.getCurrentItemFg();
        if(mBaseFg.onKeyDown(keyCode,event)){
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    Util.showToast(R.string.app_exit_tip);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 2000);
                    break;
                case 1:
                    exitProcess();
                    break;
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 退出应用处理
     */
    private void exitProcess() {
        finish();
        YanxiuApplication.getInstance().exitProcess();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        LogInfo.log(TAG, "resultCode = " + resultCode + " ,requestCode = " + requestCode);
        mNaviFragmentFactory.getCurrentItemFg().onActivityResult(requestCode, resultCode, data);
    }

    private RequestGetQReportTask requestQuestionListTask = null;

    private void cancelRequestQuestionListTask() {
        if (requestQuestionListTask != null) {
            requestQuestionListTask.cancel();
        }
        requestQuestionListTask = null;
    }

    /**
     * 请求作业报告接口
     */
    private void requestQReportList(String paperId) {
        if(!TextUtils.isEmpty(paperId)) {
            mLoading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
            cancelRequestQuestionListTask();
            requestQuestionListTask = new RequestGetQReportTask(this, paperId, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    mLoading.setViewGone();
                    SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                    if (subjectExercisesItemBean != null && subjectExercisesItemBean.getData() != null
                            && subjectExercisesItemBean.getData().size() > 0 && subjectExercisesItemBean.getData().get(0).getPaperTest() != null) {
                        subjectExercisesItemBean.setIsResolution(true);
                        QuestionUtils.initDataWithAnswer(
                                subjectExercisesItemBean);
                        AnswerReportActivity.launch(MainActivity.this,
                                subjectExercisesItemBean, YanXiuConstant.HOMEWORK_REPORT,Intent.FLAG_ACTIVITY_FORWARD_RESULT, true);
                    } else {
                        if (subjectExercisesItemBean.getStatus() == null) {
                            Util.showUserToast(R.string.net_null, -1, -1);
                        } else {
                            Util.showUserToast(subjectExercisesItemBean.getStatus().getDesc(), null, null);
                        }
                    }
                }

                @Override
                public void dataError(int type, String msg) {
                    mLoading.setViewGone();
                    if (!StringUtils.isEmpty(msg)) {
                        Util.showUserToast(msg, null, null);
                    } else {
                        Util.showUserToast(R.string.net_null, -1, -1);
                    }
                }
            });
            requestQuestionListTask.start();
        }
    }


    public void onEventMainThread(TokenInviateBean tokenInviateBean){
        LoginActivity.launcherActivity(this,
                LoginActivity.TOKEN_INVADALITE);
        PublicErrorQuestionCollectionBean.deleteAllData();
        PreferencesManager.getInstance().clearSubjectSection();
        YanxiuApplication.getInstance().setSubjectVersionBean(null);
        ActivityManager.destoryAllActivityOFFLogin();
        PublicFavouriteQuestionBean.deleteAllData();
    }

    @Override
    public void onTitleSelected(YanXiuConstant.TITLE_ENUM title_enum) {
        if (mNaviFragmentFactory.getCurrentItem() != title_enum) {
            LogInfo.log(TAG,"onTitleSelected title_enum : "+title_enum);
            showCurrentFragment(title_enum);
        }
    }


    private void requestGroupHwDotNumTask(){
        cancelRequestGroupHwDotNumTask();
        mRequestGroupHwDotNumTask = new RequestGroupHwDotNumTask(MainActivity.this, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                GroupHwwaitFinishBean bean = (GroupHwwaitFinishBean) result;
                mLeftTitleFragment.updateGroupRedDot(bean.getProperty().getPaperNum());
            }
            @Override
            public void dataError (int type, String msg) {

            }
        });
        mRequestGroupHwDotNumTask.start();
    }
    private void cancelRequestGroupHwDotNumTask(){
        if(mRequestGroupHwDotNumTask != null){
            mRequestGroupHwDotNumTask.cancel();
        }
        mRequestGroupHwDotNumTask = null;
    }
    public void onEventMainThread(GroupHwwaitFinishBean mGroupHwwaitFinishBean){
        requestGroupHwDotNumTask();
    }

    /**
     * 处理升级完设置参数
     * @param mBean
     */
    public void onEventMainThread(UpdateDelShareIconsBean mBean){
        if(mBean.getTodoType() == UpdateDelShareIconsBean.FORCE_UPGRADE_CONSTANT){//设置强制升级参数，以便推送识别
            YanxiuApplication.getInstance().setIsForceUpdate(true);
        } else {
            PreferencesManager.getInstance().setFristApp(true);//installApk function
            FileUtils.RecursionDeleteFile(new File(YanXiuConstant.SHARE_ICON_PATH));
        }
        LogInfo.log(TAG,"UpdateDelShareIconsBean");
    }
}
