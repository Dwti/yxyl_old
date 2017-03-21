package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.student.bean.PublicEditionBean;
import com.yanxiu.gphone.student.bean.PublicSubjectBaseBean;
import com.yanxiu.gphone.student.bean.PublicVolumeBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.fragment.AbsChapterFragment;
import com.yanxiu.gphone.student.fragment.FavouriteChapterFragment;
import com.yanxiu.gphone.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.student.parser.SubjectEditionParser;
import com.yanxiu.gphone.student.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.student.requestTask.RequestFavouriteSectionTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.view.ChapterTabTitleLayout;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.SortPopUpWindow;
import com.yanxiu.gphone.student.view.TitleTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/16.
 */
public class FavouriteSectionActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private PublicLoadLayout mRootView;
    private SortPopUpWindow popupWindow;
    private RequestFavouriteSectionTask mRequestFavouriteSectionTask;
    private ArrayList<DataTeacherEntity> volumeIdList;
    private String title;
    private String stageId;
    private String subjectId;
    private String editionId;
    private String editionName;
    private String chapterId;
    private String chapterName;
    private String sectionId = "0";
    private String sectionName;
    private int has_knp = 1; //=0代表没有知识点 ；=1代表有知识点
    private boolean hasChapterFavQus = true;//是否有章节收藏
    private String volumeId;
    private String volumeName;

    private int isChapterSection = 0; //=0代表章节 ；=1代表知识点
    private String uniteId = "0";
    private String uniteName = "";


    //=======================================================
    private View topRightView;
    private int currItem = -1;
    public FragmentManager fragmentManager;
    private ChapterTabTitleLayout chapterTabTitleLayout;
    private TitleTabLayout titleTabLayout;
    private FavouriteChapterFragment chapterFragment;
    private FavouriteChapterFragment testCenterFragment;
    //========================================================

    public static void launch (Activity activity, String title, String subjectId, String editionId, ArrayList<DataTeacherEntity> volumeIdList, int has_knp) {
        Intent intent = new Intent(activity, FavouriteSectionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("editionId", editionId);
        intent.putExtra("volumeIdList", volumeIdList);
        intent.putExtra("has_knp", has_knp);
        activity.startActivityForResult(intent,00);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_favourite_section_layout);
        setContentView(mRootView);
        fragmentManager = getSupportFragmentManager();
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        title = getIntent().getStringExtra("title");
        subjectId = getIntent().getStringExtra("subjectId");
        editionId = getIntent().getStringExtra("editionId");
        has_knp = getIntent().getIntExtra("has_knp", 1);
        volumeIdList = (ArrayList<DataTeacherEntity>) getIntent().getSerializableExtra("volumeIdList");
        if (volumeIdList != null && volumeIdList.size() > 0) {
            volumeId = volumeIdList.get(0).getId();
            volumeName = volumeIdList.get(0).getName();
            hasChapterFavQus = true;
        } else {
            hasChapterFavQus = false;
        }
        findView();
        initData();
    }

    private void findView () {
        mRootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                initData();
            }
        });
        chapterTabTitleLayout = (ChapterTabTitleLayout) mRootView.findViewById(R.id.rl_top_title);
        titleTabLayout = (TitleTabLayout) chapterTabTitleLayout.findViewById(R.id.view_tab);
        titleTabLayout.setOnTitleTabClick(new TitleTabLayout.OnTitleTabClick() {
            @Override
            public void onLeftClick () {
                LogInfo.log("geny", "onLeftClick =====  ");
                hideAndShowFragment(AbsChapterFragment.CHAPTER);
            }

            @Override
            public void onRightClick () {
                LogInfo.log("geny", "onRightClick =====  ");
                hideAndShowFragment(AbsChapterFragment.TEST_CENTER);
            }
        });
        topRightView = chapterTabTitleLayout.findViewById(R.id.rl_right);
        if (hasChapterFavQus) {
            topRightView.setVisibility(View.VISIBLE);
        } else {
            topRightView.setVisibility(View.GONE);
        }
        popupWindow = new SortPopUpWindow(this);
        popupWindow.create("test", topRightView);
    }

    public void hideAndShowFragment (int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currItem == index) {
            return;
        }
        currItem = index;
        if (chapterFragment != null) {
            transaction.hide(chapterFragment);
        }
        if (testCenterFragment != null) {
            transaction.hide(testCenterFragment);
        }
        switch (currItem) {
            case AbsChapterFragment.CHAPTER:
                isChapterSection = 0;
                if (hasChapterFavQus && topRightView.getVisibility() != View.VISIBLE) {
                    topRightView.setVisibility(View.VISIBLE);
                }
                if (chapterFragment == null) {
                    chapterFragment = new FavouriteChapterFragment();
                    chapterFragment.setArguments(getArguments());
                    transaction.add(R.id.view_content, chapterFragment);
                } else {
                    transaction.show(chapterFragment);
                }
                break;
            case AbsChapterFragment.TEST_CENTER:
                isChapterSection = 1;
                if (topRightView.getVisibility() == View.VISIBLE) {
                    topRightView.setVisibility(View.GONE);
                }
                if (testCenterFragment == null) {
                    testCenterFragment = new FavouriteChapterFragment();
                    testCenterFragment.setArguments(getArguments());
                    transaction.add(R.id.view_content, testCenterFragment);
                } else {
                    transaction.show(testCenterFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void setPopupWindowFilterData (int index) {
        if (index == AbsChapterFragment.CHAPTER) {
            chapterFragment.setFilterRefresh(createPublicSubjectBaseBean());
        } else if (index == AbsChapterFragment.TEST_CENTER) {
            testCenterFragment.setFilterRefresh(createPublicSubjectBaseBean());
        }
    }

    private void setRefreshData (int index) {
        if (index == AbsChapterFragment.CHAPTER) {
            chapterFragment.requestData();
        } else if (index == AbsChapterFragment.TEST_CENTER) {
            testCenterFragment.requestData();
        }
    }

    private Bundle getArguments () {
        Bundle args = new Bundle();
        args.putSerializable("dataEntity", null);
        args.putSerializable("publicSubjectBaseBean", createPublicSubjectBaseBean());
        args.putString("volume", volumeId);
        args.putString("volumeName", volumeName);
        return args;
    }

    private PublicSubjectBaseBean createPublicSubjectBaseBean () {
        PublicSubjectBaseBean mBean = new PublicSubjectBaseBean();
        mBean.setSubjectId(subjectId);
        mBean.setEditionId(editionId);
        mBean.setEditionName(editionName);
        mBean.setChapterId(chapterId);
        mBean.setChapterName(chapterName);
        mBean.setSectionId(sectionId);
        mBean.setSectionName(sectionName);

        mBean.setVolumeId(volumeId);
        mBean.setVolumeName(volumeName);
        mBean.setHasChapterFavQus(hasChapterFavQus);
        mBean.setIsChapterSection(isChapterSection);
        mBean.setUniteId(uniteId);
        mBean.setUniteName(uniteName);
        return mBean;
    }

    @Override
    protected void onResume () {
        super.onResume();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FavouriteViewActivity.FAV_ANSWER_REQUESTCODE && data != null) {
                boolean deleteAction = data.getBooleanExtra("deleteAction", false);
//                boolean isDelAll = data.getBooleanExtra("isDelAll", false);
//                String chapterId = data.getStringExtra("chapterId");
//                String sectionId = data.getStringExtra("sectionId");
//                String uniteId = getIntent().getStringExtra("uniteId");
                int isChapterSection = data.getIntExtra("isChapterSection", 0);
                if (deleteAction) {
                    setRefreshData(isChapterSection == 0 ? AbsChapterFragment.CHAPTER : AbsChapterFragment.TEST_CENTER);
                }
            }
        }
    }

    private void filterSort (List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
        if (TextUtils.isEmpty(volumeId)) {
            volumeId = mSortBeans.get(0).getId();
            volumeName = mSortBeans.get(0).getName();
        } else {
            for (SubjectEditionBean.DataEntity.ChildrenEntity mBean : mSortBeans) {
                if (volumeId.equals(mBean.getId())) {
                    volumeId = mBean.getId();
                    volumeName = mBean.getName();
                    break;
                }
            }
        }
        LogInfo.log("haitian", "volumeId=" + volumeId + "---volumeName=" + volumeName);
        initPopWindow(mSortBeans);
        requestData();
    }

    private void initData () {
        mRootView.loading(true);
        chapterTabTitleLayout.setLeftTitle("");
        chapterTabTitleLayout.setCenterTitle(title);
        if (has_knp == 1) {
            titleTabLayout.setVisibility(View.VISIBLE);
        }
        if (volumeIdList != null && volumeIdList.size() > 0) {
            List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans = new ArrayList<SubjectEditionBean.DataEntity.ChildrenEntity>();
            SubjectEditionBean.DataEntity.ChildrenEntity mSortBean = null;
            for (DataTeacherEntity mDataTeacherEntity : volumeIdList) {
                mSortBean = new SubjectEditionBean.DataEntity.ChildrenEntity();
                mSortBean.setName(mDataTeacherEntity.getName());
                mSortBean.setId(mDataTeacherEntity.getId());
                mSortBeans.add(mSortBean);
            }
            filterSort(mSortBeans);
        } else {
            LogInfo.log("haitian", "findDataSortList");
            List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans = PublicVolumeBean.findDataSortList(stageId, subjectId, editionId);
            mRootView.loading(true);
            if (mSortBeans != null) {
                filterSort(mSortBeans);
            } else {
                LogInfo.log("haitian", "RequestEditionInfoTask");
                topRightView.setVisibility(View.INVISIBLE);
                cancelEditionInfoTask();
                requestEditionInfoTask = new RequestEditionInfoTask(this, stageId,
                        subjectId, new AsyncLocalCallBack() {
                    @Override
                    public void updateLocal (YanxiuBaseBean result) {
//                        SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                        if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                            if (NetWorkTypeUtils.isNetAvailable()) {
//                                filterData(subjectEditionBean);
//                            }
//                            PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId, subjectId + "");
//                        }
                    }

                    @Override
                    public void update (YanxiuBaseBean result) {
                        mRootView.finish();
                        SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                        if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
                            filterData(subjectEditionBean);
                            PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId, subjectId + "");
                        } else {
                            filterData(subjectEditionBean);
//                            String errMsg = subjectEditionBean.getStatus().getDesc();
//                            if (TextUtils.isEmpty(errMsg)) {
//                                mRootView.dataError(true);
//                            } else {
//                                mRootView.showErrorMessage(errMsg);
//                            }
                        }
                    }

                    @Override
                    public void dataError (int type, String errMsg) {
                        mRootView.finish();
                        filterData(null);
//                        if (TextUtils.isEmpty(errMsg)) {
//                            if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
//                                mRootView.showErrorMessage(getString(R.string.no_net_tag));
//                            } else if (type == ErrorCode.NETWORK_REQUEST_ERROR) {
//                                mRootView.netError(true);
//                            } else {
//                                mRootView.dataError(true);
//                            }
//                        } else {
//                            mRootView.showErrorMessage(errMsg);
//                        }
                    }
                });
                String cacheData = requestEditionInfoTask.getCacheData();
                if (cacheData != null) {
                    try {
                        SubjectEditionBean subjectEditionBean = new SubjectEditionParser().initialParse(cacheData);
                        if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
                            if (NetWorkTypeUtils.isNetAvailable()) {
                                LogInfo.log("haitian", "RequestEditionInfoTask--updateLocal--filterData");
                                filterData(subjectEditionBean);
                                return;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                requestEditionInfoTask.start();
            }
        }
    }

    private void filterData (SubjectEditionBean subjectEditionBean) {
        if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
            int count = subjectEditionBean.getData().size();
            SubjectEditionBean.DataEntity sortEntity = null;
            boolean isPopInit = false;
            for (int i = 0; i < count; i++) {
                sortEntity = subjectEditionBean.getData().get(i);
                if (sortEntity.getId().equals(editionId)) {
                    LogInfo.log("geny", "ok-------" + sortEntity.getChildren().size());
                    initPopWindow(sortEntity.getChildren());
                    volumeId = sortEntity.getChildren().get(0).getId();
                    volumeName = sortEntity.getChildren().get(0).getName();
                    topRightView.setVisibility(View.VISIBLE);
                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
                    isPopInit = true;
                    break;
                }
            }
            if (sortEntity != null && sortEntity.getChildren() != null && sortEntity.getChildren().size() != 0 && !isPopInit) {
                volumeId = sortEntity.getChildren().get(0).getId();
                volumeName = sortEntity.getChildren().get(0).getName();
                topRightView.setVisibility(View.VISIBLE);
                chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
                initPopWindow(sortEntity.getChildren());
                LogInfo.log("geny", "ok-------volumeId" + volumeId + "----volumeName=" + volumeName);
            }
        }
        if (!hasChapterFavQus) {
            topRightView.setVisibility(View.INVISIBLE);
        }
        requestData();
    }

    private RequestEditionInfoTask requestEditionInfoTask;

    private void cancelEditionInfoTask () {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }

    private void initPopWindow (List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
        LogInfo.log("haitian", "-------------initPopWindow----------");
        if (hasChapterFavQus) {
            topRightView.setVisibility(View.VISIBLE);
            chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
            popupWindow.setDataSource(mSortBeans);
            popupWindow.getmListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    popupWindow.getmAdapter().setSelectPosition(position);
                    popupWindow.getmAdapter().notifyDataSetChanged();
                    popupWindow.closePopup();
                    SubjectEditionBean.DataEntity.ChildrenEntity entity = popupWindow.getmAdapter().getItem(position);
                    if (!entity.getId().equals(volumeId)) {
                        volumeId = entity.getId();
                        volumeName = entity.getName();
                        chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
                        setPopupWindowFilterData(AbsChapterFragment.CHAPTER);
                    }
                }
            });
        }
    }

    private void requestData () {
        mRootView.finish();
        hideAndShowFragment(AbsChapterFragment.CHAPTER);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        mRootView.finish();
        cancelEditionInfoTask();
        cancelFavouriteEditionTask();
    }

    private void cancelFavouriteEditionTask () {
        if (mRequestFavouriteSectionTask != null) {
            mRequestFavouriteSectionTask.cancel();
        }
        mRequestFavouriteSectionTask = null;
    }

    @Override
    public void onClick (View v) {
    }
}
