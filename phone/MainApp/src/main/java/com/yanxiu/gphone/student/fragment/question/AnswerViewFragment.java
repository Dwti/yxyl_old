package com.yanxiu.gphone.student.fragment.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.QuestionEntity;

import de.greenrobot.event.EventBus;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class AnswerViewFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private View rootView;

    private ViewPager vpAnswer;

    private QuestionEntity questionsEntity;

    private List<QuestionEntity> children;

    private AnswerAdapter adapter;

//    private boolean isResolution;
//    //是否是错题集
//    private boolean isWrongSet;

    private int answerViewTypyBean;

    public AnswerViewFragment(){
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        LogInfo.log("geny-", "AnswerViewFragment   onCreate");
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        this.answerViewTypyBean = (getArguments() != null) ? getArguments().getInt("answerViewTypyBean") : null;
//        isResolution =(getArguments() != null) ?  getArguments().getBoolean("isResolution") : false;
//        isWrongSet =(getArguments() != null) ?  getArguments().getBoolean("isWrongSet") : false;

        if(questionsEntity != null && questionsEntity.getChildren() != null){
            children = questionsEntity.getChildren();
//            LogInfo.log("geny", "chilid" + children.size());
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
//        LogInfo.log("geny-", "questionsEntity.getChildPageIndex()" + questionsEntity.getChildPageIndex());
        if(questionsEntity != null){
            if(questionsEntity.getChildPageIndex() != -1){
                vpAnswer.setCurrentItem(questionsEntity.getChildPageIndex());
//                LogInfo.log("geny-", "questionsEntity.getChildPageIndex()" + questionsEntity.getChildPageIndex());
//                questionsEntity.setChildPageIndex(-1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_answer_view,null);
        initView();
        return rootView;
    }

//    private float DOWN_X = 0;
//    private float DOWN_Y = 0;
//    private static final int HORIZONTAL_DISTANCE = 30;
//    private static final int VERTICAL_DISTANCE = 50;
    private void initView(){
        vpAnswer = (ViewPager) rootView.findViewById(R.id.answer_viewpager);
//        vpAnswer.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                // 在这里做手势的判断
//                // 如果是左边滑动
//
//                // 记住手指点下去的那个点
//                // 判断移动的位置和距离
//                switch (event.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//
//                        DOWN_X = event.getX();
//                        DOWN_Y = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float currentX = event.getX();
//                        float currentY = event.getY();
//                        if ((Math.abs((DOWN_Y - currentY)) > VERTICAL_DISTANCE
//                                && Math.abs(DOWN_X - currentX) < HORIZONTAL_DISTANCE)){
//                            v.getParent().requestDisallowInterceptTouchEvent(false);
//                            return true;
//                        }
//                        break;
//
//                    case MotionEvent.ACTION_UP:// 如果按下的地方和手指起来的地方在一起的话就认为是用户进行了点击
//                        break;
//
//                    default:
//                        break;
//                }
//                v.getParent().requestDisallowInterceptTouchEvent(true);//
//                // 让事件交给父控件来处理，整个Tab就可以滑动了。
//                return false;
//            }
//        });
        //=============================================
        //反射viewPager里面的mScroller
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(vpAnswer.getContext(),new AccelerateInterpolator());
            mField.set(vpAnswer, mScroller);
        } catch (Exception e) {

        }
        //=============================================
        vpAnswer.setOnPageChangeListener(this);
        adapter = new AnswerAdapter(this.getChildFragmentManager());
        adapter.setAnswerViewTypyBean(answerViewTypyBean);
        adapter.addDataSourcesForReadingQuestion(children);
        int count = adapter.getCount();
        if(this.getParentFragment() != null && this.getParentFragment() instanceof  ReadingQuestionsFragment){
            ((ReadingQuestionsFragment)this.getParentFragment()).onPageCount(count);
        }
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }




    @Override
    public void onPageSelected(int position) {
        if(this.getParentFragment() != null && this.getParentFragment() instanceof  ReadingQuestionsFragment){
            if(questionsEntity != null){
//                LogInfo.log("geny-", "questionsEntity.getPageIndex()" + questionsEntity.getPageIndex() + "position" + position);
                ((ReadingQuestionsFragment)this.getParentFragment()).onPageSelected(position);
            }
        }
    }


    public void onEventMainThread(ChildIndexEvent event) {
        if(event != null && vpAnswer != null){
//            String msg = "onEventMainThread收到了消息：" + event.getIndex();
//            Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
            vpAnswer.setCurrentItem(event.getIndex());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 200;

        public int getmDuration() {
            return mDuration;
        }
        public void setmDuration(int mDuration) {
            this.mDuration = mDuration;
        }
        public FixedSpeedScroller(Context context) {
            super(context);
        }
        public FixedSpeedScroller(Context context,Interpolator interpolator) {
            super(context,interpolator);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

    }


}
