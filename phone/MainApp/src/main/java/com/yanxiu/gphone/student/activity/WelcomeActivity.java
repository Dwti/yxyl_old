package com.yanxiu.gphone.student.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.common.core.manage.CommonActivityManager;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.constants.LoginConstants;
//import com.yanxiu.gphone.parent.activity.MainForParentActivity;
//import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
//import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.manager.AppStartPointManager;
import com.yanxiu.gphone.student.preference.PreferencesManager;
import com.yanxiu.gphone.student.requestTask.RequestCreateShareIconTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class WelcomeActivity extends YanxiuBaseActivity {

    private final static int GO_LOGIN_CHOICE = 0x01;
    private final static int GO_MAIN = 0x02;
    private final static int GO_PARENT_MAIN = 0x03;
    private final static int GO_FEATURE = 0x04;
    private final static int GO_BIND_INFO = 0x05;
    private RelativeLayout rootView;
    private ViewPager viewpager;
    private MyViewPagerAdapter adapter;
    private int lastX = 0;

    private List<View> views;
    private RequestCreateShareIconTask createShareIconTask;
    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_LOGIN_CHOICE:
                    //ActivityJumpUtils.jumpToLoginChoiceActivity(WelcomeActivity.this);
                    LoginActivity.launcherActivity(WelcomeActivity.this, 0);
                    WelcomeActivity.this.finish();
                    break;
                case GO_MAIN:
                    MainActivity.launchActivity(WelcomeActivity.this);
                    WelcomeActivity.this.finish();
                    break;
                case GO_PARENT_MAIN:
                    //MainForParentActivity.launchActivity(WelcomeActivity.this);
                    WelcomeActivity.this.finish();
                    break;
                case GO_FEATURE:
                    rootView.setVisibility(View.GONE);
                    viewpager.setVisibility(View.VISIBLE);
                    break;
                case GO_BIND_INFO:
                    //ParentActivityJumpUtils.jumpToParentBindAccountActivity(WelcomeActivity.this,-1);
                    WelcomeActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initStart();
        launch();
        savaShareIconImage();
    }

    private void savaShareIconImage() {
        createShareIconTask = new RequestCreateShareIconTask(this);
        createShareIconTask.start();
    }

    private void launch() {
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        viewpager = (ViewPager) findViewById(R.id.wel_vp);
        initviews();
        adapter = new MyViewPagerAdapter();
        viewpager.setAdapter(adapter);
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if ((lastX - event.getX()) > 100 && (
                                viewpager.getCurrentItem() == views.size() - 1)) {
                            vertifyLoginStatus();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        if (PreferencesManager.getInstance().getFristApp()) {
            mHander.sendEmptyMessageDelayed(GO_FEATURE, 2000);
            PreferencesManager.getInstance().setFristApp(false);
        } else {
            vertifyLoginStatus();
        }
    }

    private void vertifyLoginStatus() {
        if (LoginModel.getCacheData() == null || StringUtils.isEmpty(LoginModel.getCacheData().getCacheData())) {
            mHander.sendEmptyMessageDelayed(GO_LOGIN_CHOICE, 1000);
        } else {
            //int poleID=LoginModel.getCacheData().getPoleId();
            int poleID = LoginConstants.ROLE_STUDENT;
            switch (poleID) {
                case LoginConstants.ROLE_STUDENT:
                    mHander.sendEmptyMessageDelayed(GO_MAIN, 2000);
                    break;
                /*case LoginConstants.ROLE_PARENT:
                    ParentInfoBean loginInBean= (ParentInfoBean) LoginModel.getRoleLoginBean();
                    if(loginInBean.getProperty().getIsBind()== YanxiuParentConstants.HASBIND){
                        mHander.sendEmptyMessageDelayed(GO_PARENT_MAIN,2000);
                    }else{
                        mHander.sendEmptyMessageDelayed(GO_BIND_INFO,2000);
                    }

                    break;*/
            }

        }
    }

    /**
     * viewpager
     */
    private void initviews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.what_new_one, null));
        views.add(inflater.inflate(R.layout.what_new_two, null));
        views.add(inflater.inflate(R.layout.what_new_three, null));
    }

    class MyViewPagerAdapter extends PagerAdapter {
        ImageView btn;

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = views.get(position);
            ((ViewPager) container).addView(view, 0);
            return views.get(position);
        }

        @Override
        public void destroyItem(View container, int position,
                                Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }
    }

    private void initStart() {
        AppStartPointManager.getInstance().uploadStartInfo();
        if(PreferencesManager.getInstance().getFristApp()){
            AppStartPointManager.getInstance().uploadStartInfoFirstInstall();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewpager.removeAllViewsInLayout();
        viewpager.setAdapter(null);
        viewpager = null;
    }

    /**
     * 返回键处理操作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CommonActivityManager.destory();
            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
