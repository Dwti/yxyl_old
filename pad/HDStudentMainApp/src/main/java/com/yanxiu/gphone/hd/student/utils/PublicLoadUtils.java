package com.yanxiu.gphone.hd.student.utils;

import android.content.Context;

import com.yanxiu.gphone.hd.student.view.CustormLoadLayout;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;

/**
 * Created by Administrator on 2016/1/14.
 */
public class PublicLoadUtils {
    /**
     * 创建一个公共的加载布局
     */
    public static PublicLoadLayout
    createPage(Context context, int layoutId) {

        PublicLoadLayout rootView = new PublicLoadLayout(context);
        rootView.addContent(layoutId);

        return rootView;
    }

    /**
     *
     * 加载一个定制化的加载布局
     * @param context
     * @param layoutId
     * @return
     */
    public static CustormLoadLayout createCustormPage(Context context, int layoutId) {

        CustormLoadLayout rootView = new CustormLoadLayout(context);
        rootView.addContent(layoutId);

        return rootView;
    }

}
