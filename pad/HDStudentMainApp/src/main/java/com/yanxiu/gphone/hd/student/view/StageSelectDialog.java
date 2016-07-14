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
public class StageSelectDialog extends Dialog {

    private TextView stageSure;
    private TextView stageCancel;

    private StageDialogCallBack stageDialogCallBack;

    private Context mContext;

    public StageSelectDialog (Context context, StageDialogCallBack
            stageDialogCallBack) {
        super(context, R.style.alert_dialog_style);
        setOwnerActivity((Activity) context);
        mContext = context;
        this.stageDialogCallBack = stageDialogCallBack;
    }

    public void setStageDialogCallBack (StageDialogCallBack stageDialogCallBack) {
        this.stageDialogCallBack = stageDialogCallBack;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        int offsetX = CommonCoreUtil.getScreenWidth() - RightContainerUtils.getInstance().getContainerWidth();
        win.getDecorView().setPadding(offsetX, 0, 0, 0);

        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width =  WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setContentView(R.layout.stage_select_popupwindow);
        stageSure = (TextView) findViewById(R.id.stage_layout_sure);
        stageCancel = (TextView) findViewById(R.id.stage_layout_cel);
        stageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (stageDialogCallBack != null) {
                    stageDialogCallBack.cancel();
                }
                dismiss();
            }
        });
        stageSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (stageDialogCallBack != null) {
                    stageDialogCallBack.stage();
                }
                dismiss();
            }
        });
    }

    public interface StageDialogCallBack {
        void stage ();
        void cancel ();
    }
}
