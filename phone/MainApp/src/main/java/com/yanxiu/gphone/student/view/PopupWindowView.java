package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Administrator on 2016/10/25.
 */

public class PopupWindowView {

    private final GifView gifview;
    PopupWindowView windowView;
    PopupWindow popupWindow;

    public PopupWindowView getInstence(Context context){
        windowView=new PopupWindowView(context);
        return windowView;
    }

    public PopupWindowView(Context context){
        popupWindow=new PopupWindow(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        View view= LayoutInflater.from(context).inflate(R.layout.gif_popupwindow,null);
        gifview= (GifView) view.findViewById(R.id.gifview);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
    }

    public void setGif(int resID){
        if (gifview!=null){
            gifview.setMovieResource(resID);
        }
    }

    public void setPopShowing(View view){
        popupWindow.showAsDropDown(view,0,0);
    }

}
