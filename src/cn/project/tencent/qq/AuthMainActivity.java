package cn.project.tencent.qq;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import cn.project.camt.json.JsonUtils;
import cn.project.camt_cfc.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;

public class AuthMainActivity extends Activity implements
		PlatformActionListener, Callback {
	
		
	
	private Handler handler;

	private ImageView mBack;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authmain);
		ShareSDK.initSDK(this);
		initWidget();
	
		
		
		Platform qzone = ShareSDK.getPlatform(this, QZone.NAME);
		qzone.setPlatformActionListener(this);
		qzone.showUser(null);
		
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}




	private void initWidget() {
		handler = new Handler(this);
	}

	@Override
	public void onCancel(Platform weibo, int arg1) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}

	@Override
	public void onComplete(Platform weibo, int arg1,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		JsonUtils ju = new JsonUtils();
		String json = ju.fromHashMap(res);
		msg.obj = ju.format(json);
		handler.sendMessage(msg);
	}

	@Override
	public void onError(Platform weibo, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.arg1 = 2;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}


	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: { 
			Intent intent = new Intent(AuthMainActivity.this,
					UserInfoActivity.class);
			intent.putExtra("data", String.valueOf(msg.obj));
			startActivity(intent);
		}
			break;
		case 2: {
			Platform weibo = (Platform) msg.obj;
			Toast.makeText(this, weibo.getName() + "", Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case 3: {
			Platform weibo = (Platform) msg.obj;
			Toast.makeText(this, weibo.getName() + "", Toast.LENGTH_SHORT)
					.show();
		}
			break;
		}
		return false;
	}
	
	protected void onDestroy() {
		
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

}