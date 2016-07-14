package com.yanxiu.basecore.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.basecore.task.base.YanxiuBaseTaskImpl;

/**
 * 网络请求的异步任务
 */
public abstract class YanxiuHttpAsyncTask<T extends YanxiuBaseBean> extends YanxiuBaseTaskImpl implements YanxiuHttpAsyncTaskInterface<T> {

    protected Context context;

    private Handler handler;

    private boolean isLocalSucceed = false;

    private String message;

    private int errcode;

    private YanxiuDataHull<T> hull = null;

    public YanxiuHttpAsyncTask(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public final boolean run() {
        try {
            if (!isCancel) {// 加载本地数据
                final T t = loadLocalData();
                if (t != null) {
                    isLocalSucceed = true;
                    postUI(new Runnable() {
                        @Override
                        public void run() {
                            isLocalSucceed = loadLocalDataComplete(t);
                        }
                    });

                }
            }
            if (!hasNet()) {// 判断网络
                cancel();
                postUI(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLocalSucceed) {
                            netNull();
                        }
                    }
                });
                return true;
            }
            if (!isCancel) {// 加载网络数据
                final YanxiuDataHull<T> dataHull = doInBackground();
                if (!isCancel) {
                    postUI(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!isCancel && dataHull == null) {
                                    if (!isCancel && !isLocalSucceed) {
                                        netErr(0, null);
                                    }
                                } else {
                                    message = dataHull.getMessage();
                                    errcode = dataHull.getErrMsg();
                                    int dataType = dataHull.getDataType();
                                    if (!isCancel && errcode == ErrorCode.TOKEN_INVALIDATE) {
                                        tokenInvalidate(message);
                                    } else {
                                        if (!isCancel && dataType == YanxiuDataHull.DataType.DATA_IS_INTEGRITY) {
                                            onPostExecute(dataType, dataHull.getDataEntity());
                                        } else if ((!isCancel && dataType == YanxiuDataHull.DataType.DATA_PARSE_EXCEPTION) ||
                                                (!isCancel && dataType == YanxiuDataHull.DataType.DATA_CAN_NOT_PARSE)) {
                                            if (!isCancel && !isLocalSucceed) {
                                                dataNull(dataType, message);
                                            }
                                        } else {
                                            if (!isCancel && !isLocalSucceed) {
                                                netErr(dataType, message);
                                            }
                                        }
                                    }
                                }
                                cancel();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            postUI(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!isCancel) {
                            netErr(0, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } finally {

        }

        return true;
    }

    @Override
    public boolean onPreExecute() {
        return true;
    }

    private void postUI(Runnable runnable) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public String getMessage() {
        return message;
    }

    public int getErrCode() {
        return errcode;
    }

    public final void start() {
        isCancel = !onPreExecute();
        if (isCancel) {
            postUI(new Runnable() {
                @Override
                public void run() {
                    preFail();
                }
            });
        }
        mThreadPool.addNewTask(this);// 加入线程队列，等待执行
    }

    /**
     * 请求前，准备失败回调
     */
    public void preFail() {
    }

    /**
     * 没有网络，回调
     */
    public void netNull() {
    }

    /**
     * 网络异常和数据错误，回调
     */
    public void netErr(int dataTypeId, String errMsg) {
    }

    /**
     * 数据为空，回调
     */
    public void dataNull(int dataTypeId, String errMsg) {
    }

    /**
     * 数据无更新，回调
     */
    public void noUpdate() {
    }

    /**
     * token过期
     */
    public abstract void tokenInvalidate(String msg);

    /**
     * 加载本地内容
     */
    public T loadLocalData() {
        return null;
    }

    /**
     * 加载本地内容完成后，回调
     */
    public boolean loadLocalDataComplete(T t) {
        return false;
    }

    /**
     * 本地数据是否加载成功
     */
    public boolean isLocalSucceed() {
        return isLocalSucceed;
    }

    /**
     * 是否有网络
     */
    public boolean hasNet() {
        if (context == null) {
            return false;
        }

        NetworkInfo activeNetInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return (activeNetInfo != null && activeNetInfo.isAvailable());
    }
}
