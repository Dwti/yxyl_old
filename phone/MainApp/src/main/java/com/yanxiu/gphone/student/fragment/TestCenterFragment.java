package com.yanxiu.gphone.student.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.activity.SubjectSectionActivity;
import com.yanxiu.gphone.student.adapter.SubjectSectionAdapter;
import com.yanxiu.gphone.student.bean.ChapterListDataEntity;
import com.yanxiu.gphone.student.bean.ChapterListEntity;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.inter.OnChildTreeViewClickListener;
import com.yanxiu.gphone.student.requestTask.RequestKnpTask;

/**
 * Created by Administrator on 2015/10/30.
 */
public class TestCenterFragment extends AbsChapterFragment {

    public void requestData() {
//        rootView.loading(true);
        ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).loading();
        cancelChapterListTask();
//        requestChapterListTask = new RequestExamInfoTask(this.getActivity(), subjectId, new AsyncCallBack() {
        requestChapterListTask = new RequestKnpTask(this.getActivity(), stageId, subjectId, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).finishRootView();
                ChapterListEntity entity = (ChapterListEntity) result;
                if (entity != null && entity.getData() != null && entity.getData().size() > 0) {
                    ((SubjectSectionAdapter)mAdaper).setList(entity);
                    mExpandableListView.setAdapter(mAdaper);
                } else {
                    ((SubjectSectionAdapter)mAdaper).setList(new ChapterListEntity());
                    mExpandableListView.setAdapter(mAdaper);
                    if (TextUtils.isEmpty(entity.getStatus().getDesc())) {
                        ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).dataNull();
                    } else {
                        ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).dataNull(entity.getStatus().getDesc());
                    }
                }
            }

            @Override
            public void dataError(int type, String msg) {
                ((SubjectSectionAdapter)mAdaper).setList(new ChapterListEntity());
                mExpandableListView.setAdapter(mAdaper);
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).netError();
                } else {
                    if (TextUtils.isEmpty(msg)) {
                        ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).dataNull();
                    } else {
                        ((SubjectSectionActivity)TestCenterFragment.this.getActivity()).dataNull(msg);
                    }
                }
            }
        });
        requestChapterListTask.start();
    }




    @Override
    protected void initView() {
        super.initView();
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ChapterListDataEntity entity = ((SubjectSectionAdapter)mAdaper).getGroup(groupPosition);
                if(entity != null && entity.getChildren() != null && entity.getChildren().isEmpty()){
                    sectionId = "0";
                    sectionName = "";
                    cellid = "0";
                    cellName = "";
                    chapterId = entity.getId();
                    chapterName = entity.getName();
                    requestSubjectKnpExercises();
                }
                return true;   //默认为false，设为true时，点击事件不会展开Group
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mAdaper = new SubjectSectionAdapter(this.getActivity());
        ((SubjectSectionAdapter)mAdaper).setIsTestCenter(true);
        ((SubjectSectionAdapter)mAdaper).setmExpandableListView(mExpandableListView);
        ((SubjectSectionAdapter)mAdaper).setOnChildTreeViewClickListener(new OnChildTreeViewClickListener() {
            @Override
            public void onChildClickPosition(int parentPosition, int groupPosition, int childPosition) {
                LogInfo.log("geny", "parentPosition---" + parentPosition);
                LogInfo.log("geny", "groupPosition---" + groupPosition);
                LogInfo.log("geny", "childPosition---" + childPosition);
                ChapterListDataEntity.ChildrenEntity childntity = ((SubjectSectionAdapter) mAdaper).getChild(parentPosition, groupPosition).get(0);
                if(childntity != null && childntity.getChildren() != null && childntity.getChildren().isEmpty()){
                    ChapterListDataEntity groupEntity = ((SubjectSectionAdapter)mAdaper).getGroup(parentPosition);
                    cellid = "0";
                    cellName = "";
                    sectionId = childntity.getId();
                    sectionName = childntity.getName();
                    chapterId = groupEntity.getId();
                    chapterName = groupEntity.getName();
                    requestSubjectKnpExercises();
                }
            }

            @Override
            public void onGradesonClickPosition(int parentPosition, int groupPosition, int childPosition) {
                ChapterListDataEntity.ChildrenEntity childntity = ((SubjectSectionAdapter) mAdaper).getChild(parentPosition, groupPosition).get(0);
                ChapterListDataEntity groupEntity = ((SubjectSectionAdapter)mAdaper).getGroup(parentPosition);
                ChapterListDataEntity.GrandsonEntity grandsonEntity = ((SubjectSectionAdapter) mAdaper).getChild(parentPosition, groupPosition).get(0).getChildren().get(childPosition);
                cellid = grandsonEntity.getId();
                cellName = grandsonEntity.getName();
                sectionId = childntity.getId();
                sectionName = childntity.getName();
                chapterId = groupEntity.getId();
                chapterName = groupEntity.getName();
                requestSubjectKnpExercises();
            }
        });

    }


}
