package com.yanxiu.gphone.studentold.exampoint.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.common.core.utils.BasePopupWindow;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.adapter.SubjectListAdapter;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;
import com.yanxiu.gphone.studentold.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestSubjectInfoTask;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamTypeSelPop extends BasePopupWindow {
    private RequestSubjectInfoTask subTask;
    private SubjectListAdapter adapter;
    private SelResultCallBack selResultCallBack;
    private LayoutInflater inflater;
    public ExamTypeSelPop(Context mContext) {
        super(mContext);
        loadSubjectList();
    }

    public interface SelResultCallBack{
        void resultCallBack(int position ,SubjectVersionBean.DataEntity dataEntity);
    }

    public void setSelResultCallBackListener(SelResultCallBack selResultCallBack){
        this.selResultCallBack=selResultCallBack;
    }
    @Override
    protected void initView(Context mContext) {
        inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.examtype_sel_layout,null);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);
        GridView mListView = (GridView) view.findViewById(R.id.selList);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        adapter=new SubjectListAdapter((Activity)mContext);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (YanxiuApplication.getInstance().getSubjectVersionBean() == null) {
                    return;
                }

                List<SubjectVersionBean.DataEntity> data = YanxiuApplication.getInstance().getSubjectVersionBean().getData();
                if (data == null || data.isEmpty()) {
                    return;
                }
                if (selResultCallBack != null) {
                    selResultCallBack.resultCallBack(position, data.get(position));
                }
                dismiss();
            }
        });
    }

    @Override
    public void loadingData() {

    }

    @Override
    protected void destoryData() {

    }



    @Override
    public void onClick(View v) {

    }


    private void loadSubjectList() {
        if(YanxiuApplication.getInstance().getSubjectVersionBean()!=null){
            setDataToAdapter(YanxiuApplication.getInstance().getSubjectVersionBean().getData());
        }else{
            if(subTask!=null){
                subTask.cancel();
                subTask=null;
            }
            if(LoginModel.getUserinfoEntity()==null){
                return;
            }

            subTask = new RequestSubjectInfoTask(mContext,
                    LoginModel.getUserinfoEntity().getStageid(), new AsyncLocalCallBack() {
                @Override
                public void updateLocal(YanxiuBaseBean result) {
                    SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                    YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
                    setDataToAdapter(subjectVersionBean.getData());
                }

                @Override
                public void update(YanxiuBaseBean result) {
                    SubjectVersionBean subjectVersionBean = (SubjectVersionBean) result;
                    YanxiuApplication.getInstance().setSubjectVersionBean(subjectVersionBean);
                    setDataToAdapter(subjectVersionBean.getData());
                }

                @Override
                public void dataError(int type, String msg) {

                }
            });
            subTask.start();
        }

    }


    private void setDataToAdapter(List<SubjectVersionBean.DataEntity> data){
        if(data==null||data.isEmpty()){
            return;
        }
        adapter.setList(data);
    }



}
