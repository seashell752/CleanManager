package com.cleanmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * Author: Ruils 心怀产品梦的安卓码农 
 * Blog: http://blog.csdn.net/ruils
 * QQ: 5452781
 * Email: 5452781@qq.com
 */

public class AnimationActivity extends Activity {
	private static final String TAG = "AnimationActivity";

	private static final int MESSAGE_ROTATE_FINISHED = 0;
	private static final int MESSAGE_UPDATE_WIDTH = 1;

	private RelativeLayout mShortcut;
	private RelativeLayout mRelativeLayout;
	private Rect rect;

	private ImageView backImageView;
	private ImageView roateImageView;
	private TextView textView;

	private int mWidth;

	private static enum Direction {
		RIGHT, LEFT
	}

	private Direction direction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);

		Intent intent = getIntent();
		if (intent == null) {
			finish();
			return;
		}

		// 取得Lanucher传过来的所点击的快捷方式的矩形坐标。
		rect = intent.getSourceBounds();
		if (rect == null) {
			finish();
			return;
		}

		Log.d(TAG, rect.toShortString());

		mRelativeLayout = (RelativeLayout) findViewById(R.id.framelayout);
		mShortcut = (RelativeLayout) findViewById(R.id.shortcut);

		backImageView = (ImageView) findViewById(R.id.clean_back);
		roateImageView = (ImageView) findViewById(R.id.clean_rotate);
		// iconmageView = (ImageView) findViewById(R.id.clean_icon);
		textView = (TextView) findViewById(R.id.text);

		// DisplayMetrics dm = new DisplayMetrics();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int hight = getWindowManager().getDefaultDisplay().getHeight();

		Log.d(TAG, "width = " + width);
		Log.d(TAG, "hight = " + hight);

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mShortcut
				.getLayoutParams();
		layoutParams.topMargin = rect.top - (rect.bottom - rect.top) / 4;

		// 判断快捷方式在屏幕的哪一边，如果在左边，伸缩动画就会向右，如果在右边，伸缩动画向左。
		if (rect.left < width / 2) {
			direction = Direction.RIGHT;
			layoutParams.leftMargin = rect.left;

		} else {
			direction = Direction.LEFT;
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			layoutParams.rightMargin = width - rect.right;
			Log.d(TAG, "rightMargin = " + (width - rect.right));
		}

		mRelativeLayout.updateViewLayout(mShortcut, layoutParams);
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_ROTATE_FINISHED:
				mWidth = backImageView.getWidth();
				Log.d(TAG, "mWidth = " + mWidth);
				updateWidth();
				roateImageView.clearAnimation();
				roateImageView.setVisibility(View.INVISIBLE);
				break;
			case MESSAGE_UPDATE_WIDTH:
				updateWidth();
				break;

			default:
				break;
			}

		};
	};

	private void updateWidth() {
		// 宽度没有达到原来宽度的2.5度，继续做动画
		if (backImageView.getWidth() <= 2.5f * mWidth) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) backImageView
					.getLayoutParams();
			// 每次增加20的宽度，可以自行设置，和用户体验有关系，可自行调整
			layoutParams.width = backImageView.getWidth() + 20;
			mShortcut.updateViewLayout(backImageView, layoutParams);
			// 继续发更新消息。也可发送delay消息，和用户体验有关系，可自行调整
			mHandler.sendEmptyMessage(MESSAGE_UPDATE_WIDTH);
		} else {
			textView.setVisibility(View.VISIBLE);

		}

	};

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		// 旋转动画开始
		roateImageView.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.rotate_anim));

		// 假设垃圾清理了两秒钟，然后开如做伸缩动画。
		mHandler.sendEmptyMessageDelayed(MESSAGE_ROTATE_FINISHED, 2000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mRelativeLayout.setVisibility(View.GONE);
		finish();
	}

}
