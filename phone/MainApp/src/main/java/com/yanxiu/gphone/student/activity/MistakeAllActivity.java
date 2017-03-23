package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.WrongAllListAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.MistakeRedoNumberBean;
import com.yanxiu.gphone.student.bean.PageBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.YanxiuPageInfoBean;
import com.yanxiu.gphone.student.bean.statistics.MistakeCountBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.MisRedoNumQuestionTask;
import com.yanxiu.gphone.student.requestTask.RequestDelMistakeTask;
import com.yanxiu.gphone.student.requestTask.RequestMistakeRedoClassTask;
import com.yanxiu.gphone.student.requestTask.RequestWrongAllQuestionTask;
import com.yanxiu.gphone.student.utils.LoadingDialogUtils;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.ChapterTabTitleLayout;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JS-00 on 2016/6/23.
 */
public class MistakeAllActivity extends YanxiuBaseActivity{
    private PublicLoadLayout rootView;
    private int pageIndex = 1;
    private TextView backView;
    private TextView titleView;
    private TextView wrongNumView;
    private XListView listView;
    private String title;
    private String stageId;
    private String subjectId;
    private String wrongNum;
    private int ptype = 2;
    //private int currentPage = 1;
    private final int pageSize = YanXiuConstant.YX_PAGESIZE_CONSTANT;
    private int mMistakeCount;

    private WrongAllListAdapter wrongAllListAdapter;
    private List<PaperTestEntity> dataList = new ArrayList<PaperTestEntity>();
    private ArrayList<ExercisesDataEntity> exercisesList = new ArrayList<ExercisesDataEntity>();
    private RequestWrongAllQuestionTask mRequestWrongAllQuestionTask;
    private SubjectExercisesItemBean mSubjectExercisesItemBean;
    private SubjectExercisesItemBean subjectExercisesItemBeanIntent = new SubjectExercisesItemBean();;
    private MistakeRedoNumberBean numberBean;
    private Button mistake_number;
    private boolean Is_number_ready=false;
    private boolean Is_number_click=false;
    private RelativeLayout linear_number;


    public static void launch (Activity activity, String title, String subjectId, String wrongNum) {
        Intent intent = new Intent(activity, MistakeAllActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("wrongNum", wrongNum);
        activity.startActivityForResult(intent,00);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectExercisesItemBeanIntent.setIsWrongSet(true);
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        title = getIntent().getStringExtra("title");
        subjectId = getIntent().getStringExtra("subjectId");
        wrongNum = getIntent().getStringExtra("wrongNum");
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_mistake_all_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override public void refreshData() {
                pageIndex = 1;
                requestMistakeAllList(false, true, false);
            }
        });
        setContentView(rootView);
        EventBus.getDefault().register(this);
        findView();
        requestMistakeAllList(false, true, false);
    }

    private void findView() {
        View top_space_view=findViewById(R.id.top_space_view);
        top_space_view.setVisibility(View.VISIBLE);
        backView = (TextView)findViewById(R.id.pub_top_left);
        titleView = (TextView)findViewById(R.id.pub_top_mid);
        titleView.setText(title);
        wrongNumView = (TextView)findViewById(R.id.answer_exam_wrong_num_text);
        mMistakeCount = new Integer(wrongNum);
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, 0+""));
        listView = (XListView) findViewById(R.id.mistack_all_list);
        wrongAllListAdapter = new WrongAllListAdapter(this);
        listView.setAdapter(wrongAllListAdapter);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view,
                                              int position, long id) {
                if (wrongAllListAdapter != null) {
                    List<PaperTestEntity> list = wrongAllListAdapter.getList();
                    if (subjectExercisesItemBeanIntent.getData() != null && subjectExercisesItemBeanIntent.getData().size() > 0) {
                        subjectExercisesItemBeanIntent.getData().get(0).setName("");
                    }
                    if (list != null && list.size() > 0 && position>0 && position-1 < list.size()) {
                        WrongAnswerViewActivity.launch(MistakeAllActivity.this, subjectExercisesItemBeanIntent, subjectId, pageIndex, list.get(position-1).getQuestions().getChildPageIndex(), YanXiuConstant.WRONG_REPORT, String.valueOf(mMistakeCount), position-1);
                        //ResolutionAnswerViewActivity.launch(MistakeAllActivity.this, subjectExercisesItemBeanIntent, YanXiuConstant.INTELLI_REPORT);
//                        MistakeRedoActivity.launch(MistakeAllActivity.this, subjectExercisesItemBeanIntent, subjectId, pageIndex, list.get(position-1).getQuestions().getChildPageIndex(), YanXiuConstant.WRONG_REPORT, String.valueOf(mMistakeCount), position-1);
                    } else {
                        WrongAnswerViewActivity.launch(MistakeAllActivity.this, subjectExercisesItemBeanIntent, subjectId, pageIndex, 0, YanXiuConstant.WRONG_REPORT, String.valueOf(mMistakeCount), position);
                    }
                }
            }
        });

        backView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cancelTaskAndFinish();
            }
        });

        linear_number= (RelativeLayout) findViewById(R.id.linear_number);
        mistake_number= (Button) findViewById(R.id.mistake_number);
