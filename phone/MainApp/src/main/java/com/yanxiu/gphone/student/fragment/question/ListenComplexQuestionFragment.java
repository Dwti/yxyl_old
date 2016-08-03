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
public class ListenComplexQuestionFragment extends BaseQuestionFragment {

    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
    private LinearLayout ll_bottom_view;
    private ImageView ivBottomCtrl;
    private YXiuAnserTextView tvYanxiu;
    private int pageCount = 1;
    private QuestionsListener listener;
    private OnPushPullTouchListener mOnPushPullTouchListener;
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
    private Handler handler;
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

}
