package com.yanxiu.basecore.task.base.threadpool;

/**
 * 线程池工厂
 * */
public class YanxiuThreadPoolFactory {
	
	/**
	 * 默认配置
	 * */
	private static final ThreadPoolOptions defaultOptions = new ThreadPoolOptions();
	
	static{
		defaultOptions.setPriority(Thread.NORM_PRIORITY);
		defaultOptions.setSize(10);
		defaultOptions.setWaitPeriod(100);
		defaultOptions.setReplayFailTask(false);
	}
	

	public static YanxiuBaseThreadPool create(ThreadPoolOptions options){
		return initialize(options);
	}
	
	private static YanxiuBaseThreadPool initialize(ThreadPoolOptions options){
		if(options == null){
			options = defaultOptions ;
		}
		YanxiuBaseThreadPool threadPool = new YanxiuBaseThreadPool(options);
		return threadPool ;
	}
}
