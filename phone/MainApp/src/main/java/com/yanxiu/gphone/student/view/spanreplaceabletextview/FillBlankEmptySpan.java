package com.yanxiu.gphone.student.view.spanreplaceabletextview;

import android.graphics.Color;

import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.Utils;

/**
 * Created by sp on 17-3-3.
 */

public class FillBlankEmptySpan extends EmptySpan {

    @Override
    protected int width() {
        return Util.dipToPx(80);
    }

    @Override
    protected int height() {
        return Util.dipToPx(40);
    }

    @Override
    protected int color() {
        return Color.TRANSPARENT;
    }
}
