package com.yanxiu.gphone.studentold.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.AnswerViewActivity;
import com.yanxiu.gphone.studentold.bean.ExercisesDataEntity;
import com.yanxiu.gphone.studentold.bean.PublicSubjectBaseBean;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;
import com.yanxiu.gphone.studentold.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.AbstractAsyncTask;
import com.yanxiu.gphone.studentold.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.studentold.requestTask.RequestIntelliExeTask;
import com.yanxiu.gphone.studentold.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.utils.statistics.DataStatisticsUploadManager;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;
import com.yanxiu.gphone.studentold.view.StudentLoadingLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/7.
 */
public abstract class AbsChapterFragment extends Fragment implements View.OnClickListener {
    public static final int QUESTON_COUNT = 10;
    public static final int CHAPTER = 0;
    public static final int TEST_CENTER = 1;
    protected PublicLoadLayout rootView;
    protected ExpandableListView mExpandableListView;
    protected StudentLoadingLayout loadingLayout;
    protected BaseExpandableListAdapter mAdaper;
    /////////////////////////////////
    protected SubjectVersionBean.DataEntity entity;
    protected int stageId;
    protected String subjectId;
    protected String subjectName;
    protected String editionId;
    protected String editionName;
    protected String volume;
    protected String volumeName;
    protected String chapterId;
    protected String chapterName;
    protected String sectionId;
    protected String sectionName;
//    protected String cellId;

    protected int isChapterSection = 0;
    protected boolean hasChapterFavQus = true;
    protected String cellid = "0";
    protected String cellName = "";

    protected SubjectExercisesItemBean subjectExercisesItemBean;

