package com.common.core.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.common.core.CoreConfiguration;
import com.common.core.constants.CommonConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class CommonCoreUtil {

    private static final String TAG = CommonCoreUtil.class.getSimpleName();
    public static long lastClickTime = 0;

    public static long currentClickTime = 0;
    public static boolean checkClickEvent() {
        return checkClickEvent(500);
    }

    public static boolean checkScrollEvent() {
        return checkClickEvent(1000);
    }

    public static boolean checkClickEvent(long interval) {
        currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime > interval) {
            lastClickTime = currentClickTime;
            LogInfo.log(TAG,"lastClickTime: "+lastClickTime);
            return true;
        } else {
            lastClickTime = currentClickTime;
            LogInfo.log(TAG,"lastClickTime: "+lastClickTime);
            return false;
        }
    }
//    private final static Map<String, String> relation = getDataRelationMap(YanxiuApplication.getInstance(), R.array.intelli_exe_arrays);
    /**
     * appdeviceid:deviceid+macaddress
     *
     * @param context
     * @return
     */
    @Deprecated
    public static String getAppDeviceId(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) ContextProvider.getApplicationContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        WifiManager wifiManager = (WifiManager)ContextProvider.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            deviceId = deviceId + macAddress;
        }

        if (deviceId == null || "".equals(deviceId.trim())) {
            deviceId = "-";
        }

        return MD5Helper(deviceId);
    }

    public static String MD5Helper(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("no device Id");
    }


    //获取rom厂商
    public static String getRomFacturer() {

        return "" + Build.MANUFACTURER;
    }


    /**
     * 获取客户端版本号
     *
     * @param context
     * @return
     */
    public static int getClientVersionCode(Context context) {
        if (context == null) {
            return 0;
        }
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int getSDK() {
        return Build.VERSION.SDK_INT;
    }
    private static int width=0;

    public static int getWidth(){
        return width;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void getDisplayInfomation(Activity aa) {
        try {
            Point point = new Point();
            aa.getWindowManager().getDefaultDisplay().getRealSize(point);
            if (point.x<=480){
                width=180;
            }else if (point.x>480&&point.x<=640){
                width=250;
            }else {
                width=-1;
            }
        }catch (Exception e){
        }
    }

    /**
     * 得到客户端版本名
     */
    public static String getClientVersionName(Context context) {
        try {
            PackageInfo packInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取osversionname
     */
    public static String getOSVersionName() {
        return Build.VERSION.RELEASE;
    }

    public static String getOSVersionCode() {
        int version = -1;
        try {
            version = Integer.valueOf(Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(version);
    }


    /**
     * 获取厂商品牌
     */
    public static String getBrandName() {
        String brand = Build.BRAND;
        if (brand == null || brand.length() <= 0) {
            return "";
        } else {
            return getData(brand);
        }
    }

    /**
     * 获取modelname
     *
     * @return
     */
    public static String getModelName() {
        String model = Build.MODEL;
        if (model == null || model.length() <= 0) {
            return "";
        } else {
            return getData(model);
        }
    }

    public static String getData(String data) {
        if (data == null || data.length() <= 0) {
            return "-";
        } else {
            return data.replace(" ", "_");
        }
    }


    /**
     * 获取cpu核数
     */
    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }


    /**
     * 时间转换时：分：秒
     */
    public static String stringForTime(long timeMs) {

        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        try {
            int totalSeconds = (int) (timeMs / 1000);

            int seconds = totalSeconds % 60;
            int minutes = (totalSeconds / 60) % 60;
            int hours = totalSeconds / 3600;

            formatBuilder.setLength(0);

            return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } finally {
            formatter.close();
        }
    }


    /**
     * 时间转换分：秒
     */
    public static String stringForTimeNoHour(long timeMs) {

        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        try {
            int totalSeconds = (int) (timeMs / 1000);

            int seconds = totalSeconds % 60;
            int minutes = totalSeconds / 60;

            formatBuilder.setLength(0);

            return formatter.format("%02d:%02d", minutes, seconds).toString();
        } finally {
            formatter.close();
        }
    }
    // 格式：年－月－日 小时：分钟：秒
    public static final String FORMAT_ONE = "yyyy-MM-dd";

    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr
     * @return
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = new Date(dateStr);
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            d = new Date(System.currentTimeMillis());
        }
        return d;
    }
    public static String getTimeLongYMD(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// ���ø�ʽ
        Date mDate = null;
        String ymd;
        try {
            long time = Long.parseLong(date);
            mDate = new Date(time);
            ymd = format.format(mDate);
        } catch (Exception e) {
            mDate = new Date();
            ymd = format.format(mDate);
        }
        return ymd;
    }


    public static long getTimeLong(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");// ���ø�ʽ

        Date mDate;
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            mDate = new Date();
        }

        return mDate.getTime();
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String getNowHMDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public static Date getDateByString(String time) {
        Date date = null;
        if(time == null) return date;
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        try{
            date = format.parse(time);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateByFormatString(String mDate_format){
        String dateString=null;
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat(mDate_format);
        dateString = format.format(currentTime);
        return dateString;
    }

    /** * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar calendar = Calendar.getInstance();
        if(date != null){
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekOfDays[w];
    }

    public static String getShortTime(String time) {
        String shortstring = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if(date == null) return shortstring;
        long deltime = (now - date.getTime())/1000;
        if(deltime > 365*24*60*60) {
            shortstring = (int)(deltime/(365*24*60*60)) + "年前";
        } else if(deltime > 24*60*60) {
            shortstring = (int)(deltime/(24*60*60)) + "天前";
        } else if(deltime > 60*60) {
            shortstring = (int)(deltime/(60*60)) + "小时前";
        } else if(deltime > 60) {
            shortstring = (int)(deltime/(60)) + "分前";
        } else if(deltime > 1) {
            shortstring = deltime + "秒前";
        } else{
            shortstring = "1秒前";
        }
        return shortstring;
    }
    /**
     * 判断网络是否可用
     */
    public static boolean isNetAvailableForPlay(final Context context) {

        NetworkInfo networkInfo = null;
        try {
            networkInfo = NetWorkTypeUtils.getAvailableNetWorkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return networkInfo != null;

    }

    /**
     * 验证邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean emailFormats(String email) {
        if (email == null)
            return false;
        String regular = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 校验验证码是数字或者字母
     */
    public static boolean verFormat(String ver) {
        if (ver == null)
            return false;
        String regular = "^[a-zA-Z0-9_]{4}$";
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(ver);
        return matcher.matches();
    }

    /**
     * 验证密码是否是 数字和字母，英文符号 长度6-16,区分大小写
     */
    public static boolean passwordFormat(String password) {
        if (password == null)
            return false;

        String regular = "^[a-zA-Z0-9!`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]{6,16}$";
        //String regular = "^[,\\.;\\:\"\'!a-zA-Z0-9_]{6,16}$";
        //String regular = "/^[\w.]{6,16}$/";
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 验证注册手机号码是否正确
     */
    public static boolean isMobileNO(String mobiles) {
        if (mobiles == null) {
            return false;
        }
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断sdcard是否存在
     *
     * @return
     */
    public static boolean sdCardMounted() {
        final String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * 获取分辨率
     *
     * @return
     */
    public static String getResolution() {
        DisplayMetrics dm = ContextProvider.getApplicationContext().getResources().getDisplayMetrics();
        return new StringBuilder().append(dm.widthPixels).append("*").append(dm.heightPixels).toString();
    }

    /**
     * 获取密度
     *
     * @return
     */
    public static String getDensity() {
        DisplayMetrics dm = ContextProvider.getApplicationContext().getResources().getDisplayMetrics();
        return String.valueOf(dm.density);
    }

    /**
     * 得到屏幕宽度
     */    public static int getScreenWidth() {
        return ((WindowManager) ContextProvider.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();
    }

    /**
     * 得到屏幕高度
     */
    public static int getScreenHeight() {
        return ((WindowManager) ContextProvider.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight();
    }


	   /**  
	      得到等分的值
     *
     * @param resParams
     * @param divideParams
     * @return
     */
    public static int getDividePx(int resParams,int divideParams){
        if(divideParams==0){
            throw new IllegalArgumentException("divideParams cannot be zero");
        }
        return resParams/divideParams;
    }


    /**
     * 获取imei号
     *
     * @return
     */
    public static String getIMEI() {
        try {
            String deviceId = ((TelephonyManager)ContextProvider.getApplicationContext().getSystemService(
                    Context.TELEPHONY_SERVICE)).getDeviceId();
            if (null == deviceId || deviceId.length() <= 0) {
                return "";
            } else {
                return deviceId.replace(" ", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }





    /**
     * 将一倍尺寸缩放到当前屏幕大小的尺寸（宽）
     */
    public static int zoomWidth(int w) {
        int sw = 0;
        sw = Math.min(getScreenWidth(), getScreenHeight());

        return Math.round(w * sw / 320f + 0.5f);
    }

    /**
     * 将一倍尺寸缩放到当前屏幕大小的尺寸（高）
     */
    public static int zoomHeight(int h) {
        int sh = 0;
        sh = getScreenHeight();

        return (int) (h * sh / 480f + 0.5f);
    }

    /**
     * 缩放控件
     */
    public static void zoomView(int w, int h, View view) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null) {
            return;
        }

        params.width = zoomWidth(w);
        params.height = zoomWidth(h);
    }

    /**
     * 缩放控件
     */
    public static void zoomViewHeight(int h, View view) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null) {
            return;
        }

        params.height = zoomWidth(h);
    }

    /**
     * 缩放控件
     */
    public static void zoomViewWidth(int w, View view) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null) {
            return;
        }

        params.width = zoomWidth(w);
    }

    /**
     * 获取pcode
     *
     * @return
     */
    public static String getPcode() {
        return CoreConfiguration.getPcode();
    }

    /**
     * 缩放控件
     */
    public static void zoomViewFull(View view) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();

        if (params == null) {
            return;
        }

        params.width = getScreenWidth();
        params.height = getScreenHeight();
    }

    /**
     * dip转px
     */
    public static int dipToPx(int dipValue) {
        final float scale = CommonConstants.displayMetrics.density;
        int pxValue = (int) (dipValue * scale + 0.5f);

        return pxValue;
    }
    public static int px2sp(float pxValue) {
        final float fontScale = CommonConstants.displayMetrics.scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = CommonConstants.displayMetrics.density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转dip
     */
    public static float dipToPxFloat(int dipValue) {
        final float scale = CommonConstants.displayMetrics.density;
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

    /**
     * 根据资源ID得到View
     */
    public static View inflate(Context context, int resource, ViewGroup root, boolean attachToRoot) {
        return LayoutInflater.from(context).inflate(resource, root, attachToRoot);
    }

    public static void setDrawable(TextView view) {
        Drawable[] drawable = view.getCompoundDrawables();
        drawable[0].setBounds(0, 0, 0, 0);
        view.setCompoundDrawables(null, null, null, null);
    }

    public static void setDrawable(Context context,TextView view,int resId){
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable,null,null,null);
    }

    /**
     * 验证是否是手机号的格式
     */
    public static boolean isMobileNo(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证是否是身份证号格式
     */
    public static boolean isCard(String str) {
        String reg15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String reg18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        boolean flag = false;
        try {
            Pattern p;
            if (str.length() == 15) {
                p = Pattern.compile(reg15);
            } else {
                p = Pattern.compile(reg18);
            }
            Matcher m = p.matcher(str);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 是否是邮箱格式
     */
    public static boolean isEmail(String str) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            Matcher m = p.matcher(str);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 用户名是否合法
     */
    public static boolean isAccountVali(String str) {
        boolean flag = false;
        flag = isMobileNO(str);
        if (!flag) {
            flag = isCard(str);
            if (!flag) {
                flag = isEmail(str);
            }
        }
        return flag;
    }

    public static boolean isPasswordRight(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return !(str.length() < 6 || str.length() > 18);
    }

    /**
     * 是否是4位纯数字
     */
    public static boolean is4Num(String str) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^\\d{4}$");
            Matcher m = p.matcher(str);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 学习码是否合法
     * 15位纯数字
     */
    public static boolean isStudyCode(String str) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^\\d{15}$");
            Matcher m = p.matcher(str);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    public static Bitmap getImage(String imagePath) {
        Bitmap bitmap;
        int maxH = 400;//
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        bitmap = BitmapFactory.decodeFile(imagePath, options); //此时返回bm为空
        //计算缩放比
        int be = (int) (options.outHeight / (float) maxH);
        int ys = options.outHeight % maxH;//求余数
        float fe = ys / (float) maxH;
        if (fe >= 0.5) be = be + 1;
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;

        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        return bitmap;
    }

    public static Bitmap getImage(final byte[] data) {
        Bitmap bitmap;
        int maxH = 400;//
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        bitmap = BitmapFactory.decodeByteArray(data, 0,
                data.length, options);
        //计算缩放比
        int be = (int) (options.outHeight / (float) maxH);
        int ys = options.outHeight % maxH;//求余数
        float fe = ys / (float) maxH;
        if (fe >= 0.5) be = be + 1;
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;

        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(data, 0,
                data.length, options);
        return bitmap;
    }

    public static Bitmap getImage(Uri uri) {
        Bitmap bitmap;
        int maxH = 400;//
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        bitmap = BitmapFactory.decodeFile(uri.getPath(), options); //此时返回bm为空
        //计算缩放比
        int be = (int) (options.outHeight / (float) maxH);
        int ys = options.outHeight % maxH;//求余数
        float fe = ys / (float) maxH;
        if (fe >= 0.5) be = be + 1;
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;

        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(uri.getPath(), options);
        return bitmap;
    }
    public static String fileToType(File file){
        String path = file.getAbsolutePath();
        String type = null;
        if(path.lastIndexOf('.') != -1){
            type = path.substring(path.lastIndexOf('.'), path.length());
        }
        if(null == type){
            return "";
        }else{
            return type.replace(".", "").toLowerCase();
        }
    }

    public static String fileToType(String fileName){
        String type = null;
        if(fileName.lastIndexOf('.') != -1){
            type = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        }
        if(null == type){
            return "";
        }else{
            return type.replace(".", "").toLowerCase();
        }
    }
    /**
     * 将fileName转换成VideoTitle
     */
    public static String pathToTitle(File file) {
        if (null != file) {
            return pathToTitle(file.getName());
        } else {
            return "";
        }
    }
    /**
     * 将fileName转换成VideoTitle
     */
    public static String pathToTitle(String fileName) {
        String tmp = null;
        if (null != fileName && !"".equals(fileName)) {
            int index = fileName.lastIndexOf(".");
            if(index>0){
                tmp = fileName.substring(0, index);
            }else{
                tmp = fileName;
            }
        } else {
            tmp = "";
        }
        LogInfo.log("pathToTitle", "pathToTitle = " + tmp);
        return tmp;
    }

    /**
     * 从文件路径截取文件名
     * */
    public static String getFileName(String pathandname){
        LogInfo.log("king", "filePath = " + pathandname);
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }
    }

    /**
     * 获取文件大小
     * */
    public static String getFileSize(String filePath) {
        FileChannel fc= null;
        try {
            File f= new File(filePath);
            if (f.exists() && f.isFile()){
                FileInputStream fis= new FileInputStream(f);
                fc= fis.getChannel();
                return decodeBit(fc.size());
            }else{
                LogInfo.log("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            LogInfo.log(e.toString());
        } catch (IOException e) {
            LogInfo.log(e.toString());
        } finally {
            if (null!=fc){
                try{
                    fc.close();
                }catch(IOException e){
                    LogInfo.log(e.toString());
                }
            }
        }
        return "0";
    }

    /**
     * 转换字节
     * */
    public static String decodeBit(long size){
        long B = size / 8;
        if(B<1024){
            return B+"B";
        }else{
            long KB = B / 1024;
            if(KB <1024){
                return KB+"KB";
            }else{
                long M = KB / 1024;
                if(M<1024){
                    return M+"M";
                }else {
                    return "1G";
                }
            }
        }
    }

    public static String getDateFormat(int time){
        if(time<10){
            return "0"+time;
        }
        return time+"";
    }

    public static boolean isDateError(int year,int month,int day,int hour,int minute){
        Calendar calendar= Calendar.getInstance();
        if(year<calendar.get(Calendar.YEAR)){
            return false;
        }else if(year>calendar.get(Calendar.YEAR)){
            return true;
        }
        if(month<calendar.get(Calendar.MONTH)){
            return false;
        }else if(month>calendar.get(Calendar.MONTH)){
            return true;
        }
        if(day<calendar.get(Calendar.DAY_OF_MONTH)){
            return false;
        }else if(day>calendar.get(Calendar.DAY_OF_MONTH)){
            return true;
        }
        if(hour<calendar.get(Calendar.HOUR_OF_DAY)){
            return false;
        }else if(hour>calendar.get(Calendar.HOUR_OF_DAY)){
            return true;
        }
        if(minute<=calendar.get(Calendar.MINUTE)){
            return false;
        }else if(minute>calendar.get(Calendar.MINUTE)){
            return true;
        }
        return true;
    }

    public static String seccondToHour(long second){
        if(second<=0){
            return "过期";
        }
        if(second > 30000000){  //当值大于30000000时，将近于10年时，判断为无截止日期
            return "无截止日期";
        }
        long hour = second / 3600;
        int day ;
        if(hour>24){
            day = (int)hour / 24;
            return day+"天"+(hour%24)+"小时";
        }
        if(hour == 0){
            long minute = second / 60;
            if(minute == 0){
                return "过期";
            }
            return minute +"分钟";
        }
        return hour+"小时";
    }

    /**
     * yanxiu ua
     * @param context
     * @return
     */
    public static String createUA(Context context){
        String ua = "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) " + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
        ua += " " + "YanxiuMobileClient_" + getClientVersionCode(context) + "_android";
        return ua;
    }

    public static void hideSoftInput(View view){
        if(view==null){
            return;
        }
        InputMethodManager imm = ( InputMethodManager ) view.getContext( ).getSystemService(
                Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive() ) {
            imm.hideSoftInputFromWindow( view.getApplicationWindowToken( ) , 0 );
        }
    }



    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if(a.size() != b.size())
            return false;

        for (int i = 0; i < a.size(); i++) {

            T aaa = a.get(i);
            T bbb = b.get(i);
            if (a.get(i).compareTo(b.get(i)) != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 拷贝对象
     * @param <T>
     * @param <T>
     * @return
     */
    public static <T,P>T copyBean(P p, Class<T> clazz){
        if(p == null)
            return null;
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (Exception e1) {
            // do nothind
            e1.printStackTrace();
        }
        Field[] fields = null;
        try{
            fields = p.getClass().getDeclaredFields();
        }catch(Exception e){
            e.printStackTrace();
        }
        for(int i = 0; i < fields.length; i++){
            try {
                fields[i].setAccessible(true);
                String fieldName = fields[i].getName();
                Object fieldValue = fields[i].get(p);
                try{
                    Field filed = clazz.getDeclaredField(fieldName);
                    filed.setAccessible(true);
                    filed.set(t, fieldValue);
                }catch(Exception e){
                    // do nothing
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    public static boolean checkBrowser(Context context, String packageName) {

        boolean isInstalled = false;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }


    /**
     *
     * @param context
     * @param data  为xml中的数组
     * @return
     */
    public static Map<String, String> getDataRelationMap(Context context, int data) {
        String[] stringArray = context.getResources().getStringArray(data);

        Map<String, String> relation = new HashMap<String, String>();
        for (int i = 0; i < stringArray.length; i++) {
            relation.put(stringArray[i].substring(0, stringArray[i].indexOf("_")),
                    stringArray[i].substring(stringArray[i].indexOf("_") + 1));
        }
        return relation;

    }

    public static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    public static void measureView(View childView) {
        ViewGroup.LayoutParams p = childView.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int height = p.height;
        int childHeightSpec;
        if (height > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(height,
                    View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        childView.measure(childWidthSpec, childHeightSpec);
    }




    /**
     * Calling
     */
     public static void callPhone(Context context,String phoneNum){
         if(context==null){
             return;
         }
         if(StringUtils.isEmpty(phoneNum)){
             return;
         }

         Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNum));
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         context. startActivity(intent);


     }

    /**
     * 对View设置不同状态时其文字颜色。
     */
    public static ColorStateList createColorStateList (int normal, int pressed) {
        //colors里面存放的事颜色值而不是颜色id,如果是自定义颜色需要使用getResources().getColor(R.color.white)
        int[] colors = new int[]{pressed, pressed, normal, pressed, normal};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_focused, android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }


    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        Resources resources = ContextProvider.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        LogInfo.log(TAG, "Status height:" + height);
        return height;
    }

    /**
     * 获取底部虚拟导航键高度
     * @return
     */
    public static int getNavigationBarHeight() {
        Resources resources = ContextProvider.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        LogInfo.log(TAG, "Navi height:" + height);
        return height;
    }

    /**
     * 设置字体
     */
    public static void setViewTypeface(String typePath, TextView
            ...view){
        Typeface tf = Typeface.createFromAsset(ContextProvider.getApplicationContext().getAssets(),
                typePath);
        for(int i=0; i<view.length; i++) {
            view[i].setTypeface(tf);
        }
    }

}

