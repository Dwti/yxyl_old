package com.yanxiu.gphone.student.fragment.question;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.inter.OnPushPullTouchListener;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Yangjj on 2016/7/25.
 */
public class SolveComplexQuestionFragment extends BaseQuestionFragment implements View.OnClickListener, QuestionsListener, PageIndex ,ViewPager.OnPageChangeListener  {

    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
    private ImageView ivBottomCtrl;
    private YXiuAnserTextView tvYanxiu;
    private int pageCount = 1;
    private QuestionsListener listener;
    private OnPushPullTouchListener mOnPushPullTouchListener;
    private Resources mResources;

    
    private int pageCountIndex;
    private ViewPager vpAnswer;
    private List<PaperTestEntity> children;

    private AnswerAdapter adapter;

    private LinearLayout ll_bottom_view;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_solve_complex_question,null);
        initView();
        //initAnim();
        initData();
        return rootView;
    }
	
	

    private void initData() {
        mResources = getActivity().getResources();
        LogInfo.log("geny-", "pageCountIndex====" + pageCountIndex + "---pageIndex===" + pageIndex);
        if(questionsEntity != null && questionsEntity.getStem() != null){
            tvYanxiu.setTextHtml(questionsEntity.getStem());
        }
    }
    private void initView(){
        llTopView = (ExpandableRelativeLayoutlayout) rootView.findViewById(R.id.rl_top_view);
        llTopView.setOnExpandStateChangeListener(new ExpandableRelativeLayoutlayout.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(View view, boolean isExpanded) {
                if(isExpanded){
                    ivBottomCtrl.setBackgroundResource(R.drawable.read_question_arrow_up);
                }else{
                    ivBottomCtrl.setBackgroundResource(R.drawable.read_question_arrow_down);
                }
            }
        });
        ll_bottom_view = (LinearLayout) rootView.findViewById(R.id.ll_bottom_view);
        mOnPushPullTouchListener = new OnPushPullTouchListener(ll_bottom_view, getActivity());
        ivBottomCtrl = (ImageView) rootView.findViewById(R.id.iv_bottom_ctrl);
        ivBottomCtrl.setOnTouchListener(mOnPushPullTouchListener);
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

    }

    public void onPageCount(int count) {

        pageCount = count;
    }
    private boolean isVisibleToUser;
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        LogInfo.log("geny", "setUserVisibleHint");
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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
//        rootView=null;
//        llTopView=null;
//        ivBottomCtrl=null;
//        //animUp=null;
//        //listener=null;
//        mResources=null;
//        tvYanxiu=null;
//        vpAnswer=null;
//
//        children=null;
//
//        adapter=null;
//        System.gc();
    }

    public void onEventMainThread(ChildIndexEvent event) {
        if(event != null && vpAnswer != null){
//            String msg = "onEventMainThread收到了消息：" + event.getIndex();
//            Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
            vpAnswer.setCurrentItem(event.getIndex());
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
    public void onPageSelected(int childPosition) {
//        pagerIndex = position;
        if(questionsEntity != null){
            pageCountIndex = pageIndex + childPosition;
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser){
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageIndex);
            }else if(this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser){
                ((ResolutionAnswerViewActivity)this.getActivity()).setIndexFromRead(pageIndex);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
//
        if(questionsEntity != null){
            if(questionsEntity.getChildPageIndex() != -1){
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
//        LogInfo.log("geny", "ChoiceQuestionSingleFragment flipNextPager");
    }

    @Override
    public void setDataSources(AnswerBean bean) {

    }

    @Override
    public void initViewWithData(AnswerBean bean) {

    }

    @Override public void answerViewClick() {

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
}
