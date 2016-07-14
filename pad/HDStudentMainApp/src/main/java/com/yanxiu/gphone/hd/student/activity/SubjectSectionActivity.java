package com.yanxiu.gphone.hd.student.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;

/**
 * Created by Administrator on 2015/7/7.
 */
public class SubjectSectionActivity extends YanxiuBaseActivity implements View.OnClickListener {
//    public static final int QUESTON_COUNT = 10;
//
//    private PublicLoadLayout rootView;
////    private StudentLoadingLayout loadingLayout;
////    private TextView tvTopTitle;
//    private TitleTabLayout titleTabLayout;
//
//
//    private String title;
//    /////////////////////////////////
//    private SubjectVersionBean.DataEntity entity;
//    private int stageId;
//    private String subjectId;
//    private String editionId;
//    private String volume;
//    private String volumeName;
//
//
//    private View viewContent;
//
//    private int currItem = -1;
//
//    private View rightView;
//    private View rightViewTest;
//
//    private ChapterTabTitleLayout chapterTabTitleLayout;
//
//
//    private ChapterFragment chapterFragment;
//    private TestCenterFragment testCenterFragment;
//
//    ///////////////////////////
//    private SortPopUpWindow popupWindow;
//    private SubjectEditionBean.DataEntity sortEntity;
//    private RequestEditionInfoTask requestEditionInfoTask;
//
//    public FragmentManager fragmentManager;

