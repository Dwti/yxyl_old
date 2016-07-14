package com.yanxiu.gphone.hd.student.inter;

import android.view.View;

import com.common.core.utils.CommonCoreUtil;

/**
 * 防连点监听类
 * Created by Administrator on 2016/2/25.
 */
public  class ProhibitedDoubleClickImpl implements View.OnClickListener {
    private ProhibitedDroubleOnClickListener prohibitedClickListener;
    private int interval=-1;
    public static final int DEFAULT_INTERVAL=800;
    public ProhibitedDoubleClickImpl(ProhibitedDroubleOnClickListener prohibitedClickListener,int interval){
        this.prohibitedClickListener=prohibitedClickListener;
        this.interval=interval;
    }

    public ProhibitedDoubleClickImpl(ProhibitedDroubleOnClickListener prohibitedClickListener){
        this.prohibitedClickListener=prohibitedClickListener;
    }


    public interface ProhibitedDroubleOnClickListener{
         void onClick(View view);
    }
    @Override
    public void onClick(View view) {
        if(interval<=0){
            if(!CommonCoreUtil.checkClickEvent(DEFAULT_INTERVAL)){
                return;
            }
        }else{
            if(!CommonCoreUtil.checkClickEvent(interval)){
                return;
            }
        }
        if(prohibitedClickListener!=null){
            prohibitedClickListener.onClick(view);
        }
    }
}
