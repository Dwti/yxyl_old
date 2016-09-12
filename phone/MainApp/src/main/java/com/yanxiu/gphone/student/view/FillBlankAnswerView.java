package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2016/8/23.
 */
public class FillBlankAnswerView extends LinearLayout {
    private Context mContext;
    private List<View> itemViewList = new ArrayList<>();
    public FillBlankAnswerView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public FillBlankAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public FillBlankAnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        this.setOrientation(VERTICAL);
    }

    public void setAnswerTemplate(int count) {
        removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < count; i++) {
            final View itemView = inflater.inflate(R.layout.fill_blank_answer_item, this, false);
            final ImageView iv = (ImageView) itemView.findViewById(R.id.iv_middle_icon);
//            BitmapDrawable drawable= (BitmapDrawable) iv.getBackground();
//            drawable.setTileModeXY(Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
//            iv.setImageDrawable(drawable);
            TextView tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_num.setText("(" + (i + 1) + ")");
            this.addView(itemView);

            //由于5.0以下的 .9图平铺的时候监听不到填充改变，所以设置高度为屏幕的高度，并重新layout
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        itemView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        setLineHeight(iv);

                    }
                });
            }

        }
    }

    public void setLineHeight(ImageView iv) {
        FrameLayout.LayoutParams ivLp = (FrameLayout.LayoutParams) iv.getLayoutParams();
        ivLp.height = CommonCoreUtil.getScreenHeight();
        iv.requestLayout();
    }

    public ArrayList<String> getAnswerList() {
        ArrayList<String> listAnswer = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            EditText et_answer = (EditText) itemView.findViewById(R.id.et_answer);
            String answer = et_answer.getText().toString();
//            if (!TextUtils.isEmpty(answer))
                listAnswer.add(answer.trim());
        }
        return listAnswer;
    }

    public void setAnswerList(List<String> list) {
        setAnswerList(list, true);
    }

    public void setAnswerList(List<String> list, boolean editable) {
        if (list == null || list.isEmpty())
            return;
        int count;
        if (list.size() < getChildCount()) {
            count = list.size();
        } else {
            count = getChildCount();
        }
        for (int i = 0; i < count; i++) {
            View itemView = getChildAt(i);
            EditText et_answer = (EditText) itemView.findViewById(R.id.et_answer);
            et_answer.setText(list.get(i));
            et_answer.setEnabled(editable);
        }
    }

}
