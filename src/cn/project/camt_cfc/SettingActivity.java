package cn.project.camt_cfc;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
/**
 * 
 * @author Timothy Zhou
 *
 */
public class SettingActivity extends Activity {
	private ImageView mBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingpage);
		
		ShareSDK.initSDK(this);
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ListView list_view1;
		final String page_name[] = { "About Us", " Logout" };
		list_view1 = (ListView) findViewById(R.id.listView1);
		ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, page_name);
		list_view1.setAdapter(array_adapter);
		list_view1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int num,
					long arg3) {
				Toast.makeText(getBaseContext(), page_name[num],
						Toast.LENGTH_LONG).show();
				switch (num + 1) {
			
				case 1:
					Intent aboutus = new Intent(getBaseContext(),
							AboutusActivity.class);
					startActivity(aboutus);
					break;
				case 2:
					Platform qzone = ShareSDK.getPlatform(QZone.NAME);
					if (qzone.isValid()) {
						qzone.removeAccount();
						Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
						startActivity(intent);	
					}
					break;
				}
			}
		});                                                         

	}

}