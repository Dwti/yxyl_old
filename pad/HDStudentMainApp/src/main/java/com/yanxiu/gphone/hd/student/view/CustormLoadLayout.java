package com.yanxiu.gphone.hd.student.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.hd.student.R;

/**
 * 定制化的错误界面
 * Created by Administrator on 2015/12/29.
 */
public class CustormLoadLayout extends PublicLoadLayout {
    public CustormLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        errorImage.setImageResource(R.drawable.ranking_net_error);
        getErrorLayoutView().setBackgroundResource(0);
        errorTxt1.setTextColor(getResources().getColor(R.color.color_d1a52b));
        errorTxt1.setTextSize(14);
        errorTxt1.setShadowLayer(0, 0, 0, getResources().getColor(R.color.trans));
        refreshBtn.setTextSize(17);
        refreshBtn.setBackgroundResource(R.drawable.rank_refresh_btn_selector);
        refreshBtn.setTextColor(getResources().getColor(R.color.color_d1a52b));
        refreshBtn.setTypeface(Typeface.DEFAULT_BOLD);
        refreshBtn.setShadowLayer(0, 0, 0, getResources().getColor(R.color.trans));
    }

    public CustormLoadLayout(Context context) {
        super(context);
        initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void netError(boolean isShowContent) {

        loading.setVisibility(GONE);
       // halfBlackTopView.setVisibility(GONE);
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
        error.setBackgroundColor(Color.TRANSPARENT);
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }


    public void dataError(String errmsg, boolean isShowContent) {
        super.dataError(errmsg, isShowContent);
        loading.setVisibility(GONE);
        error.setVisibility(VISIBLE);
        errorTxt1.setText(errmsg);
        errorTxt2.setVisibility(GONE);
        error.setBackgroundColor(Color.TRANSPARENT);
        content.setVisibility(isShowContent ? VISIBLE : GONE);
    }

}
