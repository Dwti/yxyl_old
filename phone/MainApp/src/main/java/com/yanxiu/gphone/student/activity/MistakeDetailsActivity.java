package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.MistakeDetilsAdapter;
import com.yanxiu.gphone.student.adapter.WrongAllListAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.MistakeRefreshAllBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.YanxiuPageInfoBean;
import com.yanxiu.gphone.student.bean.statistics.MistakeCountBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestWrongAllQuestionTask;
import com.yanxiu.gphone.student.requestTask.RequestWrongDetailsTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/6 15:31.
 * Function :
 */

public class MistakeDetailsActivity extends YanxiuBaseActivity {

    private PublicLoadLayout rootView;
    private int pageIndex = 1;
    private TextView backView;
    private String title;
    private XListView listView;
    private MistakeDetilsAdapter wrongAllListAdapter;
    private RequestWrongDetailsTask mRequestWrongAllQuestionTask;
    public static SubjectExercisesItemBean subjectExercisesItemBeanIntent;
    private List<PaperTestEntity> dataList = new ArrayList<PaperTestEntity>();
    private ArrayList<ExercisesDataEntity> exercisesList = new ArrayList<ExercisesDataEntity>();
    private String stageId;
    private String subjectId;
    private ArrayList<Integer> qids;
    private ArrayList<Integer> qids_catch = new ArrayList<>();
    private int mMistakeCount;
    private SubjectExercisesItemBean mSubjectExercisesItemBean;
    private TextView wrongNumView;
    public static final int WRONG_DETAIL = 0x09;

