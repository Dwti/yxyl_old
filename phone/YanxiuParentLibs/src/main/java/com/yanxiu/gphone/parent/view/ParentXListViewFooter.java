package com.yanxiu.gphone.parent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.common.core.view.xlistview.XListViewFooter;
import com.yanxiu.gphone.parent.R;

/**
 * Created by lidongming on 16/4/1.
 */
public class ParentXListViewFooter extends XListViewFooter implements XListViewFooter.InterceptView{


    public ParentXListViewFooter(Context context) {
        super(context);
    }

    public ParentXListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public XListViewFooter.InterceptView getInterceptView() {
        return this;
    }

    @Override
    public void onIntercept() {
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.xlistview_footer_progressbar);
        mProgressBar.setIndeterminateDrawable(ParentXListViewFooter.this.getResources().getDrawable(R.anim.parent_xlistview_progress));
    }
}
