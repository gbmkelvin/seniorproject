package cn.project.camt.runnable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.project.camt.json.NetUtils;
import cn.project.camt.utils.URLUtil;

public class RecipeDetailsRunnable implements Runnable {
	private Handler handler;

	public RecipeDetailsRunnable(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (handler == null) {
			Log.e("recipe", "handler is null");
			return;
		}
		Message msg = new Message();

		String path = URLUtil.Network.RECIPEDETAILJSON_URL;

		String result = NetUtils.request(path, null,
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
