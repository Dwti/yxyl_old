package com.yanxiu.gphone.parent.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.core.utils.ContextProvider;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by lee on 16-3-21.
 */
public class ParentUtils {
    public static void showToast(int txtId) {
        Toast mToast = Toast.makeText(ContextProvider.getApplicationContext(), txtId, Toast.LENGTH_SHORT);
        mToast.setText(txtId);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }


    public static void showToast(String strTips) {
        Toast mToast = Toast.makeText(ContextProvider.getApplicationContext(), strTips, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void changeEdBg(View view,int length){
        if(view==null){
            return;
        }
        if(length>0){

            view.setBackgroundColor(ContextProvider.getApplicationContext().getResources().getColor(R.color.color_white_p));
        }else{
            view.setBackgroundColor(ContextProvider.getApplicationContext().getResources().getColor(R.color.color_ccffffff));
        }
    }

    public static void changeClearImgVisible(View view,int length){
        if(view==null){
            return;
        }
        if(length>0){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }


    public static void setIcon(int subjectId,ImageView view){
        switch (subjectId){
            case YanxiuParentConstants.SUBJECT.YUWEN:
                view.setBackgroundResource(R.drawable.parent_yuwen_icon);
                break;
            case YanxiuParentConstants.SUBJECT.SHUXUE:
                view.setBackgroundResource(R.drawable.parent_shuxu_icon);
                break;
            case YanxiuParentConstants.SUBJECT.YINGYU:
                view.setBackgroundResource(R.drawable.parent_yingyu_icon);
                break;
            case YanxiuParentConstants.SUBJECT.WULI:
                view.setBackgroundResource(R.drawable.parent_wuli_icon);
                break;
            case YanxiuParentConstants.SUBJECT.HUAXUE:
                view.setBackgroundResource(R.drawable.parent_huaxue_icon);
                break;
            case YanxiuParentConstants.SUBJECT.SHENGWU:
                view.setBackgroundResource(R.drawable.parent_shengwu_icon);
                break;
            case YanxiuParentConstants.SUBJECT.DILI:
                view.setBackgroundResource(R.drawable.parent_dili_icon);
                break;
            case YanxiuParentConstants.SUBJECT.ZHENGZHI:
                view.setBackgroundResource(R.drawable.parent_zhengzhi_icon);
                break;
            case YanxiuParentConstants.SUBJECT.LISHI:
                view.setBackgroundResource(R.drawable.parent_lishi_icon);
                break;
            case YanxiuParentConstants.SUBJECT.EXERCISE:
                view.setBackgroundResource(R.drawable.parent_exercise_icon);
                break;
            case YanxiuParentConstants.SUBJECT.HOMEWORK:
                view.setBackgroundResource(R.drawable.parent_homework_icon);
                break;
            default:
                view.setBackgroundResource(R.drawable.parent_exercise_icon);
                break;
        }
    }

    public static int getSubjectTextColor(int subjectId){
        int textColor = -1;
        switch (subjectId){
            case YanxiuParentConstants.SUBJECT.YUWEN:
                textColor = R.color.color_99804d_p;
                break;
            case YanxiuParentConstants.SUBJECT.SHUXUE:
                textColor = R.color.color_4d7f99_p;
                break;
            case YanxiuParentConstants.SUBJECT.YINGYU:
                textColor = R.color.color_7f994d_p;
                break;
            case YanxiuParentConstants.SUBJECT.WULI:
                textColor = R.color.color_4d4d99_p;
                break;
            case YanxiuParentConstants.SUBJECT.HUAXUE:
                textColor = R.color.color_7f4d99_p;
                break;
            case YanxiuParentConstants.SUBJECT.SHENGWU:
                textColor = R.color.color_994d80_p;
                break;
            case YanxiuParentConstants.SUBJECT.DILI:
                textColor = R.color.color_994d4d_p;
                break;
            case YanxiuParentConstants.SUBJECT.ZHENGZHI:
                textColor = R.color.color_4d994d_p;
                break;
            case YanxiuParentConstants.SUBJECT.LISHI:
                textColor = R.color.color_4d997f_p;
                break;
            default:
                textColor = R.color.color_black_p;
                break;
        }

        return ContextProvider.getApplicationContext().getResources().getColor(textColor);
    }


    public static int getNotSelHonorType(int type){
        int drawableValue;
        switch (type){
            case YanxiuParentConstants.HONOR_TYPE_ONE:
                drawableValue=R.drawable.jinshijidi_not_sel;
                break;
            case YanxiuParentConstants.HONOR_TYPE_TWO:
                drawableValue=R.drawable.weikuaibupo_not_sel;
                break;
            case YanxiuParentConstants.HONOR_TYPE_THREE:
                drawableValue=R.drawable.lianzhanlianjie_not_sel;
                break;
            case YanxiuParentConstants.HONOT_TYPE_FOUR:
                drawableValue=R.drawable.zhuluzhongyuan_not_sel;
                break;
            default:
                drawableValue=R.drawable.jinshijidi_not_sel;
                break;
        }
        return drawableValue;
    }

    public static int getSelHonorType(int type){
        int drawableValue;
        switch (type){
            case YanxiuParentConstants.HONOR_TYPE_ONE:
                drawableValue=R.drawable.jinshijidi_sel;
                break;
            case YanxiuParentConstants.HONOR_TYPE_TWO:
                drawableValue=R.drawable.weikuaibuopo_sel;
                break;
            case YanxiuParentConstants.HONOR_TYPE_THREE:
                drawableValue=R.drawable.lianzhanlianjie_sel;
                break;
            case YanxiuParentConstants.HONOT_TYPE_FOUR:
                drawableValue=R.drawable.zhuluzhongyuan_sel;
                break;
            default:
                drawableValue=R.drawable.jinshijidi_sel;
                break;
        }
        return drawableValue;
    }

    public static String getAquShareIconPath(int type){
        String path = null;
        switch (type){
            case YanxiuParentConstants.HONOR_TYPE_ONE:
                path=YanxiuParentConstants.XUANLIANGCIGU_ICON_PATH;
                break;
            case YanxiuParentConstants.HONOR_TYPE_TWO:
                path=YanxiuParentConstants.WEIKUAIBUPO_ICON_PATH;
                break;
            case YanxiuParentConstants.HONOR_TYPE_THREE:
                path=YanxiuParentConstants.LIANZHANLIANJIE_ICON_PATH;
                break;
            case YanxiuParentConstants.HONOT_TYPE_FOUR:
                path=YanxiuParentConstants.ZHULUZHONGYUAN_ICON_PATH;
                break;
            default:
                path=YanxiuParentConstants.DEFAULT_ICON_PATH;
                break;
        }
        return path;
    }

    public static int getAquShareIcon(int type){
        int drawableValue;
        switch (type){
            case YanxiuParentConstants.HONOR_TYPE_ONE:
                drawableValue=R.drawable.xuanliangcigu;
                break;
            case YanxiuParentConstants.HONOR_TYPE_TWO:
                drawableValue=R.drawable.weikuaibupo;
                break;
            case YanxiuParentConstants.HONOR_TYPE_THREE:
                drawableValue=R.drawable.lianzhanlianjie;
                break;
            case YanxiuParentConstants.HONOT_TYPE_FOUR:
                drawableValue=R.drawable.zhuluzhongyuan;
                break;
            default:
                drawableValue=R.drawable.xuanliangcigu;
                break;
        }
        return drawableValue;
    }

    public static void setSubjectTextColor(int subjectId, TextView view){
        int textColor = getSubjectTextColor(subjectId);
        view.setTextColor(textColor);
    }


    /**
     * 将长时间格式时间转换为字符串 MM月dd日
     *
     * @param longDate
     * @return
     */
    public static String longToFormateDate(long longDate) throws Exception{
        Date date = new Date(longDate);

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();	//今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();	//昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set( Calendar.HOUR_OF_DAY, 0);
        yesterday.set( Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);


        current.setTime(date);

        if(current.after(today)){
            return "今天";
        }else if(current.before(today) && current.after(yesterday)){

            return "昨天";
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
            String dateString = formatter.format(date);
            return dateString;
        }

    }

    /**
     * 将长时间格式时间转换为字符串 00:00
     *
     * @param longDate
     * @return
     */
    public static String longToFormateTime(long longDate) throws Exception{
        Date date = new Date(longDate);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * 时间转换分：秒 ---- 00:00:00
     */
    public static String formatTime(int timeMs) {

        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        try {
//            int totalSeconds = timeMs;
            int sec = timeMs % 60;
            timeMs = timeMs / 60;
            int min = timeMs % 60;
            timeMs = timeMs / 60;

            formatBuilder.setLength(0);

            return formatter.format("%02d:%02d:%02d", timeMs, min, sec).toString();
        } finally {
            formatter.close();
        }
    }


    public static boolean isPasswordRight(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return !(str.length() < 6 || str.length() > 18);
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public static void hideSoftInput(View view){
        InputMethodManager imm = ( InputMethodManager ) view.getContext( ).getSystemService(
                Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive() ) {
            imm.hideSoftInputFromWindow( view.getApplicationWindowToken( ) , 0 );

        }
    }




}
