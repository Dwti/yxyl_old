package com.common.share;

/**
 * Created by lee on 16-3-23.
 */
public enum ShareEnums {
    QQ(0),
    QQZONE(1),
    WEIXIN(2),
    WEIXIN_FRIENDS(3);
    public int type;
    ShareEnums(int type){
        this.type=type;
    }
}
