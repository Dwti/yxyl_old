package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.common.login.LoginModel;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.PublicEditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.view.ChapterTabTitleLayout;
import com.yanxiu.gphone.hd.student.view.ICSPopupWindow;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;
import com.yanxiu.gphone.hd.student.view.SortPopUpWindow;
import com.yanxiu.gphone.hd.student.view.TitleTabLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/1/29.
 */
public class SubjectSectionFragment extends TopBaseFragment implements View.OnClickListener {
    private HomePageFragment mFg;
//    public static final int QUESTON_COUNT = 10;

    private static SubjectSectionFragment subjectSectionFragment;
    private PublicLoadLayout rootView;
    private TitleTabLayout titleTabLayout;


    private String title;
    /////////////////////////////////
    private SubjectVersionBean.DataEntity entity;
    private int stageId;
    private String subjectId;
    private String editionId;
    private String volume;
    private String volumeName;


    private int currItem = -1;

    private View rightView;
    private View rightViewTest;

    private ChapterTabTitleLayout chapterTabTitleLayout;


    private ChapterFragment chapterFragment;
    private TestCenterFragment testCenterFragment;

    ///////////////////////////
    private SortPopUpWindow popupWindow;
    private SubjectEditionBean.DataEntity sortEntity;
//    private RequestEditionInfoTask requestEditionInfoTask;

        private FragmentManager fragmentManager;

        private SubjectEditionBean subjectEditionBean;

