package com.yanxiu.gphone.parent.contants;

/**
 * Created by lee on 16-3-21.
 */
public class YanxiuParentConstants {
    private static final String PATH_TYPEFACE = "fonts/";
    public final static String ROOT_DIR = "/YanxiuStudent/";
    //分享ICON的文件名
    private final static String SHARE_LOGO_NAME = "share_logo.png";
    /**
     * 整个应用程序运行过程中用到的数据
     */
    public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ROOT_DIR;//路径

    //分享的ICON的父目录
    public final static String SHARE_ICON_PATH =SDCARD_ROOT_PATH + "image/";

    public static final String PRIVACY_POLICY_URL="http://www.yanxiu.com/common/agreement.html";

    //我的资料个人头像保存
    public static final String HEAD_IMAGE_SAVE_DIR = YanxiuParentConstants.SDCARD_ROOT_PATH +"bitMapSave/";

    public static final String HEAD_IMAGE_FILENAME="bitMapClip.jpeg";
    public static final String METRO_STYLE=YanxiuParentConstants.PATH_TYPEFACE + "MetroLightPlayType.otf";

    public static class SUBJECT{
        public final static int YUWEN = 1102;
        public final static int SHUXUE = 1103;
        public final static int YINGYU = 1104;
        public final static int WULI= 1105;
        public final static int HUAXUE = 1106;
        public final static int SHENGWU = 1107;
        public final static int DILI = 1108;
        public final static int ZHENGZHI = 1109;
        public final static int LISHI = 1110;
        public final static int EXERCISE = 1111;
        public final static int HOMEWORK = 1112;
    }

    public static class LOGIN_FROM{
        public final static int FROM_REGISTER_FROM=0;
        public final static int FROM_FORGET_FROM=1;
    }



    public static final int FROM_SETPSD=0;

     //已经绑定孩子信息
    public static  final int HASBIND=1;
    //没有绑定孩子信息
    public static final  int NOTBIND=0;



    public static final String MAIN_APP_PACKAGE_NAME="com.yanxiu.gphone.student";
    public static final String MAIN_APP_CLASSNAME="com.yanxiu.gphone.student.activity.LoginChoiceActivity";


    //分享图片相关

    public static final int HONOR_TYPE_ONE=1;
    public static final int HONOR_TYPE_TWO=2;
    public static final int HONOR_TYPE_THREE=3;
    public static final int HONOT_TYPE_FOUR=4;

    public static final String XUANLIANGCIFUICON="xuanliangcigu.jpg";
    public static final String LIANZHANLIANJIEICON="lianzhanlianjie.jpg";
    public static final String WEIKUAIBUPOICON="weikuaibupo.jpg";
    public static final String ZHULUZHONGYUANICON="zhuluzhongyuan.jpg";
    //悬梁刺骨图片地址
    public static final String XUANLIANGCIGU_ICON_PATH=SHARE_ICON_PATH+XUANLIANGCIFUICON;
    //连战连捷图片地址
    public static final String LIANZHANLIANJIE_ICON_PATH=SHARE_ICON_PATH+LIANZHANLIANJIEICON;
    //唯快不破图片地址
    public static final String WEIKUAIBUPO_ICON_PATH=SHARE_ICON_PATH+WEIKUAIBUPOICON;
    //逐鹿中原图片地址
    public static final String ZHULUZHONGYUAN_ICON_PATH=SHARE_ICON_PATH+ZHULUZHONGYUANICON;
    //默认图片地址
    public static final String DEFAULT_ICON_PATH=SHARE_ICON_PATH+SHARE_LOGO_NAME;
}
