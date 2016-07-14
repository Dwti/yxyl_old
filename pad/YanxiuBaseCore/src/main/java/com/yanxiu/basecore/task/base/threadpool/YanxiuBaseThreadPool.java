package com.yanxiu.basecore.task.base.threadpool;

import com.yanxiu.basecore.task.base.YanxiuBaseTask;

import java.util.HashSet;
import java.util.LinkedList;

public class YanxiuBaseThreadPool {

	/**
	 * 任务队列
	 */
	private LinkedList<YanxiuBaseTask> mTaskQueue = null;

	/**
	 * 执行中的任务
	 * */
	private HashSet<YanxiuBaseTask> runningSet = null;

	/**
	 * 线程单元
	 */
	private ThreadUnit[] mThreadUnits;

	/**
	 * 线程池设置
	 * */
	private ThreadPoolOptions options;

	/**
	 * 线程池是否加锁
	 * */
	private boolean lock ;

	/**
	 * 构造方法
	 * */
	public YanxiuBaseThreadPool(ThreadPoolOptions options) {
		if (options == null) {
			throw new NullPointerException("ThreadPoolOptions is null");
		}
		this.options = options;

		mTaskQueue = new LinkedList<YanxiuBaseTask>() ;
		runningSet = new HashSet<YanxiuBaseTask>() ;

		mThreadUnits = new ThreadUnit[options.getSize()];
		for (int i = 0; i < options.getSize(); i++) {
			mThreadUnits[i] = new ThreadUnit();
			Thread thread = new Thread();
			thread = new Thread(mThreadUnits[i]);
			thread.setPriority(options.getPriority());

			thread.start();
		}
	}

	/**
	 * 添加新任务
	 */
	public boolean addNewTask(YanxiuBaseTask task) {
		synchronized (mTaskQueue) {
			boolean isSuccess = mTaskQueue.offer(task);
			if (isSuccess && !lock) {
				for(ThreadUnit threadUnit : mThreadUnits){
					if(threadUnit.isWait){
						mTaskQueue.notifyAll();
						break ;
					}
				}
			}
			return isSuccess;
		}
	}
	
	/**
	 * 移除任务
	 */
	public boolean removeTask(YanxiuBaseTask task) {
		synchronized (mTaskQueue) {
			boolean isSuccess = mTaskQueue.remove(task);
			return isSuccess;
		}
	}

	/**
	 * 销毁线程池
	 */
	public synchronized void destroyThreadPool() {
		for (int i = 0; i < options.getSize(); i++) {
			mThreadUnits[i].isRunning = false;
		}
		mTaskQueue.clear();
	}
	
	/**
	 * 停止执行任务队列
	 * */
	public void lock() {
		lock = true;
	}

	/**
	 * 解锁，开始执行任务
	 * */
	public void unlock() {
		lock = false;
		synchronized (mTaskQueue) {
			mTaskQueue.notifyAll();
		}
	}
	
	class ThreadUnit implements Runnable {

		public boolean isRunning = false;
		private YanxiuBaseTask task = null;
		public boolean isWait = true ;

		@Override
		public void run() {
			isRunning = true;
			while (isRunning) {
				isWait = true ;
				synchronized (mTaskQueue) {
					/**
					 * 线程池中无任务时每隔一段时间检测是否有新任务被添加
					 */
					while (mTaskQueue.isEmpty() || lock) {
						try {
							mTaskQueue.wait(options.getWaitPeriod());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					isWait = false ;
					/**
					 * 移除任务
					 */
					task = mTaskQueue.removeLast();
				}
				if (task == null || task.isCancelled()) {
					continue ;
				}
				synchronized (runningSet) {
					runningSet.add(task);
				}
				/**
				 * 执行移除的任务
				 */
				try {
					boolean isSucceed = task.run();
					synchronized (runningSet) {
						runningSet.remove(task);
					}
					if (!isSucceed && options.isReplayFailTask()) {
						synchronized (mTaskQueue) {
							mTaskQueue.addFirst(task);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
