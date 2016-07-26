package com.yanxiu.gphone.student.fragment.question;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Yangjj on 2016/7/25.
 */
public class ReadComplexFragment extends BaseQuestionFragment implements View.OnClickListener,QuestionsListener, PageIndex ,ViewPager.OnPageChangeListener  {

    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
    private ImageView ivBottomCtrl;
    private TextView tvPagerIndex;
    private TextView tvPagerCount;
    private YXiuAnserTextView tvYanxiu;
    private Resources mResources;

    private int pageCount = 1;
    private int pageCountIndex;
    private ViewPager vpAnswer;
    private List<QuestionEntity> children;

    private AnswerAdapter adapter;
    private YanxiuTypefaceTextView tvReadItemQuesitonType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_read_question,null);
        initView();
        //initAnim();
        initData();
        return rootView;
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
        vpAnswer.setAdapter(adapter);
        adapter.setViewPager(vpAnswer);

    }

    private void initData() {
        mResources = getActivity().getResources();
        LogInfo.log("geny-", "pageCountIndex====" + pageCountIndex + "---pageIndex===" + pageIndex);
        if(questionsEntity != null && questionsEntity.getStem() != null){
            tvYanxiu.setTextHtml(questionsEntity.getStem());
        }
    }

    @Override
    public int getPageIndex() {
        return 0;
    }

    @Override
    public void setPageIndex(int pageIndex) {

    }

    @Override
    public void flipNextPager(QuestionsListener listener) {

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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
