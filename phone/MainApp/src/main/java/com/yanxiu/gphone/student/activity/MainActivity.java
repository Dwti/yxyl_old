package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.core.view.badgeview.BadgeView;
import com.common.login.LoginModel;
import com.common.login.eventbusbean.TokenInviateBean;
import com.igexin.sdk.PushManager;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.GroupHwwaitFinishBean;
import com.yanxiu.gphone.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.student.bean.PushMsgBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.GroupFragment;
import com.yanxiu.gphone.student.fragment.HomeWorkFragment;
import com.yanxiu.gphone.student.fragment.NaviFragmentFactory;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.manager.ActivityManager;
import com.yanxiu.gphone.student.preference.PreferencesManager;
import com.yanxiu.gphone.student.push.PushManagerProxy;
import com.yanxiu.gphone.student.requestTask.RequestGetQReportTask;
import com.yanxiu.gphone.student.requestTask.RequestGroupHwDotNumTask;
import com.yanxiu.gphone.student.upgrade.UpgradeUtils;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/1.
 */
public class MainActivity extends YanxiuBaseActivity implements View.OnClickListener {
    public static final int NOTIFICATION_ACTION_HOMEWORK_CORRECTING = 0;//批改作业
    public static final int NOTIFICATION_ACTION_ASSIGN_HOMEWORK = 1;//布置作业
    public static final int NOTIFICATION_ACTION_JOIN_THE_CLASS = 2;//班级加入成功||班级审核未通过
    public static final int NOTIFICATION_ACTION_OPEN_WEBVIEW = 3;//调用内置webView

    public static void launchActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void LaunchActivityToIndex(Activity activity,int type){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("type",type);
        activity.startActivity(intent);
    }

    // private RequestUserInfoTask mRequestUserInfoTask;
    private PublicLoadLayout rootView;
    //-------modified by zengsonghai 2015-12-02-----start
    private int normalNavTxtColor, selNavTxtColor;
    private View[] navBarViews = new View[3];
    private ImageView[] navIconViews = new ImageView[3];
    private TextView[] navTextViews = new TextView[3];
    private BadgeView badgeGroup;
    private int lastSelectIndex = -1;
    //-------modified by zengsonghai 2015-12-02-----end
    private final int INDEX_HOMEWORK = 0;
    private final int INDEX_GROUP = 1;
    private final int INDEX_MY = 2;
    private int msg_type = 0;
    private FrameLayout contentMain;
    public FragmentManager fragmentManager;
    /**
     * MainActivity 实体
     */
    private static MainActivity mainInstance;
    public NaviFragmentFactory mNaviFragmentFactory;
    private PushManagerProxy mPushManager;


    private RequestGroupHwDotNumTask mRequestGroupHwDotNumTask;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
        LogInfo.log("haitian", "-----------onNewIntent----push----");
        judgeToJump(getIntent());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushManager.getInstance().initialize(this.getApplicationContext());
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_btm_navi);
        setContentView(rootView);
        EventBus.getDefault().register(this);
        mainInstance = this;
        //initXGPush();
        fragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        ActivityManager.destoryAllActivity();
        findView();
        UpgradeUtils.requestInitialize(false, this);
        LogInfo.log("haitian", "-----------onCreate----push----");
        judgeToJump(getIntent());
