package com.yanxiu.gphone.student.view.spanreplaceabletextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

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
        EditText editText = new EditText(getContext());
        editText.setSingleLine();
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
}
