package com.yanxiu.gphone.hd.student.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.view.YanxiuTypefaceTextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Util {


    private static String getMacAddress() {
        String macAddress = null;
        WifiInfo wifiInfo = ((WifiManager) YanxiuApplication.getInstance().getSystemService(Context.WIFI_SERVICE))
                .getConnectionInfo();
        if (wifiInfo != null) {
            macAddress = wifiInfo.getMacAddress();
            if (macAddress == null || macAddress.length() <= 0) {
                return "";
            } else {
                return macAddress;
            }
        } else {
            return "";
        }
    }

    public static String generateDeviceId(Context context) {
        String deviceId = PreferencesManager.getInstance().getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = StoreUtils.getRelevantData(context, "device_info");
            if (TextUtils.isEmpty(deviceId)) {
                String synthesize = CommonCoreUtil.getIMEI() + getMacAddress();
                deviceId =  CommonCoreUtil.MD5Helper(synthesize);

                PreferencesManager.getInstance().setDeviceId(deviceId);
                StoreUtils.saveRelevantData(context, "device_info", deviceId);
            } else {
                PreferencesManager.getInstance().setDeviceId(deviceId);
            }
        }
        return deviceId;
    }


    /**
     * dip转px
     */
    public static int dipToPx(int dipValue) {
        final float scale = YanXiuConstant.displayMetrics.density;
        int pxValue = (int) (dipValue * scale + 0.5f);

        return pxValue;
    }
    public static int px2sp(float pxValue) {
        final float fontScale = YanXiuConstant.displayMetrics.scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = YanXiuConstant.displayMetrics.density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转dip
     */
    public static float dipToPxFloat(int dipValue) {
        final float scale = YanXiuConstant.displayMetrics.density;
        float pxValue = dipValue * scale;

        return pxValue;
    }

    /**
     * dip转px
     */
    public static int dipToPx(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pxValue = (int) (dipValue * scale + 0.5f);

        return pxValue;
    }


    private static Toast mToast = null;

    private static View mToastView = null;
    private static TextView toastView1 = null;
    private static TextView toastView2 = null;
    private static TextView toastView3 = null;

    public static void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        if(!TextUtils.isEmpty(text)){
            mToast = Toast.makeText(YanxiuApplication.getInstance(), text, Toast.LENGTH_SHORT);
            mToast.setText(text);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void showToast(int txtId) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(YanxiuApplication.getInstance(), txtId, Toast.LENGTH_SHORT);
        mToast.setText(txtId);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showCustomToast(int txtId) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToastView = CommonCoreUtil.inflate(YanxiuApplication.getInstance(), R.layout.custom_toast, null, false);
        TextView toastTextView = (TextView) mToastView.findViewById(R.id.TextViewInfo);
        toastTextView.setText(txtId);
        mToast = new Toast(YanxiuApplication.getInstance());
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mToastView);
        mToast.show();
    }

    public static void showCustomTipToast(int txtId) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToastView = CommonCoreUtil.inflate(YanxiuApplication.getInstance(), R.layout.custom_tip_toast, null, false);
        TextView toastTextView = (TextView) mToastView.findViewById(R.id.TextViewTipToast);
        toastTextView.setText(txtId);
        mToast = new Toast(YanxiuApplication.getInstance());
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mToastView);
        mToast.show();
    }

    public static void showUserToast(int txtId1,int txtId2,int txtId3){
        mToastView = CommonCoreUtil.inflate(YanxiuApplication.getInstance(), R.layout.student_toast_layout, null, false);
        toastView1 = (TextView)mToastView.findViewById(R.id.toast1);
        toastView2 = (TextView)mToastView.findViewById(R.id.toast2);
        toastView3 = (TextView)mToastView.findViewById(R.id.toast3);

        if(txtId1!=-1){
            toastView1.setVisibility(View.VISIBLE);
            toastView1.setText(YanxiuApplication.getContext().getResources().getString(txtId1));
        }else{
            toastView1.setVisibility(View.GONE);
        }
        if(txtId2!=-1){
            toastView2.setVisibility(View.VISIBLE);
            toastView2.setText(YanxiuApplication.getContext().getResources().getString(txtId2));
        }else{
            toastView2.setVisibility(View.GONE);
        }
        if(txtId3!=-1){
            toastView3.setVisibility(View.VISIBLE);
            toastView3.setText(YanxiuApplication.getContext().getResources().getString(txtId3));
        }else{
            toastView3.setVisibility(View.GONE);
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(YanxiuApplication.getInstance());
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mToastView);
        mToast.show();
    }

    public static void showUserToast(String txt1,String txt2,String txt3){
        mToastView = CommonCoreUtil.inflate(YanxiuApplication.getInstance(), R.layout.student_toast_layout, null, false);
        toastView1 = (TextView)mToastView.findViewById(R.id.toast1);
        toastView2 = (TextView)mToastView.findViewById(R.id.toast2);
        toastView3 = (TextView)mToastView.findViewById(R.id.toast3);

        if(!StringUtils.isEmpty(txt1)){
            toastView1.setVisibility(View.VISIBLE);
            toastView1.setText(txt1);
        }else{
            toastView1.setVisibility(View.GONE);
        }
        if(!StringUtils.isEmpty(txt2)){
            toastView2.setVisibility(View.VISIBLE);
            toastView2.setText(txt2);
        }else{
            toastView2.setVisibility(View.GONE);
        }
        if(!StringUtils.isEmpty(txt3)){
            toastView3.setVisibility(View.VISIBLE);
            toastView3.setText(txt3);
        }else{
            toastView3.setVisibility(View.GONE);
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(YanxiuApplication.getInstance());
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(mToastView);
        mToast.show();
    }





    public static int getIconRes(String subjectId){
        if(subjectId.equals(YanXiuConstant.SUBJECT.YUWEN+"")){
            return R.drawable.group_yuwen_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.SHUXUE+"")){
            return R.drawable.group_shuxue_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.YINYU+"")){
            return R.drawable.group_english_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.WULI+"")){
            return R.drawable.group_wuli_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.HUAXUE+"")){
            return R.drawable.group_huaxue_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.SHENGWU+"")){
            return R.drawable.group_shengwu_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.DILI+"")){
            return R.drawable.group_dili_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.ZHENGZHI+"")){
            return R.drawable.group_zhengzhi_icon;
        } else if(subjectId.equals(YanXiuConstant.SUBJECT.LISHI+"")){
            return R.drawable.group_lishi_icon;
        } else{
            return R.drawable.logo;
        }
    }
    public static void setIcon(int subjectId,ImageView view){
        switch (subjectId){
        case YanXiuConstant.SUBJECT.YUWEN:
            view.setBackgroundResource(R.drawable.group_yuwen_icon);
            break;
        case YanXiuConstant.SUBJECT.SHUXUE:
            view.setBackgroundResource(R.drawable.group_shuxue_icon);
            break;
        case YanXiuConstant.SUBJECT.YINYU:
            view.setBackgroundResource(R.drawable.group_english_icon);
            break;
        case YanXiuConstant.SUBJECT.WULI:
            view.setBackgroundResource(R.drawable.group_wuli_icon);
            break;
        case YanXiuConstant.SUBJECT.HUAXUE:
            view.setBackgroundResource(R.drawable.group_huaxue_icon);
            break;
        case YanXiuConstant.SUBJECT.SHENGWU:
            view.setBackgroundResource(R.drawable.group_shengwu_icon);
            break;
        case YanXiuConstant.SUBJECT.DILI:
            view.setBackgroundResource(R.drawable.group_dili_icon);
            break;
        case YanXiuConstant.SUBJECT.ZHENGZHI:
            view.setBackgroundResource(R.drawable.group_zhengzhi_icon);
            break;
        case YanXiuConstant.SUBJECT.LISHI:
            view.setBackgroundResource(R.drawable.group_lishi_icon);
            break;
        }
    }



    public static JSONArray sortQuestionData(QuestionEntity entity){
        int typeId = entity.getType_id();
        AnswerBean answerBean = entity.getAnswerBean();
        JSONArray array = new JSONArray();
        if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES.type || typeId == YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE.type) {
            if(!TextUtils.isEmpty(answerBean.getSelectType())){
                array.put(answerBean.getSelectType());
            }
        }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES.type){
            int countMulti = answerBean.getMultiSelect().size();
            Collections.sort(answerBean.getMultiSelect());
            for(int j = 0; j < countMulti; j++){
                try {
                    array.put(answerBean.getMultiSelect().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS.type){
            int countFill = answerBean.getFillAnswers().size();
            for(int j = 0; j < countFill; j++){
                try {
                    array.put(answerBean.getFillAnswers().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(typeId == YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE.type){
            int imageUri = answerBean.getSubjectivImageUri().size();
            for(int j = 0; j < imageUri; j++){
                try {
                    array.put(answerBean.getSubjectivImageUri().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return array;
    }



    /**
     * 分析布局层次
     * @param activity
     * @param layoutId
     */
    public static void toDispScalpelFrameLayout(Activity activity, int layoutId){
        ScalpelFrameLayout mScalpelView = new ScalpelFrameLayout(activity);
        mScalpelView.setLayerInteractionEnabled(true);
        mScalpelView.addView(activity.getLayoutInflater().inflate(layoutId, null));
        mScalpelView.setDrawViews(true);
        mScalpelView.setDrawIds(true);
        activity.setContentView(mScalpelView);
    }

    /**
     * 设置字体
     * @param view
     * @param typefaceType
     */
    public static void setViewTypeface(YanxiuTypefaceTextView.TypefaceType typefaceType, TextView
            ...view){
//        LogInfo.log("haitian", typefaceType.path);
        Typeface tf = Typeface.createFromAsset(YanxiuApplication.getInstance().getAssets(),
                typefaceType
                .path);
        for(int i=0; i<view.length; i++) {
            view[i].setTypeface(tf);
        }
    }

    /**
     * yanxiu ua
     * @param context
     * @return
     */
    public static String createUA(Context context){
        String ua = "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) " + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
        ua += " " + "YanxiuMobileClient_" + CommonCoreUtil.getClientVersionName(context) + "_android";
        return ua;
    }

    /**
     * 复制到剪贴板
     *
     */
    public static void copyToClipData(Context context,String copyMsg){
        if(context==null){
            return;
        }
        if(StringUtils.isEmpty(copyMsg)){
            return;
        }
        ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("text",copyMsg);
        clipboardManager.setPrimaryClip(clipData);
        showToast(context.getResources().getString(R.string.copy_data));
    }

    //list转换成json字符串
    public static String listToJson(ArrayList arrayList) {
        String string = "[";
        for (int i=0; i<arrayList.size(); i++) {
            string = string + Util.hashMapToJsonTwo((HashMap)arrayList.get(i));
        }
        string = string + "]";
        return string;
    }

    //map转换为json字符串
    public static String hashMapToJsonTwo(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getKey().equals(YanXiuConstant.reserved)){
                string += "\"" + e.getKey() + "\":";
                string += "" + e.getValue() + ",";
            } else {
                string += "\"" + e.getKey() + "\":";
                string += "\"" + e.getValue() + "\",";
            }

        }

        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;

    }

    //map转换为json字符串
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            string += "" + e.getValue() + ",";
        }

        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;

    }

    //typetoText
    public static String examTypeToText(int i) {
        switch (i) {
            case 1:
                return "单选题";
            case 2:
                return "多选题";
            case 3:
                return "填空题";
            case 4:
                return "判断题";
            case 5:
                return "材料题";
            case 6:
                return "主观题";
        }
        return "单选题";
    }


}