//        PublicErrorQuestionCollectionBean.createDataForErrorCollection();
        PushManager.getInstance().bindAlias(this.getApplicationContext(), String.valueOf(LoginModel.getUid()));
    }

    public void judgeToJump(Intent intent) {
        if (intent != null) {
            int currIndex = mNaviFragmentFactory.getCurrentItem();
            PushMsgBean mPushMsgBean = (PushMsgBean) intent.getSerializableExtra("mPushMsgBean");
            if(mPushMsgBean != null) {
                msg_type = mPushMsgBean.getMsg_type();//msg_type：0为作业报告，1为学科作业列表，2为作业首页；
                LogInfo.log("haitian", "msg_type =" + msg_type);
                switch (msg_type) {
                    case NOTIFICATION_ACTION_HOMEWORK_CORRECTING:
                        requestQReportList(String.valueOf(mPushMsgBean.getId()));
                        break;
                    case NOTIFICATION_ACTION_ASSIGN_HOMEWORK:
                        GroupHwActivity.launchActivity(MainActivity.this, mPushMsgBean.getId(), mPushMsgBean.getName());
                        break;
                    case NOTIFICATION_ACTION_JOIN_THE_CLASS:
                        if (currIndex == INDEX_GROUP) {
                            if (mNaviFragmentFactory.getCurrentItem() == 1) {
                                LogInfo.log("king", "mNaviFragmentFactory");
                                ((GroupFragment) mNaviFragmentFactory.getItem(1)).requestGroupData(false,true);
                            }
                        } else {
                            ActivityManager.destoryAllActivity();
                            showCurrentFragment(INDEX_GROUP);
                        }
                        break;
                    case NOTIFICATION_ACTION_OPEN_WEBVIEW:
                        ActivityManager.destoryWebviewActivity();
                        WebViewActivity.launch(MainActivity.this, mPushMsgBean.getName(),
                                getString(R.string.app_name));
                        break;
                    default:
                        break;
                }
            }
            int type=intent.getIntExtra("type",0);
            if (type==1){
                showCurrentFragment(type);
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
        i.putExtra("mPushMsgBean", mPushMsgBean);
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
        if(badgeGroup != null) {
            requestGroupHwDotNumTask();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findView() {
        contentMain = (FrameLayout) findViewById(R.id.content_main);
        initNvaBar();
        showCurrentFragment(0);
    }
    private void initNvaBar(){
        selNavTxtColor = getResources().getColor(R.color.color_805500);
        normalNavTxtColor = getResources().getColor(R.color.color_006666);
        navBarViews[0] = findViewById(R.id.navi_homework);
        navBarViews[1] = findViewById(R.id.navi_group);
        navBarViews[2] = findViewById(R.id.navi_my);
        for (int i=0; i<3; i++) {
            navBarViews[i].setOnClickListener(this);
            navIconViews[i] = (ImageView) navBarViews[i].findViewById(R.id.nav_icon);
            navTextViews[i] = (TextView) navBarViews[i].findViewById(R.id.nav_txt);
        }
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, navTextViews[0],
                navTextViews[1], navTextViews[2]);
        navTextViews[0].setText(R.string.exe);
        navTextViews[1].setText(R.string.navi_tbm_group);
        navTextViews[2].setText(R.string.navi_tbm_my);
    }
    @Override
    public void onClick (View view) {
        int curItem = INDEX_HOMEWORK;
        switch (view.getId()){
            case R.id.navi_homework:
                curItem = INDEX_HOMEWORK;
                break;
            case R.id.navi_group:
                curItem = INDEX_GROUP;
                break;
            case R.id.navi_my:
                curItem = INDEX_MY;
                break;
            default:break;
        }
        if (mNaviFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }
    private void checkNavBarProcess(int index){
        if(index>=0 && index<3) {
            resetNavBar();
            navBarViews[index].setBackgroundResource(R.drawable.home_nav_bar_sel);
            navTextViews[index].setTextColor(selNavTxtColor);
            navTextViews[index].setShadowLayer(2, 0, 2, getResources().getColor(R.color
                    .color_ffff99));
            switch (index) {
                case INDEX_HOMEWORK:
                    navIconViews[0].setBackgroundResource(R.drawable.navi_main_selected);
                    break;
                case INDEX_GROUP:
                    navIconViews[1].setBackgroundResource(R.drawable.navi_group_selected);
                    break;
                case INDEX_MY:
                    navIconViews[2].setBackgroundResource(R.drawable.navi_my_selected);
                    break;
            }
        }
    }
    private void resetNavBar(){
        for (int i=0; i<3; i++){
            navBarViews[i].setBackgroundResource(R.drawable.home_nav_bar_nor);
            navTextViews[i].setTextColor(normalNavTxtColor);
            navTextViews[i].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_33ffff));
        }
        navIconViews[0].setBackgroundResource(R.drawable.navi_main_normal);
        navIconViews[1].setBackgroundResource(R.drawable.navi_group_normal);
        navIconViews[2].setBackgroundResource(R.drawable.navi_my_normal);

    }
    private void checkButton(int curItem) {
        switch (curItem) {
            case INDEX_HOMEWORK:
                break;
            case INDEX_GROUP:
                break;
            case INDEX_MY:
                break;
        }
    }
    private void showCurrentFragment(int index) {
        if(index == lastSelectIndex) {
            return;
        }
        lastSelectIndex = index;
        checkNavBarProcess(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        if(index != INDEX_GROUP){
            requestGroupHwDotNumTask();
        }
        mNaviFragmentFactory.hideAndShowFragment(fragmentManager, index);
    }

    private void requestGroupHwDotNumTask(){
        cancelRequestGroupHwDotNumTask();
        mRequestGroupHwDotNumTask = new RequestGroupHwDotNumTask(MainActivity.this, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                GroupHwwaitFinishBean bean = (GroupHwwaitFinishBean) result;
                if(badgeGroup == null) {
                    badgeGroup = new BadgeView(MainActivity.this);
                    badgeGroup.setTargetView(navBarViews[1]);
                    badgeGroup.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
                    badgeGroup.setBackgroundResource(R.drawable.bg_oval_with_gradient);
                    badgeGroup.setBadgeTextSize(12);
                }
                badgeGroup.setBadgeCount(bean.getProperty().getPaperNum());
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cancelRequestQuestionListTask();
        cancelRequestGroupHwDotNumTask();
        mainInstance = null;
//        unregisterReceiver(updatePushReceiver);
//        cancelUserInfoTask();
    }

    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    private Handler handler = new Handler();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        LogInfo.log("king", "resultCode = " + resultCode + " ,requestCode = " + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SubjectVersionActivity.LAUNCHER_SUBJECT_VERSION_ACTIVITY:
                    LogInfo.log("king", "SubjectVersionActivity");
                    Fragment fragment = mNaviFragmentFactory.getItem(mNaviFragmentFactory.getCurrentItem());
                    if (fragment instanceof HomeWorkFragment && data != null) {
                        HomeWorkFragment homeFragment = (HomeWorkFragment) fragment;
                        Bundle bundle = data.getBundleExtra("data");
                        SubjectEditionBean.DataEntity dataBean = (SubjectEditionBean.DataEntity) bundle.getSerializable("bean");
                        homeFragment.refreshData(dataBean);
                    }
                    break;
                case MyStageSelectActivity.STAGE_REQUEST_CODE:
                case MyUserInfoActivity.MY_USERINFO_REQUESTCODE:
                    LogInfo.log("haitian", "xxx------requestCode=" + requestCode);
                    if (mNaviFragmentFactory.getCurrentItem() == 2) {
                        mNaviFragmentFactory.getItem(2).onActivityResult(requestCode, resultCode, data);
                    }
                    break;
                case GroupHwActivity.LAUNCHER_GROUP_HW:
                case GroupHwUndoActivity.LAUNCHER_GROUP_HW_UNDO:
                case AddGroupActivity.LAUNCHER_ADDGROUPACTIVITY:
                case GroupInfoActivity.CANCEL_OR_EXIT_CLASSS_FOR_ACTIVITY:
                    if (mNaviFragmentFactory.getCurrentItem() == 1) {
                        mNaviFragmentFactory.getItem(1).onActivityResult(requestCode, resultCode, data);
                    }
                    break;
            }
        }
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
            rootView.loading(true);
            cancelRequestQuestionListTask();
            requestQuestionListTask = new RequestGetQReportTask(this, paperId, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    rootView.finish();
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
                    rootView.finish();
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
        //ActivityJumpUtils.jumpToLoginChoiceActivity(this);
        LoginActivity.launcherActivity(this, 0);
        PublicErrorQuestionCollectionBean.deleteAllData();
        PreferencesManager.getInstance().clearSubjectSection();
        YanxiuApplication.getInstance().setSubjectVersionBean(null);
        ActivityManager.destoryAllActivityOFFLogin();
        PublicFavouriteQuestionBean.deleteAllData();
    }
    public void onEventMainThread(GroupHwwaitFinishBean groupHwwaitFinishBean){
        requestGroupHwDotNumTask();
    }
}
