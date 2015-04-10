package cn.project.camt_cfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		new Handler().postDelayed(new Runnable(){   
			// 为了减少代码使用匿名Handler创建一个延时的调用
			            public void run() {   
			                Intent i = new Intent(SplashActivity.this, HomeActivity.class);    
			                //通过Intent打开最终真正的主界面Main这个Activity
			                SplashActivity.this.startActivity(i);    //启动Main界面
			                SplashActivity.this.finish();    //关闭自己这个开场屏
			            }   
			        }, 3000);   //5秒，够用了吧
	}

	

}
