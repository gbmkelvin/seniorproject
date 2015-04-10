package cn.project.camt_cfc.voice;

import android.app.Application;

import cn.project.camt_cfc.R;

import com.iflytek.cloud.SpeechUtility;

public class SpeechVoiceControlApp extends Application {
	@Override
	public void onCreate() {
		// 应用程序入口处调,避免手机内存过小，杀死后台进,造成SpeechUtility对象为null
		// 设置你申请的应用appid
		SpeechUtility.createUtility(SpeechVoiceControlApp.this, "appid="
				+ getString(R.string.app_id));
		super.onCreate();
	}
}
