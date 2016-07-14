package com.yanxiu.gphone.hd.student.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YanxiuLogApiTool {

	private final String PATH = Environment.getExternalStorageDirectory() + "/YanxiuStudent/exceptionInfo/";

	private File dir = new File(PATH);
	private File file = null;
	private File fileNull = null;

	private final int POOL_SIZE = 3;
	private int cpuNums = Runtime.getRuntime().availableProcessors();
	private ExecutorService executor = Executors.newFixedThreadPool(cpuNums * POOL_SIZE);

	private static YanxiuLogApiTool mLetvLogTool = null;

	public final String EXCEPTIONINFO_NAME = "exceptionInfo.log";
	public final String NULL_EXCEPTIONINFO_NAME = "nullInfo.log";

	public synchronized static YanxiuLogApiTool getInstance() {
		if (mLetvLogTool == null) {
			mLetvLogTool = new YanxiuLogApiTool();
		}
		return mLetvLogTool;
	}
    private double timeOutValue = 1.5;

    public double getTimeOutValue() {
        return timeOutValue;
    }

    public void setTimeOutValue(double timeOutValue) {
        this.timeOutValue = timeOutValue;
    }

    /**
	 * ����Ƿ�װ��sd��
	 *
	 * @return
	 */
	private static boolean sdCardMounted() {
		final String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	}

	/**
	 * ���ص�ǰʱ���ַ���
	 * @return
	 */
	private static String getTimeStamp() {
		// ���Զ����ʽSimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatTime.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		Date currentTime = new Date(System.currentTimeMillis());
		String tt = formatTime.format(currentTime);
		return tt ;
	}
	public static void createExceptionInfo(int  errorType, String errorDes, String requestUrl,  String responseData){
		YanxiuLogApiTool.getInstance().saveExceptionInfo(getTimeStamp() + " errorType = " + errorType+ "  requestUrl =" + requestUrl + errorDes + "\n\r  responseData=" + responseData);
	}

	public File getExceptionFile(){
		return new File(dir, EXCEPTIONINFO_NAME);
	}
	public File getNullExceptionFile(){
		if (!dir.exists()) {
			dir.mkdirs();
		}
		fileNull = new File(dir, NULL_EXCEPTIONINFO_NAME);
		try {
			if (fileNull.exists()) {
			} else {
				fileNull.createNewFile();
				executor.execute(new Handler("to be or not to be!", file));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileNull;
	}
	/**
	 * �洢�ļ�����
	 * @param data
	 */
	public void saveExceptionInfo(String data) {
		if (!sdCardMounted()) {
			return;
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		file = new File(dir, EXCEPTIONINFO_NAME);
		if (file.exists() && file.length() > 1*1024*1024) {
			file.delete();
		}
		try {
			file.createNewFile();
			executor.execute(new Handler(data, file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class Handler implements Runnable {
		private String data;
		private File file;
		public Handler(String data, File file) {
			this.data = data;
			this.file = file;
		}
		@Override
		public void run() {
			synchronized (executor) {
				BufferedReader stringReader = null;
				FileWriter fileWriter = null;
				try {
					stringReader = new BufferedReader(new StringReader(data));
					fileWriter = new FileWriter(file, true);
					String line = null;
					while ((line = stringReader.readLine()) != null) {
						fileWriter.write(line);
						fileWriter.write("\r\n");
					}
					stringReader.close();
					fileWriter.flush();
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						stringReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
