package cn.project.tencent.qq;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.project.camt_cfc.HomeActivity;
import cn.project.camt_cfc.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.tencent.qzone.QZone;

public class LoginActivity extends Activity implements Callback,
		OnClickListener, PlatformActionListener {
	private static final int MSG_AUTH_CANCEL = 1;
	private static final int MSG_AUTH_ERROR = 2;
	private static final int MSG_AUTH_COMPLETE = 3;
	private static final int MSG_USERID_FOUND = 4;
	// back to pre
	private ImageView mBack;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);

		setContentView(R.layout.third_party_login_page);

		findViewById(R.id.tvQq).setOnClickListener(this);

		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						HomeActivity.class);
				startActivity(intent);
				
			}
		});

	}

	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvQq: {
			authorize(new QZone(this));
		}
			break;
		}
	}

	private void authorize(Platform plat) {
		if (plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (userId != null) {
				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				login(plat.getName(), userId, null);
				return;
			} else {
				Log.d("message", userId);
			}
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(false);
		plat.showUser(null);
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
			login(platform.getName(), platform.getDb().getUserId(), res);
		}
		// System.out.println(res);
	}

	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
		}
		t.printStackTrace();
	}

	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
		}
	}

	private void login(String plat, String userId,
			HashMap<String, Object> userInfo) {
		Message msg = new Message();

		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {

		case MSG_AUTH_CANCEL: {
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case MSG_AUTH_ERROR: {
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case MSG_AUTH_COMPLETE: {
			Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		}
		Intent intent = new Intent(LoginActivity.this, AuthMainActivity.class);
		startActivity(intent);
		return false;
	}

}
