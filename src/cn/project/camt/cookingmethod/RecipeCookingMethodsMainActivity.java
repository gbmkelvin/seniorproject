package cn.project.camt.cookingmethod;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import cn.project.camt.json.JSONfunction;
import cn.project.camt.utils.URLUtil;
import cn.project.camt_cfc.R;

public class RecipeCookingMethodsMainActivity extends Activity {

	JSONObject jsonobject;
	JSONArray jsonarray;
	ListView listView;
	RecipeCookingMethodsListViewAdapter adapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> arraylist;
	static String CMID = "CookingMethodID";
	static String CMINSTRUCTION = "CookingMethodInstruction";
	static String CMIMG = "CookingMethodImg";
	String imgurl = URLUtil.Network.COOKINGMETHODIMG_URL;
	
	private ImageView mBack;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipecookingmethods_listview_main);

		if (isInternetConnected()) {
			new DownloadJSON().execute();
		} else {
			showAlertNoNet();
		}
		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(
					RecipeCookingMethodsMainActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Call for Cooking");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		JSONObject CookingMethodObject;
		JSONObject subobject;
		JSONArray array;

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			arraylist = new ArrayList<HashMap<String, String>>();
			// Retrive JSON Objects from the given website URL in
			// JSONfunctions.class

			jsonobject = JSONfunction
					.getJSONfromURL(URLUtil.Network.COOKINGMETHODJSON_URL);

			try {

				array = jsonobject.getJSONArray("result");

				for (int i = 0; i < array.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();

					subobject = array.getJSONObject(i);

					CookingMethodObject = subobject
							.getJSONObject("CookingMethod");// model name

					map.put("CookingMethodID",
							CookingMethodObject.getString("id"));// database 's param name
					map.put("CookingMethodInstruction",
							CookingMethodObject.getString("instruction"));

					String IMGURL = imgurl
							+ CookingMethodObject.getString("img_file");
					map.put("CookingMethodImg", IMGURL);
					// set the JSON Objects into the Array
					arraylist.add(map);
				}

			} catch (JSONException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			listView = (ListView) findViewById(R.id.listview);

			adapter = new RecipeCookingMethodsListViewAdapter(
					RecipeCookingMethodsMainActivity.this, arraylist);

			listView.setAdapter(adapter);

			mProgressDialog.dismiss();

		}
	}



	private boolean isInternetConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;

		}

		else {
			return false;
		}
	}

	private void showAlertNoNet() {
		AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
		alertDlg.setMessage("This Application require internet connection")
				.setTitle("Alert Message")
				.setIcon(R.drawable.ic_launcher)
				.setCancelable(false)
				.setPositiveButton("Setting internet connection",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dualog, int id) {
								Intent gpsOptionsIntent = new Intent(
										android.provider.Settings.ACTION_SETTINGS);
								startActivityForResult(gpsOptionsIntent, 0);
							}
						});
		alertDlg.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDlg.create();
		alert.show();

	}
}