    protected RequestEditionInfoTask requestEditionInfoTask;
    protected AbstractAsyncTask requestChapterListTask;
    protected PublicSubjectBaseBean mPublicSubjectBaseBean;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPublicSubjectBaseBean = (getArguments() != null) ? (PublicSubjectBaseBean) getArguments().getSerializable("publicSubjectBaseBean") :
                null;
        this.entity = (getArguments() != null) ? (SubjectVersionBean.DataEntity) getArguments().getSerializable("dataEntity") : null;
        this.volume = (getArguments() != null) ? getArguments().getString("volume") : "";
        this.volumeName = (getArguments() != null) ? getArguments().getString("volumeName") : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = PublicLoadUtils.createPage(this.getActivity(), R.layout.fragment_chapter);
        initView();
        initData();
        requestData();
        return rootView;
    }
    protected void initView() {
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestData();
            }
        });
        loadingLayout = (StudentLoadingLayout) rootView.findViewById(R.id.loading_layout);
        mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.elv_subject_section);
        mExpandableListView.setGroupIndicator(null);
    }
    protected void initData() {
        if (entity != null && entity.getData() != null) {
            stageId = LoginModel.getUserinfoEntity().getStageid();
            subjectId = entity.getId();
            subjectName = entity.getName();
            editionId = entity.getData().getEditionId();
            editionName = entity.getData().getEditionName();
        }
        if(mPublicSubjectBaseBean != null){
            stageId  = LoginModel.getUserinfoEntity().getStageid();
            subjectId = mPublicSubjectBaseBean.getSubjectId();
            subjectName = mPublicSubjectBaseBean.getSubjectName();
            editionId = mPublicSubjectBaseBean.getEditionId();
            editionName = mPublicSubjectBaseBean.getEditionName();
            isChapterSection = mPublicSubjectBaseBean.getIsChapterSection();//0: 代表章节收藏; 1:代表考点收藏
            volume = mPublicSubjectBaseBean.getVolumeId();
            volumeName = mPublicSubjectBaseBean.getVolumeName();
            chapterId = mPublicSubjectBaseBean.getChapterId();
            chapterName = mPublicSubjectBaseBean.getChapterName();
            sectionId = mPublicSubjectBaseBean.getSectionId();
            sectionName = mPublicSubjectBaseBean.getSectionName();
            cellid = mPublicSubjectBaseBean.getUniteId();
            cellName = mPublicSubjectBaseBean.getUniteName();
            hasChapterFavQus = mPublicSubjectBaseBean.isHasChapterFavQus();
        }
    }

    protected void requestSubjectKnpExercises() {
        LogInfo.log("geny", "requestSubjectExercises");
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
        new RequestKnpointQBlockTask(this.getActivity(), stageId, subjectId, chapterId, sectionId, cellid, RequestKnpointQBlockTask.EXAM_QUSETION, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()) {
                    if (subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()) {

                        setDataSource();
                        subjectExercisesItemBean.getData().get(0).setIsChapterSection(ExercisesDataEntity.TEST_CENTER);

                        AnswerViewActivity.launch(AbsChapterFragment.this.getActivity(), subjectExercisesItemBean, YanXiuConstant.KPN_REPORT);
                    }
                } else {
                    if (subjectExercisesItemBean != null && subjectExercisesItemBean.getStatus() != null && subjectExercisesItemBean.getStatus().getDesc() != null) {
                        Util.showToast(subjectExercisesItemBean.getStatus().getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
                loadingLayout.setViewGone();
            }

            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", "dataError type =====" + type);
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                loadingLayout.setViewGone();
            }
        }).start();

    }



    protected void requestSubjectExercises() {
        LogInfo.log("geny", "requestSubjectExercises");
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_INTELLI_EXE);
        new RequestIntelliExeTask(this.getActivity(), stageId, subjectId, editionId, chapterId, sectionId, QUESTON_COUNT, volume,cellid, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                subjectExercisesItemBean = (SubjectExercisesItemBean) result;
                if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().get(0).getPaperTest() != null && !subjectExercisesItemBean.getData().get(0).getPaperTest().isEmpty()) {
                    if (subjectExercisesItemBean.getData() != null && !subjectExercisesItemBean.getData().isEmpty()) {
                        setDataSource();

                        subjectExercisesItemBean.getData().get(0).setIsChapterSection(ExercisesDataEntity.CHAPTER);

                        AnswerViewActivity.launch(AbsChapterFragment.this.getActivity(), subjectExercisesItemBean, YanXiuConstant.INTELLI_REPORT);
                        intoExerciseStatistic();
//                        AnswerViewActivity.launch(AbsChapterFragment.this.getActivity(), subjectExercisesItemBean);
                    }
                } else {
                    if (subjectExercisesItemBean != null && subjectExercisesItemBean.getStatus() != null && subjectExercisesItemBean.getStatus().getDesc() != null) {
                        Util.showToast(subjectExercisesItemBean.getStatus().getDesc());
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
                loadingLayout.setViewGone();
            }

            //进入练习题
            private void intoExerciseStatistic() {
                StatisticHashMap statisticHashMap = new StatisticHashMap();
                statisticHashMap.put(YanXiuConstant.eventID, "20:event_5");//5:进入练习
                ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
                arrayList.add(statisticHashMap);
                HashMap<String, String> intoExerciseHashMap = new HashMap<>();
                intoExerciseHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
                DataStatisticsUploadManager.getInstance().NormalUpLoadData(getActivity(), intoExerciseHashMap);
            }


            @Override
            public void dataError(int type, String msg) {
                LogInfo.log("geny", "dataError type =====" + type);
                if (TextUtils.isEmpty(msg)) {
                    Util.showToast(R.string.server_connection_erro);
                } else {
                    Util.showToast(msg);
                }
                loadingLayout.setViewGone();
            }
        }).start();

    }


    private void setDataSource(){
        subjectExercisesItemBean.getData().get(0).setStageid(stageId + "");
        subjectExercisesItemBean.getData().get(0).setSubjectid(subjectId);
        subjectExercisesItemBean.getData().get(0).setSubjectName(subjectName);

        subjectExercisesItemBean.getData().get(0).setBedition(editionId);
        subjectExercisesItemBean.getData().get(0).setEditionName(editionName);

        subjectExercisesItemBean.getData().get(0).setVolume(volume);
        subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);

        subjectExercisesItemBean.getData().get(0).setChapterid(chapterId);
        subjectExercisesItemBean.getData().get(0).setChapterName(chapterName);

        subjectExercisesItemBean.getData().get(0).setSectionid(sectionId);
        subjectExercisesItemBean.getData().get(0).setSectionName(sectionName);

        subjectExercisesItemBean.getData().get(0).setCellid(cellid);
        subjectExercisesItemBean.getData().get(0).setCellName(cellName);
    }

    protected void cancelEditionInfoTask() {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }



    protected void cancelChapterListTask() {
        if (requestChapterListTask != null) {
            requestChapterListTask.cancel();
        }
        requestChapterListTask = null;
    }

    protected abstract void requestData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelEditionInfoTask();
        cancelChapterListTask();
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
