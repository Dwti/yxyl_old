package com.yanxiu.gphone.student.fragment.question;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.ResolutionAnswerViewActivity;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.ChildIndexEvent;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.ExpandableRelativeLayoutlayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Yangjj on 2016/7/25.
 */
public class ReadComplexQuestionFragment extends BaseQuestionFragment implements View.OnClickListener, View.OnTouchListener, QuestionsListener, PageIndex ,ViewPager.OnPageChangeListener  {

    private View rootView;
    private ExpandableRelativeLayoutlayout llTopView;
    private LinearLayout ll_bottom_view;
    private ImageView ivBottomCtrl;
    private YXiuAnserTextView tvYanxiu;
    private int pageCount = 1;
    private QuestionsListener listener;
    private Resources mResources;

    
    private int pageCountIndex;
    private ViewPager vpAnswer;
    private List<QuestionEntity> children;
    private boolean isVisibleToUser;

    private AnswerAdapter adapter;

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
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_read_complex_question,null);
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

    }

    public void onPageCount(int count) {

        pageCount = count;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
//        LogInfo.log("geny", "setUserVisibleHint");
        this.isVisibleToUser = isVisibleToUser;
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

    public int x;//触点X坐标
    public int y;//触点Y坐标

    public int yy;//控件高度
    public int xx;//控件宽度

    private int move_x;//x轴移动距离
    private int move_y;//y轴移动距离

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x= (int) motionEvent.getRawX();
                y= (int) motionEvent.getRawY();
                xx= (int) ll_bottom_view.getWidth();
                yy= (int) ll_bottom_view.getHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                int x_now= (int) motionEvent.getRawX();
                int y_now= (int) motionEvent.getRawY();

                //用来说明滑动情况，无意义
                if (Math.abs(x_now)>Math.abs(x)) {
                    //right
                    LogInfo.log("flip","right");
                    if (Math.abs(y_now)>Math.abs(y)){
                        //down
                        LogInfo.log("flip","down");
                    }else {
                        //up
                        LogInfo.log("flip","up");
                    }
                }else {
                    //left
                    LogInfo.log("flip","left");
                    if (Math.abs(y_now)>Math.abs(y)){
                        //down
                        LogInfo.log("flip","down");
                    }else {
                        //up
                        LogInfo.log("flip","up");
                    }
                }

                move_x=x_now-x;
                move_y=y_now-y;
                setMove();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private void setMove(){
        LogInfo.log("move",xx+"+XXXXXXXXX");
        LogInfo.log("move",yy-move_y+"+YYYYYYYYY");
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(getActivity().WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        LogInfo.log("move",height+"+YYYYYYYYY");
        if (yy-move_y < height*3/5) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(xx, yy - move_y);
            ll_bottom_view.setLayoutParams(layoutParams);
        }
        //layoutParams.addRule(LinearLayout., RelativeLayout.TRUE);
    }
}
