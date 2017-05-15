package com.yanxiu.gphone.studentold.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;

/**
 * Created by sunpeng on 2016/8/25.
 */
public class TipsDialog {
    private Button btnGuide;
    private Dialog dialog;
    private TextView tv_msg;
    public TipsDialog(Context context){
        dialog = new Dialog(context, R.style.del_dialog_style);
        dialog.setContentView(R.layout.tips_dialog);
        tv_msg= (TextView) dialog.findViewById(R.id.tv_guide_text);
        btnGuide = (Button) dialog.findViewById(R.id.btn_guide_btn);
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void show(String msg){
        if(dialog!=null){
            tv_msg.setText(msg);
            dialog.show();
        }
    }

    public void dismiss(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
