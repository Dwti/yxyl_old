package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.PublicFavouriteQuestionBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestDelFavouriteListTask;
import com.yanxiu.gphone.student.requestTask.RequestFavouriteQuestionTask;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/28.
 */
public class FavouriteViewActivity extends BaseAnswerViewActivity {
    public final static int FAV_ANSWER_REQUESTCODE = 0x100;
    private RequestFavouriteQuestionTask mRequestFavouriteQuestionTask;
    private PaperTestEntity mPaperTestEntity;
    private int pageIndex = 0;
    private String stageId;
    private String subjectId;
    private String editionId;
    private String volumeId;
    private String chapterId;
    private String sectionId;
    private String questionId;
    private int isChapterSection = 0;
    private String uniteId = "0";
    private int currentPageIndex = 1;
    private int delQueNum = 0;
    private boolean isNetData = true;
    private boolean deleteAction = false;
//    private ArrayList<String> delQuestionTmpList = new ArrayList<String>();

    private ArrayList<String> delQuestionTmpList = new ArrayList<String>();
    private HashMap<String, String> delQuestionTmpMap = new HashMap<String, String>();

    public static void launch(Activity context, SubjectExercisesItemBean bean, String subjectId, String editionId, String volumeId, String
            chapterId, String sectionId, String uniteId, int isChapterSection, boolean isNetData) {
        Intent intent = new Intent(context, FavouriteViewActivity.class);
        QuestionUtils.initDataWithAnswer(bean);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("editionId", editionId);
        intent.putExtra("volumeId", volumeId);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("sectionId", sectionId);
        intent.putExtra("uniteId", uniteId);
        intent.putExtra("isChapterSection", isChapterSection);
        intent.putExtra("isNetData", isNetData);
        context.startActivityForResult(intent, FAV_ANSWER_REQUESTCODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        subjectId = getIntent().getStringExtra("subjectId");
        editionId = getIntent().getStringExtra("editionId");
        volumeId = getIntent().getStringExtra("volumeId");
        chapterId = getIntent().getStringExtra("chapterId");
        sectionId = getIntent().getStringExtra("sectionId");
        uniteId = getIntent().getStringExtra("uniteId");
        isChapterSection = getIntent().getIntExtra("isChapterSection", 0);
        isNetData = getIntent().getBooleanExtra("isNetData", true);
        initView();
        initData();
    }

    @Override
    public void initData() {
        super.initData();
        if (dataSources != null && dataSources.getData() != null) {
            pageCount = dataSources.getTotalNum();
            tvPagerIndex.setText("1");
            tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(pageCount)));
            tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
            tvToptext.setCompoundDrawables(null, null, null, null);
//            tvAnswerCard.setVisibility(View.GONE);
            ivAnswerCard.setVisibility(View.GONE);
            ivFavCard.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogInfo.log("geny", "position" + position + "----positionOffset" + positionOffset + "----positionOffsetPixels" + positionOffsetPixels);

    }


    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);

        if(vpAnswer.getCurrentItem() == pageCount - delQueNum - 1){
            btnNextQuestion.setVisibility(View.GONE);
        }else{
            btnNextQuestion.setVisibility(View.VISIBLE);
        }

        tvPagerIndex.setText(String.valueOf(position + 1));
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf((pageCount - delQueNum))));
        pageIndex = position;
        int currentTotal = currentPageIndex * YanXiuConstant.YX_PAGESIZE_CONSTANT;
        LogInfo.log("haitian", "currentTotal =" + currentTotal + "   position=" + position + "   currentTotal - position - 1 - delQueNum="
                + (currentTotal - position - 1 - delQueNum) + " adapter.getCount() - position - 1=" + (adapter.getCount() - position - 1));
        if (pageCount > currentTotal && (adapter.getCount() - position - 1) == 3) {
            String currentId = null;
            try {
                int size = dataSources.getData().get(0).getPaperTest().size();
                PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                currentId = mPaperTestEntity.getId() + "";
            } catch (Exception e) {
            }
            LogInfo.log("haitian", "onPageSelected currentId =" + currentId);
            requestFavouriteQuestion(subjectId, editionId, volumeId, chapterId, sectionId, currentPageIndex + 1, currentId);
        }
        getDelTaskInfo(pageIndex);
        if (delQuestionTmpMap.containsKey(questionId)) {
            ivFavCard.setBackgroundResource(R.drawable.selector_answer_uncollection);
        } else {
            ivFavCard.setBackgroundResource(R.drawable.selector_answer_collection);
        }
    }

    private void getDelTaskInfo(int position) {
        if (dataSources != null && dataSources.getData() != null) {
            if (dataSources.getData().get(0) != null && dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().size() > position) {
                mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(position);
                if (mPaperTestEntity != null) {
                    questionId = mPaperTestEntity.getQid() + "";
                }
            } else {
                questionId = null;
                mPaperTestEntity = null;
            }
        } else {
            questionId = null;
            mPaperTestEntity = null;
        }
    }

    private void requestFavouriteQuestion(final String subjectId, final String editionId, final String volumeId, final String chapterId, final String
            sectionId, final int currentPage, final String currentId) {
        cancelWrongQuestionTask();
        if (!isNetData) {
            new YanxiuSimpleAsyncTask<SubjectExercisesItemBean>(this) {
                @Override
                public SubjectExercisesItemBean doInBackground() {
                    SubjectExercisesItemBean mBean = null;
                    try {
                        ArrayList<ExercisesDataEntity> data = null;
                        ExercisesDataEntity mExercisesDataEntity = PublicFavouriteQuestionBean.findExercisesDataEntityWithChapter(stageId,
                                subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection, (currentPage - 1) * YanXiuConstant
                                        .YX_PAGESIZE_CONSTANT);
                        mBean = new SubjectExercisesItemBean();
                        data = new ArrayList<ExercisesDataEntity>();
                        data.add(mExercisesDataEntity);
                        mBean.setData(data);
                    } catch (Exception e) {
                    }
                    return mBean;
                }

                @Override
                public void onPostExecute(SubjectExercisesItemBean result) {
                    if (result != null && result.getData() != null) {
                        QuestionUtils.initDataWithAnswer(result);
                        currentPageIndex++;
                        dataSources.getData().get(0).getPaperTest().addAll(result.getData().get(0).getPaperTest());
                        adapter.addDataSourcesMore(result.getData().get(0).getPaperTest());
                    } else {
                        if (NetWorkTypeUtils.isNetAvailable()) {
                            Util.showToast(R.string.server_connection_erro);
                        }
                    }
                }
            }.start();
        } else {
            mRequestFavouriteQuestionTask = new RequestFavouriteQuestionTask(FavouriteViewActivity.this, stageId,
                    subjectId, editionId, chapterId, sectionId, volumeId, currentPage, currentId, uniteId, isChapterSection, mWrongQuesAsyncCallBack);
            mRequestFavouriteQuestionTask.start();
        }
    }

    private void cancelWrongQuestionTask() {
        if (mRequestFavouriteQuestionTask != null) {
            mRequestFavouriteQuestionTask.cancel();
        }
        mRequestFavouriteQuestionTask = null;
    }

    private AsyncCallBack mWrongQuesAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
            if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().size() >= 1) {
                QuestionUtils.initDataWithAnswer(subjectExercisesItemBean);
                currentPageIndex++;
                dataSources.getData().get(0).getPaperTest().addAll(subjectExercisesItemBean.getData().get(0).getPaperTest());
                adapter.addDataSourcesMore(subjectExercisesItemBean.getData().get(0).getPaperTest());
            } else {
                Util.showToast(R.string.server_connection_erro);
            }
        }

        @Override
        public void dataError(int type, String msg) {
            if (TextUtils.isEmpty(msg)) {
                Util.showToast(R.string.server_connection_erro);
            } else {
                Util.showToast(msg);
            }
        }
    };

    private boolean isDelAll = false;
    private void finishResult() {
        if (delQuestionTmpMap != null && delQuestionTmpMap.size() > 0) {
            if (delQuestionTmpMap.size() >= pageCount) {
                isDelAll = true;
            }
            mRootView.loading(true);
            ArrayList<String> delQuestionTmpList = new ArrayList(delQuestionTmpMap.values());
            PublicFavouriteQuestionBean.updateAllDelData(delQuestionTmpList);
            PublicFavouriteQuestionBean.deleteItemList(volumeId, delQuestionTmpList);
            deleteAction = true;
            RequestDelFavouriteListTask.requestTask(this, delQuestionTmpList, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    mRootView.finish();
                    finishActivity();
                }

                @Override
                public void dataError(int type, String msg) {
                    mRootView.finish();
                    finishActivity();
                }
            });
        } else {
            deleteAction = false;
            finishActivity();
        }
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("deleteAction", deleteAction);
        intent.putExtra("isDelAll", isDelAll);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("sectionId", sectionId);
        intent.putExtra("uniteId", uniteId);
        intent.putExtra("isChapterSection", isChapterSection);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ivBack) {
            finishResult();
        } else if (v == ivFavCard) {
            getDelTaskInfo(pageIndex);
            LogInfo.log("haitian", "questionId =" + questionId);
            if (TextUtils.isEmpty(questionId)) {
                Util.showToast(R.string.select_location_data_error);
            } else {
                if (NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.public_loading_net_null_errtxt);
                    return;
                }
                if (delQuestionTmpMap.containsKey(questionId)) {
                    delQuestionTmpMap.remove(questionId);
                    ivFavCard.setBackgroundResource(R.drawable.selector_answer_collection);
//                    mRootView.loading(true);
//                    RequestSubmitFavouriteTask.requestTask(this,stageId,
//                            //TODO
//                            subjectId, editionId, volumeId, chapterId, sectionId, questionId, uniteId ,(isChapterSection == 1 ? 2 :
//                                    isChapterSection) , new
//                            AsyncCallBack() {
//                                @Override
//                                public void update(YanxiuBaseBean result) {
//                                    mRootView.finish();
//                                    delQuestionTmpMap.remove(questionId);
//                                    ivFavCard.setBackgroundResource(R.drawable.star_press);
//                                }
//                                @Override
//                                public void dataError(int type, String msg) {
//                                    mRootView.finish();
//                                    if (!TextUtils.isEmpty(msg)) {
//                                        Util.showToast(msg);
//                                    } else {
//                                    }
//                                }
//                            });
                } else {
                    delQuestionTmpMap.put(questionId, questionId);
                    ivFavCard.setBackgroundResource(R.drawable.selector_answer_uncollection);
//                    mRootView.loading(true);
//                    RequestDelFavouriteTask.requestTask(this, questionId, new AsyncCallBack() {
//                        @Override
//                        public void update(YanxiuBaseBean result) {
//                            mRootView.finish();
//                            deleteAction = true;
//                            Util.showToast(R.string.fav_question_del_done);
//                            delQuestionTmpMap.put(questionId, questionId);
//                            ivFavCard.setBackgroundResource(R.drawable.star_normal);
//                            //                        deleteProcess(pageIndex);
//                        }
//                        @Override
//                        public void dataError(int type, String msg) {
//                            mRootView.finish();
//                            if (!TextUtils.isEmpty(msg)) {
//                                Util.showToast(msg);
//                            } else {
//                                Util.showToast(R.string.fav_question_del_failed);
//                            }
//                        }
//                    });
                }
            }
        }
    }

    private void deleteProcess(int index) {
        delQueNum++;
        int currentPageCount = pageCount - delQueNum;
        if (dataSources.getData().get(0).getPaperTest() != null && dataSources.getData().get(0).getPaperTest().size() > index) {
            dataSources.getData().get(0).getPaperTest().remove(index);
        }
        delQuestionTmpMap.put(questionId, questionId);
        adapter.deleteFragment(index);
        if (currentPageCount <= 0) {
            finishResult();
        } else {
//            LogInfo.log("haitian", "adapter.getCount()%10 =" + adapter.getCount() % YanXiuConstant
//                    .YX_PAGESIZE_CONSTANT + "----currentPageCount=" + currentPageCount + "---index=" + index);
            if (adapter.getCount() % YanXiuConstant.YX_PAGESIZE_CONSTANT == 3 && currentPageCount > 3) {
                int currentTotal = (currentPageIndex + 1) * YanXiuConstant.YX_PAGESIZE_CONSTANT;
//                LogInfo.log("haitian", "currentTotal =" + (currentTotal - YanXiuConstant.YX_PAGESIZE_CONSTANT + 1) + "----pageCount=" + pageCount);
                if (pageCount >= (currentTotal - YanXiuConstant.YX_PAGESIZE_CONSTANT + 1)) {
                    String currentId = null;
                    try {
                        int size = dataSources.getData().get(0).getPaperTest().size();
                        PaperTestEntity mPaperTestEntity = dataSources.getData().get(0).getPaperTest().get(size - 1);
                        currentId = mPaperTestEntity.getId() + "";
                    } catch (Exception e) {

                    }
                    LogInfo.log("haitian", "deleteProcess currentId =" + currentId);
                    requestFavouriteQuestion(subjectId, editionId, volumeId, chapterId, sectionId, currentPageIndex + 1, currentId);
                }
            }
            int currentIndex = index;
            if (index > 0 && index == currentPageCount) {//最后一个
                currentIndex = index - 1;
            }
            vpAnswer.setCurrentItem(currentIndex);
            if (currentPageCount > 0) {
                tvPagerIndex.setText(String.valueOf(currentIndex + 1));
                tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(currentPageCount)));
            } else {
                finishResult();
            }
        }
    }
}