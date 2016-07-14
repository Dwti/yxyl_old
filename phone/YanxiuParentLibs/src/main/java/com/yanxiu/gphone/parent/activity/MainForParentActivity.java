package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.manage.CommonActivityManager;
import com.common.core.utils.FileUtils;
import com.common.core.utils.LogInfo;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.eventbusbean.TokenInviateBean;
import com.common.share.ShareManager;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.YanxiuParentBaseActivity;
import com.yanxiu.gphone.parent.bean.ParentRemindBean;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.eventbus.bean.HonorRedFlagEventBean;
import com.yanxiu.gphone.parent.eventbus.bean.ParentJumpToLoginChoiceEventBean;
import com.yanxiu.gphone.parent.fragment.ParentNavFragmentFactory;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.out.utils.ParentOutActivityJumpUtils;
import com.yanxiu.gphone.parent.manager.ParentActivityManager;
import com.yanxiu.gphone.parent.requestTask.RequestHonorRedFlagTask;
import com.yanxiu.gphone.parent.requestTask.RequestSetHonorRedFlagTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.upgrade.bean.UpdateDelShareIconsBean;
import com.yanxiu.gphone.upgrade.utils.PublicUpgradeUtils;

import java.io.File;

import de.greenrobot.event.EventBus;

/**
 * Created by hai8108 on 16/3/17.
 */
public class MainForParentActivity extends YanxiuParentBaseActivity implements View.OnClickListener {
    private final static String TAG=MainForParentActivity.class.getSimpleName();
    private final static int MAIN_ACTIVITY_PAGE_COUNTS = 4;
    private int normalNavTxtColor, selNavTxtColor;
    private View[] navBarViews = new View[MAIN_ACTIVITY_PAGE_COUNTS];
    private ImageView[] navIconViews = new ImageView[MAIN_ACTIVITY_PAGE_COUNTS];
    private TextView[] navTextViews = new TextView[MAIN_ACTIVITY_PAGE_COUNTS];
    private int[] navTextResId = {R.string.navi_tbm_hometab, R.string.navi_tbm_weekworktab, R.string
            .navi_tbm_honortab, R.string.navi_tbm_mytab};
    private int[] navDrawableId = {R.drawable.parent_home_nor, R.drawable.parent_weekreport_nor,
            R.drawable.parent_honor_nor, R.drawable.parent_my_nor};
    private int[] navSelDrawableId = {R.drawable.parent_home_sel, R.drawable.parent_weekreport_sel,
            R.drawable.parent_honor_sel, R.drawable.parent_my_sel};

    private int lastSelectIndex = -1;
    private final int INDEX_HOME = 0;
    private final int INDEX_WEEKREPORT = 1;
    private final int INDEX_HONOR = 2;
    private final int INDEX_MY = 3;
    private RoundedImageView navRedDotView;
    private FrameLayout contentMain;
    public FragmentManager fragmentManager;
    public ParentNavFragmentFactory mNaviFragmentFactory;
    private boolean isHonorRedFlag = false;

    public static void launchActivity (Activity activity) {
        Intent intent = new Intent(activity, MainForParentActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_parent_navi_layout);
        fragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new ParentNavFragmentFactory();
        ParentActivityManager.destoryAllActivity();
        PublicUpgradeUtils.getInstance().requestInitialize(false, MainForParentActivity.this,
                LoginModel.getToken(),
                LoginModel.getMobile());
        findView();
    }

    private void findView () {
        contentMain = (FrameLayout) findViewById(R.id.content_parent_main);
        initNvaBar();
        showCurrentFragment(0);
    }

    private void initNvaBar () {

        selNavTxtColor = getResources().getColor(R.color.color_805500_p);
        normalNavTxtColor = getResources().getColor(R.color.color_333333_p);

        navBarViews[0] = findViewById(R.id.navi_homepage);
        navBarViews[1] = findViewById(R.id.navi_weekreport);
        navBarViews[2] = findViewById(R.id.navi_honortitle);
        navBarViews[3] = findViewById(R.id.navi_mytitle);
        navRedDotView = (RoundedImageView) navBarViews[2].findViewById(R.id.nav_red_dot);
        for (int i = 0; i < MAIN_ACTIVITY_PAGE_COUNTS; i++) {
            navBarViews[i].setOnClickListener(this);
            navIconViews[i] = (ImageView) navBarViews[i].findViewById(R.id.nav_icon);
            navIconViews[i].setImageResource(navDrawableId[i]);

            navTextViews[i] = (TextView) navBarViews[i].findViewById(R.id.nav_txt);
            navTextViews[i].setText(navTextResId[i]);
        }
    }

