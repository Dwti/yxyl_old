package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.WrongAnswerViewActivity;
import com.yanxiu.gphone.hd.student.adapter.FavouriteAndMistakeChapterAdapter;
import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.bean.ExercisesDataEntity;
import com.yanxiu.gphone.hd.student.bean.PublicErrorQuestionCollectionBean;
import com.yanxiu.gphone.hd.student.bean.PublicSubjectBaseBean;
import com.yanxiu.gphone.hd.student.bean.SectionMistakeBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.inter.OnChildTreeViewClickListener;
import com.yanxiu.gphone.hd.student.requestTask.RequestMistakeSectionTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestWrongQuestionTask;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/30.
 */
public class MistakeChapterFragment extends AbsChapterFragment {
    private String wrongTotalNum = "0";
    private RequestMistakeSectionTask mRequestMistakeSectionTask;
    private RequestWrongQuestionTask mRequestWrongQuestionTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout flRootViewBg = (FrameLayout) rootView.findViewById(R.id.fl_root_view);
        flRootViewBg.setBackgroundResource(R.drawable.small_blue_bg);
        return rootView;
    }

    private void cancelFavouriteEditionTask() {
        if (mRequestMistakeSectionTask != null) {
            mRequestMistakeSectionTask.cancel();
        }
        mRequestMistakeSectionTask = null;
    }

    public void setFilterRefresh(PublicSubjectBaseBean mPublicSubjectBaseBean) {
        if (mPublicSubjectBaseBean != null) {
            this.mPublicSubjectBaseBean = mPublicSubjectBaseBean;
            stageId = LoginModel.getUserinfoEntity().getStageid();
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
            requestData();
        }
    }

    public void requestData() {
        cancelFavouriteEditionTask();
        rootView.loading(true);
        if (mAdaper != null) {
            ((FavouriteAndMistakeChapterAdapter) mAdaper).setList(null);
        }
        if (NetWorkTypeUtils.isNetAvailable()) {
            new YanxiuSimpleAsyncTask<SectionMistakeBean>(getActivity()) {
                @Override
                public SectionMistakeBean doInBackground() {
                    SectionMistakeBean mBean = null;
                    try {
                        mBean = new SectionMistakeBean();
                        mBean.setData(PublicErrorQuestionCollectionBean.findDataListToChapterEntity(stageId + "", subjectId, editionId, volume,
                                isChapterSection));
                    } catch (Exception e) {
                        LogInfo.log("haitian", "getMessage=" + e.getMessage());
                    }
                    return mBean;
                }

                @Override
                public void onPostExecute(SectionMistakeBean result) {
                    rootView.finish();
                    LogInfo.log("haitian", "onPostExecute");
                    if (result != null && result.getData() != null) {
                        rootView.finish();
                        updateView(result);
                    } else {
                        if (NetWorkTypeUtils.isNetAvailable()) {
                            if (isChapterSection == 1) {
                                rootView.dataNull(isChapterSection, R.drawable.public_no_qus_bg);
                            } else {
                                if (hasChapterFavQus) {
                                    rootView.dataNull(true);
                                } else {
                                    rootView.dataNull(isChapterSection, R.drawable.public_no_qus_bg);
                                }
                            }
                        }
                    }
                }
            }.start();
        } else {
            mRequestMistakeSectionTask = new RequestMistakeSectionTask(getActivity(), stageId + "", subjectId, editionId, volume, isChapterSection,
                    mAsyncCallBack);
            mRequestMistakeSectionTask.start();
        }
    }

    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            rootView.finish();
            SectionMistakeBean mMistakeEditionBean = (SectionMistakeBean) result;
            if (mMistakeEditionBean.getData() == null || mMistakeEditionBean.getData().size() <= 0) {
                if (isChapterSection == 1) {
                    rootView.dataNull(isChapterSection, R.drawable.public_no_qus_bg);
                } else {
                    if (hasChapterFavQus) {
                        if (TextUtils.isEmpty(mMistakeEditionBean.getStatus().getDesc())) {
                            rootView.dataNull(true);
                        } else {
                            rootView.dataNull(mMistakeEditionBean.getStatus().getDesc());
                        }
                    } else {
                        rootView.dataNull(isChapterSection, R.drawable.public_no_qus_bg);
                    }
                }
            } else {
                updateView(mMistakeEditionBean);
            }
        }

        @Override
        public void dataError(int type, String msg) {
            rootView.finish();
            if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                rootView.netError(true);
            } else {
                if (TextUtils.isEmpty(msg)) {
                    rootView.dataNull(true);
                } else {
                    rootView.dataNull(msg);
                }
            }
        }
    };

    private void updateView(SectionMistakeBean mSectionMistakeBean) {
        if (mAdaper == null) {
            initAdapter();
        }
        ((FavouriteAndMistakeChapterAdapter) mAdaper).setList(mSectionMistakeBean.getData());
    }
    private FavouriteAndMistakeChapterAdapter.OnRowClickListener mOnClickListener = new FavouriteAndMistakeChapterAdapter.OnRowClickListener() {
        @Override
        public void onClick (int groupPosition) {
            if (CommonCoreUtil.checkClickEvent()) {
                DataTeacherEntity entity = (DataTeacherEntity) mAdaper.getGroup(groupPosition);
                if (entity != null && entity.getData() != null) {
                    String wrongNum = entity.getData().getWrongNum();
//                        if (isChapterSection == 1 && entity.getChildren() != null && entity
//                                .getChildren().size() > 0) {//考点，有第三级列表，则此二级列表不可点击
//                            return true;
//                        }
                    if (wrongNum != null && !"0".equals(wrongNum)) {
                        sectionId = "0";
                        sectionName = "";
                        chapterId = entity.getId();
                        chapterName = entity.getName();
                        wrongTotalNum = wrongNum;
                        cellid = "0";
                        requestMistakeQuestion(subjectId, editionId, volume, chapterId, sectionId, cellid, isChapterSection, 1);
                    } else {
                        wrongTotalNum = "0";
                    }
                } else {
                    wrongTotalNum = "0";
                }
            }
        }
    };
    @Override
    protected void initView() {
        super.initView();
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick (ExpandableListView parent, View v, int groupPosition, long id) {
                mOnClickListener.onClick(groupPosition);
                return true;   //默认为false，设为true时，点击事件不会展开Group
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initAdapter();
    }

    private void initAdapter() {
        mAdaper = new FavouriteAndMistakeChapterAdapter(getActivity(),
                FavouriteAndMistakeChapterAdapter.CONSTANT_MISTAKECHAPTERADAPTER);
        ((FavouriteAndMistakeChapterAdapter) mAdaper).setmOnClickListener(mOnClickListener);
        ((FavouriteAndMistakeChapterAdapter) mAdaper).setIsTestCenter(isChapterSection == 1);
        ((FavouriteAndMistakeChapterAdapter) mAdaper).setmExpandableListView(mExpandableListView);
        ((FavouriteAndMistakeChapterAdapter) mAdaper).setOnChildTreeViewClickListener(new OnChildTreeViewClickListener() {
            @Override
            public void onChildClickPosition(int parentPosition, int groupPosition, int childPosition) {
                    ArrayList<DataTeacherEntity> childList = ((FavouriteAndMistakeChapterAdapter) mAdaper).getChild(parentPosition, groupPosition);
                    DataTeacherEntity mChildData = null;
                    if (childList != null) {
                        mChildData = childList.get(0);
                    }
                    DataTeacherEntity groupEntity = (DataTeacherEntity) mAdaper.getGroup(parentPosition);
                    if (mChildData != null) {
//                        if(isChapterSection == 1 && mChildData.getChildren() != null && mChildData
//                                .getChildren().size() > 0) {//考点，有第三级列表，则此二级列表不可点击
//                            return;
//                        }
                        sectionId = mChildData.getId();
                        sectionName = mChildData.getName();
                        if (mChildData.getData() != null) {
                            wrongTotalNum = mChildData.getData().getWrongNum();
                        } else {
                            wrongTotalNum = "0";
                        }
                    } else {
                        wrongTotalNum = "0";
                        sectionId = "0";
                        sectionName = "";
                    }
                    chapterId = groupEntity.getId();
                    chapterName = groupEntity.getName();
                    cellid = "0";
                    if (!"0".equals(wrongTotalNum)) {
                        requestMistakeQuestion(subjectId, editionId, volume, chapterId, sectionId, cellid, isChapterSection, 1);
                    }
            }

            @Override
            public void onGradesonClickPosition(int parentPosition, int groupPosition, int childPosition) {
                ArrayList<DataTeacherEntity> childList = ((FavouriteAndMistakeChapterAdapter) mAdaper).getChild(parentPosition, groupPosition);
                DataTeacherEntity mChildData = null;
                if (childList != null) {
                    mChildData = childList.get(0);
                }
                DataTeacherEntity mGradesonData = null;
                if (mChildData != null && mChildData.getChildren() != null) {
                    mGradesonData = mChildData.getChildren().get(childPosition);
                }
                DataTeacherEntity groupEntity = (DataTeacherEntity) mAdaper.getGroup(parentPosition);

                if (mGradesonData.getData() != null) {
                    wrongTotalNum = mGradesonData.getData().getWrongNum();
                    cellid = mGradesonData.getId();
                } else {
                    wrongTotalNum = "0";
                    cellid = "0";
                }
                chapterId = groupEntity.getId();
                chapterName = groupEntity.getName();
                if (mChildData != null) {
                    sectionId = mChildData.getId();
                    sectionName = mChildData.getName();
                } else {
                    sectionId = "0";
                    sectionName = "";
                }
                if (!"0".equals(wrongTotalNum)) {
                    requestMistakeQuestion(subjectId, editionId, volume, chapterId, sectionId, cellid, isChapterSection, 1);
                }
            }
        });
        mExpandableListView.setAdapter(mAdaper);
    }

    private void cancelWrongQuestionTask() {
        if (mRequestWrongQuestionTask != null) {
            mRequestWrongQuestionTask.cancel();
        }
        mRequestWrongQuestionTask = null;
    }

    private void requestMistakeQuestion(final String subjectId, final String editionId, final String volumeId, final String chapterId, final String
            sectionId, final String uniteId, final int isChapterSection, final int currentPage) {
        LogInfo.log("geny", "requestMistakeQuestion");
        rootView.loading(true);
        cancelWrongQuestionTask();
        if (NetWorkTypeUtils.isNetAvailable()) {
            new YanxiuSimpleAsyncTask<SubjectExercisesItemBean>(getActivity()) {
                @Override
                public SubjectExercisesItemBean doInBackground() {
                    SubjectExercisesItemBean mBean = null;
                    try {
                        ArrayList<ExercisesDataEntity> data = null;
                        ExercisesDataEntity mExercisesDataEntity = PublicErrorQuestionCollectionBean.findExercisesDataEntityWithChapter(stageId + "",
                                subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection, (currentPage - 1) * YanXiuConstant
                                        .YX_PAGESIZE_CONSTANT);
                        if (mExercisesDataEntity != null) {
                            mBean = new SubjectExercisesItemBean();
                            data = new ArrayList<>();
                            data.add(mExercisesDataEntity);
                            try {
                                mBean.setTotalNum(Integer.parseInt(wrongTotalNum));
                            } catch (Exception e) {
                                mBean.setTotalNum(0);
                            }
                            mBean.setData(data);
                        }
                    } catch (Exception e) {

                    }
                    return mBean;
                }

                @Override
                public void onPostExecute(SubjectExercisesItemBean result) {
                    rootView.finish();
                    if (result != null && result.getData() != null) {
                        result.setIsWrongSet(true);
                        result.setIsResolution(false);
                        WrongAnswerViewActivity.launch(getActivity(), result, subjectId, editionId, volumeId, chapterId, sectionId, uniteId,
                                isChapterSection, false);
                    } else {
                        Util.showToast(R.string.server_connection_erro);
                    }
                }
            }.start();
        } else {
            mRequestWrongQuestionTask = new RequestWrongQuestionTask(getActivity(), stageId + "",
                    subjectId, editionId, chapterId, sectionId, volumeId, currentPage, null, uniteId, isChapterSection, mWrongQuesAsyncCallBack);
            mRequestWrongQuestionTask.start();
        }
    }

    private AsyncCallBack mWrongQuesAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            rootView.finish();
            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
            if (subjectExercisesItemBean.getData() != null && subjectExercisesItemBean.getData().size() >= 1) {
                subjectExercisesItemBean.setIsWrongSet(true);
                subjectExercisesItemBean.setIsResolution(false);
                subjectExercisesItemBean.getData().get(0).setStageid(stageId + "");
                subjectExercisesItemBean.getData().get(0).setSubjectid(subjectId);
                subjectExercisesItemBean.getData().get(0).setSubjectName(subjectName);

                subjectExercisesItemBean.getData().get(0).setBedition(editionId);
                if (!TextUtils.isEmpty(editionName)) {
                    subjectExercisesItemBean.getData().get(0).setEditionName(editionName);
                }

                subjectExercisesItemBean.getData().get(0).setVolume(volume);
                if (!TextUtils.isEmpty(volumeName)) {
                    subjectExercisesItemBean.getData().get(0).setVolumeName(volumeName);
                }

                subjectExercisesItemBean.getData().get(0).setChapterid(chapterId);
                if (!TextUtils.isEmpty(chapterName)) {
                    subjectExercisesItemBean.getData().get(0).setChapterName(chapterName);
                }

                subjectExercisesItemBean.getData().get(0).setSectionid(sectionId);
                if (!TextUtils.isEmpty(sectionName)) {
                    subjectExercisesItemBean.getData().get(0).setSectionName(sectionName);
                }
                try {
                    subjectExercisesItemBean.setTotalNum(Integer.parseInt(wrongTotalNum));
                } catch (Exception e) {
                    subjectExercisesItemBean.setTotalNum(0);
                }
                WrongAnswerViewActivity.launch(getActivity(), subjectExercisesItemBean, subjectId, editionId, volume, chapterId, sectionId,
                        cellid, isChapterSection, true);
            } else {
                Util.showToast(R.string.server_connection_erro);
            }
        }

        @Override
        public void dataError(int type, String msg) {
            rootView.finish();
            if (TextUtils.isEmpty(msg)) {
                Util.showToast(R.string.server_connection_erro);
            } else {
                Util.showToast(msg);
            }
        }
    };
    @Override
    public void onReset() {
        super.onDestroy();
    }


}
