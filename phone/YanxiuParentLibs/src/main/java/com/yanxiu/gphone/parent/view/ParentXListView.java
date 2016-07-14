package com.yanxiu.gphone.parent.view;

import android.content.Context;
import android.util.AttributeSet;

import com.common.core.view.xlistview.XListView;
import com.common.core.view.xlistview.XListViewFooter;
import com.common.core.view.xlistview.XListViewHeaderNew;

/**
 * Created by lidongming on 16/4/1.
 */
public class ParentXListView extends XListView{
    public ParentXListView(Context context) {
        super(context);
    }

    public ParentXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentXListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected XListViewHeaderNew getHeaderView(Context context) {
        return new ParentXListViewHeader(context);
    }


    @Override
    protected XListViewFooter getFootView(Context context) {
        return new ParentXListViewFooter(context);
    }
}
