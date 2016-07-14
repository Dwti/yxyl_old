/** 
 * @ProjectName:CYFrameAndroid  
 * @Title: DensityUtils.java 
 * @Package com.cyou.cyframeandroid.util 
 * @Description: (用一句话描述该文件做什么) 
 * @author liuqi qiliu_17173@cyou-inc.com   
 * @date 2013-8-19 下午2:37:32 
 * @version V1.0   
 * Copyright (c) 2013搜狐公司-版权所有
 */
package com.common.core.utils;

import android.content.Context;

/**
 * @ClassName: DensityUtils
 * @Description: (方便进行px和dp之间的转换)
 * @date 2013-8-19 下午2:37:32
 */
public class DensityUtils {
	/**
	 * 
	 * @Title: dip2px
	 * @Description: (根据手机的分辨率从 dp 的单位 转成为 px(像素))
	 * @param: @param context
	 * @param: @param dpValue
	 * @param: @return 设定文件
	 * @return: int 返回类型
	 * @date: 2013-8-19 下午2:38:08
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 
	 * @Title: px2dip
	 * @Description: (根据手机的分辨率从 px(像素) 的单位 转成为 dp )
	 * @param: @param context
	 * @param: @param pxValue
	 * @param: @return 设定文件
	 * @return: int 返回类型
	 * @date: 2013-8-19 下午2:38:24
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     *            （DisplayMetrics类中属性scaledDensity）
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     *            （DisplayMetrics类中属性scaledDensity）
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
}