    public static void launch(Context context, String title, SubjectVersionBean.DataEntity entity) {
        Intent intent = new Intent(context, SubjectSectionActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("bean", entity);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        rootView = PublicLoadUtils.createPage(this, R.layout.activity_subject_section);
//        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
//            @Override
//            public void refreshData() {
//                isInit = false;
//                requestSortData();
//            }
//        });
//        this.setContentView(rootView);
//        initView();
//        initData();
//        requestSortData();
//    }
//
//
//
//    private void initView() {
//
//        chapterTabTitleLayout = (ChapterTabTitleLayout) findViewById(R.id.rl_top_title);
//        titleTabLayout = (TitleTabLayout) findViewById(R.id.view_tab);
//        titleTabLayout.setOnTitleTabClick(new TitleTabLayout.OnTitleTabClick() {
//            @Override
//            public void onLeftClick() {
//                LogInfo.log("geny", "onLeftClick =====  ");
//                hideAndShowFragment(AbsChapterFragment.CHAPTER);
////                chapterTabTitleLayout.getRlRight().setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onRightClick() {
//                LogInfo.log("geny", "onRightClick =====  ");
//                hideAndShowFragment(AbsChapterFragment.TEST_CENTER);
////                chapterTabTitleLayout.getRlRight().setVisibility(View.INVISIBLE);
//            }
//        });
////        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
////        mViewPager = (ViewPager)rootView.findViewById(R.id.vp_chapter);
////        ivBack.setOnClickListener(this);
////        tvTopTitle = (TextView) this.findViewById(R.id.tv_top_title);
////        intellVolumeTxt = (TextView) this.findViewById(R.id.intell_volume_txt);
//
//        rightView = this.findViewById(R.id.rl_right);
//        rightViewTest = this.findViewById(R.id.rl_right_test);
//        rightViewTest.setOnClickListener(this);
//        popupWindow = new SortPopUpWindow(SubjectSectionActivity.this);
//        popupWindow.create("subject", rightView);
////        popupWindow.setOnClickListener();
//    }
//
//
//    public void hideAndShowFragment(int index){
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if(currItem == index){
//            return;
//        }
//        currItem = index;
//        if (chapterFragment != null) {
//            transaction.hide(chapterFragment);
//            LogInfo.log("geny", "chapterFragment =====  transaction.hide(chapterFragment)");
//        }
//        if (testCenterFragment != null) {
//            transaction.hide(testCenterFragment);
//            LogInfo.log("geny", "testCenterFragment =====  transaction.hide(testCenterFragment)");
//        }
//        switch (currItem) {
//            case AbsChapterFragment.CHAPTER:
//                chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
//                chapterTabTitleLayout.setRightImageViewDisp(true);
//                if (chapterFragment == null) {
//                    chapterFragment = new ChapterFragment();
//                    chapterFragment.setArguments(getArguments());
//                    transaction.add(R.id.view_content, chapterFragment);
//                } else {
//                    transaction.show(chapterFragment);
//                }
//                break;
//            case AbsChapterFragment.TEST_CENTER:
//                if(PreferencesManager.getInstance().getFirstTestCenter()){
//                    ICSPopupWindow icsPopupWindow = new ICSPopupWindow(this);
//                    icsPopupWindow.showAsDropDown(rightView);
//                    PreferencesManager.getInstance().setFirstTestCenter();
//                }
//                chapterTabTitleLayout.setRightImageViewDisp(false);
//                if (testCenterFragment == null) {
//                    testCenterFragment = new TestCenterFragment();
//                    testCenterFragment.setArguments(getArguments());
//                    transaction.add(R.id.view_content, testCenterFragment);
//                } else {
//                    transaction.show(testCenterFragment);
//                }
//                break;
//        }
//        transaction.commit();
//    }
//
//    private Bundle getArguments(){
//        Bundle args = new Bundle();
//        args.putSerializable("dataEntity", entity);
//        args.putString("volume", volume);
//        args.putString("volumeName", volumeName);
//        return args;
//    }
//
//    private void initData() {
//        fragmentManager = getSupportFragmentManager();
//        title = this.getIntent().getStringExtra("title");
//        if (!TextUtils.isEmpty(title)) {
////            tvTopTitle.setText(title);
////            chapterTabTitleLayout.setLeftTitle(title);
//            chapterTabTitleLayout.setCenterTitle(title);
//            titleTabLayout.setVisibility(View.INVISIBLE);
//        }
//        entity = (SubjectVersionBean.DataEntity) this.getIntent().getSerializableExtra("bean");
//
//        if (entity != null && entity.getData() != null) {
////           stageId 学段id
////            volume 册id
////            subjectId 学科id
////            editionId 教材版本id
//            stageId = LoginModel.getUserInfo().getStageid();
//            subjectId = entity.getId();
////            subjectName = entity.getName();
//            editionId = entity.getData().getEditionId();
////            editionName = entity.getData().getEditionName();
//        }
//
//
//    }
//
//
//    private void filterData(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
//        if(!TextUtils.isEmpty(PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId))){
//            volume = PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId);
//            LogInfo.log("geny", "volume =====  " + volume);
//        }
//        int count = mSortBeans.size();
//        for (int i = 0; i < count; i++) {
//            SubjectEditionBean.DataEntity.ChildrenEntity entity = mSortBeans.get(i);
//            LogInfo.log("geny", "filterData =====  " + entity.getId());
//            if(volume != null && volume.trim().equals(entity.getId().trim())){
//                volumeName = entity.getName();
//                if(currItem == AbsChapterFragment.CHAPTER){
//                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
//                }
//                LogInfo.log("geny", "volumeName =====  " + volumeName);
//                break;
//            }
//
//            if(volume == null && i == count -1){
//                volume = mSortBeans.get(0).getId();
//                volumeName = mSortBeans.get(0).getName();
//                if(currItem == AbsChapterFragment.CHAPTER){
//                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
//                }
//            }
//        }
//        initPopWindow(mSortBeans);
//        requestData();
//    }
//
//    private int hasKnp = -1;
//    public static final int HAS_KNP = 1;
//
//    private void filterData(SubjectEditionBean subjectEditionBean) {
//        if(!TextUtils.isEmpty(PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId))){
//            volume = PreferencesManager.getInstance().getSubjectSection(stageId+"", subjectId, editionId);
//            LogInfo.log("geny", "local volume =====  " + volume);
//        }
//        int count = subjectEditionBean.getData().size();
//        boolean isPopInit = false;
//        for (int i = 0; i < count; i++) {
//            SubjectEditionBean.DataEntity entity = subjectEditionBean.getData().get(i);
//            if (entity.getId().equals(editionId) && entity!= null && entity.getChildren() != null) {
//                sortEntity = entity;
//                int childCount = sortEntity.getChildren().size();
//                for (int j = 0; j < childCount; j++) {
//                    LogInfo.log("geny", "local filterData =====  " + sortEntity.getChildren().get(j).getId());
//                    if(volume != null && volume.equals(sortEntity.getChildren().get(j).getId())) {
//                        volumeName = sortEntity.getChildren().get(j).getName();
//                        hasKnp = sortEntity.getData().getHas_knp();
//                        isPopInit = true;
//                        break;
//                    }
//                }
//            }
//        }
//        if (sortEntity != null && sortEntity.getChildren() != null && sortEntity.getChildren().size() != 0 && !isPopInit) {
//            volume = sortEntity.getChildren().get(0).getId();
//            volumeName = sortEntity.getChildren().get(0).getName();
//            hasKnp = sortEntity.getData().getHas_knp();
//        }
//        if(currItem == AbsChapterFragment.CHAPTER){
//            chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
//        }
//        initPopWindow(sortEntity.getChildren());
//        requestData();
//    }
//
//    private void initPopWindow(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans) {
//        popupWindow.setDataSource(mSortBeans);
//        popupWindow.getmAdapter().setVolume(volume);
//        popupWindow.getmListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SubjectEditionBean.DataEntity.ChildrenEntity entity = popupWindow.getmAdapter().getItem(position);
//                popupWindow.getmAdapter().setSelectPosition(position);
//                popupWindow.getmAdapter().setVolume(null);
//                popupWindow.getmAdapter().notifyDataSetChanged();
//                popupWindow.closePopup();
//                if (!entity.getId().equals(volume)) {
//                    volume = entity.getId();
//                    volumeName = entity.getName();
//                    chapterTabTitleLayout.setIntellVolumeTxt(volumeName);
////                    requestData();
//                    filterData();
//                    PreferencesManager.getInstance().setSubjectSection(stageId + "", subjectId, editionId, volume.trim());
//                }
//            }
//        });
//    }
//
//    private void requestSortData() {
//        rootView.loading(true);
//
//        //TODO 暂时不读缓存
//        List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans = null;
////                PublicVolumeBean.findDataSortList(stageId + "", subjectId + "", editionId);
//        if (mSortBeans != null) {
//            filterData(mSortBeans);
//        } else {
//            cancelEditionInfoTask();
//            requestEditionInfoTask = new RequestEditionInfoTask(this, stageId + "",
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
//    }
//
//
//    public void finishRootView(){
//        rootView.finish();
//    }
//
//    public void loading(){
//        rootView.loading(true);
//    }
//
//    public void netError(){
//        rootView.netError(true);
//    }
//
//    public void dataNull(String msg){
//        rootView.dataNull(msg);
//    }
//
//    public void dataNull(){
//        rootView.netError(true);
//    }
//
//    private void cancelEditionInfoTask() {
//        if (requestEditionInfoTask != null) {
//            requestEditionInfoTask.cancel();
//        }
//        requestEditionInfoTask = null;
//    }
//    private boolean isInit = false;
//
//    private void requestData() {
//        if(!isInit){
//            rootView.finish();
//            chapterTabTitleLayout.setLeftTitle("");
//            chapterTabTitleLayout.setCenterTitle(title);
//            if(hasKnp == HAS_KNP){
//                titleTabLayout.setVisibility(View.VISIBLE);
//            }
////            if(hasKnp == HAS_KNP){
////                chapterTabTitleLayout.setLeftTitle(title);
////                chapterTabTitleLayout.setCenterTitle("");
////                titleTabLayout.setVisibility(View.VISIBLE);
//////            chapterTabTitleLayout.setVisibility(View.INVISIBLE);
////            }else{
////                chapterTabTitleLayout.setCenterTitle(title);
////                chapterTabTitleLayout.setLeftTitle("");
////                titleTabLayout.setVisibility(View.INVISIBLE);
//////            chapterTabTitleLayout.setVisibility(View.VISIBLE);
////            }
//            hideAndShowFragment(AbsChapterFragment.CHAPTER);
//            isInit = true;
//        }
//    }
//
//    private void filterData(){
//        if(chapterFragment != null) {
//            chapterFragment.refreshFilterData(volume, volumeName);
//        }
////        LogInfo.log("geny", "filterData------" + volume);
////        FragmentTransaction transaction = fragmentManager.beginTransaction();
////        if(chapterFragment != null){
////            transaction.remove(chapterFragment);
////        }
////        chapterFragment = new ChapterFragment();
////        chapterFragment.setArguments(getArguments());
////        transaction.add(R.id.view_content, chapterFragment);
////        transaction.commit();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cancelEditionInfoTask();
//    }
//
////    @Override
////    public void onBackPressed() {
////        if (loadingLayout.isShown()) {
////            loadingLayout.setViewGone();
////        } else {
////            super.onBackPressed();
////        }
////    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == rightViewTest) {
//            ActivityJumpUtils.jumpToExamPointActivity(this, entity);
//        }
//    }

}
