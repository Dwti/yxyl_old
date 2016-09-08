package com.yanxiu.gphone.student.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.preference.PreferencesManager;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        mToastView = CommonCoreUtil.inflate(YanxiuApplication.getInstance(), R.layout.student_toast_layout,null,false);
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
        String template = entity.getTemplate();
        AnswerBean answerBean = entity.getAnswerBean();
        JSONArray array = new JSONArray();
        if(YanXiuConstant.SINGLE_CHOICES.equals(template) || YanXiuConstant.JUDGE_QUESTION.equals(template)) {
            if(!TextUtils.isEmpty(answerBean.getSelectType())){
                array.put(answerBean.getSelectType());
            }
        }else if(YanXiuConstant.MULTI_CHOICES.equals(template)){
            int countMulti = answerBean.getMultiSelect().size();
            Collections.sort(answerBean.getMultiSelect());
            for(int j = 0; j < countMulti; j++){
                try {
                    array.put(answerBean.getMultiSelect().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(YanXiuConstant.FILL_BLANK.equals(template) || YanXiuConstant.NEW_FILL_BLANK.equals(template) ){
            int countFill = answerBean.getFillAnswers().size();
            for(int j = 0; j < countFill; j++){
                try {
                    array.put(answerBean.getFillAnswers().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (YanXiuConstant.CONNECT_QUESTION.equals(template) || YanXiuConstant.CLASSIFY_QUESTION.equals(template) ){
            int count_connect_classfy = answerBean.getConnect_classfy_answer().size();
            for(int j = 0; j < count_connect_classfy; j++){
                try {
                    array.put(answerBean.getConnect_classfy_answer().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(YanXiuConstant.ANSWER_QUESTION.equals(template)){
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
        CommonCoreUtil.setViewTypeface( typefaceType
                .path,view);

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
            string += ",";
        }
        string = string.substring(0, string.lastIndexOf(",")) + "]";
        return string;
    }

    //map转换为json字符串
    public static String hashMapToJsonTwo(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getKey().equals(YanXiuConstant.reserved) || e.getKey().equals(YanXiuConstant.qID)){
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

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return int2ip(i);
        } catch (Exception ex) {
            return " 获取IP出错\n" + ex.getMessage();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String testDataStr = "";
    public static SubjectExercisesItemBean getSubjectExercisesItemBean() {
        String str = "{\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"authorid\": 20049003,\n" +
                "            \"bedition\": 1452,\n" +
                "            \"begintime\": 1468320000000,\n" +
                "            \"buildtime\": 1468320062000,\n" +
                "            \"chapterName\": \"第1章 统计\",\n" +
                "            \"chapterid\": 201804,\n" +
                "            \"editionName\": \"大纲版\",\n" +
                "            \"endtime\": 1468339140000,\n" +
                "            \"id\": 88398,\n" +
                "            \"name\": \"7月12日数学作业\",\n" +
                "            \"paperStatus\": {\n" +
                "                \"begintime\": 1470103308000,\n" +
                "                \"checkStatus\": 0,\n" +
                "                \"costtime\": 3,\n" +
                "                \"endtime\": 1470103441000,\n" +
                "                \"id\": 70835,\n" +
                "                \"ppid\": 88398,\n" +
                "                \"rate\": 0,\n" +
                "                \"status\": 1,\n" +
                "                \"tid\": 0,\n" +
                "                \"uid\": 6355\n" +
                "            },\n" +
                "            \"paperTest\": [\n" +
                "               {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"考查对词组看电视的掌握。<p><br/></p>\",\n" +
                "                        \"answer\": [\n" +
                "                            \"watches\",\n" +
                "                            \"TV\"\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2740230\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"26021\",\n" +
                "                                \"name\": \"watchTV(watchtelevision)\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"<p><br/>Tonyoften(_)(_)onSunday.<br/></p>\",\n" +
                "                        \"template\": \"fill\",\n" +
                "                        \"type_id\": \"6\"\n" +
                "                    }\n" +
                "                }," +
                "               {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"考查对词组看电视的掌握。<p><br/></p>\",\n" +
                "                        \"answer\": [\n" +
                "                            \"watches\",\n" +
                "                            \"TV\"\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2740230\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"26021\",\n" +
                "                                \"name\": \"watchTV(watchtelevision)\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"<p><br/>Tonyoften(_)(_)onSunday.<br/></p>\",\n" +
                "                        \"template\": \"fill\",\n" +
                "                        \"type_id\": \"3\"\n" +
                "                    }\n" +
                "                }," +
                "                {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"\",\n" +
                "                        \"answer\": [\n" +
                "                            null\n" +
                "                        ],\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"dsafasf\",\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"sadfasf\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734958\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"ddd\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"1\"\n" +
                "                                ],\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734959\",\n" +
                "                                \"stem\": \"asdfasfasfasfsaf\",\n" +
                "                                \"template\": \"alter\",\n" +
                "                                \"type_id\": \"4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2734957\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"1033\",\n" +
                "                                \"name\": \"社会主义现代化成就\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"<p>One evening, it was raining and the wind was blowing hard. An old couple came to a small hotel and wanted to stay there for the night. A young man welcomed them warmly, but said “I’m sorry! Our rooms here are all full and the hotels nearby are all full too, for there will be an important meeting held here tomorrow.”</p><p>Hearing the young man’s words, the old couple felt very disappointed, and turned around to leave.</p><p>Just as they were leaving, the young man came up to them and stopped them: “Madam and sir, if you don’t mind, you can sleep in my bedroom for a night…”</p><p>The next morning, the old couple took out lots of money to give it to the young man, but he refused to take it.</p><p>“No! You needn’t pay me any money, for I only lend my room to you.” said the young man with a smile on his face.</p><p>“You’re great, young man! It’s very kind of you. Maybe one day, I’ll build a hotel for you!” said the old man. With these words, the old couple left. The young man only laughed and went on working.</p><p>Several years later, the young man got a letter from the old couple, inviting him to go to Manhattan. The young man met the old couple in front of a five-star hotel.</p><p>“Do you still remember what I said to you several years ago? Look! ________” said the old man. Soon, the young man became the manager of the hotel.</p><p><br/></p>\",\n" +
                "                        \"template\": \"multi\",\n" +
                "                        \"type_id\": \"13\"\n" +
                "                    },\n" +
                "                    \"sectionid\": 201804\n" +
                "                },\n" +
                "                {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"\",\n" +
                "                        \"answer\": [\n" +
                "                            null\n" +
                "                        ],\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"dsafasf\",\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"sadfasf\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734958\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"test\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"test1\",\n" +
                "                                        \"test2\",\n" +
                "                                        \"test3\",\n" +
                "                                        \"test4\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734959\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"test\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"ceshi1\",\n" +
                "                                        \"ceshi2\",\n" +
                "                                        \"ceshi3\",\n" +
                "                                        \"ceshi4\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734960\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
//                "                            {\n" +
//                "                                \"analysis\": \"ddd\",\n" +
//                "                                \"answer\": [\n" +
//                "                                    \"1\"\n" +
//                "                                ],\n" +
//                "                                \"difficulty\": \"1\",\n" +
//                "                                \"id\": \"2734959\",\n" +
//                "                                \"stem\": \"asdfasfasfasfsaf\",\n" +
//                "                                \"template\": \"alter\",\n" +
//                "                                \"type_id\": \"4\"\n" +
//                "                            }\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2734957\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"1033\",\n" +
                "                                \"name\": \"社会主义现代化成就\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"鲁迅，原名(_)字，他的代表作品是小说集，散文集。鲁迅再《琐记》一文中，用了来讥讽洋务(_)派的办学。鲁迅写出了中国现代第一篇白话小说(_)，1918年在上发表其后又发表等著名小说。\",\n" +
                "                        \"template\": \"multi\",\n" +
                "                        \"type_id\": \"15\"\n" +
                "                    },\n" +
                "                    \"sectionid\": 201804\n" +
                "                },\n" +
                "                {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"\",\n" +
                "                        \"answer\": [\n" +
                "                            null\n" +
                "                        ],\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"dsafasf\",\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"sadfasf\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734958\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"ddd\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"1\"\n" +
                "                                ],\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734959\",\n" +
                "                                \"stem\": \"asdfasfasfasfsaf\",\n" +
                "                                \"template\": \"alter\",\n" +
                "                                \"type_id\": \"4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2734957\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"1033\",\n" +
                "                                \"name\": \"社会主义现代化成就\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"<p>One evening, it was raining and the wind was blowing hard. An old couple came to a small hotel and wanted to stay there for the night. A young man welcomed them warmly, but said “I’m sorry! Our rooms here are all full and the hotels nearby are all full too, for there will be an important meeting held here tomorrow.”</p><p>Hearing the young man’s words, the old couple felt very disappointed, and turned around to leave.</p><p>Just as they were leaving, the young man came up to them and stopped them: “Madam and sir, if you don’t mind, you can sleep in my bedroom for a night…”</p><p>The next morning, the old couple took out lots of money to give it to the young man, but he refused to take it.</p><p>“No! You needn’t pay me any money, for I only lend my room to you.” said the young man with a smile on his face.</p><p>“You’re great, young man! It’s very kind of you. Maybe one day, I’ll build a hotel for you!” said the old man. With these words, the old couple left. The young man only laughed and went on working.</p><p>Several years later, the young man got a letter from the old couple, inviting him to go to Manhattan. The young man met the old couple in front of a five-star hotel.</p><p>“Do you still remember what I said to you several years ago? Look! ________” said the old man. Soon, the young man became the manager of the hotel.</p><p><br/></p>\",\n" +
                "                        \"template\": \"multi\",\n" +
                "                        \"type_id\": \"16\"\n" +
                "                    },\n" +
                "                    \"sectionid\": 201804\n" +
                "                },\n" +
                "                {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"\",\n" +
                "                        \"answer\": [\n" +
                "                            null\n" +
                "                        ],\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"dsafasf\",\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"sadfasf\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734958\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"ddd\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"1\"\n" +
                "                                ],\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734959\",\n" +
                "                                \"stem\": \"asdfasfasfasfsaf\",\n" +
                "                                \"template\": \"alter\",\n" +
                "                                \"type_id\": \"4\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2734957\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"1033\",\n" +
                "                                \"name\": \"社会主义现代化成就\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"<p>One evening, it was raining and the wind was blowing hard. An old couple came to a small hotel and wanted to stay there for the night. A young man welcomed them warmly, but said “I’m sorry! Our rooms here are all full and the hotels nearby are all full too, for there will be an important meeting held here tomorrow.”</p><p>Hearing the young man’s words, the old couple felt very disappointed, and turned around to leave.</p><p>Just as they were leaving, the young man came up to them and stopped them: “Madam and sir, if you don’t mind, you can sleep in my bedroom for a night…”</p><p>The next morning, the old couple took out lots of money to give it to the young man, but he refused to take it.</p><p>“No! You needn’t pay me any money, for I only lend my room to you.” said the young man with a smile on his face.</p><p>“You’re great, young man! It’s very kind of you. Maybe one day, I’ll build a hotel for you!” said the old man. With these words, the old couple left. The young man only laughed and went on working.</p><p>Several years later, the young man got a letter from the old couple, inviting him to go to Manhattan. The young man met the old couple in front of a five-star hotel.</p><p>“Do you still remember what I said to you several years ago? Look! ________” said the old man. Soon, the young man became the manager of the hotel.</p><p><br/></p>\",\n" +
                "                        \"template\": \"multi\",\n" +
                "                        \"type_id\": \"17\"\n" +
                "                    },\n" +
                "                    \"sectionid\": 201804\n" +
                "                },\n" +
                "                {\n" +
                "                    \"difficulty\": 3,\n" +
                "                    \"id\": 570354,\n" +
                "                    \"isfavorite\": 0,\n" +
                "                    \"knowledgepoint\": \"44463\",\n" +
                "                    \"pad\": {\n" +
                "                        \"answer\": \"[]\",\n" +
                "                        \"costtime\": 0,\n" +
                "                        \"id\": 390741,\n" +
                "                        \"jsonAnswer\": [],\n" +
                "                        \"ptid\": 570354,\n" +
                "                        \"status\": 3,\n" +
                "                        \"uid\": 6355\n" +
                "                    },\n" +
                "                    \"pid\": 88398,\n" +
                "                    \"qid\": 2734957,\n" +
                "                    \"qtype\": 0,\n" +
                "                    \"questions\": {\n" +
                "                        \"analysis\": \"\",\n" +
                "                        \"answer\": [\n" +
                "                            null\n" +
                "                        ],\n" +
                "                        \"children\": [\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"dsafasf\",\n" +
                "                                        \"asdfas\",\n" +
                "                                        \"sadfasf\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734958\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"test1\",\n" +
                "                                        \"test2\",\n" +
                "                                        \"test3\",\n" +
                "                                        \"test4\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734959\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"analysis\": \"asdfasfs\",\n" +
                "                                \"answer\": [\n" +
                "                                    \"3\"\n" +
                "                                ],\n" +
                "                                \"content\": {\n" +
                "                                    \"choices\": [\n" +
                "                                        \"ceshi1\",\n" +
                "                                        \"ceshi2\",\n" +
                "                                        \"ceshi3\",\n" +
                "                                        \"ceshi4\"\n" +
                "                                    ]\n" +
                "                                },\n" +
                "                                \"difficulty\": \"1\",\n" +
                "                                \"id\": \"2734960\",\n" +
                "                                \"stem\": \"<p>testtesds</p><br/>\",\n" +
                "                                \"template\": \"choice\",\n" +
                "                                \"type_id\": \"1\"\n" +
                "                            },\n" +
                "                        ],\n" +
                "                        \"difficulty\": \"1\",\n" +
                "                        \"id\": \"2734957\",\n" +
                "                        \"point\": [\n" +
                "                            {\n" +
                "                                \"id\": \"1033\",\n" +
                "                                \"name\": \"社会主义现代化成就\"\n" +
                "                            }\n" +
                "                        ],\n" +
                "                        \"stem\": \"鲁迅，原名(_)字，他的代表作品是小说集，散文集。鲁迅再《琐记》一文中，用了来讥讽洋务(_)派的办学。鲁迅写出了中国现代第一篇白话小说(_)，1918年在上发表其后又发表等著名小说。\",\n" +
                "                        \"template\": \"multi\",\n" +
                "                        \"type_id\": \"15\"\n" +
                "                    },\n" +
                "                    \"sectionid\": 201804\n" +
                "                }\n" +
                "            ],\n" +
                "            \"ptype\": 1,\n" +
                "            \"quesnum\": 1,\n" +
                "            \"sectionid\": 0,\n" +
                "            \"showana\": 1,\n" +
                "            \"stageName\": \"高中\",\n" +
                "            \"stageid\": 1204,\n" +
                "            \"status\": 1,\n" +
                "            \"subjectName\": \"数学\",\n" +
                "            \"subjectid\": 1103,\n" +
                "            \"subquesnum\": 2,\n" +
                "            \"volume\": 201803,\n" +
                "            \"volumeName\": \"高三上\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"status\": {\n" +
                "        \"code\": 0,\n" +
                "        \"desc\": \"get question list success\"\n" +
                "    }\n" +
                "}";
        if (!TextUtils.isEmpty(testDataStr)) {
            return (SubjectExercisesItemBean) JSON.parseObject(testDataStr, SubjectExercisesItemBean.class);
        } else {
            return (SubjectExercisesItemBean) JSON.parseObject(str, SubjectExercisesItemBean.class);
        }
    }

    public static int convertDpToPx(Context context, int dp) {
        if (context == null)
            return 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    public static String splitMiddleChar(String string) {
        Pattern pattern = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher matcher = pattern.matcher(string);
        while(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

}

