package com.yanxiu.gphone.student.HtmlParser;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;

import com.yanxiu.gphone.student.HtmlParser.Html.HtmlParser;
import com.yanxiu.gphone.student.HtmlParser.Html.HtmlSchema;
import com.yanxiu.gphone.student.HtmlParser.Html.HtmlToSpannedConverter;
import com.yanxiu.gphone.student.HtmlParser.Interface.ImageGetterListener;
import com.yanxiu.gphone.student.HtmlParser.Interface.ImageSpanOnclickListener;

import org.xml.sax.XMLReader;


/**
 * Created by Administrator on 2016/9/2.
 */
public class MyHtml {

    private Context context;

    public static interface TagHandler {
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader);
    }

    public static Spanned fromHtml(Context context, String source) {
        return fromHtml(context,source, null, null,null,null);
    }

    private static class HtmlParserSchema {
        private static final HtmlSchema schema = new HtmlSchema();
    }

    public static Spanned fromHtml(Context context, String source, ImageGetterListener imageGetterListener, TagHandler tagHandler, Object object, ImageSpanOnclickListener listener) {
        HtmlParser parser = new HtmlParser();
        try {
            parser.setProperty(HtmlParser.schemaProperty, HtmlParserSchema.schema);
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            throw new RuntimeException(e);
        } catch (org.xml.sax.SAXNotSupportedException e) {
            throw new RuntimeException(e);
        }

        HtmlToSpannedConverter converter = new HtmlToSpannedConverter(context,source, imageGetterListener, tagHandler, parser,object,listener);
        return converter.convert();
    }
}