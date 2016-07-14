package com.yanxiu.gphone.student.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

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
