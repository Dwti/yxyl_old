package com.yanxiu.gphone.parent.utils;

import android.content.Context;
import android.view.ViewGroup;

import com.yanxiu.gphone.parent.view.PublicLoadLayout;


/**
 * Created by Administrator on 2016/1/14.
 */
public class PublicLoadUtils {
    /**
     * 创建一个公共的加载布局
     */
    public static PublicLoadLayout createPage(Context context, int layoutId) {
        PublicLoadLayout rootView = new PublicLoadLayout(context);
        rootView.addContent(layoutId, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }

}
