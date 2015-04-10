package cn.project.camt.runnable;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.project.camt.json.NetUtils;
import cn.project.camt.utils.URLUtil;

public class ReicipeIngredientRunnable implements Runnable{
	private Handler handler;
	private String keynum;
	
	public ReicipeIngredientRunnable(Handler handler,String keyword){
		this.handler = handler;
		this.keynum = keyword;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (handler == null) {
			Log.e("Recipe","handler is null");
			return;
		}
		Message msg = new Message();
		String pathString = URLUtil.Network.RECIPEINGREDINETJSON_URL;
		
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("keywords", keynum);
		
		String result = NetUtils.request(pathString,null,NetUtils.HTTP_REQUEST_METHOD_GET);
		
		if (result!=null) {
			msg.arg1 =1;
			Bundle bundle = new Bundle();
			bundle.putCharSequence("data", result);
			msg.setData(bundle);
			handler.sendMessage(msg);
		}else {
			msg.arg1=0;
			handler.sendMessage(msg);
		}
		
		
	}

}
