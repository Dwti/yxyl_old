package com.yanxiu.gphone.student.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.exampoint.PullDownTitleView;
import com.yanxiu.gphone.student.utils.Configuration;
import com.yanxiu.gphone.student.utils.LoadingLocalImgUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

/**
 *带顶部导航BaseView 所有具备顶部导航工具都可继承
 * Created by Administrator on 2015/9/23.
 */
public abstract class TopViewBaseActivity extends YanxiuJumpBaseActivity implements View.OnClickListener {
    private final static String TAG=TopViewBaseActivity.class.getSimpleName();
    protected PublicLoadLayout mPublicLayout;
    protected RelativeLayout rightView;
    protected TextView titleText;
    protected View bottomView;
    protected TextView leftView;
    protected TextView rightText;
    protected LinearLayout contentContainer;

    protected RelativeLayout topRootView;
    protected RelativeLayout rootView;
    protected final int INDEX_ZERO=0;
    protected final int INDEX_FIRST=1;
    protected final int INDEX_SECOND=2;
    protected final int INDEX_THRIRD=3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Configuration.isAnalyLayout()){
            Util.toDispScalpelFrameLayout(this, R.layout.top_view_activity_layout);
        }else{
            setContentView(R.layout.top_view_activity_layout);
        }
        initTopView();
        setListener();
    }

    private void setListener() {
        rightView.setOnClickListener(this);
        leftView.setOnClickListener(this);
        rightText.setOnClickListener(this);
        setContentListener();
    }


    private void initTopView() {
        rootView=(RelativeLayout)findViewById(R.id.root_view);
        topRootView=(RelativeLayout)findViewById(R.id.top_layout);
        rightView=(RelativeLayout)findViewById(R.id.pub_right_layout);
        rightText=(TextView)findViewById(R.id.pub_top_right);
        titleText=(TextView)findViewById(R.id.pub_top_mid);
        bottomView=findViewById(R.id.bottom_view);
        bottomView.setVisibility(View.GONE);
        leftView=(TextView)findViewById(R.id.pub_top_left);
        contentContainer=(LinearLayout)findViewById(R.id.content_linear);

        setRootView();
        setTopView();
        setContentContainerView();
        View view=getContentView();
        if(!isAttach()){
            if(view==null){
                return;
            }
            contentContainer.addView(view);
        }

    }

    protected abstract boolean isAttach();

    /**
     * 得到MergeView   isAttach为true调用此方法
     * @param layoutId
     * @return
     */
    protected View getAttachView(int layoutId){
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(layoutId,contentContainer,true);
        return view;
    }


    protected void setTopView() {
        RelativeLayout.LayoutParams topRootViewParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonCoreUtil.dipToPx(this, 64));
        topRootView.setLayoutParams(topRootViewParams);
        topRootView.setBackgroundResource(R.color.trans);
    }

    protected void setRootView(){
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    protected void setContentContainerView(){
        contentContainer.setPadding(CommonCoreUtil.dipToPx(this, 10), 0, CommonCoreUtil.dipToPx(this, 14), CommonCoreUtil.dipToPx(this, 21));
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        contentParams.leftMargin= CommonCoreUtil.dipToPx(this, 15);
        contentParams.rightMargin= CommonCoreUtil.dipToPx(this, 15);
        contentContainer.setLayoutParams(contentParams);
        LoadingLocalImgUtils.getInstence().getImage(contentContainer,R.drawable.plastic_bg);
    }

    /**
     * 初始化child Activity 布局View 除去 TopView
     * @return
     */
    protected abstract View getContentView();

    /**
     *  设置所有view的监听
     */
    protected abstract void setContentListener();

    /**
     *  销毁相关数据
     */
    protected abstract void destoryData();

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pub_top_left:
                executeFinish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            executeFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void executeFinish(){
        destoryData();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 公用的错误加载页面
     * @param type 错误码
     * @param msg 错误描述
     * @param isFristLoad 是否是第一次加载 判断是使用Toast还是背景图
     */
    protected void showError(int type,String msg,boolean isFristLoad){

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


    /**
     * 公用的错误加载页面
     * @param type 错误码
     * @param msg 错误描述
     * @param isFristLoad 是否是第一次加载 判断是使用Toast还是背景图
     */
    protected void showErrorWithFlag(int type,String msg,boolean isFristLoad){
        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
               showNetErrorWithFlag(isFristLoad);
                break;
            case ErrorCode.NETWORK_REQUEST_ERROR:

            case ErrorCode.DATA_REQUEST_NULL:

            case ErrorCode.JOSN_PARSER_ERROR:
                showDataError(isFristLoad);
                break;
        }
        LogInfo.log(TAG, msg);
    }




    protected void finishPublicLayout(){
        if(mPublicLayout!=null){
            mPublicLayout.finish();
        }

    }
    protected void showDataError(boolean isFirstLoad){
        if(isFirstLoad){
            if(mPublicLayout!=null){
                mPublicLayout.dataError(getResources().getString(R.string.data_request_fail),false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.data_request_fail));
        }
    }

    protected void showDataErrorWithFlag(boolean isFirstLoad){
        if(isFirstLoad){
            if(mPublicLayout!=null){
                mPublicLayout.dataError(getResources().getString(R.string.data_request_fail),false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.data_request_fail));
        }
    }


    protected void showNetError(boolean isFristLoad){
        if(isFristLoad){
            if(mPublicLayout!=null){
                mPublicLayout.netError(false);
            }
        }else{
            Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
        }
    }


    protected void showNetErrorWithFlag(boolean isShowContent){
        if(isShowContent){
            if(mPublicLayout!=null){
                mPublicLayout.netError(isShowContent);
            }
        }else{
            Util.showToast(getResources().getString(R.string.public_loading_net_null_errtxt));
        }
    }

}
