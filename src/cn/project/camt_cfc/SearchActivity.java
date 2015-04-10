package cn.project.camt_cfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import cn.project.camt.runnable.RecipeSearchRunnable;

import com.sto.json.JSON;

/**
 * 
 * @author Timothy Zhou
 * 
 */
public class SearchActivity extends Activity {

	EditText editSearch;
	ListView listView;

	ProgressDialog mProgressDialog;
	static String sRNAME = "RecipeName";
	static String sRCATEGORY = "RecipeCategory";
	static String sRIMG = "RecipeImg";

	/**
	 * comment item
	 */

	public static String TEST_IMAGE = "/pic_fish_stew.jpg";

	/**
	 * Declare a button for submitting the searching keywords By stone
	 */
	private Button but_search;

	private Handler handler;

	private ImageView mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_listview_main);

		editSearch = (EditText) findViewById(R.id.editText1);
		listView = (ListView) findViewById(R.id.listView1);

		but_search = (Button) findViewById(R.id.but_search);

		initHandler();

		but_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String keywords = editSearch.getText().toString();
				new Thread(new RecipeSearchRunnable(handler, keywords)).start();
			}
		});

		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.arg1 == 1) {
					CharSequence recipes = msg.getData()
							.getCharSequence("data");
					JSON json = JSON.fromParse(recipes.toString());
					ListAdapter adapter = new RecipeSearchAdapter(
							SearchActivity.this, json);
					listView.setAdapter(adapter);
				} else {
					Toast.makeText(SearchActivity.this,
							"Can't get the result of your search!",
							Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

}
