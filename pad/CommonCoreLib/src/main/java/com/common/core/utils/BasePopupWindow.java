package com.common.core.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


@SuppressWarnings("ALL")
public abstract class BasePopupWindow<T extends Object> implements View.OnClickListener {

    protected final PopupWindow pop;
    protected Context mContext;
    private OnTouchEventListener onTouchEventListener;
    protected OnDissmissListener onDissmissListener;
    protected ItemClickListener onItemClickListener;
    protected  T t;
    public BasePopupWindow(Context mContext) {
        this.mContext = mContext;
        this.pop = new PopupWindow(mContext);
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        pop.setWidth(CommonCoreUtil.getScreenWidth());
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        //Added in API level 3
        //Set a callback for all touch events being dispatched to the popup window.
        this.pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (onTouchEventListener != null) {
                    onTouchEventListener.onTouchEventListener(view, motionEvent);
                }
                return false;
            }
        });
        this.pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(onDissmissListener!=null){
                    onDissmissListener.onDismiss();
                }
            }
        });
        this.initView(mContext);
    }
 
    public void showAsDropDown(View anchor) {
        if (!isShowing()) {
            this.pop.showAsDropDown(anchor);
        } else {
            this.pop.dismiss();
        }
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (!isShowing()) {
            this.pop.showAsDropDown(anchor, xoff, yoff);
        } else {
            this.pop.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (!isShowing()) {
            this.pop.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            this.pop.dismiss();
        }

    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (!isShowing()) {
            this.pop.showAtLocation(parent, gravity, x, y);
        } else {
            this.pop.dismiss();
        }
    }

    public void dismiss() {
        if (isShowing()) {
            this.pop.dismiss();
        }
        destoryData();
    }

    public boolean isShowing(){
        return pop.isShowing();
    }

    public void setOnTouchListener(OnTouchEventListener onTouchEventListener){
        this.onTouchEventListener=onTouchEventListener;
    }

    public void setOnDissmissListener(OnDissmissListener onDissmissListener){
        this.onDissmissListener=onDissmissListener;
    }

    public void setOnItemClickListener (ItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    public interface OnDissmissListener{
        void onDismiss();
    }
    public interface ItemClickListener{
        public static int ONE=0;
        public static int TWO=1;
        public static int THR=2;
        public static int FOUR=3;
        void onItemClick(int position);
    }
    //监听POPupWindow是否被Touch
    public interface OnTouchEventListener{
        void onTouchEventListener(View v, MotionEvent motionEvent);
    }

    protected abstract void initView(Context mContext );
    //
    public abstract void loadingData();
    //销毁相关数据
    protected abstract void destoryData();

    public  void setParams(T t){
        this.t=t;
    };

    public  T getParams(){
        return this.t;
    };


}
