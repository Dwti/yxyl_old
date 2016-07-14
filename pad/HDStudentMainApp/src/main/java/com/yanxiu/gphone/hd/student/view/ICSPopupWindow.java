package com.yanxiu.gphone.hd.student.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lidm on 2015/10/17 00:35
 * 描述： 新增菜单样式
 */
public class ICSPopupWindow {

    protected ArrayList<String> itemList;
    protected Context context;
    protected PopupWindow popupWindow ;

    protected List<String> dataList;
    protected String[] menuTxt;
    protected int[] iconList;
    protected Class[] clazzList;// 跳转的activity

    public ICSPopupWindow(final Context context) {
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.popup_ics_layout, null);

//        listView.setAdapter(new PopAdapter());

        popupWindow = new PopupWindow(view, FrameLayout.LayoutParams.WRAP_CONTENT, context.getResources()
                .getDimensionPixelSize(R
                        .dimen.dimen_60));

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

    }


    //下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent) {
        LogInfo.log("geny", "popupWindow.getWidth()==" + popupWindow.getWidth());
        LogInfo.log("geny", "parent.getWidth()==" + parent.getWidth());
        popupWindow.showAsDropDown(parent, -(popupWindow.getWidth() - parent.getWidth() + context
                .getResources().getDimensionPixelSize(R.dimen.dimen_10)), -context
                .getResources().getDimensionPixelSize(R.dimen.dimen_10));
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
    }

    //隐藏菜单
    public void dismiss() {
        popupWindow.dismiss();
    }


}
