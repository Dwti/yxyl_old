package com.yanxiu.gphone.student.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.PracticeHistoryBean;
import com.yanxiu.gphone.student.bean.PracticeHistoryChildBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.student.requestTask.RequestPracticeHistoryTask;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.SortPopUpWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ChapterHistoryFragment extends AbsHistoryFragment{

    private SortPopUpWindow popupWindow;

    private int pageIndex = 1;
    private boolean isLoadMore = false;

    @Override
    protected void initView() {
        super.initView();
//        popupWindow = new SortPopUpWindow(getActivity());
//        popupWindow.create("history", topRightView);
    }

    @Override
    protected void initData() {
        super.initData();
        requestPracticeHistoryTask(true, stageId, subjectId, editionId, null, pageIndex);
//        List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans = PublicVolumeBean.findDataSortList(stageId, subjectId, editionId);
//        if (mSortBeans != null) {
//            filterSort(mSortBeans);
//        } else {
//            topRightView.setVisibility(View.INVISIBLE);
//            rootView.loading(true);
//            cancelEditionInfoTask();
//            requestEditionInfoTask = new RequestEditionInfoTask(getActivity(), stageId,
//                    subjectId, new AsyncLocalCallBack() {
//                @Override
//                public void updateLocal(YanxiuBaseBean result) {
//                    SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                    if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                        if(NetWorkTypeUtils.isNetAvailable()){
//                            rootView.netError(true);
//                        } else {
//                            filterData(subjectEditionBean);
//                        }
//                        PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
//                    }
//                }
//                @Override
//                public void update(YanxiuBaseBean result) {
//                    SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                    if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                        filterData(subjectEditionBean);
//                        PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
//                    }else {
//                        String errMsg = subjectEditionBean.getStatus().getDesc();
//                        if(TextUtils.isEmpty(errMsg)) {
//                            rootView.dataError(true);
//                        } else {
//                            rootView.dataNull(errMsg);
//                        }
//                    }
//                }
//                @Override
//                public void dataError(int type, String errMsg) {
//                    if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
//                        rootView.netError(true);
//                    } else {
//                        if (TextUtils.isEmpty(errMsg)) {
//                            rootView.dataNull(true);
//                        } else {
//                            rootView.dataNull(errMsg);
//                        }
//                    }
//                }
//            });
//            requestEditionInfoTask.start();
//        }
    }

    private RequestEditionInfoTask requestEditionInfoTask;

    private void cancelEditionInfoTask() {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }

    private void filterData(SubjectEditionBean subjectEditionBean) {
        int count = subjectEditionBean.getData().size();
        SubjectEditionBean.DataEntity sortEntity = null;
        boolean hasEditionId = false;
        for (int i = 0; i < count; i++) {
            sortEntity = subjectEditionBean.getData().get(i);
            if (sortEntity.getId().equals(editionId)) {
                LogInfo.log("geny", "ok-------" + sortEntity.getChildren().size());
                hasEditionId = true;
//                initPopWindow(sortEntity.getChildren());
                break;
            }
        }
        pageIndex = 1;

        if (sortEntity != null && sortEntity.getChildren() != null && sortEntity.getChildren().size() != 0) {
            if(hasEditionId) {
                filterSort(sortEntity.getChildren());
            } else {
                volumeId = sortEntity.getChildren().get(0).getId();
                volumeName = sortEntity.getChildren().get(0).getName();
                topRightView.setVisibility(View.VISIBLE);
                rightTxtView.setText(volumeName);
            }
            LogInfo.log("geny", "ok-------volume" + volumeId + "----volumeName=" + volumeName);
        }
        if(!hasEditionId){
            requestPracticeHistoryTask(true, stageId, subjectId, editionId, volumeId, pageIndex);
        }
    }

    private void filterSort(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
        if (TextUtils.isEmpty(volumeId)) {
            volumeId = mSortBeans.get(0).getId();
            volumeName = mSortBeans.get(0).getName();
        } else {
            boolean hasVolumeId = false;
            for (SubjectEditionBean.DataEntity.ChildrenEntity mBean : mSortBeans) {
                if (volumeId.equals(mBean.getId())) {
                    hasVolumeId = true;
                    volumeId = mBean.getId();
                    volumeName = mBean.getName();
                    break;
                }
            }
            if(!hasVolumeId){
                volumeId = mSortBeans.get(0).getId();
                volumeName = mSortBeans.get(0).getName();
            }
        }
        initPopWindow(mSortBeans);
        pageIndex = 1;
        requestPracticeHistoryTask(true,stageId, subjectId, editionId, volumeId, pageIndex);
    }

    private void initPopWindow(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans){
        topRightView.setVisibility(View.VISIBLE);
        rightTxtView.setText(volumeName);
        popupWindow.setDataSource(mSortBeans);
        popupWindow.getmAdapter().setVolume(volumeId);
        popupWindow.getmListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.getmAdapter().setSelectPosition(position);
                popupWindow.getmAdapter().setVolume(null);
                popupWindow.getmAdapter().notifyDataSetChanged();
                popupWindow.closePopup();
                SubjectEditionBean.DataEntity.ChildrenEntity entity = popupWindow.getmAdapter().getItem(position);
                if (!entity.getId().equals(volumeId)) {
                    volumeId = entity.getId();
                    volumeName = entity.getName();
                    rightTxtView.setText(volumeName);
                    pageIndex = 1;
                    isLoadMore = false;
                    xListView.setPullLoadEnable(false);
                    if (adapter != null) {
                        ArrayList<PracticeHistoryChildBean> data = null;
                        adapter.setList(data);
                        adapter = null;
                    }
                    requestPracticeHistoryTask(true, stageId, subjectId, editionId, volumeId, pageIndex);
                }
            }
        });
    }

    private RequestPracticeHistoryTask mRequestPracticeHistoryTask;

    private void requestPracticeHistoryTask(boolean isLoading,String stageId, String subjectId, String beditionId, String volume, int nextPage) {
        if(isLoading){
            rootView.loading(true);
        }
        cancelPracticeHistoryTask();
        mRequestPracticeHistoryTask = new RequestPracticeHistoryTask(getActivity(), stageId, subjectId, beditionId, null, nextPage, mAsyncCallBack);
        mRequestPracticeHistoryTask.start();
    }

    private void cancelPracticeHistoryTask() {
        if (mRequestPracticeHistoryTask != null) {
            mRequestPracticeHistoryTask.cancel();
        }
        mRequestPracticeHistoryTask = null;
    }

    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            rootView.finish();
            cacheTime = System.currentTimeMillis();
            xListView.setSuccRefreshTime(cacheTime);
            xListView.stopRefresh();
            xListView.stopLoadMore();
            if(isLoadMore){
                toLoadMore((PracticeHistoryBean)result);
            }else{
                updateView((PracticeHistoryBean) result);
            }
            isLoadMore = false;
        }
        @Override
        public void dataError(int type, String errMsg) {
            xListView.stopRefresh();
            xListView.stopLoadMore();
            if( adapter == null ) {
                if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    rootView.netError(true);
                } else {
                    rootView.dataNull(0, R.drawable.public_no_qus_bg);
//                    if (TextUtils.isEmpty(errMsg)) {
//                        rootView.dataNull(true);
//                    } else {
//                        rootView.dataNull(errMsg);
//                    }
                }
            } else {
                if(TextUtils.isEmpty(errMsg)){
                    Util.showToast(R.string.no_net_tag);
                } else {
                    Util.showToast(errMsg);
                }
                rootView.finish();
            }
            isLoadMore = false;
        }
    };

    @Override
    protected void requestData() {
        pageIndex = 1;
        requestPracticeHistoryTask(true, stageId, subjectId, editionId,
                volumeId, pageIndex);
    }

    @Override
    public void refreshData() {
        pageIndex = 1;
        requestPracticeHistoryTask(false, stageId, subjectId, editionId,
                volumeId, pageIndex);
    }

    @Override
    protected void loadMoreData() {
        isLoadMore = true;
        requestPracticeHistoryTask(false, stageId, subjectId, editionId,
                volumeId, pageIndex+1);
    }

    @Override
    protected void refreshOver(boolean success) {
        if(success){

        }else{

        }
    }

    @Override
    protected void loadMoreOver(boolean success) {
        if(success){
            pageIndex+=1;
        }else{

        }
    }
}
