package com.yanxiu.gphone.hd.student.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.ThirdExamSiteAdapter;
import com.yanxiu.gphone.hd.student.bean.ExamInfoBean;
import com.yanxiu.gphone.hd.student.bean.ExamListInfo;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.bean.ThridExamiEvent;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.jump.ThirdExamSiteJumpModel;
import com.yanxiu.gphone.hd.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.hd.student.requestTask.RequestKnpointQBlockTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestSingleExamInfoTask;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 三级考点
 * Created by Administrator on 2015/11/13.
 */
public class ThirdExamSiteActivity extends TopViewBaseActivity {
    public static final String TAG=ThirdExamSiteActivity.class.getSimpleName();
    public static final int REQUEST_RESULT_CODE=0X00;

    private XListView mListView;
    private ThirdExamSiteAdapter adapter;
    private SubjectVersionBean.DataEntity mDataEntity;
    private ExamInfoBean secexamInfoBean; //二级考点
    private ExamInfoBean firstInfoBean;//一级考点
    private ExamInfoBean thirdInfoBean;//三级考点
    private RequestKnpointQBlockTask blockTask;

    private RequestSingleExamInfoTask singleTask;
    private boolean isRefresh;//记录是否刷新考点列表


    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {

        EventBus.getDefault().register(this);

        titleText.setText(secexamInfoBean.getName());

        setPullDownTitleView();

        setPublicLayout();

        mListView= (XListView) mPublicLayout.findViewById(R.id.examsiteList);
        initAdapter();
        return mPublicLayout;
    }
    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        contentContainer.setPadding(CommonCoreUtil.dipToPx(this, 15),CommonCoreUtil.dipToPx(this, 1), CommonCoreUtil.dipToPx(this, 23), CommonCoreUtil.dipToPx(this, 21));
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        contentParams.leftMargin= CommonCoreUtil.dipToPx(this, 193);
        contentParams.rightMargin= CommonCoreUtil.dipToPx(this, 196);
        contentParams.bottomMargin=CommonCoreUtil.dipToPx(this,12);
        contentParams.topMargin=CommonCoreUtil.dipToPx(this,10);
        contentContainer.setLayoutParams(contentParams);
        contentContainer.setBackgroundResource(R.drawable.plastic_bg);
    }
    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.findViewById(R.id.topTitleView).setBackgroundResource(R.color.trans);
        topRootView.findViewById(R.id.lineView).setVisibility(View.GONE);
        bottomSecLine.setVisibility(View.GONE);
        bottomFirLine.setVisibility(View.GONE);
    }


    private void setPublicLayout() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.second_examsite_layout);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPublicLayout.setLayoutParams(params);
        mPublicLayout.setContentBackground(getResources().getColor(R.color.color_transparent));
        mPublicLayout.setBackgroundColor(getResources().getColor(R.color.color_transparent));
    }

    private void setPullDownTitleView() {
        titleText.setVisibility(View.GONE);
    }

    private void initAdapter() {
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        adapter=new ThirdExamSiteAdapter(this);
        mListView.setAdapter(adapter);
        if(secexamInfoBean.getChildren()!=null&&secexamInfoBean.getChildren().size()>0){
            adapter.setList(mergeList());
        }

    }

    private  List<ExamInfoBean> mergeList() {
        List<ExamInfoBean> tempList=new ArrayList<>();
        ExamInfoBean secondBean=new ExamInfoBean();
        secondBean.setName(secexamInfoBean.getName());
        secondBean.setId(secexamInfoBean.getId());
        secondBean.setData(secexamInfoBean.getData());
        tempList.add(secondBean);
        tempList.addAll(secexamInfoBean.getChildren());
        return tempList;
    }

    @Override
    protected void setContentListener() {
        mPublicLayout.setmRefreshData(refresh);
        mListView.setOnItemClickListener(onItemClickListener);
    }

    public PublicLoadLayout.RefreshData refresh=new PublicLoadLayout.RefreshData() {
        @Override
        public void refreshData() {

        }
    };


    public AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position>0){
                ExamInfoBean thrBean = adapter.getItem(position - 1);
                thirdInfoBean = thrBean;
                if(StringUtils.isEmpty(thrBean.getId())|| StringUtils.isEmpty(secexamInfoBean.getId())||thrBean.getId().equals(secexamInfoBean.getId())){
                    //如果当前选中的是列表中的二级考点标题则不出题
                    return;
                }
                RequestKnpointQBlockTask(mDataEntity.getId(),firstInfoBean.getId(),secexamInfoBean.getId(),thrBean.getId());
            }
        }
    } ;
    private void RequestKnpointQBlockTask(String subjectId,String firstId,String secondId,String thrId) {
        if(StringUtils.isEmpty(subjectId)){
            return;
        }
        if(StringUtils.isEmpty(firstId)&& StringUtils.isEmpty(secondId)&& StringUtils.isEmpty(thrId)){
            return;
        }
        loading();
        cancelTask();
        blockTask=new RequestKnpointQBlockTask(ThirdExamSiteActivity.this, LoginModel.getUserinfoEntity().getStageid(),subjectId,
               firstId,secondId,thrId,RequestKnpointQBlockTask.EXAM_QUSETION,new RequestKnpointQBlockCallBack());
        blockTask.start();
    }

    private void loading(){
        mPublicLayout.loading(true);
    }
    private void lodingFinish(){
        mPublicLayout.finish();
    }
    private class RequestKnpointQBlockCallBack implements AsyncCallBack {

        @Override
        public void update(YanxiuBaseBean result) {
            lodingFinish();
            SubjectExercisesItemBean subjectExercisesItemBean = (SubjectExercisesItemBean) result;
            if(subjectExercisesItemBean!=null){
                isRefresh=true;
                subjectExercisesItemBean.getData().get(0).setStageid(LoginModel.getUserinfoEntity
                        ().getStageid()+"");
                subjectExercisesItemBean.getData().get(0).setSubjectid(mDataEntity.getId());
                subjectExercisesItemBean.getData().get(0).setSubjectName(mDataEntity.getName());

                subjectExercisesItemBean.getData().get(0).setBedition("0");
                subjectExercisesItemBean.getData().get(0).setEditionName("");

                subjectExercisesItemBean.getData().get(0).setVolume("0");
                subjectExercisesItemBean.getData().get(0).setVolumeName("");

                subjectExercisesItemBean.getData().get(0).setChapterid(firstInfoBean.getId());
                subjectExercisesItemBean.getData().get(0).setChapterName(firstInfoBean.getName());

                subjectExercisesItemBean.getData().get(0).setSectionid(secexamInfoBean.getId());
                subjectExercisesItemBean.getData().get(0).setSectionName(secexamInfoBean.getName());

                subjectExercisesItemBean.getData().get(0).setCellid(thirdInfoBean.getId());
                subjectExercisesItemBean.getData().get(0).setCellName(thirdInfoBean.getName());
                subjectExercisesItemBean.getData().get(0).setIsChapterSection(1);
                LogInfo.log("haitian", "ExercisesDataEntity.toString= "+subjectExercisesItemBean
                        .getData().get(0).toString());
                AnswerViewActivity.launchForResult(ThirdExamSiteActivity.this, subjectExercisesItemBean, YanXiuConstant.KPN_REPORT);
            }else{
                isRefresh=false;
                Util.showToast(R.string.server_connection_erro);
            }

        }

        @Override
        public void dataError(int type, String msg) {
            lodingFinish();
            Util.showToast(R.string.server_connection_erro);
        }
    }
    private void cancelTask(){

        if(blockTask!=null){
            blockTask.cancel();
            blockTask=null;
        }
    }


    private void cancelSingleTask(){
        if(singleTask!=null){
            singleTask.cancel();
            singleTask=null;
        }
    }




    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    protected void destoryData() {
        if(isRefresh){
            ActivityJumpUtils.jumpBackFromThirdExamSiteActivity(this, firstInfoBean, RESULT_OK);
        }else{
            ActivityJumpUtils.jumpBackFromThirdExamSiteActivity(this,firstInfoBean,RESULT_CANCELED);
        }
        EventBus.getDefault().unregister(this);
        cancelTask();
        cancelSingleTask();
    }

    @Override
    protected void initLaunchIntentData() {
        ThirdExamSiteJumpModel jumpModel= (ThirdExamSiteJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        mDataEntity=jumpModel.getmDataEntity();
        secexamInfoBean=jumpModel.getSecondExamBean();
        firstInfoBean=jumpModel.getFirstExamBean();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case AnswerViewActivity.LAUNCHER_FROM_GROUP:

                break;
            }
        }
    }





    private void handleChangeData() {
        loading();
        cancelSingleTask();
        singleTask=new RequestSingleExamInfoTask(this, mDataEntity.getId(), firstInfoBean.getId(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                lodingFinish();
                ExamListInfo info= (ExamListInfo) result;
                ExamInfoBean singleInfo=info.getData().get(0);
                firstInfoBean=singleInfo;

                int size=firstInfoBean.getChildren().size();
                for(int i=0;i<size;i++){
                    ExamInfoBean secondExamInfo=firstInfoBean.getChildren().get(i);
                    if(!StringUtils.isEmpty(secexamInfoBean.getId())&&!StringUtils.isEmpty(secondExamInfo.getId())&&secexamInfoBean.getId().equals(secondExamInfo.getId())){
                        secexamInfoBean.setChildren(secondExamInfo.getChildren());
                        break;
                    }
                }
                adapter.setList(mergeList());


            }

            @Override
            public void dataError(int type, String msg) {
                lodingFinish();
                Util.showToast(R.string.server_connection_erro);
            }
        });
        singleTask.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onEventMainThread(ThridExamiEvent event) {
            if(event==null){
                return;
            }
            if(event.isRefresh()){
                handleChangeData();
            }

    }

}
