package com.yanxiu.gphone.student.fragment.question;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/14.
 */
public class ReadingQuestionsFragment extends BaseQuestionFragment implements View.OnClickListener,QuestionsListener, PageIndex ,ViewPager.OnPageChangeListener {
    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
//    private TextView  tvBottomCtrl;
    private ImageView ivBottomCtrl;
    private TextView tvPagerIndex;
    private TextView tvPagerCount;
//    private TranslateAnimation animDown;
    private TranslateAnimation animUp;
    private int pageCount = 1;
    private QuestionsListener listener;
    private Resources mResources;

    private int pageCountIndex;

    private YXiuAnserTextView tvYanxiu;

    private ViewPager vpAnswer;

    private List<QuestionEntity> children;

    private AnswerAdapter adapter;
    private YanxiuTypefaceTextView tvReadItemQuesitonType;

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
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_read_question,null);

//        android.support.v4.app.FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
//        Bundle args = new Bundle();
//        if(questionsEntity != null){
//            args.putSerializable("questions", questionsEntity);
//            args.putInt("answerViewTypyBean", answerViewTypyBean);
//        }
//        ft.add(R.id.content_read_question_frament, Fragment.instantiate(this.getActivity(), AnswerViewFragment.class.getName(), args)).commitAllowingStateLoss();

        initView();
        initAnim();
        initData();
        return rootView;
    }


//    public int getChildCount(){
//
//        if(questionsEntity != null && questionsEntity.getChildren()!= null){
//            childCount = questionsEntity.getChildren().size();
//        }
//
//        return childCount;
//    }

    private void initData() {
        mResources = ReadingQuestionsFragment.this.getActivity().getResources();
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
//                    animOpen(ivBottomCtrl);
                }else{
                    ivBottomCtrl.setBackgroundResource(R.drawable.read_question_arrow_down);
//                    animClose(ivBottomCtrl);
                }
            }
        });
        ivBottomCtrl = (ImageView) rootView.findViewById(R.id.iv_bottom_ctrl);
        ivBottomCtrl.setOnClickListener(this);
        tvPagerIndex = (TextView) rootView.findViewById(R.id.tv_pager_index);
        tvPagerCount = (TextView) rootView.findViewById(R.id.tv_pager_count);
        tvYanxiu = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        tvReadItemQuesitonType = (YanxiuTypefaceTextView) rootView.findViewById(R.id.tv_read_item_quesiton_type);
        tvReadItemQuesitonType.setTypefaceName(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD);


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
//        if(this.getParentFragment() != null && this.getParentFragment() instanceof  ReadingQuestionsFragment){
//            ((ReadingQuestionsFragment)this.getParentFragment()).
//        }
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);

    }

    public void onPageCount(int count) {

        pageCount = count;
        if(count > 0){
            tvPagerIndex.setText("1");
            tvPagerCount.setText("/" + count);

        }
        if(children != null && !children.isEmpty()){
            tvReadItemQuesitonType.setText(children.get(0).getReadItemName());
        }
    }
    private boolean isVisibleToUser;
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        LogInfo.log("geny", "setUserVisibleHint");
        this.isVisibleToUser = isVisibleToUser;
    }

//    public int getPagerIndex() {
//        return pagerIndex;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
        rootView=null;
        llTopView=null;
        ivBottomCtrl=null;
        tvPagerIndex=null;
        tvPagerCount=null;
        animUp=null;
        listener=null;
        mResources=null;
        tvYanxiu=null;
        vpAnswer=null;

        children=null;

        adapter=null;
        tvReadItemQuesitonType=null;
        System.gc();
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
            tvPagerIndex.setText(String.valueOf(childPosition + 1));
            tvPagerCount.setText("/" + pageCount);
            tvReadItemQuesitonType.setText(children.get(childPosition).getReadItemName());
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser){
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            }else if(this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser){
                ((ResolutionAnswerViewActivity)this.getActivity()).setIndexFromRead(pageCountIndex);
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
    }

    @Override
    public void onClick(View v) {
        if(v == ivBottomCtrl){
            llTopView.onExpandable(null);
        }
    }
    private void animOpen(final ImageView arrow) {
        Animation ani = AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.fenlei_rotate);
        ani.setFillAfter(true);
        ani.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setImageResource(R.drawable.read_question_arrow_up);
                arrow.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        arrow.startAnimation(ani);
    }

    private void animClose(final ImageView arrow) {
//        Util.showToast("animClose");
        Animation ani = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fenlei_rotate_back);
        ani.setFillAfter(true);
        ani.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setImageResource(R.drawable.read_question_arrow_down);
                arrow.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        arrow.startAnimation(ani);
    }


    private void initAnim() {
        long ANIMDURATION = 300l;
        animUp = new TranslateAnimation(0, 0, 0, -CommonCoreUtil.getScreenHeight());
        animUp.setDuration(ANIMDURATION);
        animUp.setInterpolator(new AccelerateInterpolator());
        animUp.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
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
