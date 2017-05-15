package com.yanxiu.gphone.studentold.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class Connectlines extends View {

    private static final int COLOR_DEFULT=Color.parseColor("#ccc4a3");
    private static final int COLOR_RIGHT=Color.parseColor("#00cccc");
    private static final int COLOR_FALSE=Color.parseColor("#ff99bb");
    private Context context;
    private List<MyLinesBean> list=new ArrayList<MyLinesBean>();
    private Paint paint;
    private int width;
    private int padding=2;
    private boolean isClick=true;

    public Connectlines(Context context) {
        this(context,null);
    }

    public Connectlines(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Connectlines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
    }

    public void setDatas(List<ConnectLinesLinearLayout.BaseBean> datas){
        list.clear();
        for (ConnectLinesLinearLayout.BaseBean bean:datas){
            if (bean.getLocation().equals(ConnectLinesLinearLayout.LOCATION_LEFT)){
                if (bean.isSelect()){
                    MyLinesBean linesBean=new MyLinesBean();
                    linesBean.setY_left(bean.getY());
                    int y_right=getRight_Y(datas,bean.getSelect_id());
                    linesBean.setY_right(y_right);
                    linesBean.setRight(bean.getIsRight());
                    list.add(linesBean);
                }
            }
        }
        invalidate();
    }

    private int getRight_Y(List<ConnectLinesLinearLayout.BaseBean> datas, int id){
        for (ConnectLinesLinearLayout.BaseBean bean:datas){
            if (bean.getLocation().equals(ConnectLinesLinearLayout.LOCATION_RIGHT)){
                if (bean.getId()==id){
                    return bean.getY();
                }
            }
        }
        return 0;
    }

    public void setIsClick(boolean isClick){
        this.isClick=isClick;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width=right-left;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        for (MyLinesBean bean:list){
            if (isClick) {
                paint.setColor(COLOR_DEFULT);
            }else {
                if (bean.isRight()){
                    paint.setColor(COLOR_RIGHT);
                }else {
                    paint.setColor(COLOR_FALSE);
                }
            }
            canvas.drawLine(0+padding, bean.getY_left(),width-padding,bean.getY_right(),paint);
        }
    }


    public class MyLinesBean{

        private int y_left;
        private int y_right;
        private boolean IsRight=false;

        public MyLinesBean() {
        }

        public boolean isRight() {
            return IsRight;
        }

        public void setRight(boolean right) {
            IsRight = right;
        }

        public int getY_left() {
            return y_left;
        }

        public void setY_left(int y_left) {
            this.y_left = y_left;
        }

        public int getY_right() {
            return y_right;
        }

        public void setY_right(int y_right) {
            this.y_right = y_right;
        }
    }
}
