package com.yanxiu.gphone.studentold.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.HtmlParser.Html.HtmlParser;
import com.yanxiu.gphone.studentold.HtmlParser.Html.HtmlSchema;
import com.yanxiu.gphone.studentold.HtmlParser.Interface.ImageGetterListener;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.utils.ConnectConverter;
import com.yanxiu.gphone.studentold.utils.ConnectImageGetter;
import com.yanxiu.gphone.studentold.utils.Util;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ConnectTextView extends TextView implements View.OnClickListener{

    private Context context;
    private YanxiuApplication application;
    private OnCheckListener listener;
    private ConnectLinesLinearLayout.BaseBean bean;
    private ConnectImageGetter imageGetter;

    public ConnectTextView(Context context) {
        this(context,null);
    }

    public ConnectTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConnectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        this.application = (YanxiuApplication) ((Activity)context).getApplication();
        this.setOnClickListener(this);
        this.setTextColor(context.getResources().getColor(R.color.color_333333));
        int textsize= Util.getTextPX((Activity)context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);//32
    }

    public void setBaseBean(ConnectLinesLinearLayout.BaseBean bean){
        this.bean=bean;
    }

    public void setHtmlText(String text){
        imageGetter = new ConnectImageGetter(this, context);
        Spanned spanned = Html(context,text, imageGetter);
//        mSpannedStr= SpanCenterUtils.getInstence().getSpan(mSpannedStr);
        this.setText(spanned);
    }

    public interface OnCheckListener{
        void OnCheckListener(ConnectLinesLinearLayout.BaseBean bean);
    }

    public void setCheckListener(OnCheckListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View view) {
        listener.OnCheckListener(bean);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    private static class HtmlParserSchema {
        private static final HtmlSchema schema = new HtmlSchema();
    }

    public Spanned Html(Context context, String source, ImageGetterListener imageGetterListener) {
        HtmlParser parser = new HtmlParser();
        try {
            parser.setProperty(HtmlParser.schemaProperty, HtmlParserSchema.schema);
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            throw new RuntimeException(e);
        } catch (org.xml.sax.SAXNotSupportedException e) {
            throw new RuntimeException(e);
        }

        ConnectConverter converter = new ConnectConverter(context,source, imageGetterListener, null, parser,null,null);
        return converter.convert();
    }
}
