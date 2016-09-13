package com.yanxiu.gphone.student.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.AnswerAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.Configuration;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2015/7/6.
 */
public class BaseAnswerViewActivity extends YanxiuBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,QuestionsListener {

    private final static String TAG=BaseAnswerViewActivity.class.getSimpleName();
    protected PublicLoadLayout mRootView;
    protected SubjectExercisesItemBean dataSources;

    protected AnswerAdapter adapter;

    protected ImageView ivBack;
    protected ViewPager vpAnswer;
    public YanxiuTypefaceTextView tvToptext;
    protected TextView tvPagerIndex;
    protected TextView tvPagerCount;
    protected ImageView ivAnswerCard;
    protected ImageView ivFavCard;
    protected boolean flag=false;   //是否点了上一题按钮切换
//    private static BaseAnswerViewActivity act;

    protected int pageCount;
    protected int currentIndex;

    public YanxiuTypefaceTextView getTvToptext() {
        return tvToptext;
    }

    public void setTvToptext(YanxiuTypefaceTextView tvToptext) {
        this.tvToptext = tvToptext;
    }
//    protected StudentLoadingLayout loadingLayout;

    protected TextView tvQuestionTitle;
    protected Button btnWrongError;
    protected StudentLoadingLayout loadingLayout;

    protected Button btnLastQuestion, btnNextQuestion;
    protected QuestionsListener listener;
    protected int nextPager_onclick=0;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_answer_question);
        loadingLayout = (StudentLoadingLayout) mRootView.findViewById(R.id.loading_layout);
        setContentView(mRootView);
        if(savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void showDialog(){
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
    }


    public void showCommonDialog(){
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
    }


    public void hideDialog(){
        loadingLayout.setViewGone();
    }



    protected void initView(){
        btnLastQuestion = (Button) this.findViewById(R.id.btn_last_question);
        btnLastQuestion.setVisibility(View.GONE);
        btnNextQuestion = (Button) this.findViewById(R.id.btn_next_question);
        btnNextQuestion.setVisibility(View.VISIBLE);

        btnNextQuestion.setOnClickListener(this);
        btnLastQuestion.setOnClickListener(this);

        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        tvToptext = (YanxiuTypefaceTextView) findViewById(R.id.tv_top_title);
        tvToptext.setTypefaceName(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD);
        ivAnswerCard = (ImageView) findViewById(R.id.iv_answer_card);

        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        ivFavCard = (ImageView) findViewById(R.id.iv_fav_card);
        vpAnswer = (ViewPager) findViewById(R.id.answer_viewpager);
        vpAnswer.setOffscreenPageLimit(4);
        tvPagerIndex = (TextView) findViewById(R.id.tv_pager_index);
        tvPagerCount = (TextView) this.findViewById(R.id.tv_pager_count);
        //=============================================
        //反射viewPager里面的mScroller
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(vpAnswer.getContext(),new AccelerateInterpolator());
            mField.set(vpAnswer, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //=============================================
        vpAnswer.setOnPageChangeListener(this);
        tvQuestionTitle = (TextView) this.findViewById(R.id.tv_anwser_tips);
        adapter = new AnswerAdapter(getSupportFragmentManager());
    }

    protected void initData(){
        dataSources =  (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
        //String jsonString = "{\"ret\":0,\"data\":[{\"id\":\"2734957\",\"type_id\":\"5\",\"difficulty\":\"1\",\"template\":\"multi\",\"stem\":\"<p>One evening, it was raining and the wind was blowing hard. An old couple came to a small hotel and wanted to stay there for the night. A young man welcomed them warmly, but said “I’m sorry! Our rooms here are all full and the hotels nearby are all full too, for there will be an important meeting held here tomorrow.”<\\/p><p>Hearing the young man’s words, the old couple felt very disappointed, and turned around to leave.<\\/p><p>Just as they were leaving, the young man came up to them and stopped them: “Madam and sir, if you don’t mind, you can sleep in my bedroom for a night…”<\\/p><p>The next morning, the old couple took out lots of money to give it to the young man, but he refused to take it.<\\/p><p>“No! You needn’t pay me any money, for I only lend my room to you.” said the young man with a smile on his face.<\\/p><p>“You’re great, young man! It’s very kind of you. Maybe one day, I’ll build a hotel for you!” said the old man. With these words, the old couple left. The young man only laughed and went on working.<\\/p><p>Several years later, the young man got a letter from the old couple, inviting him to go to Manhattan. The young man met the old couple in front of a five-star hotel.<\\/p><p>“Do you still remember what I said to you several years ago? Look! ________” said the old man. Soon, the young man became the manager of the hotel.<\\/p><p><br\\/><\\/p>\",\"answer\":[null],\"content\":null,\"analysis\":\"\",\"point\":[{\"id\":\"1033\",\"name\":\"社会主义现代化成就\"}],\"children\":[{\"id\":\"2734958\",\"type_id\":\"1\",\"difficulty\":\"1\",\"template\":\"choice\",\"stem\":\"<p>testtesds<\\/p><br\\/>\",\"answer\":[\"3\"],\"content\":{\"choices\":[\"asdfas\",\"dsafasf\",\"asdfas\",\"sadfasf\"]},\"analysis\":\"asdfasfs\",\"point\":null},{\"id\":\"2734959\",\"type_id\":\"4\",\"difficulty\":\"1\",\"template\":\"alter\",\"stem\":\"asdfasfasfasfsaf\",\"answer\":[1],\"content\":null,\"analysis\":\"ddd\",\"point\":null}]}]}\n";
        //dataSources = JSON.parseObject(jsonString, SubjectExercisesItemBean.class);

        if(dataSources==null){
            LogInfo.log("geny","dataSources==null");
            return;
        }

        if(dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()){
            LogInfo.log(TAG,"dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()");
            this.finish();
        }else{
            LogInfo.log(TAG,"dataSources： "+dataSources);
            List<PaperTestEntity> dataList = dataSources.getData().get(0).getPaperTest();
            if(dataList != null && !dataList.isEmpty()){
//                /**我的错题测试用*/
//                if (this instanceof WrongAnswerViewActivity){
//                    PaperTestEntity paperTestEntity=dataList.get(0);
//                    dataSources.getData().get(0).getPaperTest().add(paperTestEntity);
//                }
                QuestionUtils.addChildQuestionToParent(dataList);     //对题目的pageIndex childPageIndex,positionForCard,childPositionForCard进行赋值
                adapter.addDataSources(dataSources);
                vpAnswer.setAdapter(adapter);
                adapter.setViewPager(vpAnswer);
                LogInfo.log(TAG, "Adapter Refresh ");
                pageCount = adapter.getCount();

                if(1 == adapter.getTotalCount()){
                    btnNextQuestion.setVisibility(View.GONE);
                }

                ivBack.setOnClickListener(this);
                ivAnswerCard.setOnClickListener(this);
                ivFavCard.setOnClickListener(this);
                if(!TextUtils.isEmpty(dataSources.getData().get(0).getName())){
                    tvQuestionTitle.setText(dataSources.getData().get(0).getName());
                }
            }
        }
        setReportError();
    }

    protected void setReportError(){
        if(Configuration.isDebug()){
            btnWrongError = (Button) this.findViewById(R.id.btn_report_error);
            btnWrongError.setVisibility(View.VISIBLE);
            btnWrongError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataSources != null && dataSources.getData() != null &&
                            dataSources.getData().get(0) != null &&
                            dataSources.getData().get(0).getPaperTest() != null &&
                            !dataSources.getData().get(0).getPaperTest().isEmpty()) {
                        StringBuffer sb = new StringBuffer();
                        for (String srt : dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getAnswer()) {
                            sb.append("-" + srt + "-");
                        }
                        Util.showToast(sb.toString());

//                        RequestFeedBackTask.startFeedBack(BaseAnswerViewActivity.this, dataSources.getData().get(0).getPaperTest().get(currentIndex).getQid() + "");

                    }
                }
            });
        }
    }



    protected int vpState;


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        currentIndex = position;

