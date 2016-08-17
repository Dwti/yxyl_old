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
import com.yanxiu.gphone.student.bean.PaperTestEntity;
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

    private List<PaperTestEntity> children;

    private AnswerAdapter childAnswerAdapter;

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
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        this.answerViewTypyBean = (getArguments() != null) ? getArguments().getInt("answerViewTypyBean") : null;
        if(questionsEntity != null && questionsEntity.getChildren() != null){
            children = questionsEntity.getChildren();
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(questionsEntity != null){
            if(questionsEntity.getChildPageIndex() != -1){
                vpAnswer.setCurrentItem(questionsEntity.getChildPageIndex());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_answer_view,null);
        initView();
        return rootView;
    }
    private void initView(){
        vpAnswer = (ViewPager) rootView.findViewById(R.id.answer_viewpager);
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
        childAnswerAdapter = new AnswerAdapter(this.getChildFragmentManager());
        childAnswerAdapter.setAnswerViewTypyBean(answerViewTypyBean);
        childAnswerAdapter.addDataSourcesForReadingQuestion(children);
        int count = childAnswerAdapter.getCount();
        if(this.getParentFragment() != null && this.getParentFragment() instanceof  ReadingQuestionsFragment){
            ((ReadingQuestionsFragment)this.getParentFragment()).onPageCount(count);
        }
        vpAnswer.setAdapter(childAnswerAdapter);
        childAnswerAdapter.setViewPager(vpAnswer);


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
                ((ReadingQuestionsFragment)this.getParentFragment()).onPageSelected(position);
            }
        }
    }


    public void onEventMainThread(ChildIndexEvent event) {
        if(event != null && vpAnswer != null){
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
