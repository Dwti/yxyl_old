package com.yanxiu.gphone.studentold.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.adapter.AnswerAdapter;
import com.yanxiu.gphone.studentold.adapter.MistakeRedoAdapter;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.AnswerBean;
import com.yanxiu.gphone.studentold.bean.PaperTestEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.utils.Configuration;
import com.yanxiu.gphone.studentold.utils.CorpUtils;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.QuestionUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.MyViewPager;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;
import com.yanxiu.gphone.studentold.view.StudentLoadingLayout;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.studentold.view.question.QuestionsListener;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Administrator on 2015/7/6.
 */
public class BaseAnswerViewActivity extends YanxiuBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, QuestionsListener {

    private final static String TAG = BaseAnswerViewActivity.class.getSimpleName();
    protected PublicLoadLayout mRootView;
    protected static SubjectExercisesItemBean dataSources;

    protected AnswerAdapter adapter;
    protected MistakeRedoAdapter mistakeRedoAdapter;

    protected ImageView ivBack;
    protected MyViewPager vpAnswer;
    public YanxiuTypefaceTextView tvToptext;
    protected TextView tvPagerIndex;
    protected TextView tvPagerCount;
    protected ImageView ivAnswerCard;
    protected ImageView ivFavCard;
    protected boolean flag = false;   //是否点了上一题按钮切换
//    private static BaseAnswerViewActivity act;

