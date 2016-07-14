package com.yanxiu.gphone.parent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yanxiu.gphone.parent.R;

/**
 * Created by Administrator on 2015/12/18.
 * 使用不同的字体库
 */
public class YanxiuParentTypefaceTextView extends TextView{

    private static final String PATH_TYPEFACE = "fonts/";
    private Context mContext;
    private String faceType;
    private static String metro_light_type = "MetroLightPlayType.otf";
    private static String metor_play_type = "MetroPlayType.otf";

    public YanxiuParentTypefaceTextView (Context context) {
        super(context);
        initData(context, null);
    }

    public YanxiuParentTypefaceTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    public YanxiuParentTypefaceTextView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    public void setTypefaceName(TypefaceType typefaceType){
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), typefaceType.path);
        this.setTypeface(tf);
    }


    private void initData(Context context, AttributeSet attrs) {
        mContext = context;
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable
                    .yanxiu_p_type_face);
            faceType = typedArray.getString(R.styleable.yanxiu_p_type_face_type_p_face);
            if(!TextUtils.isEmpty(faceType)){
                if(faceType.equals(metro_light_type)){
                    setTypefaceName(TypefaceType.METRO_LIGHT);
                }else if(faceType.equals(metor_play_type)){
                    setTypefaceName(TypefaceType.METOR_PLAY);
                }
            }
            typedArray.recycle();
        }
    }

    public enum TypefaceType{
        METRO_LIGHT(PATH_TYPEFACE + metro_light_type),
        METOR_PLAY(PATH_TYPEFACE + metor_play_type);
        public String path;
        TypefaceType(String path){
            this.path = path;
        }
    }
}
