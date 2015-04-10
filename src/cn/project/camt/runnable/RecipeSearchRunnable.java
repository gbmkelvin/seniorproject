package cn.project.camt.runnable;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.project.camt.json.NetUtils;
import cn.project.camt.utils.URLUtil;

public class RecipeSearchRunnable implements Runnable {
	private Handler handler;
	private String keywords;

	public RecipeSearchRunnable(Handler handler, String keywords) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
		this.keywords = keywords;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (handler == null) {
			Log.e("RecipeSearch", "handler or keywords is null");
			return;
		}
		Message msg = new Message();

		String path =URLUtil.Network.SEARCH_PATH_URL;

		Map<String, Object> params = new HashMap<>();
		params.put("keywords", keywords);

		String result = NetUtils.request(path, params,
				NetUtils.HTTP_REQUEST_METHOD_GET);
		if (result != null) {
			msg.arg1 = 1;
			Bundle bundle = new Bundle();
			bundle.putCharSequence("data", result);
			msg.setData(bundle);
			handler.sendMessage(msg);
		} else {
			msg.arg1 = 0;
			handler.sendMessage(msg);
		}

	}

}
