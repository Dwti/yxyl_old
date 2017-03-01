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
public class MistakeRedoDialog extends Dialog {

    private TextView stageSure;
    private TextView stageCancel;

    private StageDialogCallBack stageDialogCallBack;

    private Context mContext;
    private TextView tatle;
    private TextView right;
    private TextView fail;

    public MistakeRedoDialog(Context context, StageDialogCallBack
            stageDialogCallBack) {
        super(context, R.style.alert_dialog_style);
        setOwnerActivity((Activity) context);
        mContext = context;
        this.stageDialogCallBack = stageDialogCallBack;
    }

    public void setQuestionNumber(String tatledata,String rightdata,String faildata){
        tatle.setText(tatledata);
        right.setText(rightdata);
        fail.setText(faildata);
    }

    public void setStageDialogCallBack (StageDialogCallBack stageDialogCallBack) {
        this.stageDialogCallBack = stageDialogCallBack;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mistake_redo_popupwindow);
        tatle= (TextView) findViewById(R.id.mistake_question_tatle);
        right= (TextView) findViewById(R.id.mistake_question_right);
        fail= (TextView) findViewById(R.id.mistake_question_fail);
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
        void stage();
        void cancel();
    }
}