//        btnLastQuestion.setVisibility(View.VISIBLE);
//        btnNextQuestion.setVisibility(View.VISIBLE);
//        if(vpAnswer.getCurrentItem() == 0){
//            btnLastQuestion.setVisibility(View.GONE);
//        }else if(vpAnswer.getCurrentItem() == adapter.getTotalCount() - 1){
//            btnNextQuestion.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        vpState = state;
    }

    @Override
    public void onClick(View v) {
        if(v == btnLastQuestion){
            flag=true;
            nextPager_onclick = 1;
            btnNextQuestion.setVisibility(View.VISIBLE);
//            if(vpAnswer.getCurrentItem() != 0){
                if (listener!=null) {
                    int tatle_count=((AnswerAdapter)listener).getCount();
                    int currenItem=((AnswerAdapter)listener).getViewPagerCurrentItem();

                    if (currenItem==1&&vpAnswer.getCurrentItem()==0){
                        btnLastQuestion.setVisibility(View.GONE);
                    }else {
                        btnLastQuestion.setVisibility(View.VISIBLE);
                    }

                    if (currenItem==0){
                        YanXiuConstant.OnClick_TYPE=1;
                        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() - 1));
                    }else {
                        ((AnswerAdapter)listener).setPagerLift();
                    }
                }else {
                    YanXiuConstant.OnClick_TYPE=1;
                    vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() - 1));
                }
