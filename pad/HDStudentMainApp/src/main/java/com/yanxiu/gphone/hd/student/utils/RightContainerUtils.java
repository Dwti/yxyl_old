package com.yanxiu.gphone.hd.student.utils;

import android.view.View;

import com.common.core.utils.CommonCoreUtil;

/**
 * 获取右侧容器相关参数
 * Created by libo on 16/2/18.
 */
public class RightContainerUtils {
    private static RightContainerUtils mRightContainer;
    private View mContainer;
    private RightContainerUtils(){}
    public  static RightContainerUtils getInstance(){
        if(mRightContainer==null){
            mRightContainer=new RightContainerUtils();
        }
        return mRightContainer;
    }
    public void measureRightContainer(View container){
        if(container==null){
            return;
        }
        this.mContainer=container;
        CommonCoreUtil.measureView(mContainer);
    }

    public int getContainerWidth(){
        if(mContainer==null){
            return 0;
        }
        return mContainer.getMeasuredWidth();
    }

    public int getContainerHeight(){
        if(mContainer==null){
            return 0;
        }
        return mContainer.getMeasuredHeight();
    }

}
