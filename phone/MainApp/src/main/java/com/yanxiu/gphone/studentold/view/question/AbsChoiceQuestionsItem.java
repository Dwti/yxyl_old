package com.yanxiu.gphone.studentold.view.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2015/7/10.
 * 选择题 item
 */
public abstract class AbsChoiceQuestionsItem extends FrameLayout implements Checkable {

    public Context mCtx;
    //是否item被选中
    protected boolean isChecked = false;
    //是否这item 可以点击在题目解析中不可点击
    protected boolean isClick = true;

    protected String selectTpye;

    public AbsChoiceQuestionsItem(Context context) {
        super(context);
        mCtx = context;
        LayoutInflater.from(mCtx).inflate(getLayoutId(), this);
        initView();
    }

    public AbsChoiceQuestionsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        LayoutInflater.from(mCtx).inflate(getLayoutId(), this);
        initView();
    }

    public AbsChoiceQuestionsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        LayoutInflater.from(mCtx).inflate(getLayoutId(), this);
        initView();
    }

    /**
     * 初始化view
     */
    protected abstract void initView();

    protected abstract View getView();

    /**
     * 当前layout的id
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 设置item是否可以点击
     * @param isClick
     */
    public void setOnItemClick(boolean isClick){
        this.isClick = isClick;
    }

    /**
     * 返回当前是否可以点击
     * @return
     */
    protected abstract boolean isClick();


    protected abstract String getSelectType();


    protected abstract void setSelectType(String type);


    protected abstract void setItemContentText(String str);
}
