package com.yanxiu.gphone.student.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.yanxiu.gphone.student.YanxiuApplication;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cangHaiXiao.
 * Time : 2016/12/28 11:39.
 * Function :以节省内存的方式加载资源图片
 */

public class LoadingLocalImgUtils {

    private static LoadingLocalImgUtils utils;
    private Map<Integer,WeakReference<BitmapDrawable>> map=new HashMap<>();

    public static LoadingLocalImgUtils getInstence(){
        if (utils==null){
            utils=new LoadingLocalImgUtils();
        }
        return utils;
    }

    /**
     * 加载本地资源图片
     * @param view 要设置背景图的view
     * @param resID 背景图的资源id
     * */
    public Drawable getImage(View view, int resID){
        BitmapDrawable drawable = null;
        if (map!=null){
            WeakReference<BitmapDrawable> weak=map.get(resID);
            if (weak!=null){
                drawable=weak.get();
            }
        }else {
            map=new HashMap<>();
        }

        if (drawable==null){
            drawable=getBitmapDrawable(resID);
            WeakReference<BitmapDrawable> weak=new WeakReference<>(drawable);
            map.put(resID,weak);
        }
        if (drawable!=null&&view!=null){
            view.setBackground(drawable);
        }
        return drawable;
    }

    private BitmapDrawable getBitmapDrawable(int resID){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = YanxiuApplication.getInstance().getResources().openRawResource(resID);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(YanxiuApplication.getInstance().getResources(), bm);
        return bd;
    }
}