    public static Fragment newInstance(){
        if(subjectSectionFragment==null){
            subjectSectionFragment=new SubjectSectionFragment();
        }
        return subjectSectionFragment;
    }



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.entity = (getArguments() != null) ? (SubjectVersionBean.DataEntity) getArguments().getSerializable("entity") : null;
            this.subjectEditionBean = (getArguments() != null) ? (SubjectEditionBean) getArguments().getSerializable("subjectEditionBean") : null;
            this.title = (getArguments() != null) ? getArguments().getString("title") : null;

        }

        @Override
        protected IFgManager getFragmentManagerFromSubClass() {
            return null;
        }

        @Override
        protected int getFgContainerIDFromSubClass() {
            return 0;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = PublicLoadUtils.createPage(this.getActivity(), R.layout.activity_subject_section);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                isInit = false;
                requestSortData();
            }
        });
        initView();
        initData();
        requestSortData();
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //解决当前Framgemt被下一个Fragment覆盖后还能响应事件的Bug
                return true;
            }
        });
        return rootView;
    }




    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mFg= (HomePageFragment) getParentFragment();
        return null;
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

        finish();
    }

    private void finish() {
        LogInfo.log("geny", "SubjectSectionFragment-----mFg.mIFgManager.popStack()");
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }

        subjectSectionFragment = null;
    }




    private void initView() {
        chapterTabTitleLayout = (ChapterTabTitleLayout) rootView.findViewById(R.id.rl_top_title);
        titleTabLayout = (TitleTabLayout) rootView.findViewById(R.id.view_tab);
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

        chapterTabTitleLayout.getLlRightBack().setOnClickListener(this);

        rightView = rootView.findViewById(R.id.rl_right);
        rightViewTest = rootView.findViewById(R.id.rl_right_test);
        rightViewTest.setOnClickListener(this);
        popupWindow = new SortPopUpWindow(this.getActivity());
        popupWindow.create("subject", rightView);
    }


    private void hideAndShowFragment(int index){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(currItem == index){
            return;
        }
        currItem = index;
        if (chapterFragment != null) {
            transaction.hide(chapterFragment);
            LogInfo.log("geny", "chapterFragment =====  transaction.hide(chapterFragment)");
        }
        if (testCenterFragment != null) {
            transaction.hide(testCenterFragment);
            LogInfo.log("geny", "testCenterFragment =====  transaction.hide(testCenterFragment)");
        }
        switch (currItem) {
            case AbsChapterFragment.CHAPTER:
                chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
                chapterTabTitleLayout.setRightImageViewDisp(true);
                if (chapterFragment == null) {
                    chapterFragment = new ChapterFragment();
                    chapterFragment.setArguments(getContentArguments());
                    transaction.add(R.id.view_content, chapterFragment);
                } else {
                    transaction.show(chapterFragment);
                }
                break;
            case AbsChapterFragment.TEST_CENTER:
                if(PreferencesManager.getInstance().getFirstTestCenter()){
                    ICSPopupWindow icsPopupWindow = new ICSPopupWindow(this.getActivity());
                    icsPopupWindow.showAsDropDown(rightView);
                    PreferencesManager.getInstance().setFirstTestCenter();
                }
                chapterTabTitleLayout.setRightImageViewDisp(false);
                if (testCenterFragment == null) {
                    testCenterFragment = new TestCenterFragment();
                    testCenterFragment.setArguments(getContentArguments());
                    transaction.add(R.id.view_content, testCenterFragment);
                } else {
                    transaction.show(testCenterFragment);
                }
                break;
        }
        transaction.commit();
    }

    private Bundle getContentArguments(){
        Bundle args = new Bundle();
        args.putSerializable("dataEntity", entity);
        args.putString("volume", volume);
        args.putString("volumeName", volumeName);
        return args;
    }

    private void initData() {
        fragmentManager = this.getActivity().getSupportFragmentManager();
//        title = this.getActivity().getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
//            tvTopTitle.setText(title);
//            chapterTabTitleLayout.setLeftTitle(title);
            chapterTabTitleLayout.setCenterTitle(title);
            titleTabLayout.setVisibility(View.INVISIBLE);
        }
        if (entity != null && entity.getData() != null) {
//           stageId 学段id
//            volume 册id
//            subjectId 学科id
//            editionId 教材版本id
            if( LoginModel.getUserinfoEntity()!=null){
                stageId = LoginModel.getUserinfoEntity().getStageid();
            }else{
                stageId = -1;
            }

            subjectId = entity.getId();
//            subjectName = entity.getName();
            editionId = entity.getData().getEditionId();
//            editionName = entity.getData().getEditionName();
        }


    }


    private void filterData(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
        if(!TextUtils.isEmpty(PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId))){
            volume = PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId);
            LogInfo.log("geny", "volume =====  " + volume);
        }
        int count = mSortBeans.size();
        for (int i = 0; i < count; i++) {
            SubjectEditionBean.DataEntity.ChildrenEntity entity = mSortBeans.get(i);
            LogInfo.log("geny", "filterData =====  " + entity.getId());
            if(volume != null && volume.trim().equals(entity.getId().trim())){
                volumeName = entity.getName();
                if(currItem == AbsChapterFragment.CHAPTER){
                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
                }
                LogInfo.log("geny", "volumeName =====  " + volumeName);
                break;
            }
            if(volume == null && i == count -1){
                volume = mSortBeans.get(0).getId();
                volumeName = mSortBeans.get(0).getName();
                if(currItem == AbsChapterFragment.CHAPTER){
                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
                }
            }
        }
        initPopWindow(mSortBeans);
        requestData();
    }

    private int hasKnp = -1;
    private static final int HAS_KNP = 1;

    private void filterData(SubjectEditionBean subjectEditionBean) {
        if(!TextUtils.isEmpty(PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId))){
            volume = PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId);
            LogInfo.log("geny", "local volume =====  " + volume);
        }
        int count = subjectEditionBean.getData().size();
        boolean isPopInit = false;
        LogInfo.log("haitian", "-----requestSortData----filterData-----count="+count);
        for (int i = 0; i < count; i++) {
            SubjectEditionBean.DataEntity entity = subjectEditionBean.getData().get(i);
            if (entity.getId().equals(editionId) && entity!= null && entity.getChildren() != null) {
                sortEntity = entity;
                int childCount = sortEntity.getChildren().size();
                for (int j = 0; j < childCount; j++) {
                    LogInfo.log("geny", "local filterData =====  " + sortEntity.getChildren().get(j).getId());
                    if(volume != null && volume.equals(sortEntity.getChildren().get(j).getId())) {
                        volumeName = sortEntity.getChildren().get(j).getName();
                        hasKnp = sortEntity.getData().getHas_knp();
                        isPopInit = true;
                        break;
                    }
                }
            }
        }
        if (sortEntity != null && sortEntity.getChildren() != null && sortEntity.getChildren().size() != 0 && !isPopInit) {
            volume = sortEntity.getChildren().get(0).getId();
            volumeName = sortEntity.getChildren().get(0).getName();
            hasKnp = sortEntity.getData().getHas_knp();
        }
        if(currItem == AbsChapterFragment.CHAPTER){
            chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
        }
        if(sortEntity!=null&&sortEntity.getChildren()!=null){
            initPopWindow(sortEntity.getChildren());
        }
        requestData();
    }

    private void initPopWindow(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
        popupWindow.setDataSource(mSortBeans);
        popupWindow.getmAdapter().setVolume(volume);
        popupWindow.getmListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectEditionBean.DataEntity.ChildrenEntity entity = popupWindow.getmAdapter().getItem(position);
                popupWindow.getmAdapter().setSelectPosition(position);
                popupWindow.getmAdapter().setVolume(null);
                popupWindow.getmAdapter().notifyDataSetChanged();
                popupWindow.closePopup();
                if (!entity.getId().equals(volume)) {
                    volume = entity.getId();
                    volumeName = entity.getName();
                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
//                    requestData();
                    filterData();
                    PreferencesManager.getInstance().setSubjectSection(stageId + "", subjectId, editionId, volume.trim());
                }
            }
        });
    }

    private void requestSortData() {
        LogInfo.log("haitian", "-----requestSortData---------");
        if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
            LogInfo.log("haitian", "-----requestSortData----filterData-----");
            filterData(subjectEditionBean);
            PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
        } else {
            rootView.dataNull(true);
        }

