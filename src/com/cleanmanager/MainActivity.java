package com.cleanmanager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

/*
 * Author: Ruils 心怀产品梦的安卓码农 
 * Blog: http://blog.csdn.net/ruils
 * QQ: 5452781
 * Email: 5452781@qq.com
 */

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Intent launchIntent = getIntent();
		final String action = launchIntent.getAction();

		Intent shortcutIntent = new Intent();
		//设置点击快捷方式时启动的Activity,因为是从Lanucher中启动，所以包名类名要写全。
		shortcutIntent.setComponent(new ComponentName(getPackageName(),
				getPackageName() + "."
						+ AnimationActivity.class.getSimpleName()));
		//设置启动的模式
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		Intent resultIntent = new Intent();
		//设置快捷方式图标
		resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this,
						R.drawable.shortcut_proc_clean));
		//启动的Intent
		resultIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		//设置快捷方式的名称
		resultIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		// 长按方式添加快捷方式----->即被动的Action方式。
		if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
			Log.d(TAG, "action " + action);
			setResult(RESULT_OK, resultIntent);
			finish();
		} else {
			// 发送广播方式添加快捷方式----->即主动的发广播方式
			Log.d(TAG, "sendBroadcast " + resultIntent);
			resultIntent.setAction(ACTION_INSTALL_SHORTCUT);
			sendBroadcast(resultIntent);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
