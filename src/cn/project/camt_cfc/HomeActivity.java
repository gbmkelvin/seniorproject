package cn.project.camt_cfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.project.camt.recipedetail.RecipeDetailMainActivity;
import cn.project.tencent.qq.LoginActivity;

/**
 * 
 * @author Timothy Zhou
 * 
 */
public class HomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);

		// btt_seeallrecipe
		Button btt_seeallrecipe = (Button) findViewById(R.id.btt_seeallrecipe);

		btt_seeallrecipe.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(),
						RecipeDetailMainActivity.class);
				startActivity(i);
			}
		});

		Button btt_ingredient = (Button) findViewById(R.id.btt_ingredient);
		btt_ingredient.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), SearchActivity.class);
				startActivity(i);
			}
		});

		// menu bottom bar

		ImageButton img_btt_home = (ImageButton) findViewById(R.id.image_btt_home);



		img_btt_home.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "This is Homepage",
						Toast.LENGTH_SHORT).show();
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
	

