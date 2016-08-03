package com.yanxiu.gphone.student.fragment.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.fillblanks.FillBlanksButtonFramelayout;

import java.lang.reflect.Field;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/7/28.
 */
public class GestaltFillBlanksQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex, ViewPager.OnPageChangeListener,AnswerCallback {

    private AnswerBean bean;
    private FillBlanksButtonFramelayout fill_blanks_button;
    private int pageCountIndex;
    private ViewPager vpAnswer;
    private AnswerAdapter adapter;
    private int pageCount = 1;
    private List<QuestionEntity> children;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageCountIndex = this.pageIndex;
        if(questionsEntity != null && questionsEntity.getChildren() != null){
            children = questionsEntity.getChildren();
//            LogInfo.log("geny", "chilid" + children.size());
        }
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_gestaltfillblanks, null);
            initview();
            listener();
        }
        return rootView;
    }

    private void listener() {
        fill_blanks_button.setListener(new FillBlanksButtonFramelayout.QuestionPositionSelectListener() {
            @Override
            public void QuestionPosition(int position) {
//                Toast.makeText(getActivity(),position+"",Toast.LENGTH_SHORT).show();
                if (vpAnswer!=null) {
                    int count = adapter.getCount();
                    if (position<count) {
                        vpAnswer.setCurrentItem(position);
                    }
                }
            }
        });
    }

    private void initview() {
        ExpandableRelativeLayoutlayout rl_top_view= (ExpandableRelativeLayoutlayout) rootView.findViewById(R.id.rl_top_view);
        fill_blanks_button= (FillBlanksButtonFramelayout) rootView.findViewById(R.id.fill_blanks_button);
        if(questionsEntity != null && questionsEntity.getStem() != null){
            fill_blanks_button.setQuestionsEntity(questionsEntity);
            fill_blanks_button.setData(questionsEntity.getStem());
//            Log.d("asd","Stem+++++"+questionsEntity.getStem());
            fill_blanks_button.setAnswers(questionsEntity.getAnswer());
        }

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
        adapter.setAnswerCallback(this);
        adapter.setAnswerViewTypyBean(answerViewTypyBean);
        adapter.addDataSourcesForReadingQuestion(children);
        int count = adapter.getCount();
        onPageCount(count);
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);
    }

    public void onEventMainThread(ChildIndexEvent event) {
        if(event != null && vpAnswer != null){
//            String msg = "onEventMainThread收到了消息：" + event.getIndex();
//            Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
//            vpAnswer.setCurrentItem(event.getIndex());
        }
    }

    public void onPageCount(int count) {

        pageCount = count;
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        String ss="";
        ss="";
    }

    @Override
    public void onResume() {
        super.onResume();
        if(bean == null){
            bean = questionsEntity.getAnswerBean();
        }
        setDataSources(bean);
        LogInfo.log("geny", "onResume");
        if (vpAnswer!=null) {
            vpAnswer.setCurrentItem(0);
        }
//        if(questionsEntity != null){
//            if(questionsEntity.getChildPageIndex() != -1){
//                vpAnswer.setCurrentItem(questionsEntity.getChildPageIndex());
//            }
//        }
        //        LogInfo.log("geny", paperTestEntity.getQuestions().getStem());
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if(fill_blanks_button!=null){
            fill_blanks_button.setDataSources(bean);
        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {
        if(fill_blanks_button!=null){
            fill_blanks_button.hideSoftInput();
            if(bean!=null){
                LogInfo.log("king","answerViewClick saveAnswers");
                fill_blanks_button.saveAnswers();
            }
        }
    }

    private boolean isVisibleToUser;
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        LogInfo.log("geny", "setUserVisibleHint");
        this.isVisibleToUser = isVisibleToUser;
        if(!isVisibleToUser && fill_blanks_button!=null){
            fill_blanks_button.hideSoftInput();
            if(bean!=null){
                LogInfo.log("king","saveAnswers");
                fill_blanks_button.saveAnswers();
            }
        }
        if (isVisibleToUser&&!ischild){
            if (adapter!=null){
                ((QuestionsListener)getActivity()).flipNextPager(adapter);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(questionsEntity != null){
            pageCountIndex = pageIndex + position;
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser){
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            }else if(this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser){
                ((ResolutionAnswerViewActivity)this.getActivity()).setIndexFromRead(pageCountIndex);
            }
            if (fill_blanks_button!=null){
                fill_blanks_button.setTextViewSelect(position);
            }
        }
        ((BaseAnswerViewActivity)getActivity()).setPagerSelect(adapter.getCount(),position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
//        rootView=null;
//        //animUp=null;
//        //listener=null;
//        vpAnswer=null;
//
//        children=null;
//
//        adapter=null;
//        System.gc();
    }

    @Override
    public void answercallback(int position,String answer) {
        if (fill_blanks_button!=null) {
            fill_blanks_button.setAnswersToPosition(position, answer);
        }
    }
}