    @Override
    public void onClick (View view) {
        int curItem = INDEX_HOME;
        if (view == navBarViews[0]) {
            curItem = INDEX_HOME;
        } else if (view == navBarViews[1]) {
            curItem = INDEX_WEEKREPORT;
        } else if (view == navBarViews[2]) {
            curItem = INDEX_HONOR;
        } else if (view == navBarViews[3]) {
            curItem = INDEX_MY;
        }
        if (mNaviFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }

    private void checkNavBarProcess (int index) {
        if (index >= 0 && index < MAIN_ACTIVITY_PAGE_COUNTS) {
            resetNavBar();
            navTextViews[index].setTextColor(selNavTxtColor);
//            navTextViews[index].setShadowLayer(2, 0, 2, getResources().getColor(R.color
//                    .color_333333_p));
            navIconViews[index].setImageResource(navSelDrawableId[index]);

        }
    }

    private void resetNavBar () {
        for (int i = 0; i < MAIN_ACTIVITY_PAGE_COUNTS; i++) {
            navTextViews[i].setTextColor(normalNavTxtColor);
//            navTextViews[i].setShadowLayer(2, 0, 2, getResources().getColor(R.color.color_333333_p));
            navIconViews[i].setImageResource(navDrawableId[i]);
        }

    }

    private void showCurrentFragment (int index) {
        if (index == lastSelectIndex) {
            return;
        }
        lastSelectIndex = index;
        checkNavBarProcess(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new ParentNavFragmentFactory();
        }
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        mNaviFragmentFactory.hideAndShowFragment(fragmentManager, index);
        requestHonorRedFlag(index);
    }
    private void updateHonorRedDot(){
        if(isHonorRedFlag){
            navRedDotView.setVisibility(View.VISIBLE);
        } else {
            navRedDotView.setVisibility(View.GONE);
        }
    }
    private RequestHonorRedFlagTask mRequestHonorRedFlagTask;
    private RequestSetHonorRedFlagTask mRequestSetHonorRedFlagTask;

    private void cancelHonorRedFlag(){
        if(mRequestHonorRedFlagTask != null){
            mRequestHonorRedFlagTask.cancel();
        }
        mRequestHonorRedFlagTask = null;
    }

    private void requestHonorRedFlag(int index){
        if(index == INDEX_HOME){
            cancelHonorRedFlag();
            mRequestHonorRedFlagTask = new RequestHonorRedFlagTask(this, new AsyncCallBack() {
                @Override
                public void update (YanxiuBaseBean result) {
                    ParentRemindBean mBean = (ParentRemindBean) result;
                    isHonorRedFlag = mBean.getProperty().getShouldShow() == 1 ? true : false;
                    updateHonorRedDot();
                }
                @Override
                public void dataError (int type, String msg) {

                }
            });
            mRequestHonorRedFlagTask.start();
        }
    }
    private void cancelSetHonorRedFlag(){
        if(mRequestSetHonorRedFlagTask != null){
            mRequestSetHonorRedFlagTask.cancel();
        }
        mRequestSetHonorRedFlagTask = null;
    }

    private void requestSetHonorRedFlag(String honorIds){
            cancelSetHonorRedFlag();
            mRequestSetHonorRedFlagTask = new RequestSetHonorRedFlagTask(this, honorIds,new
                    AsyncCallBack () {
                @Override
                public void update (YanxiuBaseBean result) {
                    isHonorRedFlag = false;
                    updateHonorRedDot();
                }
                @Override
                public void dataError (int type, String msg) {

                }
            });
        mRequestSetHonorRedFlagTask.start();
    }
    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    private Handler handler = new Handler();

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {
                case 0:
                    ParentUtils.showToast(R.string.parent_app_exit_tip);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run () {
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
    private void exitProcess () {
        CommonActivityManager.destory();
        android.os.Process.killProcess(android.os.Process.myPid());
        finish();
    }

    public void onEventMainThread(TokenInviateBean tokenInviateBean){
        LogInfo.log("geny", "MainForParentActivity-------onEventMainThread");
        ParentOutActivityJumpUtils.jumpOutToLoginChoiceActivity(MainForParentActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ParentJumpToLoginChoiceEventBean eventBean = new ParentJumpToLoginChoiceEventBean();
                EventBus.getDefault().post(eventBean);
            }
        }, 2000);

    }
    public void onEventMainThread(HonorRedFlagEventBean mHonorRedFlagEventBean){
        if(mHonorRedFlagEventBean != null && !TextUtils.isEmpty(mHonorRedFlagEventBean.getRedIds())){
            LogInfo.log("haitian", "onEventMainThread ids=" + mHonorRedFlagEventBean.getRedIds());
            requestSetHonorRedFlag(mHonorRedFlagEventBean.getRedIds());
        }
    }

    /**
     * 分享图片在升级完成后删除
     * @param mBean
     */
    public void onEventMainThread(UpdateDelShareIconsBean mBean){
        LogInfo.log(TAG,"UpdateDelShareIconsBean");
        FileUtils.RecursionDeleteFile(new File(YanxiuParentConstants.SHARE_ICON_PATH));
    }
    @Override
    protected void onDestroy() {
        LogInfo.log("geny", "MainForParentActivity-------onDestroy");
        super.onDestroy();
        ShareManager.getInstance().clearInstance();
        cancelHonorRedFlag();
        cancelSetHonorRedFlag();
        EventBus.getDefault().unregister(this);
    }
}
