package com.yanxiu.gphone.student.fragment.question;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.activity.WrongAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.CorpListener;
import com.yanxiu.gphone.student.inter.OnPushPullTouchListener;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.SimpleAudioPlayer;
import com.yanxiu.gphone.student.view.question.GuideQuestionView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sunpeng on 2016/8/2.
 */
public class ListenComplexQuestionFragment extends BaseQuestionFragment implements View.OnClickListener,  QuestionsListener, PageIndex, ViewPager.OnPageChangeListener {

    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
    private LinearLayout ll_bottom_view;
    private ImageView ivBottomCtrl;
    private YXiuAnserTextView tvYanxiu;
    private TextView tv_timer;
    private int pageCount = 1;
    private QuestionsListener listener;
    private Resources mResources;
    private static final int UPDATE_PROGRESS = 0;
    private boolean isNeedUpdate;
    private TelephonyManager manager;
    private int pageCountIndex;
    private ViewPager vpAnswer;
    private List<PaperTestEntity> children;
    private boolean isVisibleToUser;
    private AnswerAdapter adapter;
    private SimpleAudioPlayer mSimplePlayer;
    private MediaPlayer mediaPlayer;
    private Context mContext;
    private int mDuration;   //音频总时长
    private int mMinutes;  //总的分钟数
    private CountDownTimer mDownTimer;   //倒计时器
    private long mMillisUntilFinished;
    private String url;
    private int lastViewPagerPosition=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageCountIndex = this.pageIndex;
        if (questionsEntity != null && questionsEntity.getChildren() != null) {
            children = questionsEntity.getChildren();
            url = questionsEntity.getUrl();
        }
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void setChildPagerIndex(int childPagerIndex) {
        super.setChildPagerIndex(childPagerIndex);
        if (vpAnswer != null) {
            vpAnswer.setCurrentItem(childPagerIndex);
        }
    }

