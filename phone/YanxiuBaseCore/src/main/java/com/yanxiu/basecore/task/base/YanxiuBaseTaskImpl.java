package com.yanxiu.basecore.task.base;


import com.yanxiu.basecore.task.base.threadpool.ThreadPoolOptions;
import com.yanxiu.basecore.task.base.threadpool.YanxiuBaseThreadPool;
import com.yanxiu.basecore.task.base.threadpool.YanxiuThreadPoolFactory;

public abstract class YanxiuBaseTaskImpl implements YanxiuBaseTask {

    protected boolean isCancel = false;
    /**
     * 线程池
     */
    protected static final YanxiuBaseThreadPool mThreadPool;

    static {// 初始化线程池
        ThreadPoolOptions options = new ThreadPoolOptions();
        options.setPriority(Thread.NORM_PRIORITY + 1);
        options.setSize(20);
        options.setWaitPeriod(1000);
        options.setReplayFailTask(false);
        mThreadPool = YanxiuThreadPoolFactory.create(options);
    }

    @Override
    public void cancel() {
        this.isCancel = true;
        if (mThreadPool != null) {
            mThreadPool.removeTask(this);
        }
    }

    @Override
    public boolean isCancelled() {
        return this.isCancel;
    }
}
