package com.yanxiu.gphone.student.view.spanreplaceabletextview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.MyEdittext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-2-24.
 */

public class FillBlankTextView extends SpanReplaceableTextView<EditText> {
    public FillBlankTextView(Context context) {
        super(context);
    }

    public FillBlankTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillBlankTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected EditText getReplaceView() {
        EditText editText = new MyEdittext(getContext());
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        editText.setTextColor(Color.BLACK);
        editText.setSingleLine();
        editText.setPadding(10,5,15,5);
//        setEditTextCusrorDrawable(editText);
        return editText;
    }

    @Override
    protected ReplaceTagHandler getTagHandler() {
        return new FillBlankTagHandler();
    }

    private void initFilledContent(List<String> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (data == null || data.size() == 0)
            return;
        List<EditText> editTexts = getReplacement();
        for (int i = 0; i < editTexts.size(); i++) {
            if (data.size() == i)
                break;
            editTexts.get(i).setText(data.get(i));
        }
    }

    public void setFilledContent(final List<String> data) {
        if (isReplaceCompleted)
            initFilledContent(data);
        else {
            post(new Runnable() {
                @Override
                public void run() {
                    setFilledContent(data);
                }
            });
        }
    }

    public List<String> getFilledContent() {
        List<String> list = new ArrayList<>();
        List<EditText> editTexts = getReplacement();
        for (EditText et : editTexts) {
            list.add(et.getText().toString());
        }
        return list;
    }

    public void setEditable(boolean editable) {
        List<EditText> editTexts = getReplacement();
        for (EditText editText : editTexts) {
            editText.setEnabled(editable);
        }
    }

    private void setEditTextCusrorDrawable(EditText editText) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field fCursorDrawableRes = TextView.class.getDeclaredField(
                    "mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            @DrawableRes int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[1] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
            int color = getResources().getColor(R.color.color_black);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_OVER);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_OVER);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }
}
