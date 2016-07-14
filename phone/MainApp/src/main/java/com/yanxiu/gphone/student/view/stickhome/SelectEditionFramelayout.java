package com.yanxiu.gphone.student.view.stickhome;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.SubjectVersionAdapter;
import com.yanxiu.gphone.student.bean.DataStatusEntityBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestSaveEditionInfoTask;
import com.yanxiu.gphone.student.utils.Util;

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

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    private int mAnimationDuration = DEFAULT_ANIM_DURATION;
    private SubjectVersionBean.DataEntity dataSources;
    private ArrayList<SubjectEditionBean.DataEntity> enditionList;

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
        this.dataSources = dataSources;
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
//        this.enditionList = enditionList;
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
        Animation animation;
        RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) rlEditionList.getLayoutParams();
        animation = new ExpandCollapseAnimation(rlEditionList, count, params.height);
        animation.setFillAfter(true);
        clearAnimation();
        startAnimation(animation);
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

//    public void removeCache() {
//        ImageView imageView = (ImageView) this.findViewById(R.id.intelliexe_icon);
//        imageView.setBackgroundResource(-1);
//        tvSname.setText("");
//    }


    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private int count = 3;
        private int height;

        public ExpandCollapseAnimation(View view, int count, int height) {
            mTargetView = view;
            this.count = count;
            this.height = height;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) mTargetView.getLayoutParams();
            params.height = height + (int)(CommonCoreUtil.dipToPx(itemHeight) * count * interpolatedTime) + CommonCoreUtil.dipToPx(itemHint);
            mTargetView.requestLayout();
//            LogInfo.log("geny", "height" + "---" + height);
//            LogInfo.log("geny", "itemHeight" + "---" + itemHeight);
//            LogInfo.log("geny", "count" + "---" + count);
//            LogInfo.log("geny", "(int)(itemHeight * count * interpolatedTime" + "---" + (int)(itemHeight * count * interpolatedTime));
        }

        @Override
        public void initialize( int width, int height, int parentWidth, int parentHeight ) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds( ) {
            return true;
        }
    }


    private void saveData() {
//        rootView.loading(true);
//        cancelRequest();
        if(requestSaveEditionInfoTask != null){
            requestSaveEditionInfoTask.cancel();
        }
        requestSaveEditionInfoTask = new RequestSaveEditionInfoTask(mContext, stageId, subjectId, selectedEntity.getId(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
//                rootView.finish();
                DataStatusEntityBean bean = (DataStatusEntityBean) result;
                if (bean.getCode() == 0) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemChangeListener(selectedEntity);
                    }
//                    forResult(selectedEntity);
                } else {
                    Util.showToast(R.string.save_fail_try_again);
                }
            }

            @Override
            public void dataError(int type, String msg) {
//                rootView.finish();
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
