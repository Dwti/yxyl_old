package com.yanxiu.gphone.hd.student.view.stickhome;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.SubjectVersionAdapter;
import com.yanxiu.gphone.hd.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestSaveEditionInfoTask;
import com.yanxiu.gphone.hd.student.utils.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/4.
 */
public class SelectEditionFramelayout extends FrameLayout{
    private RelativeLayout rlEditionList;
    private ListView lvEdition;
    private TextView tvSname;
    private Context mContext;
    public static int itemHeight = 46;
    public static int itemHint = 5;
    public static int maxCount = 6;
    private SubjectVersionAdapter mAdapter;

    private SubjectEditionBean.DataEntity selectedEntity;

    private String subjectId;
    private String editionId;
    private int stageId;
    private RequestSaveEditionInfoTask requestSaveEditionInfoTask;

    private OnItemClickListener onItemClickListener;
    private int resId;


    public SelectEditionFramelayout(Context context) {
        super(context);
        initView(context);
    }

    public SelectEditionFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SelectEditionFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlEditionList = (RelativeLayout) this.findViewById(R.id.rl_edition_list);
        tvSname = (TextView) this.findViewById(R.id.tv_sname);
        lvEdition = (ListView) this.findViewById(R.id.lv_edition);
        initData();
    }

    private void initView(Context context){
        mContext = context;
    }

    private void initData(){
        mAdapter = new SubjectVersionAdapter((Activity)mContext);
        mAdapter.setmListener(new SubjectVersionAdapter.SelectPositionEntityListener() {
            @Override
            public void onSelectionPosition(SubjectEditionBean.DataEntity entity) {
                selectedEntity = entity;
                if (selectedEntity != null) {
                    if (editionId != null && editionId.equals(selectedEntity.getId())) {
//                        this.finish();
                        LogInfo.log("geny", "onSelectionPosition-----editionId----" + "---" + editionId);
                        LogInfo.log("geny", "onSelectionPosition-----selectedEntity.getId()----" + "---" + selectedEntity.getId());
                        if(onItemClickListener != null){
                            onItemClickListener.onItemChangeListener(null);
                        }
                    } else {
                        saveData();
                    }
                } else {
                    Util.showToast(R.string.no_data_save);
                }
            }
        });
        lvEdition.setAdapter(mAdapter);
        mAdapter.setListView(lvEdition);
    }

    public void setDataSources(SubjectVersionBean.DataEntity dataSources) {
//        this.dataSources = dataSources;
        LogInfo.log("geny", "SelectEditionFramelayout-----setDataSources----" + "---");
        if(dataSources != null && !TextUtils.isEmpty(dataSources.getName())){
            tvSname.setText(dataSources.getName());
            subjectId = dataSources.getId();
            if(dataSources.getData() != null){
                editionId = dataSources.getData().getEditionId();
            }
            stageId = LoginModel.getUserinfoEntity().getStageid();
        }
    }
    public void setEnditionList(ArrayList<SubjectEditionBean.DataEntity> enditionList) {
        ImageView imageView = (ImageView) this.findViewById(R.id.intelliexe_icon);
        imageView.setBackgroundResource(resId);
        mAdapter.setList(enditionList);
        int count = 1;
        if(enditionList != null && !enditionList.isEmpty()){
            count = enditionList.size();
            if(count > maxCount){
                count = maxCount;
            }else{
                lvEdition.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return event.getAction() == MotionEvent.ACTION_MOVE;
                    }
                });
            }
        }
        RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) rlEditionList.getLayoutParams();
        setMaxHeight(rlEditionList, count, params.height);
    }




    private void setMaxHeight(final RelativeLayout view, int count, int height){

        RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.height = height + (Util.dipToPx(itemHeight) * count) + Util.dipToPx(itemHint);
        view.requestLayout();

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        animator.setDuration(300);
        animator.start();


    }

    public void recycleView() {
        cancelTask();

        rlEditionList = null;
        lvEdition = null;
        tvSname = null;
        mContext = null;
        mAdapter = null;

        selectedEntity = null;

        requestSaveEditionInfoTask = null;
        onItemClickListener = null;
    }


    private void cancelTask(){
        if(requestSaveEditionInfoTask != null){
            requestSaveEditionInfoTask.cancel();
        }
    }


    public void setResId(int resId) {
        this.resId = resId;
    }


    private void saveData() {
        cancelTask();
        requestSaveEditionInfoTask = new RequestSaveEditionInfoTask(mContext, stageId, subjectId, selectedEntity.getId(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                DataStatusEntityBean bean = (DataStatusEntityBean) result;
                if (bean.getCode() == 0) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemChangeListener(selectedEntity);
                    }
                } else {
                    Util.showToast(R.string.save_fail_try_again);
                }
            }

            @Override
            public void dataError(int type, String msg) {
                Util.showToast(R.string.save_fail_try_again);
            }
        });
        requestSaveEditionInfoTask.start();
    }

    public interface OnItemClickListener{
        void onItemChangeListener(SubjectEditionBean.DataEntity selectedEntity);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
