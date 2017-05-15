package com.yanxiu.gphone.studentold.exampoint.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.adapter.SubjectListAdapter;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;
import com.yanxiu.gphone.studentold.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestSubjectInfoTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2015/12/9.
 */
public class SubjectTypeContainer extends RelativeLayout implements Observer   {
    private Context mContext;
    private RequestSubjectInfoTask subTask;
    private SubjectListAdapter adapter;
    private SelResultCallBack selResultCallBack;
    private ViewStatusObserable viewStatusObserable;
    private SubjectVersionBean.DataEntity mDataEntity;
    private List<SubjectVersionBean.DataEntity> data=new ArrayList<>();
    private int selectPos=-1;
    public void setSubjectId(SubjectVersionBean.DataEntity mDataEntity) {
        this.mDataEntity=mDataEntity;
        loadSubjectList();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o==null){
            return;
        }
        replaceSelItemForCurItem(selectPos);
    }

    private void replaceSelItemForCurItem(int selectPos) {
        if(selectPos<0){
            return;

        }
        if(data==null||data.size()==0){
            return;
        }
        SubjectVersionBean.DataEntity itemDataEntity = data.get(selectPos);
        if(itemDataEntity==null||mDataEntity==null|| StringUtils
                .isEmpty(itemDataEntity.getId())||StringUtils.isEmpty(mDataEntity.getId())){
            return;
        }
        if(itemDataEntity.getId().equals(mDataEntity.getId())){
            return;
        }
        data.remove(selectPos);
        data.add(selectPos, mDataEntity);
        mDataEntity = itemDataEntity;
        adapter.notifyDataSetChanged();
    }

    public interface SelResultCallBack{

        void viewStatus(int visible);

        void resultCallBack(int position ,SubjectVersionBean.DataEntity dataEntity);
    }


    public void setSelResultCallBackListener(SelResultCallBack selResultCallBack){
        this.selResultCallBack=selResultCallBack;
    }

    public void addObserver(Observer object){
        if(object==null){
            return;
        }
        viewStatusObserable.addObserver(object);
    }

    public SubjectTypeContainer(Context context) {
        super(context);
        init(context);
    }


    public SubjectTypeContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SubjectTypeContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SubjectTypeContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    class ViewStatusObserable extends Observable{
         public void notityAllObserver(View v){
             setChanged();
            notifyObservers(v);
         }
    }



    private void init(Context context) {
        mContext=context;
        adapter=new SubjectListAdapter((Activity)mContext);
        viewStatusObserable=new ViewStatusObserable();
        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.examtype_sel_layout,this);
        setBackgroundColor(getResources().getColor(R.color.color_b2000000));
        GridView mListView = (GridView)findViewById(R.id.selList);
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
                if(selResultCallBack!=null){
                    selResultCallBack.viewStatus(v.getVisibility());
                }
            }
        });

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SubjectTypeContainer.this.data == null) {
                    return;
                }
                if (data == null || data.isEmpty()) {
                    return;
                }
                if (selResultCallBack != null) {
                    SubjectVersionBean.DataEntity itemDataEntity = data.get(position);
                    selectPos=position;
                    selResultCallBack.resultCallBack(position, itemDataEntity);
                }
                setVisibility(View.GONE);
            }
        });


    }


    private void setDataToAdapter(List<SubjectVersionBean.DataEntity> tempdata){
        if(tempdata==null||tempdata.isEmpty()){
            return;
        }
        if(!this.data.isEmpty()){
            this.data.clear();
        }
        this.data.addAll(tempdata);
        int j=data.size();
        for(int i=0;i<j;i++){
            if(mDataEntity.getId().equals(data.get(i).getId())){
                this.data.remove(data.get(i));
                break;
            }
        }
        adapter.setList(data);
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



    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        viewStatusObserable.notityAllObserver(this);
    }
}