    protected int pageCount;
    protected int currentIndex;
    protected FrameLayout gif_framelayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    protected LinearLayout answer_view_type;

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

//    protected Button btnLastQuestion, btnNextQuestion;
    protected QuestionsListener listener;
    protected int nextPager_onclick = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_answer_question);
        loadingLayout = (StudentLoadingLayout) mRootView.findViewById(R.id.loading_layout);
        setContentView(mRootView);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("data",dataSources);
        super.onSaveInstanceState(outState);
    }

    public boolean getCurrent(int fragment_ID){
        try {
            Fragment fragment = null;
            if (adapter!=null&&adapter.getmFragments().size()>0) {
                fragment = adapter.getmFragments().get(vpAnswer.getCurrentItem());
            }
            if (fragment==null){
                fragment = mistakeRedoAdapter.getFragmentAtNow();
            }
            if (fragment.hashCode() == fragment_ID) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.toString();
        }
        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (dataSources==null&&savedInstanceState!=null&&savedInstanceState.getSerializable("data")!=null){
            dataSources= (SubjectExercisesItemBean) savedInstanceState.getSerializable("data");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showDialog() {
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
    }


    public void showCommonDialog() {
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
    }


    public void hideDialog() {
        loadingLayout.setViewGone();
    }


    protected void initView() {
        answer_view_type=(LinearLayout) this.findViewById(R.id.answer_view_type);
        gif_framelayout = (FrameLayout) this.findViewById(R.id.gif_framelayout);
//        try {
//            btnLastQuestion = (Button) this.findViewById(R.id.btn_last_question);
//            btnLastQuestion.setVisibility(View.GONE);
//            btnNextQuestion = (Button) this.findViewById(R.id.btn_next_question);
//            btnNextQuestion.setVisibility(View.VISIBLE);
//
//            btnNextQuestion.setOnClickListener(this);
//            btnLastQuestion.setOnClickListener(this);
//        }catch (Exception e){}


        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        tvToptext = (YanxiuTypefaceTextView) findViewById(R.id.tv_top_title);
        tvToptext.setTypefaceName(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD);
        ivAnswerCard = (ImageView) findViewById(R.id.iv_answer_card);

        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        ivFavCard = (ImageView) findViewById(R.id.iv_fav_card);
        vpAnswer = (MyViewPager) findViewById(R.id.answer_viewpager);
//        vpAnswer.setOffscreenPageLimit(4);
        tvPagerIndex = (TextView) findViewById(R.id.tv_pager_index);
        tvPagerCount = (TextView) this.findViewById(R.id.tv_pager_count);

        /*WebView webview_test_one = (WebView) this.findViewById(R.id.webview_test_one);
        webview_test_one.loadUrl("http://www.sohu.com");
        webview_test_one.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });
        WebView webview_test_two = (WebView) this.findViewById(R.id.webview_test_two);
        WebView webview_test_three = (WebView) this.findViewById(R.id.webview_test_three);
        WebView webview_test_four = (WebView) this.findViewById(R.id.webview_test_four);
        WebView webview_test_five = (WebView) this.findViewById(R.id.webview_test_five);
        webview_test_two.loadUrl("http://www.sohu.com");
        webview_test_two.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });
        webview_test_three.loadUrl("http://www.sohu.com");
        webview_test_three.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });
        webview_test_four.loadUrl("http://www.sohu.com");
        webview_test_four.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });
        webview_test_five.loadUrl("http://www.sohu.com");
        webview_test_five.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
        });*/
        //=============================================
        //反射viewPager里面的mScroller
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(vpAnswer.getContext(), new AccelerateInterpolator());
            mField.set(vpAnswer, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //=============================================
        vpAnswer.setOnPageChangeListener(this);
        tvQuestionTitle = (TextView) this.findViewById(R.id.tv_anwser_tips);
        adapter = new AnswerAdapter(getSupportFragmentManager());
    }

    protected void initData() {
        initDataSource();
        if (dataSources == null) {
            return;
        }

        if (dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()) {
            this.finish();
        } else {
            LogInfo.log(TAG, "dataSources： " + dataSources);
            List<PaperTestEntity> dataList = dataSources.getData().get(0).getPaperTest();
            if (dataList != null && !dataList.isEmpty()) {
//                /**我的错题测试用*/
                QuestionUtils.addChildQuestionToParent(dataList);     //对题目的pageIndex childPageIndex,positionForCard,childPositionForCard进行赋值
                adapter.addDataSources(dataSources);
                vpAnswer.setAdapter(adapter);
                adapter.setViewPager(vpAnswer);
                pageCount = adapter.getCount();

                ivBack.setOnClickListener(this);
                ivAnswerCard.setOnClickListener(this);
                ivFavCard.setOnClickListener(this);
                if (!TextUtils.isEmpty(dataSources.getData().get(0).getName())) {
                    tvQuestionTitle.setText(dataSources.getData().get(0).getName());
                }
            }
        }
        setReportError();
    }

    protected void initDataSource(){
        if (getIntent()!=null){
            if (getIntent().getSerializableExtra("subjectExercisesItemBean")!=null){
                dataSources = (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
            }
        }
    }

    protected void setReportError() {
        if (Configuration.isDebug()) {
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
        String ss="";
        ss="1";
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
        CorpUtils.getInstence().setClear();
    }

    @Override
    public void onClick(View v) {
//        if (v == btnLastQuestion) {
//            flag = true;
//            nextPager_onclick = 1;
//            btnNextQuestion.setVisibility(View.VISIBLE);
////            if(vpAnswer.getCurrentItem() != 0){
//            if (listener != null) {
//                int tatle_count = ((AnswerAdapter) listener).getCount();
//                int currenItem = ((AnswerAdapter) listener).getViewPagerCurrentItem();
//
//                if (currenItem == 1 && vpAnswer.getCurrentItem() == 0) {
//                    btnLastQuestion.setVisibility(View.GONE);
//                } else {
//                    btnLastQuestion.setVisibility(View.VISIBLE);
//                }
//
//                if (currenItem == 0) {
//                    YanXiuConstant.OnClick_TYPE = 1;
//                    vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() - 1));
//                } else {
//                    ((AnswerAdapter) listener).setPagerLift();
//                }
//            } else {
//                YanXiuConstant.OnClick_TYPE = 1;
//                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() - 1));
//            }
////            }
//
//
//        } else if (v == btnNextQuestion) {
//            btnLastQuestion.setVisibility(View.VISIBLE);
////            if(vpAnswer.getCurrentItem() != adapter.getTotalCount() - 1 || vpAnswer.getCurrentItem() != adapter.getListCount() - 1){
//            LogInfo.log(vpAnswer.getCurrentItem() + "");
//            LogInfo.log(adapter.getTotalCount() + "");
//            if (listener != null) {
//                int tatle_count = ((AnswerAdapter) listener).getCount();
//                int currenItem = ((AnswerAdapter) listener).getViewPagerCurrentItem();
//                if (vpAnswer.getCurrentItem() == adapter.getCount() - 1 && tatle_count - 2 == currenItem) {
//                    btnNextQuestion.setVisibility(View.GONE);
//                } else {
//                    btnNextQuestion.setVisibility(View.VISIBLE);
//                }
//                if (currenItem < tatle_count - 1) {
//                    listener.flipNextPager(listener);
//                } else {
//                    if (vpAnswer.getCurrentItem() < adapter.getCount() - 1) {
//                        vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
//                    }
//                }
//            } else {
//                if (vpAnswer.getCurrentItem() < adapter.getCount() - 1) {
//                    vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
//                }
//            }
////                vpAnswer.setCurrentItem((vpAnswer.getCurrentItem() + 1));
////            }
//        }
    }

    public SubjectExercisesItemBean getDataSources() {
        return dataSources;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
//        if (listener != null) {
//            int tatle_count = ((AnswerAdapter) listener).getCount();
//            int currenItem = ((AnswerAdapter) listener).getViewPagerCurrentItem();
//            if (vpAnswer.getCurrentItem() == adapter.getCount() - 1 && tatle_count - 1 == currenItem) {
//                btnNextQuestion.setVisibility(View.GONE);
//            } else {
//                btnNextQuestion.setVisibility(View.VISIBLE);
//            }
//            if (currenItem == 0 && vpAnswer.getCurrentItem() == 0) {
//                btnLastQuestion.setVisibility(View.GONE);
//            } else {
//                btnLastQuestion.setVisibility(View.VISIBLE);
//            }
//        } else {
//            if (vpAnswer.getCurrentItem() == 0) {
//                btnLastQuestion.setVisibility(View.GONE);
//            } else {
//                btnLastQuestion.setVisibility(View.VISIBLE);
//            }
//
//            if (vpAnswer.getCurrentItem() == adapter.getCount() - 1) {
//                btnNextQuestion.setVisibility(View.GONE);
//            } else {
//                btnNextQuestion.setVisibility(View.VISIBLE);
//            }
//        }
    }

    public void setPagerSelect(int count, int position) {
//        if (vpAnswer.getCurrentItem() == adapter.getCount() - 1 && count - 1 == position) {
//            btnNextQuestion.setVisibility(View.GONE);
//        } else {
//            btnNextQuestion.setVisibility(View.VISIBLE);
//        }
//        if (vpAnswer.getCurrentItem() == 0 && position == 0) {
//            btnLastQuestion.setVisibility(View.GONE);
//        } else {
//            btnLastQuestion.setVisibility(View.VISIBLE);
//        }
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("BaseAnswerView Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class FixedSpeedScroller extends Scroller {
        protected int mDuration = 200;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
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

    protected void setNextShow() {
//        btnNextQuestion.setVisibility(View.VISIBLE);
    }
}
