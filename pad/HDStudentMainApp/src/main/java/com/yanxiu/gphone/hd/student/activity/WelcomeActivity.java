package com.yanxiu.gphone.hd.student.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.model.LocalCacheBean;
import com.common.share.utils.ShareUtils;
import com.tendcloud.tenddata.TCAgent;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class WelcomeActivity extends YanxiuBaseActivity{

    private final static int GO_LOGIN = 0x01;
    private final static int GO_MAIN = 0x02;
    private final static int GO_FEATURE = 0x03;

    private RelativeLayout rootView;
    private ViewPager viewpager;
    private MyViewPagerAdapter adapter;
    private int lastX = 0;

    private List<View> views;

    private Handler mHander = new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_LOGIN:
                    LoginActivity.launcherActivity(WelcomeActivity.this,0);
                    WelcomeActivity.this.finish();
                    break;
                case GO_MAIN:
                    MainActivity.launchActivity(WelcomeActivity.this);
                    WelcomeActivity.this.finish();
                    break;
                case GO_FEATURE:
                    rootView.setVisibility(View.GONE);
                    viewpager.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.welcome);
//        LogInfo.log("haitian", CommonCoreUtil.getScreenWidth() + "*" + CommonCoreUtil.getScreenHeight
//                () + "---density:" + CommonCoreUtil.getDensity());
//        LogInfo.log("haitian", "getStatusBarHeight ="+getStatusBarHeight());
//        LogInfo.log("haitian", "getNavigationBarHeight ="+getNavigationBarHeight());
        TCAgent.init(getApplicationContext());
        launch();
        savaImage();
    }

    private void savaImage(){
        final String filePath =YanXiuConstant.SHARE_ICON_PATH  + YanXiuConstant.SHARE_LOGO_NAME;
        File file = new File(filePath);
        if(!file.exists()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_app_icon);
                    ShareUtils.saveShareBitmap(bitmap, YanXiuConstant.SHARE_ICON_PATH ,YanXiuConstant.SHARE_LOGO_NAME);
                }
            }).start();
        }
    }

    private void launch(){
        rootView = (RelativeLayout) findViewById(R.id.root_view);
        viewpager = (ViewPager) findViewById(R.id.wel_vp);
        initviews();
        adapter = new MyViewPagerAdapter();
        viewpager.setAdapter(adapter);
        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if ((lastX - event.getX()) > 100 && (
                            viewpager.getCurrentItem() == views.size() - 1)) {
                        verifyLoginStatus();
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

        if(PreferencesManager.getInstance().getFristApp()){
            mHander.sendEmptyMessageDelayed(GO_FEATURE,2000);
            PreferencesManager.getInstance().setFristApp(false);
        }else{
            verifyLoginStatus();
        }
    }

    private void verifyLoginStatus() {
        LocalCacheBean cacheBean=LoginModel.getCacheData();
        if(cacheBean==null||StringUtils.isEmpty(cacheBean.getCacheData())){
            mHander.sendEmptyMessageDelayed(GO_LOGIN,1000);
        }else{
            mHander.sendEmptyMessageDelayed(GO_MAIN,2000);
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

//    private void initMAT() {
//        // 因此，MTA的初始化工作�?要在本onCreate中进�?
//        String appkey = "A1XDURM19G9L";
//        // 初始化并启动MTA
//        // 第三方SDK必须按以下代码初始化MTA，其中appkey为规定的格式或MTA分配的代码�??
//        // 其它普�?�的app可自行�?�择是否调用
//        try {
//            //会话统计用于统计启动次数,视为用户打开�?次新的会�? (1) 应用第一次启动，或�?�应用进程在后台被杀掉之后启�?
//            //(2) 应用�?到后台或锁屏超过X之后再次回到前台
//            //(3) X秒�?�过StatConfig.setSessionTimoutMillis(int)函数设置，默认为30000ms，即30�?
//            StatConfig.setSessionTimoutMillis(60 * 1000);
//
//            StatConfig.setAutoExceptionCaught(false); // 禁止捕获app未处理的异常
//            StatConfig.setEnableSmartReporting(false); // 禁止WIFI网络实时上报
//            StatConfig.setSendPeriodMinutes(8 * 60); // PERIOD间隔周期�?8小时
//            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD); //
//            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
//            StatService.startStatService(YanxiuApplication.getInstance(), appkey,
//                    com.tencent.stat.common.StatConstants.VERSION);
//        } catch (MtaSDkException e) {
//            // MTA初始化失�?
//            LogInfo.log("haitian", "MTA start failed.");
//        }
//    }

    @Override protected void onDestroy() {
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
            //            if(ActivityManager.hasMainActivity()){
            YanxiuApplication.getInstance().exitProcess();
            finish();
            return true;
            //            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
