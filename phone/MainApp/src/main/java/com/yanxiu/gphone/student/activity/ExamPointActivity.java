package com.yanxiu.gphone.student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ExamPointListAdapter;
import com.yanxiu.gphone.student.bean.ExamInfoBean;
import com.yanxiu.gphone.student.bean.ExamListInfo;
import com.yanxiu.gphone.student.bean.ExamPointEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.exampoint.ExamAnalyContainer;
import com.yanxiu.gphone.student.exampoint.PullDownTitleView;
import com.yanxiu.gphone.student.exampoint.view.SubjectTypeContainer;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.inter.GetDataCommonInter;
import com.yanxiu.gphone.student.jump.ExamPointJumpModel;
import com.yanxiu.gphone.student.jump.ThirdExamSiteJumpBackModel;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.requestTask.RequestExamInfoTask;
import com.yanxiu.gphone.student.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.student.requestTask.RequestSingleExamInfoTask;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamPointActivity extends  TopViewBaseActivity implements GetDataCommonInter {

    private static final String TAG = ExamPointActivity.class.getSimpleName();

    private ListView pinnedSectionListView;
    private View bottomShadow;
    private ExamAnalyContainer container;
    private ExamPointListAdapter adapter;
    private RequestExamInfoMananger manager;
    private RequestExamInfoTask multiTask;
    private RequestSingleExamInfoTask singleTask;
    private boolean isFirstLoad=true;
    private SubjectVersionBean.DataEntity mDataEntity;
    private List<ExamPointEntity> entityList=new ArrayList<>();

    private RequestKnpointQBlockTask blockTask;

    private ExamListInfo currentInfo;

    private ExamPointEntity currentExamPointBean;//列表当前选中的考点对象

    private final static  String EXAMINFO_KEY="ExamListInfo";

    private final static String EXAMPOINT_KEY="ExamPointEntity";

    private final  static String EXAMPOINT_LIST_KEY="ExampointList";

    private SubjectTypeContainer typeContainer;

    private View  mPublicLayout;

    private LayoutInflater mLayoutInflater;
    private PublicLoadLayout errorLayout;

    private PullDownTitleView titleView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        initView();
        initHeadView();
        initAdapter();
        initRequestExamInfoMananger();
        requestServerData(mDataEntity);
        return mPublicLayout;
    }



    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.findViewById(R.id.topTitleView).setBackgroundResource(R.color.trans);
        topRootView.findViewById(R.id.lineView).setVisibility(View.GONE);
    }

    private void initView() {
        addPencilImage(rootView);
        setPullDownTitleView();
        setPublicLayout();
        initErrorLayout();
        pinnedSectionListView=(ListView)mPublicLayout.findViewById(R.id.pinnedListView);
        bottomShadow=mPublicLayout.findViewById(R.id.bottomShadow);
        isShowListViewWithShadow(View.GONE);

        typeContainer=(SubjectTypeContainer)mPublicLayout.findViewById(R.id.SubjectTypeContainer);
        typeContainer.setVisibility(View.GONE);
        typeContainer.setSubjectId(mDataEntity);
        typeContainer.addObserver(titleView);
        typeContainer.setEnabled(false);


    }

    private void initErrorLayout() {
        errorLayout=(PublicLoadLayout)mPublicLayout.findViewById(R.id.errorLayout);
        RelativeLayout.LayoutParams errorChildParams= (RelativeLayout.LayoutParams) errorLayout.getErrorLayoutView().getLayoutParams();
        errorChildParams.leftMargin=10;
        errorChildParams.rightMargin=10;
        errorLayout.getErrorLayoutView().setLayoutParams(errorChildParams);
        errorLayout.setContentVisible(View.GONE);
        errorLayout.setBackgroundColor(getResources().getColor(R.color.trans));
        errorLayout.setContentBackground(getResources().getColor(R.color.trans));
    }

    private void  isShowListViewWithShadow(int visible){
        pinnedSectionListView.setVisibility(visible);
        bottomShadow.setVisibility(visible);
    }

    private void setPublicLayout() {
        mLayoutInflater=LayoutInflater.from(this);
        mPublicLayout = mLayoutInflater.inflate(R.layout.exam_point_layout,null);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPublicLayout.setLayoutParams(params);
        mPublicLayout.setBackgroundColor(getResources().getColor(R.color.color_transparent));
    }

    private void setPullDownTitleView() {
        titleText.setVisibility(View.GONE);
        titleView=new PullDownTitleView(this);
        RelativeLayout.LayoutParams titleViewParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleViewParams.topMargin=getResources().getDimensionPixelOffset(R.dimen.dimen_40);
        titleViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleView.setLayoutParams(titleViewParams);
        titleView.setVisibility(View.VISIBLE);
        titleView.setEnabled(false);
        rootView.addView(titleView, rootView.getChildCount());
        setTopTitle(mDataEntity);
    }

    private void addPencilImage(RelativeLayout rootView) {
        ImageView pencilImg=new ImageView(this);
        pencilImg.setImageResource(R.drawable.pencil_bg);
        RelativeLayout.LayoutParams pencilParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pencilParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        pencilParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        pencilParams.leftMargin= CommonCoreUtil.dipToPx(this, 94);
        pencilImg.setLayoutParams(pencilParams);
        rootView.addView(pencilImg);
    }

    private void setTopTitle(SubjectVersionBean.DataEntity mDataEntity){
        if(mDataEntity==null){
            return;
        }
        titleView.setTitle(mDataEntity.getName());
    }


    private void requestServerData(SubjectVersionBean.DataEntity mDataEntity) {
        loading();
        manager.setDataEntity(mDataEntity);
        manager.requestExamInfo(mDataEntity);
    }

    private void initRequestExamInfoMananger() {
        manager=new RequestExamInfoMananger();
        manager.addObserver(container);
        manager.addObserver(typeContainer);
    }

    private void initAdapter() {
        adapter=new ExamPointListAdapter(this);
        pinnedSectionListView.setAdapter(adapter);
    }

    private void initHeadView() {
        container=new ExamAnalyContainer(this);
        container.setBackgroundColor(getResources().getColor(R.color.color_fff5cc));
        container.setGetDataCommonInter(this);
        container.initData();

        pinnedSectionListView.addHeaderView(container);
    }

    @Override
    protected void setContentListener() {
        errorLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
               requestServerData(mDataEntity);
            }
        });
        titleView.setCommunicateInter(new PullDownTitleView.CommunicateInter() {
            @Override
            public void onClick(View view) {}

            @Override
            public View getView() {
                return typeContainer;
            }
        });
        typeContainer.setSelResultCallBackListener(new SubjectTypeContainer.SelResultCallBack() {
            @Override
            public void viewStatus(int visible) {
                LogInfo.log(TAG, "VISIBLE: " + visible);
            }

            @Override
            public void resultCallBack(int position, SubjectVersionBean.DataEntity dataEntity) {
                if (mDataEntity.getId().equals(dataEntity.getId())) {
                    return;
                }
                requestServerData(dataEntity);
            }
        });

        pinnedSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    position=position - pinnedSectionListView.getHeaderViewsCount();
                    if (position > 0) {
                        currentExamPointBean = adapter.getItem(position);
                        if (currentExamPointBean.getType() == ExamPointEntity.ITEM) {
                            ExamInfoBean examInfoBean = (ExamInfoBean) currentExamPointBean.getOriginData();
                            if (examInfoBean.getChildren() == null || examInfoBean.getChildren().isEmpty()) {
                                RequestKnpointQBlockTask(currentExamPointBean);
                            } else {
                                ExamInfoBean sectionBean = (ExamInfoBean) currentExamPointBean.getSectionOrignData();
                                ActivityJumpUtils.jumpToThirdExamSiteActivityForResult(ExamPointActivity.this, ThirdExamSiteActivity.REQUEST_RESULT_CODE, mDataEntity, sectionBean, examInfoBean);
                            }

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void RequestKnpointQBlockTask(ExamPointEntity bean) {
        if(bean==null){
            return;
        }

        ExamInfoBean  sectionBean= (ExamInfoBean) bean.getSectionOrignData();
        ExamInfoBean  itemBean= (ExamInfoBean) bean.getOriginData();
        loading();
        cancelBlockTask();
        if(LoginModel.getUserinfoEntity()==null){
            return;
        }
        blockTask=new RequestKnpointQBlockTask(ExamPointActivity.this,LoginModel.getUserinfoEntity().getStageid(),mDataEntity.getId(),
                sectionBean.getId(),itemBean.getId(),"",RequestKnpointQBlockTask.EXAM_QUSETION,new RequestKnpointCallBack());
        blockTask.start();
    }

    private void loading(){
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.loadingWithNoMargin(true);
    }


    private void loadingFinish(){
        errorLayout.setVisibility(View.GONE);
        errorLayout.finishWithNoContent();
    }

    private void cancelBlockTask(){
        if(blockTask!=null){
            blockTask.cancel();
            blockTask=null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable(EXAMINFO_KEY, currentInfo);
        outState.putSerializable(EXAMPOINT_KEY, currentExamPointBean);
        outState.putSerializable(EXAMPOINT_LIST_KEY, (Serializable) entityList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentInfo= (ExamListInfo) savedInstanceState.getSerializable(EXAMINFO_KEY);
        currentExamPointBean= (ExamPointEntity) savedInstanceState.getSerializable(EXAMPOINT_KEY);
        entityList= (List<ExamPointEntity>) savedInstanceState.getSerializable(EXAMPOINT_LIST_KEY);

    }

    private class RequestKnpointCallBack implements  AsyncCallBack{

        @Override
        public void update(YanxiuBaseBean result) {
            loadingFinish();
            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
            if(subjectExercisesItemBean!=null){
                AnswerViewActivity.launchForResult(ExamPointActivity.this, subjectExercisesItemBean, YanXiuConstant.KPN_REPORT);
            }else{
                Util.showToast(R.string.server_connection_erro);
            }

        }

        @Override
        public void dataError(int type, String msg) {
            loadingFinish();
            if(StringUtils.isEmpty(msg)){
                Util.showToast(R.string.server_connection_erro);

            }else{
                Util.showToast(msg);
            }

        }
    }


    @Override
    public Object getData() {
        return titleView;
    }


    class RequestExamInfoMananger extends Observable{
        private SubjectVersionBean.DataEntity rDataEntity=null;

        public void setDataEntity(SubjectVersionBean.DataEntity dataEntity){
            this.rDataEntity=dataEntity;
        }
        public void requestExamInfo(final SubjectVersionBean.DataEntity dataEntity){

            if(dataEntity==null||StringUtils.isEmpty(dataEntity.getId())){
                return;
            }
            cancelTask();
            multiTask=new RequestExamInfoTask(ExamPointActivity.this, dataEntity.getId(), new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    titleView.setEnabled(true);
                     loadingFinish();
                     isFirstLoad=false;
                     mDataEntity = rDataEntity;
                     setTopTitle(rDataEntity);
                     setChanged();
                     notifyObservers(result);
                     updateUI(result);

                }

                @Override
                public void dataError(int type, String msg) {
                     titleView.setEnabled(true);
                     loadingFinish();
                     showError(type, msg, isFirstLoad);
                }
            });
            multiTask.start();
        }


        private void  showError(int type,String msg,boolean isFristLoad){

            switch (type){
                case ErrorCode.NETWORK_NOT_AVAILABLE:
                    showNetError(isFristLoad);
                    break;
                case ErrorCode.NETWORK_REQUEST_ERROR:

                case ErrorCode.DATA_REQUEST_NULL:

                case ErrorCode.JOSN_PARSER_ERROR:
                    showDataError(isFristLoad);
                    break;
            }
            LogInfo.log(TAG, msg);
        }


        private void showNetError(boolean isFristLoad){
            if(isFristLoad){
                if(errorLayout!=null){
                    errorLayout.netError(isFristLoad);
                }
            }else{
                Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
            }
        }

        protected void showDataError(boolean isFirstLoad){
            if(isFirstLoad){
                if(errorLayout!=null){
                    errorLayout.dataError(getResources().getString(R.string.data_request_fail), isFirstLoad);
                }
            }else{
                Util.showToast(getResources().getString(R.string.data_request_fail));
            }
        }





        public void requestSingleExamInfo(final SubjectVersionBean.DataEntity dataEntity,String chapterId){

            if(dataEntity==null||StringUtils.isEmpty(dataEntity.getId())||StringUtils.isEmpty(chapterId)){
                return;
            }
            cancelSingleTask();
            singleTask=new RequestSingleExamInfoTask(ExamPointActivity.this, dataEntity.getId(), chapterId, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    loadingFinish();
                    handleChangeData(result);
                    setChanged();
                    notifyObservers(currentInfo);
                    LogInfo.log(TAG, "RequestSingleExamInfoTask update");
                }

                @Override
                public void dataError(int type, String msg) {
                    loadingFinish();
                    showError(type, msg, false);
                    LogInfo.log(TAG, "RequestSingleExamInfoTask dataError");
                }
            });
            singleTask.start();
        }

        private void cancelSingleTask(){
            if(singleTask!=null){
                singleTask.cancel();
                singleTask=null;
            }
        }



        private void cancelTask(){
            if(multiTask!=null){
                multiTask.cancel();
                multiTask=null;
            }
        }

    }

    private boolean checkParams(ExamListInfo info){
        return !(info == null || info.getData() == null || info.getData().isEmpty());
    }

    /**
     * 处理一级考点更新
     * @param result
     */
    private void  handleChangeData(YanxiuBaseBean result){
        if(result instanceof ExamListInfo){
            if(!checkParams(currentInfo)){
                return;
            }
            ExamListInfo info= (ExamListInfo) result;
            ExamInfoBean singleInfo=info.getData().get(0);
            List<ExamInfoBean> chapterList=currentInfo.getData();
            currentInfo.setData(  chapterListData(chapterList,singleInfo));
        }
        updateList(currentInfo);

    }


    /**
     * 更新考点列表
     * @param chapterList
     * @param singleInfo
     * @return
     */
    private List<ExamInfoBean>  chapterListData(  List<ExamInfoBean> chapterList, ExamInfoBean singleInfo){
        int size=chapterList.size();
        for(int i=0;i<size;i++){
            ExamInfoBean infoBean =chapterList.get(i);
            if(!StringUtils.isEmpty(infoBean.getId())&&!StringUtils.isEmpty(singleInfo.getId())&&infoBean.getId().equals(singleInfo.getId())){
                infoBean.setChildren(singleInfo.getChildren());
                infoBean.setData(singleInfo.getData());
                infoBean.setId(singleInfo.getId());
                infoBean.setName(singleInfo.getName());
                break;
            }
        }
        return chapterList;
    }


    private void updateUI(YanxiuBaseBean result){
        try{
            if(result instanceof ExamListInfo){
               isShowListViewWithShadow(View.VISIBLE);
                ExamListInfo info= (ExamListInfo) result;
                currentInfo=info;
                updateList(currentInfo);
            }
        }catch (Exception e){
            showDataError(true);
        }

    }

    /**
     * 更新列表数据
     * @param info
     */

    private void updateList(ExamListInfo info){
        clearLastData();
        int firSize=info.getData().size();
        for(int i=0;i<firSize;i++){
            ExamInfoBean firstExamPointBean=info.getData().get(i);
            if(firstExamPointBean.getChildren()!=null){
                ExamPointEntity entity=new ExamPointEntity();
                entity.setType(ExamPointEntity.SECTION);
                entity.setName(firstExamPointBean.getName());
                entity.setValue(firstExamPointBean.getData().getMasterNum() + "/" + firstExamPointBean.getData().getTotalNum());
                entity.setOriginData(firstExamPointBean);
                entityList.add(entity);

                int secSize=firstExamPointBean.getChildren().size();
                for(int j=0;j<secSize;j++){
                    ExamInfoBean secondExamPointBean=firstExamPointBean.getChildren().get(j);
                    ExamPointEntity secondEntity=new ExamPointEntity();
                    secondEntity.setOriginData(secondExamPointBean);
                    secondEntity.setSectionOrignData(firstExamPointBean);
                    secondEntity.setType(ExamPointEntity.ITEM);
                    secondEntity.setName(secondExamPointBean.getName());
                    secondEntity.setValue(secondExamPointBean.getData().getMasterNum() + "/" + secondExamPointBean.getData().getTotalNum());
                    entityList.add(secondEntity);
                }

            }

        }
        adapter.setList(entityList);
        pinnedSectionListView.setSelection(0);
    }






    /**
     * 清除缓存数据
     */
    private void clearLastData(){
        if(!entityList.isEmpty()){
            entityList.clear();
        }
    }



    @Override
    protected void destoryData() {
        cancelBlockTask();
        manager.cancelTask();
        manager.cancelSingleTask();
    }

    @Override
    protected void initLaunchIntentData() {
        ExamPointJumpModel jumpModel= (ExamPointJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        mDataEntity=jumpModel.getDataEntity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case AnswerViewActivity.LAUNCHER_FROM_GROUP:
                    LogInfo.log(TAG,"AnswerViewActivity.LAUNCHER_FROM_GROUP");
                    manager.requestSingleExamInfo(mDataEntity,((ExamInfoBean)currentExamPointBean.getOriginData()).getId());
                    break;
                case ThirdExamSiteActivity.REQUEST_RESULT_CODE:
                    LogInfo.log(TAG,"ThirdExamSiteActivity.REQUEST_RESULT_CODE");
                    ThirdExamSiteJumpBackModel jumpBackModel= (ThirdExamSiteJumpBackModel) getBaseJumpModeFromSetResult(data);
                    if(jumpBackModel==null){
                        return;
                    }
                    if(jumpBackModel.getChapterExam()==null){
                        LogInfo.log(TAG,"ThirdExamSiteActivity.REQUEST_RESULT_CODE--requestSingleExamInfo");
                        manager.requestSingleExamInfo(mDataEntity,jumpBackModel.getChapterExam().getId());
                    }else{
                        if(!checkParams(currentInfo)){
                            return;
                        }
                        LogInfo.log(TAG,"ThirdExamSiteActivity.REQUEST_RESULT_CODE--update");
                        List<ExamInfoBean> chapterList=currentInfo.getData();
                        currentInfo.setData(chapterListData(chapterList,jumpBackModel.getChapterExam()));
                        updateList(currentInfo);
                    }
                    break;

            }
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(typeContainer.getVisibility()==View.VISIBLE){
            typeContainer.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
