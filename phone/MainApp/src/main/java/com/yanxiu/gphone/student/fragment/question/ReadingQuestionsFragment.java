package com.yanxiu.gphone.student.fragment.question;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.BaseAnswerViewActivity;
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.activity.WrongAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.CorpListener;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    private List<PaperTestEntity> children;

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
        selectTypeView();
        return rootView;
    }

    public void selectTypeView(){
        switch (answerViewTypyBean) {
            case SubjectExercisesItemBean.RESOLUTION:
                FrameLayout layout= (FrameLayout) rootView.findViewById(R.id.fra_sub_or_del);
                layout.setVisibility(View.VISIBLE);
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment1=manager.findFragmentByTag("sub_or_del");
                if (fragment1==null) {
                    final SubmitOrDeleteFragment fragment = new SubmitOrDeleteFragment();
                    fragment.setEntity(questionsEntity);
                    initSubOrDel(fragment);
                    fragment.setListener(new ReadingQuestionsFragment.listener(fragment));
                    transaction.add(R.id.fra_sub_or_del, fragment,"sub_or_del");
                    transaction.show(fragment);
                    transaction.commit();
                }else {
                    final SubmitOrDeleteFragment fragment= (SubmitOrDeleteFragment) fragment1;
                    fragment.setEntity(questionsEntity);
                    initSubOrDel(fragment);
                    fragment.setListener(new ReadingQuestionsFragment.listener(fragment));
                }
                break;
        }
    }

    private class listener implements SubmitOrDeleteFragment.OnButtonClickListener{

        private SubmitOrDeleteFragment fragment;

        public listener(SubmitOrDeleteFragment fragment){
            this.fragment=fragment;
        }

        @Override
        public void onClick(String type) {
            switch (type) {
                case SubmitOrDeleteFragment.TYPE_SUBMIT:
                    questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                    checkTheAnswer();
                    fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
                    setMistakeSubmit();
                    break;
                case SubmitOrDeleteFragment.TYPE_DELETE:
                    questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
                    fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
                    setMistakeDelete();
                    break;
            }
        }
    }

    @Override
    public void setMistakeDelete() {
        super.setMistakeDelete();
        ArrayList<Fragment> list=adapter.getmFragments();
        if (list==null){
            return;
        }
        for (int i=0;i<list.size();i++){
            BaseQuestionFragment fragment= (BaseQuestionFragment) list.get(i);
            fragment.setMistakeDelete();
        }
    }

    @Override
    public void setMistakeSubmit() {
        super.setMistakeSubmit();
        ArrayList<Fragment> list=adapter.getmFragments();
        if (list==null){
            return;
        }
        for (int i=0;i<list.size();i++){
            BaseQuestionFragment fragment= (BaseQuestionFragment) list.get(i);
            fragment.setMistakeSubmit();
        }
    }

    private void checkTheAnswer(){
        if (children!=null&&children.size()>0){
            for (int i=0;i<children.size();i++){
                QuestionEntity entity=children.get(i).getQuestions();
                if (entity.getAnswerIsRight()!=1){
                    ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.FAIL);
                    questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
                    return;
                }
            }
        }
        ((MistakeRedoActivity)getActivity()).showPopup(MistakeRedoActivity.RIGHT);
        questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
    }

    @Override
    public void redoCallback() {
        super.redoCallback();
        Fragment fragment1=getChildFragmentManager().findFragmentByTag("sub_or_del");
        if (fragment1!=null) {
            SubmitOrDeleteFragment fragment = (SubmitOrDeleteFragment) fragment1;
            initSubOrDel(fragment);
        }
    }

    private void initSubOrDel(SubmitOrDeleteFragment fragment) {
        if (QuestionEntity.TYPE_SUBMIT.equals(questionsEntity.getType())) {
            if (children!=null&&children.size()>0){
                for (int i=0;i<children.size();i++){
                    QuestionEntity entity=children.get(i).getQuestions();
                    if (!entity.isHaveAnser()){
                        fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_NOANSWER);
                        return;
                    }
                }
            }
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_HASANSWER);
        }else if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())){
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
        }
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

    @Override
    public Fragment getChildFragment() {
        int position=vpAnswer.getCurrentItem();
        Fragment fragment=adapter.getmFragments().get(position);
        return fragment;
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
        adapter.addDataSourcesForReadingQuestion(children, questionsEntity.getTemplate(), questionsEntity.getType_id(), getTotalCount(),this);
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
            tvReadItemQuesitonType.setText(children.get(0).getQuestions().getReadItemName());
        }
    }
    private boolean isVisibleToUser;
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        LogInfo.log("geny", "setUserVisibleHint");
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser&&!ischild){
            if (adapter!=null){
                ((QuestionsListener)getActivity()).flipNextPager(adapter);
            }
        }
