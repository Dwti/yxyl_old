package com.yanxiu.gphone.parent.utils;

import android.content.Context;

import com.common.core.utils.ContextProvider;

/**
 * Created by lee on 16-3-29.
 */
public class ParentPreferenceManager {
    private final static String PARENT_COMMON_PRE="parent_common_pre";
    private final int PRIVATE_MODE= Context.MODE_PRIVATE;

    private Context context;
    public static ParentPreferenceManager getInstance(){
        return ShareManagerHolder.sShareManager;
    }

    private ParentPreferenceManager(Context context) {
        this.context = context;
    }
    private static class ShareManagerHolder {
        private static final ParentPreferenceManager sShareManager=new ParentPreferenceManager(ContextProvider.getApplicationContext());
    }


}
