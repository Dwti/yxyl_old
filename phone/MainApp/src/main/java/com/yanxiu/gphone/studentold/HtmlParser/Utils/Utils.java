package com.yanxiu.gphone.studentold.HtmlParser.Utils;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/2.
 */
public class Utils {

    public static final int M_AREA = 1 << 1;
    public static final int M_BLOCK = 1 << 2;
    public static final int M_BLOCKINLINE = 1 << 3;
    public static final int M_BODY = 1 << 4;
    public static final int M_CELL = 1 << 5;
    public static final int M_COL = 1 << 6;
    public static final int M_DEF = 1 << 7;
    public static final int M_FORM = 1 << 8;
    public static final int M_FRAME = 1 << 9;
    public static final int M_HEAD = 1 << 10;
    public static final int M_HTML = 1 << 11;
    public static final int M_INLINE = 1 << 12;
    public static final int M_LEGEND = 1 << 13;
    public static final int M_LI = 1 << 14;
    public static final int M_NOLINK = 1 << 15;
    public static final int M_OPTION = 1 << 16;
    public static final int M_OPTIONS = 1 << 17;
    public static final int M_P = 1 << 18;
    public static final int M_PARAM = 1 << 19;
    public static final int M_TABLE = 1 << 20;
    public static final int M_TABULAR = 1 << 21;
    public static final int M_TR = 1 << 22;

    private static final HashMap<String, Integer> sColorNameMap;
    private static final int BLACK = 0xFF000000;
    private static final int DKGRAY = 0xFF444444;
    private static final int GRAY = 0xFF888888;
    private static final int LTGRAY = 0xFFCCCCCC;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int RED = 0xFFFF0000;
    private static final int GREEN = 0xFF00FF00;
    private static final int BLUE = 0xFF0000FF;
    private static final int YELLOW = 0xFFFFFF00;
    private static final int CYAN = 0xFF00FFFF;
    private static final int MAGENTA = 0xFFFF00FF;
    private static final int TRANSPARENT = 0;

    static {
        sColorNameMap = new HashMap<String, Integer>();
        sColorNameMap.put("black", BLACK);
        sColorNameMap.put("darkgray", DKGRAY);
        sColorNameMap.put("gray", GRAY);
        sColorNameMap.put("lightgray", LTGRAY);
        sColorNameMap.put("white", WHITE);
        sColorNameMap.put("red", RED);
        sColorNameMap.put("green", GREEN);
        sColorNameMap.put("blue", BLUE);
        sColorNameMap.put("yellow", YELLOW);
        sColorNameMap.put("cyan", CYAN);
        sColorNameMap.put("magenta", MAGENTA);
        sColorNameMap.put("aqua", 0xFF00FFFF);
        sColorNameMap.put("fuchsia", 0xFFFF00FF);
        sColorNameMap.put("darkgrey", DKGRAY);
        sColorNameMap.put("grey", GRAY);
        sColorNameMap.put("lightgrey", LTGRAY);
        sColorNameMap.put("lime", 0xFF00FF00);
        sColorNameMap.put("maroon", 0xFF800000);
        sColorNameMap.put("navy", 0xFF000080);
        sColorNameMap.put("olive", 0xFF808000);
        sColorNameMap.put("purple", 0xFF800080);
        sColorNameMap.put("silver", 0xFFC0C0C0);
        sColorNameMap.put("teal", 0xFF008080);
    }

    public static int getHtmlColor(String color) {
        Integer i = sColorNameMap.get(color.toLowerCase(Locale.ROOT));
        if (i != null) {
            return i;
        } else {
            try {
                return Utils.convertValueToInt(color, -1);
            } catch (NumberFormatException nfe) {
                return -1;
            }
        }
    }

    public static final int convertValueToInt(CharSequence charSeq, int defaultValue) {
        if (null == charSeq) {
            return defaultValue;
        }

        String nm = charSeq.toString();

        int value;
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;

        if ('-' == nm.charAt(0)) {
            sign = -1;
            index++;
        }

        if ('0' == nm.charAt(index)) {
            if (index == (len - 1)) {
                return 0;
            }

            char c = nm.charAt(index + 1);

            if ('x' == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        } else if ('#' == nm.charAt(index)) {
            index++;
            base = 16;
        }
        return Integer.parseInt(nm.substring(index), base) * sign;
    }
}
