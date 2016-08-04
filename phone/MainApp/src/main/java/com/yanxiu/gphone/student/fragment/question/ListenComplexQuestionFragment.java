package com.yanxiu.gphone.student.fragment.question;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.inter.OnPushPullTouchListener;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.SimpleAudioPlayer;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sunpeng on 2016/8/2.
 */
public class ListenComplexQuestionFragment extends BaseQuestionFragment implements View.OnClickListener, View.OnTouchListener, QuestionsListener, PageIndex, ViewPager.OnPageChangeListener {

    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
    private LinearLayout ll_bottom_view;
    private ImageView ivBottomCtrl;
    private YXiuAnserTextView tvYanxiu;
    private int pageCount = 1;
    private QuestionsListener listener;
    private Resources mResources;
    private long subtime = 0, beginTime = 0, falgTime = 0, pauseTime = 0;
    private static final int UPDATE_PROGRESS = 0;
    private boolean isNeedUpdate;
    private TelephonyManager manager;
    private int pageCountIndex;
    private ViewPager vpAnswer;
    private List<QuestionEntity> children;
    private boolean isVisibleToUser;
    private AnswerAdapter adapter;
    private Chronometer et_time;
    private SimpleAudioPlayer mSimplePlayer;
    private MediaPlayer mediaPlayer;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageCountIndex = this.pageIndex;
        if (questionsEntity != null && questionsEntity.getChildren() != null) {
            children = questionsEntity.getChildren();
        }
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listen_complex_question, null);
        isNeedUpdate=true;
        mContext = getActivity();
        initView();
        initData();
        return rootView;
    }


    private void initData() {
        manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//        manager.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
        mResources = getActivity().getResources();
        LogInfo.log("geny-", "pageCountIndex====" + pageCountIndex + "---pageIndex===" + pageIndex);
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            tvYanxiu.setTextHtml(questionsEntity.getStem());
        }
    }

    private void initView() {
        mSimplePlayer = (SimpleAudioPlayer) rootView.findViewById(R.id.audioPlayer);
        et_time = (Chronometer) rootView.findViewById(R.id.et_time);
        llTopView = (ExpandableRelativeLayoutlayout) rootView.findViewById(R.id.rl_top_view);
        llTopView.setOnExpandStateChangeListener(new ExpandableRelativeLayoutlayout.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(View view, boolean isExpanded) {
                if (isExpanded) {
                    ivBottomCtrl.setBackgroundResource(R.drawable.read_question_arrow_up);
                } else {
                    ivBottomCtrl.setBackgroundResource(R.drawable.read_question_arrow_down);
                }
            }
        });
        ivBottomCtrl = (ImageView) rootView.findViewById(R.id.iv_bottom_ctrl);
        ivBottomCtrl.setOnTouchListener(this);
        ll_bottom_view = (LinearLayout) rootView.findViewById(R.id.ll_bottom_view);
        tvYanxiu = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);


        vpAnswer = (ViewPager) rootView.findViewById(R.id.answer_viewpager);
        //=============================================
        //反射viewPager里面的mScroller
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
        } catch (Exception e) {

        }
        //=============================================
        vpAnswer.setOnPageChangeListener(this);
        adapter = new AnswerAdapter(this.getChildFragmentManager());
        adapter.setAnswerViewTypyBean(answerViewTypyBean);
        adapter.addDataSourcesForReadingQuestion(children);
        int count = adapter.getCount();
        onPageCount(count);
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);
        mSimplePlayer.setOnControlButtonClickListener(new SimpleAudioPlayer.OnControlButtonClickListener() {
            @Override
            public void onClick(ImageView imageButton) {
                if (mSimplePlayer.getProgress() == 0) {
                    //开始播放
                    String path = "http://abv.cn/music/光辉岁月.mp3";
                    falgTime = SystemClock.elapsedRealtime();
                    try {
                        play(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pauseTime = 0;
                    et_time.setBase(falgTime);
                    et_time.start();
                } else {
                    pause();
                }
            }
        });

    }

    public void onPageCount(int count) {

        pageCount = count;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser){
            if (!ischild) {
                if (adapter != null) {
                    ((QuestionsListener) getActivity()).flipNextPager(adapter);
                }
            }
            if (vpAnswer != null) {
                if (!is_reduction) {
                    vpAnswer.setCurrentItem(0);
                } else {
                    vpAnswer.setCurrentItem(adapter.getCount() - 1);
                }
            }
        }else {
            isNeedUpdate = false;
        }
        if (!isVisibleToUser && mediaPlayer != null && mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            et_time.stop();
            pauseTime = SystemClock.elapsedRealtime();
            mSimplePlayer.setState(SimpleAudioPlayer.PAUSE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_PROGRESS && isNeedUpdate) {
                mSimplePlayer.setProgress(mediaPlayer.getCurrentPosition());
                Log.i("progress", mediaPlayer.getCurrentPosition() + "");
                handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
            }
        }
    };

    Runnable updateThread = new Runnable() {
        public void run() {
            // 获得歌曲现在播放位置并设置成播放进度条的值
            if (mediaPlayer != null) {
                mSimplePlayer.setProgress(mediaPlayer.getCurrentPosition());
                Log.i("progress", mediaPlayer.getCurrentPosition() + "");
                // 每次延迟100毫秒再启动线程
                handler.postDelayed(updateThread, 100);
            }
        }
    };


    /**
     * 暂停播放
     */
    private void pause() {
        if(mediaPlayer==null)
            return;
        if (mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            et_time.stop();
            pauseTime = SystemClock.elapsedRealtime();
            isNeedUpdate=false;
        } else {
            //继续播放
            subtime += SystemClock.elapsedRealtime() - pauseTime;
            mediaPlayer.start();
            beginTime = falgTime + subtime;
            et_time.setBase(beginTime);
            et_time.start();
            isNeedUpdate=true;
            handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
        }
    }

    /**
     * 播放指定地址的音乐文件 .mp3 .wav .amr
     *
     * @param url
     */
    private void play(String url) throws Exception {
        Uri uri = Uri.parse(url);
        mediaPlayer = MediaPlayer.create(mContext, uri);
        // 为播放器注册
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                mSimplePlayer.setMax(mediaPlayer.getDuration());
                Log.i("max", mediaPlayer.getDuration() + "");
//                handler.post(updateThread);
                handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
            }
        });

        // 注册播放完毕后的监听事件
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
                et_time.setBase(SystemClock.elapsedRealtime());
                et_time.start();
                et_time.stop();
                mSimplePlayer.setPlayOver();
            }
        });
        //缓冲的监听接口。缓冲的百分比，percent的值从0-100
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if(!mp.isPlaying()&&mSimplePlayer.isPlaying){
                    mSimplePlayer.setState(SimpleAudioPlayer.PAUSE);
                }else if(mp.isPlaying() && !mSimplePlayer.isPlaying){
                    mSimplePlayer.setState(SimpleAudioPlayer.PLAY);
                }
                Log.i("buffering", percent + "");
            }
        });

    }


    /**
     * 释放音乐播放器
     */
    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(updateThread);
        }
        mSimplePlayer.setPlayOver();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            et_time.stop();
            pauseTime = SystemClock.elapsedRealtime();
            mSimplePlayer.setState(SimpleAudioPlayer.PAUSE);
            isNeedUpdate=false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    public void onEventMainThread(ChildIndexEvent event) {
        if (event != null && vpAnswer != null) {
            vpAnswer.setCurrentItem(event.getIndex());
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    public void onPageSelected(int childPosition) {
//        pagerIndex = position;
        if (questionsEntity != null) {
            pageCountIndex = pageIndex + childPosition;
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser) {
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            } else if (this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser) {
                ((ResolutionAnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (questionsEntity != null) {
            if (questionsEntity.getChildPageIndex() != -1) {
                vpAnswer.setCurrentItem(questionsEntity.getChildPageIndex());
            }
        }

        if (vpAnswer != null) {
            if (!is_reduction) {
                vpAnswer.setCurrentItem(0);
            } else {
                vpAnswer.setCurrentItem(adapter.getCount() - 1);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {

    }

    @Override
    public void initViewWithData(AnswerBean bean) {

    }

    @Override
    public void answerViewClick() {

    }

    @Override
    public int getPageIndex() {
        LogInfo.log("geny-", "pageCountIndex====" + pageCountIndex + "---pageIndex===" + pageIndex);
        return pageCountIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int x;//触点X坐标
    public int y;//触点Y坐标

    public int yy;//控件高度
    public int xx;//控件宽度

    private int move_x;//x轴移动距离
    private int move_y;//y轴移动距离

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) motionEvent.getRawX();
                y = (int) motionEvent.getRawY();
                xx = (int) ll_bottom_view.getWidth();
                yy = (int) ll_bottom_view.getHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                int x_now = (int) motionEvent.getRawX();
                int y_now = (int) motionEvent.getRawY();

                //用来说明滑动情况，无意义
                if (Math.abs(x_now) > Math.abs(x)) {
                    //right
                    LogInfo.log("flip", "right");
                    if (Math.abs(y_now) > Math.abs(y)) {
                        //down
                        LogInfo.log("flip", "down");
                    } else {
                        //up
                        LogInfo.log("flip", "up");
                    }
                } else {
                    //left
                    LogInfo.log("flip", "left");
                    if (Math.abs(y_now) > Math.abs(y)) {
                        //down
                        LogInfo.log("flip", "down");
                    } else {
                        //up
                        LogInfo.log("flip", "up");
                    }
                }

                move_x = x_now - x;
                move_y = y_now - y;
                setMove();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private void setMove() {
        LogInfo.log("move", xx + "+XXXXXXXXX");
        LogInfo.log("move", yy - move_y + "+YYYYYYYYY");
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(getActivity().WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        LogInfo.log("move", height + "+YYYYYYYYY");
        if (yy - move_y < height * 3 / 5) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(xx, yy - move_y);
            ll_bottom_view.setLayoutParams(layoutParams);
        }
    }

    private class MyListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // 音乐播放器暂停
                    pause();
                    Log.i("pause","call_state_ringing");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // 重新播放音乐
                    pause();
                    Log.i("pause", "call_state_idle");
                    break;
            }
        }
    }

    @Override
    public int getChildCount() {
        if (adapter!=null) {
            return adapter.getCount();
        }else {
            return super.getChildCount();
        }
    }
}
