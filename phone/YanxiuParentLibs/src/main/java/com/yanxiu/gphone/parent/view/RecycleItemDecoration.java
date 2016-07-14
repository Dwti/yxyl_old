package com.yanxiu.gphone.parent.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lee on 16-3-25.
 */
public class RecycleItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecycleItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
