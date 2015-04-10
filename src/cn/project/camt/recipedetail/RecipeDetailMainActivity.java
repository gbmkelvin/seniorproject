package cn.project.camt.recipedetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.project.camt.runnable.RecipeDetailsRunnable;
import cn.project.camt_cfc.HomeActivity;
import cn.project.camt_cfc.R;
import cn.project.camt_cfc.SettingActivity;
import cn.project.tencent.qq.LoginActivity;

import com.sto.json.JSON;

public class RecipeDetailMainActivity extends Activity {
	// Declare Variables
	ListView listview;

	private ImageView mBack;

	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from listview_main.xml
		setContentView(R.layout.recipe_listview_main);

		// Get bottom bar
		bottombar();

		listview = (ListView) findViewById(R.id.rd_lv);
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);

		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg != null && msg.arg1 == 1) {
					String data = null;
					try {
						data = msg.getData().getString("data");

					} catch (Exception e) {
						// TODO: handle exception
						Log.e("error, can't get the recipes", e.getMessage());
					}

					// System.out.println(data);

					if (data != null) {
						JSON json = JSON.fromParse(data);
						ListAdapter adapter = new RecipeDetailsAdapter(
								RecipeDetailMainActivity.this, json);
						listview.setAdapter(adapter);
					} else {
						Toast.makeText(RecipeDetailMainActivity.this,
								"Don't find any recipes!", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(RecipeDetailMainActivity.this,
							"Can't load the recipes, Please check networks!",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		new Thread(new RecipeDetailsRunnable(handler)).start();
	}

	public void bottombar() {
		// menu bottom bar

		ImageButton img_btt_home = (ImageButton) findViewById(R.id.image_btt_home);

		img_btt_home.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), HomeActivity.class);
				startActivity(i);
			}
		});

		ImageButton img_btt_info = (ImageButton) findViewById(R.id.image_btt_info);

		img_btt_info.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent i = new Intent(v.getContext(), LoginActivity.class);
				startActivity(i);
			}
		});

		ImageButton img_btt_setting = (ImageButton) findViewById(R.id.image_btt_setting);

		img_btt_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), SettingActivity.class);
				startActivity(i);
			}
		});
	}
}