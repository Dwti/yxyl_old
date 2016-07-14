package com.yanxiu.gphone.hd.student.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.R;

/**
 * Created by Administrator on 2015/12/18.
 * 使用不同的字体库
 */
public class YanxiuTypefaceTextView extends TextView{

    private static final String PATH_TYPEFACE = "fonts/";
    private Context mContext;
    private String faceType;
    private String fz_ttf = "fz.ttf";
    private String metor_bold_OTF = "metor_bold.OTF";
    private String arial_runded_bold_ttf = "arial_runded_bold.ttf";

    public YanxiuTypefaceTextView(Context context) {
        super(context);
        initData(context, null);
    }

    public YanxiuTypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
    }

    public YanxiuTypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.yanxiu_type_face);
            faceType = typedArray.getString(R.styleable.yanxiu_type_face_type_face);
            if(!TextUtils.isEmpty(faceType)){
                if(faceType.equals(fz_ttf)){
                    setTypefaceName(TypefaceType.FANGZHENG);
                }else if(faceType.equals(metor_bold_OTF)){
                    setTypefaceName(TypefaceType.METRO_BOLD);
                }else if(faceType.equals(arial_runded_bold_ttf)){
                    setTypefaceName(TypefaceType.ARAL_ROUNDED_BOLD);
                }
            }
            typedArray.recycle();
        }
    }

    public enum TypefaceType{
        FANGZHENG(PATH_TYPEFACE + "fz.ttf"),
        METRO_BOLD(PATH_TYPEFACE + "metor_bold.OTF"),
        ARAL_ROUNDED_BOLD(PATH_TYPEFACE + "arial_runded_bold.ttf");
        public String path;
        TypefaceType(String path){
            this.path = path;
        }
    }
}