//        if (vpAnswer != null) {
//            if (!is_reduction) {
//                vpAnswer.setCurrentItem(YanXiuConstant.index_position);
//                YanXiuConstant.index_position=0;
//            } else {
////                vpAnswer.setCurrentItem(adapter.getCount() - 1);
//            }
//        }
//        if (!isVisibleToUser){
//            if (vpAnswer!=null&&adapter!=null){
//                try {
//                    if (CorpUtils.getInstence().getCorpListener()!=null){
//                        BaseQuestionFragment fragment= (BaseQuestionFragment) adapter.getItem(vpAnswer.getCurrentItem());
//                        if (fragment instanceof SubjectiveQuestionFragment){
//                            if (((CorpListener)fragment).hashCode()!=CorpUtils.getInstence().getCorpListener().hashCode()){
//                                vpAnswer.setCurrentItem(0);
//                            }
//                        }
//                    }else {
//                        vpAnswer.setCurrentItem(0);
//                    }
//                }catch (Exception e){}
//
//            }
//
//        }
        if (!isVisibleToUser) {
            setCurrent(vpAnswer);
        }
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
        if(answerViewTypyBean == SubjectExercisesItemBean.ANSWER_QUESTION) {
            int costtime = AnswerViewActivity.totalTime - AnswerViewActivity.lastTime;
            AnswerViewActivity.lastTime = AnswerViewActivity.totalTime;
            adapter.setCostTime(costtime, questionsEntity.getPageIndex(), childPagerIndex);
            childPagerIndex = childPosition;
            AnswerViewActivity.childIndex = childPosition;
        }
        if(questionsEntity != null){
            pageCountIndex = pageIndex + childPosition;
            tvPagerIndex.setText(String.valueOf(childPosition + 1));
            tvPagerCount.setText("/" + pageCount);
            tvReadItemQuesitonType.setText(children.get(childPosition).getQuestions().getReadItemName());
            if (this.getActivity() instanceof AnswerViewActivity && isVisibleToUser){
                ((AnswerViewActivity) this.getActivity()).setIndexFromRead(pageCountIndex);
            }else if(this.getActivity() instanceof ResolutionAnswerViewActivity && isVisibleToUser){
                ((ResolutionAnswerViewActivity)this.getActivity()).setIndexFromRead(pageCountIndex);
            }else if(this.getActivity() instanceof WrongAnswerViewActivity && isVisibleToUser){
                ((WrongAnswerViewActivity)this.getActivity()).setIndexFromRead(pageCountIndex);
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
//
//        if(questionsEntity != null){
//            vpAnswer.setCurrentItem(childPagerIndex);
//        }

//        if (vpAnswer != null&&adapter!=null) {
//            if (!is_reduction) {
//                BaseQuestionFragment fragment= (BaseQuestionFragment) adapter.getItem(vpAnswer.getCurrentItem());
//                if (CorpUtils.getInstence().getCorpListener()!=null){
//                    if (fragment instanceof SubjectiveQuestionFragment){
//                        if (((CorpListener)fragment).hashCode()==CorpUtils.getInstence().getCorpListener().hashCode()){
//                            vpAnswer.setCurrentItem(YanXiuConstant.index_position);
//                            YanXiuConstant.index_position=0;
//                        }
//                    }
//                }else {
//                    vpAnswer.setCurrentItem(0);
//                }
//            } else {
////                vpAnswer.setCurrentItem(adapter.getCount() - 1);
//            }
//        }
//        if (!isVisibleToUser) {
            setCurrent(vpAnswer);
//        }
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