//        if (title.equals(getResources().getString(R.string.mistake_redo_math))||title.equals(getResources().getString(R.string.mistake_redo_english))){
            requestMistakeNumber();
            mistake_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootView.loading(true);
                    if (Is_number_ready) {
                        MistakeNumClick();
                    }else {
                        Is_number_click=true;
                        requestMistakeNumber();
                    }
                }
            });
//        }else {
//            linear_number.setVisibility(View.GONE);
//        }
    }

    private void MistakeNumClick(){
        RequestMistakeRedoClassTask redoClassTask=new RequestMistakeRedoClassTask(this, stageId, subjectId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                rootView.finish();
                SubjectExercisesItemBean itemBean= (SubjectExercisesItemBean) result;
                if (itemBean!=null&&itemBean.getStatus().getCode()==0&&itemBean.getData()!=null&&itemBean.getData().size()>0) {
                    MistakeRedoActivity.launch(MistakeAllActivity.this,itemBean,numberBean.getProperty().getQuestionNum()+"",stageId,subjectId);
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                rootView.finish();
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.server_connection_erro);
                }else {
                    Util.showToast(R.string.data_erro);
                }
            }
        });
        redoClassTask.start();
    }

    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener(){

        @Override public void onRefresh(XListView view) {
            if(NetWorkTypeUtils.isNetAvailable()){
                pageIndex = 1;
                requestMistakeAllList(true, false, false);
            }else {
                listView.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }

        @Override public void onLoadMore(XListView view) {
            if(NetWorkTypeUtils.isNetAvailable()){
                requestMistakeAllList(false, false, true);
            }else {
                listView.stopLoadMore();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
    };

    private void requestMistakeNumber(){
//        rootView.loading(true);
        MisRedoNumQuestionTask numQuestionTask=new MisRedoNumQuestionTask(this, stageId, subjectId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
//                rootView.loading(false);
                if (!isMistakeNumReady) {
                    isMistakeNumReady = true;
                    httpEnd();
                }
                numberBean= (MistakeRedoNumberBean) result;
                if (numberBean!=null&&numberBean.getStatus().getCode()==0) {
                    String s = getResources().getString(R.string.mistake_redo_number, numberBean.getProperty().getQuestionNum() + "");
                    mistake_number.setText(s);
                    if (numberBean.getProperty().getQuestionNum()==0){
                        mistake_number.setClickable(false);
                        mistake_number.setAlpha(0.6f);
                    }else {
                        mistake_number.setClickable(true);
                        mistake_number.setAlpha(1f);
                    }
                    Is_number_ready=true;
                    if (Is_number_click){
                        MistakeNumClick();
                    }
                }else {
                    rootView.finish();
                    Util.showToast(R.string.data_erro);
                }
                Is_number_click=false;
                linear_number.setVisibility(View.VISIBLE);
            }

            @Override
            public void dataError(int type, String msg) {
                Is_number_ready=false;
                Is_number_click=false;
                isMistakeNumReady=true;
                httpEnd();
                linear_number.setVisibility(View.VISIBLE);
                mistake_number.setClickable(true);
                mistake_number.setAlpha(1f);
                if (!NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.server_connection_erro);
                }else {
                    Util.showToast(R.string.data_erro);
                }

            }
        });
        numQuestionTask.start();
    }

    private boolean isWrongListReady=false;
    private boolean isMistakeNumReady=false;

    private void httpEnd(){
        if (isWrongListReady&&isMistakeNumReady) {
            rootView.finish();
        }
    }

    private void requestMistakeAllList(final boolean isRefresh,final boolean showLoading,
                               final boolean isLoaderMore){
        if(showLoading){
            rootView.loading(true);
        }
        int page = pageIndex;
        if(isLoaderMore){
            page +=1;
        }
        final int currentPage = page;
        if (NetWorkTypeUtils.isNetAvailable()) {
            if (mRequestWrongAllQuestionTask != null) {
                mRequestWrongAllQuestionTask.cancel();
                mRequestWrongAllQuestionTask = null;
            }
            mRequestWrongAllQuestionTask = new RequestWrongAllQuestionTask(this, stageId, subjectId, page, "0", ptype, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
//                    rootView.finish();
                    isWrongListReady=true;
                    httpEnd();
                    listView.stopRefresh();
                    listView.stopLoadMore();

                    mSubjectExercisesItemBean = (SubjectExercisesItemBean) result;
                    if (mSubjectExercisesItemBean.getData()!=null&&mSubjectExercisesItemBean.getPage()!=null) {
                        wrongNumView.setVisibility(View.VISIBLE);
                        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mSubjectExercisesItemBean.getPage().getTotalCou() + ""));
                        mMistakeCount = mSubjectExercisesItemBean.getPage().getTotalCou();

                        QuestionUtils.settingAnswer(mSubjectExercisesItemBean);
                        QuestionUtils.initDataWithAnswer(mSubjectExercisesItemBean);

                        ArrayList<ExercisesDataEntity> exerciseData = mSubjectExercisesItemBean.getData();
                        List<PaperTestEntity> data = new ArrayList<PaperTestEntity>();
                        for (int i = 0; i < exerciseData.size(); i++) {
                            data.addAll(exerciseData.get(i).getPaperTest());
                        }

                        QuestionUtils.CleanData(data);

                        if (isLoaderMore) {
//                        pageIndex += 1;
                        } else if (isRefresh) {
//                        pageIndex = 1;
                            dataList.clear();
                            exercisesList.clear();
                        }

                        if (data != null && data.size() > 0) {
                            if (isLoaderMore) {
                                pageIndex += 1;
                            } else if (isRefresh) {
                                pageIndex = 1;
//                            dataList.clear();
//                            exercisesList.clear();
                            }
                            if (exercisesList.size() != 0) {
                                exercisesList.get(0).getPaperTest().addAll(exerciseData.get(0).getPaperTest());
                            } else {
                                exercisesList.addAll(exerciseData);
                            }
                            //

                            subjectExercisesItemBeanIntent.setData(exercisesList);
                            //subjectExercisesItemBeanIntent.getData().get(0).getPaperTest().addAll(exerciseData.get(0).getPaperTest());
                            dataList.addAll(data);
                            YanxiuPageInfoBean pageBean = mSubjectExercisesItemBean.getPage();
                            if (pageBean != null) {
                                if (pageIndex == pageBean.getTotalPage()) {
                                    listView.setPullLoadEnable(false);
                                } else {
                                    listView.setPullLoadEnable(true);
                                }
                            } else {
                                listView.setPullLoadEnable(false);
                            }
                            updateUI();
                        } else {
                            if (isRefresh) {
//                                dataList.clear();
//                                exercisesList.clear();
//                                updateUI();
                                MistakeAllActivity.this.finish();
                            }
//                        rootView.dataNull(getResources().getString(R.string.no_group_hw_list_tip));
//                    noCommentView.setVisibility(View.VISIBLE;);
                        }
                    }else {
                        if (isRefresh) {
//                            dataList.clear();
//                            exercisesList.clear();
//                            updateUI();
                            MistakeAllActivity.this.finish();
                        }
                    }

                }

                @Override
                public void dataError(int type, String msg) {
//                    rootView.finish();
                    isWrongListReady=true;
                    httpEnd();
                    wrongNumView.setVisibility(View.VISIBLE);
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    if (isRefresh || isLoaderMore) {
                        if (!StringUtils.isEmpty(msg)) {
                            Util.showUserToast(msg, null, null);
                        } else {
                            Util.showUserToast(R.string.net_null_one, -1, -1);
                        }
                    } else if (showLoading) {
                        rootView.netError(true);
                    }
                }
            });
            mRequestWrongAllQuestionTask.start();
        } else {
            new YanxiuSimpleAsyncTask<SubjectExercisesItemBean>(this) {
                @Override
                public SubjectExercisesItemBean doInBackground() {
                    SubjectExercisesItemBean mBean = null;
                    try {
                        ArrayList<ExercisesDataEntity> data = null;
                        ExercisesDataEntity mExercisesDataEntity = PublicErrorQuestionCollectionBean.findExercisesDataEntityWithAll(stageId,
                                subjectId, (currentPage - 1) * YanXiuConstant
                                        .YX_PAGESIZE_CONSTANT);
                        mBean = new SubjectExercisesItemBean();
                        mBean.setIsResolution(true);
                        data = new ArrayList<ExercisesDataEntity>();
                        if (mExercisesDataEntity != null) {
                            data.add(mExercisesDataEntity);
                        }
                        mBean.setData(data);
                    } catch (Exception e) {
                    }
                    return mBean;
                }

                @Override
                public void onPostExecute(SubjectExercisesItemBean result) {
                    rootView.finish();
                    if (result != null && result.getData() != null && result.getData().size()>0) {
                        QuestionUtils.initDataWithAnswer(result);
                        pageIndex++;
                        wrongNumView.setVisibility(View.VISIBLE);
                        dataList.addAll(result.getData().get(0).getPaperTest());
                        if(wrongAllListAdapter != null){
                            wrongAllListAdapter.setList(dataList);
                        }
                    } else {
                        if (!NetWorkTypeUtils.isNetAvailable()) {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 更新列表UI
     * */
    private void updateUI(){
        rootView.finish();
//        noCommentView.setVisibility(View.GONE);
        listView.setScrollable(true);
        listView.setPullRefreshEnable(true);
        if(wrongAllListAdapter != null){
            wrongAllListAdapter.setList(dataList);
        }
    }

    private void forResult(){
        Intent intent = new Intent();
        boolean resultToRefresh = true;
        if(wrongAllListAdapter == null || wrongAllListAdapter.getCount() <= 0) {
            resultToRefresh = false;
        }
        intent.putExtra("toRefresh", resultToRefresh);
        setResult(RESULT_OK, intent);
    }

    private void cancelTaskAndFinish(){
        cancelTask();
        forResult();
        MistakeAllActivity.this.finish();
    }

    private void cancelTask() {
        if (mRequestWrongAllQuestionTask != null) {
            mRequestWrongAllQuestionTask.cancel();
            mRequestWrongAllQuestionTask = null;
        }
    }

    public void onEventMainThread(MistakeCountBean event) {
        mMistakeCount = mMistakeCount - 1;
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mMistakeCount+""));
        pageIndex = 1;
        requestMistakeAllList(true, false, false);
        requestMistakeNumber();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, data.getIntExtra("wrongNum", 0)+""));
            mMistakeCount = data.getIntExtra("wrongNum", 0);
            if (mMistakeCount==0){
                this.finish();
            }else {
                pageIndex = 1;
                requestMistakeAllList(true, false, false);
                requestMistakeNumber();
            }
        }else {
            pageIndex = 1;
            requestMistakeAllList(true, false, false);
            requestMistakeNumber();
        }

    }

    @Override protected void onDestroy() {
//        Intent intent=new Intent();
//        setResult(00,intent);
        super.onDestroy();
        cancelTask();
        EventBus.getDefault().unregister(this);
    }
}
