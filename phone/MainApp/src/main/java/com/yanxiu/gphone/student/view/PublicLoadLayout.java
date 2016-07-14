package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;


public class PublicLoadLayout extends FrameLayout implements View.OnClickListener{

    protected FrameLayout content;

    protected StudentLoadingLayout loading;

    protected View error,errorChildLayout;


    protected TextView refreshBtn;
    protected Context context;
    protected TextView errorTxt1;
    protected TextView errorTxt2;
    protected ImageView errorImage;
//    protected View halfBlackTopView;
    protected RefreshData mRefreshData;
    protected RelativeLayout.LayoutParams layoutParams;
    public RefreshData getmRefreshData() {
        return mRefreshData;
    }

    public void setmRefreshData(RefreshData refreshData) {
        this.mRefreshData = refreshData;
    }

    public PublicLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PublicLoadLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflate(context, R.layout.public_loading_layout, this);
        layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        findView();
        setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    public void addContent(int viewId) {
        inflate(getContext(), viewId, content);
    }

    private void findView() {
//        halfBlackTopView = findViewById(R.id.content_top_half_balck_view);
        errorImage = (ImageView) findViewById(R.id.net_error_flag);
        content = (FrameLayout) findViewById(R.id.content);
        loading = (StudentLoadingLayout)findViewById(R.id.loading);
        error = findViewById(R.id.error);

        errorChildLayout=findViewById(R.id.error_child_view);
        refreshBtn = (TextView) findViewById(R.id.try_again);
        refreshBtn.setOnClickListener(this);
        errorTxt1 = (TextView) findViewById(R.id.errorTxt1);
        errorTxt2 = (TextView) findViewById(R.id.errorTxt2);
    }

    /**
     * 设置装载内容布局容器背景颜色
     * @param color
     */
    public void setContentBackground(int color){
        if(content==null){
            return;
        }
        content.setBackgroundColor(color);
    }

    /**
     * 获取错误提示页面
     * @return
     */
    public View getErrorLayoutView(){
        return errorChildLayout;
    }

//    public View getErrorLayoutViewBgShadow(){
//        return halfBlackTopView;
//    }


    /**
     * 设置装载布局是否可见
     * @param visible
     */
    public void setContentVisible(int visible){
        if(content==null){
            return;
        }
        content.setVisibility(visible);
    }

