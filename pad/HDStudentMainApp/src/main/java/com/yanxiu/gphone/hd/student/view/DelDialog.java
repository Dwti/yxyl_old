package com.yanxiu.gphone.hd.student.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.utils.RightContainerUtils;

/**
 * Created by Administrator on 2015/6/3.
 */
public class DelDialog extends Dialog{

    protected TextView cel_view;
    protected TextView del_view;
    protected TextView title_view;

    protected DelCallBack delCallBack;
    protected String title;
    protected String sure;
    protected String cel;

    protected Context mContext;

    public DelDialog(Context context, String title,String sure,String cel, DelCallBack callBack) {
        super(context, R.style.del_dialog_style);
        setOwnerActivity((Activity) context);
        this.title = title;
        this.sure = sure;
        this.cel = cel;
        this.delCallBack = callBack;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        int offsetX = CommonCoreUtil.getScreenWidth() - RightContainerUtils.getInstance().getContainerWidth();
        win.getDecorView().setPadding(offsetX, 0, 0, 0);
//        LogInfo.log("geny", "RightContainerUtils.getInstance().getContainerWidth()" +
//                RightContainerUtils.getInstance().getContainerWidth());

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width =  WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setContentView(R.layout.del_dialog);

        cel_view = (TextView)findViewById(R.id.del_dialog_cel);
        del_view = (TextView)findViewById(R.id.del_dialog_sure);
        title_view = (TextView)findViewById(R.id.del_dialog_title);

        if(midViewColor != -1){
            del_view.setTextColor(midViewColor);
        }
        if(topViewColor != -1){
            title_view.setTextColor(topViewColor);
        }
        if(bottomViewColor != -1){
            cel_view.setTextColor(bottomViewColor);
        }

        cel_view.setText(cel);
        del_view.setText(sure);
        title_view.setText(title);

        title_view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(delCallBack!=null){
                    delCallBack.sure();
                }
            }
        });
        cel_view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(delCallBack!=null){
                    delCallBack.cancel();
                }
            }
        });
        del_view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(delCallBack!=null){
                    delCallBack.del();
                }
            }
        });
    }
    protected int topViewColor = -1;
    protected int midViewColor = -1;
    protected int bottomViewColor = -1;
    public void setTopTextViewColor(int colorValue){
        topViewColor = colorValue;
    }
    public void setMiddleTextViewColor(int colorValue){
        midViewColor = colorValue;
    }
    public void setBottomTextViewColor(int colorValue){
        bottomViewColor = colorValue;
    }
    public interface DelCallBack{
        void del();
        void sure();
        void cancel();
    }
}
