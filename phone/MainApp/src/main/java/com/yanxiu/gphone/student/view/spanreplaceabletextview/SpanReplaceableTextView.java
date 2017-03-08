package com.yanxiu.gphone.student.view.spanreplaceabletextview;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sp on 17-2-23.
 */

public abstract class SpanReplaceableTextView<T extends View> extends FrameLayout {
    protected MyTextView mTextView;
    protected RelativeLayout mRelativeLayout;
    protected Context mContext;
    protected Spanned spanned;
    protected EmptySpan[] tagSpans;
    private LinkedHashMap<EmptySpan,T> linkedHashMap;
    protected boolean isReplaceCompleted = false;
    public SpanReplaceableTextView(Context context) {
        super(context);
        init(context);
    }

    public SpanReplaceableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpanReplaceableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        linkedHashMap = new LinkedHashMap<>();
        View view = LayoutInflater.from(context).inflate(R.layout.replaceable_text_view,this,true);
        mTextView = (MyTextView) view.findViewById(R.id.textView);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        mTextView.setTextColor(Color.BLACK);
//        mTextView.setLineSpacing(Util.px2dip(50), 1);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        mTextView.setOnDrawFinishedListener(new TextViewOnDrawFinishedListener());
    }

    public void setText(String text){
        spanned = Html.fromHtml(text,getImageGetter(), getTagHandler());
        tagSpans = spanned.getSpans(0,spanned.length(),getTagHandler().getSpanType());
        for(EmptySpan span: tagSpans){
            span.lineHeight = mTextView.getLineHeight();
        }
        mTextView.setText(spanned, TextView.BufferType.SPANNABLE);
    }

    protected void replaceSpanWithView() {
        if(spanned == null){
            isReplaceCompleted = true;
            return;
        }
        for (EmptySpan s : tagSpans) {

            int start = spanned.getSpanStart(s);
            Layout layout = mTextView.getLayout();
            int line = layout.getLineForOffset(start);
            int topPadding = mTextView.getCompoundPaddingTop();
            int leftMargin = (int) layout.getPrimaryHorizontal(start);
            int descent = layout.getLineDescent(line);
            int base = layout.getLineBaseline(line);
            int spanTop = base + descent - s.height();
            int topMargin = spanTop + topPadding;

            T view = linkedHashMap.get(s);
            if(view == null){
                view = getReplaceView();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(s.width(), s.height());
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                mRelativeLayout.addView(view, params);
                linkedHashMap.put(s,view);
            }else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                view.setLayoutParams(params);
            }
        }
        isReplaceCompleted = true;
    }

    public List<T> getReplacement(){
        List<T> list = new ArrayList<>();
        Set<Map.Entry<EmptySpan,T>> entries =  linkedHashMap.entrySet();
        Iterator<Map.Entry<EmptySpan,T>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<EmptySpan,T> entry = iterator.next();
            list.add(entry.getValue());
        }
        return list;
    }
    public void removeAllReplacementView(){
        if(mRelativeLayout.getChildCount()>0){
            mRelativeLayout.removeAllViews();
            linkedHashMap.clear();
        }
    }
    protected Html.ImageGetter getImageGetter(){
        return new HtmlImageGetter(mContext, mTextView);
    }
    protected abstract T getReplaceView();

    protected abstract ReplaceTagHandler getTagHandler();

    private class TextViewOnDrawFinishedListener implements MyTextView.OnDrawFinishedListener{

        @Override
        public void onDrawFinished() {
            replaceSpanWithView();
        }
    }
}
