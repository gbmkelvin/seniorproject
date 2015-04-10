package cn.project.camt_cfc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.project.camt.utils.ImageLoader;
import cn.project.camt.utils.URLUtil;

import com.sto.json.JSON;
/**
 * 
 * @author Timothy Zhou
 *
 */
public class RecipeSearchAdapter extends BaseAdapter {
	private Context context;
	private JSON json;

	private LayoutInflater listContainer;

	public final class ListItemView {
		public TextView name;
		public TextView category;
		public ImageView image;
	}

	private ImageLoader imageLoader;

	public RecipeSearchAdapter(Context context, JSON json) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.json = json;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (json == null || json.isEmpty())
			return 0;
		else
			return json.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (json == null || json.isEmpty())
			return null;
		final int selectID = position;
		ListItemView listItemView = null;

		if (convertView == null) {
			listItemView = new ListItemView();
			convertView = listContainer.inflate(R.layout.search_listview_item,
					null);
			listItemView.name = (TextView) convertView
					.findViewById(R.id.srname);
			listItemView.category = (TextView) convertView
					.findViewById(R.id.srcategory);
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.srimg);

			convertView.setTag(listItemView);

		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		JSON j = (JSON) json.get(position);

		if (j == null || j.isEmpty())
			return null;

		listItemView.name.setText(j.get("recipe_name").toString());
		listItemView.category.setText(j.get("recipe_category").toString());

		String url = URLUtil.Network.RECIPEDETAILIMG_URL
				+ j.get("img_file").toString();

		imageLoader.DisplayImage(url, listItemView.image);

		if (convertView != null) {
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(context, selectID,
					// Toast.LENGTH_SHORT).show();
					JSON d = (JSON) json.get(selectID);
					System.out.println(d.toString());
					Intent intent = new Intent(context,
							SearchSingleItemView.class);
					intent.putExtra("rid", d.get("id").toString());
					intent.putExtra("rname", d.get("recipe_name").toString());
					intent.putExtra("rcategory", d.get("recipe_category")
							.toString());
					intent.putExtra("rimg", d.get("img_file").toString());

					context.startActivity(intent);

				}
			});
		}

		return convertView;
	}

}
