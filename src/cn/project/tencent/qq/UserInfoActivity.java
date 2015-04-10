package cn.project.tencent.qq;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.project.camt.json.JsonUtils;
import cn.project.camt_cfc.HomeActivity;
import cn.project.camt_cfc.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;

public class UserInfoActivity extends Activity implements OnClickListener,
		PlatformActionListener, Callback {
	private String data;
	String nameString;
	String imageString;
	private TextView userNameTextView;
	private ImageView mUserImage;
	Bitmap bmImg = null;
	private Button logoutBtn;
	private ImageView mBack;
	@SuppressWarnings("unused")
	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_userinfo);

		// userNameTextView = (TextView)findViewById(R.id.tvJson);
		// data = getIntent().getStringExtra("data");
		//
		// userNameTextView.setText(data);
		// System.out.println("tupian zhi" + imageString);

		new loadViewForUser().execute();

		initWidget();
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserInfoActivity.this,
						HomeActivity.class);
				startActivity(intent);
			}
		});
	}

	// figureurl_qq_1

	public class loadViewForUser extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				data = getIntent().getStringExtra("data");
				JsonUtils jsonUtils = new JsonUtils();
				HashMap<String, Object> jsonObejct = jsonUtils.fromJson(data);
				nameString = (String) jsonObejct.get("nickname");
				imageString = (String) jsonObejct.get("figureurl_qq_1");

				URL url = new URL(imageString);
				try {
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream iStream = conn.getInputStream();
					bmImg = BitmapFactory.decodeStream(iStream);
				} catch (IOException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String args) {

			userNameTextView = (TextView) findViewById(R.id.tvJson);
			mUserImage = (ImageView) findViewById(R.id.ivJson);

			userNameTextView.setText(nameString);
			mUserImage.setImageBitmap(bmImg);
		}

	}

	private void initWidget() {

		logoutBtn = (Button) findViewById(R.id.main_btn_logout);

		logoutBtn.setOnClickListener(this);
		handler = new Handler(this);
	}

	public void onClick(View v) {

		if (v == logoutBtn) {// logout
			Platform qzone = ShareSDK.getPlatform(this, QZone.NAME);
			if (qzone.isValid()) {
				qzone.removeAccount();
				Intent intent = new Intent(UserInfoActivity.this,
						HomeActivity.class);
				startActivity(intent);
				
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub

	}
}
