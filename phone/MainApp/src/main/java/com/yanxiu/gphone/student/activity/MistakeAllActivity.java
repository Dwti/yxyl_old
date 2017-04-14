package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.common.login.model.UserInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.MistakeDetilsAdapter;
import com.yanxiu.gphone.student.adapter.WrongAllListAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.MistakeRedoNumberBean;
import com.yanxiu.gphone.student.bean.MistakeRefreshAllBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.YanxiuPageInfoBean;
import com.yanxiu.gphone.student.bean.statistics.MistakeCountBean;
import com.yanxiu.gphone.student.fragment.MistakeAllFragment;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.MisRedoNumQuestionTask;
import com.yanxiu.gphone.student.requestTask.RequestMistakeRedoClassTask;
import com.yanxiu.gphone.student.requestTask.RequestWrongAllQuestionTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JS-00 on 2016/6/23.
 */
public class MistakeAllActivity extends YanxiuBaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String FRAGMENT_TAG="mistake_all";
    public static final String MISTAKE_CHAPTER="chapter";
    public static final String MISTAKE_KONGLEDGE="kongledge";
    private PublicLoadLayout rootView;
    private int pageIndex = 1;
    private TextView backView;
    private TextView titleView;
    private TextView wrongNumView;
    private XListView listView;
    private String title;
    private String stageId;
    private String editionId;
    private String subjectId;
    private String wrongNum;
    private int ptype = 2;
    //private int currentPage = 1;
    private final int pageSize = YanXiuConstant.YX_PAGESIZE_CONSTANT;
    private int mMistakeCount;

    private UserInfo mUserinfoEntity = LoginModel.getUserinfoEntity();

    private WrongAllListAdapter wrongAllListAdapter;
    private List<PaperTestEntity> dataList = new ArrayList<PaperTestEntity>();
    private ArrayList<ExercisesDataEntity> exercisesList = new ArrayList<ExercisesDataEntity>();
    private RequestWrongAllQuestionTask mRequestWrongAllQuestionTask;
    private SubjectExercisesItemBean mSubjectExercisesItemBean;
    public static SubjectExercisesItemBean subjectExercisesItemBeanIntent;
    private MistakeRedoNumberBean numberBean;
    private Button mistake_number;
    private boolean Is_number_ready=false;
    private boolean Is_number_click=false;
    private RelativeLayout linear_number;
    private FrameLayout flFragemtView;
    private RelativeLayout rlXListTotalView;
    public static final int WRONG_LIST = 0x08;


    public static void launch (Activity activity, String title, String subjectId, String wrongNum,String editionId) {
        Intent intent = new Intent(activity, MistakeAllActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("wrongNum", wrongNum);
        intent.putExtra("editionId",editionId);
        activity.startActivityForResult(intent,00);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectExercisesItemBeanIntent = new SubjectExercisesItemBean();
        subjectExercisesItemBeanIntent.setIsWrongSet(true);
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        title = getIntent().getStringExtra("title");
        subjectId = getIntent().getStringExtra("subjectId");
        wrongNum = getIntent().getStringExtra("wrongNum");
        editionId=getIntent().getStringExtra("editionId");
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_mistake_all_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override public void refreshData() {
                pageIndex = 1;
                requestMistakeAllList(true, true, false);
            }
        });
        setContentView(rootView);
        EventBus.getDefault().register(this);
        findView();
        requestMistakeAllList(true, true, false);
    }

    private void findView() {
        View top_space_view=findViewById(R.id.top_space_views);
        top_space_view.setVisibility(View.VISIBLE);
        backView = (TextView)findViewById(R.id.pub_top_left);
        titleView = (TextView)findViewById(R.id.pub_top_mid);
        titleView.setText(title);

        RelativeLayout rlConverView= (RelativeLayout) findViewById(R.id.rl_conver_total);
        rlConverView.setVisibility(View.GONE);

        RadioGroup rgTitleView= (RadioGroup) findViewById(R.id.rg_title);
        rgTitleView.setOnCheckedChangeListener(this);
        RadioButton rbTotalView= (RadioButton) findViewById(R.id.rb_total);
        RadioButton rbChapterView= (RadioButton) findViewById(R.id.rb_chapter);
        RadioButton rbKongLedgeView= (RadioButton) findViewById(R.id.rb_kongledge);

//        RelativeLayout rlImageleft= (RelativeLayout) findViewById(R.id.rl_image1);
        RelativeLayout rlImagecenter= (RelativeLayout) findViewById(R.id.rl_image2);
        RelativeLayout rlImageright= (RelativeLayout) findViewById(R.id.rl_image3);

        flFragemtView= (FrameLayout) findViewById(R.id.fl_fragment);
        rlXListTotalView= (RelativeLayout) findViewById(R.id.rl_list_total);
        rbTotalView.setChecked(true);

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
        ft.replace(R.id.fl_fragment,new Fragment());
        ft.commitAllowingStateLoss();

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
                        List<PaperTestEntity> lists=new ArrayList<PaperTestEntity>();
                        lists.addAll(list);
                        subjectExercisesItemBeanIntent.getData().get(0).setPaperTest(lists);
                    }
                    if (list != null && list.size() > 0 && position>0 && position-1 < list.size()) {
                        WrongAnswerViewActivity.launch(MistakeAllActivity.this, subjectId, pageIndex, list.get(position-1).getQuestions().getChildPageIndex(), WRONG_LIST, String.valueOf(mMistakeCount), position-1);
                    } else {
                        WrongAnswerViewActivity.launch(MistakeAllActivity.this, subjectId, pageIndex, 0, WRONG_LIST, String.valueOf(mMistakeCount), position);
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
        int stageId=0;
        if (mUserinfoEntity!=null) {
            stageId = mUserinfoEntity.getStageid();
            if (stageId == MyStageSelectActivity.STAGE_TYPE_PRIM || stageId == MyStageSelectActivity.STAGE_TYPE_JUIN) {
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction fts=fm.beginTransaction();
                MistakeAllFragment fragment=null;
                Bundle bundle=new Bundle();
                bundle.putString("stageId",stageId+"");
                bundle.putString("subjectId",subjectId);
                bundle.putString("editionId",editionId);
                if (title.equals(getResources().getString(R.string.mistake_redo_math))) {
                    rlConverView.setVisibility(View.VISIBLE);
                    fragment=new MistakeAllFragment();
                    fragment.setArguments(bundle);
                    rgTitleView.setVisibility(View.VISIBLE);
                    rbKongLedgeView.setVisibility(View.VISIBLE);
                    rlImagecenter.setVisibility(View.VISIBLE);
                    rlImageright.setVisibility(View.VISIBLE);
                    fts.replace(R.id.fl_fragment,fragment,FRAGMENT_TAG);
                    fts.commitAllowingStateLoss();
                } else if (title.equals(getResources().getString(R.string.mistake_redo_english))){
                    rlConverView.setVisibility(View.VISIBLE);
                    fragment=new MistakeAllFragment();
                    fragment.setArguments(bundle);
                    rgTitleView.setVisibility(View.VISIBLE);
                    rbKongLedgeView.setVisibility(View.GONE);
                    rlImagecenter.setVisibility(View.VISIBLE);
                    rlImageright.setVisibility(View.GONE);
                    fts.replace(R.id.fl_fragment,fragment,FRAGMENT_TAG);
                    fts.commitAllowingStateLoss();
                } else {
                    rbChapterView.setVisibility(View.GONE);
                    rbKongLedgeView.setVisibility(View.GONE);
                    rlImagecenter.setVisibility(View.GONE);
                    rlImageright.setVisibility(View.GONE);
                }
            }
        }
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

        @Override
        public void onLoadMore(XListView view) {
            if(NetWorkTypeUtils.isNetAvailable()){
                if (wrongAllListAdapter!=null){
                    List<PaperTestEntity> list=wrongAllListAdapter.getList();
                    int count=list.size();
                    pageIndex=count/10;
                }
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
                            if (isRefresh) {
                                List<PaperTestEntity> datas = new ArrayList<PaperTestEntity>();
                                datas.addAll(data);
                                dataList.addAll(datas);
                            }else if (isLoaderMore){
                                if (dataList.size()==pageIndex*10){
                                    List<PaperTestEntity> datas = new ArrayList<PaperTestEntity>();
                                    datas.addAll(data);
                                    dataList.addAll(datas);
                                }else {
                                    for (int i=(pageIndex-1)*10;i<dataList.size();i++){
                                        dataList.set(i,data.get(0));
                                        data.remove(0);
                                    }
                                    List<PaperTestEntity> datas = new ArrayList<PaperTestEntity>();
                                    datas.addAll(data);
                                    dataList.addAll(datas);
                                }
                            }
                            try {
                                List<PaperTestEntity> dataLists = new ArrayList<PaperTestEntity>();
                                dataLists.addAll(dataList);
                                subjectExercisesItemBeanIntent.getData().get(0).setPaperTest(dataLists);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
        setRefresh();
    }

    public void onEventMainThread(WrongAllListAdapter.NoRefreshBean event) {
        mMistakeCount = mMistakeCount - 1;
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mMistakeCount+""));
//        pageIndex = 1;
//        requestMistakeAllList(true, false, false);
        requestMistakeNumber();
        setRefresh();
    }

    public class MistakeFragRefreshBean{}

    private void setRefresh(){
        MistakeFragRefreshBean bean=new MistakeFragRefreshBean();
        EventBus.getDefault().post(bean);
    }

    public void onEventMainThread(MistakeRefreshAllBean event) {
        mMistakeCount = mMistakeCount - 1;
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mMistakeCount+""));
        pageIndex = 1;
        requestMistakeAllList(true, false, false);
        requestMistakeNumber();
    }

    public void onEventMainThread(WrongAnswerViewActivity.WrongAnswerDeleteBean bean){
        try {
            int position=getIndexFromList(wrongAllListAdapter.getList(),bean.id);
            if (position!=-1) {
                wrongAllListAdapter.getList().remove(position);
                wrongAllListAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getIndexFromList(List<PaperTestEntity> list,int id){
        for (int i=0;i<list.size();i++){
            PaperTestEntity entity=list.get(i);
            if (entity.getId()==id){
                return i;
            }
        }
        return -1;
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
                List<PaperTestEntity> list=wrongAllListAdapter.getList();
                if (list!=null) {
                    int count = list.size();
                    if (count == 0) {
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
        }else {
            pageIndex = 1;
            requestMistakeAllList(true, false, false);
            requestMistakeNumber();
            setRefresh();
        }

    }

    @Override protected void onDestroy() {
//        Intent intent=new Intent();
//        setResult(00,intent);
        super.onDestroy();
        cancelTask();
        EventBus.getDefault().unregister(this);
        subjectExercisesItemBeanIntent = null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.rb_total:
                rlXListTotalView.setVisibility(View.VISIBLE);
                flFragemtView.setVisibility(View.GONE);
                break;
            case R.id.rb_chapter:
                rlXListTotalView.setVisibility(View.GONE);
                flFragemtView.setVisibility(View.VISIBLE);
                Fragment fragment=getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                if (fragment!=null&&fragment instanceof MistakeAllFragment){
                    MistakeAllFragment allFragment= (MistakeAllFragment) fragment;
                    allFragment.setData(MISTAKE_CHAPTER);
                }
                break;
            case R.id.rb_kongledge:
                rlXListTotalView.setVisibility(View.GONE);
                flFragemtView.setVisibility(View.VISIBLE);
                Fragment fragmentByTag=getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                if (fragmentByTag!=null&&fragmentByTag instanceof MistakeAllFragment){
                    MistakeAllFragment allFragment= (MistakeAllFragment) fragmentByTag;
                    allFragment.setData(MISTAKE_KONGLEDGE);
                }
                break;
        }
    }
}