//        rootView.loading(true);

//        //TODO 暂时不读缓存
//        List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans = null;
////                PublicVolumeBean.findDataSortList(stageId + "", subjectId + "", editionId);
//        if (mSortBeans != null) {
//            filterData(mSortBeans);
//        } else {
//            cancelEditionInfoTask();
//            requestEditionInfoTask = new RequestEditionInfoTask(this.getActivity(), stageId + "",
//                    subjectId, new AsyncLocalCallBack() {
//                @Override
//                public void updateLocal(YanxiuBaseBean result) {
//                    SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                    if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                        if (NetWorkTypeUtils.isNetAvailable()) {
//                            rootView.netError(true);
//                        } else {
//                            LogInfo.log("geny", "updateLocal");
//                            filterData(subjectEditionBean);
//                        }
//                        PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
//                    }
//                }
//
//                @Override
//                public void update(YanxiuBaseBean result) {
//                    LogInfo.log("geny", "update");
//                    SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
//                    if (subjectEditionBean != null && subjectEditionBean.getData() != null) {
//                        filterData(subjectEditionBean);
//                        PublicEditionBean.saveListFromSubjectEditionBean(subjectEditionBean.getData(), stageId + "", subjectId + "");
//                    } else {
//                        rootView.dataNull(true);
//                    }
//                }
//
//                @Override
//                public void dataError(int type, String msg) {
//                    if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
//                        rootView.netError(true);
//                    } else {
//                        if (TextUtils.isEmpty(msg)) {
//                            rootView.dataNull(true);
//                        } else {
//                            rootView.dataNull(msg);
//                        }
//                    }
//                }
//            });
//            requestEditionInfoTask.start();
//        }
    }


    public void loading(){
        rootView.loading(true);
    }

    public void netError(){
        rootView.netError(true);
    }

    public void dataNull(String msg){
        rootView.dataNull(msg);
    }

    public void dataNull(){
        rootView.netError(true);
    }

//    private void cancelEditionInfoTask() {
//        if (requestEditionInfoTask != null) {
//            requestEditionInfoTask.cancel();
//        }
//        requestEditionInfoTask = null;
//    }
    private boolean isInit = false;

    private void requestData() {

        LogInfo.log("haitian", "-----requestData----isInit="+isInit);
        if(!isInit){
            rootView.finish();
            LogInfo.log("haitian", "-----requestData----title=" + title);
            chapterTabTitleLayout.setLeftTitle("");
            chapterTabTitleLayout.setCenterTitle(title);
            if(hasKnp == HAS_KNP){
                titleTabLayout.setVisibility(View.VISIBLE);
            }
            hideAndShowFragment(AbsChapterFragment.CHAPTER);
            isInit = true;
        }
    }

    private void filterData(){
        if(chapterFragment != null) {
            chapterFragment.refreshFilterData(volume, volumeName);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogInfo.log("haitian", "isInit =" + isInit);
        chapterFragment = null;
        testCenterFragment = null;
        currItem = -1;
        isInit = false;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mFg=null;
        rootView=null;
        titleTabLayout=null;
        title=null;
        entity=null;
        subjectId=null;
        editionId=null;
        volume=null;
        volumeName=null;
        rightView=null;
        rightViewTest=null;
        chapterTabTitleLayout=null;

        chapterFragment=null;
        testCenterFragment=null;
         popupWindow=null;
        sortEntity=null;
        fragmentManager=null;

        subjectEditionBean=null;
    }
    //    @Override
//    public void onBackPressed() {
//        if (loadingLayout.isShown()) {
//            loadingLayout.setViewGone();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onClick(View v) {
        if (v == rightViewTest) {
            ActivityJumpUtils.jumpToExamPointActivity(this.getActivity(), entity);
        }else if(v == chapterTabTitleLayout.getLlRightBack()){
            executeFinish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log("geny", "SubjectSectionFragment-----onKeyDown");
        if(keyCode==KeyEvent.KEYCODE_BACK){
            executeFinish();
            return true;
        }
        return false;
    }

    @Override
    public void onReset() {
        destoryData();
    }
}
