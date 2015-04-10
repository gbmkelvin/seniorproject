package cn.project.camt.cookingmethod;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.project.camt.utils.ImageLoader;
import cn.project.camt_cfc.R;

public class RecipeCookingMethodsListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;

	public RecipeCookingMethodsListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {

		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// System.out.println(data.size());
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return data.indexOf(data);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView cmid;
		TextView cminstruction;
		ImageView cmimg;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(
				R.layout.recipecookingmethods_listview_item, parent, false);
		// Get the position from the results
		HashMap<String, String> resultp = new HashMap<String, String>();
		resultp = data.get(position);
		// Locate the TextViews in listview_item.xml
		cmid = (TextView) itemView.findViewById(R.id.cmid);
		cminstruction = (TextView) itemView.findViewById(R.id.cminstruction);
		// Locate the ImageView in listview_item.xml
		cmimg = (ImageView) itemView.findViewById(R.id.cmimg);
		// Capture position and set results to the TextViews
		cmid.setText(resultp.get(RecipeCookingMethodsMainActivity.CMID));
		cminstruction.setText(resultp
				.get(RecipeCookingMethodsMainActivity.CMINSTRUCTION));
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class to download and cache
		// images
		imageLoader.DisplayImage(
				resultp.get(RecipeCookingMethodsMainActivity.CMIMG), cmimg);
		// Capture button clicks on ListView items
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get the position from the results
				HashMap<String, String> resultp = new HashMap<String, String>();
				resultp = data.get(position);
				// Send single item click data to CmViewPagerActivity Class
				Intent intent = new Intent(context, CmViewPagerActivity.class);
				// Bundle to get the data and send the data to
				// CmViewPagerActivity's bundle
				Bundle bundle = new Bundle();
				// get cmid by position
				bundle.putString("cmid",
						resultp.get(RecipeCookingMethodsMainActivity.CMID));
				// get cminstruction by position
				bundle.putString("cminstruction", resultp
						.get(RecipeCookingMethodsMainActivity.CMINSTRUCTION));
				// get the method's position
				bundle.putInt("position", position);
				// get the list of the methods
				bundle.putInt("lists", getCount());
				// intent the bundle
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		return itemView;
	}
}
