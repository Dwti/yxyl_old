package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.bean.PublicEditionBean;
import com.yanxiu.gphone.hd.student.bean.PublicSubjectBaseBean;
import com.yanxiu.gphone.hd.student.bean.PublicVolumeBean;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.hd.student.eventbusbean.UpdateMistakeQuestionBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.hd.student.parser.SubjectEditionParser;
import com.yanxiu.gphone.hd.student.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.view.ChapterTabTitleLayout;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;
import com.yanxiu.gphone.hd.student.view.SortPopUpWindow;
import com.yanxiu.gphone.hd.student.view.TitleTabLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/2/17.
 */
public class MistakeSectionFragment extends TopBaseFragment implements View.OnClickListener  {

    private MyErrorRecordContainerFragment mFg;
    private SortPopUpWindow popupWindow;
    private static final String TITLE = "title";
    private static final String SUBJECTID = "subjectId";
    private static final String EDITIONID = "editionId";
    private static final String VOLUMEIDLIST = "volumeIdList";
    private static final String HAS_KNP = "has_knp";

    private PublicLoadLayout rootView;
    

    private int isChapterSection = 0; //=0代表章节 ；=1代表知识点
    private String uniteId = "0";
    private String uniteName = "";

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


    //=======================================================
    private View topRightView;
    private int currItem = -1;
    public FragmentManager fragmentManager;
    private ChapterTabTitleLayout chapterTabTitleLayout;
    private TitleTabLayout titleTabLayout;
    private MistakeChapterFragment chapterFragment;
    private MistakeChapterFragment testCenterFragment;
    //========================================================


    private static MistakeSectionFragment mistakeSectionFragment;

    public static Fragment newInstance(String title, String subjectId, String editionId, ArrayList<DataTeacherEntity> volumeIdList, int has_knp){
        if(mistakeSectionFragment == null){
            mistakeSectionFragment =new MistakeSectionFragment();
            Bundle bundle=new Bundle();
            bundle.putString(TITLE, title);
            bundle.putString(SUBJECTID, subjectId);
            bundle.putString(EDITIONID, editionId);
            bundle.putSerializable(VOLUMEIDLIST, volumeIdList);
            bundle.putInt(HAS_KNP, has_knp);
            mistakeSectionFragment.setArguments(bundle);
        }
        return mistakeSectionFragment;
    }


    private RequestEditionInfoTask requestEditionInfoTask;

    private void cancelEditionInfoTask () {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }


    @Override
    protected void setRootView() {
        super.setRootView();
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(
                this
        );
        getArgumentsData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFg= (MyErrorRecordContainerFragment) getParentFragment();
        rootView= PublicLoadUtils.createPage(getActivity(), R.layout.fragment_favourite_section_layout);
        rootView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        rootView.setContentBackground(android.R.color.transparent);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //解决当前Framgemt被下一个Fragment覆盖后还能响应事件的Bug
                return true;
            }
        });
        findView();
        initData();
        return rootView;
    }

    private void getArgumentsData(){
        stageId = LoginModel.getUserinfoEntity().getStageid()+"";
        title=getArguments().getString(TITLE);
        subjectId=getArguments().getString(SUBJECTID);
        editionId=getArguments().getString(EDITIONID);
        volumeIdList = (ArrayList<DataTeacherEntity>) getArguments().getSerializable(VOLUMEIDLIST);
        has_knp = getArguments().getInt(HAS_KNP);

        if (volumeIdList != null && volumeIdList.size() > 0) {
            volumeId = volumeIdList.get(0).getId();
            volumeName = volumeIdList.get(0).getName();
            hasChapterFavQus = true;
        } else {
            hasChapterFavQus = false;
        }
    }

    private void findView () {
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData () {
                initData();
            }
        });
        chapterTabTitleLayout = (ChapterTabTitleLayout) rootView.findViewById(R.id.rl_top_title);
        chapterTabTitleLayout.getLlRightBack().setOnClickListener(this);
        titleTabLayout = (TitleTabLayout) chapterTabTitleLayout.findViewById(R.id.view_tab);
        titleTabLayout.setOnTitleTabClick(new TitleTabLayout.OnTitleTabClick() {
            @Override
            public void onLeftClick() {
                LogInfo.log("geny", "onLeftClick =====  ");
                hideAndShowFragment(AbsChapterFragment.CHAPTER);
            }

            @Override
            public void onRightClick() {
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
        popupWindow = new SortPopUpWindow(this.getActivity());
        popupWindow.create("test", topRightView);
    }


    private void initData () {
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        fragmentManager = this.getChildFragmentManager();
        rootView.loading(true);
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
            rootView.loading(true);
            if (mSortBeans != null) {
                filterSort(mSortBeans);
            } else {
                LogInfo.log("haitian", "RequestEditionInfoTask");
                topRightView.setVisibility(View.INVISIBLE);
                cancelEditionInfoTask();
                requestEditionInfoTask = new RequestEditionInfoTask(this.getActivity(), stageId,
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
                        rootView.finish();
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
                        rootView.finish();
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
                    chapterFragment = new MistakeChapterFragment();
                    chapterFragment.setArguments(getDataBundle());
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
                    testCenterFragment = new MistakeChapterFragment();
                    testCenterFragment.setArguments(getDataBundle());
                    transaction.add(R.id.view_content, testCenterFragment);
                } else {
                    transaction.show(testCenterFragment);
                }
                break;
        }
        transaction.commit();
    }


    @Override
    protected void initLoadData() {

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

    private void requestData () {
        rootView.finish();
        hideAndShowFragment(AbsChapterFragment.CHAPTER);
    }


    private Bundle getDataBundle () {
        Bundle args = new Bundle();
        args.putSerializable("dataEntity", null);
        args.putSerializable("publicSubjectBaseBean", createPublicSubjectBaseBean());
        args.putString("volume", volumeId);
        args.putString("volumeName", volumeName);
        return args;
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

    private void setPopupWindowFilterData (int index) {
        if (index == AbsChapterFragment.CHAPTER) {
            chapterFragment.setFilterRefresh(createPublicSubjectBaseBean());
        } else if (index == AbsChapterFragment.TEST_CENTER) {
            testCenterFragment.setFilterRefresh(createPublicSubjectBaseBean());
        }
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
    protected void setContentListener() {

    }


    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
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




    @Override
    public void onClick(View v) {
        if(v == chapterTabTitleLayout.getLlRightBack()){
            executeFinish();
        }
    }

    @Override
    protected void destoryData() {
        finish();
    }

    private void finish() {
        cancelEditionInfoTask();
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }
        mistakeSectionFragment = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mFg=null;
        popupWindow=null;
        rootView=null;
        volumeIdList=null;
        topRightView=null;
        fragmentManager=null;
        chapterTabTitleLayout=null;
        titleTabLayout=null;
        chapterFragment=null;
        testCenterFragment=null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            executeFinish();
            return true;
        }
        return false;
    }


    public void onEventMainThread (UpdateMistakeQuestionBean updateBean) {
        if(updateBean==null){
            return;
        }
        switch (currItem){
            case AbsChapterFragment.CHAPTER:
                chapterFragment.setFilterRefresh(createPublicSubjectBaseBean());
                break;
            case AbsChapterFragment.TEST_CENTER:
                testCenterFragment.setFilterRefresh(createPublicSubjectBaseBean());
                break;
        }
    }

    @Override
    public void onReset() {
     destoryData();
    }
}
