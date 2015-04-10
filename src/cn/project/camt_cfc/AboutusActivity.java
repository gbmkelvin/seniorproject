package cn.project.camt_cfc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
/**
 * 
 * @author Timothy Zhou
 *
 */
public class AboutusActivity extends Activity {
	private ImageView mBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
