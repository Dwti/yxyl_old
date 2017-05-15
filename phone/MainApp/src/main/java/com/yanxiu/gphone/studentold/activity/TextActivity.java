package com.yanxiu.gphone.studentold.activity;

import android.os.Bundle;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;

/**
 * Created by Administrator on 2015/7/6.
 */
public class TextActivity extends YanxiuBaseActivity{
    private YanxiuApplication application;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final DMTextView tv = new DMTextView(this);
//        this.application = (YanxiuApplication) this.getApplication();
//        String htmlSnippetCode = "如图，已知抛物线 y=ax<sup>2</sup>+bx+c（a≠0）的顶点坐标为（4，<img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iQLN4bAAABOwdsNv0023.png\">），且与y轴交于点C，与x轴交于A，B两点（点A在点B的左边），且A点坐标为（2，0）。在抛物线的对称轴 <img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iTXZhbAAABESQziLA683.png\" >上是否存在一点P，使AP+CP的值最小？若存在，则AP+CP的最小值为（&nbsp;&nbsp;&nbsp;&nbsp;）<img src=\"http://p1.qingguo.com/G1/M00/62/2A/rBACFFPfJ7iiO8gTAAALHRPDATw26.jpeg\">";
//        FillBlankImageGetterTrick imageGetter = new FillBlankImageGetterTrick(tv,this,this.application);
//        Spanned spanned = Html.fromHtml(htmlSnippetCode, imageGetter, null);
//        tv.setText(spanned);
//        setContentView(tv);

//        String htmlSnippetCode = "如图，已知抛物线 y=ax<sup>2</sup>+bx+c（a≠0）的顶点坐标为（4，<img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iQLN4bAAABOwdsNv0023.png\" height=\"42\" width=\"7\">），且与y轴交于点C，与x轴交于A，B两点（点A在点B的左边），且A点坐标为（2，0）。在抛物线的对称轴 <img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iTXZhbAAABESQziLA683.png\" height=\"21\" width=\"6\">上是否存在一点P，使AP+CP的值最小？若存在，则AP+CP的最小值为（&nbsp;&nbsp;&nbsp;&nbsp;）<img src=\"http://p1.qingguo.com/G1/M00/62/2A/rBACFFPfJ7iiO8gTAAALHRPDATw26.jpeg\" height=\"165\" width=\"193\">";
//        Spanned sp = Html.fromHtml(String.valueOf(htmlSnippetCode), new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                InputStream is = null;
//                try {
//                    is = (InputStream) new URL(source).getContent();
//                    final Drawable d = Drawable.createFromStream(is, "src");
//                    d.setBounds(0, 0, d.getIntrinsicWidth(),
//                            d.getIntrinsicHeight());
//                    is.close();
//                    LogInfo.log("geny", "source" + source);
//                    tv.post(new Runnable() {
//
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        @Override
//                        public void run() {
//                            tv.setBackground(d);
//                        }
//                    });
//                    return d;
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        }, null);
//        setContentView(tv);
//        tv.setText(sp);
//        setContentView(R.layout.fragment_read_question);
    }

}