    public static void launch(Activity activity, String title, String subjectId, ArrayList<Integer> qids) {
        Intent intent = new Intent(activity, MistakeDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("subjectId", subjectId);
        intent.putIntegerArrayListExtra("qids", qids);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectExercisesItemBeanIntent = new SubjectExercisesItemBean();
        subjectExercisesItemBeanIntent.setIsWrongSet(true);
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        title = getIntent().getStringExtra("title");
        subjectId = getIntent().getStringExtra("subjectId");
        qids = getIntent().getIntegerArrayListExtra("qids");
        qids_catch.clear();
        qids_catch.addAll(qids);
        mMistakeCount=qids.size();
        rootView = PublicLoadUtils.createPage(this, R.layout.activity_mistake_all_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                pageIndex = 1;
                requestMistakeAllList(true, true, false);
            }
        });
        setContentView(rootView);
        findView();
        requestMistakeAllList(true, true, false);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        subjectExercisesItemBeanIntent = null;
    }

    private void findView() {
        View top_space_view = findViewById(R.id.top_space_views);
        top_space_view.setVisibility(View.VISIBLE);
        backView = (TextView) findViewById(R.id.pub_top_left);
        TextView titleView = (TextView) findViewById(R.id.pub_top_mid);
        titleView.setText(title);

        wrongNumView = (TextView) findViewById(R.id.answer_exam_wrong_num_text);
//        mMistakeCount = new Integer(wrongNum);
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, 0 + ""));

        LinearLayout llBelowTitle = (LinearLayout) findViewById(R.id.ll_below_title);
        llBelowTitle.setVisibility(View.GONE);

        listView = (XListView) findViewById(R.id.mistack_all_list);
        wrongAllListAdapter = new MistakeDetilsAdapter(this);
        listView.setAdapter(wrongAllListAdapter);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (wrongAllListAdapter != null) {
                    List<PaperTestEntity> list = wrongAllListAdapter.getList();
                    if (subjectExercisesItemBeanIntent.getData() != null && subjectExercisesItemBeanIntent.getData().size() > 0) {
                        subjectExercisesItemBeanIntent.getData().get(0).setName("");
                        List<PaperTestEntity> lists = new ArrayList<PaperTestEntity>();
                        lists.addAll(list);
                        subjectExercisesItemBeanIntent.getData().get(0).setPaperTest(lists);
                    }
                    if (list != null && list.size() > 0 && position > 0 && position - 1 < list.size()) {
                        WrongAnswerViewActivity.launch(MistakeDetailsActivity.this,qids,qids_catch, subjectId, pageIndex, list.get(position - 1).getQuestions().getChildPageIndex(), WRONG_DETAIL, String.valueOf(mMistakeCount), position - 1);
                    } else {
                        WrongAnswerViewActivity.launch(MistakeDetailsActivity.this,qids,qids_catch, subjectId, pageIndex, 0, WRONG_DETAIL, String.valueOf(mMistakeCount), position);
                    }
                }
            }
        });

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTaskAndFinish();
            }
        });

        RelativeLayout linear_number = (RelativeLayout) findViewById(R.id.linear_number);
        linear_number.setVisibility(View.GONE);
    }

    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener() {

        @Override
        public void onRefresh(XListView view) {
            if (NetWorkTypeUtils.isNetAvailable()) {
                qids_catch.clear();
                qids_catch.addAll(qids);
                pageIndex = 1;
                requestMistakeAllList(true, false, false);
            } else {
                listView.stopRefresh();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }

        @Override
        public void onLoadMore(XListView view) {
            if (NetWorkTypeUtils.isNetAvailable()) {
                if (wrongAllListAdapter != null) {
                    List<PaperTestEntity> list = wrongAllListAdapter.getList();
                    int count = list.size();
                    pageIndex = count / 10;
                }
                requestMistakeAllList(false, false, true);
            } else {
                listView.stopLoadMore();
                Util.showUserToast(R.string.net_null, -1, -1);
            }
        }
    };

    private void cancelTaskAndFinish() {
        cancelTask();
//        forResult();
        MistakeDetailsActivity.this.finish();
    }

    private void cancelTask() {
        if (mRequestWrongAllQuestionTask != null) {
            mRequestWrongAllQuestionTask.cancel();
            mRequestWrongAllQuestionTask = null;
        }
    }

    private void requestMistakeAllList(final boolean isRefresh, final boolean showLoading, final boolean isLoaderMore) {
        if (showLoading) {
            rootView.loading(true);
        }
        int page = pageIndex;
        if (isLoaderMore) {
            page += 1;
        }
        if (mRequestWrongAllQuestionTask != null) {
            mRequestWrongAllQuestionTask.cancel();
            mRequestWrongAllQuestionTask = null;
        }
        ArrayList<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (qids_catch.size() > 0) {
                datas.add(qids_catch.get(0));
                qids_catch.remove(0);
            }
        }
        mRequestWrongAllQuestionTask = new RequestWrongDetailsTask(this, subjectId, datas, page, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                listView.stopRefresh();
                listView.stopLoadMore();

                mSubjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if (mSubjectExercisesItemBean.getData() != null && mSubjectExercisesItemBean.getPage() != null) {
                    wrongNumView.setVisibility(View.VISIBLE);
                    wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mSubjectExercisesItemBean.getPage().getTotalCou() + ""));
//                    mMistakeCount = mSubjectExercisesItemBean.getPage().getTotalCou();

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
                        } else if (isLoaderMore) {
                            List<PaperTestEntity> datas = new ArrayList<PaperTestEntity>();
                            datas.addAll(data);
                            dataList.addAll(datas);
                        }
                        try {
                            List<PaperTestEntity> dataLists = new ArrayList<PaperTestEntity>();
                            dataLists.addAll(dataList);
                            subjectExercisesItemBeanIntent.getData().get(0).setPaperTest(dataLists);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        YanxiuPageInfoBean pageBean = mSubjectExercisesItemBean.getPage();
                        if (qids_catch.size() > 0) {
                            listView.setPullLoadEnable(true);
                        } else {
                            listView.setPullLoadEnable(false);
                        }
                        updateUI();
                    } else {
                        if (isRefresh) {
//                                dataList.clear();
//                                exercisesList.clear();
//                                updateUI();
                            MistakeDetailsActivity.this.finish();
                        }
//                        rootView.dataNull(getResources().getString(R.string.no_group_hw_list_tip));
//                    noCommentView.setVisibility(View.VISIBLE;);
                    }
                } else {
                    if (isRefresh) {
//                            dataList.clear();
//                            exercisesList.clear();
//                            updateUI();
                        MistakeDetailsActivity.this.finish();
                    }
                }

            }

            @Override
            public void dataError(int type, String msg) {
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
    }

    private void updateUI() {
        rootView.finish();
        listView.setScrollable(true);
        listView.setPullRefreshEnable(true);
        if (wrongAllListAdapter != null) {
            wrongAllListAdapter.setList(dataList);
        }
    }

    public void onEventMainThread(WrongAnswerViewActivity.WrongAnswerDeleteBean bean) {
        try {
            int position = getIndexFromList(wrongAllListAdapter.getList(), bean.id);
            if (position != -1) {
                wrongAllListAdapter.getList().remove(position);
                wrongAllListAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIndexFromList(List<PaperTestEntity> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            PaperTestEntity entity = list.get(i);
            if (entity.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public void onEventMainThread(MistakeDetilsAdapter.RefreshMisDetilsBean event) {
        mMistakeCount = mMistakeCount - 1;
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mMistakeCount + ""));
        pageIndex = 1;
        int position = event.position;
        qids.remove(position);
        qids_catch.clear();
        qids_catch.addAll(qids);
        requestMistakeAllList(true, false, false);
//        requestMistakeNumber();
        setRefresh();
    }

    public void onEventMainThread(MistakeDetilsAdapter.NoRefreshMisDetilsBean event) {
        mMistakeCount = mMistakeCount - 1;
        wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, mMistakeCount + ""));
        int position = event.position;
        qids.remove(position);
//        pageIndex = 1;
//        requestMistakeAllList(true, false, false);
//        requestMistakeNumber();
        setRefresh();
    }

    private void setRefresh() {
        MistakeRefreshAllBean bean = new MistakeRefreshAllBean();
        EventBus.getDefault().post(bean);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            wrongNumView.setText(getResources().getString(R.string.mistake_all_num_text, data.getIntExtra("wrongNum", 0) + ""));
            mMistakeCount = data.getIntExtra("wrongNum", 0);
            if (mMistakeCount == 0) {
                this.finish();
            }
        } else {
            this.finish();
        }
    }
}
