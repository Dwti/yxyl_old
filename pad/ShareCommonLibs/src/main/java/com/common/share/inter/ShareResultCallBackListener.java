package com.common.share.inter;

import com.common.share.ShareExceptionEnums;

/**
 * Created by lee on 16-3-23.
 */
public interface ShareResultCallBackListener {
    public void notInstall();
    public void shareException(ShareExceptionEnums exceptionEnums);
    public void shareSuccess(Object o);
    public void shareFailrue(Object o);
}
