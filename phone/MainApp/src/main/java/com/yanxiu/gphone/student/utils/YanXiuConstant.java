package com.yanxiu.gphone.student.utils;

import android.os.Environment;
import android.util.DisplayMetrics;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;

public class YanXiuConstant {

    public final static String ROOT_DIR = "/YanxiuStudent/";

    //分享的ICON的父目录
    public final static String SHARE_ICON_PATH =
            Environment.getExternalStorageDirectory() + YanXiuConstant.ROOT_DIR
                    + "image/";
    //分享ICON的文件名
    public final static String SHARE_LOGO_NAME = "share_logo.png";
    /**
     * 整个应用程序运行过程中用到的数据
     */
    public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + ROOT_DIR;//路径

    public static final String DEVICEID = Util.generateDeviceId(
            YanxiuApplication.getInstance());

    public static final String BRAND = CommonCoreUtil.getBrandName();

    public static final String OS = "android";

    public static final String OS_TYPE = "0";

    public static final String PCODE = CommonCoreUtil.getPcode();

    public static final String OPERTYPE = "app.upload.log";

    public static final int PRODUCTLINE = 1;

    /**
     * 0正常还原1代表切换小题
     */
    public static int OnClick_TYPE = 0;
    public static int index_position = 0;
    public static int catch_position = 0;

    public static final String VERSION = CommonCoreUtil.getClientVersionName(
            YanxiuApplication.getInstance());

    public static final int VERSION_CODE = CommonCoreUtil.getClientVersionCode(
            YanxiuApplication.getInstance());

    public static final String OS_VERSION = CommonCoreUtil.getOSVersionCode();


    public static final DisplayMetrics displayMetrics = YanxiuApplication.getInstance().getResources().getDisplayMetrics();


    public static final int YX_PAGESIZE_CONSTANT = 10;


    public static final int SERVER_0 = 0;

    public static final int SERVER_2 = 2;
    public static final int SERVER_3 = 3;
    public static final int SERVER_4 = 4;


    public static class SUBJECT {
        public final static int YUWEN = 1102;
        public final static int SHUXUE = 1103;
        public final static int YINYU = 1104;
        public final static int WULI = 1105;
        public final static int HUAXUE = 1106;
        public final static int SHENGWU = 1107;
        public final static int DILI = 1108;
        public final static int ZHENGZHI = 1109;
        public final static int LISHI = 1110;
        public final static int KEXUE = 1114;
        public final static int XXJS = 1115;
    }

    public static class STAGE {
        public final static int PRIM = 1202;
        public final static int JUIN = 1203;
        public final static int HIGH = 1204;
    }

    public interface Gender {
        int GENDER_TYPE_MALE = 2;
        int GENDER_TYPE_FEMALE = 1;
        int GENDER_TYPE_UNKNOWN = 0;
    }


    public enum QUESTION_TYP {
        QUESTION_SINGLE_CHOICES(1, YanxiuApplication.getInstance().getResources().getString(R.string.question_choice_single)),
        QUESTION_MULTI_CHOICES(2, YanxiuApplication.getInstance().getResources().getString(R.string.question_choice_multi)),
        QUESTION_JUDGE(4, YanxiuApplication.getInstance().getResources().getString(R.string.question_judge)),
        QUESTION_FILL_BLANKS(3, YanxiuApplication.getInstance().getResources().getString(R.string.question_fill_blanks)),
        QUESTION_READING(5, YanxiuApplication.getInstance().getResources().getString(R.string.question_reading)),
        QUESTION_SUBJECTIVE(6, YanxiuApplication.getInstance().getResources().getString(R.string.question_subjective)),
        QUESTION_CONNECT(7, YanxiuApplication.getInstance().getResources().getString(R.string.question_connect)),
        QUESTION_COMPUTE(8, "计算题"),
        QUESTION_LISTEN_COMPLEX(9, YanxiuApplication.getInstance().getResources().getString(R.string.question_listen_complex)),
        QUESTION_CLASSFY(13, YanxiuApplication.getInstance().getResources().getString(R.string.question_classfy)),
        QUESTION_CLOZE_COMPLEX(15, YanxiuApplication.getInstance().getResources().getString(R.string.question_cloze_complex)),
        QUESTION_READ_COMPLEX(14, YanxiuApplication.getInstance().getResources().getString(R.string.question_read_complex)),
        QUESTION_SOLVE_COMPLEX(22, YanxiuApplication.getInstance().getResources().getString(R.string.question_solve_complex));