    public void loading(boolean isShowContent) {

        loading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        error.setVisibility(GONE);
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void loadingWithNoMargin(boolean isShowContent) {

        loading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        error.setVisibility(GONE);
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }




    public void finish() {
        loading.setVisibility(GONE);
        error.setVisibility(GONE);
 //      halfBlackTopView.setVisibility(GONE);
        content.setVisibility(VISIBLE);
    }

    public void finishWithNoContent(){
        loading.setVisibility(GONE);
        error.setVisibility(GONE);
        error.setVisibility(GONE);
  //      halfBlackTopView.setVisibility(GONE);
        content.setVisibility(GONE);
    }

    public void netError(boolean isShowContent) {
        loading.setVisibility(GONE);
  //      halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        if (!errorImage.isShown()) {
            errorImage.setVisibility(VISIBLE);
        }
        if (!errorTxt1.isShown()) {
            errorTxt1.setVisibility(VISIBLE);
        }
        errorTxt2.setVisibility(GONE);
        if (!refreshBtn.isShown()) {
            refreshBtn.setVisibility(VISIBLE);
        }
        errorTxt1.setText(R.string.public_loading_net_errtxt);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void showErrorMessage(String errMsg) {
        loading.setVisibility(GONE);
        errorImage.setVisibility(GONE);
    //    halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        error.setBackgroundColor(Color.TRANSPARENT);
        errorTxt2.setVisibility(GONE);
        errorTxt1.setText(errMsg);
        errorTxt1.setTextColor(getResources().getColor(R.color.color_ffc8c7cc));
        content.setVisibility(VISIBLE);
        refreshBtn.setVisibility(VISIBLE);
    }

    public void dataError(int errmsg, boolean isShowContent) {
        loading.setVisibility(GONE);
     //   halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        errorTxt1.setText(errmsg);
        errorImage.setVisibility(VISIBLE);
        errorImage.setImageResource(R.drawable.data_error);
        errorTxt2.setVisibility(GONE);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void dataError(String errmsg, boolean isShowContent) {
        LogInfo.log("lee", "parentDataError");
        loading.setVisibility(GONE);
   //     halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        errorImage.setVisibility(VISIBLE);
        errorImage.setImageResource(R.drawable.data_error);
        errorTxt2.setVisibility(GONE);
        errorTxt1.setText(errmsg);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }
    //TODO 此方法容易引起误解  数据加载失败  提示 网络失败？
    public void dataError(boolean isShowContent) {
        loading.setVisibility(GONE);
    //    halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        errorTxt2.setVisibility(GONE);
        errorTxt1.setText(R.string.public_loading_net_errtxt);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

    public void dataNull(boolean isShowContent) {
        loading.setVisibility(GONE);
    //    halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        errorTxt2.setVisibility(GONE);
        errorImage.setVisibility(VISIBLE);
        errorImage.setImageResource(R.drawable.no_data_icon);
        errorTxt1.setText(R.string.public_loading_data_null);
        refreshBtn.setVisibility(GONE);
        error.setBackgroundColor(isShowContent ?
                Color.TRANSPARENT :
                getResources().getColor(R.color.color_white));
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }
    public void dataNull(int msgRes) {
        loading.setVisibility(GONE);
       // halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        errorTxt2.setVisibility(GONE);
        errorImage.setVisibility(VISIBLE);
        errorImage.setImageResource(R.drawable.no_data_icon);
        refreshBtn.setVisibility(GONE);
        errorTxt1.setText(msgRes);
        error.setBackgroundColor(Color.TRANSPARENT);
        content.setVisibility(VISIBLE);
    }
    public void dataNull(String msg) {
        loading.setVisibility(GONE);
     //   halfBlackTopView.setVisibility(VISIBLE);
        error.setVisibility(VISIBLE);
        errorTxt2.setVisibility(GONE);
        errorImage.setVisibility(VISIBLE);
        errorImage.setImageResource(R.drawable.no_data_icon);
        refreshBtn.setVisibility(GONE);
        errorTxt1.setText(msg);
        error.setBackgroundColor(Color.TRANSPARENT);
        content.setVisibility(VISIBLE);
    }
    public void dataNull(int isChapterSection, int eImageRes) {
        loading.setVisibility(GONE);
     //   halfBlackTopView.setVisibility(VISIBLE);
//        layoutParams.topMargin = 0;
//        halfBlackTopView.setLayoutParams(layoutParams);
        error.setVisibility(VISIBLE);
        errorTxt2.setVisibility(VISIBLE);
        errorImage.setVisibility(VISIBLE);
        errorImage.setImageResource(eImageRes);
        refreshBtn.setVisibility(GONE);
        errorTxt1.setText(Html.fromHtml("<big><strong>"+context.getString(R.string.public_has_no_question_tag)+"</strong></big>"));
        if(isChapterSection == 1){
            errorTxt2.setText(Html.fromHtml(context.getString(R.string
                    .public_has_no_question_switch)+
                    context.getString
                    (R.string.public_has_no_question_chapter) +
                    context.getString(R.string
                    .public_has_no_question_tag_1)));
        } else {
            errorTxt2.setText(Html.fromHtml(context.getString(R.string.public_has_no_question_switch)+
                    context.getString(R.string.public_has_no_question_test)+
                    context.getString(R.string
                    .public_has_no_question_tag_1)));
        }
        error.setBackgroundColor(Color.TRANSPARENT);
        content.setVisibility(VISIBLE);
    }
    @Override
    public void onClick(View v) {
        if(v==refreshBtn){
            if (mRefreshData != null) {
                mRefreshData.refreshData();
            }
        }
    }

    public interface RefreshData {
        void refreshData();
    }
}