    private void setViewPagerCurrent(){
        if (vpAnswer!=null&&selectPagerIndex!=-1&&isVisibleToUser){
            vpAnswer.setCurrentItem(selectPagerIndex);
            selectPagerIndex=-1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_listen_complex_question, null);
            isNeedUpdate = true;
            mContext = getActivity();
            initView();
            initData();
        }
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
        tv_timer = (TextView) rootView.findViewById(R.id.tv_timer);
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
        ll_bottom_view = (LinearLayout) rootView.findViewById(R.id.ll_bottom_view);
        OnPushPullTouchListener mOnPushPullTouchListener = new OnPushPullTouchListener(ll_bottom_view,llTopView, getActivity());
        ivBottomCtrl = (ImageView) rootView.findViewById(R.id.iv_bottom_ctrl);
        LinearLayout ll_bottom_ctrl= (LinearLayout) rootView.findViewById(R.id.ll_bottom_ctrl);
        ll_bottom_ctrl.setOnTouchListener(mOnPushPullTouchListener);
//        ivBottomCtrl.setOnTouchListener(this);

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
        adapter.addDataSourcesForReadingQuestion(children, questionsEntity.getTemplate(), questionsEntity.getType_id(), getTotalCount());
        int count = adapter.getCount();
        onPageCount(count);
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);
        mSimplePlayer.setOnControlButtonClickListener(new SimpleAudioPlayer.OnControlButtonClickListener() {
            @Override
            public void onClick(ImageView imageButton) {
                if (mSimplePlayer.getProgress() == 0) {
                    //开始播放
//                    String path = "http://abv.cn/music/光辉岁月.mp3";
                    String path = "http://data.5sing.kgimg.com/G034/M05/16/17/ApQEAFXsgeqIXl7gAAVVd-n31lcAABOogKzlD4ABVWP363.mp3";
                    if (TextUtils.isEmpty(url))
                        return;
                    try {
                        play(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        if (isVisibleToUser) {
            if (vpAnswer != null) {
                if (!is_reduction) {
                    vpAnswer.setCurrentItem(YanXiuConstant.index_position);
                    YanXiuConstant.index_position=0;
                } else {
                    if (adapter!=null) {
//                        vpAnswer.setCurrentItem(adapter.getCount() - 1);
                    }
                }
            }
            if (!ischild) {
                if (adapter != null) {
                    try {
                        ((QuestionsListener) getActivity()).flipNextPager(adapter);
                    }catch (Exception e){}
                }
            }
        } else {
            if (vpAnswer!=null){
                if (CorpUtils.getInstence().getCorpListener()!=null){
                    BaseQuestionFragment fragment= (BaseQuestionFragment) adapter.getItem(vpAnswer.getCurrentItem());
                    if (fragment instanceof SubjectiveQuestionFragment){
                        if (((CorpListener)fragment).hashCode()!=CorpUtils.getInstence().getCorpListener().hashCode()){
                            vpAnswer.setCurrentItem(0);
                        }
                    }
                }else {
                    vpAnswer.setCurrentItem(0);
                }
            }

//            try {
//                ArrayList<Fragment> list= adapter.getmFragments();
//                for (int i=0;i<list.size();i++){
//                    BaseQuestionFragment fragment= (BaseQuestionFragment) list.get(i);
//                    fragment.saveAnwser();
//                }
//            }catch (Exception e){}
            super.saveAnwser();
            answerViewClick();
            isNeedUpdate = false;
        }
        if (!isVisibleToUser && mediaPlayer != null && mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            mDownTimer.cancel();

            mSimplePlayer.setState(SimpleAudioPlayer.PAUSE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_PROGRESS && isNeedUpdate) {
                try {
                    mSimplePlayer.setProgress(mediaPlayer.getCurrentPosition());
                    Log.i("progress", mediaPlayer.getCurrentPosition() + "");
                    handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 暂停播放
     */
    private void pause() {
        if (mediaPlayer == null)
            return;
        if (mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            mDownTimer.cancel();
            isNeedUpdate = false;
        } else {
            //继续播放
            mediaPlayer.start();
            mDownTimer = createNewCountDownTimer(mMillisUntilFinished);
            mDownTimer.start();
            isNeedUpdate = true;
            handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
        }
    }

    /**
     * 播放指定地址的音乐文件 .mp3 .wav
     *
     * @param url
     */
    private void play(String url) throws Exception {
        Uri uri = Uri.parse(url);
        if (mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(mContext, uri);
        
        // 为播放器注册
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                isNeedUpdate = true;
                mediaPlayer.start();
                mSimplePlayer.setMax(mediaPlayer.getDuration());
                mDuration = mediaPlayer.getDuration() / 1000;
                mMinutes = mDuration / 60;
                mDownTimer = createNewCountDownTimer(mediaPlayer.getDuration());
                mDownTimer.start();
                Log.i("max", mediaPlayer.getDuration() + "");
//                handler.post(updateThread);
                handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 100);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Util.showToast("加载失败");
                return false;
            }
        });
        // 注册播放完毕后的监听事件
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                isNeedUpdate = false;
                mediaPlayer.release();
                mediaPlayer = null;    //此处不置空，播放完了不能播放第二次,这是因为mediaPlayer.release()之后，mediaPlayer并不为空，导致上面没有重新create，然后又释放了，所以不能播放第二次
                mSimplePlayer.setPlayOver();
            }
        });
        //缓冲的监听接口。缓冲的百分比，percent的值从0-100
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (!mp.isPlaying() && mSimplePlayer.isPlaying) {
                    mSimplePlayer.setState(SimpleAudioPlayer.PAUSE);
                } else if (mp.isPlaying() && !mSimplePlayer.isPlaying) {
                    mSimplePlayer.setState(SimpleAudioPlayer.PLAY);
                }
                Log.i("buffering", percent + "");
            }
        });

    }

    private CountDownTimer createNewCountDownTimer(long milliSeconds) {
        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(milliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //保存剩余时长
                mMillisUntilFinished = millisUntilFinished;
                tv_timer.setText(String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
            }

            @Override
            public void onFinish() {
                tv_timer.setText("00:00");
            }
        };
        return countDownTimer;
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
        }
        if (mSimplePlayer != null)
            mSimplePlayer.setPlayOver();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            mDownTimer.cancel();
            mSimplePlayer.setState(SimpleAudioPlayer.PAUSE);
            isNeedUpdate = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        EventBus.getDefault().unregister(this);//反注册EventBus
        //children=null;
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
        if (answerViewTypyBean == SubjectExercisesItemBean.ANSWER_QUESTION) {
            int costtime = AnswerViewActivity.totalTime - AnswerViewActivity.lastTime;
            AnswerViewActivity.lastTime = AnswerViewActivity.totalTime;
            adapter.setCostTime(costtime, questionsEntity.getPageIndex(), childPagerIndex);
            childPagerIndex = childPosition;
            AnswerViewActivity.childIndex = childPosition;
//            ((AnswerAdapter)vpAnswer.getAdapter()).getItem(0);
        }
        if (questionsEntity != null) {
            pageCountIndex = pageIndex + childPosition;
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser) {
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            } else if (this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser) {
                ((ResolutionAnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            } else if (this.getActivity() instanceof WrongAnswerViewActivity && isVisibleToUser) {
                ((WrongAnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            }
        }
        ((BaseAnswerViewActivity) getActivity()).setPagerSelect(adapter.getCount(), childPosition);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (questionsEntity != null) {
//            vpAnswer.setCurrentItem(childPagerIndex);
//        }

        if (vpAnswer != null) {
            if (!is_reduction) {
                BaseQuestionFragment fragment= (BaseQuestionFragment) adapter.getItem(vpAnswer.getCurrentItem());
                if (CorpUtils.getInstence().getCorpListener()!=null){
                    if (fragment instanceof SubjectiveQuestionFragment){
                        if (((CorpListener)fragment).hashCode()==CorpUtils.getInstence().getCorpListener().hashCode()){
                            vpAnswer.setCurrentItem(YanXiuConstant.index_position);
                            YanXiuConstant.index_position=0;
                        }
                    }
                }else {
                    vpAnswer.setCurrentItem(0);
                }
            } else {
//                vpAnswer.setCurrentItem(adapter.getCount() - 1);
            }
        }
        if (!ischild && isVisibleToUser) {
            if (adapter != null) {
                try {
                    ((QuestionsListener) getActivity()).flipNextPager(adapter);
                }catch (Exception e){}
            }
        }
        setViewPagerCurrent();
    }

    @Override
    public void setRefresh() {
        super.setRefresh();
        if (vpAnswer != null) {
            if (!is_reduction) {
                vpAnswer.setCurrentItem(childPagerIndex);
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
        try {
            BaseQuestionFragment fragment= (BaseQuestionFragment) adapter.getmFragments().get(vpAnswer.getCurrentItem());
            fragment.saveAnwser();
        }catch (Exception e){}
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
    private class MyListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // 音乐播放器暂停
                    pause();
                    Log.i("pause", "call_state_ringing");
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
    public Fragment getChildFragment() {
        return adapter.getItem(vpAnswer.getCurrentItem());
    }

    @Override
    public int getChildCount() {
        if (adapter != null && adapter.getCount() != 0) {
            return adapter.getCount();
        } else {
            return super.getChildCount();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
