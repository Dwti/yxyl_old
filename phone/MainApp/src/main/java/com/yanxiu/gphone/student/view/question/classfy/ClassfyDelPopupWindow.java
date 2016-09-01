package com.yanxiu.gphone.student.view.question.classfy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.common.core.utils.BasePopupWindow;
import com.yanxiu.gphone.student.R;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ClassfyDelPopupWindow extends BasePopupWindow {
    public ClassfyDelPopupWindow(Context mContext) {
        super(mContext);
    }

    @Override
    protected void initView(Context mContext) {
        View view=View.inflate(mContext, R.layout.pic_sel_pop_view,null);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);
    }

    @Override
    public void loadingData() {

    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View view) {

    }
}
