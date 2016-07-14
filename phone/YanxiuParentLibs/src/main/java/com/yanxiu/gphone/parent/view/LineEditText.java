package com.yanxiu.gphone.parent.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;

/**
 * Created by hai8108 on 15/11/23.
 */
public class LineEditText extends EditText implements TextWatcher,
        View.OnFocusChangeListener {
    private Paint mPaint;
    private int bottomLineColor;
    private int textColor;
    public static final int STATUS_FOCUSED = 1;
    public static final int STATUS_UNFOCUSED = 2;
    public static final int STATUS_ERROR = 3;
    private int status = 2;
    private Drawable del_btn;
    private int focusedDrawableId = R.drawable.phone_icon;// 默认的
    private int rightDrawableId = R.drawable.clear;// 默认的
    private boolean isNeedDrawableLeft = false; //是否需要左边显示图片
    private boolean isNeedDrawableRight = true; //是否需要右边显示图片
    private boolean isNeedDrawableBottomLine = true; //是否需要底部线条
    Drawable left = null;
    private Context mContext;
    /**
     * 是否获取焦点，默认没有焦点
     */
    private boolean hasFocus = false;
    /**
     * 手指抬起时的X坐标
     */
    private int xUp = 0;

    public LineEditText (Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public LineEditText (Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public LineEditText (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs);
    }

    public void setBottomLineColor (@ColorRes int resColorId) {
        bottomLineColor = mContext.getResources()
                .getColor(resColorId);
        invalidate();
    }

    public void setTypeFace (String typePath) {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
                typePath);
        setTypeface(tf);
    }

    /**
     * 2014/7/31
     *
     * @author Aimee.ZHANGßßßß
     */
    private void init (AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.line_edit_text);
            focusedDrawableId = a.getResourceId(
                    R.styleable.line_edit_text_drawableleftfocus, R.drawable.phone_icon);
            rightDrawableId = a.getResourceId(
                    R.styleable.line_edit_text_drawablerightfocus, R.drawable.clear);
            isNeedDrawableLeft = a.getBoolean(R.styleable.line_edit_text_isneeddrawableleft, false);
            isNeedDrawableRight = a.getBoolean(R.styleable.line_edit_text_isneeddrawableright,
                    true);
            isNeedDrawableBottomLine = a.getBoolean(R.styleable
                    .line_edit_text_isneeddrawablebottomline, true);
            bottomLineColor = a.getColor(R.styleable.line_edit_text_bottomlinecolor, mContext
                    .getResources().getColor(R.color.color_e6e6e6_p));
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setStrokeWidth(1.0f);
        bottomLineColor = mContext.getResources().getColor(R.color.color_e6e6e6_p);
        setTypeFace(YanxiuParentConstants.METRO_STYLE);
        setStatus(status);
        del_btn = mContext.getResources().getDrawable(rightDrawableId);
        addListeners();
        setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft ? left : null, null, null, null);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        if(isNeedDrawableBottomLine) {
            mPaint.setColor(bottomLineColor);
            int x = this.getScrollX();
            int w = this.getMeasuredWidth();
            canvas.drawLine(0, this.getHeight() - 1, w + x,
                    this.getHeight() - 1, mPaint);   //画editText最下方的线
        }
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (del_btn != null && event.getAction() == MotionEvent.ACTION_UP) {
            // 获取点击时手指抬起的X坐标
            xUp = (int) event.getX();
            /*Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;*/
            // 当点击的坐标到当前输入框右侧的距离小于等于getCompoundPaddingRight()的距离时，则认为是点击了删除图标
            if (this.hasFocus && (getWidth() - xUp) <= getCompoundPaddingRight()) {
                if (!TextUtils.isEmpty(getText().toString())) {
                    setText("");
                }
            }
        } else if (del_btn != null && event.getAction() == MotionEvent.ACTION_DOWN && getText().length() != 0) {
            setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null,
                    isNeedDrawableRight?del_btn:null,
                    null);
        } else if (getText().length() != 0) {
            setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null, isNeedDrawableRight?del_btn:null, null);
        }
        return super.onTouchEvent(event);
    }

    public void setStatus (int status) {
        this.status = status;
        LogInfo.log("haitian", "setStatus isNeedDrawableLeft =" + isNeedDrawableLeft);
        boolean isEmpy = TextUtils.isEmpty(this.getEditableText().toString());
        if (status == STATUS_ERROR) {
            try {
                left = getResources().getDrawable(focusedDrawableId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            if(isEmpy) {
//                setColor(mContext.getResources().getColor(R.color.color_cccccc_p));
                setBackgroundColor(mContext.getResources().getColor(R.color.color_ccffffff));
            }else {
//                setColor(mContext.getResources().getColor(R.color.color_333333_p));
                setBackgroundColor(mContext.getResources().getColor(R.color.color_white_p));
            }
        } else if (status == STATUS_FOCUSED) {
            try {
                left = getResources().getDrawable(focusedDrawableId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
//            setColor(mContext.getResources().getColor(R.color.color_333333_p));
            setBackgroundColor(mContext.getResources().getColor(R.color.color_white_p));
        } else {
            try {
                left = getResources().getDrawable(focusedDrawableId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            if(isEmpy) {
//                setColor(mContext.getResources().getColor(R.color.color_cccccc_p));
                setBackgroundColor(mContext.getResources().getColor(R.color.color_ccffffff));
            } else {
//                setColor(mContext.getResources().getColor(R.color.color_333333_p));
                setBackgroundColor(mContext.getResources().getColor(R.color.color_white_p));
            }

        }
        if (left != null && isNeedDrawableLeft) {
            if (this.hasFocus && getText().length() != 0) {
                setCompoundDrawablesWithIntrinsicBounds(left, null, isNeedDrawableRight?del_btn:null, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
            }
        }
        postInvalidate();
    }

    public void setLeftDrawable (int focusedDrawableId) {
        this.focusedDrawableId = focusedDrawableId;
        setStatus(status);
    }

    private void addListeners () {
        try {
            setOnFocusChangeListener(this);
            addTextChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFocusChanged (boolean focused, int direction,
                                   Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.hasFocus = focused;
        if (focused) {
            setStatus(STATUS_FOCUSED);
        } else {
            setStatus(STATUS_UNFOCUSED);
            setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null, null, null);
        }
    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
    }

    ;

    public void setColor (int color) {
        this.textColor = color;
        this.setTextColor(textColor);
        invalidate();
    }

    @Override
    public void afterTextChanged (Editable arg0) {
        // TODO Auto-generated method stub
        postInvalidate();
    }

    @Override
    public void beforeTextChanged (CharSequence arg0, int arg1, int arg2,
                                   int arg3) {
        // TODO Auto-generated method stub
        if (TextUtils.isEmpty(arg0)) {
            // 如果为空，则不显示删除图标
            setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null, null, null);
        } else {
            // 如果非空，则要显示删除图标
            setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null, isNeedDrawableRight?del_btn:null, null);
        }
    }

    @Override
    public void onTextChanged (CharSequence s, int start, int before, int after) {
        if (hasFocus) {
            if (TextUtils.isEmpty(s)) {
                // 如果为空，则不显示删除图标
                setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null, null, null);
            } else {
                // 如果非空，则要显示删除图标
                setCompoundDrawablesWithIntrinsicBounds(isNeedDrawableLeft?left:null, null, isNeedDrawableRight?del_btn:null, null);
            }
        }
    }

    @Override
    public void onFocusChange (View arg0, boolean arg1) {
        // TODO Auto-generated method stub
        try {
            LogInfo.log("haitian", "1---this.hasFocus =" + this.hasFocus);
            this.hasFocus = arg1;
            LogInfo.log("haitian", "2---this.hasFocus =" + this.hasFocus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
