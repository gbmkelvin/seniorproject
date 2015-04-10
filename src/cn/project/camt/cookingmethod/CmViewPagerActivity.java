package cn.project.camt.cookingmethod;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.project.camt.json.JsonParser;
import cn.project.camt_cfc.R;
import cn.project.camt_cfc.voice.IatSettings;
import cn.project.camt_cfc.voice.TtsSettings;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

@SuppressLint({ "SdCardPath", "ShowToast" })
public class CmViewPagerActivity extends FragmentActivity {
	/**
	 * declare the cooking method 's id & instruction
	 */
	private String cmId;
	private String cmInstruction;
	/**
	 * Viewpager & fragment
	 */
	private ViewPager mViewPager;
	private ArrayList<Fragment> fragmentsList;
	private SingleItemViewFragment mSingleItemViewFragment;

	/**
	 * Speech voice control
	 */
	private static String TAG = "CmViewPagerActivity";
	// the object of speech recognizer
	private SpeechRecognizer mSpeechRecognizer;
	// the object of speech synthesizer
	private SpeechSynthesizer mTtSpeechSynthesizer;
	// voicer
	private String voicer = "Catherine";

	// buffering
	private int mPercentForBuffering = 0;
	// playing
	private int mPercentForPlaying = 0;
	// Speech Engine
	private String mTypeEngineString = SpeechConstant.TYPE_CLOUD;

	Bundle nBundle;

	private String result = "";
	private Toast mToast;
	private SharedPreferences mSharedPreferencesIat;

