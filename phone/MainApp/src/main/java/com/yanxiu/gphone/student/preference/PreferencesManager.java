package com.yanxiu.gphone.student.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * preference manager
 * @author 
 *
 */

@SuppressLint("NewApi")
public class PreferencesManager {
	private Context context;

	/**
	 * 是否是第一次登陆
	 */
    private static final String FRISTAPP = "fristapp";
	private static final String SETTINGS = "settings";
	private static final String LAST_REFRESH_TIME= "last_refresh_time";
	private static final String CRASH_COUNT = "crash_count";
	private static final String NATION_TRAIN_W = "nation_train_w";
	/**
	 * 用户登录信息
	 */
	private static final String LOGIN_INFO = "login_info";

	private static final String SUBJECT_SECTION = "SUBJECT_SECTION";

	/**
	 * 是否第一次答题
	 */
	private static final String FRIST_QUESTION = "fris_qustion";

	/**
	 * Crash
	 * @param context
	 */

	private PreferencesManager(Context context) {
		this.context = context;
	}

	private static PreferencesManager instance = new PreferencesManager(YanxiuApplication.getInstance());

	public static PreferencesManager getInstance() {
		return instance;
	}
	//崩溃次数
	public void setCrashCount(int count) {
		SharedPreferences preferences = context.getSharedPreferences(CRASH_COUNT, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("crash", count);
		editor.apply();
	}

	public int getCrashCount() {
		SharedPreferences preferences = context.getSharedPreferences(CRASH_COUNT, Context.MODE_PRIVATE);
		return preferences.getInt("crash", 0);
	}
	public void setFristApp(boolean flag){
		SharedPreferences sp = context.getSharedPreferences(FRISTAPP,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean("frist", flag).apply();
	}  
	
	public boolean getFristApp(){
		SharedPreferences sp = context.getSharedPreferences(FRISTAPP, Context.MODE_PRIVATE);
		return sp.getBoolean("frist", true);
	}

	/**
	 * 获取设备唯一标识
	 * @return
	 */
	public String getDeviceId() {
		SharedPreferences sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
		return sp.getString("yanxiu_deviceId", null);
	}

	/**
	 * 保存设备唯一标识
	 * @param deviceId
	 */
	public void setDeviceId(String deviceId) {
		SharedPreferences sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("yanxiu_deviceId", deviceId);
		editor.apply();
	}

	/**
	 *
	 * @param title:新式下来刷新样式的页面title，必须是唯一的
	 * @param time:本次下拉刷新的时间
	 */
	public void saveLastRefreshTime(String title, String time) {

		SharedPreferences preferences = context.getSharedPreferences(
				LAST_REFRESH_TIME, Context.MODE_PRIVATE);
		preferences.edit().putString(title, time).apply();
	}

	public String getLastRefreshTime(String title) {

		SharedPreferences preferences = context.getSharedPreferences(LAST_REFRESH_TIME, Context.MODE_PRIVATE);
		String temp =preferences.getString(title, "" + 1);
		return temp;
	}


	public void saveUserUid(int uid){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("uid", uid);
		editor.apply();
	}
	public int getUserUid() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO,
				Context.MODE_PRIVATE);
		return sp.getInt("uid", 1);
	}

	public String getToken() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("token", "");
	}

	public void setToken(String token){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("token", token);
		editor.apply();
	}

	public String getUname() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("uname", "unKnown");
	}

	public void setUname(String uname){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("uname", uname);
		editor.apply();
	}

	public String getRealName() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("realName", "unKnown");
	}

	public void setRealName(String realName){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("realName", realName);
		editor.apply();
	}



	public String getNickName() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("nickName", "unKnown");
	}

	public void setNickName(String nickName){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("nickName", nickName);
		editor.apply();
	}

	public String getHead() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("head", "xxxx");
	}

	public void setHead(String head){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("head", head);
		editor.apply();
	}
	public String getSchool() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("school", "unKnown");
	}

	public void setSchool(String school){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("school", school);
		editor.apply();
	}
	public String getMobile() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("mobile", "unKnown");
	}

	public void setMobile(String mobile){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("mobile", mobile);
		editor.apply();
	}

	public String getEmail() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("email", "unKnown");
	}

	public void setEmail(String email){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("email", email);
		editor.apply();
	}

	public String getPersonalld() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getString("personalld", "unKnown");
	}

	public void setPersonalld(String personalld){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("personalld", personalld);
		editor.apply();
	}


	public String getSubjectSection(String stageId, String subjectId, String editionId){
		SharedPreferences sp = context.getSharedPreferences(SUBJECT_SECTION, Context.MODE_PRIVATE);
		return sp.getString(stageId+subjectId+editionId, "");
	}

	public void setSubjectSection(String stageId, String subjectId, String editionId, String value){
		SharedPreferences sp = context.getSharedPreferences(SUBJECT_SECTION, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(stageId+subjectId+editionId, value);
		editor.apply();
	}

	public void clearSubjectSection(){
		SharedPreferences sp = context.getSharedPreferences(SUBJECT_SECTION, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.apply();
	}



	public boolean getFirstQuestion() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getBoolean("first_question", true);
	}

	public void setFirstQuestion(){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("first_question", false);
		editor.apply();
	}


	public boolean getFirstTestCenter() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getBoolean("first_test_center", true);
	}

	public void setFirstTestCenter(){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("first_test_center", false);
		editor.apply();
	}

	public boolean getIsThirdLogIn() {
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		return sp.getBoolean("IsThirdLogIn", false);
	}

	public void setIsThirdLogIn(boolean flag){
		SharedPreferences sp = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("IsThirdLogIn", flag);
		editor.apply();
	}

}