        public int type;
        public String name;

        QUESTION_TYP(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    public final static String SINGLE_CHOICES = "choice";//单选
    public final static String MULTI_CHOICES = "multi-choice";//多选
    public final static String FILL_BLANK = "fill";//填空
    public final static String NEW_FILL_BLANK = "newfill";//新填空
    public final static String JUDGE_QUESTION = "alter";//判断
    public final static String CONNECT_QUESTION = "connect";//连线
    public final static String CLASSIFY_QUESTION = "classify";//归类
    public final static String ANSWER_QUESTION = "answer";//主观(问答题)
    public final static String MULTI_QUESTION = "multi";//复合
    public final static String CLOZE_QUESTION = "cloze";//完形填空
    public final static String LISTEN_QUESTION = "listen";//听力


    //作业报告截止时间
    public static final int END_TIME = -1;
    //作业报告
    public static final int HOMEWORK_REPORT = 1;
    //智能出题报告
    public static final int INTELLI_REPORT = 0;
    //智能出题报告
    public static final int WRONG_REPORT = 6;
    // 试题类型 0 表示智能练习， 1表示群组作业 2 //3表示知识点出题
    public static final int KPN_REPORT = 3;

    public static final int EXAM_REPORT = 2;
    //联系历史
    public static final int HISTORY_REPORT = 4;
    //三级考点
    public static final int THIRD_EXAMPOINT = 5;

    //作业报告
    public static final int HOMEWORK_WRONG = 6;

    //	public static final String PRIVACY_POLICY_URL="http://www.yanxiu.com/common/agreement.html";
    public static final String PRIVACY_POLICY_URL = "http://mobile.hwk.yanxiu.com/privacy_policy.html";

    //我的资料个人头像保存
    public static final String HEAD_IMAGE_SAVE_DIR = YanXiuConstant.SDCARD_ROOT_PATH + "bitMapSave/";

    public static final String HEAD_IMAGE_FILENAME = "bitMapClip.jpeg";

    //统计
    //事件：eventID
    /*
     0:注册成功
     1:每次启动
     2:提交练习/作业
     3:收到作业(每份作业统计一次)
     4:完成作业
     5:进入练习
     6:进入后台
     7:进入前台
     8:退出app
     9:加入班级成功
    */
    public static final String eventID = "eventID";
    //用户id：uid
    public static final String uid = "uid";

    public static final String appkey = "appkey";
    //时间戳：timestamp
    public static final String timestamp = "timestamp";
    //来源：source（0，移动端，1，页面）
    public static final String source = "source";
    //客户端类型：client（0，iOS，1，android）
    public static final String clientType = "clientType";
    //手机型号
    public static final String mobileModel = "mobileModel";
    //手机品牌
    public static final String brand = "brand";
    //操作系统
    public static final String system = "system";
    //屏幕分辨率
    public static final String resolution = "resolution";
    //网络类型 0：WIFI；2：4G；3：3G；4：2G，5:其它。
    public static final String netModel = "netModel";

    public static final String ip = "ip";

    public static final String url = "url";

    public static final String resID = "resID";

    public static final String qID = "questionID";

    public static final String reserved = "reserved";

    public static final String content = "content";
    //教材版本：editionID
    public static final String editionID = "editionID";
    //年级：gradeID
    public static final String gradeID = "gradeID";
    //班级：gradeID
    public static final String classID = "classID";
    //学科：subjectID
    public static final String subjectID = "subjectID";
    //试卷类型：paperType（0，练习，1，作业）
    public static final String paperType = "paperType";
    //题目数量：quesNum
    public static final String quesNum = "quesNum";

    public static final String yxyl_statistic = "yxyl_statistic";

    public final static int LAUNCHER_FROM_MISTAKE = 0x12;

    public static final String IMG_SRC = "<img src=";

    public static int updata=0;
}