//            }


        }else if(v == btnNextQuestion){
            btnLastQuestion.setVisibility(View.VISIBLE);
//            if(vpAnswer.getCurrentItem() != adapter.getTotalCount() - 1 || vpAnswer.getCurrentItem() != adapter.getListCount() - 1){
                LogInfo.log(vpAnswer.getCurrentItem()+"");
                LogInfo.log(adapter.getTotalCount()+"");
                if (listener!=null) {
                    int tatle_count=((AnswerAdapter)listener).getCount();
                    int currenItem=((AnswerAdapter)listener).getViewPagerCurrentItem();
                    if (vpAnswer.getCurrentItem() == adapter.getCount() - 1&&tatle_count-2==currenItem){
                        btnNextQuestion.setVisibility(View.GONE);
                    }else {
                        btnNextQuestion.setVisibility(View.VISIBLE);
                    }
                    if (currenItem<tatle_count-1) {
                        listener.flipNextPager(listener);
                    }else {
                        if (vpAnswer.getCurrentItem()<adapter.getCount()-1) {
                            vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
                        }
                    }
                }else {
                    if (vpAnswer.getCurrentItem()<adapter.getCount()-1) {
                        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
                    }
                }
//                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
//            }
        }
    }

    public SubjectExercisesItemBean getDataSources() {
        return dataSources;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener=listener;
        if (listener!=null) {
            int tatle_count = ((AnswerAdapter) listener).getCount();
            int currenItem = ((AnswerAdapter) listener).getViewPagerCurrentItem();
            if (vpAnswer.getCurrentItem() == adapter.getCount() - 1 && tatle_count - 1 == currenItem) {
                btnNextQuestion.setVisibility(View.GONE);
            } else {
                btnNextQuestion.setVisibility(View.VISIBLE);
            }
            if (currenItem==0&&vpAnswer.getCurrentItem()==0){
                btnLastQuestion.setVisibility(View.GONE);
            }else {
                btnLastQuestion.setVisibility(View.VISIBLE);
            }
        }else {
            if(vpAnswer.getCurrentItem() == 0){
                btnLastQuestion.setVisibility(View.GONE);
            }else{
                btnLastQuestion.setVisibility(View.VISIBLE);
            }

            if(vpAnswer.getCurrentItem() == adapter.getCount() - 1){
                btnNextQuestion.setVisibility(View.GONE);
            }else {
                btnNextQuestion.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setPagerSelect(int count,int position){
        if (vpAnswer.getCurrentItem() == adapter.getCount() - 1 && count - 1 == position) {
            btnNextQuestion.setVisibility(View.GONE);
        } else {
            btnNextQuestion.setVisibility(View.VISIBLE);
        }
        if (vpAnswer.getCurrentItem()==0&&position==0){
            btnLastQuestion.setVisibility(View.GONE);
        }else {
            btnLastQuestion.setVisibility(View.VISIBLE);
        }
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

    public class FixedSpeedScroller extends Scroller {
        protected int mDuration = 200;

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

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void setNextShow(){
        btnNextQuestion.setVisibility(View.VISIBLE);
    }
}
