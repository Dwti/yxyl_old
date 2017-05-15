package com.yanxiu.gphone.studentold.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.adapter.SubjectVersionSortAdapter;
import com.yanxiu.gphone.studentold.bean.SubjectEditionBean;
import com.yanxiu.gphone.studentold.utils.Util;

import java.util.List;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SortPopUpWindow {
    /**
     * popupWindow
     */
    private SortPopupWindow popupWindow;

    private Activity mContext;
    private TranslateAnimation animDown;

    private TranslateAnimation animUp;

    /**
     * 确定触发该dialog的view组件，也是创建popupwindow必须的参数
     */
    private View parentView;

    private View popupView;

    private ImageView rightArrow;
    private View rlRightBgView;

    private ListView mListView;
    private SubjectVersionSortAdapter mAdapter;

    public SortPopUpWindow(Activity context) {
        this.mContext = context;
    }


    public void create(String menuId, View parentView) {
        this.parentView = parentView;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        rightArrow = (ImageView) parentView.findViewById(R.id.iv_right);
        rlRightBgView = parentView.findViewById(R.id.rl_right_bg_view);
        popupView = inflater.inflate(R.layout.activity_sort, null);

        mListView = (ListView) popupView.findViewById(R.id.lv_subject_version);
//        Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable
//                .dash_line_6b6b9494);
//        mListView.setDivider(new BitmapDrawable(mBitmap));
        initAnim();
        // 生成PopupWindow对象
        popupWindow = new SortPopupWindow(popupView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true, onSortDismissListener);
        // 设置PopupWindow对象需要属性参数
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        // 设置popupwindow以外的区域是否可以触摸
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow阴影部分的点击事件
        parentView.setOnClickListener(new ShowPopupWindowListener());
        popupView.setOnClickListener(new PopupWindowListener());
    }

    public void setOnClickListener(){
        parentView.setOnClickListener(new ShowPopupWindowListener());
    }

    private void animOpen(final ImageView arrow) {
        Animation ani = AnimationUtils.loadAnimation(mContext,
                R.anim.fenlei_rotate);
        ani.setFillAfter(true);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setImageResource(R.drawable.intelli_exe_up_btn);
                arrow.clearAnimation();
                rlRightBgView.setBackgroundResource(R.drawable.rl_right_bg_sel);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

        });
        arrow.startAnimation(ani);
    }

    public SubjectVersionSortAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(SubjectVersionSortAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public ListView getmListView() {
        return mListView;
    }

    public void setmListView(ListView mListView) {
        this.mListView = mListView;
    }

    private void animClose(final ImageView arrow) {
//        Util.showToast("animClose");
        Animation ani = AnimationUtils.loadAnimation(mContext, R.anim.fenlei_rotate_back);
        ani.setFillAfter(true);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrow.setImageResource(R.drawable.intelli_exe_down_btn);
                arrow.clearAnimation();
                rlRightBgView.setBackgroundResource(R.drawable.rl_right_bg_nor);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        arrow.startAnimation(ani);
    }


    private void initAnim() {
        long ANIMDURATION = 300l;
        animUp = new TranslateAnimation(0, 0, 0, -CommonCoreUtil.getScreenHeight());
        animUp.setDuration(ANIMDURATION);
        animUp.setInterpolator(new AccelerateInterpolator());
        animUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                popupWindow.superDismiss();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }

    OnSortDismissListener onSortDismissListener = new OnSortDismissListener() {

        @Override
        public void onDismiss() {
            startAnimUp();
            animClose(rightArrow);
        }
    };

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    public void closePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void startAnimUp() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupView.startAnimation(animUp);
//            mListView.startAnimation(animUp);
        }
    }

    public class SortPopupWindow extends PopupWindow {

        OnSortDismissListener linstener;

        boolean isAnimFinish = true;

        public SortPopupWindow(View contentView, int width, int height, boolean focusable,
                               OnSortDismissListener linstener) {
            super(contentView, height, width, focusable);
            this.linstener = linstener;
        }

        @Override
        public void dismiss() {
            if (isAnimFinish) {
                linstener.onDismiss();
                isAnimFinish = false;
            }
        }

        public void superDismiss() {
            isAnimFinish = true;
            super.dismiss();
        }

    }
    private class PopupWindowListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            closePopup();
        }
    }

    interface OnSortDismissListener {
        void onDismiss();
    }

    /**
     * 给parentView设置onclicklistener事件，用于显示popupwindow
     *
     * @author kingzhang
     */
    private class ShowPopupWindowListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(mAdapter != null && mAdapter.getCount() > 0) {
                if (popupWindow.isShowing()) {
                    closePopup();
                } else {
                    animOpen(rightArrow);
                    showPopWindowForView();
                }
            } else {
               Util.showToast(R.string.select_location_data_error);
            }
        }
    }


    private void showPopWindowForView() {
        TranslateAnimation animTopIn = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -0.5f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animTopIn.setDuration(300);
        animTopIn.setFillAfter(true);
//        mListView.setAnimation(animTopIn);
        popupView.setAnimation(animTopIn);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.showAsDropDown(parentView, 0, CommonCoreUtil.dipToPx(2));
    }

    public void setDataSource(List<SubjectEditionBean.DataEntity.ChildrenEntity> mSortBeans){
        mAdapter = new SubjectVersionSortAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mAdapter.setList(mSortBeans);
//        mAdapter.setmListener(new SubjectVersionAdapter.SelectPositionEntityListener() {
//            @Override
//            public void onSelectionPosition(SubjectEditionBean.DataEntity.ChildrenEntity entity) {
//
//            }
////            @Override
////            public void onSelectionPosition(SubjectVersionBean.DataEntity entity) {
////                Util.showToast("保存" + entity.getName() + "--" + entity.getData());
////            }
//        });
//        mAdapter.setListView(mListView);
    }


}