	private SharedPreferences mSharedPreferencesTts;
	private ImageButton btn_listen;
	private ImageButton btn_wenhao;
	private int ret = 0;// 函数调用返回值

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.recipecookingmethods_single_item_view);
		initLayout();

		// initial the speech syn
		mTtSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this,
				mTtsInitListener);
		mSharedPreferencesTts = getSharedPreferences(TtsSettings.PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

	}

	/**
	 * initLayout
	 */
	private void initLayout() {
		// initial the recognizer object
		mSpeechRecognizer = SpeechRecognizer.createRecognizer(this,
				mInitListener);
		mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
		mSharedPreferencesIat = getSharedPreferences(IatSettings.PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mViewPager = (ViewPager) findViewById(R.id.cm_view_pager);
		// viewpager can show the data
		fragmentsList = new ArrayList<Fragment>();
		nBundle = getIntent().getExtras();
		int infoPosition = getIntent().getIntExtra("position", 0);
		int steps = getIntent().getIntExtra("lists", 0);
		// add the data
		for (int i = 0; i < steps; i++) {
			mSingleItemViewFragment = new SingleItemViewFragment();
			Bundle bundle = new Bundle();
			cmId = nBundle.getString("cmid");
			cmInstruction = nBundle.getString("cminstruction");
			bundle.putString("cmid", cmId);
			bundle.putString("cminstruction", cmInstruction);
			mSingleItemViewFragment.setArguments(bundle);// set the data
			fragmentsList.add(mSingleItemViewFragment);
		}
		mViewPager.setAdapter(new MyCookingMethodsFragmentAdapter(
				getSupportFragmentManager(), fragmentsList));

		mViewPager.setCurrentItem(infoPosition);

		btn_listen = (ImageButton) findViewById(R.id.btn_listen);
		btn_listen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setParam();
				ret = mSpeechRecognizer.startListening(recognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					showTip("Listening failed, code:" + ret);
				} else {
					showTip(getString(R.string.text_begin));
				}
			}
		});
		btn_wenhao = (ImageButton) findViewById(R.id.wenhao_btn);
		btn_wenhao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CmViewPagerActivity.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("Help Dialog")
						.setMessage(
								"voice commands manuals\n"
										+ "【commands】---【action】\n"
										+ "【Read】 ===> Start to read\n"
										+ "【Next】 ===> Go to next step\n"
										+ "【Back】 ===> Go to prior\n"
										+ "【Exit】 ===> Quit detail model\n")
						.setNegativeButton("I Go it", null).create().show();
			}
		});

	}

	private class MyCookingMethodsFragmentAdapter extends

	FragmentStatePagerAdapter {

		private List<Fragment> fragments;
		private List<String> stepList;
		private FragmentManager fm;

		public MyCookingMethodsFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyCookingMethodsFragmentAdapter(FragmentManager fm,
				List<Fragment> fragmentList) {
			super(fm);
			this.fm = fm;
			this.fragments = fragmentList;
		}

		// viewpager 中显示的内容
		@Override
		public Fragment getItem(int arg0) {
			return (fragments == null || fragments.size() == 0) ? null
					: fragments.get(arg0);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (stepList.size() > position) ? stepList.get(position) : "";
		}

		@Override
		public int getCount() {
			return fragments == null ? 0 : fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}

//		@SuppressWarnings("deprecation")
//		@Override
//		public Object instantiateItem(View container, int position) {
//			SingleItemViewFragment fViewFragment = (SingleItemViewFragment) super
//					.instantiateItem(container, position);
//			int pos = mViewPager.getCurrentItem();
//			fViewFragment.setTargetFragment(fViewFragment, pos);
//			return fViewFragment;
//
//		}

		/**
		 * every pager need the method to loading data
		 */
		// @Override
		// public void setPrimaryItem(ViewGroup container, int position,
		// Object object) {
		// // setPosition(position);
		// super.setPrimaryItem(container, position, object);
		// }

		// @Override
		// public void destroyItem(ViewGroup container, int position, Object
		// object) {

		// super.destroyItem(container, position, object);
		// }

	
		public void setFragmentList(List<Fragment> fragmentList) {
			if (this.fragments != null) {
				FragmentTransaction fTransaction = fm.beginTransaction();
				for (Fragment f : fragments) {
					fTransaction.remove(f);
				}
				fTransaction.commit();
				fTransaction = null;
				fm.executePendingTransactions();
			}
			this.fragments = fragmentList;
			notifyDataSetChanged();
		}
	}

	/**
	 * initial listener
	 */
	private InitListener mInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code=" + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("Initial failed, code ：" + code);
			}
		}
	};

	/**
	 * show Tips
	 * 
	 * @param string
	 */
	private void showTip(final String string) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(string);
				mToast.show();
			}
		});
	}

	/**
	 * Listener
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onVolumeChanged(int volume) {
			showTip("The volume is " + volume);

		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			result = JsonParser.parseIatResult(results.getResultString());
			int pos = mViewPager.getCurrentItem();
			if (result.contains("Read")) {
				showTip("read the Text");
				String readText = nBundle.getString("cminstruction");
				int code = mTtSpeechSynthesizer.startSpeaking(readText,
						mTtSynthesizerListener);
				if (code != ErrorCode.SUCCESS) {
					showTip("Syntheizer is failed, code :" + code);
				}
			} else if (result.contains("Next") && result.contains("Back")) {
				showTip("Please speak again!");
			} else if (result.contains("Next") && !result.contains("Back")) {

				if (pos + 1 < fragmentsList.size()) {
					pos++;
					mViewPager.setCurrentItem(pos);
				} else {
					showTip("Nothing more^_^!");
				}
			} else if (!result.contains("Next") && result.contains("Back")) {
				if (pos > 0) {
					pos--;
					mViewPager.setCurrentItem(pos);
				} else {
					showTip("Nothing more^_^!");
				}
			} else if (result.contains("Exit")) {
				showTip("Exit to the cooking method detail");
				finish();
			} else {
				showTip("what you want?");
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));

		}

		@Override
		public void onEndOfSpeech() {
			showTip("Please wait....");

		}

		@Override
		public void onBeginOfSpeech() {
			showTip("Speaking.....");

		}
	};

	/**
	 * initial Tts listener
	 * 
	 */
	private InitListener mTtsInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			// TODO Auto-generated method stub
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("Initial failed, the code: " + code);
			}
		}
	};

	/**
	 * synthesizer listener
	 */
	private SynthesizerListener mTtSynthesizerListener = new SynthesizerListener() {

		@Override
		public void onSpeakResumed() {
			showTip("Resumed....");

		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			mPercentForBuffering = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			mPercentForPlaying = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));

		}

		@Override
		public void onSpeakPaused() {
			showTip("Pused....");

		}

		@Override
		public void onSpeakBegin() {
			showTip("Reading...");

		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				showTip("finished");
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}

		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

		}
	};

	/**
	 * set the param
	 */
	public void setParam() {

		if (mTypeEngineString.equals(SpeechConstant.TYPE_CLOUD)) {
			mTtSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// 设置发音人
			mTtSpeechSynthesizer
					.setParameter(SpeechConstant.VOICE_NAME, voicer);
		} else {
			mTtSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// 设置发音人 voicer为空默认通过语音+界面指定发音人。
			mTtSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "");
		}

		// 设置语速
		mTtSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
				mSharedPreferencesTts.getString("speed_preference", "50"));

		// 设置音调
		mTtSpeechSynthesizer.setParameter(SpeechConstant.PITCH,
				mSharedPreferencesTts.getString("pitch_preference", "50"));

		// 设置音量
		mTtSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,
				mSharedPreferencesTts.getString("volume_preference", "50"));

		// 设置播放器音频流类型
		mTtSpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE,
				mSharedPreferencesTts.getString("stream_preference", "3"));
		// 设置语音前端点
		mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferencesIat
						.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点
		mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferencesIat
						.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferencesIat.getString("iat_punc_preference", "0"));
		// 设置音频保存路径
		mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				"/sdcard/iflytek/wavaudio.pcm");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		mSpeechRecognizer.cancel();
		mSpeechRecognizer.destroy();
	}

}
