package com.yanxiu.gphone.studentold.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.activity.SubjectSectionActivity;
import com.yanxiu.gphone.studentold.adapter.SubjectSectionAdapter;
import com.yanxiu.gphone.studentold.bean.ChapterListDataEntity;
import com.yanxiu.gphone.studentold.bean.ChapterListEntity;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.inter.OnChildTreeViewClickListener;
import com.yanxiu.gphone.studentold.requestTask.RequestChapterListTask;

/**
 * Created by Administrator on 2015/10/30.
 */
public class ChapterFragment extends AbsChapterFragment {
    public void refreshFilterData(String volume, String volumeName){
        this.volume = volume;
        this.volumeName = volumeName;
        requestData();
    }
    public void requestData() {
//        rootView.loading(true);
        ((SubjectSectionActivity)ChapterFragment.this.getActivity()).loading();
        cancelChapterListTask();
        requestChapterListTask = new RequestChapterListTask(this.getActivity(), stageId,
                subjectId, editionId, volume, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                ((SubjectSectionActivity)ChapterFragment.this.getActivity()).finishRootView();
                ChapterListEntity entity = (ChapterListEntity) result;
                if (entity != null && entity.getData() != null && entity.getData().size() > 0) {
                    ((SubjectSectionAdapter)mAdaper).setList(entity);
                    mExpandableListView.setAdapter(mAdaper);
                } else {
                    ((SubjectSectionAdapter)mAdaper).setList(new ChapterListEntity());
                    mExpandableListView.setAdapter(mAdaper);
                    if (TextUtils.isEmpty(entity.getStatus().getDesc())) {
                        ((SubjectSectionActivity)ChapterFragment.this.getActivity()).dataNull();
                    } else {
                        ((SubjectSectionActivity)ChapterFragment.this.getActivity()).dataNull(entity.getStatus().getDesc());
                    }
                }
            }

            @Override
            public void dataError(int type, String msg) {
                ((SubjectSectionAdapter)mAdaper).setList(new ChapterListEntity());
                mExpandableListView.setAdapter(mAdaper);
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    ((SubjectSectionActivity)ChapterFragment.this.getActivity()).netError();
                } else {
                    if (TextUtils.isEmpty(msg)) {
                        ((SubjectSectionActivity)ChapterFragment.this.getActivity()).dataNull();
                    } else {
                        ((SubjectSectionActivity)ChapterFragment.this.getActivity()).dataNull(msg);
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
                sectionId = "0";
                sectionName = "";
                cellid = "0";
                cellName = "";
                chapterId = entity.getId();
                chapterName = entity.getName();
                requestSubjectExercises();
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
        ((SubjectSectionAdapter)mAdaper).setmExpandableListView(mExpandableListView);
        ((SubjectSectionAdapter)mAdaper).setOnChildTreeViewClickListener(new OnChildTreeViewClickListener() {
            @Override
            public void onChildClickPosition(int parentPosition, int groupPosition, int childPosition) {
                LogInfo.log("geny", "parentPosition---" + parentPosition);
                LogInfo.log("geny", "groupPosition---" + groupPosition);
                LogInfo.log("geny", "childPosition---" + childPosition);
                ChapterListDataEntity.ChildrenEntity Childntity = ((SubjectSectionAdapter) mAdaper).getChild(parentPosition, groupPosition).get(0);
                ChapterListDataEntity groupEntity = ((SubjectSectionAdapter)mAdaper).getGroup(parentPosition);
                cellid = "0";
                cellName = "";
                sectionId = Childntity.getId();
                sectionName = Childntity.getName();
                chapterId = groupEntity.getId();
                chapterName = groupEntity.getName();
                requestSubjectExercises();
            }

            @Override
            public void onGradesonClickPosition(int parentPosition, int groupPosition, int childPosition) {
                ChapterListDataEntity.ChildrenEntity Childntity = ((SubjectSectionAdapter) mAdaper).getChild(parentPosition, groupPosition).get(0);
                ChapterListDataEntity groupEntity = ((SubjectSectionAdapter)mAdaper).getGroup(parentPosition);
                ChapterListDataEntity.GrandsonEntity grandsonEntity = ((SubjectSectionAdapter) mAdaper).getChild(parentPosition, groupPosition).get(0).getChildren().get(childPosition);
                cellid = grandsonEntity.getId();
                cellName = grandsonEntity.getName();
                sectionId = Childntity.getId();
                sectionName = Childntity.getName();
                chapterId = groupEntity.getId();
                chapterName = groupEntity.getName();
                requestSubjectExercises();
            }
        });

    }
}
