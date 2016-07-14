package com.yanxiu.gphone.student.view.stickhome;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.SubjectSectionActivity;
import com.yanxiu.gphone.student.adapter.IntelliExeAdapter;
import com.yanxiu.gphone.student.bean.PublicEditionBean;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.inter.AsyncLocalCallBack;
import com.yanxiu.gphone.student.requestTask.RequestEditionInfoTask;
import com.yanxiu.gphone.student.utils.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/3.
 */
public class StickHomeLayout extends RelativeLayout implements View.OnClickListener{

    public static final int ROW_NUM = 2;

    private Context mContext;
    private IntelliExeAdapter mAdapter;
    private ScrollHomeView mScrollView;
    private View viewMark;
    private POSITION_TYPE type;

    private int lastPosition = 0;
    private int currentPosition = 0;

    private SelectEditionFramelayout containter;

    private SubjectVersionBean.DataEntity currentDataEntity;

    private FrameLayout flLeftContainter;
    private FrameLayout flRightContainter;

    private GridView mGridView;

    private View viewBottom;

    private int resId;

    private OnItemClickListener onItemClickListener;
    public StickHomeLayout(Context context) {
        super(context);
        initView(context);
    }

    public StickHomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StickHomeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.stick_grid_view, this);

        mGridView = (GridView) this.findViewById(R.id.home_grid_view);
        mScrollView = (ScrollHomeView) this.findViewById(R.id.home_scroll_view);
        mScrollView.setScrollFixPositionListener(new ScrollHomeView.ScrollFixPositionListener() {

            @Override
            public void scrollFixPosition() {
                mScrollView.setIsFromUserClick(false);
                LogInfo.log("geny", "StickHomeLayout------scrollFixPosition");
                showContainterView(type, currentDataEntity);
            }
        });
        viewBottom = this.findViewById(R.id.view_bottom);


        flLeftContainter = (FrameLayout) this.findViewById(R.id.fl_left_containt);
        flRightContainter = (FrameLayout) this.findViewById(R.id.fl_right_containt);
        viewMark = this.findViewById(R.id.view_mark);
        viewMark.setOnClickListener(this);
    }
    private int selectPosition = -1;
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            LogInfo.log("geny", "MyOnGlobalLayoutListener----------onGlobalLayout");
            viewBottom.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            //如果是点击是当前位置直接展示containterview 如果不是就滑动到相对应的位置后展示
            if(currentPosition == lastPosition){
                mScrollView.setIsScrolling(true);
                mScrollView.setIsFromUserClick(false);
                showContainterView(type, currentDataEntity);
                LogInfo.log("geny", "MyOnGlobalLayoutListener----------showContainterView");
            }else{
//                mScrollView.setIsFromUserClick(true);
                LogInfo.log("geny", "MyOnGlobalLayoutListener----------else  showContainterView");
                mScrollView.setIsScrolling(true);
                mScrollView.setFixPosition(currentPosition);
                mScrollView.smoothScrollTo(0, currentPosition);
            }


        }
    }


    private void srcollViewToPosition(){
        LogInfo.log("geny", "srcollViewToPosition");
        mScrollView.setIsFromUserClick(true);
        ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        viewBottom.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        ViewGroup.LayoutParams layoutParams = viewBottom.getLayoutParams();
        layoutParams.height = CommonCoreUtil.getScreenHeight();
        viewBottom.setLayoutParams(layoutParams);
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    /**
     * 内容的填充
     * 版本选择的view
     */
    private void showContainterView(POSITION_TYPE type, SubjectVersionBean.DataEntity subjectData){
        LogInfo.log("geny", "StickHomeLayout------showContainterView");
        containter = (SelectEditionFramelayout) LayoutInflater.from(mContext).inflate(R.layout.item_intelliexe_containter, null);
        switch (type) {
            case LEFT_TYPE:
                flLeftContainter.addView(containter);
                break;
            case RIGHT_TYPE:
                flRightContainter.addView(containter);
                break;
        }
        containter.setDataSources(subjectData);
        containter.setResId(resId);
        containter.setEnditionList(dataEntity);
        containter.setOnItemClickListener(new SelectEditionFramelayout.OnItemClickListener() {

            @Override
            public void onItemChangeListener(SubjectEditionBean.DataEntity selectedEntity) {
                dismissContaintner();
                if(selectedEntity != null){
                    LogInfo.log("geny", "StickHomeLayout------onItemChangeListener");
                    if(onItemClickListener != null){
                        onItemClickListener.onItemChangeListener(selectedEntity);
                    }
                }
            }
        });

        viewMark.setVisibility(View.VISIBLE);
        mScrollView.setFixPosition(-1);
        mScrollView.setIsScrolling(false);
    }



    public void setAdapter(IntelliExeAdapter adapter) {
        this.mAdapter = adapter;
        mAdapter.setOnSubjectListener(new IntelliExeAdapter.OnSubjectListener() {
            @Override
            public void selectSubjectVersion(int position, View view) {
                selectItem(position, view);

            }

            @Override
            public void selectSubject(int position, View view) {
                if (mAdapter.getItem(position).getData() != null) {
                    String editionName = mAdapter.getItem(position).getData().getEditionName();

                    if (!TextUtils.isEmpty(editionName) && editionName.equals("未选择")) {
                        selectItem(position, view);
                    } else {
                        SubjectSectionActivity.launch(mContext,
                                mAdapter.getItem(position).getName(),
                                mAdapter.getItem(position));
                    }
                } else {
                    selectItem(position, view);
                }

            }
        });
        mGridView.setAdapter(mAdapter);
    }

    private void selectItem(int position, View view){
        stageId = LoginModel.getUserinfoEntity().getStageid();
        subjectId = mAdapter.getItem(position).getId();
        currentDataEntity = mAdapter.getItem(position);
        resId = currentDataEntity.getResId();

        selectPosition = position;

        if (position % ROW_NUM == 0) {
            type = POSITION_TYPE.LEFT_TYPE;
        } else {
            type = POSITION_TYPE.RIGHT_TYPE;
        }
        lastPosition = mScrollView.getLastPosition();
        currentPosition = (int) view.getY();
        requestData();
    }

    private void dismissContaintner(){
        ViewGroup.LayoutParams layoutParams = viewBottom.getLayoutParams();
        layoutParams.height = 0;
        viewBottom.setLayoutParams(layoutParams);

        containter.removeAllViews();

        flLeftContainter.removeAllViews();
        flRightContainter.removeAllViews();
        mScrollView.smoothScrollTo(0, lastPosition);
        viewMark.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if(v == viewMark){
            dismissContaintner();
        }
    }

    private enum POSITION_TYPE{
        LEFT_TYPE,
        RIGHT_TYPE
    }


    //////add by 2.0
    private ArrayList<SubjectEditionBean.DataEntity> dataEntity;
    private RequestEditionInfoTask requestEditionInfoTask;
    private int stageId;
    private String subjectId;


    private void cancelRequestEdition() {
        if (requestEditionInfoTask != null) {
            requestEditionInfoTask.cancel();
        }
        requestEditionInfoTask = null;
    }

    private void requestData() {
        if(onItemClickListener != null){
            onItemClickListener.loading();
        }
        cancelRequestEdition();
        requestEditionInfoTask = new RequestEditionInfoTask(mContext, stageId + "",
                subjectId, new AsyncLocalCallBack() {
            @Override
            public void updateLocal(YanxiuBaseBean result) {
                LogInfo.log("geny", "updateLocal");

                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                dataEntity = (ArrayList) subjectEditionBean.getData();
                if (dataEntity != null && !dataEntity.isEmpty()) {
                    if(onItemClickListener != null){
                        onItemClickListener.finish();
                    }
                    srcollViewToPosition();
                    PublicEditionBean.saveListFromSubjectEditionBean(dataEntity, stageId + "", subjectId + "");
                }
            }

            @Override
            public void update(YanxiuBaseBean result) {
                LogInfo.log("geny", "update");
                SubjectEditionBean subjectEditionBean = (SubjectEditionBean) result;
                dataEntity = (ArrayList) subjectEditionBean.getData();
                if (dataEntity == null || dataEntity.size() <= 0) {
                    if (TextUtils.isEmpty(subjectEditionBean.getStatus().getDesc())) {

                        if(onItemClickListener != null){
                            onItemClickListener.dataNull(true);
                        }
                    } else {
                        if(onItemClickListener != null){
                            onItemClickListener.dataNull(subjectEditionBean.getStatus().getDesc());
                        }
                    }
                } else {
                    if(onItemClickListener != null){
                        onItemClickListener.finish();
                    }
                    PublicEditionBean.saveListFromSubjectEditionBean(dataEntity, stageId + "", subjectId + "");
                    srcollViewToPosition();
                }
            }

            @Override
            public void dataError(int type, String msg) {
                if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    if(onItemClickListener != null){
                        onItemClickListener.finish();
                    }
                    Util.showToast(R.string.net_null);
                    return;
                }
                LogInfo.log("geny", "dataError type=" + type + " msg=" + msg);
                if (mAdapter == null || mAdapter.getCount() <= 0) {
                    if (type == ErrorCode.NETWORK_REQUEST_ERROR || type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        if(onItemClickListener != null){
                            onItemClickListener.netError();
                        }
                    } else {
                        if (TextUtils.isEmpty(msg)) {
                            if(onItemClickListener != null){
                                onItemClickListener.dataNull(true);
                            }
                        } else {
                            if(onItemClickListener != null){
                                onItemClickListener.dataNull(msg);
                            }
                        }
                    }
                } else {
                    if(onItemClickListener != null){
                        onItemClickListener.finish();
                    }
                    if (TextUtils.isEmpty(msg)) {
                        Util.showToast(R.string.public_loading_data_null);
                    } else {
                        Util.showToast(msg);
                    }
                }
            }
        });
        requestEditionInfoTask.start();
    }


    public interface OnItemClickListener{
        void onItemChangeListener(SubjectEditionBean.DataEntity selectedEntity);
        void loading();
        void finish();
        void netError();
        void dataNull(String str);
        void dataNull(boolean isDataNull);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
