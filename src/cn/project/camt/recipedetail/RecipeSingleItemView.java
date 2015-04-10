package cn.project.camt.recipedetail;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.project.camt.cookingmethod.RecipeCookingMethodsMainActivity;
import cn.project.camt.json.NetUtils;
import cn.project.camt.runnable.RecipeDetailsRunnable;
import cn.project.camt.utils.ImageLoader;
import cn.project.camt.utils.URLUtil;
import cn.project.camt_cfc.R;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.CommentFilter;
import cn.sharesdk.socialization.CommentFilter.FilterItem;
import cn.sharesdk.socialization.QuickCommentBar;
import cn.sharesdk.socialization.Socialization;

@SuppressLint("DefaultLocale")
public class RecipeSingleItemView extends Activity implements Callback,
		OnClickListener {
	private ImageView image;
	private TextView id;
	private TextView name;
	private TextView category;
	private TextView descripte;

	private ImageView backHome;
	private ImageLoader imageLoader;

	private Handler handler;

	// Button
	private Button but_howtocook;


	/**
	 * comment item
	 */
	// Simulat topic ID
	private String topicId;
	// Simulat topic title
	private String topicTitle;
	// Simulat topic publish time
	private String topicPublishTime;
	// Simulat topic author
	private String topicAuthor;
	private OnekeyShare oks;
	private QuickCommentBar qcBar;
	private CommentFilter filter;
	public static String TEST_IMAGE = "/pic_fish_stew.jpg";

	public static String idName = "cfcs2014102800000";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_single_item_view);
		ShareSDK.initSDK(this);
		ShareSDK.registerService(Socialization.class);

		new Thread() {
			public void run() {
				initImagePath();
				UIHandler.sendEmptyMessageDelayed(1, 100,
						RecipeSingleItemView.this);
			}
		}.start();

		loadViews();

		// get intent parameters
		Intent request = getIntent();
		String rimg = request.getStringExtra("rimg");
		String rid = request.getStringExtra("rid");
		String rname = request.getStringExtra("rname");
		String rcategory = request.getStringExtra("rcategory");

		id.setText(rid);
		name.setText(rname);
		category.setText(rcategory);

		// load image
		String url = URLUtil.Network.RECIPEDETAILIMG_URL + rimg;
		imageLoader.DisplayImage(url, image);

		// load recipe detail
		new Thread(new RecipeDetailsRunnable(handler)).start();

		// load description
		new Thread(new DescriptRunnable()).start();

		// back to the previous activity
		backHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private void loadViews() {
		image = (ImageView) findViewById(R.id.rimgImg);
		id = (TextView) findViewById(R.id.ridtxt);
		name = (TextView) findViewById(R.id.rnametxt);
		category = (TextView) findViewById(R.id.categorytxt);
		descripte = (TextView) findViewById(R.id.rdestxt);

		backHome = (ImageView) findViewById(R.id.about_imageview_gohome);
		imageLoader = new ImageLoader(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg != null && msg.arg1 == 1) {
					Bundle data = msg.getData();
					descripte.setText("\u3000\u3000" + data.getString("data"));
				} else {
					Toast.makeText(RecipeSingleItemView.this,
							"Can't access the server, Check your networks!",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		but_howtocook = (Button) findViewById(R.id.howtocookbtt);
		

		// add listener
		ClickListener listener = new ClickListener();
		but_howtocook.setOnClickListener(listener);
	

	}

	public class DescriptRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String retval = null;
			try {
				String url = URLUtil.Network.DESCRIPTE_URL
						+ Integer.parseInt(id.getText().toString());
				retval = NetUtils.request(url, null,
						NetUtils.HTTP_REQUEST_METHOD_POST);

				// System.out.println(retval);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Message msg = new Message();
			if (retval == null) {
				msg.arg1 = 0;
			} else {
				msg.arg1 = 1;
				Bundle data = new Bundle();
				data.putString("data", retval);
				msg.setData(data);
			}

			handler.sendMessage(msg);
			System.out.println(msg);
		}

	}

	String rcmidString;
	Bundle nBundle ;
	Context context;
	public class ClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			// how to cook
			case R.id.howtocookbtt:
				context =v.getContext();
				Intent cook_intent = new Intent(context,
						RecipeCookingMethodsMainActivity.class);
				Bundle cook_bundle = new Bundle();
				nBundle = getIntent().getExtras();
				rcmidString = nBundle.getString("rid");
				cook_bundle.putString("rcmid", rcmidString);
				cook_intent.putExtras(cook_bundle);
				context.startActivity(cook_intent);
				break;
			default:
				break;

			}

		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		topicId = idName + id.getText().toString();
		topicTitle = getString(R.string.comment_like_title);
		initOnekeyShare();
		initQuickCommentBar();
		return false;
	}

	// Socialization service dependents on OnekeyShare this method initializes a
	// onekeyshare instance
	private void initOnekeyShare() {
		oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, "Stew Fish");

		oks.setTitle("Stew Fish");
		oks.setTitleUrl("http://sharesdk.cn");

		oks.setImagePath(TEST_IMAGE);

		oks.setComment(getString(R.string.share));
		oks.setSite("Call for cooking");

		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.disableSSOWhenAuthorize();
	}

	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				File baseFile = new File(
						Environment.getExternalStorageDirectory(), "share");
				if (!baseFile.exists()) {
					baseFile.mkdir();
				}
				TEST_IMAGE = baseFile.getAbsolutePath() + "/pic_fish_stew.jpg";
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
						+ "/pic_fish_stew.jpg";
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
						R.drawable.pic_fish_stew);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	private void initQuickCommentBar() {
		qcBar = (QuickCommentBar) findViewById(R.id.qcBar);
		qcBar.setTopic(topicId, topicTitle, topicPublishTime, topicAuthor);

		qcBar.setTextToShare(getString(R.string.share_content));
		qcBar.getBackButton().setOnClickListener(this);
		qcBar.setAuthedAccountChangeable(true);
		CommentFilter.Builder builder = new CommentFilter.Builder();
		// non-empty filter
		builder.append(new FilterItem() {
			public boolean onFilter(String comment) {
				if (TextUtils.isEmpty(comment)) {
					return true;
				} else if (comment.trim().length() <= 0) {
					return true;
				} else if (comment.trim().toLowerCase().equals("null")) {
					return true;
				}
				return false;
			}

			@Override
			public int getFilterCode() {
				return 0;
			}
		});
		// limit filter
		builder.append(new FilterItem() {
			// returns true if the comment passed in is spam
			public boolean onFilter(String comment) {
				if (comment != null) {
					String pureComment = comment.trim();
					String wordText = cn.sharesdk.framework.utils.R.toWordText(
							pureComment, 140);
					if (wordText.length() != pureComment.length()) {
						return true;
					}
				}
				return false;
			}

			@Override
			public int getFilterCode() {
				return 0;
			}
		});
		filter = builder.build();
		qcBar.setCommentFilter(filter);
		qcBar.setOnekeyShare(oks);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(qcBar.getBackButton())) {
			finish();
		}

	}

}
