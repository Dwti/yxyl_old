package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.common.core.utils.imageloader.UilImageGetter;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;

/**
 * Created by JS-00 on 2016/11/23.
 */

public class TestActivity  extends Activity{
    String ss="<p><img src=\"http://scc.jsyxw.cn/image/20160816/1471340539307436.png\" title=\"1471340539307436.png\" alt=\"blob.png\"/><br/>                    <img src=\"http://scc.jsyxw.cn/image/20160816/1471340594454593.png\" title=\"1471340594454593.png\" alt=\"blob.png\"/><br/>                    <img src=\"http://scc.jsyxw.cn/image/20160816/1471340610860973.png\" title=\"1471340610860973.png\" alt=\"blob.png\"/><br/>                    <img src=\"http://scc.jsyxw.cn/image/20160816/1471340627478473.png\" title=\"1471340627478473.png\" alt=\"blob.png\"/><br/>                    <img src=\"http://scc.jsyxw.cn/image/20160816/1471340643499205.png\" title=\"1471340643499205.png\" alt=\"blob.png\"/><br/>                    <img src=\"http://scc.jsyxw.cn/image/20160816/1471340660827624.png\" title=\"1471340660827624.png\" alt=\"blob.png\"/><br/><br/></p>  \n" +
            "  \n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tests);
        TextView text= (TextView) findViewById(R.id.text);
        UilImageGetter imageGetter = new UilImageGetter(text, this, ((YanxiuApplication) TestActivity.this.getApplication()));
        Spanned spanned = Html.fromHtml(ss, imageGetter, null);
        text.setText(spanned);
    }
}
