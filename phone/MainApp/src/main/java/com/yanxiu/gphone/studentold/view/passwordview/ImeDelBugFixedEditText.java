package com.yanxiu.gphone.studentold.view.passwordview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * @see <a href="http://stackoverflow.com/questions/4886858/android-edittext-deletebackspace-key-event">Stack
 * Overflow</a>
 */
public class ImeDelBugFixedEditText extends EditText implements View.OnKeyListener, View.OnClickListener {

    private OnDelKeyEventListener delKeyEventListener;
    private OnTouchLinstener touchLinstener;
    private int tag;

    public ImeDelBugFixedEditText(Context context) {
        this(context,null);
    }

    public ImeDelBugFixedEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImeDelBugFixedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
        setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction()== KeyEvent.ACTION_UP&&keyCode== KeyEvent.KEYCODE_DEL){
            if (delKeyEventListener != null) {
                delKeyEventListener.onDeleteClick();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (touchLinstener!=null) {
            touchLinstener.onTouchListener(tag,ImeDelBugFixedEditText.this);
        }
    }

//    @Override
//    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//        return new ZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
//    }

    private class ZanyInputConnection extends InputConnectionWrapper {

        public ZanyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (delKeyEventListener != null) {
                    delKeyEventListener.onDeleteClick();
                    return true;
                }
            }
            return super.sendKeyEvent(event);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public void setDelKeyEventListener(OnDelKeyEventListener delKeyEventListener) {
        this.delKeyEventListener = delKeyEventListener;
    }

    public interface OnDelKeyEventListener {

        void onDeleteClick();

    }

    public interface OnTouchLinstener{
        void onTouchListener(int tag, ImeDelBugFixedEditText view);
    }

    public void setTouchLinstener(OnTouchLinstener touchLinstener,int tag){
        this.touchLinstener=touchLinstener;
        this.tag=tag;
    }

    private OnClickListener listener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (touchLinstener!=null) {
                touchLinstener.onTouchListener(tag,ImeDelBugFixedEditText.this);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (touchLinstener!=null&&event.getAction()==MotionEvent.ACTION_DOWN) {
//            touchLinstener.onTouchListener(tag,ImeDelBugFixedEditText.this);
//            return true;
//        }
//        if (event.getAction()==MotionEvent.)
        return super.onTouchEvent(event);
    }
}