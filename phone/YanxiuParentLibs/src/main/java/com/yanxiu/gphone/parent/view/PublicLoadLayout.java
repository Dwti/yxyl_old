package com.yanxiu.gphone.parent.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.utils.ParentUtils;


public class PublicLoadLayout extends FrameLayout implements View.OnClickListener {

    protected FrameLayout content;

    protected ParentLoadingLayout loading;

    protected View error, errorChildLayout;


    protected TextView refreshBtn;
    protected Context context;
    protected TextView errorTxt1;
    protected TextView errorTxt2;
    protected ImageView errorImage;

    protected RefreshData mRefreshData;


    public void setmRefreshData (RefreshData refreshData) {
        this.mRefreshData = refreshData;
    }

    public PublicLoadLayout (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PublicLoadLayout (Context context) {
        super(context);
        init(context);
    }

    private void init (Context context) {
        this.context = context;
        inflate(context, R.layout.public_loading_layout_p, this);
        findView();
//        setBackgroundColor(getResources().getColor(R.color.color_white_p));
    }

    public void addContent(int viewId, ViewGroup.LayoutParams layoutParams) {
        inflate(getContext(), viewId, content);
    }

    private void findView () {

        content = (FrameLayout) findViewById(R.id.content);
        loading = (ParentLoadingLayout) findViewById(R.id.loading);
        error = findViewById(R.id.error);
        errorChildLayout = error.findViewById(R.id.error_child_view);
        errorTxt2 = (TextView) error.findViewById(R.id.errorTxt2);
        errorImage = (ImageView) errorChildLayout.findViewById(R.id.net_error_flag);
        errorTxt1 = (TextView) errorChildLayout.findViewById(R.id.errorTxt1);
        refreshBtn = (TextView) errorChildLayout.findViewById(R.id.try_again);
        refreshBtn.setOnClickListener(this);
    }

    /**
     * 设置装载内容布局容器背景颜色
     *
     * @param color
     */
    public void setContentBackground (int color) {
        if (content == null) {
            return;
        }
        content.setBackgroundColor(color);
    }

    /**
     * 获取错误提示页面
     *
     * @return
     */
    public View getErrorLayoutView () {
        return errorChildLayout;
    }


    /**
     * 设置装载布局是否可见
     *
     * @param visible
     */
    public void setContentVisible (int visible) {
        if (content == null) {
            return;
        }
        content.setVisibility(visible);
    }

    public void loading (boolean isShowContent) {

        loading.setViewType(ParentLoadingLayout.LoadingType.LAODING_COMMON);
        error.setVisibility(GONE);
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void loadingWithNoMargin (boolean isShowContent) {

        loading.setViewType(ParentLoadingLayout.LoadingType.LAODING_COMMON);
        error.setVisibility(GONE);
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }


    public void finish () {
        loading.setVisibility(GONE);
        error.setVisibility(GONE);
        //      halfBlackTopView.setVisibility(GONE);
        content.setVisibility(VISIBLE);
    }

    public void finishWithNoContent () {
        loading.setVisibility(GONE);
        error.setVisibility(GONE);
        error.setVisibility(GONE);
        //      halfBlackTopView.setVisibility(GONE);
        content.setVisibility(GONE);
    }

    public void netError (boolean isShowContent) {
        loading.setVisibility(GONE);
        error.setVisibility(VISIBLE);

        errorChildLayout.setVisibility(VISIBLE);
//        if (!errorImage.isShown()) {
//            errorImage.setVisibility(VISIBLE);
//        }
        if (!errorTxt1.isShown()) {
            errorTxt1.setVisibility(VISIBLE);
        }
        errorTxt2.setVisibility(GONE);
        if (!refreshBtn.isShown()) {
            refreshBtn.setVisibility(VISIBLE);
        }
        errorTxt1.setText(R.string.public_loading_net_errtxt_p);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white_p));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void showErrorMessage (String errMsg) {
        loading.setVisibility(GONE);
//        errorImage.setVisibility(GONE);

        error.setVisibility(VISIBLE);
        errorChildLayout.setVisibility(VISIBLE);
        error.setBackgroundColor(Color.TRANSPARENT);
        errorTxt2.setVisibility(GONE);
        errorTxt1.setText(errMsg);

        content.setVisibility(VISIBLE);
        refreshBtn.setVisibility(VISIBLE);
    }

    public void dataError (int errmsg, boolean isShowContent) {
        loading.setVisibility(GONE);

        error.setVisibility(VISIBLE);
        errorChildLayout.setVisibility(VISIBLE);
        errorTxt1.setText(errmsg);
//        errorImage.setVisibility(VISIBLE);

        errorTxt2.setVisibility(GONE);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white_p));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void dataError (String errmsg, boolean isShowContent) {
        LogInfo.log("lee", "parentDataError");
        loading.setVisibility(GONE);

        errorChildLayout.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
//        errorImage.setVisibility(VISIBLE);

        errorTxt2.setVisibility(GONE);
        errorTxt1.setText(errmsg);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white_p));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    //TODO 此方法容易引起误解  数据加载失败  提示 网络失败？
    public void dataError (boolean isShowContent) {
        loading.setVisibility(GONE);

        error.setVisibility(VISIBLE);
        errorChildLayout.setVisibility(VISIBLE);
        errorTxt2.setVisibility(GONE);
        errorTxt1.setText(R.string.public_loading_net_errtxt_p);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white_p));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void dataNull (boolean isShowContent) {
        loading.setVisibility(GONE);
        errorChildLayout.setVisibility(GONE);
        error.setVisibility(VISIBLE);
        refreshBtn.setVisibility(GONE);
//        errorImage.setVisibility(VISIBLE);

        errorTxt2.setVisibility(VISIBLE);
        errorTxt2.setText(R.string.public_loading_data_null_p);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white_p));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void dataNull (int msgRes) {

        loading.setVisibility(GONE);
        errorChildLayout.setVisibility(GONE);
        error.setVisibility(VISIBLE);
        refreshBtn.setVisibility(GONE);
        error.setBackgroundColor(Color.TRANSPARENT);
//        errorImage.setVisibility(VISIBLE);

        errorTxt2.setVisibility(VISIBLE);
        errorTxt2.setText(msgRes);
        content.setVisibility(VISIBLE);
    }

    public void dataNull (String msg) {
        loading.setVisibility(GONE);
        errorChildLayout.setVisibility(GONE);
        error.setVisibility(VISIBLE);
        refreshBtn.setVisibility(GONE);
        error.setBackgroundColor(Color.TRANSPARENT);

        errorTxt2.setVisibility(VISIBLE);
        errorTxt2.setText(msg);
        content.setVisibility(VISIBLE);
//        errorImage.setVisibility(VISIBLE);

    }

    @Override
    public void onClick (View v) {
        if (v == refreshBtn) {
            if (mRefreshData != null) {
                mRefreshData.refreshData();
            }
        }
    }

    public interface RefreshData {
        void refreshData ();
    }

    /**
     * 公用的错误加载页面
     * @param type 错误码
     * @param msg 错误描述
     * @param isFristLoad 是否是第一次加载 判断是使用Toast还是背景图
     */
    public  void showErrorWithFlag(int type,String msg,boolean isFristLoad){
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

    }

    protected void showDataError(boolean isFirstLoad){
        if(isFirstLoad){
            dataError(getResources().getString(R.string.data_request_fail_p),
                    false);
        }else{
            ParentUtils.showToast(R.string.data_request_fail_p);
        }
    }

    public void showNetErrorWithFlag(boolean isShowContent){
        if(isShowContent){
            netError(isShowContent);
        }else{
            ParentUtils.showToast(R.string.public_loading_net_null_errtxt_p);
        }
    }

}
