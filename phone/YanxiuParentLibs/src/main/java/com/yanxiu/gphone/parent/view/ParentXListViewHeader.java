package com.yanxiu.gphone.parent.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.view.xlistview.XListViewHeaderNew;
import com.yanxiu.gphone.parent.R;

/**
 * Created by lidongming on 16/4/1.
 */
public class ParentXListViewHeader extends XListViewHeaderNew implements XListViewHeaderNew.InterceptView{

    public ParentXListViewHeader(Context context) {
        super(context);
        setInterceptView(this);
    }

    public ParentXListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInterceptView(this);
    }


    @Override
    public InterceptView getInterceptView() {
        return this;
    }

    @Override
    public void onIntercept() {
        RelativeLayout.LayoutParams lp;
        LinearLayout.LayoutParams llp;

        RelativeLayout contetnView = (RelativeLayout) findViewById(R.id.xlistview_header_content);
        llp = (LayoutParams) contetnView.getLayoutParams();
        llp.height = CommonCoreUtil.dipToPx(25);
        contetnView.setLayoutParams(llp);

        ImageView imageView = (ImageView) findViewById(R.id.xlistview_header_progressbar);
        imageView.setImageResource(R.drawable.listview_loading);
        lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.width = CommonCoreUtil.dipToPx(25);
        lp.height = CommonCoreUtil.dipToPx(25);
        LogInfo.log("geny", "width--------height-----" + CommonCoreUtil.dipToPx(180));
        imageView.setLayoutParams(lp);

        imageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        imageView.setImageResource(R.drawable.listview_loading);
        lp = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        lp.width = CommonCoreUtil.dipToPx(25);
        lp.height = CommonCoreUtil.dipToPx(25);
        imageView.setLayoutParams(lp);
    }
}
