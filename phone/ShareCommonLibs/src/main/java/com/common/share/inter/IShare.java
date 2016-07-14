package com.common.share.inter;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by lee on 16-3-23.
 */
public interface IShare {
    void onShare(Context context,Bundle bundle,ShareResultCallBackListener shareResultCallBackListener);
    void clear();
}
